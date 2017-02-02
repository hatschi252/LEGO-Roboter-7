package curlingBot.motorControl;

import curlingBot.main.Globals;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public final class MotorControl extends Thread {
	public static final Port MOTORPORT_LEFT = MotorPort.A;
	public static final Port MOTORPORT_RIGHT = MotorPort.B;
	public static final Port MOTORPORT_ULTRASONIC = MotorPort.D;
	private static final int ULTRASONIC_MOVE_ANGLE = 60;

	private static MotorControl motorControlInstance;

	private MoveState desiredMoveState;
	private int currentMaximumAcc;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private EV3MediumRegulatedMotor ultrasonicMotor;
	private boolean isUltrasonicUp;

	private MotorControl() {
		leftMotor = new EV3LargeRegulatedMotor(MOTORPORT_LEFT);
		rightMotor = new EV3LargeRegulatedMotor(MOTORPORT_RIGHT);
		ultrasonicMotor = new EV3MediumRegulatedMotor(MOTORPORT_ULTRASONIC);
		// TODO: take care that the ultrasonic sensor is moved up for
		// initialization
		isUltrasonicUp = true;
		
		desiredMoveState = new MoveState(0, 0);
	}

	@Override
	public void run() {
		while (true) {
			// Ist eine Geschwindigkeit wirklich jemals gleich der
			// desiredVelocity?
			// Es handelt sich um floats - was wäre ein passendes MIN_DELTA?
			MoveState currentMoveState = getCurrentMoveState();
			float vDeltaLeft = desiredMoveState.getLeftVelocity() - currentMoveState.getLeftVelocity();
			float vDeltaRight = desiredMoveState.getRightVelocity() - currentMoveState.getRightVelocity();
			if (vDeltaLeft > 0) {
				leftMotor.setAcceleration(currentMaximumAcc);
			} else if (vDeltaLeft < 0) {
				leftMotor.setAcceleration(-currentMaximumAcc);
			} else {
				leftMotor.setAcceleration(0);
			}
			if (vDeltaRight > 0) {
				rightMotor.setAcceleration(currentMaximumAcc);
			} else if (vDeltaRight < 0) {
				rightMotor.setAcceleration(-currentMaximumAcc);
			} else {
				rightMotor.setAcceleration(0);
			}
			
			Globals.sleep(30);
		}
	}



	public static MotorControl getInstance() {
		if (motorControlInstance == null) {
			motorControlInstance = new MotorControl();
		}
		return motorControlInstance;
	}

	/**
	 * Stops all motors as fast as possible.
	 */
	public void emergencyStop() {
		leftMotor.stop(false);
		rightMotor.stop(false);
	}

	/**
	 * Returns the CURRENT moveState.
	 * 
	 * @return
	 */
	public MoveState getCurrentMoveState() {
		float vRight = rightMotor.getRotationSpeed();
		float velocity = (leftMotor.getRotationSpeed() + vRight) / 2;
		float steer = velocity - vRight;
		return new MoveState(velocity, steer);
	}

	/**
	 * Returns the DESIRED moveState that was set earlier.
	 * 
	 * @return
	 */
	public MoveState getMoveState() {
		return desiredMoveState;
	}

	public void moveUltrasonicDown() {
		if (!isUltrasonicUp) {
			return;
		}
		ultrasonicMotor.rotate(ULTRASONIC_MOVE_ANGLE, false);
		isUltrasonicUp = false;
	}

	public void moveUltrasonicUp() {
		if (isUltrasonicUp) {
			return;
		}
		ultrasonicMotor.rotate(-ULTRASONIC_MOVE_ANGLE, false);
		isUltrasonicUp = true;
	}

	/**
	 * Sets the desired moveState. MotorControl will then smoothly transit into
	 * this desired state with the given maximum acceleration.
	 * 
	 * @param moveState
	 * @param maxAcc
	 */
	public void setMoveState(MoveState moveState, int maxAcc) {
		desiredMoveState = moveState;
		currentMaximumAcc = maxAcc;
	}
}
