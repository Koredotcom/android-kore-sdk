package kore.botssdk.charts.buffer;

import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;

public class BarBuffer extends AbstractBuffer<IBarDataSet> {
    protected int mDataSetIndex = 0;
    protected int mDataSetCount = 1;
    protected boolean mContainsStacks = false;
    protected boolean mInverted = false;
    protected float mBarWidth = 1.0F;

    public BarBuffer(int size, int dataSetCount, boolean containsStacks) {
        super(size);
        this.mDataSetCount = dataSetCount;
        this.mContainsStacks = containsStacks;
    }

    public void setBarWidth(float barWidth) {
        this.mBarWidth = barWidth;
    }

    public void setDataSet(int index) {
        this.mDataSetIndex = index;
    }

    public void setInverted(boolean inverted) {
        this.mInverted = inverted;
    }

    protected void addBar(float left, float top, float right, float bottom) {
        this.buffer[this.index++] = left;
        this.buffer[this.index++] = top;
        this.buffer[this.index++] = right;
        this.buffer[this.index++] = bottom;
    }

    public void feed(IBarDataSet data) {
        float size = (float)data.getEntryCount() * this.phaseX;
        float barWidthHalf = this.mBarWidth / 2.0F;

        for(int i = 0; (float)i < size; ++i) {
            BarEntry e = data.getEntryForIndex(i);
            if (e != null) {
                float x = e.getX();
                float y = e.getY();
                float[] vals = e.getYVals();
                float posY;
                float negY;
                float yStart;
                if (this.mContainsStacks && vals != null) {
                    posY = 0.0F;
                    negY = -e.getNegativeSum();

                    for (float value : vals) {
                        if (value == 0.0F && (posY == 0.0F || negY == 0.0F)) {
                            y = value;
                            yStart = value;
                        } else if (value >= 0.0F) {
                            y = posY;
                            yStart = posY + value;
                            posY = yStart;
                        } else {
                            y = negY;
                            yStart = negY + Math.abs(value);
                            negY += Math.abs(value);
                        }

                        float left = x - barWidthHalf;
                        float right = x + barWidthHalf;
                        float bottom;
                        float top;
                        if (this.mInverted) {
                            bottom = Math.max(y, yStart);
                            top = Math.min(y, yStart);
                        } else {
                            top = Math.max(y, yStart);
                            bottom = Math.min(y, yStart);
                        }

                        top *= this.phaseY;
                        bottom *= this.phaseY;
                        this.addBar(left, top, right, bottom);
                    }
                } else {
                    posY = x - barWidthHalf;
                    negY = x + barWidthHalf;
                    float top;
                    if (this.mInverted) {
                        yStart = Math.max(y, 0.0F);
                        top = Math.min(y, 0.0F);
                    } else {
                        top = Math.max(y, 0.0F);
                        yStart = Math.min(y, 0.0F);
                    }

                    if (top > 0.0F) {
                        top *= this.phaseY;
                    } else {
                        yStart *= this.phaseY;
                    }

                    this.addBar(posY, top, negY, yStart);
                }
            }
        }

        this.reset();
    }
}
