package curlingBot.main;

import curlingBot.logic.Logic;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;

public final class Globals {
	public static Logic logic;
	public static MotorControl motorControl;
	public static SensorBuffer sensorBuffer;
	public static ExitThread exitThread;
	
	private Globals() {}
}
