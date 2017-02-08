package curlingBot.logic;

import curlingBot.main.Output;

public class EndMode extends MoveMode {

	public EndMode(String description) {
		super(description);
	}

	@Override
	public void init() {
	}

	@Override
	public void perform() {
		Output.put("EndMode");
		Output.finished();
	}
	

}
