package curlingBot.logic;

import java.util.ArrayList;
import java.util.List;

import curlingBot.main.Output;

public class Logic implements Runnable {
	private static Logic logicInstance;

	/**
	 * The list of moveModes. The order of the elements in the parkour is the
	 * same as the order of the corresponding modes in this list.
	 */
	private List<MoveMode> moveModeList;
	private MoveMode currentMoveMode;
	private int startIndex;
	private Thread logicThread;

	private Logic() {
		startIndex = 0;
		moveModeList = new ArrayList<MoveMode>();
	}

	/**
	 * Returns the only instance of Logic.
	 * 
	 * @return
	 */
	public static Logic getInstance() {
		if (logicInstance == null) {
			logicInstance = new Logic();
		}
		return logicInstance;
	}
	
	public int getModeCount() {
		return moveModeList.size();
	}
	
	public String getDescrByIndex(int index) {
		return moveModeList.get(index).getDescription();
	}

	public void restart(int startIndex) {
		this.startIndex = startIndex;
		if (logicThread != null) {
			logicThread.interrupt();
		}
		logicThread = new Thread(this);
		logicThread.start();
	}

	@Override
	public void run() {
		try {
			// Run through the parkour
			for (int i = startIndex; i < moveModeList.size(); i++) {
				currentMoveMode = moveModeList.get(i);
				Output.put("Starting: " + currentMoveMode.getDescription());
				Output.beep();
				currentMoveMode.init();
				currentMoveMode.perform();
			}
			// Finished?
		} catch (Exception ex) {
			Output.handleError("Logic got exception: " + ex.getMessage());
		}
	}

	/**
	 * Adds a moveMode to the moveMode list. The next moveMode to be performed
	 * is taken from the beginning of the list (index 0). The given modeMode is
	 * added at the end of the list.
	 * 
	 * @param moveMode
	 *            the moveMode to be added
	 */
	public void addMoveMode(MoveMode moveMode) {
		moveModeList.add(moveMode);
	}
}
