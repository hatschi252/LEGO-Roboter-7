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
		// TODO Auto-generated method stub
		float lastSensorValue;
		float leftSpeed;
		float rightSpeed;
		//motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(wallPC.getSpeed0(), wallPC.getSpeed0()),
		//		6000);
		motorControl.getLeftMotor().setSpeed(wallPC.getSpeed0());
		motorControl.getRightMotor().setSpeed(wallPC.getSpeed0());
		motorControl.getLeftMotor().forward();
		motorControl.getRightMotor().forward();
		do {
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			leftSpeed = wallPC.getSpeedLeft(lastSensorValue);
			rightSpeed = wallPC.getSpeedRight(lastSensorValue);
			//motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(leftSpeed, rightSpeed), 6000);
			motorControl.getLeftMotor().setSpeed(leftSpeed);
	        motorControl.getRightMotor().setSpeed(rightSpeed);
	        motorControl.getLeftMotor().forward();
	        motorControl.getRightMotor().forward();
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
			//motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(leftSpeed, rightSpeed), 6000);
			motorControl.getLeftMotor().setSpeed(leftSpeed);
            motorControl.getRightMotor().setSpeed(rightSpeed);
            motorControl.getLeftMotor().forward();
            motorControl.getRightMotor().forward();
		}
		motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(200, 200), 6000);
		motorControl.getLeftMotor().setSpeed(200);
        motorControl.getRightMotor().setSpeed(200);
        motorControl.getLeftMotor().forward();
        motorControl.getRightMotor().forward();
	}

}
