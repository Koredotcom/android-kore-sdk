package com.kore.ai.widgetsdk.charts.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.utils.Utils;

public class LimitLine extends ComponentBase {
    private float mLimit = 0.0F;
    private float mLineWidth = 2.0F;
    private int mLineColor = Color.rgb(237, 91, 91);
    private Paint.Style mTextStyle;
    private String mLabel;
    private DashPathEffect mDashPathEffect;
    private com.kore.ai.widgetsdk.charts.components.LimitLine.LimitLabelPosition mLabelPosition;

    public LimitLine(float limit) {
        this.mTextStyle = Paint.Style.FILL_AND_STROKE;
        this.mLabel = "";
        this.mDashPathEffect = null;
        this.mLabelPosition = com.kore.ai.widgetsdk.charts.components.LimitLine.LimitLabelPosition.RIGHT_TOP;
        this.mLimit = limit;
    }

    public LimitLine(float limit, String label) {
        this.mTextStyle = Paint.Style.FILL_AND_STROKE;
        this.mLabel = "";
        this.mDashPathEffect = null;
        this.mLabelPosition = com.kore.ai.widgetsdk.charts.components.LimitLine.LimitLabelPosition.RIGHT_TOP;
        this.mLimit = limit;
        this.mLabel = label;
    }

    public float getLimit() {
        return this.mLimit;
    }

    public void setLineWidth(float width) {
        if (width < 0.2F) {
            width = 0.2F;
        }

        if (width > 12.0F) {
            width = 12.0F;
        }

        this.mLineWidth = Utils.convertDpToPixel(width);
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public void setLineColor(int color) {
        this.mLineColor = color;
    }

    public int getLineColor() {
        return this.mLineColor;
    }

    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        this.mDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }

    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }

    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }

    public void setTextStyle(Paint.Style style) {
        this.mTextStyle = style;
    }

    public Paint.Style getTextStyle() {
        return this.mTextStyle;
    }

    public void setLabelPosition(com.kore.ai.widgetsdk.charts.components.LimitLine.LimitLabelPosition pos) {
        this.mLabelPosition = pos;
    }

    public com.kore.ai.widgetsdk.charts.components.LimitLine.LimitLabelPosition getLabelPosition() {
        return this.mLabelPosition;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public enum LimitLabelPosition {
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM;

        LimitLabelPosition() {
        }
    }
}
