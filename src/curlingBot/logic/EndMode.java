package curlingBot.logic;

import curlingBot.main.Output;

public class EndMode implements IMoveMode {

	@Override
	public void init() {
	}

	@Override
	public void perform() {
		Output.put("EndMode");
	}
	

}
