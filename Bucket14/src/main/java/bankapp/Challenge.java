package bankapp;

public class Challenge {
	public float goal;
	public float progress;
	public String name;
	
	public Challenge(String name) {
		this.name = name;
		init();
	}
	
	public void init(){
		goal=0;
		progress=0;
		
	}
	
	public void setgoal(float goal_amount){
		goal = goal_amount;
	}
	
	public int advance(float advance_amount){
		progress += advance_amount;
		if(progress >= goal) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
}
