package curlingBot.main;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class ExitThread extends Thread {
	@Override
	public void run() {
		
		Globals.waitForKey(Button.ID_ESCAPE);
		System.exit(0);
//		while (true) {
//			Output.put("Press ESCAPE to exit.");
//			int key = Button.waitForAnyEvent();
//			// Escape - exit program
//			if ((key & Button.ID_ESCAPE) != 0) {
//				System.exit(0);
//				// Enter - go into mode selection menu
//			} else if ((key & Button.ID_ENTER) != 0) {
//				key = 0;
//				int startIndex = 0;
//				int modeCount = Globals.logic.getModeCount();
//
//				// When enter is pressed again, we start logic with selected
//				// index
//				outer: while (true) {
//					LCD.clear();
//					LCD.drawString("Start index: " + startIndex, 0, 0);
//					LCD.drawString(Globals.logic.getDescrByIndex(startIndex), 0, 1);
//					key = Button.waitForAnyPress();
//					switch (key) {
//					case Button.ID_ENTER:
//						break outer;
//					case Button.ID_LEFT:
//					case Button.ID_UP:
//						startIndex = (startIndex + 1) % modeCount;
//						break;
//					case Button.ID_RIGHT:
//					case Button.ID_DOWN:
//						startIndex = (startIndex + modeCount - 1) % modeCount;
//						break;
//					case Button.ID_ESCAPE:
//						System.exit(0);
//						break;
//					default:
//						break;
//					}
//				}
//				LCD.clear();
//				Globals.logic.restart(startIndex);
//			}
//		}
	}
}
