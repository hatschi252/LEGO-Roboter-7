package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Stopwatch;

public class AlternativeLineFollower {
	private static final int STANDART_SPEED = 400;
	private static final float TARGET_VALUE = (0.6f + 0.3f) / 2f;
	private static final int CORRECTION_SPEED = 70;
	private static final int MAX_ALLOWED_TIME_LINE_LOST = 1500;
	private static final int CURVE_DEGREE = 360 * 3/2;

	public static void main(String[] args) {
		ExitThread exit = new ExitThread();
		exit.start();

		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
		SampleProvider detektor = colorSensor.getRedMode();
		float[] buffer = new float[10];

		EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
		left.setSpeed(STANDART_SPEED);
		right.setSpeed(STANDART_SPEED);
		left.forward();
		right.forward();

		loop(buffer, left, right, detektor);

	}

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
				System.exit(1);
			}
			stopwatch.reset();
			// turn to the left until line detected
		}

	}

}
