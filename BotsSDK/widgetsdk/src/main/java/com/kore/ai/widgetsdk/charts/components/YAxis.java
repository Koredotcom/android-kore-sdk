package com.kore.ai.widgetsdk.charts.components;

import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.utils.Utils;

public class YAxis extends AxisBase {
    private final boolean mDrawBottomYLabelEntry = true;
    private boolean mDrawTopYLabelEntry = true;
    protected boolean mInverted = false;
    protected boolean mDrawZeroLine = false;
    private boolean mUseAutoScaleRestrictionMin = false;
    private boolean mUseAutoScaleRestrictionMax = false;
    protected int mZeroLineColor = -7829368;
    protected float mZeroLineWidth = 1.0F;
    protected float mSpacePercentTop = 10.0F;
    protected float mSpacePercentBottom = 10.0F;
    private com.kore.ai.widgetsdk.charts.components.YAxis.YAxisLabelPosition mPosition;
    private final com.kore.ai.widgetsdk.charts.components.YAxis.AxisDependency mAxisDependency;
    protected float mMinWidth;
    protected float mMaxWidth;

    public YAxis() {
        this.mPosition = com.kore.ai.widgetsdk.charts.components.YAxis.YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0F;
        this.mMaxWidth = 1.0F / 0.0F;
        this.mAxisDependency = com.kore.ai.widgetsdk.charts.components.YAxis.AxisDependency.LEFT;
        this.mYOffset = 0.0F;
    }

    public YAxis(com.kore.ai.widgetsdk.charts.components.YAxis.AxisDependency position) {
        this.mPosition = com.kore.ai.widgetsdk.charts.components.YAxis.YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0F;
        this.mMaxWidth = 1.0F / 0.0F;
        this.mAxisDependency = position;
        this.mYOffset = 0.0F;
    }

    public com.kore.ai.widgetsdk.charts.components.YAxis.AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }

    public float getMinWidth() {
        return this.mMinWidth;
    }

    public void setMinWidth(float minWidth) {
        this.mMinWidth = minWidth;
    }

    public float getMaxWidth() {
        return this.mMaxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public com.kore.ai.widgetsdk.charts.components.YAxis.YAxisLabelPosition getLabelPosition() {
        return this.mPosition;
    }

    public void setPosition(com.kore.ai.widgetsdk.charts.components.YAxis.YAxisLabelPosition pos) {
        this.mPosition = pos;
    }

    public boolean isDrawTopYLabelEntryEnabled() {
        return this.mDrawTopYLabelEntry;
    }

    public boolean isDrawBottomYLabelEntryEnabled() {
        return this.mDrawBottomYLabelEntry;
    }

    public void setDrawTopYLabelEntry(boolean enabled) {
        this.mDrawTopYLabelEntry = enabled;
    }

    public void setInverted(boolean enabled) {
        this.mInverted = enabled;
    }

    public boolean isInverted() {
        return this.mInverted;
    }

    /** @deprecated */
    @Deprecated
    public void setStartAtZero(boolean startAtZero) {
        if (startAtZero) {
            this.setAxisMinimum(0.0F);
        } else {
            this.resetAxisMinimum();
        }

    }

    public void setSpaceTop(float percent) {
        this.mSpacePercentTop = percent;
    }

    public float getSpaceTop() {
        return this.mSpacePercentTop;
    }

    public void setSpaceBottom(float percent) {
        this.mSpacePercentBottom = percent;
    }

    public float getSpaceBottom() {
        return this.mSpacePercentBottom;
    }

    public boolean isDrawZeroLineEnabled() {
        return this.mDrawZeroLine;
    }

    public void setDrawZeroLine(boolean mDrawZeroLine) {
        this.mDrawZeroLine = mDrawZeroLine;
    }

    public int getZeroLineColor() {
        return this.mZeroLineColor;
    }

    public void setZeroLineColor(int color) {
        this.mZeroLineColor = color;
    }

    public float getZeroLineWidth() {
        return this.mZeroLineWidth;
    }

    public void setZeroLineWidth(float width) {
        this.mZeroLineWidth = Utils.convertDpToPixel(width);
    }

    public float getRequiredWidthSpace(Paint p) {
        p.setTextSize(this.mTextSize);
        String label = this.getLongestLabel();
        float width = (float)Utils.calcTextWidth(p, label) + this.getXOffset() * 2.0F;
        float minWidth = this.getMinWidth();
        float maxWidth = this.getMaxWidth();
        if (minWidth > 0.0F) {
            minWidth = Utils.convertDpToPixel(minWidth);
        }

        if (maxWidth > 0.0F && maxWidth != 1.0F / 0.0) {
            maxWidth = Utils.convertDpToPixel(maxWidth);
        }

        width = Math.max(minWidth, Math.min(width, (double)maxWidth > 0.0D ? maxWidth : width));
        return width;
    }

    public float getRequiredHeightSpace(Paint p) {
        p.setTextSize(this.mTextSize);
        String label = this.getLongestLabel();
        return (float)Utils.calcTextHeight(p, label) + this.getYOffset() * 2.0F;
    }

    public boolean needsOffset() {
        return this.isEnabled() && this.isDrawLabelsEnabled() && this.getLabelPosition() == com.kore.ai.widgetsdk.charts.components.YAxis.YAxisLabelPosition.OUTSIDE_CHART;
    }

    /** @deprecated */
    @Deprecated
    public boolean isUseAutoScaleMinRestriction() {
        return this.mUseAutoScaleRestrictionMin;
    }

    /** @deprecated */
    @Deprecated
    public void setUseAutoScaleMinRestriction(boolean isEnabled) {
        this.mUseAutoScaleRestrictionMin = isEnabled;
    }

    /** @deprecated */
    @Deprecated
    public boolean isUseAutoScaleMaxRestriction() {
        return this.mUseAutoScaleRestrictionMax;
    }

    /** @deprecated */
    @Deprecated
    public void setUseAutoScaleMaxRestriction(boolean isEnabled) {
        this.mUseAutoScaleRestrictionMax = isEnabled;
    }

    public void calculate(float dataMin, float dataMax) {
        float min = dataMin;
        float max = dataMax;
        float range = Math.abs(dataMax - dataMin);
        if (range == 0.0F) {
            max = dataMax + 1.0F;
            min = dataMin - 1.0F;
        }

        range = Math.abs(max - min);
        this.mAxisMinimum = this.mCustomAxisMin ? this.mAxisMinimum : min - range / 100.0F * this.getSpaceBottom();
        this.mAxisMaximum = this.mCustomAxisMax ? this.mAxisMaximum : max + range / 100.0F * this.getSpaceTop();
        this.mAxisRange = Math.abs(this.mAxisMinimum - this.mAxisMaximum);
    }

    public enum AxisDependency {
        LEFT,
        RIGHT;

        AxisDependency() {
        }
    }

    public enum YAxisLabelPosition {
        OUTSIDE_CHART,
        INSIDE_CHART;

        YAxisLabelPosition() {
        }
    }
}
