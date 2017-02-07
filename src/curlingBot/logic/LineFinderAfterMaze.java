package curlingBot.logic;

import curlingBot.main.Globals;
import lejos.utility.Delay;

public class LineFinderAfterMaze implements IMoveMode {

    private final int STANDARD_SPEED = 100;
    private final float BRIGHTNESS_THRESHHOLD = 0.2f;

    @Override
    public void init() {
        Globals.sensorBuffer.setColorSensorActive(true);
        Globals.sensorBuffer.setTouchSensorActive(false);
        Globals.sensorBuffer.setGyroSensorActive(false);
        Globals.sensorBuffer.setUltraSonicSensorActive(false);
    }

    @Override
    public void perform() {
        Globals.motorControl.setLeftAndRightSpeed(this.STANDARD_SPEED, this.STANDARD_SPEED);
        Delay.msDelay(750);
        Globals.motorControl.setLeftAndRightSpeed(STANDARD_SPEED, STANDARD_SPEED * 0.9f);
        while (Globals.sensorBuffer.getLastMessurementColor() < this.BRIGHTNESS_THRESHHOLD) {
            // do nothing
        }
        Globals.motorControl.setLeftAndRightSpeed(0, 0);
    }

}
