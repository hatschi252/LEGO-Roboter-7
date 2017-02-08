package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;

public class SwampMode extends MoveMode {

	private final int TACHO_LIMIT = 360 * 2;
	
	public SwampMode(String description) {
		super(description);
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
		float desiredSpeed = Math.min(Globals.motorControl.getLeftMotor().getMaxSpeed(),
				Globals.motorControl.getRightMotor().getMaxSpeed()) * 0.8f;
		// Globals.motorControl.getLeftMotor().startSynchronization();
		Globals.motorControl.getLeftMotor().setAcceleration(Integer.MAX_VALUE);
		Globals.motorControl.getRightMotor().setAcceleration(Integer.MAX_VALUE);
		Globals.motorControl.getLeftMotor().setSpeed(desiredSpeed);
		Globals.motorControl.getRightMotor().setSpeed(desiredSpeed);
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

			Globals.motorControl.getRightMotor().setSpeed(desiredSpeed + (desiredSpeed - curVelocityRight) / 10);
			Globals.motorControl.getLeftMotor().setSpeed(desiredSpeed + (desiredSpeed - curVelocityLeft) / 10);
			Globals.motorControl.getLeftMotor().forward();
			Globals.motorControl.getRightMotor().forward();
		}
		Output.beep();
		
		//Globals.motorControl.getLeftMotor().stop();
		//Globals.motorControl.getRightMotor().stop();
	}

}
