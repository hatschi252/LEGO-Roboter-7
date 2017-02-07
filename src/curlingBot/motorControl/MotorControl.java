package curlingBot.motorControl;

import curlingBot.main.Globals;
import curlingBot.main.Output;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public final class MotorControl extends Thread {
	public static final Port MOTORPORT_LEFT = MotorPort.A;
	public static final Port MOTORPORT_RIGHT = MotorPort.B;
	public static final Port MOTORPORT_ULTRASONIC = MotorPort.D;
	private static final int ULTRASONIC_MOVE_ANGLE = 70;
	private static final float ACC_FAC = 0.1f;
	private static final int SLEEP_TIME = 30;

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
	
//	private float absMax(float a, float b) {
//		return Math.max(Math.abs(a), Math.abs(b));
//	}
//	
//	private float absAdd(float a, float b) {
//		return Math.signum(a) * (Math.abs(a) + b);
//	}

	@Override
	public void run() {
//		Output.put("vMax = " + leftMotor.getMaxSpeed());
//		while (true) {
//			MoveState currentMoveState = getCurrentMoveState();
//			float correctionOffset = absMax(desiredMoveState.getLeftVelocity(), desiredMoveState.getRightVelocity())
//					- leftMotor.getMaxSpeed();
//			float vLeftCorrected = absAdd(desiredMoveState.getLeftVelocity(), -correctionOffset);
//			float vRightCorrected = absAdd(desiredMoveState.getRightVelocity(), -correctionOffset);
//			float vDeltaLeft = vLeftCorrected - currentMoveState.getLeftVelocity();
//			float vDeltaRight = vRightCorrected - currentMoveState.getRightVelocity();
//			if (vDeltaLeft > 0) {
//				leftMotor.setAcceleration((int) (ACC_FAC * vDeltaLeft * currentMaximumAcc));
//				leftMotor.forward();
//			} else if (vDeltaLeft < 0) {
//				leftMotor.setAcceleration((int) (ACC_FAC * vDeltaLeft * currentMaximumAcc));
//				leftMotor.backward();
//			} else {
//				leftMotor.setAcceleration(0);
//				leftMotor.forward();
//			}
//			if (vDeltaRight > 0) {
//				rightMotor.setAcceleration((int) (ACC_FAC * vDeltaRight * currentMaximumAcc));
//				rightMotor.forward();
//			} else if (vDeltaRight < 0) {
//				rightMotor.setAcceleration((int) (ACC_FAC * vDeltaRight * currentMaximumAcc));
//				rightMotor.backward();
//			} else {
//				rightMotor.setAcceleration(0);
//				rightMotor.forward();
//			}
//			
////			Output.put("currMS = " + currentMoveState);
////			Output.put("lAcc = " + leftMotor.getAcceleration());
////			Output.put("rAcc = " + rightMotor.getAcceleration());
//
//			Globals.sleep(SLEEP_TIME);
//		}
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
		setMoveState(new MoveState(0, 0), Integer.MAX_VALUE);
		rightMotor.setSpeed(0);
		leftMotor.setSpeed(0);
		rightMotor.forward();
		leftMotor.forward();
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
		leftMotor.setSpeed(moveState.getLeftVelocity());
		rightMotor.setSpeed(moveState.getRightVelocity());
		desiredMoveState = moveState;
		currentMaximumAcc = maxAcc;
	}
	

	public EV3LargeRegulatedMotor getLeftMotor() {
		return leftMotor;
	}

	public EV3LargeRegulatedMotor getRightMotor() {
		return rightMotor;
	}
}
