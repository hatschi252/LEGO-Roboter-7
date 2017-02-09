package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.motorControl.MotorControl;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class RawAccelerationTest {

    @SuppressWarnings("resource")
	public static void main(String[] args) {

		System.out.println("RawAccelerationTest");

		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();

		Globals.waitForKey(Button.ID_ENTER);

		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorControl.MOTORPORT_LEFT);
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorControl.MOTORPORT_RIGHT);
		
		System.out.println("vMax" + leftMotor.getMaxSpeed());

		leftMotor.setSpeed(700);
		rightMotor.setSpeed(700);
		leftMotor.setAcceleration(Integer.MAX_VALUE);
		rightMotor.setAcceleration(Integer.MAX_VALUE);

		leftMotor.forward();
		rightMotor.forward();

		while (true) {
			System.out.println("leftVel: " + leftMotor.getRotationSpeed());
			System.out.println("rightVel: " + rightMotor.getRotationSpeed());
			System.out.println("leftAcc: " + leftMotor.getAcceleration());
			System.out.println("rightAcc: " + rightMotor.getAcceleration());
			System.out.println();
			Globals.sleep(100);
		}

	}

}
