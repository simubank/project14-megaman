package com.example.Buckets;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import bankapp.BucketJar;
import bankapp.Transaction;
import bankapp.User;

import static org.mockito.Matchers.intThat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BucketController {
    
    private static final String ParameterStringBuilder = null;
    protected static Logger logger = Logger.getLogger(BucketController.class);
    
    // ----------------------------------app endpoints----------------------------------------
    @RequestMapping("/login")
    public String login(@RequestParam("id") String id){
    	GlobalInstance.global_user = new User(id);
    	JsonObject user = GlobalInstance.global_user.getUserInfo();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(GlobalInstance.dateTime);
    	int date = cal.get(Calendar.DAY_OF_MONTH);
    	int month = cal.get(Calendar.MONTH) + 1;
    	JsonObject response = new JsonObject();
    	response.add("userInfo", user);
    	response.addProperty("serverTime", GlobalInstance.dateTime.toString());
    	response.addProperty("date", date);
    	response.addProperty("month", month);
    	
    	return response.toString();
    }
    
    @GetMapping("/serverDate")
    public String getServerDate() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(GlobalInstance.dateTime);
    	int date = cal.get(Calendar.DAY_OF_MONTH);
    	// january is 1, feb. is 2, etc.
    	int month = cal.get(Calendar.MONTH) + 1;
    	JsonObject response = new JsonObject();
    	response.addProperty("serverTimeStr", GlobalInstance.dateTime.toString());
    	response.addProperty("date", date);
    	response.addProperty("month", month);
    	return response.toString();
    }
    
    @GetMapping("/userInfo")
    public String getUserInfo() {
    	JsonObject response = GlobalInstance.global_user.getUserInfo();
    	return response.toString();
    }
    
    @PostMapping("/createJar")
    public String createNewJar(@RequestParam("name") String name, @RequestParam("goal") int goal) {
    	BucketJar newJar = new BucketJar(name, (float)goal);
    	GlobalInstance.global_user.jars.add(newJar);
    	JsonObject response = new JsonObject();
    	response.addProperty("result", "success");
    	return response.toString();
    }
    
    //-----------------------------------testing endpoints -----------------------------------
    
    
    @PostMapping("/nextDay")
    public String nextDay() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(GlobalInstance.dateTime);
    	cal.add(Calendar.DATE, 1);
    	GlobalInstance.dateTime = cal.getTime();
    	JsonObject response = new JsonObject();
    	response.addProperty("date", GlobalInstance.dateTime.toString());
    	GlobalInstance.global_user.endOfDayProcedure();
    	// calculate progresses for challenges
    	if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
    		GlobalInstance.global_user.endOfWeekProcedure();
    	}
    	if(cal.get(Calendar.DAY_OF_MONTH) == 1) {
    		GlobalInstance.global_user.endOfMonthProcedure();
    	}
    	return response.toString();
    }
    
    @PostMapping("/nextWeek")
    public String nextWeek() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(GlobalInstance.dateTime);
    	cal.add(Calendar.DATE, 7);
    	GlobalInstance.dateTime = cal.getTime();
    	JsonObject response = new JsonObject();
    	response.addProperty("date", GlobalInstance.dateTime.toString());
    	return response.toString();
    }
    
    @PostMapping("/makeTransaction")
    public String makeTransaction(@RequestBody String transactionStr) {
    	
    	
    	JsonParser parser = new JsonParser();
    	JsonObject transaction = parser.parse(transactionStr).getAsJsonObject();
    	GlobalInstance.global_user.processNewTransaction(transaction);
    	String id = "";
    	
    	return id;
    }
    
	/*@RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }*/
    
    @GetMapping("/test1")
    public String test1() {
        return "Greetings from Spring Boot!";
    }
    
    @PostMapping("/test2")
    public String test2() {
    	String a = GlobalInstance.postHttp("", "");
        return "Greetings from Spring Boot!";
    }
    
    @GetMapping("/apitest")
    public String apiTest(@RequestParam("id") String id) {
    	URL url;
		try {
			logger.info(" getting account info from botsfinancial accounts api with account #" + id);
			url = new URL("https://dev.botsfinancial.com/api/accounts/" + id);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	con.setRequestMethod("GET");
	    	con.setRequestProperty("Content-Type", "application/json");
	    	con.setRequestProperty("Authorization", GlobalInstance.KEY);
	    	int status = con.getResponseCode();
	    	logger.info("server responded with status code " + status);
	    	StringBuffer content = new StringBuffer();
	    	if(status >= 200 && status < 400) {
	    		BufferedReader in = new BufferedReader(
	    				  new InputStreamReader(con.getInputStream()));
	    		String inputLine;
	    		while ((inputLine = in.readLine()) != null) {
	    			content.append(inputLine);
	    		}
	    		in.close();
	    	}
	    	// 
	    	
	    	//JsonObject jsonO = content.toString();
	    	
	    	
	    	
	    	con.disconnect();
    		return content.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
    	return "";
    }
    
    
}
