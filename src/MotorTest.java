import exitThread.ExitThreadRunnable;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class MotorTest {

    public static void main(String[] args) {
        int speed = 240;
        Button.waitForAnyPress();
        EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
        left.setSpeed(speed);
        right.setSpeed(speed);
        left.forward();
        right.forward();
        Thread exitThread = new Thread(new ExitThreadRunnable());
        exitThread.start();
        while (left.getTachoCount() < 360 * 100) {
            balance(left, right, speed);
        }
        left.stop();
        right.stop();
        left.close();
        right.close();
    }

    private static void balance(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right, int speed) {
        // TODO Auto-generated method stub
        if (left.getTachoCount() < right.getTachoCount()) {
            // right is faster than left
            right.setSpeed((int) (0.9 * speed));
            left.setSpeed(speed);
            left.forward();
            right.forward();
        } else {
            //left is faster than right
            left.setSpeed((int) (0.9 * speed));
            right.setSpeed(speed);
            left.forward();
            right.forward();
        }
    }

}
