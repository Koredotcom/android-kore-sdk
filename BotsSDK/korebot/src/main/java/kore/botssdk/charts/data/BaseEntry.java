package kore.botssdk.charts.data;

import android.graphics.drawable.Drawable;

public abstract class BaseEntry {
    private float y;
    private Object mData;
    private Drawable mIcon;

    public BaseEntry() {
        this.y = 0.0F;
        this.mData = null;
        this.mIcon = null;
    }

    public BaseEntry(float y) {
        this.y = 0.0F;
        this.mData = null;
        this.mIcon = null;
        this.y = y;
    }

    public BaseEntry(float y, Object data) {
        this(y);
        this.mData = data;
    }

    public BaseEntry(float y, Drawable icon) {
        this(y);
        this.mIcon = icon;
    }

    public BaseEntry(float y, Drawable icon, Object data) {
        this(y);
        this.mIcon = icon;
        this.mData = data;
    }

    public float getY() {
        return this.y;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Object getData() {
        return this.mData;
    }

    public void setData(Object data) {
        this.mData = data;
    }
}
