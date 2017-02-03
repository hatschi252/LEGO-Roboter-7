package curlingBot.main;

import lejos.hardware.Button;

public class ExitThread extends Thread {
    @Override
    public void run() {
    	Output.put("Press ESCAPE to exit.");
    	Globals.waitForKey(Button.ID_ESCAPE);
        System.exit(0);
    }
}
