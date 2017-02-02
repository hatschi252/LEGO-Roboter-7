package curlingBot.main;

import lejos.hardware.Button;

public class ExitThread extends Thread {
    @Override
    public void run() {
        System.out.println("Press ESCAPE to exit.");
        while ((Button.waitForAnyPress() & Button.ID_ESCAPE) == 0) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Output.handleError(ex);
			}
        }
        System.exit(0);
    }
}
