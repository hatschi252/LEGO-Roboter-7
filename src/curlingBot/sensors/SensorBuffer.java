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
    // TODO buffers
    private CyclicBuffer ultrasonicBuffer;
    private CyclicBuffer colorBuffer;
    private CyclicBuffer touchBuffer;
    private CyclicBuffer gyroBuffer;
    // locks
    private Object ultraSonicLock = new Object();
    private Object colorLock = new Object();
    private Object touchLock = new Object();
    private Object gyroLock = new Object();
    // true if touch detected something since last call
    private boolean pressedSinceLastCallBool = false;

    private SensorBuffer() {
        ultrasonicSensor = new EV3UltrasonicSensor(ULTRASONIC_SENSOR);
        colorSensor = new EV3ColorSensor(COLOR_SENSOR);
        touchSensor = new EV3TouchSensor(TOUCH_SENSOR);
        gyroSensor = new EV3GyroSensor(GYRO_SENSOR);

        // all Sensor samples are sized 1, except the gyro samples with size 2
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
        for (;;) { // ever
            synchronized (ultraSonicLock) {
                if (ultraSonicSensorActive) {
                    getSample(ultrasonicProvider, ultrasonicBuffer);
                }
            }
            synchronized (colorLock) {
                if (colorSensorActive) {
                    getSample(colorProvider, colorBuffer);
                }
            }
            synchronized (touchLock) {
                if (touchSensorActive) {
                    getSample(touchProvider, touchBuffer);
                    if (touchBuffer.getBuffer()[touchBuffer.getIndexOfLastInsertedElement()] == 1.0f) {
                        this.pressedSinceLastCallBool = true;
                    }
                }
            }
            synchronized (gyroLock) {
                if (gyroSensorActive) {
                    getSample(gyroProvider, gyroBuffer);
                }
            }

        }

    }

    private void getSample(SampleProvider sampleProvider, CyclicBuffer cBuffer) {
        cBuffer.incrementIndex();
        sampleProvider.fetchSample(cBuffer.getBuffer(), cBuffer.getIndexOfLastInsertedElement());

    }

    public float getLastMessurementUltraSonic() {
        synchronized (ultraSonicLock) {
            return ultrasonicBuffer.getBuffer()[ultrasonicBuffer.getIndexOfLastInsertedElement()];
        }
    }

    public float getLastMessurementColor() {
        synchronized (colorLock) {
            return colorBuffer.getBuffer()[colorBuffer.getIndexOfLastInsertedElement()];
        }
    }

    public boolean touchPressedSinceLastCall() {
        synchronized (touchLock) {
            if (pressedSinceLastCallBool) {
                pressedSinceLastCallBool = false;
                return true;
            } else {
                return false;
            }
        }
    }

    public float[] getLastMessurementGyro() {
        synchronized (gyroLock) {
            float[] resultArray = new float[2];
            resultArray[0] = gyroBuffer.getBuffer()[gyroBuffer.getIndexOfLastInsertedElement() - 1]; // second
                                                                                                     // last
                                                                                                     // element
                                                                                                     // is
                                                                                                     // needed

            resultArray[1] = gyroBuffer.getBuffer()[gyroBuffer.getIndexOfLastInsertedElement()];
            return resultArray;
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
