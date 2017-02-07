package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import lejos.utility.Delay;

public class MazeMode implements IMoveMode {
	private final static int ROTATION_DELAY = 750;
	private final static int BACKWARD_DELAY = 200;
	private final static int STANDARD_SPEED = 240;
	private final static float LINE_BRIGHTNESS = 0.2f;
	private final static float CORRECTION_FACTOR = 0.35f;
	private final static float WALL_MIN_DISTANCE = 0.1f;
	private final static float WALL_ACCEPTABLE_DISTANCE = 0.15f;
	private final static int SLEEP_TIME = 50;
	

	@Override
	public void init() {
		Output.put("WallFollowerMode");
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setColorSensorActive(true);
	}

	@Override
	public void perform() {
		// setup motors
		Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED, STANDARD_SPEED);
		while (!hasLineDetected()) { // infinity loop
			float currentWallDistance = Globals.sensorBuffer.getLastMessurementUltraSonic();
			if (currentWallDistance < WALL_MIN_DISTANCE) {
				// robot is close to the wall
				//Globals.motorControl.getRightMotor().setSpeed((int) ((float) STANDARD_SPEED * correctionFactor));
				//Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
				Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED, STANDARD_SPEED * CORRECTION_FACTOR);
			} else if (currentWallDistance < WALL_ACCEPTABLE_DISTANCE) {
				// the distance to the wall is acceptable
				//Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
				//Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
				Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED, STANDARD_SPEED);
			} else {
				// robot is too far away from the wall
				// Globals.motorControl.getLeftMotor().setSpeed((int) ((float) STANDARD_SPEED * correctionFactor));
				// Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
				Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED * CORRECTION_FACTOR, STANDARD_SPEED);
			}

			// check if touch was pressed -> 90° rotate to the right
			if (Globals.sensorBuffer.getLastMeasurementTouch()) {
				rotate();
			}
			
			//Globals.motorControl.getLeftMotor().forward();
			//Globals.motorControl.getRightMotor().forward();
			Delay.msDelay(SLEEP_TIME);
		}

		// after line detection
	}


    private void rotate() {
		// drive backwards
		Globals.motorControl.getLeftMotor().stop();
		Globals.motorControl.getRightMotor().stop();
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().backward();
		Globals.motorControl.getLeftMotor().backward();
		Delay.msDelay(BACKWARD_DELAY);
		// turn 90° to the right
		Globals.motorControl.getLeftMotor().forward();
		Globals.motorControl.getRightMotor().backward();
		Delay.msDelay(ROTATION_DELAY);
		// drive forward and continue to follow the wall
		Globals.motorControl.getRightMotor().forward();
	}

	private boolean hasLineDetected(){
		float currentBrightness = Globals.sensorBuffer.getLastMessurementColor();
		System.out.println("color: " + currentBrightness);
		return (currentBrightness > LINE_BRIGHTNESS);
	}
}
