package bankapp;

public class Challenge {
	public float goal;
	public float progress;
	
	
	
	public void init(){
		goal=0;
		progress=0;
		
	}
	
	public void setgoal(float goal_amount){
		goal = goal_amount;
	}
	
	private void advance(float advance_amount){
		progress += advance_amount;
	}
	
	
}
