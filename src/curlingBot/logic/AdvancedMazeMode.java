package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.PController;
import lejos.robotics.RegulatedMotor;

/**
 * A maze moveMode with a pController and collision detection.
 * 
 *
 */
public class AdvancedMazeMode extends MoveMode {

	private final static int STANDARD_ACC = 4000;

	// with 45°
	// private final static int ROTATION_ANGLE = 90; // 180=90°
	// private final static int BACKWARD_DISTANCE = 70;
	// private final static int FORWARD_DISTANCE = 130;
	// private final static int STANDARD_SPEED = 420;
	// private final static int SLOW_SPEED = 220;

	// without 45° (only right angles) TODO: experimental values, does not work
	// yet
	private final static int ROTATION_ANGLE = 90; // 180=90°
	private final static int BACKWARD_DISTANCE = 50;
	private final static int FORWARD_DISTANCE = 0;
	private int STANDARD_SPEED = 420;
	private final int SLOW_SPEED = STANDARD_SPEED;

	private final static float LINE_BRIGHTNESS = 0.2f;
	private float WALL_MIN_DISTANCE = 0.1f;
	private float WALL_MAX_DISTANCE = 0.15f;
	private final static float P_MIDPOINT = 0.3f;
	private final static float KP_VALUE = -0.3f;
	private final static int SLEEP_TIME = 20;

	private PController pController;

	public AdvancedMazeMode(String description) {
		super(description);
	}

	public AdvancedMazeMode(String description, int speed, float maxWallDistance, float minWallDistance) {
		super(description);
		this.STANDARD_SPEED = speed;
		this.WALL_MAX_DISTANCE = maxWallDistance;
		this.WALL_MIN_DISTANCE = minWallDistance;
	}

	@Override
	public void init() {
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setColorSensorActive(true);

		pController = new PController(KP_VALUE, STANDARD_SPEED, WALL_MIN_DISTANCE, WALL_MAX_DISTANCE, P_MIDPOINT);
	}

	@Override
	public void perform() {
		while (!hasLineDetected()) {
			while (!Globals.sensorBuffer.getLastMeasurementTouch() && !hasLineDetected()) {
				followWall(Globals.sensorBuffer.getLastMessurementUltraSonic());
				Globals.sleep(SLEEP_TIME);
			}
			if (hasLineDetected())
				break;
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

		Globals.motorControl.getLeftMotor().setSpeed(SLOW_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(SLOW_SPEED);
		Globals.motorControl.getLeftMotor().startSynchronization();
		Globals.motorControl.getLeftMotor().rotate(FORWARD_DISTANCE);
		Globals.motorControl.getRightMotor().rotate(FORWARD_DISTANCE);
		Globals.motorControl.getLeftMotor().endSynchronization();
		Globals.motorControl.getLeftMotor().waitComplete();
		Globals.motorControl.getRightMotor().waitComplete();
		// // drive backwards
		// Globals.motorControl.getLeftMotor().stop();
		// Globals.motorControl.getRightMotor().stop();
		// Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		// Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		// Globals.motorControl.getRightMotor().backward();
		// Globals.motorControl.getLeftMotor().backward();
		// Delay.msDelay(BACKWARD_DELAY);
		// // turn 90° to the right
		// Globals.motorControl.getLeftMotor().forward();
		// Globals.motorControl.getRightMotor().backward();
		// Delay.msDelay(ROTATION_DELAY);
		// // drive forward and continue to follow the wall
		// Globals.motorControl.getRightMotor().forward();
	}

	private boolean hasLineDetected() {
		float currentBrightness = Globals.sensorBuffer.getLastMessurementColor();
		// Output.put("color: " + currentBrightness);
		return (currentBrightness > LINE_BRIGHTNESS);
	}

	private void followWall(float sensorInput) {
		Globals.motorControl.getLeftMotor().setAcceleration(STANDARD_ACC);
		Globals.motorControl.getRightMotor().setAcceleration(STANDARD_ACC);
		// p-controlled line following
		float leftSpeed = pController.getSpeedLeft(sensorInput);
		float rightSpeed = pController.getSpeedRight(sensorInput);

		// Output.put("maxSpeed"+(leftSpeed-pController.getSpeed0()));
		// Output.put("maxSpeed"+(rightSpeed-pController.getSpeed0()));

		Globals.motorControl.getLeftMotor().setSpeed(leftSpeed);
		Globals.motorControl.getRightMotor().setSpeed(rightSpeed);
		Globals.motorControl.getLeftMotor().forward();
		Globals.motorControl.getRightMotor().forward();
	}
}
