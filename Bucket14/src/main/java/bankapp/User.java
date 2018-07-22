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
	public ArrayList<Transaction> newTransactions;
	public ArrayList<JsonObject> transactions;


	public User(String login_customer_id) {
		//set the customer id
		customer_id = login_customer_id;
		bank_accounts = new ArrayList<Account>();
		list_of_challenges = new ArrayList<Challenge>();
		transactions = new ArrayList<JsonObject>();
		credit_card_accounts = new ArrayList<Account>();
		
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
		
	}
	
	public void fetchUserTransactions() {
		String transStr = GlobalInstance.getResult("https://dev.botsfinancial.com/api/customers/" + customer_id + "/transactions");
		JsonParser parser = new JsonParser();
		JsonArray transArray = parser.parse(transStr).getAsJsonObject().get("result").getAsJsonArray();
		for(JsonElement tr: transArray) {
			transactions.add(tr.getAsJsonObject());
		}
	}

}







