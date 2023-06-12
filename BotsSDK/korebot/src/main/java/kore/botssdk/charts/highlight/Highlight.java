package kore.botssdk.charts.highlight;

import kore.botssdk.charts.components.YAxis;

public class Highlight {
    private float mX;
    private float mY;
    private float mXPx;
    private float mYPx;
    private int mDataIndex;
    private final int mDataSetIndex;
    private int mStackIndex;
    private YAxis.AxisDependency axis;
    private float mDrawX;
    private float mDrawY;

    public Highlight(float x, float y, int dataSetIndex) {
        this.mX = 0.0F / 0.0F;
        this.mY = 0.0F / 0.0F;
        this.mDataIndex = -1;
        this.mStackIndex = -1;
        this.mX = x;
        this.mY = y;
        this.mDataSetIndex = dataSetIndex;
    }

    public Highlight(float x, int dataSetIndex, int stackIndex) {
        this(x, 0.0F / 0.0F, dataSetIndex);
        this.mStackIndex = stackIndex;
    }

    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, YAxis.AxisDependency axis) {
        this.mX = 0.0F / 0.0F;
        this.mY = 0.0F / 0.0F;
        this.mDataIndex = -1;
        this.mStackIndex = -1;
        this.mX = x;
        this.mY = y;
        this.mXPx = xPx;
        this.mYPx = yPx;
        this.mDataSetIndex = dataSetIndex;
        this.axis = axis;
    }

    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, int stackIndex, YAxis.AxisDependency axis) {
        this(x, y, xPx, yPx, dataSetIndex, axis);
        this.mStackIndex = stackIndex;
    }

    public float getX() {
        return this.mX;
    }

    public float getY() {
        return this.mY;
    }

    public float getXPx() {
        return this.mXPx;
    }

    public float getYPx() {
        return this.mYPx;
    }

    public int getDataIndex() {
        return this.mDataIndex;
    }

    public void setDataIndex(int mDataIndex) {
        this.mDataIndex = mDataIndex;
    }

    public int getDataSetIndex() {
        return this.mDataSetIndex;
    }

    public int getStackIndex() {
        return this.mStackIndex;
    }

    public boolean isStacked() {
        return this.mStackIndex >= 0;
    }

    public YAxis.AxisDependency getAxis() {
        return this.axis;
    }

    public void setDraw(float x, float y) {
        this.mDrawX = x;
        this.mDrawY = y;
    }

    public float getDrawX() {
        return this.mDrawX;
    }

    public float getDrawY() {
        return this.mDrawY;
    }

    public boolean equalTo(Highlight h) {
        if (h == null) {
            return false;
        } else {
            return this.mDataSetIndex == h.mDataSetIndex && this.mX == h.mX && this.mStackIndex == h.mStackIndex && this.mDataIndex == h.mDataIndex;
        }
    }

    public String toString() {
        return "Highlight, x: " + this.mX + ", y: " + this.mY + ", dataSetIndex: " + this.mDataSetIndex + ", stackIndex (only stacked barentry): " + this.mStackIndex;
    }
}
