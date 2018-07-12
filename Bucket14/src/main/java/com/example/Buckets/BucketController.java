package com.example.Buckets;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class BucketController {
    
    private static final String ParameterStringBuilder = null;

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
    public String apiTest() {
    	URL url;
		try {
			url = new URL("https://dev.botsfinancial.com/api/accounts/123");
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	con.setRequestMethod("GET");
	    	con.setRequestProperty("Content-Type", "application/json");
	    	int status = con.getResponseCode();
	    	if(true) {
	    		BufferedReader in = new BufferedReader(
	    				  new InputStreamReader(con.getInputStream()));
	    		String inputLine;
	    		StringBuffer content = new StringBuffer();
	    		while ((inputLine = in.readLine()) != null) {
	    			content.append(inputLine);
	    		}
	    		in.close();
	    	}
	    	con.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "";
    }
}
