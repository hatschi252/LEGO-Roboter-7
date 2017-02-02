package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CliffDetectionTest {

    private final static int standardSpeedMotors = 150;
    private final static int sizeOfSampleArray = 10;

    private final static float CORRECTIOM_FACTOR_TO_RIGHT_TURN = 0.9f;
    private final static float CORRECTIOM_FACTOR_TO_LEFT_TURN = 0.8f;
    
    public static void main(String[] args) {
        // setup motors
        EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
        left.setSpeed(standardSpeedMotors);
        right.setSpeed(standardSpeedMotors);

        // setup ultrasonic sensor
        EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
        SampleProvider sampleProviderUltraSonic = ultraSonicSensor.getDistanceMode();
        float[] sampleValuesUltrasonic = new float[sizeOfSampleArray];

        // start exit thread
        Thread exitThread = new ExitThread();
        exitThread.start();

        // Behavior loop
        int i = 0;
        while (true) {
            //get value at position i
            sampleProviderUltraSonic.fetchSample(sampleValuesUltrasonic, i); 
            if (sampleValuesUltrasonic[i] < 0.1f) {
                //robot and ultrasonic sensor are on the bridge 
                //turn right slowly
                right.setSpeed(standardSpeedMotors);
                left.setSpeed(standardSpeedMotors * CORRECTIOM_FACTOR_TO_RIGHT_TURN);
            } else {
                // ultrasonic sensor is over the cliff
                // turn left 
                right.setSpeed(standardSpeedMotors * CORRECTIOM_FACTOR_TO_LEFT_TURN);
                left.setSpeed(standardSpeedMotors);
            }
            left.forward();
            right.forward();
            i = ++i % sizeOfSampleArray; // to use the sampleValues.. array cyclic
        }
    }

}
