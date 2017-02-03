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
