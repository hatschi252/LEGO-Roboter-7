package curlingBot.sensors;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * SensorBuffer collects sensor data automatically if the sensor is set active.
 * 
 * @author Robert Zimmermann, Leonard Otto
 *
 */
public final class SensorBuffer extends Thread {

    /**
     * Singleton instance
     */
    private static SensorBuffer sensorBufferInstance;
    // sensor active boolean
    private boolean touchSensorActive = false;
    private boolean gyroSensorActive = false;
    private boolean ultraSonicSensorActive = false;
    private boolean colorSensorActive = false;

    /**
     * size of all buffers
     */
    private final int sizeOfBuffers = 10;
    // Port
    private final Port ULTRASONIC_SENSOR = SensorPort.S1;
    private final Port COLOR_SENSOR = SensorPort.S2;
    private final Port TOUCH_SENSOR = SensorPort.S3;
    private final Port GYRO_SENSOR = SensorPort.S4;
    // sensor classes
    private EV3UltrasonicSensor ultrasonicSensor;
    private EV3ColorSensor colorSensor;
    private EV3TouchSensor touchSensor;
    private EV3GyroSensor gyroSensor;
    // sample provider
    private SampleProvider ultrasonicProvider;
    private SampleProvider colorProvider;
    private SampleProvider touchProvider;
    private SampleProvider gyroProvider;
    //TODO buffers
    private CyclicBuffer ultrasonicBuffer;
    private CyclicBuffer colorBuffer;
    private CyclicBuffer touchBuffer;
    private CyclicBuffer gyroBuffer;
    

    private SensorBuffer() {
        ultrasonicSensor = new EV3UltrasonicSensor(ULTRASONIC_SENSOR);
        colorSensor = new EV3ColorSensor(COLOR_SENSOR);
        touchSensor = new EV3TouchSensor(TOUCH_SENSOR);
        gyroSensor = new EV3GyroSensor(GYRO_SENSOR);
        
        //all Sensor samples are sized 1, except the gyro samples with size 2
        ultrasonicBuffer = new CyclicBuffer(sizeOfBuffers, 1);
        colorBuffer = new CyclicBuffer(sizeOfBuffers, 1);
        touchBuffer = new CyclicBuffer(sizeOfBuffers, 1);
        gyroBuffer = new CyclicBuffer(sizeOfBuffers * 2, 2);
        
        ultrasonicProvider = ultrasonicSensor.getDistanceMode();
        colorProvider = colorSensor.getRedMode();
        touchProvider = touchSensor.getTouchMode();
        gyroProvider = gyroSensor.getAngleAndRateMode();
    }

    public static SensorBuffer getInstance() {
        if (sensorBufferInstance == null) {
            sensorBufferInstance = new SensorBuffer();
        }
        return sensorBufferInstance;
    }

    @Override
    public void run() {
        // TODO implement
    	for(;;) { //ever
    		if (ultraSonicSensorActive) {
    			//TODO implement fetch
			}
    		if (colorSensorActive) {
				//TODO implement fetch
			}
    		if (touchSensorActive) {
				//TODO implement fetch
			}
    		if (gyroSensorActive) {
				//TODO implement fetch
			}
    	}

    }

    public boolean isTouchSensorActive() {
        return touchSensorActive;
    }

    public void setTouchSensorActive(boolean touchSensorActive) {
        this.touchSensorActive = touchSensorActive;
    }

    public boolean isGyroSensorActive() {
        return gyroSensorActive;
    }

    public void setGyroSensorActive(boolean gyroSensorActive) {
        this.gyroSensorActive = gyroSensorActive;
    }

    public boolean isUltraSonicSensorActive() {
        return ultraSonicSensorActive;
    }

    public void setUltraSonicSensorActive(boolean ultraSonicSensorActive) {
        this.ultraSonicSensorActive = ultraSonicSensorActive;
    }

    public boolean isColorSensorActive() {
        return colorSensorActive;
    }

    public void setColorSensorActive(boolean colorSensorActive) {
        this.colorSensorActive = colorSensorActive;
    }

}
