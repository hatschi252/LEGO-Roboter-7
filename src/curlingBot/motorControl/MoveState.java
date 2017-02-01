package curlingBot.motorControl;

public class MoveState {
	private float velocity;
	private float steer;
	
	public MoveState(float velocity, float steer) {
		this.velocity = velocity;
		this.steer = steer;
	}
	
	public float getVelocity() {
		return velocity;
	}
	
	public float getSteer() {
		return steer;
	}
}
