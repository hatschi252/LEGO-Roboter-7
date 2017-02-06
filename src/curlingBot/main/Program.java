package curlingBot.main;

import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;
import lejos.hardware.Button;

public class Program {
	public static void main(String[] args) {
		//Start exit thread first. If something goes wrong we can still shut everything down.
		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();
		Globals.motorControl.start();
		
		Globals.sensorBuffer = SensorBuffer.getInstance();
		Globals.sensorBuffer.start();
		
		Globals.logic = Logic.getInstance();
		//Add the moveModes in the order they are appearing in the parkour
		Globals.logic.addMoveMode(new LineFollowerMode());
		Globals.logic.start();		
	}
}
