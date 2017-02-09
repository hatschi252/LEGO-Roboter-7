package curlingBot.logic;

import curlingBot.logic.endboss.EndBossStrategy;
import curlingBot.logic.endboss.Random;

public class EndMode extends MoveMode {
	
	private EndBossStrategy strat;

	public EndMode(String description) {
		super(description);
		this.strat = new Random();
	}
	public EndMode(String description, EndBossStrategy strat) {
		super(description);
		this.strat = strat;
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
