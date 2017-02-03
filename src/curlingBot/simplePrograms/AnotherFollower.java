package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class AnotherFollower {
	private static final int STANDART_SPEED = 50;
	private static final float TARGET_VALUE = (0.4f + 0.03f) / 2f;
	private static final int CORRECTION_SPEED = 25;
	
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
		int cyclicCount = 0;
		for (;;) { // ever
			detektor.fetchSample(buffer, cyclicCount);
			if (buffer[cyclicCount] >= TARGET_VALUE) {
				left.setSpeed(STANDART_SPEED + CORRECTION_SPEED);
				right.setSpeed(STANDART_SPEED - CORRECTION_SPEED);
			} else if (buffer[cyclicCount] < TARGET_VALUE) {
				left.setSpeed(STANDART_SPEED - CORRECTION_SPEED);
				right.setSpeed(STANDART_SPEED + CORRECTION_SPEED);
			}
			left.forward();
			right.forward();
			
			cyclicCount = ++cyclicCount % 10;
		}

	}

}
