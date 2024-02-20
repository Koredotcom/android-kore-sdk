package kore.botssdk.charts.buffer;

public abstract class AbstractBuffer<T> {
    protected int index = 0;
    public final float[] buffer;
    protected float phaseX = 1.0F;
    protected float phaseY = 1.0F;

    public AbstractBuffer(int size) {
        this.index = 0;
        this.buffer = new float[size];
    }

    public void reset() {
        this.index = 0;
    }

    public int size() {
        return this.buffer.length;
    }

    public void setPhases(float phaseX, float phaseY) {
        this.phaseX = phaseX;
        this.phaseY = phaseY;
    }

    public abstract void feed(T var1);
}