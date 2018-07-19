package bankapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class User {
	public int customer_id;
	public Account[] list_of_accounts;
	public String first_name;
	public String last_name;
	public Challenge list_of_challenges;
	public ArrayList<Transaction> newTransactions;


	public void login(int login_customer_id) {
		//set the customer id
		customer_id = login_customer_id;
	}

}







