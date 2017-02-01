import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class LineFollowerTest {
	
	private static final double targetValue = (0.55+0.3)/3;
	

	public static void main(String[] args)
	{
		Thread exitThread = new Thread(new exitThread.ExitThreadRunnable());
		exitThread.start();
		EV3LargeRegulatedMotor m_left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor m_right = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
		SampleProvider detektor = colorSensor.getRedMode();
		setForwardhalfMax(m_left, m_right);
		float[] sampleSet = new float[20];
		int circleCount = 0;
		float speedCorrection = m_left.getMaxSpeed()/10;
		for(;;)//ever
		{
			detektor.fetchSample(sampleSet, circleCount);
			float midSpeed = (m_left.getSpeed() + m_right.getSpeed())/2;
			if (sampleSet[circleCount] >= targetValue)
			{
				m_left.setSpeed(midSpeed+speedCorrection);
				m_right.setSpeed(midSpeed-speedCorrection);
			}
			else if (sampleSet[circleCount] < targetValue)
			{
				m_left.setSpeed(midSpeed-speedCorrection);
				m_right.setSpeed(midSpeed+speedCorrection);
			}
			m_left.forward();
			m_right.forward();
			circleCount = ++circleCount % 20;
		}
		
	}	
	private static void setForwardhalfMax(EV3LargeRegulatedMotor m_left, EV3LargeRegulatedMotor m_right)
	{
		float speed = (m_left.getMaxSpeed() + m_right.getMaxSpeed())/5;
		m_left.setSpeed(speed);
		m_right.setSpeed(speed);
		m_left.forward();
		m_right.forward();
	}
}
