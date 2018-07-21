package bankapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Buckets.BucketUtilities;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class User {
	public String customer_id;
	public Account[] list_of_accounts;
	public String first_name;
	public String last_name;
	public Challenge list_of_challenges;
	public ArrayList<Transaction> newTransactions;
	public ArrayList<JsonObject> transactions;


	public void login(String login_customer_id) {
		//set the customer id
		customer_id = login_customer_id;
		String infoStr = BucketUtilities.getResult("https://dev.botsfinancial.com/api/customers/" + customer_id);
		JsonParser parser = new JsonParser();
		JsonElement ele = parser.parse(infoStr);
	}
	
	public void fetchUserTransactions() {
		String transStr = BucketUtilities.getResult("https://dev.botsfinancial.com/api/customers/" + customer_id + "/transactions");
	}

}







