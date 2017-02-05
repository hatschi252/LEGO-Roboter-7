package main;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class AdvancedFollower {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float kp = 1.95f;
		int speed0 = 250;
		float correction = (1f / 100f) * 2.5f * speed0;
		Motor.A.setSpeed(speed0);
		Motor.B.setSpeed(speed0);
		LightSensor ls = new LightSensor(SensorPort.S4);
		ls.setFloodlight(true);
		System.out.println("Set on white and press Button");
		Button.waitForAnyPress();
		int white = (int) (ls.getLightValue() * correction);
		System.out.print(ls.getLightValue());
		System.out.print("Set on line and press Button");
		Button.waitForAnyPress();
		int black = (int) (ls.getLightValue() * correction);
		System.out.println("Press Button to run");
		Button.waitForAnyPress();
		forward();
		int midpoint = (white + black) / 2;
		int lightNow;
		for(;;)//ever
		{
			lightNow = ls.getLightValue();
			System.out.println(midpoint - lightNow);
			Motor.A.setSpeed(speed0 + kp * (midpoint - lightNow * correction));
			Motor.B.setSpeed(speed0 - kp * (midpoint - lightNow * correction)); 
			forward();
		}
	}

	static void forward()
	{
		Motor.A.forward();
		Motor.B.forward();
	}

}
