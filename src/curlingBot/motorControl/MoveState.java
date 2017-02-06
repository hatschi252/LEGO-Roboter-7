package curlingBot.motorControl;

import curlingBot.main.Globals;
import curlingBot.main.Output;

public class MoveState {
	public static final MoveState CURVE1 = null;

	private float velocity;
	private float radius;

	/**
	 * Creates a new MoveState object. The first argument is the velocity the robot tries to maintain.
	 * The second argument is the radius of the circle the robot tries to follow. Use infinity for a
	 * straight movement. 
	 * @param velocity
	 * @param radius
	 */
	public MoveState(float velocity, float radius) {
		this.velocity = velocity;
		this.radius = radius;
	}
	
	public static MoveState fromWheelVelocities(float vLeft, float vRight) {
		float v = (vLeft + vRight) / 2f;
		float tmp = (2 * vLeft) / (vLeft + vRight);
		float radius = Globals.AXIS_LENGTH / (2 * (1 - tmp));
		Output.put("v=" + v + " tmp=" + tmp + " radius=" + radius);
		if (Float.isNaN(radius)) {
			radius = Float.POSITIVE_INFINITY;
		}
		return new MoveState(v, radius);
	}

	public float getVelocity() {
		return velocity;
	}

	public float getRadius() {
		return radius;
	}

	public float getLeftVelocity() {
		float result = ((radius - Globals.AXIS_LENGTH / 2) * velocity) / radius;
		if (Float.isNaN(result)) {
			return velocity;
		} else {
			return result;			
		}
	}

	public float getRightVelocity() {
		float result = ((radius + Globals.AXIS_LENGTH / 2) * velocity) / radius;
		if (Float.isNaN(result)) {
			return velocity;
		} else {
			return result;			
		}
	}

	@Override
	public String toString() {
		return "{vL: " + getLeftVelocity() + ", vR: " + getLeftVelocity() + ", v: " + getVelocity()
				+ ", r: " + radius + "}";
	}
}
