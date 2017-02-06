package curlingBot.main;

import curlingBot.logic.Logic;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;
import lejos.hardware.Button;

public final class Globals {
	//Distance between the middle of the two wheels in cm
	public static final float AXIS_LENGTH = 19f;
	
	public static Logic logic;
	public static MotorControl motorControl;
	public static SensorBuffer sensorBuffer;
	public static ExitThread exitThread;
	
	private Globals() {}
	

	public static void waitForKey(int key) {
		while ((Button.waitForAnyEvent() & key) == 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
