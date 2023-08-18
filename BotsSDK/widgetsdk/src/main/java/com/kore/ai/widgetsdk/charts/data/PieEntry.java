package com.kore.ai.widgetsdk.charts.data;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class PieEntry extends Entry {
    private String label;

    public PieEntry(float value) {
        super(0.0F, value);
    }

    public PieEntry(float value, Object data) {
        super(0.0F, value, data);
    }

    public PieEntry(float value, Drawable icon) {
        super(0.0F, value, icon);
    }

    public PieEntry(float value, Drawable icon, Object data) {
        super(0.0F, value, icon, data);
    }

    public PieEntry(float value, String label) {
        super(0.0F, value);
        this.label = label;
    }

    public PieEntry(float value, String label, Object data) {
        super(0.0F, value, data);
        this.label = label;
    }

    public PieEntry(float value, String label, Drawable icon) {
        super(0.0F, value, icon);
        this.label = label;
    }

    public PieEntry(float value, String label, Drawable icon, Object data) {
        super(0.0F, value, icon, data);
        this.label = label;
    }

    public float getValue() {
        return this.getY();
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /** @deprecated */
    @Deprecated
    public void setX(float x) {
        super.setX(x);
        Log.i("DEPRECATED", "Pie entries do not have x values");
    }

    /** @deprecated */
    @Deprecated
    public float getX() {
        Log.i("DEPRECATED", "Pie entries do not have x values");
        return super.getX();
    }

    public com.kore.ai.widgetsdk.charts.data.PieEntry copy() {
        com.kore.ai.widgetsdk.charts.data.PieEntry e = new com.kore.ai.widgetsdk.charts.data.PieEntry(this.getY(), this.label, this.getData());
        return e;
    }
}
