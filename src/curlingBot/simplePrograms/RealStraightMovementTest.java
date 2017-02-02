package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
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
		
		while (true) {
			Globals.waitForKey(Button.ID_ENTER);
			Globals.motorControl.setMoveState(new MoveState(100, 0), 10);
			Globals.sleep(3000);
			Globals.motorControl.setMoveState(new MoveState(0, 0), 0);
			Globals.waitForKey(Button.ID_ENTER);
			Globals.motorControl.setMoveState(new MoveState(100, 0), 10);
			Globals.sleep(3000);
			Globals.motorControl.emergencyStop();
		}
	}

}
