package curlingBot.main;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public final class Output {
	private static final int LINES_ON_LCD = 5;
	private static Object lock = new Object();
	private static int currentLine = 0;

	private Output() {
	}

	public static void put(String message) {
		synchronized (lock) {
			LCD.drawString(message, 0, currentLine++);
			if (currentLine > LINES_ON_LCD) {
				LCD.clear();
				currentLine = 0;
			}
		}
		// System.out.println(message);
	}

	public static void handleError(Exception ex) {
		synchronized (lock) {
			LCD.drawString("ERR: " + ex.getMessage(), 0, currentLine++);
			if (currentLine > LINES_ON_LCD) {
				LCD.clear();
				currentLine = 0;
			}
		}
		// System.out.println("ERR: " + ex.getMessage());
	}

	public static void handleError(String message) {
		synchronized (lock) {
			LCD.drawString("ERR: " + message, 0, currentLine++);
			if (currentLine > LINES_ON_LCD) {
				LCD.clear();
				currentLine = 0;
			}
		}
		// System.out.println("ERR: " + message);
	}

	public static void finished() {
		Sound.setVolume(100);
		Sound.beepSequenceUp();
	}

	public static void beep() {
		Sound.setVolume(100);
		Sound.beep();
	}
}
