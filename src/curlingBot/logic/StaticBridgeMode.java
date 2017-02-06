package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;

public class StaticBridgeMode implements IMoveMode {

    private final int SPEED = 100;
    private final float CORRECTION_FACTOR_TURN_RIGHT = 0.6f; // must be between
                                                             // 0 and 1
    private final float CORRECTION_FACTOR_TURN_LEFT = 0.7f; // must be between 0
                                                            // and 1

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
        // on bridge turn ultra sonic sensor
        Globals.motorControl.moveUltrasonicDown();
        // TODO check if the robot can find the edge of the bridge
        while (true) {
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
            // TODO check when to brake the loop
            // TODO add gyro
        }
        // TODO turn up ultrasonic sensor 
        // TODO check if robot can find line after the bridge
    }

}
