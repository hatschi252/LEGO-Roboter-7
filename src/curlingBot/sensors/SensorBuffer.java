package curlingBot.sensors;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

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

    /**
     * bool is ture if data from the sensor will be collected
     */
    private boolean touchSensorActive = false;

    /**
     * bool is ture if data from the sensor will be collected
     */
    private boolean gyroSensorActive = false;

    /**
     * bool is ture if data from the sensor will be collected
     */
    private boolean ultraSonicSensorActive = false;

    /**
     * bool is ture if data from the sensor will be collected
     */
    private boolean colorSensorActive = false;

    /**
     * size of all buffers
     */
    private final int sizeOfBuffers = 10;

    /**
     * Port of the ultrasonic sensor
     */
    private final Port ULTRASONIC_SENSOR = SensorPort.S1;

    /**
     * Port of the color sensor
     */
    private final Port COLOR_SENSOR = SensorPort.S2;

    /**
     * Port of the touch sensor
     */
    private final Port TOUCH_SENSOR = SensorPort.S3;

    /**
     * Port of the Gyro sensor
     */
    private final Port GYRO_SENSOR = SensorPort.S4;
    
    
    private SensorBuffer() {
        //TODO init sensors
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ULTRASONIC_SENSOR);
        
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
    }
}
