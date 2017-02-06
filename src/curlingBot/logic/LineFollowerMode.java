package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MoveState;
import curlingBot.motorControl.PController;

public class LineFollowerMode implements IMoveMode {

	private PController pController;
	private final int maxAcc = 100; // TODO test acc s
	private final int STANDARD_SPEED = 240;

	@Override
	public void init() {
		Output.put("Enter LineFollowerMode");
		Globals.sensorBuffer.setGyroSensorActive(true); // for see saw
		Globals.sensorBuffer.setUltraSonicSensorActive(false);
		Globals.sensorBuffer.setTouchSensorActive(false); // TODO check
		Globals.sensorBuffer.setColorSensorActive(true);

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
			float leftSpeed = pController.getSpeedLeft(sensorInput);
			float rightSpeed = pController.getSpeedRight(sensorInput);

			Globals.motorControl.getLeftMotor().setSpeed(leftSpeed);
			Globals.motorControl.getRightMotor().setSpeed(rightSpeed);
			Globals.motorControl.getLeftMotor().forward();
			Globals.motorControl.getRightMotor().forward();
			// TODO add end of line
		}
	}
}
