package curlingBot.sensors;

public class CyclicBuffer {

    private float[] buffer;
    private int index = 0;
    private int sampleSize = 1;

    public CyclicBuffer(int sizeOfBuffer, int sampleSize) {
        this.buffer = new float[sizeOfBuffer];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
        this.sampleSize = sampleSize;
    }

    public float[] getBuffer() {
        return this.buffer;
    }

    public int getIndexOfLastInsertedElement() {
        return this.index;
    }

    public void incrementIndex() {
        this.index = (this.sampleSize + this.index) % this.buffer.length;
    }
}
