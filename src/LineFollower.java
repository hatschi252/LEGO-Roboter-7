import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class LineFollower {
	
	EV3LargeRegulatedMotor m_left;
	EV3LargeRegulatedMotor m_right;
	SampleProvider detektor;
	
	public LineFollower()
	{
		m_left = new EV3LargeRegulatedMotor(MotorPort.A);
		m_right = new EV3LargeRegulatedMotor(MotorPort.B);
		detektor = new EV3ColorSensor(SensorPort.S2);
	}
}
