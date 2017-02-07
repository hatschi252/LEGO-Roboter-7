package curlingBot.logic;

import curlingBot.main.Globals;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class LineFinderMode implements IMoveMode {
    public static final int SEESAW_SPEED = 500;
    public static final int SEESAW_STEER = 0;
    private final float BRIGHTNESS_THRESHHOLD = 0.2f;

    private int leftSpeed;
    private int rightSpeed;
    private int driveForwardDelay;
    private int timeoutMilliSeconds;

    /**
     * Move mode which tries to find the line after an obstacle.
     * First the robot drives with the given speeds forward (without looking on the ground).
     * If the timeoutMil.. is smaller than zero the robot will continue to drive forward 
     * with the given speed (until it finds a line).
     * if the timeoutMil.. is greater than -1 the robot drives until it finds a line or the timer runs out.
     * @param vLeft Left speed
     * @param vRight Right speed
     * @param driveForwardDelay With this delay (in milliseconds) the robot will drive forward
     * @param timeoutMilliSeconds the robot stops after the time (timeoutMilliSeconds) runs out (or it found a line). 
     * (-1 for infinite time)
     */
    public LineFinderMode(int vLeft, int vRight, int driveForwardDelay, int timeoutMilliSeconds) {
        this.leftSpeed = vLeft;
        this.rightSpeed = vRight;
        this.driveForwardDelay = driveForwardDelay;
        this.timeoutMilliSeconds = timeoutMilliSeconds;
    }

    @Override
    public void init() {
        Globals.sensorBuffer.setColorSensorActive(true);
        Globals.sensorBuffer.setTouchSensorActive(false);
        Globals.sensorBuffer.setGyroSensorActive(false);
        Globals.sensorBuffer.setUltraSonicSensorActive(false);
    }

    @Override
    public void perform() {
        Globals.motorControl.setLeftAndRightSpeed(this.leftSpeed, this.rightSpeed);
        Delay.msDelay(this.driveForwardDelay);
        Stopwatch timer = new Stopwatch();
        if (this.timeoutMilliSeconds > -1) {
            while (Globals.sensorBuffer.getLastMessurementColor() < this.BRIGHTNESS_THRESHHOLD
                    && timer.elapsed() < this.timeoutMilliSeconds) {
                // do nothing
            }
        } else {
            while (Globals.sensorBuffer.getLastMessurementColor() < this.BRIGHTNESS_THRESHHOLD) {
                // do nothing
            }
        }

        // stop
        Globals.motorControl.setLeftAndRightSpeed(0, 0);
    }
}
