package curlingBot.motorControl;

public class MoveState {
	public static final MoveState CURVE1 = null;

	private float velocity;
	private float steer;

	public MoveState(float velocity, float steer) {
		if (Float.isInfinite(steer)) {
			steer = Float.MAX_VALUE * Math.signum(steer);
		}
		this.velocity = velocity;
		this.steer = steer;
	}
	
	public static MoveState getMoveStateWithLeftAndRightSpeed(float leftSpeed, float rightSpeed) {
	    float left = infiniteHandler(leftSpeed);
	    float right = infiniteHandler(rightSpeed);
	    float speed = (left + right) / 2;
	    float steerValue = (Math.max(right, left) - Math.min(right, left)) / 2;
	    return (new MoveState(speed, steerValue));
	}
	
	private static float infiniteHandler(float floatToControl) {
	    if (Float.isInfinite(floatToControl)) {
            return Float.MAX_VALUE * Math.signum(floatToControl);
        } else {
            return floatToControl;
        }
	}

	public float getVelocity() {
		return velocity;
	}

	public float getSteer() {
		return steer;
	}

	public float getLeftVelocity() {
		return velocity + steer;
	}

	public float getRightVelocity() {
		return velocity - steer;
	}

	@Override
	public String toString() {
		return "{vL: " + getLeftVelocity() + ", vR: " + getLeftVelocity() + ", vA: " + getVelocity()
				+ ", s: " + getSteer() + "}";
	}
}
