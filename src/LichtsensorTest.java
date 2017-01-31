import lejos.hardware.Device;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class LichtsensorTest {

    public static void main(String[] args) {
        //EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
        EV3ColorSensor sensor3 = new EV3ColorSensor(SensorPort.S2);
        SampleProvider light = sensor3.getRedMode(); //getMode("Red");
        float sample[] = new float[light.sampleSize()];
        for (int i = 0; i < 50 ;++i) {
            light.fetchSample(sample, 0);
            System.out.println(sample[0]);
            Delay.msDelay(750);
        }
        ((Device) sensor3).close();
    }

}
