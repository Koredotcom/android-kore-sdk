package kore.botssdk.charts.buffer;

import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer {
    public HorizontalBarBuffer(int size, int dataSetCount, boolean containsStacks) {
        super(size, dataSetCount, containsStacks);
    }

    public void feed(IBarDataSet data) {
        float size = (float)data.getEntryCount() * this.phaseX;
        float barWidthHalf = this.mBarWidth / 2.0F;

        for(int i = 0; (float)i < size; ++i) {
            BarEntry e = (BarEntry)data.getEntryForIndex(i);
            if (e != null) {
                float x = e.getX();
                float y = e.getY();
                float[] vals = e.getYVals();
                float bottom;
                float top;
                float yStart;
                if (this.mContainsStacks && vals != null) {
                    bottom = 0.0F;
                    top = -e.getNegativeSum();
                    yStart = 0.0F;

                    for(int k = 0; k < vals.length; ++k) {
                        float value = vals[k];
                        if (value >= 0.0F) {
                            y = bottom;
                            yStart = bottom + value;
                            bottom = yStart;
                        } else {
                            y = top;
                            yStart = top + Math.abs(value);
                            top += Math.abs(value);
                        }

                        bottom = x - barWidthHalf;
                        top = x + barWidthHalf;
                        float left;
                        float right;
                        if (this.mInverted) {
                            left = y >= yStart ? y : yStart;
                            right = y <= yStart ? y : yStart;
                        } else {
                            right = y >= yStart ? y : yStart;
                            left = y <= yStart ? y : yStart;
                        }

                        right *= this.phaseY;
                        left *= this.phaseY;
                        this.addBar(left, top, right, bottom);
                    }
                } else {
                    bottom = x - barWidthHalf;
                    top = x + barWidthHalf;
                    float right;
                    if (this.mInverted) {
                        yStart = y >= 0.0F ? y : 0.0F;
                        right = y <= 0.0F ? y : 0.0F;
                    } else {
                        right = y >= 0.0F ? y : 0.0F;
                        yStart = y <= 0.0F ? y : 0.0F;
                    }

                    if (right > 0.0F) {
                        right *= this.phaseY;
                    } else {
                        yStart *= this.phaseY;
                    }

                    this.addBar(yStart, top, right, bottom);
                }
            }
        }

        this.reset();
    }
}
