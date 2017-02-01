package curlingBot.motorControl;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public final class MotorControl extends Thread {
	public static final Port MOTORPORT_LEFT = MotorPort.A;
	public static final Port MOTORPORT_RIGHT = MotorPort.B;
	
	private static MotorControl motorControlInstance;
	
	private MoveState desiredMoveState;
	private float currentMaximumAcc;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	private MotorControl() {
		 leftMotor = new EV3LargeRegulatedMotor(MOTORPORT_LEFT);
		 rightMotor = new EV3LargeRegulatedMotor(MOTORPORT_RIGHT);
	}
	
	@Override
	public void run() {
		while (true) {
			//Ist eine Geschwindigkeit wirklich jemals gleich der desiredVelocity?
			//Es handelt sich um floats - was wäre ein passendes MIN_DELTA?
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
	 * @return
	 */
	public MoveState getMoveState() {
		return desiredMoveState;
	}
	
	/**
	 * Sets the desired moveState. MotorControl will then smoothly transit into this desired state
	 * with the given maximum acceleration.
	 * @param moveState
	 * @param maxAcc
	 */
	public void setMoveState(MoveState moveState, float maxAcc) {
		desiredMoveState = moveState;
		currentMaximumAcc = maxAcc;
	}
}
