package curlingBot.simplePrograms;

import java.util.Arrays;

import curlingBot.main.ExitThread;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class AnotherFollower {
	private static final int STANDART_SPEED = 50;
	private static float TARGET_VALUE;
	private static final int CORRECTION_SPEED = 25;
	
	public static void main(String[] args) {
		ExitThread exit = new ExitThread();
		exit.start();

		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
		SampleProvider detektor = colorSensor.getRedMode();
		float[] buffer = new float[10];

		EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
		calibrateSensor(left, right, detektor);
		left.setSpeed(STANDART_SPEED);
		right.setSpeed(STANDART_SPEED);
		left.forward();
		right.forward();

		loop(buffer, left, right, detektor);
		colorSensor.close();
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
	private static void calibrateSensor(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right, SampleProvider detektor) {
		left.setSpeed(50);
		right.setSpeed(50);
		float[] largeBuffer = new float[1000000];
		Arrays.fill(largeBuffer, 0);
		int i = 0;
		left.rotate(90, true);
		int tachoC = right.getTachoCount();
		right.rotate(-90, true);
		while (right.getTachoCount() > tachoC - 88)
		{
			detektor.fetchSample(largeBuffer, i++);
		}
		left.rotate(-180, true);
		right.rotate(180, true);
		tachoC = right.getTachoCount();
		while(right.getTachoCount() < tachoC + 178) {
			detektor.fetchSample(largeBuffer, i++);
		}
		left.rotate(90,true);
		right.rotate(-90,true);
		float max = 0;
		float min = 2;
		for (float j: largeBuffer)
		{
			max = j > max ? j : max;
			min = j != 0 && j < min ? j : min;
		}
		System.out.println("Max:" + max + "\nMin:" + min);
		TARGET_VALUE = (max + min) / 2f;
		Button.waitForAnyPress();
	}

}
