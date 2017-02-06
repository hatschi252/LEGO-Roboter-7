package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TachoTest {
	public static void main(String[] args) {
		Output.put("RawAccelerationTest");

		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();

		Globals.waitForKey(Button.ID_ENTER);

		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorControl.MOTORPORT_LEFT);
		
		leftMotor.setSpeed(700);
		leftMotor.setAcceleration(Integer.MAX_VALUE);

		Output.put("2s forward");
		leftMotor.forward();
		Globals.sleep(2000);
		Output.put("Tacho: " + leftMotor.getTachoCount());
		
		Output.put("6s backward");
		leftMotor.backward();
		Globals.sleep(2000);
		Output.put("Tacho: " + leftMotor.getTachoCount());
		
		leftMotor.stop();
	}
}
