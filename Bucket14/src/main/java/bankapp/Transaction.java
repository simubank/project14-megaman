package bankapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

public class Transaction {
	public String type;
	public String description;
	public String merchant;
	public float amount;
	public Date date;
	
	public Transaction(JsonObject tr) {
		type = tr.get("type").getAsString();
		description = tr.get("description").getAsString();
		merchant = tr.get("merchantName").getAsString();
		amount = tr.get("currencyAmount").getAsFloat();
		String dateStr = tr.get("postDate").getAsString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init(){
		
	}
}
