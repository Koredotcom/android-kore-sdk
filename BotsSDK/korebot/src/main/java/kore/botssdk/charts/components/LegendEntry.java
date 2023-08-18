package kore.botssdk.charts.components;

import android.graphics.DashPathEffect;

public class LegendEntry {
    public String label;
    public Legend.LegendForm form;
    public float formSize;
    public float formLineWidth;
    public DashPathEffect formLineDashEffect;
    public int formColor;

    public LegendEntry() {
        this.form = Legend.LegendForm.DEFAULT;
        this.formSize = 0.0F / 0.0F;
        this.formLineWidth = 0.0F / 0.0F;
        this.formLineDashEffect = null;
        this.formColor = 1122867;
    }

    public LegendEntry(String label, Legend.LegendForm form, float formSize, float formLineWidth, DashPathEffect formLineDashEffect, int formColor) {
        this.form = Legend.LegendForm.DEFAULT;
        this.formSize = 0.0F / 0.0F;
        this.formLineWidth = 0.0F / 0.0F;
        this.formLineDashEffect = null;
        this.formColor = 1122867;
        this.label = label;
        this.form = form;
        this.formSize = formSize;
        this.formLineWidth = formLineWidth;
        this.formLineDashEffect = formLineDashEffect;
        this.formColor = formColor;
    }
}
