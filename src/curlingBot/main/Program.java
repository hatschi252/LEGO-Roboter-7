package curlingBot.main;

import curlingBot.logic.EndMode;
import curlingBot.logic.LineFinderAfterMaze;
import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.logic.StaticBridgeMode;
import curlingBot.logic.SwampMode;
import curlingBot.logic.SwingBridgeMode;
import curlingBot.logic.WallFollowerMode;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;

public class Program {
	public static void main(String[] args) {
		Output.put("Launching program");
		//Start exit thread first. If something goes wrong we can still shut everything down.
		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();

		
		Globals.sensorBuffer = SensorBuffer.getInstance();
		Globals.sensorBuffer.start();
		
		Globals.logic = Logic.getInstance();
		//Add the moveModes in the order they are appearing in the parkour
		
		Globals.logic.addMoveMode(new WallFollowerMode());
		Globals.logic.addMoveMode(new LineFinderAfterMaze());
		Globals.logic.addMoveMode(new LineFollowerMode());
		Globals.logic.addMoveMode(new StaticBridgeMode());
		Globals.logic.addMoveMode(new LineFollowerMode());
		// TODO add move mode for seesaw (wippe) and insert linefollowermode after it
		Globals.logic.addMoveMode(new SwampMode());
		Globals.logic.addMoveMode(new SwingBridgeMode());
		// TODO endboss mode
		
		Globals.logic.start();		
	}
}
