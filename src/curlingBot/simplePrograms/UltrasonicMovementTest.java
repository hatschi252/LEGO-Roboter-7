package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.motorControl.MotorControl;
import lejos.hardware.Button;

public class UltrasonicMovementTest {
	public static void main(String[] args) {
		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();

		Globals.motorControl = MotorControl.getInstance();
		Globals.motorControl.start();
		
		System.out.println("ultrasonic movement test");

		while (true) {
			Globals.waitForKey(Button.ID_ENTER);
			Globals.motorControl.moveUltrasonicDown();
			Globals.waitForKey(Button.ID_ENTER);
			Globals.motorControl.moveUltrasonicUp();
		}
	}
}
