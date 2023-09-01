package kore.botssdk.charts.data;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.interfaces.datasets.IBarDataSet;

public class BarDataSet extends BarLineScatterCandleBubbleDataSet<BarEntry> implements IBarDataSet {
    private int mStackSize = 1;
    private int mBarShadowColor = Color.rgb(215, 215, 215);
    private float mBarBorderWidth = 0.0F;
    private int mBarBorderColor = -16777216;
    private int mHighLightAlpha = 120;
    private int mEntryCountStacks = 0;
    private String[] mStackLabels = new String[]{"Stack"};

    public BarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
        this.mHighLightColor = Color.rgb(0, 0, 0);
        this.calcStackSize(yVals);
        this.calcEntryCountIncludingStacks(yVals);
    }

    public DataSet<BarEntry> copy() {
        List<BarEntry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        BarDataSet copied = new BarDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(BarDataSet barDataSet) {
        super.copy(barDataSet);
        barDataSet.mStackSize = this.mStackSize;
        barDataSet.mBarShadowColor = this.mBarShadowColor;
        barDataSet.mBarBorderWidth = this.mBarBorderWidth;
        barDataSet.mStackLabels = this.mStackLabels;
        barDataSet.mHighLightAlpha = this.mHighLightAlpha;
    }

    private void calcEntryCountIncludingStacks(List<BarEntry> yVals) {
        this.mEntryCountStacks = 0;

        for(int i = 0; i < yVals.size(); ++i) {
            float[] vals = yVals.get(i).getYVals();
            if (vals == null) {
                ++this.mEntryCountStacks;
            } else {
                this.mEntryCountStacks += vals.length;
            }
        }

    }

    private void calcStackSize(List<BarEntry> yVals) {
        for(int i = 0; i < yVals.size(); ++i) {
            float[] vals = yVals.get(i).getYVals();
            if (vals != null && vals.length > this.mStackSize) {
                this.mStackSize = vals.length;
            }
        }

    }

    protected void calcMinMax(BarEntry e) {
        if (e != null && !Float.isNaN(e.getY())) {
            if (e.getYVals() == null) {
                if (e.getY() < this.mYMin) {
                    this.mYMin = e.getY();
                }

                if (e.getY() > this.mYMax) {
                    this.mYMax = e.getY();
                }
            } else {
                if (-e.getNegativeSum() < this.mYMin) {
                    this.mYMin = -e.getNegativeSum();
                }

                if (e.getPositiveSum() > this.mYMax) {
                    this.mYMax = e.getPositiveSum();
                }
            }

            this.calcMinMaxX(e);
        }

    }

    public int getStackSize() {
        return this.mStackSize;
    }

    public boolean isStacked() {
        return this.mStackSize > 1;
    }

    public int getEntryCountStacks() {
        return this.mEntryCountStacks;
    }

    public void setBarShadowColor(int color) {
        this.mBarShadowColor = color;
    }

    public int getBarShadowColor() {
        return this.mBarShadowColor;
    }

    public void setBarBorderWidth(float width) {
        this.mBarBorderWidth = width;
    }

    public float getBarBorderWidth() {
        return this.mBarBorderWidth;
    }

    public void setBarBorderColor(int color) {
        this.mBarBorderColor = color;
    }

    public int getBarBorderColor() {
        return this.mBarBorderColor;
    }

    public void setHighLightAlpha(int alpha) {
        this.mHighLightAlpha = alpha;
    }

    public int getHighLightAlpha() {
        return this.mHighLightAlpha;
    }

    public void setStackLabels(String[] labels) {
        this.mStackLabels = labels;
    }

    public String[] getStackLabels() {
        return this.mStackLabels;
    }
}
