package com.example.Buckets;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import bankapp.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    	String challengesStr = "";
    	String jarStr = "";
    	JsonObject response = new JsonObject();
    	response.addProperty("serverTime", GlobalInstance.dateTime.toString());
    	response.addProperty("userName", GlobalInstance.global_user.first_name + " " + GlobalInstance.global_user.last_name);;
    	response.addProperty("numStars", GlobalInstance.global_user.stars);
    	response.addProperty("challenges", challengesStr);
    	response.addProperty("jars", jarStr);
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
    	return response.toString();
    }
    
    @PostMapping("/nextWeek")
    public String nextWeek() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(GlobalInstance.dateTime);
    	cal.add(Calendar.DATE, 1);
    	GlobalInstance.dateTime = cal.getTime();
    	JsonObject response = new JsonObject();
    	response.addProperty("date", GlobalInstance.dateTime.toString());
    	return response.toString();
    }
    
	@RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @GetMapping("/test1")
    public String test1() {
        return "Greetings from Spring Boot!";
    }
    
    @GetMapping("/test2")
    public String test2() {
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
