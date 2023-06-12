package com.kore.ai.widgetsdk.charts.buffer;

public abstract class AbstractBuffer<T> {
    protected int index = 0;
    public final float[] buffer;
    protected float phaseX = 1.0F;
    protected float phaseY = 1.0F;
    protected int mFrom = 0;
    protected int mTo = 0;

    public AbstractBuffer(int size) {
        this.index = 0;
        this.buffer = new float[size];
    }

    public void limitFrom(int from) {
        if (from < 0) {
            from = 0;
        }

        this.mFrom = from;
    }

    public void limitTo(int to) {
        if (to < 0) {
            to = 0;
        }

        this.mTo = to;
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