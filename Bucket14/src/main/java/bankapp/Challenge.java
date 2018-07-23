package bankapp;

import com.google.gson.JsonObject;

public class Challenge {
	// all challenges work in similar ways: 
	// if you have less than fixed_unit of merchandise with
	// transaction_desc_key as description, then progress advances 
	// towards the goal by the difference. When hitting the goal, 
	// saving_accumulated amount of money is saved to your jar
	// and you gain one star.
	public int progress;
	public String name;
	public final int goal;
	public final int fixed_unit;
	public final String transaction_desc_key;
	public float saving_per_unit;
	public final int type;
	
	public final static int WEEKLY_CHALLENGE = 0;
	public final static int MONTHLY_CHALLENGE = 1;
	
	public Challenge(String name, int goal, int unit, int type, float saving) {
		this.type = type;
		this.goal = goal;
		this.transaction_desc_key = name;
		this.saving_per_unit = saving;
		this.fixed_unit = unit;
		this.progress = 0;
	}

	
	public int advance(int advance_amount, float saving){
		progress += advance_amount;
		
		if(progress >= goal) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public JsonObject getChallengeInfo() {
		JsonObject info = new JsonObject();
		info.addProperty("name", transaction_desc_key);
		info.addProperty("goal", goal);
		info.addProperty("fixedUnit", fixed_unit);
		info.addProperty("progress", progress);
		return info;
	}
	
	public void reset() {
		progress = 0;
	}
	
}
