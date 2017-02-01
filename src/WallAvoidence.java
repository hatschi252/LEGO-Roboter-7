import exitThread.ExitThreadRunnable;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class WallAvoidence {

    public static void main(String[] args) {
        
        // setup ultrasonic sensor
        EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
        SampleProvider ultraSampleProvider = ultraSonicSensor.getDistanceMode();
        float[] ultraSonicSampleValues = new float[10];

        // testSensor(sampleValues, ultraSample);

        // setup motors
        int standardSpeedMotors = 240;
        EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
        left.setSpeed(standardSpeedMotors);
        right.setSpeed(standardSpeedMotors);

        // setup touch sensor
        EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
        SampleProvider touchProvider = touchSensor.getTouchMode();
        float[] touchOutput = new float[1];
        touchProvider.fetchSample(touchOutput, 0);

        // Loop until robot is positioned correctly
        initPosition(ultraSampleProvider, ultraSonicSampleValues);
        // call startExitThread after initPosition!
        startExitThread(); // if button is pressed programm ends
        
        left.forward();
        right.forward();
        for(int i = 0; i < 10000; ++i) {
            ultraSampleProvider.fetchSample(ultraSonicSampleValues, 0);
            if (ultraSonicSampleValues[0] < 0.1f) {
                // robot is close to the wall
                right.setSpeed(standardSpeedMotors * 0.9f);
                left.setSpeed(standardSpeedMotors);
            } else if (ultraSonicSampleValues[0] < 0.23f) {
                // the distance to the wall is acceptable
                right.setSpeed(standardSpeedMotors);
                left.setSpeed(standardSpeedMotors);
                
            } else {
                // robot is too far away from the wall

                left.setSpeed(standardSpeedMotors * 0.9f);
                right.setSpeed(standardSpeedMotors);
            }
        }
       left.stop();
       right.stop();
        
       // System.out.print("Press Button.");

       ultraSonicSensor.close();
       left.close();
       right.close();
       touchSensor.close();
       // Button.waitForAnyPress();

    }

    private static void startExitThread() {
        Thread exitThread = new Thread(new ExitThreadRunnable());
        exitThread.start();
    }

    private static void initPosition(SampleProvider ultraSampleProvider, float[] sampleValues) {
        ultraSampleProvider.fetchSample(sampleValues, 0); // get distance in
                                                          // sam..[0]
        while (sampleValues[0] > 0.2f) {
            System.out.print("Too far away from wall. Press Button.");
            Button.waitForAnyPress();
            ultraSampleProvider.fetchSample(sampleValues, 0); // get distance in
                                                              // sam..[0]
        }

    }

    public static void testSensor(float[] sampleValues, SampleProvider ultraSampleProvider) {
        // test sensor
        for (int i = 0; i < sampleValues.length * 10; i++) {
            ultraSampleProvider.fetchSample(sampleValues, i % sampleValues.length);
            System.out.println(sampleValues[i % sampleValues.length]);
            Delay.msDelay(750);
        }
    }

    static void acc_forward(EV3LargeRegulatedMotor m_left, EV3LargeRegulatedMotor m_right) {
        m_left.setSpeed(m_left.getSpeed());
    }
}
