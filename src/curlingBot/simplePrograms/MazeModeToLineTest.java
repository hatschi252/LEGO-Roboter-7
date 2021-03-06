package curlingBot.simplePrograms;

import curlingBot.logic.EndMode;
import curlingBot.logic.LineFollowerMode;
import curlingBot.logic.Logic;
import curlingBot.logic.AdvancedMazeMode;
import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import curlingBot.sensors.SensorBuffer;

public class MazeModeToLineTest {

	public static void main(String[] args) {
		Output.put("MazeModeToLineTest");
		//Start exit thread first. If something goes wrong we can still shut everything down.
		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();
//		Globals.motorControl.start();
		
		Globals.sensorBuffer = SensorBuffer.getInstance();
		Globals.sensorBuffer.start();
		
		Globals.logic = Logic.getInstance();
		
		//Add the moveModes in the order they are appearing in the parkour
		Globals.logic.addMoveMode(new AdvancedMazeMode(""));
		Globals.logic.addMoveMode(new LineFollowerMode(""));
		Globals.logic.addMoveMode(new EndMode(""));
		
		Globals.logic.restart(0);		

	}

}
