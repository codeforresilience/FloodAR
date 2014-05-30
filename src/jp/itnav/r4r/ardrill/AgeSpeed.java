package jp.itnav.r4r.ardrill;

public class AgeSpeed {
	
	private static float setSpeed(int age){
		float mSpeed = 0;
		if(age<10){
			mSpeed = 0.01f;
		}
		if(age<15){
			mSpeed = 0.02f;
		}
		if(age<25){
			mSpeed = 0.03f;
		}
		if(age>25&&age<30){
			mSpeed = 0.02f;
		}
		if(age>30&&age<40){
			mSpeed = 0.01f;
		}else{
			mSpeed = 0;
		}
		return mSpeed;
	}
	
	public static float getSpeed(int age){
		return setSpeed(age);
	}
}
