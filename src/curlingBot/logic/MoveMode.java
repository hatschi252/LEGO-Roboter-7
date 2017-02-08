package curlingBot.logic;

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
