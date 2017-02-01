package exitThread;

import lejos.hardware.Button;

public class ExitThreadRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("Press Button to exit.");
        Button.waitForAnyPress(); // TODO check if this works
        System.exit(0);
    }

}
