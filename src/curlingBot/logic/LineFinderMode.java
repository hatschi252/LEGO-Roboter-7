package curlingBot.logic;

import curlingBot.main.Globals;
import lejos.utility.Delay;

public class LineFinderMode implements IMoveMode {
	public static final int SEESAW_SPEED = 500;
	public static final int SEESAW_STEER = 0;
    private final float BRIGHTNESS_THRESHHOLD = 0.2f;
	
	private int speed;
	private float steer;
	
	public LineFinderMode(int vLeft, int vRight) {
		speed = (vLeft + vRight) / 2;
		//TODO: CALC STEER
	}
	
	public LineFinderMode(int speed, float steer) {
		this.speed = speed;
		this.steer = steer;
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
        Globals.motorControl.setLeftAndRightSpeed(this.speed, this.speed);
        Delay.msDelay(750);
        Globals.motorControl.setLeftAndRightSpeed(speed, speed * 0.9f);
        while (Globals.sensorBuffer.getLastMessurementColor() < this.BRIGHTNESS_THRESHHOLD) {
            // do nothing
        }
        Globals.motorControl.setLeftAndRightSpeed(0, 0);
    }
}
