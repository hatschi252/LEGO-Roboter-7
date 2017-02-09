package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.PController;

public class SwampMode extends MoveMode {

	private final int TACHO_LIMIT = 360 * 3;
	private final static int STANDARD_ACC = 4000;

//	private final static float LINE_BRIGHTNESS = 0.2f;
	private float WALL_MIN_DISTANCE = 0.1f;
	private float WALL_MAX_DISTANCE = 0.15f;
	private final static float P_MIDPOINT = 0.3f;
	private final static float KP_VALUE = -0.6f;
	private final static int SLEEP_TIME = 20;
	private static final float STANDARD_SPEED = 500;
	
	private PController pController;
	
	public SwampMode(String description) {
		super(description);	pController = new PController(KP_VALUE, STANDARD_SPEED, WALL_MIN_DISTANCE, WALL_MAX_DISTANCE, P_MIDPOINT);

//		float desiredSpeed = Math.min(Globals.motorControl.getLeftMotor().getMaxSpeed(),
//				Globals.motorControl.getRightMotor().getMaxSpeed()) * 0.8f;
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setColorSensorActive(true);
	}

	@Override
	public void init() {
		Output.put("SwampMode");
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setColorSensorActive(false);

		Globals.motorControl.getLeftMotor().resetTachoCount();
		Globals.motorControl.getRightMotor().resetTachoCount();
	}

	@Override
	public void perform() {
		/*
		 * Before this mode we followed a line until there was another line in a
		 * 90° angle to the first line (not a curve). We can now assume that the
		 * robot is in front of the swamp and still going in the direction of
		 * the swamp. The idea is to go into the swamp with low speed so we get
		 * blocked by the first pole on the ground. Hopefully we are then
		 * aligned with the swamp. We should then be able to go straight
		 * backwards and then forward with full speed.
		 */
		// TODO: move in swamp mode
		
		// Globals.motorControl.getLeftMotor().startSynchronization();
		Globals.motorControl.getLeftMotor().setAcceleration(Integer.MAX_VALUE);
		Globals.motorControl.getRightMotor().setAcceleration(Integer.MAX_VALUE);
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getLeftMotor().forward();
		Globals.motorControl.getRightMotor().forward();
		Globals.motorControl.getLeftMotor().resetTachoCount();
		Globals.motorControl.getRightMotor().resetTachoCount();
		// Globals.motorControl.getLeftMotor().endSynchronization();
		long lastTimeMillis = System.currentTimeMillis();
		int totalTacho = 0;
		while (!Globals.sensorBuffer.getLastMeasurementTouch() && totalTacho < TACHO_LIMIT) {
			Globals.sleep(200);
			float timeStep = (System.currentTimeMillis() - lastTimeMillis) / 1000f;
			lastTimeMillis = System.currentTimeMillis();
			float tachoRightStep = Globals.motorControl.getRightMotor().getTachoCount();
			totalTacho += tachoRightStep;
			float tachoLeftStep = Globals.motorControl.getLeftMotor().getTachoCount();
			Globals.motorControl.getLeftMotor().resetTachoCount();
			Globals.motorControl.getRightMotor().resetTachoCount();

			float curVelocityRight = tachoRightStep / timeStep;
			float curVelocityLeft = tachoLeftStep / timeStep;

			Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED + (STANDARD_SPEED - curVelocityRight) / 10);
			Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED + (STANDARD_SPEED - curVelocityLeft) / 10);
			Globals.motorControl.getLeftMotor().forward();
			Globals.motorControl.getRightMotor().forward();
		}
		Output.beep();
		
		while (true) {
			followWall(Globals.sensorBuffer.getLastMessurementUltraSonic());
			Globals.sleep(SLEEP_TIME);
		}
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
