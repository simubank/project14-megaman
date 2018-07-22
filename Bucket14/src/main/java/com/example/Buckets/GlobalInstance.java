package com.example.Buckets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import bankapp.User;

public class GlobalInstance {
	
	// this class contains global variables and helper functions
	public static final String KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDQlAiLCJ0ZWFtX2lkIjoiMjgxMzg1MCIsImV4cCI6OTIyMzM3MjAzNjg1NDc3NSwiYXBwX2lkIjoiMzFkMWYyNWItMDU0Ni00NTczLTkxOTItZGJmNTVlZjRjZGZkIn0.08_I3LRu0KvoId4drqurwIkxrNA4vJBlrlAGBt5b3do";
	public static User global_user;
	
	
	public static String getResult(String endpoint) {
    	URL url;
    	try {
    		url = new URL(endpoint);
        	HttpURLConnection con = (HttpURLConnection) url.openConnection();
        	con.setRequestMethod("GET");
        	con.setRequestProperty("Content-Type", "application/json");
        	con.setRequestProperty("Authorization", KEY);
        	int status = con.getResponseCode();
        	//System.out.println("server responded with status code" + status);
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
        	
        	
        	
        	
        	
        	con.disconnect();
    		return content.toString();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return "";
    }
	
}
