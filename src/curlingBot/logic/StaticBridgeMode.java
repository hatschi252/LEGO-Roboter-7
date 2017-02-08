package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import lejos.utility.Delay;

public class StaticBridgeMode extends MoveMode {

	private final int SPEED = 100;
    private final float CORRECTION_FACTOR_TURN_RIGHT = 0.6f; // must be between
                                                             // 0 and 1
    private final float CORRECTION_FACTOR_TURN_LEFT = 0.7f; // must be between 0
                                                            // and 1
    private final int DELAY_TO_GET_ON_BRIDGE = 4000;
    private final float BRIGHTNESS_THRESHHOLD = 0.2f;

    public StaticBridgeMode(String description) {
		super(description);
	}
    
    @Override
    public void init() {
        Output.put("Enter StaticBridgeMode");
        Globals.sensorBuffer.setColorSensorActive(true);
        Globals.sensorBuffer.setGyroSensorActive(true);
        Globals.sensorBuffer.setUltraSonicSensorActive(true);
        Globals.sensorBuffer.setTouchSensorActive(false);
    }

    @Override
    public void perform() {
        // robot is infront of the bridge
        Globals.motorControl.setLeftAndRightSpeed(SPEED, SPEED);
        Delay.msDelay(DELAY_TO_GET_ON_BRIDGE);
        // on bridge turn ultra sonic sensor
        Globals.motorControl.moveUltrasonicDown();
        // TODO check if the robot can find the edge of the bridge
        while (Globals.sensorBuffer.getLastMessurementColor() < this.BRIGHTNESS_THRESHHOLD) {
            if (Globals.sensorBuffer.getLastMessurementUltraSonic() < 0.1f) {
                // robot and ultrasonic sensor are on the bridge

                // turn left
                Globals.motorControl.getRightMotor().setSpeed(SPEED);
                Globals.motorControl.getLeftMotor().setSpeed(SPEED * CORRECTION_FACTOR_TURN_LEFT);
            } else {
                // ultrasonic sensor is over the cliff

                // turn right
                Globals.motorControl.getRightMotor().setSpeed(SPEED * CORRECTION_FACTOR_TURN_RIGHT);
                Globals.motorControl.getLeftMotor().setSpeed(SPEED);
            }
            Globals.motorControl.getRightMotor().forward();
            Globals.motorControl.getLeftMotor().forward();
        }
        // turn up ultrasonic sensor 
        Globals.motorControl.moveUltrasonicUp();
    }

}
