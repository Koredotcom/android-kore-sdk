package com.kore.ai.widgetsdk.charts.components;

import com.kore.ai.widgetsdk.charts.utils.Utils;

public class XAxis extends AxisBase {
    public int mLabelWidth = 1;
    public int mLabelHeight = 1;
    public int mLabelRotatedWidth = 1;
    public int mLabelRotatedHeight = 1;
    protected float mLabelRotationAngle = 0.0F;
    private boolean mAvoidFirstLastClipping = false;
    private com.kore.ai.widgetsdk.charts.components.XAxis.XAxisPosition mPosition;

    public XAxis() {
        this.mPosition = com.kore.ai.widgetsdk.charts.components.XAxis.XAxisPosition.TOP;
        this.mYOffset = Utils.convertDpToPixel(4.0F);
    }

    public com.kore.ai.widgetsdk.charts.components.XAxis.XAxisPosition getPosition() {
        return this.mPosition;
    }

    public void setPosition(com.kore.ai.widgetsdk.charts.components.XAxis.XAxisPosition pos) {
        this.mPosition = pos;
    }

    public float getLabelRotationAngle() {
        return this.mLabelRotationAngle;
    }

    public void setLabelRotationAngle(float angle) {
        this.mLabelRotationAngle = angle;
    }

    public void setAvoidFirstLastClipping(boolean enabled) {
        this.mAvoidFirstLastClipping = enabled;
    }

    public boolean isAvoidFirstLastClippingEnabled() {
        return this.mAvoidFirstLastClipping;
    }

    public enum XAxisPosition {
        TOP,
        BOTTOM,
        BOTH_SIDED,
        TOP_INSIDE,
        BOTTOM_INSIDE;

        XAxisPosition() {
        }
    }
}
