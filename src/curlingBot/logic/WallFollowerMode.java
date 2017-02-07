package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class WallFollowerMode implements IMoveMode {

	private final int STANDARD_SPEED = 240;
	private final float LINE_BRIGHTNESS = 0.2f;

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
		float correctionFactor = 0.50f;
		while (!hasLineDetected()) { // infinity loop
			float currentWallDistance = Globals.sensorBuffer.getLastMessurementUltraSonic();
			if (currentWallDistance < 0.1f) {
				// robot is close to the wall
				//Globals.motorControl.getRightMotor().setSpeed((int) ((float) STANDARD_SPEED * correctionFactor));
				//Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
				Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED, STANDARD_SPEED * correctionFactor);
			} else if (currentWallDistance < 0.15f) {
				// the distance to the wall is acceptable
				//Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
				//Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
				Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED, STANDARD_SPEED);
			} else {
				// robot is too far away from the wall
				// Globals.motorControl.getLeftMotor().setSpeed((int) ((float) STANDARD_SPEED * correctionFactor));
				// Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
				Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED * correctionFactor, STANDARD_SPEED);
			}

			// check if touch was pressed -> 90° rotate to the right
			if (Globals.sensorBuffer.getLastMeasurementTouch()) {
				rotate();
			}
			
			Globals.motorControl.getLeftMotor().forward();
			Globals.motorControl.getRightMotor().forward();
			Delay.msDelay(50);
		}

		// after line detection
		findLine();
	}

	private void findLine() {
        // TODO Auto-generated method stub
        
    }

    private void rotate() {
		// drive backwards
		Globals.motorControl.getLeftMotor().stop();
		Globals.motorControl.getRightMotor().stop();
		Globals.motorControl.getLeftMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().setSpeed(STANDARD_SPEED);
		Globals.motorControl.getRightMotor().backward();
		Globals.motorControl.getLeftMotor().backward();
		Delay.msDelay(750);
		// turn 90° to the right
		Globals.motorControl.getLeftMotor().forward();
		Globals.motorControl.getRightMotor().backward();
		Delay.msDelay(750);
		// drive forward and continue to follow the wall
		Globals.motorControl.getRightMotor().forward();
	}

	private boolean hasLineDetected(){
		float currentBrightness = Globals.sensorBuffer.getLastMessurementColor();
		System.out.println("color: " + currentBrightness);
		return (currentBrightness > LINE_BRIGHTNESS);
	}
}
