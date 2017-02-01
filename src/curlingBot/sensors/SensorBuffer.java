package curlingBot.sensors;

public final class SensorBuffer extends Thread {
	public static SensorBuffer sensorBufferInstance;
	
	private SensorBuffer() {
		
	}
	
	public static SensorBuffer getInstance() {
		if (sensorBufferInstance == null) {
			sensorBufferInstance = new SensorBuffer();
		}
		return sensorBufferInstance;
	}
}
