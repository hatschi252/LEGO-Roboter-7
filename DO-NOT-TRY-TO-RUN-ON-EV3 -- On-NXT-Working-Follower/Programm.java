package main;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Programm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float kp = 4.5f;
		int speed0 = 80;
		Motor.A.setSpeed(speed0);
		Motor.B.setSpeed(speed0);
		LightSensor ls = new LightSensor(SensorPort.S4);
		ls.setFloodlight(true);
		System.out.println("Set on white and press Button");
		Button.waitForAnyPress();
		int white = ls.getLightValue();
		System.out.print(ls.getLightValue());
		System.out.print("Set on line and press Button");
		Button.waitForAnyPress();
		int black = ls.getLightValue();
		System.out.println("Press Button to run");
		Button.waitForAnyPress();
		forward();
		int midpoint = (white + black) / 2;
		int lightNow;
		for(;;)//ever
		{
			lightNow = ls.getLightValue();
			System.out.println(midpoint - lightNow);
			Motor.A.setSpeed(speed0 + kp * (midpoint - lightNow));
			Motor.B.setSpeed(speed0 - kp * (midpoint - lightNow)); 
			forward();
		}
	}

	static void forward()
	{
		Motor.A.forward();
		Motor.B.forward();
	}

}
