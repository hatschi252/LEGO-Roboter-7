package main;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class FurthAdvFollower {
	static float kp;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		kp = 0.5f;
		int speed0 = 250;
		float correction = 1.5f * speed0;
		Motor.A.setSpeed(speed0);
		Motor.B.setSpeed(speed0);
		LightSensor ls = new LightSensor(SensorPort.S4);
		ls.setFloodlight(true);
		System.out.println("Set on white and press Button");
		Button.waitForAnyPress();
		int white = ls.getLightValue(); // here max value
		System.out.println(white);
		System.out.println(ls.getLightValue());
		System.out.println("Set on line and press Button");
		Button.waitForAnyPress();
		int black = ls.getLightValue(); // here min value
		System.out.println(black);
		System.out.println("Press Button to run");
		Button.waitForAnyPress();
		forward();
		float midpoint = 0.5f; // 
		float lightNow;
		for(;;)//ever
		{
			lightNow = (float )ls.getLightValue();
			float err = midpoint - ((lightNow - black) / (white - black));
			Motor.A.setSpeed(speed0 + kp * (err * correction)); // norm to 0 - 1
			Motor.B.setSpeed(speed0 - kp * (err * correction)); 
			forward();
		}
	}

	static void forward()
	{
		Motor.A.forward();
		Motor.B.forward();
	}
}
