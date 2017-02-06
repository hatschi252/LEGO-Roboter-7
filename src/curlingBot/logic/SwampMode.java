package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.main.Output;

public class SwampMode implements IMoveMode {

	@Override
	public void init() {
		Output.put("SwampMode");
		Globals.sensorBuffer.setGyroSensorActive(false);
		Globals.sensorBuffer.setUltraSonicSensorActive(true);
		Globals.sensorBuffer.setTouchSensorActive(true);
		Globals.sensorBuffer.setColorSensorActive(true);
	}

	@Override
	public void perform() {
		/*
		 * Before this mode we followed a line until there was another line in a 90° angle
		 * to the first line (not a curve). We can now assume that the robot is in front of the swamp and still going
		 * in the direction of the swamp. The idea is to go into the swamp with low speed so we 
		 * get blocked by the first pole on the ground. Hopefully we are then aligned with the swamp.
		 * We should then be able to go straight backwards and then forward with full speed.
		 */
	}

}
