package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.PController;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Stopwatch;

public class LineFollowerMode extends MoveMode {

	private final int STANDARD_ACC = 4000; // TODO test acc s
	private final int TURN_ACC = 2000;
	private final int STANDARD_SPEED = 240;
	private final int TURN_SPEED = 180;
	private final int LOST_LINE_TIMEOUT = 600;
	private final int SLEEP_TIME_AFTER_TURN = 200;
	private final float LINE_SPEED  = 100f;
	

	private final static float P_MIDPOINT = 0.5f;
	private final static float KP_VALUE = -0.8f;
	private final static float DARK = 0;
	private final static float BRIGHT = 0.3f;
	

	/**
	 * After endo of line the robot is slightly turned right, this constant is
	 * used to correct that.
	 */
	private final int TURN_ERROR = 30;
	/*
	 * Used to turn left and right with 100 degrees
	 */
	private final int TACHO_COUNT_100_DEGREE_TURN = 150;
	private final int TACHO_COUNT_5CM = 60;

	private PController pController;
	private Stopwatch stopwatch = new Stopwatch();
	// private boolean currentlyTurningLeft;
	private SearchMode currentSearchMode = SearchMode.ON_LINE;

	private enum SearchMode {
		ON_LINE, LOOK_LEFT, LOOK_RIGHT, LOOK_FRONT, MOVE_FORWARD, TURN_CORRECTION, END;
	}
	
	public LineFollowerMode(String description) {
		super(description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		Globals.sensorBuffer.setGyroSensorActive(true); // for see saw
		Globals.sensorBuffer.setUltraSonicSensorActive(false);
		Globals.sensorBuffer.setTouchSensorActive(false); // TODO check
		Globals.sensorBuffer.setColorSensorActive(true);
		currentSearchMode = SearchMode.ON_LINE;
		// currentSearchMode = SearchMode.END;
	}

	private void searchLine() {
		if (currentSearchMode == SearchMode.ON_LINE) {
			currentSearchMode = SearchMode.TURN_CORRECTION;
			Globals.motorControl.getLeftMotor().resetTachoCount();
			Globals.motorControl.getRightMotor().resetTachoCount();
			Globals.motorControl.getLeftMotor().stop();
			Globals.motorControl.getRightMotor().stop();
			Globals.sleep(SLEEP_TIME_AFTER_TURN);
		}

		int currentRightTacho = Globals.motorControl.getRightMotor().getTachoCount();
		int currentLeftTacho = Globals.motorControl.getLeftMotor().getTachoCount();
		Globals.motorControl.getLeftMotor().setAcceleration(TURN_ACC);
		Globals.motorControl.getRightMotor().setAcceleration(TURN_ACC);
		Globals.motorControl.getLeftMotor().setSpeed(TURN_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(TURN_SPEED);

		Globals.motorControl.getLeftMotor()
				.synchronizeWith(new RegulatedMotor[] { Globals.motorControl.getRightMotor() });

		switch (currentSearchMode) {
		case TURN_CORRECTION:
			boolean bothDone = true;
			Globals.motorControl.getLeftMotor().startSynchronization();
			if (currentRightTacho < TURN_ERROR) {
				Globals.motorControl.getRightMotor().forward();
				bothDone = false;
			} else {
				Globals.motorControl.getRightMotor().stop();
			}
			if (currentLeftTacho > -TURN_ERROR) {
				Globals.motorControl.getLeftMotor().backward();
				bothDone = false;
			} else {
				Globals.motorControl.getLeftMotor().stop();
			}
			Globals.motorControl.getLeftMotor().endSynchronization();
			// turned back to straight, corrected turn error.
			if (bothDone) {
				Globals.motorControl.getLeftMotor().resetTachoCount();
				Globals.motorControl.getRightMotor().resetTachoCount();
				currentSearchMode = SearchMode.MOVE_FORWARD;
				Output.beep();
				Globals.sleep(SLEEP_TIME_AFTER_TURN);
//				Output.put("mode: " + currentSearchMode);
			}
			break;
		case MOVE_FORWARD:
			bothDone = true;
			Globals.motorControl.getLeftMotor().startSynchronization();
			if (currentRightTacho < TACHO_COUNT_5CM) {
				Globals.motorControl.getRightMotor().forward();
				bothDone = false;
			} else {
				Globals.motorControl.getRightMotor().stop();
			}
			if (currentLeftTacho < TACHO_COUNT_5CM) {
				Globals.motorControl.getLeftMotor().forward();
				bothDone = false;
			} else {
				Globals.motorControl.getLeftMotor().stop();
			}
			Globals.motorControl.getLeftMotor().endSynchronization();
			// turned back to straight, corrected turn error.
			if (bothDone) {
				currentSearchMode = SearchMode.LOOK_RIGHT;
				Output.beep();
				Globals.sleep(SLEEP_TIME_AFTER_TURN);
//				Output.put("mode: " + currentSearchMode);
			}
			break;
		case LOOK_LEFT:
			bothDone = true;
			Globals.motorControl.getLeftMotor().startSynchronization();
			if (currentRightTacho < TACHO_COUNT_100_DEGREE_TURN) {
				Globals.motorControl.getRightMotor().forward();
				bothDone = false;
			} else {
				Globals.motorControl.getRightMotor().stop();
				Globals.motorControl.getRightMotor().rotate(currentRightTacho - TACHO_COUNT_100_DEGREE_TURN);
			}
			if (currentLeftTacho > -TACHO_COUNT_100_DEGREE_TURN) {
				Globals.motorControl.getLeftMotor().backward();
				bothDone = false;
			} else {
				Globals.motorControl.getLeftMotor().stop();
				Globals.motorControl.getLeftMotor().rotate(currentLeftTacho - TACHO_COUNT_100_DEGREE_TURN);
			}
			Globals.motorControl.getLeftMotor().endSynchronization();
			// Both motors are done turning left, now turn back to straight.
			if (bothDone) {
				currentSearchMode = SearchMode.LOOK_FRONT;
				Output.beep();
				Globals.sleep(SLEEP_TIME_AFTER_TURN);
//				Output.put("mode: " + currentSearchMode);
			}
			break;
		case LOOK_RIGHT:
			bothDone = true;
			Globals.motorControl.getLeftMotor().startSynchronization();
			if (currentRightTacho > -TACHO_COUNT_100_DEGREE_TURN) {
				Globals.motorControl.getRightMotor().backward();
				bothDone = false;
			} else {
				Globals.motorControl.getRightMotor().stop();
				Globals.motorControl.getRightMotor().rotate(currentRightTacho - TACHO_COUNT_100_DEGREE_TURN);
			}
			if (currentLeftTacho < TACHO_COUNT_100_DEGREE_TURN) {
				Globals.motorControl.getLeftMotor().forward();
				bothDone = false;
			} else {
				Globals.motorControl.getLeftMotor().stop();
				Globals.motorControl.getLeftMotor().rotate(currentLeftTacho - TACHO_COUNT_100_DEGREE_TURN);
			}
			Globals.motorControl.getLeftMotor().endSynchronization();
			// Both motors are done turning left, now turn back to straight.
			if (bothDone) {
				currentSearchMode = SearchMode.LOOK_LEFT;
				Output.beep();
				Globals.sleep(SLEEP_TIME_AFTER_TURN);
//				Output.put("mode: " + currentSearchMode);
			}
			break;
		case LOOK_FRONT:
			bothDone = true;
			Globals.motorControl.getLeftMotor().startSynchronization();
			if (currentRightTacho > 0) {
				Globals.motorControl.getRightMotor().backward();
				bothDone = false;
			} else {
				Globals.motorControl.getRightMotor().rotate(currentRightTacho);
				Globals.motorControl.getRightMotor().stop();
			}
			if (currentLeftTacho < 0) {
				Globals.motorControl.getLeftMotor().forward();
				bothDone = false;
			} else {
				Globals.motorControl.getRightMotor().rotate(-currentLeftTacho);
				Globals.motorControl.getLeftMotor().stop();
			}
			Globals.motorControl.getLeftMotor().endSynchronization();
			// Both motors are done turning left, now turn back to straight.
			if (bothDone) {
				currentSearchMode = SearchMode.END;
				Output.beep();
				Globals.sleep(SLEEP_TIME_AFTER_TURN);
//				Output.put("mode: " + currentSearchMode);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void perform() {
		// setup motors
		Globals.motorControl.getLeftMotor().setAcceleration(STANDARD_ACC);
		Globals.motorControl.getRightMotor().setAcceleration(STANDARD_ACC);
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);


		this.pController = new PController(KP_VALUE, LINE_SPEED, DARK, BRIGHT, P_MIDPOINT);
		while (currentSearchMode != SearchMode.END) {
			float sensorInput = Globals.sensorBuffer.getLastMessurementColor();
			if (sensorInput > (DARK + BRIGHT) / 2) {
				stopwatch.reset();
			}
			// Output.put(stopwatch.elapsed()+ ", " + sensorInput);
			if (stopwatch.elapsed() > LOST_LINE_TIMEOUT) {
				searchLine();
			} else {
				stayOnLine(sensorInput);
			}
		}
	}

	private void stayOnLine(float sensorInput) {
		currentSearchMode = SearchMode.ON_LINE;
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
