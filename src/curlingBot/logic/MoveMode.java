package curlingBot.logic;

/**
 * This abstract class encapsulates the behavior of the robot.
 * 
 */
public abstract class MoveMode {
	private String description;
	
	protected MoveMode(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
    public abstract void init();
	public abstract void perform();
}
