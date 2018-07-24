package bankapp;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Buckets.Bucket14Application;
import com.example.Buckets.BucketController;
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
	public ArrayList<Challenge> completedChallenges;
	public ArrayList<BucketJar> jars;
	public ArrayList<Account> bank_accounts;
	public ArrayList<Account> credit_card_accounts;
	public ArrayList<Challenge> list_of_challenges;
	public ArrayList<Transaction> todaysTransactions;// new transactions for today
	public ArrayList<Transaction> curWeekTransactions;// new transactions for this week
	public ArrayList<Transaction> curMonthTransactions;// new transactions for this month
	public ArrayList<Transaction> transactions;

    protected static Logger logger = Logger.getLogger(User.class);


	public User(String login_customer_id) {
		//set the customer id
		stars = 0;
		customer_id = login_customer_id;
		bank_accounts = new ArrayList<Account>();
		challenges = new ArrayList<Challenge>();
		completedChallenges = new ArrayList<Challenge>();
		transactions = new ArrayList<Transaction>();
		credit_card_accounts = new ArrayList<Account>();
		todaysTransactions = new ArrayList<Transaction>();
		curWeekTransactions = new ArrayList<Transaction>();
		curMonthTransactions = new ArrayList<Transaction>();
		jars = new ArrayList<BucketJar>();
		
		// retrieve info for the user from api
		String infoStr = GlobalInstance.getResult("https://dev.botsfinancial.com/api/customers/" + customer_id);
		JsonParser parser = new JsonParser();
		JsonElement ele = parser.parse(infoStr);
		
		if (ele.isJsonNull()) {
			first_name = null;
			logger.error("Incorrect credentials");
		} else {
		
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
		// credit accounts info & gets user's average cost on certain item
		JsonArray creditAccounts = userInfo.get("maskedRelatedCreditCardAccounts")
				.getAsJsonObject().get("authorized").getAsJsonArray();
		for(JsonElement acctElement: creditAccounts) {
			String acctNum = acctElement.getAsJsonObject().get("accountId").getAsString();
			Account acct = new Account(acctNum);
			credit_card_accounts.add(acct);
		}
		
		
		// generate challenges and jars here
		Challenge coffeeChallenge = new Challenge("TIM HORTONS", 3, 3, Challenge.WEEKLY_CHALLENGE, (float)1.78);
		challenges.add(coffeeChallenge);
		Challenge McDsChallenge = new Challenge("MCDONALDS", 7, 3, Challenge.WEEKLY_CHALLENGE, (float)10);
		challenges.add(McDsChallenge);
		BucketJar defaultJar = new BucketJar("General Savings", 90000);
		jars.add(defaultJar);
		}
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
		todaysTransactions.add(transaction);
		curWeekTransactions.add(transaction);
		curMonthTransactions.add(transaction);
		//String desc = trans.get("description").getAsString();

	}
	
	public void newChallenge() {
		
	}
	
	// calculate progression for each challenge 
	public void endOfDayProcedure() {
		ArrayList<Challenge> completedToday = new ArrayList<Challenge>(); 
		for(Challenge ch: challenges) {
			int transCount = 0;
			for(Transaction tr: todaysTransactions) {
				if(tr.description.contains(ch.transaction_desc_key) || 
					tr.merchantName.contains(ch.transaction_desc_key)) {
					transCount++;
					break;
				}
			}
			if(transCount == 0) {
				int result = ch.advance(1, 0);
				if(result == 1) {
					completedToday.add(ch);
					BucketJar defaultJar = jars.get(0);
					defaultJar.fillJar(ch.saving_per_unit * ch.goal);
					stars++;
					completedChallenges.add(ch);
				}
			}
		}
		for(Challenge chToRemove: completedToday) {
			int index = challenges.indexOf(chToRemove);
			if(index != -1)
				challenges.remove(index);
		}
		todaysTransactions.clear();
	}
	// logistics for endofweek or endofmonth procedures:
	// 1. for each weekly challenge, count progress towards
	public void endOfWeekProcedure() {
		challenges.addAll(completedChallenges);
		completedChallenges.clear();
		for(Challenge ch: challenges) {
			if(ch.type == Challenge.WEEKLY_CHALLENGE) {
				ch.reset();
			}
		}
		/*if(ch.type != Challenge.WEEKLY_CHALLENGE) continue;
		int progression = 0;
		float total_saving = 0;
		for(Transaction tr: curWeekTransactions) {
			if(tr.description.contains(ch.transaction_desc_key) || 
				tr.merchantName.contains(ch.transaction_desc_key)) {
				progression++;
				total_saving += tr.currencyAmount;
			}
		}
		progression = ch.fixed_unit - progression;
		if(progression <= 0) continue;
		if(ch.advance(progression, total_saving) != 0) {
			BucketJar defaultJar = jars.get(0);
			defaultJar.fillJar(ch.saving_per_unit * progression);
		}
		curWeekTransactions.clear();*/
	}
	
	public void endOfMonthProcedure() {
		ArrayList<Challenge> remaining = new ArrayList<Challenge>();
		for(Challenge ch: completedChallenges) {
			if(ch.type == Challenge.MONTHLY_CHALLENGE) {
				challenges.add(ch);
			} else {
				remaining.add(ch);
			}
		}
		completedChallenges.clear();
		completedChallenges.addAll(remaining);
		for(Challenge ch: challenges) {
			if(ch.type == Challenge.MONTHLY_CHALLENGE) {
				ch.reset();
			}
		}
	}
	
	/*public void transferFromJar(String from, String to, amount) {
		for()
	}*/

}







