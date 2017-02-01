package curlingBot.simplePrograms;

public class LineFollowControllSystemTest {

    private double propFactor;
    
    public LineFollowControllSystemTest(double propFactor) {
        this.propFactor = propFactor;
    }
    
    
    
    public double getPropFactor() {
        return propFactor;
    }



    public void setPropFactor(double propFactor) {
        this.propFactor = propFactor;
    }



    public int calcSpeedLeftMotor(float targetValue, float currentValue, int speedLeft) {
        return (int) (speedLeft + (targetValue - currentValue) * propFactor);
    }
    public int calcSpeedRightMotor(float targetValue, float currentValue, int speedRight) {
        return (int) (speedRight - (targetValue - currentValue) * propFactor);
    }
}
