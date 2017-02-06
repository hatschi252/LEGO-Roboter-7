package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.motorControl.MotorControl;
import curlingBot.motorControl.MoveState;
import curlingBot.motorControl.PController;
import curlingBot.sensors.SensorBuffer;
import lejos.utility.Stopwatch;

public class SwingBridgeMode implements IMoveMode {
	
	private final int TIME_TO_CROSS_BRIDGE = 10; // TODO find out time;
	
	private PController wallPC;
	private PController bridgePC;
	private SensorBuffer sensorBuffer;
	private MotorControl motorControl;
	

	@Override
	public void init() {
		wallPC = new PController(0.8f, 200, 0.05f, 0.35f, 0.5f);
		bridgePC = new PController(-1f, 60, 0.05f, 0.30f, 0.5f); //TODO werte genau abmessen
		sensorBuffer = Globals.sensorBuffer;
		sensorBuffer.setUltraSonicSensorActive(true); //TODO set all other sensors to false
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
		motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(wallPC.getSpeed0(), wallPC.getSpeed0()), 6000);
		do
		{
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			leftSpeed = wallPC.getSpeedLeft(lastSensorValue);
			rightSpeed = wallPC.getSpeedRight(lastSensorValue);
			motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(leftSpeed, rightSpeed), 6000);
		}
		while(!Float.isInfinite(lastSensorValue));
		motorControl.moveUltrasonicDown();
		Stopwatch timer = new Stopwatch();
		while (timer.elapsed() < TIME_TO_CROSS_BRIDGE)
		{
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			leftSpeed = bridgePC.getSpeedLeft(lastSensorValue);
			rightSpeed = bridgePC.getSpeedRight(lastSensorValue);
			motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(leftSpeed, rightSpeed), 6000);
		}
		motorControl.setMoveState(MoveState.getMoveStateWithLeftAndRightSpeed(200, 200), 6000);
	}

}
