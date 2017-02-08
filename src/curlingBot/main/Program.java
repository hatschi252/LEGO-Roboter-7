package curlingBot.main;

import curlingBot.logic.AdvancedMazeMode;
import curlingBot.logic.EndMode;
import curlingBot.logic.LineFinderAfterMaze;
import curlingBot.logic.LineFinderLineSearchAfterBridge;
import curlingBot.logic.LineFinderMode;
import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.logic.StaticBridgeMode;
import curlingBot.logic.SwampMode;
import curlingBot.logic.SwingBridgeMode;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;

public class Program {
	public static void main(String[] args) {
		Output.put("Launching program");
		// Start exit thread first. If something goes wrong we can still shut
		// everything down.
		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();

		Globals.motorControl = MotorControl.getInstance();

		Globals.sensorBuffer = SensorBuffer.getInstance();
		Globals.sensorBuffer.start();

		Globals.logic = Logic.getInstance();
		// Add the moveModes in the order they are appearing in the parkour

		Globals.logic.addMoveMode(new AdvancedMazeMode());
		Globals.logic.addMoveMode(new LineFinderMode(100, 90, 750, -1));
		Globals.logic.addMoveMode(new LineFollowerMode());
		Globals.logic.addMoveMode(new StaticBridgeMode());
		//Globals.logic.addMoveMode(new LineFinderMode(80, 100, 500, -1));
        Globals.logic.addMoveMode(new LineFinderLineSearchAfterBridge());
        Globals.logic.addMoveMode(new LineFinderMode(110, 80, 0, 1000));
		Globals.logic.addMoveMode(new LineFollowerMode());
        Globals.logic.addMoveMode(new LineFinderMode(400, 400, 1500, 0)); // burst blindly onto see saw
        Globals.logic.addMoveMode(new LineFollowerMode()); // find/drive on line on the see saw
        Globals.logic.addMoveMode(new LineFinderMode(400, 400, 0, 1500));  // overcome the gap after see saw
        Globals.logic.addMoveMode(new LineFollowerMode()); // go on with line
		Globals.logic.addMoveMode(new SwampMode());
		Globals.logic.addMoveMode(new SwingBridgeMode());
		// TODO endboss mode

		Globals.logic.start();
	}
}
