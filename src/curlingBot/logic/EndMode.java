package curlingBot.logic;

import curlingBot.logic.endboss.EndBossStrategy;
import curlingBot.main.Output;

public class EndMode extends MoveMode {
	
	private EndBossStrategy strat;

	public EndMode(String description, EndBossStrategy strat) {
		this.strat = strat;
		super(description);
	}

	@Override
	public void init() {
		strat.init();
	}

	@Override
	public void perform() {
		strat.perform();
	}
	

}
