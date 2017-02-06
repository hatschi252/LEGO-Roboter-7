package curlingBot.logic;

import java.util.ArrayList;
import java.util.List;

public class Logic extends Thread {
	private static Logic logicThreadInstance;
	
	/**
	 * The list of moveModes. The order of the elements in the parkour is the same
	 * as the order of the corresponding modes in this list.
	 */
	private List<IMoveMode> moveModeList;
	private IMoveMode currentMoveMode;
	
	private Logic() {
		moveModeList = new ArrayList<IMoveMode>();
	}
	
	/**
	 * Returns the only instance of Logic.
	 * @return
	 */
	public static Logic getInstance() {
		if (logicThreadInstance == null) {
			logicThreadInstance = new Logic();
		}
		return logicThreadInstance;
	}
	
	@Override
	public void run() {
		//Run through the parkour
		while (!moveModeList.isEmpty()) {
			currentMoveMode = moveModeList.remove(0);
			currentMoveMode.init();
			currentMoveMode.perform();
		}
		//Finished?
	}
	
	/**
	 * Adds a moveMode to the moveMode list. The next moveMode to be performed is taken
	 * from the beginning of the list (index 0). The given modeMode is added at the end
	 * of the list.
	 * @param moveMode the moveMode to be added
	 */
	public void addMoveMode(IMoveMode moveMode) {
		moveModeList.add(moveMode);
	}
}
