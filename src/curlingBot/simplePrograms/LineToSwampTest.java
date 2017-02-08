package curlingBot.simplePrograms;

import curlingBot.logic.AdvancedMazeMode;
import curlingBot.logic.EndMode;
import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.logic.SwampMode;
import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;

public class LineToSwampTest {
	public static void main(String[] args) {
		Output.put("LineToSwampTest");
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
		Globals.logic.addMoveMode(new LineFollowerMode(""));
		Globals.logic.addMoveMode(new SwampMode("")); // into the swamp
		Globals.logic.addMoveMode(new AdvancedMazeMode("")); // out of the swamp
		Globals.logic.addMoveMode(new AdvancedMazeMode("")); // follow wall after swamp
		Globals.logic.addMoveMode(new EndMode(""));
		
		Globals.logic.restart(0);		
	}
}
