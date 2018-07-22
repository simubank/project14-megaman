package bankapp;

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
	public float saving_accumulated = 0;
	public final int type;
	
	public final static int WEEKLY_CHALLENGE = 0;
	public final static int MONTHLY_CHALLENGE = 1;
	
	public Challenge(String name, int goal, int unit, int type) {
		this.type = type;
		this.goal = goal;
		this.transaction_desc_key = name;
		//this.saving_per_unit = saving;
		this.fixed_unit = unit;
	}

	
	public int advance(int advance_amount, float saving){
		progress += advance_amount;
		
		if(progress >= goal) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
}
