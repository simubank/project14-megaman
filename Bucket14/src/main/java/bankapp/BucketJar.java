package bankapp;

public class BucketJar {
	// total stands for total value saved, current represents the value currently saved. 
	public float total;
	public float current;
	public String name;
	
	public BucketJar(String name) {
		total = 0;
		current = 0;
		this.name = name;
	}
	
	public float fillJar(float amount) {
		if(current + amount > total) {
			float overflow = current + amount - total;
			current = total;
			return overflow;
		} else {
			current += amount;
			return 0;
		}
	}
}
