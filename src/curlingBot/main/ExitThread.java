package curlingBot.main;

import lejos.hardware.Button;

public class ExitThread extends Thread {
    @Override
    public void run() {
        System.out.println("Press Button to exit.");
        Button.waitForAnyPress();
        System.exit(0);
    }
    
    public static Thread startExitThread() {
        Thread exitThread = new Thread(new ExitThread());
        exitThread.start();
        return exitThread;
    }
}
