package curlingBot.sensors;

public class CyclicBuffer {

    float[] buffer;
    int index = 0;
    Object lock = new Object();

    public CyclicBuffer(int sizeOfBuffer) {
        this.buffer = new float[sizeOfBuffer];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
    }

    public float[] getBuffer() {
        return this.buffer;
    }

    public int getIndexOfLastInsertedElement() {
        return this.index;
    }

    public void incrementIndex() {
        this.index++;
    }
}
