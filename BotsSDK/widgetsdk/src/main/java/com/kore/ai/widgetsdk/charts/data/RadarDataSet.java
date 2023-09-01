package com.kore.ai.widgetsdk.charts.data;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

public class RadarDataSet extends LineRadarDataSet<RadarEntry> implements IRadarDataSet {
    protected boolean mDrawHighlightCircleEnabled = false;
    protected int mHighlightCircleFillColor = -1;
    protected int mHighlightCircleStrokeColor = 1122867;
    protected int mHighlightCircleStrokeAlpha = 76;
    protected float mHighlightCircleInnerRadius = 3.0F;
    protected float mHighlightCircleOuterRadius = 4.0F;
    protected float mHighlightCircleStrokeWidth = 2.0F;

    public RadarDataSet(List<RadarEntry> yVals, String label) {
        super(yVals, label);
    }

    public boolean isDrawHighlightCircleEnabled() {
        return this.mDrawHighlightCircleEnabled;
    }

    public void setDrawHighlightCircleEnabled(boolean enabled) {
        this.mDrawHighlightCircleEnabled = enabled;
    }

    public int getHighlightCircleFillColor() {
        return this.mHighlightCircleFillColor;
    }

    public void setHighlightCircleFillColor(int color) {
        this.mHighlightCircleFillColor = color;
    }

    public int getHighlightCircleStrokeColor() {
        return this.mHighlightCircleStrokeColor;
    }

    public void setHighlightCircleStrokeColor(int color) {
        this.mHighlightCircleStrokeColor = color;
    }

    public int getHighlightCircleStrokeAlpha() {
        return this.mHighlightCircleStrokeAlpha;
    }

    public void setHighlightCircleStrokeAlpha(int alpha) {
        this.mHighlightCircleStrokeAlpha = alpha;
    }

    public float getHighlightCircleInnerRadius() {
        return this.mHighlightCircleInnerRadius;
    }

    public void setHighlightCircleInnerRadius(float radius) {
        this.mHighlightCircleInnerRadius = radius;
    }

    public float getHighlightCircleOuterRadius() {
        return this.mHighlightCircleOuterRadius;
    }

    public void setHighlightCircleOuterRadius(float radius) {
        this.mHighlightCircleOuterRadius = radius;
    }

    public float getHighlightCircleStrokeWidth() {
        return this.mHighlightCircleStrokeWidth;
    }

    public void setHighlightCircleStrokeWidth(float strokeWidth) {
        this.mHighlightCircleStrokeWidth = strokeWidth;
    }

    public DataSet<RadarEntry> copy() {
        List<RadarEntry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        com.kore.ai.widgetsdk.charts.data.RadarDataSet copied = new com.kore.ai.widgetsdk.charts.data.RadarDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(RadarDataSet radarDataSet) {
        super.copy(radarDataSet);
        radarDataSet.mDrawHighlightCircleEnabled = this.mDrawHighlightCircleEnabled;
        radarDataSet.mHighlightCircleFillColor = this.mHighlightCircleFillColor;
        radarDataSet.mHighlightCircleInnerRadius = this.mHighlightCircleInnerRadius;
        radarDataSet.mHighlightCircleStrokeAlpha = this.mHighlightCircleStrokeAlpha;
        radarDataSet.mHighlightCircleStrokeColor = this.mHighlightCircleStrokeColor;
        radarDataSet.mHighlightCircleStrokeWidth = this.mHighlightCircleStrokeWidth;
    }
}
