package curlingBot.main;

public final class Output {
	private Output() {}
	
	public static void put(String message) {
		System.out.println(message);
	}
	
	public static void handleError(Exception ex) {
		System.out.println("ERR: " + ex.getMessage());
	}
}
