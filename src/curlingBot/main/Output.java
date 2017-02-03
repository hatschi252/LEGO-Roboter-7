package curlingBot.main;

import lejos.hardware.Sound;

public final class Output {
	private Output() {}
	
	public static void put(String message) {
		System.out.println(message);
	}
	
	public static void handleError(Exception ex) {
		System.out.println("ERR: " + ex.getMessage());
	}
	
	public static void finished() {
		Sound.setVolume(100);
		Sound.beepSequenceUp();
	}
	
	public static void beep() {
		Sound.setVolume(100);
		Sound.beep();
	}
	
//	public static void stupidItalian() {
//		Sound.
//	}
}
