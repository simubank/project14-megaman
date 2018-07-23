package bankapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Buckets.GlobalInstance;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class User {
	public String customer_id;
	public int stars;
	public String first_name;
	public String last_name;
	public ArrayList<Challenge> challenges;
	public ArrayList<BucketJar> jars;
	public ArrayList<Account> bank_accounts;
	public ArrayList<Account> credit_card_accounts;
	public ArrayList<Challenge> list_of_challenges;
	public ArrayList<Transaction> curWeekTransactions;// new transactions for this week
	public ArrayList<Transaction> curMonthTransactions;// new transactions for this week
	public ArrayList<Transaction> transactions;


	public User(String login_customer_id) {
		//set the customer id
		customer_id = login_customer_id;
		bank_accounts = new ArrayList<Account>();
		challenges = new ArrayList<Challenge>();
		transactions = new ArrayList<Transaction>();
		credit_card_accounts = new ArrayList<Account>();
		curWeekTransactions = new ArrayList<Transaction>();
		curMonthTransactions = new ArrayList<Transaction>();
		jars = new ArrayList<BucketJar>();
		
		// retrieve info for the user from api
		String infoStr = GlobalInstance.getResult("https://dev.botsfinancial.com/api/customers/" + customer_id);
		JsonParser parser = new JsonParser();
		JsonElement ele = parser.parse(infoStr);
		JsonObject userInfo = ele.getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();
		// get name info
		first_name = userInfo.get("givenName").getAsString();
		last_name = userInfo.get("surname").getAsString();
		
		//account info
		JsonArray individualAccounts = userInfo.get("maskedRelatedBankAccounts")
				.getAsJsonObject().get("individual").getAsJsonArray();
		for(JsonElement acctElement: individualAccounts) {
			String acctNum = acctElement.getAsJsonObject().get("accountId").getAsString();
			Account acct = new Account(acctNum);
			bank_accounts.add(acct);
		}
		// credit accounts info
		JsonArray creditAccounts = userInfo.get("maskedRelatedCreditCardAccounts")
				.getAsJsonObject().get("authorized").getAsJsonArray();
		for(JsonElement acctElement: creditAccounts) {
			String acctNum = acctElement.getAsJsonObject().get("accountId").getAsString();
			Account acct = new Account(acctNum);
			credit_card_accounts.add(acct);
		}
		
		Challenge coffeeChallenge = new Challenge("TIM HORTONS", 6, 3, Challenge.WEEKLY_CHALLENGE);
		challenges.add(coffeeChallenge);
		BucketJar defaultJar = new BucketJar("General Savings", 99999);
		jars.add(defaultJar);
	}
	
	public JsonObject getUserInfo() {
		JsonArray chsArray = new JsonArray();
		for(Challenge ch: challenges) {
			chsArray.add(ch.getChallengeInfo());
		}
		
		JsonArray jarsArray = new JsonArray();
		for(BucketJar j: jars) {
			jarsArray.add(j.getJarInfo());
		}
    	JsonObject userInfo = new JsonObject();
    	userInfo.addProperty("userName", first_name + " " + last_name);;
    	userInfo.addProperty("numStars", stars);
    	userInfo.add("challenges", chsArray);;
    	userInfo.add("jars", jarsArray);
		
		return userInfo;
	}
	
	public void fetchUserTransactions() {
		String transStr = GlobalInstance.getResult("https://dev.botsfinancial.com/api/customers/" + customer_id + "/transactions");
		JsonParser parser = new JsonParser();
		JsonArray transArray = parser.parse(transStr).getAsJsonObject().get("result").getAsJsonArray();
		for(JsonElement tr: transArray) {
			transactions.add(new Transaction(tr.getAsJsonObject()));
		}
	}
	
	public void processNewTransaction(JsonObject trans) {
		Transaction transaction = new Transaction(trans);
		// what should do when a new transaction comes in?
		// first see the description of the transaction
		curWeekTransactions.add(transaction);
		curMonthTransactions.add(transaction);
		String desc = trans.get("description").getAsString();

	}
	
	public void newChallenge() {
		
	}
	
	// logistics for endofweek or endofmonth procedures:
	// 1. for each weekly challenge, count progress towards
	public void endOfWeekProcedure() {
		for(Challenge ch: challenges) {
			if(ch.type != Challenge.WEEKLY_CHALLENGE) continue;
			int progression = 0;
			float total_saving = 0;
			for(Transaction tr: curWeekTransactions) {
				if(tr.description.contains(ch.transaction_desc_key) || 
					tr.merchantName.contains(ch.transaction_desc_key)) {
					progression++;
					total_saving += tr.currencyAmount;
				}
			}
			if(ch.advance(progression, total_saving) != 0) {
				BucketJar defaultJar = jars.get(0);
				defaultJar.fillJar(ch.saving_accumulated);
			}
			curWeekTransactions.clear();
		}
	}
	
	public void endOfMonthProcedure() {
		for(Challenge ch: challenges) {
			if(ch.type != Challenge.WEEKLY_CHALLENGE) continue;
			int progression = 0;
			float total_saving = 0;
			for(Transaction tr: curMonthTransactions) {
				if(tr.description.contains(ch.transaction_desc_key) || 
					tr.merchantName.contains(ch.transaction_desc_key)) {
					progression++;
					total_saving += tr.currencyAmount;
				}
			}
			if(ch.advance(progression, total_saving) != 0) {
				BucketJar defaultJar = jars.get(0);
				defaultJar.fillJar(ch.saving_accumulated);
			}
			curMonthTransactions.clear();
		}
	}
	
	/*public void transferFromJar(String from, String to, amount) {
		for()
	}*/

}







