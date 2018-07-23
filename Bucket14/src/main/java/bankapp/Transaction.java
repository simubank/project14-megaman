package bankapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

public class Transaction {
	public String type;
	public String description;
	public String merchantName;
	public float currencyAmount;
	public String postDate;
	
	public String getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getMerchantName() {
		return merchantName;
	}
	
	public float getCurrencyAmount() {
		return currencyAmount;
	}
	
	public String getPostDate() {
		return postDate;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public void setCurrencyAmount(float currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
	
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	
	public Transaction(JsonObject tr) {
		type = tr.get("type").getAsString();
		description = tr.get("description").getAsString();
		merchantName = tr.get("merchantName").getAsString();
		currencyAmount = tr.get("currencyAmount").getAsFloat();
		postDate = tr.get("postDate").getAsString();
		/*String dateStr = tr.get("postDate").getAsString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	
	public void init(){
		
	}
}
