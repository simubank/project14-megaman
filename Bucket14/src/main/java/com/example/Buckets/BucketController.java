package com.example.Buckets;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BucketController {
    
    private static final String ParameterStringBuilder = null;
    protected static Logger logger = Logger.getLogger(BucketController.class);
    
    // ----------------------------------app endpoints----------------------------------------
    
    
    
    //-----------------------------------testing endpoints -----------------------------------
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
	    	con.setRequestProperty("Authorization", BucketUtilities.KEY);
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
