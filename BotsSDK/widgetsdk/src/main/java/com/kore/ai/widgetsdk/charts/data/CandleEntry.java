package com.kore.ai.widgetsdk.charts.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

@SuppressLint({"ParcelCreator"})
public class CandleEntry extends Entry {
    private float mShadowHigh = 0.0F;
    private float mShadowLow = 0.0F;
    private float mClose = 0.0F;
    private float mOpen = 0.0F;

    public CandleEntry(float x, float shadowH, float shadowL, float open, float close) {
        super(x, (shadowH + shadowL) / 2.0F);
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public CandleEntry(float x, float shadowH, float shadowL, float open, float close, Object data) {
        super(x, (shadowH + shadowL) / 2.0F, data);
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public CandleEntry(float x, float shadowH, float shadowL, float open, float close, Drawable icon) {
        super(x, (shadowH + shadowL) / 2.0F, icon);
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public CandleEntry(float x, float shadowH, float shadowL, float open, float close, Drawable icon, Object data) {
        super(x, (shadowH + shadowL) / 2.0F, icon, data);
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public float getShadowRange() {
        return Math.abs(this.mShadowHigh - this.mShadowLow);
    }

    public float getBodyRange() {
        return Math.abs(this.mOpen - this.mClose);
    }

    public float getY() {
        return super.getY();
    }

    public com.kore.ai.widgetsdk.charts.data.CandleEntry copy() {
        com.kore.ai.widgetsdk.charts.data.CandleEntry c = new com.kore.ai.widgetsdk.charts.data.CandleEntry(this.getX(), this.mShadowHigh, this.mShadowLow, this.mOpen, this.mClose, this.getData());
        return c;
    }

    public float getHigh() {
        return this.mShadowHigh;
    }

    public void setHigh(float mShadowHigh) {
        this.mShadowHigh = mShadowHigh;
    }

    public float getLow() {
        return this.mShadowLow;
    }

    public void setLow(float mShadowLow) {
        this.mShadowLow = mShadowLow;
    }

    public float getClose() {
        return this.mClose;
    }

    public void setClose(float mClose) {
        this.mClose = mClose;
    }

    public float getOpen() {
        return this.mOpen;
    }

    public void setOpen(float mOpen) {
        this.mOpen = mOpen;
    }
}
