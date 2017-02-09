package curlingBot.simplePrograms;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import curlingBot.main.ExitThread;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class LineFollowerTest {
	
//	private static final double targetValue = (0.55+0.3)/3;
	
	final static int STANDARD_SPEED = 240;
	final static float TARGET_VALUE = (0.5f + 0.05f) / 2.0f;
	

    @SuppressWarnings("resource")
	public static void main(String[] args)
	{
	    // start exit Thread
		Thread exitThread = new ExitThread();
		exitThread.start();
		// setup motors
		EV3LargeRegulatedMotor m_left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor m_right = new EV3LargeRegulatedMotor(MotorPort.B);
		// setup color sensor
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
		SampleProvider detector = colorSensor.getRedMode();
		float[] sampleSet = new float[20];
		
		startMoving(m_left, m_right);
        
		int circleCount = 0; // index for the cyclic array
//		float speedCorrection = STANDARD_SPEED / 2; //m_left.getMaxSpeed()/10;
		
		LineFollowControllSystemTest pController = new LineFollowControllSystemTest(0.1f);
		for (;;) {
            detector.fetchSample(sampleSet, circleCount);

            m_left.setSpeed(pController.calcSpeedLeftMotor(TARGET_VALUE, 
                    sampleSet[circleCount], m_left.getSpeed()));

            m_right.setSpeed(pController.calcSpeedLeftMotor(TARGET_VALUE, 
                    sampleSet[circleCount], m_right.getSpeed()));
            m_right.forward();
            m_left.forward();
            circleCount = ++circleCount % 20;
        }
		
		/*
		for(;;)//ever
		{
			detector.fetchSample(sampleSet, circleCount); // get sensor value
			float midSpeed = STANDARD_SPEED; //(m_left.getSpeed() + m_right.getSpeed())/2;
			// robot tries to stay on the left side of the bright stripe
			if (sampleSet[circleCount] <= targetValue * 0.9f)
			{
			    // sensor is on the bright stripe
			    
				m_left.setSpeed(midSpeed+speedCorrection);
				m_right.setSpeed(midSpeed);
			}
			else if (sampleSet[circleCount] > targetValue * 1.1f)
			{
			    // sensor is over the floor
				m_left.setSpeed(midSpeed);
				m_right.setSpeed(midSpeed+speedCorrection);
			} else {
			    //sensor is on the edge 
			    m_left.setSpeed(midSpeed);
			    m_right.setSpeed(midSpeed);
			}
			m_left.forward();
			m_right.forward();
			circleCount = ++circleCount % 20;
		}
		*/
	}	
	private static void startMoving(EV3LargeRegulatedMotor m_left, EV3LargeRegulatedMotor m_right)
	{
		float speed = STANDARD_SPEED;//(m_left.getMaxSpeed() + m_right.getMaxSpeed())/7;
		m_left.setSpeed(speed);
		m_right.setSpeed(speed);
		m_left.forward();
		m_right.forward();
	}
}
