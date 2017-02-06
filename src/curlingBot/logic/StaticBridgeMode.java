package curlingBot.logic;

import curlingBot.main.Globals;

public class StaticBridgeMode implements IMoveMode {

    @Override
    public void init() {
        Globals.sensorBuffer.setColorSensorActive(false);
        Globals.sensorBuffer.setGyroSensorActive(true);
        Globals.sensorBuffer.setUltraSonicSensorActive(true);
        Globals.sensorBuffer.setTouchSensorActive(false);

    }

    @Override
    public void perform() {
        // TODO Auto-generated method stub

    }

}
