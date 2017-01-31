import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class MotorAccelerationTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        EV3LargeRegulatedMotor m_left = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor m_right = new EV3LargeRegulatedMotor(MotorPort.B);
        m_left.setAcceleration(600);
        m_right.setAcceleration(600);
        m_left.setSpeed(700);
        m_right.setSpeed(700);
        m_left.forward();
        m_right.forward();
        Delay.msDelay(10000);
        m_right.stop();
        m_left.stop();
        Delay.msDelay(2000);
        m_left.close();
        m_right.close();
    }

}
