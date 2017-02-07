package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.PController;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MazeMode implements IMoveMode {
	private final static int STANDARD_ACC = 4000;
	private final static int ROTATION_ANGLE = 180;
	private final static int BACKWARD_DISTANCE = 70;
	private final static int STANDARD_SPEED = 200;
	private final static float LINE_BRIGHTNESS = 0.2f;
	private final static float WALL_MIN_DISTANCE = 0.1f;
	private final static float WALL_MAX_DISTANCE = 0.15f;
	private final static float P_MIDPOINT = 0.3f;
	private final static float KP_VALUE = -0.3f;
	private final static int SLEEP_TIME = 20;

	private PController pController;

	@Override
	public void init() {
		Output.put("WallFollowerMode");
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setColorSensorActive(true);

		pController = new PController(KP_VALUE, STANDARD_SPEED, WALL_MIN_DISTANCE, WALL_MAX_DISTANCE, P_MIDPOINT);
	}

	@Override
	public void perform() {
		while (!hasLineDetected()) {
			while (!Globals.sensorBuffer.getLastMeasurementTouch()) {
				followWall(Globals.sensorBuffer.getLastMessurementUltraSonic());
				Globals.sleep(SLEEP_TIME);
			}
			rotate();
		}
		// end of maze, TODO: move a few cm straight, to overcome the detected
		// mark
	}

	private void rotate() {
		Globals.motorControl.getLeftMotor()
		.synchronizeWith(new RegulatedMotor[] { Globals.motorControl.getRightMotor() });
		
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		
		Globals.motorControl.getLeftMotor().startSynchronization();
		Globals.motorControl.getLeftMotor().rotate(-BACKWARD_DISTANCE);
		Globals.motorControl.getRightMotor().rotate(-BACKWARD_DISTANCE);
		Globals.motorControl.getLeftMotor().endSynchronization();
		Globals.motorControl.getLeftMotor().waitComplete();
		Globals.motorControl.getRightMotor().waitComplete();
		
		Globals.motorControl.getLeftMotor().startSynchronization();
		Globals.motorControl.getLeftMotor().rotate(ROTATION_ANGLE);
		Globals.motorControl.getRightMotor().rotate(-ROTATION_ANGLE);
		Globals.motorControl.getLeftMotor().endSynchronization();
		Globals.motorControl.getLeftMotor().waitComplete();
		Globals.motorControl.getRightMotor().waitComplete();
		
//		// drive backwards
//		Globals.motorControl.getLeftMotor().stop();
//		Globals.motorControl.getRightMotor().stop();
//		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
//		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
//		Globals.motorControl.getRightMotor().backward();
//		Globals.motorControl.getLeftMotor().backward();
//		Delay.msDelay(BACKWARD_DELAY);
//		// turn 90° to the right
//		Globals.motorControl.getLeftMotor().forward();
//		Globals.motorControl.getRightMotor().backward();
//		Delay.msDelay(ROTATION_DELAY);
//		// drive forward and continue to follow the wall
//		Globals.motorControl.getRightMotor().forward();
	}

	private boolean hasLineDetected() {
		float currentBrightness = Globals.sensorBuffer.getLastMessurementColor();
		System.out.println("color: " + currentBrightness);
		return (currentBrightness > LINE_BRIGHTNESS);
	}

	private void followWall(float sensorInput) {
		Globals.motorControl.getLeftMotor().setAcceleration(STANDARD_ACC);
		Globals.motorControl.getRightMotor().setAcceleration(STANDARD_ACC);
		// p-controlled line following
		float leftSpeed = pController.getSpeedLeft(sensorInput);
		float rightSpeed = pController.getSpeedRight(sensorInput);

		Globals.motorControl.getLeftMotor().setSpeed(leftSpeed);
		Globals.motorControl.getRightMotor().setSpeed(rightSpeed);
		Globals.motorControl.getLeftMotor().forward();
		Globals.motorControl.getRightMotor().forward();
	}
}
