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

		Globals.logic.addMoveMode(new AdvancedMazeMode("First maze mode"));
		Globals.logic.addMoveMode(new LineFinderMode("Line finder mode", 100, 100, 500, 0));
		Globals.logic.addMoveMode(new AdvancedMazeMode("Advanced maze mode"));
		Globals.logic.addMoveMode(new LineFinderMode("Line finder mode", 100, 90, 750, -1));
		Globals.logic.addMoveMode(new LineFollowerMode("Line follower"));
		Globals.logic.addMoveMode(new StaticBridgeMode("Static bridge mode"));
		//Globals.logic.addMoveMode(new LineFinderMode(80, 100, 500, -1));
        Globals.logic.addMoveMode(new LineFinderLineSearchAfterBridge("LineFinderLineaSearchAfterBridge"));
        Globals.logic.addMoveMode(new LineFinderMode("Line finder mode", 110, 80, 0, 1000));
		Globals.logic.addMoveMode(new LineFollowerMode("Line follower mode"));
        Globals.logic.addMoveMode(new LineFinderMode("Line finder mode", 400, 400, 1500, 0)); // burst blindly onto see saw
        Globals.logic.addMoveMode(new LineFollowerMode("Line follower mode")); // find/drive on line on the see saw
        Globals.logic.addMoveMode(new LineFinderMode("Line Finder mode", 400, 400, 0, 1500));  // overcome the gap after see saw
        Globals.logic.addMoveMode(new LineFollowerMode("Line follower mode")); // go on with line
		Globals.logic.addMoveMode(new SwampMode("Swamp mode")); // into the swamp
		Globals.logic.addMoveMode(new AdvancedMazeMode("Advanced maze mode")); // out of the swamp
		Globals.logic.addMoveMode(new AdvancedMazeMode("Advanced maze mode")); // follow wall after swamp
		Globals.logic.addMoveMode(new SwingBridgeMode("Swing bridge mode"));
		// TODO endboss mode

		Globals.logic.restart(0);
	}
}
