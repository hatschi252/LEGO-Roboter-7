package curlingBot.motorControl;

public class PController {
    private float kp = -0.8f;
    private float speed0 = 100f;
    private float low = 0.02f; // background
    private float high = 0.45f; // line
    private float midPoint = 0.5f;

    public PController(float kp, float speed, float low, float high, float midPoint) {
        this.kp = kp;
        this.speed0 = speed;
        this.low = low;
        this.high = high;
        this.midPoint = midPoint;
    }

    public PController(float kp, float speed) {
        this.kp = kp;
        this.speed0 = speed;
    }
    
    /**
     * Instance with default parameters.
     */
    public PController() {
        
    }

    public float getSpeedLeft(float sensorInput) {
        return speed0 - calcErrorTimesKp(sensorInput);
    }

    public float getSpeedRight(float sensorInput) {
        return speed0 + calcErrorTimesKp(sensorInput);
    }

    private float calcErrorTimesKp(float sensorInput) {
    	float normalizedSensorInput = sensorInput > high ? high : sensorInput;
    	normalizedSensorInput = normalizedSensorInput < low ? low : normalizedSensorInput;
        float correction = 2.0f * this.speed0;
        float returnValue = this.kp * ((midPoint - ((normalizedSensorInput - low) / (high - low))) * correction);
        return returnValue <= speed0 ? returnValue : speed0;
    }

    public float getKp() {
        return kp; 
    }

    public void setKp(float kp) {
        this.kp = kp;
    }

    public float getSpeed0() {
        return speed0;
    }

    public void setSpeed0(float speed0) {
        this.speed0 = speed0;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(float midPoint) {
        this.midPoint = midPoint;
    }

}
