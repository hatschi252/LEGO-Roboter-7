import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class WallAvoidence {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
        SampleProvider ultraSample = ultraSonicSensor.getDistanceMode();
        float[] sampleValues = new float[10];
        
        for (int i = 0; i < sampleValues.length * 10; i++) {
            ultraSample.fetchSample(sampleValues, i % sampleValues.length);
            System.out.println(sampleValues[i % sampleValues.length]);
            Delay.msDelay(1000);
        }
        
        System.out.print("Finished.");
        
        
        Button.waitForAnyPress();
        ultraSonicSensor.close();
    }
    static void acc_forward(EV3LargeRegulatedMotor m_left, EV3LargeRegulatedMotor m_right)
    {
        m_left.setSpeed(m_left.getSpeed());
    }
}
