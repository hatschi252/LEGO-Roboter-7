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

    public int calcSpeedLeftMotor(float targetValue, float currentValue, int speedTargetLeft) {
        System.out.println("deltal: " + (targetValue - currentValue));
        return (int) (speedTargetLeft + (targetValue - currentValue) * propFactor);
    }

    public int calcSpeedRightMotor(float targetValue, float currentValue, int speedTargetRight) {
        System.out.println("deltar: " + (targetValue - currentValue));
        return (int) (speedTargetRight - (targetValue - currentValue) * propFactor);
    }
}
