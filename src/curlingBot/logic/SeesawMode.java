package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import lejos.robotics.RegulatedMotor;

public class SeesawMode implements IMoveMode {
	private final static int DISTANCE = 4 * 360;
	private final static int SPEED = 500;

	@Override
	public void init() {
		Output.put("SeesawMode");
        Globals.sensorBuffer.setColorSensorActive(false);
        Globals.sensorBuffer.setTouchSensorActive(false);
        Globals.sensorBuffer.setGyroSensorActive(false);
        Globals.sensorBuffer.setUltraSonicSensorActive(false);
	}

	@Override
	public void perform() {		
		Globals.motorControl.getLeftMotor()
		.synchronizeWith(new RegulatedMotor[] { Globals.motorControl.getRightMotor() });
		
		Globals.motorControl.getLeftMotor().setSpeed(SPEED);
		Globals.motorControl.getRightMotor().setSpeed(SPEED);
		
		Globals.motorControl.getLeftMotor().startSynchronization();

		Globals.motorControl.getLeftMotor().rotate(DISTANCE);
		Globals.motorControl.getRightMotor().rotate(DISTANCE);
		
		Globals.motorControl.getLeftMotor().endSynchronization();
		Globals.motorControl.getLeftMotor().waitComplete();
		Globals.motorControl.getRightMotor().waitComplete();
		
		
	}

}
