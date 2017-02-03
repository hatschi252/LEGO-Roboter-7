package curlingBot.sensors;

public class CyclicBuffer {

    float[] buffer;
    int index = 0;
    Object lock = new Object();

    public CyclicBuffer(int sizeOfBuffer) {
        this.buffer = new float[sizeOfBuffer];

    }
}
