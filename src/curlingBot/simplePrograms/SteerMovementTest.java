package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import curlingBot.motorControl.MoveState;
import lejos.hardware.Button;

public class SteerMovementTest {
	public static void main(String[] args) {
		System.out.println("SteerMovementTest");

		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();
		Globals.motorControl.start();
		

		Globals.waitForKey(Button.ID_ENTER);
		while (true) {
			Output.put("Circle with 500 for 5secs");
			Globals.motorControl.setMoveState(new MoveState(200, 500), 5);
			Globals.sleep(5000);
			Output.put("Circle with 400 for 5secs");
			Globals.motorControl.setMoveState(new MoveState(200, 200), 5);
			Globals.sleep(5000);
			Output.finished();
		}
	}
}
