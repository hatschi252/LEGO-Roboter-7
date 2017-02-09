package curlingBot.logic.endboss;

import curlingBot.main.Globals;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

/**
 * This endboss strategy is randomized. The robot drives a random 
 * time a random turn and random speed. When the robot drives against 
 * an obstacle it drives backward for a short period of time.
 * 
 */
public class Random implements EndBossStrategy {

	private final float BRIGHTNESS_THRESH = 0.2f;
	private final int BACKWARD_SPEED = 200;
	private final int DRIVE_BACKWARD_DELAY = 1000;
	private final int STANDARD_SPEED = 700;
	private final int MIN_TIME = 1500;
	private final int TIMEINTERVALL = 2000;

	@Override
	public void init() {
		Globals.sensorBuffer.setColorSensorActive(true);
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
	}

	@Override
	public void perform() {
		// TODO Auto-generated method stub
		// line detection -> turn around
		// random turn and random drive forwards
		//
		while (true) {
			driveRandomForward();
		}
	}

	private void driveRandomForward() {
		float randomLeftSpeed = (float) (Math.random() * this.STANDARD_SPEED);
		float randomRightSpeed = (float) (Math.random() * this.STANDARD_SPEED);
		int randomTime = (int) (Math.random() * this.TIMEINTERVALL + this.MIN_TIME);
		helperDriveForward(randomLeftSpeed, randomRightSpeed, randomTime);
	}

	private void helperDriveForward(float leftSpeed, float rightSpeed, int timeToDriveForward) {
		Stopwatch timer = new Stopwatch();
		Globals.motorControl.setLeftAndRightSpeed(leftSpeed, rightSpeed);
		while (timer.elapsed() < timeToDriveForward && !hasLineFound()) {
			//System.out.println("Touched");
			if (Globals.sensorBuffer.getLastMeasurementTouch()) {
				//Globals.motorControl.setLeftAndRightSpeed(this.BACKWARD_SPEED, this.BACKWARD_SPEED);
				driveBackwardWithConstantSpeed();
			    Delay.msDelay(DRIVE_BACKWARD_DELAY); // TODO maybe change non
														// blocking
				break;
			}
		}
		if (hasLineFound()) {
			// robot is over the line return in endboss area (turn around)
			//Globals.motorControl.setLeftAndRightSpeed(this.BACKWARD_SPEED, this.BACKWARD_SPEED);
		    driveBackwardWithConstantSpeed();
			Delay.msDelay(this.DRIVE_BACKWARD_DELAY * 5);
		}
	}

	private boolean hasLineFound() {
		return Globals.sensorBuffer.getLastMessurementColor() > this.BRIGHTNESS_THRESH;
	}
	
	private void driveBackwardWithConstantSpeed() {
	    Globals.motorControl.getLeftMotor().setSpeed(this.BACKWARD_SPEED);
        Globals.motorControl.getRightMotor().setSpeed(this.BACKWARD_SPEED);
        Globals.motorControl.getLeftMotor().backward();
        Globals.motorControl.getRightMotor().backward();
	}

}
