package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.motorControl.MotorControl;
import curlingBot.motorControl.PController;
import curlingBot.sensors.SensorBuffer;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
/**
 * MoveMode to move over the swing bridge. robot drives along the wall.
 * If the wall is to far away (robot is on the bridge) it toggles the 
 * movable ultrasonic (us) sensor to detect the abyss. After a certain amount of the 
 * robot toggles the us sensor (to move it up again). Finally the robot
 * finds the wall again and finishes the obstacle.
 */
public class SwingBridgeMode extends MoveMode {

	private final int TIME_TO_CROSS_BRIDGE = 16000; // TODO find out time;
	private final int TIME_EXIT_THE_BRIDGE = 8000;
	private final int TIME_TO_DRIVE_BLIND = 4500;
	private final int SPEED_FOR_BLIND_DRIVE = 100;
//	private final float BRIGHTNESS_TRESH = 0.2f;
	
	private PController wallPC;
	private PController bridgePC;
	private SensorBuffer sensorBuffer;
	private MotorControl motorControl;

	public SwingBridgeMode(String description) {
		super(description);
	}

	@Override
	public void init() {
		wallPC = new PController(-0.8f, 100, 0.01f, 0.2f, 0.5f);
		bridgePC = new PController(1f, 80, 0.01f, 0.13f, 0.5f); // TODO werte
																	// genau
																	// abmessen
		sensorBuffer = Globals.sensorBuffer;
		sensorBuffer.setUltraSonicSensorActive(true);
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
		do { //follow the wall
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			leftSpeed = wallPC.getSpeedLeft(lastSensorValue);
			rightSpeed = wallPC.getSpeedRight(lastSensorValue);
	        motorControl.setLeftAndRightSpeed(leftSpeed, rightSpeed);
	        //Output.put("us: " + lastSensorValue);
		} while (/*!Float.isInfinite(lastSensorValue) || */!(lastSensorValue > 0.17f));
		Stopwatch timer = new Stopwatch();
		motorControl.setLeftAndRightSpeed(this.SPEED_FOR_BLIND_DRIVE * 1.27f, this.SPEED_FOR_BLIND_DRIVE);
		while (timer.elapsed() < this.TIME_TO_DRIVE_BLIND) {
		    //do nothing (robot drives forward)
		}
		timer.reset();    
		motorControl.moveUltrasonicDown(); //we should be on the bridge
		Delay.msDelay(100);
		//Delay.msDelay(20);
		
		while (timer.elapsed() < TIME_TO_CROSS_BRIDGE) { //cross the bridge
			lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
			leftSpeed = bridgePC.getSpeedLeft(lastSensorValue);
			rightSpeed = bridgePC.getSpeedRight(lastSensorValue);
            motorControl.setLeftAndRightSpeed(leftSpeed, rightSpeed);
		}
        motorControl.setLeftAndRightSpeed(100, 100);
        motorControl.moveUltrasonicUp(); //robot is over the bridge
        timer.reset();
        while (timer.elapsed() < this.TIME_EXIT_THE_BRIDGE) {
            // do nothing
        }
        // follow the wall
        timer.reset();
       /* do {
            lastSensorValue = sensorBuffer.getLastMessurementUltraSonic();
            leftSpeed = wallPC.getSpeedLeft(lastSensorValue);
            rightSpeed = wallPC.getSpeedRight(lastSensorValue);
            motorControl.setLeftAndRightSpeed(leftSpeed, rightSpeed);
        } while (!hasFoundLine());//timer.elapsed() < 10000);*/
        Globals.motorControl.setLeftAndRightSpeed(10, 10);
	}
	
//	private boolean hasFoundLine() {
//	    return Globals.sensorBuffer.getLastMessurementColor() > 0.35f;
//	}

}
