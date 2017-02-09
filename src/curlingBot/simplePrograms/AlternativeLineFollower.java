package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Stopwatch;

public class AlternativeLineFollower {
    private static final int STANDART_SPEED = 50;
    private static final float TARGET_VALUE = (0.5f + 0.05f) / 2.0f;
//    private static final int CORRECTION_SPEED = 70; //
//    private static final int MAX_ALLOWED_TIME_LINE_LOST = 1500;//
//    private static final int CURVE_DEGREE = 360 * 3 / 2;//
    private static final int timeForSearch = 15500;
    // motor classes
    private static EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
    private static EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    // sensor classes
    private static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);

    // sample providers
    private static SampleProvider detector = colorSensor.getRedMode();

    // buffers
    private static float[] buffer = new float[10];

    // stopwatch
    private static Stopwatch stopwatch = new Stopwatch();

    public static void main(String[] args) {
        ExitThread exit = new ExitThread();
        exit.start();

        leftMotor.setSpeed(STANDART_SPEED);
        rightMotor.setSpeed(STANDART_SPEED);
        leftMotor.forward();
        rightMotor.forward();

        stopwatch.reset();
        followLine();
        // loop(buffer, left, right, detector);
    }

    private static void followLine() {
        while (true) {
            // the robot is trying to stay on the left side of the stripe
            // is robot on the line if not start timer and
            if (isNearToLine()) {
                // robot is over the line
                // drive left
                leftMotor.setSpeed(STANDART_SPEED / 2);
                rightMotor.setSpeed(STANDART_SPEED);
                stopwatch.reset();
            } else {
                // robot is left from the line
                // drive right
                leftMotor.setSpeed(STANDART_SPEED);
                rightMotor.setSpeed(STANDART_SPEED / 2);
            }
            leftMotor.forward();
            rightMotor.forward();
            if (stopwatch.elapsed() > 3000) {
                // we may lost the line -> search for it
                searchLine();
            }
        }
    }

    private static void searchLine() {
        stopwatch.reset();
        // stop motors
        leftMotor.stop();
        rightMotor.stop();
        // turn to the right
        leftMotor.setSpeed(STANDART_SPEED);
        leftMotor.forward();

        while (stopwatch.elapsed() < timeForSearch) {
            detector.fetchSample(buffer, 0);
            if (buffer[0] > TARGET_VALUE) {
                leftMotor.stop();
                stopwatch.reset();
                return;
            }
        }
        leftMotor.stop();
        // turn robot back
        leftMotor.backward();
        stopwatch.reset();
        while (stopwatch.elapsed() < timeForSearch) {

        }
        stopwatch.reset();
        // turn back and to the left
        rightMotor.setSpeed(STANDART_SPEED);
        rightMotor.forward();
        while (stopwatch.elapsed() < timeForSearch) {
            detector.fetchSample(buffer, 0);
            if (buffer[0] > TARGET_VALUE) {
                rightMotor.stop();
                stopwatch.reset();
                return;
            }
        }
        // here the line is lost completely

    }

    private static boolean isNearToLine() {
        detector.fetchSample(buffer, 0);
        float detectedValue = buffer[0];
        if (detectedValue < TARGET_VALUE) {
            // robot is not on the line or near it
            return false;
        } else {
            return true;
        }

    }

/*
    private static void loop(float[] buffer, EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right,
            SampleProvider detektor) {
        Stopwatch stopwatch = new Stopwatch();
        int cyclicCount = 0;
        for (;;) { // ever
            detektor.fetchSample(buffer, cyclicCount);
            if (buffer[cyclicCount] >= TARGET_VALUE * 1.05f) {
                left.setSpeed(STANDART_SPEED + CORRECTION_SPEED);
                right.setSpeed(STANDART_SPEED - CORRECTION_SPEED);
            } else if (buffer[cyclicCount] < TARGET_VALUE * 0.95f) {
                left.setSpeed(STANDART_SPEED - CORRECTION_SPEED);
                right.setSpeed(STANDART_SPEED + CORRECTION_SPEED);
            } else {
                left.setSpeed(STANDART_SPEED);
                right.setSpeed(STANDART_SPEED);
            }
            left.forward();
            right.forward();

            if (buffer[cyclicCount] > 0.3) {
                stopwatch.reset();
            }

            ifLineLostSearch(stopwatch, left, right, detektor, cyclicCount, buffer);
            cyclicCount = ++cyclicCount % 10;
        }

    }

    private static void ifLineLostSearch(Stopwatch stopwatch, EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right,
            SampleProvider detektor, int cyclicCount, float[] buffer) {
        // TODO Auto-generated method stub
        int timePassed = stopwatch.elapsed();
        if (timePassed > MAX_ALLOWED_TIME_LINE_LOST) {
            // line is lost. find it
            // stop motor
            left.stop();
            right.stop();
            // turn to the right and look if line detected
            left.setSpeed(STANDART_SPEED);
            left.rotate(CURVE_DEGREE, true);
            boolean lineFound = false;
            while (left.getSpeed() > 1) {
                detektor.fetchSample(buffer, cyclicCount);
                if (buffer[cyclicCount] > 0.3) {
                    left.stop();
                    lineFound = true;
                }
            }
            if (!lineFound) { // line not found on right side
                left.rotate(-CURVE_DEGREE);
                right.setSpeed(STANDART_SPEED);
                right.rotate(CURVE_DEGREE, true);
                detektor.fetchSample(buffer, cyclicCount);
                if (buffer[cyclicCount] > 0.3) {
                    right.stop();
                    lineFound = true;
                }
            }
            if (!lineFound) {
                // System.exit(1);
                // TODO add handle

            }
            stopwatch.reset();
            // turn to the left until line detected
        }

    }
*/
}