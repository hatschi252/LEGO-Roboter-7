package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import curlingBot.motorControl.MoveState;
import lejos.hardware.Button;

public class RealStraightMovementTest {

	public static void main(String[] args) {
		System.out.println("StraightMovementTest");

		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();
		Globals.motorControl.start();

		Globals.waitForKey(Button.ID_ENTER);
		while (true) {
			Output.put("700");
			Globals.motorControl.setMoveState(new MoveState(700, 0), 5);
			Globals.sleep(5000);
			
			Globals.motorControl.emergencyStop();
			Globals.sleep(2000);
			
			Output.put("100");
			Globals.motorControl.setMoveState(new MoveState(100, 0), 5);
			Globals.sleep(5000);
			
			Globals.motorControl.emergencyStop();
			Globals.sleep(2000);
			
			Output.put("8===D");
			Output.finished();
		}
	}

}
