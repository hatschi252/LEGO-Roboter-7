package curlingBot.simplePrograms;

import java.util.Arrays;

import curlingBot.main.ExitThread;
import curlingBot.main.Globals;
import curlingBot.main.Output;
import curlingBot.motorControl.MotorControl;
import curlingBot.motorControl.MoveState;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class MotorControlledWallAvoidance {
	private static final int SAMPLE_ARRAY_SIZE = 3;
	private static final float VELOCITY = 300;
	private static final float DESIRED_DISTANCE = 0.15f;
	private static final int MAX_ACCELERATION = 5;
	private static final int SLEEP_TIME = 30;
	private static final int DISTANCE_TO_STEER_FACTOR = (int) (-1 * VELOCITY);
	private static final int MAX_STEER = 300;
	
	private static SampleProvider ultraSampleProvider;
	private static EV3UltrasonicSensor ultraSonicSensor;
	
	public static void main(String[] args) {
		Output.put("MotorControlledWallAvoidance");

		Globals.exitThread = new ExitThread();
		Globals.exitThread.start();
		
		Globals.motorControl = MotorControl.getInstance();
		Globals.motorControl.start();
		
        // setup ultrasonic sensor
        ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
        ultraSampleProvider = ultraSonicSensor.getDistanceMode();


//		Globals.waitForKey(Button.ID_ENTER);
		while (true) {
			float deltaDistance = getSample() - DESIRED_DISTANCE;
			float steer = Math.min(MAX_STEER,
					Math.max(-MAX_STEER, DISTANCE_TO_STEER_FACTOR * deltaDistance));
			Output.put("dDistance = " + deltaDistance + " steer = " + steer);
			Globals.motorControl.setMoveState(new MoveState(VELOCITY,
					steer), MAX_ACCELERATION);
			Globals.sleep(SLEEP_TIME);
		}		
	}
	
	private static float getSample() {
		float[] buffer = new float[SAMPLE_ARRAY_SIZE];
		for (int i = 0; i < buffer.length; i++) {
			ultraSampleProvider.fetchSample(buffer, i);			
		}
		Arrays.sort(buffer);
		return buffer[SAMPLE_ARRAY_SIZE / 2];
	}
}
