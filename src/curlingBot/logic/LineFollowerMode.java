package curlingBot.logic;

import curlingBot.main.Globals;

public class LineFollowerMode implements IMoveMode {

    @Override
    public void init() {
        Globals.sensorBuffer.setGyroSensorActive(true); // for see saw
        Globals.sensorBuffer.setUltraSonicSensorActive(false);
        Globals.sensorBuffer.setTouchSensorActive(false); // TODO check
        Globals.sensorBuffer.setColorSensorActive(true);

    }
    
    @Override
	public void perform() {
		
	}
}
