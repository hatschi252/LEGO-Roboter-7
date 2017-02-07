package curlingBot.main;

import curlingBot.logic.EndMode;
import curlingBot.logic.LineFinderAfterMaze;
import curlingBot.logic.LineFinderMode;
import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.logic.StaticBridgeMode;
import curlingBot.logic.SwampMode;
import curlingBot.logic.SwingBridgeMode;
import curlingBot.logic.MazeMode;
import curlingBot.logic.SeesawMode;
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


		Globals.logic.addMoveMode(new MazeMode());
		Globals.logic.addMoveMode(new LineFinderMode(100, 90, 750, -1));
		Globals.logic.addMoveMode(new LineFollowerMode());
		Globals.logic.addMoveMode(new StaticBridgeMode());
		Globals.logic.addMoveMode(new LineFinderMode(80, 100, 500, -1));
		Globals.logic.addMoveMode(new LineFollowerMode());
        Globals.logic.addMoveMode(new LineFinderMode(400, 400, 2000, 500)); // burst onto see saw
        Globals.logic.addMoveMode(new LineFollowerMode()); // drive on line on the see saw
        Globals.logic.addMoveMode(new LineFinderMode(400, 400, 0, 800));  // overcome the gap after see saw
        Globals.logic.addMoveMode(new LineFollowerMode()); // go on with line
		Globals.logic.addMoveMode(new SwampMode());
		Globals.logic.addMoveMode(new SwingBridgeMode());
		// TODO endboss mode

		Globals.logic.start();
	}
}
