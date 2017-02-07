package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import curlingBot.motorControl.MoveState;
import curlingBot.motorControl.PController;
import curlingBot.sensors.SensorBuffer;
import lejos.utility.Stopwatch;

public class SwingBridgeMode implements IMoveMode {

	private final int TIME_TO_CROSS_BRIDGE = 20000; // TODO find out time;

	private PController wallPC;
	private PController bridgePC;
	private SensorBuffer sensorBuffer;
	private MotorControl motorControl;

	@Override
	public void init() {
		wallPC = new PController(-0.8f, 100, 0.01f, 0.15f, 0.5f);
		bridgePC = new PController(1f, 80, 0.01f, 0.15f, 0.5f); // TODO werte
																	// genau
																	// abmessen
		sensorBuffer = Globals.sensorBuffer;
		sensorBuffer.setUltraSonicSensorActive(true); // TODO set all other
														// sensors to false
		sensorBuffer.setTouchSensorActive(false);
		sensorBuffer.setGyroSensorActive(false);
		sensorBuffer.setColorSensorActive(false);
		motorControl = Globals.motorControl;
	}

	@Override
	public void perform() {
		float lastSensorValue;
		float leftSpeed;
		float rightSpeed;
		motorControl.setLeftAndRightSpeed(wallPC.getSpeed0(), wallPC.getSpeed0());
		do {
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			leftSpeed = wallPC.getSpeedLeft(lastSensorValue);
			rightSpeed = wallPC.getSpeedRight(lastSensorValue);
	        motorControl.setLeftAndRightSpeed(leftSpeed, rightSpeed);
		} while (!Float.isInfinite(lastSensorValue));
		Stopwatch timer = new Stopwatch();
		while (timer.elapsed() < 2000) {
		    
		}
		timer.reset();    
		motorControl.moveUltrasonicDown();
		
		while (timer.elapsed() < TIME_TO_CROSS_BRIDGE) {
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			Output.put("us: " + lastSensorValue);
			leftSpeed = bridgePC.getSpeedLeft(lastSensorValue);
			rightSpeed = bridgePC.getSpeedRight(lastSensorValue);
            motorControl.setLeftAndRightSpeed(leftSpeed, rightSpeed);
		}
        motorControl.setLeftAndRightSpeed(200, 200);
	}

}
