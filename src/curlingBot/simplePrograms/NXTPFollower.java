package curlingBot.simplePrograms;

import curlingBot.main.ExitThread;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class NXTPFollower {
	final static float KP = -0.8f;
	final static float SPEED0 = 100f;
	final static float LOW = 0.02f; // background
	final static float HIGH = 0.45f; // line
	final static float MIDPOINT = 0.5f;

    @SuppressWarnings("resource")
	public static void main(String[] args) {
		new ExitThread().start();

		float correction = SPEED0 * 2f;
		EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);

		EV3ColorSensor colorSens = new EV3ColorSensor(SensorPort.S2);
		SampleProvider colorProv = colorSens.getRedMode();
		float[] buffer = new float[1];
		float err;

		left.setSpeed(SPEED0);
		right.setSpeed(SPEED0);
		left.forward();
		right.forward();

		for (;;)// ever
		{
			colorProv.fetchSample(buffer, 0);
			/*
			 * normalize from not on line == 0, on line == 1. Therefore with KP == 1
			 * max steering with one wheel stopping and the other turning at
			 * twice the speed
			 */
			err = (MIDPOINT - ((buffer[0] - LOW) / (HIGH - LOW))) * correction;

			left.setSpeed(SPEED0 - KP * err);
			right.setSpeed(SPEED0 + KP * err);

			left.forward();
			right.forward();
		}
	}

}
