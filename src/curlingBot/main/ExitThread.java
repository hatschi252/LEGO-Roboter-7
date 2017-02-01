package curlingBot.main;

import lejos.hardware.Button;

public class ExitThread extends Thread {
    @Override
    public void run() {
        System.out.println("Press Button to exit.");
        Button.waitForAnyPress(); // TODO check if this works
        System.exit(0);
    }
}
