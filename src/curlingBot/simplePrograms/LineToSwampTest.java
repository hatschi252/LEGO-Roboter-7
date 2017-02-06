package curlingBot.simplePrograms;

import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;

public class LineToSwampTest {
	public static void main(String[] args) {
		//Start exit thread first. If something goes wrong we can still shut everything down.
		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();
//		Globals.motorControl.start();
		
		Globals.sensorBuffer = SensorBuffer.getInstance();
		Globals.sensorBuffer.start();
		
		Globals.logic = Logic.getInstance();
		//Add the moveModes in the order they are appearing in the parkour
		//Globals.logic.addMoveMode(new WallFollowerMode());
		Globals.logic.addMoveMode(new LineFollowerMode());
		Globals.logic.start();		
	}
}