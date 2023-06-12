package com.kore.ai.widgetsdk.charts.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

@SuppressLint({"ParcelCreator"})
public class BubbleEntry extends Entry {
    private float mSize = 0.0F;

    public BubbleEntry(float x, float y, float size) {
        super(x, y);
        this.mSize = size;
    }

    public BubbleEntry(float x, float y, float size, Object data) {
        super(x, y, data);
        this.mSize = size;
    }

    public BubbleEntry(float x, float y, float size, Drawable icon) {
        super(x, y, icon);
        this.mSize = size;
    }

    public BubbleEntry(float x, float y, float size, Drawable icon, Object data) {
        super(x, y, icon, data);
        this.mSize = size;
    }

    public com.kore.ai.widgetsdk.charts.data.BubbleEntry copy() {
        com.kore.ai.widgetsdk.charts.data.BubbleEntry c = new com.kore.ai.widgetsdk.charts.data.BubbleEntry(this.getX(), this.getY(), this.mSize, this.getData());
        return c;
    }

    public float getSize() {
        return this.mSize;
    }

    public void setSize(float size) {
        this.mSize = size;
    }
}
