package curlingBot.logic;

import curlingBot.main.Globals;
import curlingBot.motorControl.MoveState;
import curlingBot.motorControl.PController;

public class LineFollowerMode implements IMoveMode {

    private PController pController;
    private final int maxAcc = 100; //TODO test acc s

    @Override
    public void init() {
        Globals.sensorBuffer.setGyroSensorActive(true); // for see saw
        Globals.sensorBuffer.setUltraSonicSensorActive(false);
        Globals.sensorBuffer.setTouchSensorActive(false); // TODO check
        Globals.sensorBuffer.setColorSensorActive(true);

    }

    @Override
    public void perform() {
        this.pController = new PController();
        for (;;) {
            float sensorInput = Globals.sensorBuffer.getLastMessurementColor();
            float leftSpeed = pController.getSpeedLeft(sensorInput);
            float rightSpeed = pController.getSpeedRight(sensorInput);
            MoveState moveState = MoveState.getMoveStateWithLeftAndRightSpeed(leftSpeed, rightSpeed);
            Globals.motorControl.setMoveState(moveState, maxAcc);
        }

    }
}
