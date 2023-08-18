package kore.botssdk.charts.components;

import android.graphics.Typeface;

import kore.botssdk.charts.utils.Utils;

public abstract class ComponentBase {
    protected boolean mEnabled = true;
    protected float mXOffset = 5.0F;
    protected float mYOffset = 5.0F;
    protected Typeface mTypeface = null;
    protected float mTextSize = Utils.convertDpToPixel(10.0F);
    protected int mTextColor = -16777216;

    public ComponentBase() {
    }

    public float getXOffset() {
        return this.mXOffset;
    }

    public void setXOffset(float xOffset) {
        this.mXOffset = Utils.convertDpToPixel(xOffset);
    }

    public float getYOffset() {
        return this.mYOffset;
    }

    public void setYOffset(float yOffset) {
        this.mYOffset = Utils.convertDpToPixel(yOffset);
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public void setTypeface(Typeface tf) {
        this.mTypeface = tf;
    }

    public void setTextSize(float size) {
        if (size > 24.0F) {
            size = 24.0F;
        }

        if (size < 6.0F) {
            size = 6.0F;
        }

        this.mTextSize = Utils.convertDpToPixel(size);
    }

    public float getTextSize() {
        return this.mTextSize;
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }
}
