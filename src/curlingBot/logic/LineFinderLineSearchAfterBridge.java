package curlingBot.logic;

import curlingBot.main.Globals;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

/**
 * The robot has not always the same pose after the bridge. This 
 * MoveMode searches for the line to follow.
 * 
 *
 */
public class LineFinderLineSearchAfterBridge extends MoveMode {

	private final int TURN_SPEED = 150;
    private final int DURATION_TURN = 1500;
    private final float BRIGHTNESS_THRESHHOLD = 0.2f;
    private final int TIME_MOVE_FORWARD_BLIND = 750;
    private final int SPEED_BLIND_DRIVE = 150;

    public LineFinderLineSearchAfterBridge(String description) {
		super(description);
	}
    
    @Override
    public void init() {
        Globals.sensorBuffer.setColorSensorActive(true);
        Globals.sensorBuffer.setGyroSensorActive(false);
        Globals.sensorBuffer.setUltraSonicSensorActive(false);
        Globals.sensorBuffer.setTouchSensorActive(false);
        
    }

    @Override
    public void perform() {
        Stopwatch timer = new Stopwatch();
        Globals.motorControl.setLeftAndRightSpeed(this.SPEED_BLIND_DRIVE, this.SPEED_BLIND_DRIVE);
        Delay.msDelay(this.TIME_MOVE_FORWARD_BLIND);
        timer.reset();
        // make a right turn (with one wheel)
        Globals.motorControl.setLeftAndRightSpeed(this.TURN_SPEED, 0);
        boolean foundLine = foundLine();
        while (!foundLine && timer.elapsed() < this.DURATION_TURN) {
            foundLine = foundLine();
        }
        if (foundLine) {
            Globals.motorControl.setLeftAndRightSpeed(0, 0); //stop
            return;
        }
        // don't turn back and make a left bigger turn (with one wheel)
        timer.reset();
        Globals.motorControl.setLeftAndRightSpeed(0, this.TURN_SPEED);
        while (!foundLine && timer.elapsed() < this.DURATION_TURN * 3) {
            foundLine = foundLine();
        }
        if (foundLine) {
            Globals.motorControl.setLeftAndRightSpeed(0, 0);
            return;
        }
        // small right turn
        timer.reset();
        Globals.motorControl.setLeftAndRightSpeed(this.TURN_SPEED, 0);
        while (!foundLine && timer.elapsed() < this.DURATION_TURN) {
            foundLine = foundLine();
        }
        if (foundLine) {
            Globals.motorControl.setLeftAndRightSpeed(0, 0); //stop
            return;
        }
    }

    private boolean foundLine() {
        return Globals.sensorBuffer.getLastMessurementColor() > this.BRIGHTNESS_THRESHHOLD;

    }

}
