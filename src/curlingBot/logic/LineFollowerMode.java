package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MoveState;
import curlingBot.motorControl.PController;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class LineFollowerMode implements IMoveMode {
	private final int MAX_ACC = 100; // TODO test acc s
	private final int STANDARD_SPEED = 240;
	private final int LOST_LINE_TIMEOUT = 600;
	/*
	 * Used to turn left and right with 100 degrees
	 */
	private final int TACHO_COUNT_100_DEGREES = 100;

	private PController pController;
	private Stopwatch stopwatch = new Stopwatch();
	private boolean currentlyTurningLeft;

	@Override
	public void init() {
		Output.put("LineFollowerMode");
		Globals.sensorBuffer.setGyroSensorActive(true); // for see saw
		Globals.sensorBuffer.setUltraSonicSensorActive(false);
		Globals.sensorBuffer.setTouchSensorActive(false); // TODO check
		Globals.sensorBuffer.setColorSensorActive(true);
		currentlyTurningLeft = true;
	}

	private void rotate100(boolean left) {
		// drive backwards
		Globals.motorControl.getLeftMotor().stop();
		Globals.motorControl.getRightMotor().stop();
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		// turn 90° to the right
		if (!left) {
			Globals.motorControl.getLeftMotor().forward();
			Globals.motorControl.getRightMotor().backward();
		} else {
			Globals.motorControl.getLeftMotor().backward();
			Globals.motorControl.getRightMotor().forward();
		}
		Delay.msDelay(820);
		Globals.motorControl.getLeftMotor().stop();
		Globals.motorControl.getRightMotor().stop();
	}

	private void searchLine() {
		int currentLeftTacho = Globals.motorControl.getLeftMotor().getTachoCount();
		int currentRightTacho = Globals.motorControl.getRightMotor().getTachoCount();
		if (currentlyTurningLeft) {
			if (currentRightTacho < TACHO_COUNT_100_DEGREES) {
				Globals.motorControl.getRightMotor().forward();
				Globals.motorControl.getLeftMotor().backward();
			} else {
				Globals.motorControl.getRightMotor().stop();
				Globals.motorControl.getLeftMotor().stop();
				currentlyTurningLeft = false;
			}
		} else {
			if (currentRightTacho > -TACHO_COUNT_100_DEGREES ) {
				Globals.motorControl.getRightMotor().backward();
				Globals.motorControl.getLeftMotor().forward();
			} else {
				Globals.motorControl.getRightMotor().stop();
				Globals.motorControl.getLeftMotor().stop();
				currentlyTurningLeft = true;				
			}
		}
		
	}

	@Override
	public void perform() {
		// setup motors
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getLeftMotor().forward();
		Globals.motorControl.getRightMotor().forward();

		float kp = -0.8f;
		float speed0 = 100f;
		float low = 0.00f; // background
		float high = 0.3f; // line
		float midPoint = 0.5f;

		this.pController = new PController(kp, speed0, low, high, midPoint);
		for (;;) {
			float sensorInput = Globals.sensorBuffer.getLastMessurementColor();
			if (sensorInput > (low + high) / 2) {
				stopwatch.reset();
			}
			// Output.put(stopwatch.elapsed()+ ", " + sensorInput);
			if (stopwatch.elapsed() > LOST_LINE_TIMEOUT) {
				searchLine();
			} else {
				Globals.motorControl.getLeftMotor().resetTachoCount();
				Globals.motorControl.getRightMotor().resetTachoCount();
				// p-controlled line following
				float leftSpeed = pController.getSpeedLeft(sensorInput);
				float rightSpeed = pController.getSpeedRight(sensorInput);

				Globals.motorControl.getLeftMotor().setSpeed(leftSpeed);
				Globals.motorControl.getRightMotor().setSpeed(rightSpeed);
				Globals.motorControl.getLeftMotor().forward();
				Globals.motorControl.getRightMotor().forward();
			}
		}
	}
}
