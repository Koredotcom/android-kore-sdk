package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.charts.PieChart;
import com.kore.ai.widgetsdk.charts.data.PieEntry;

import java.text.DecimalFormat;

public class PercentFormatter extends ValueFormatter {
    public final DecimalFormat mFormat;
    private PieChart pieChart;

    public PercentFormatter() {
        this.mFormat = new DecimalFormat("###,###,##0.0");
    }

    public PercentFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    public String getFormattedValue(float value) {
        return this.mFormat.format(value) + " %";
    }

    public String getPieLabel(float value, PieEntry pieEntry) {
        return this.pieChart != null && this.pieChart.isUsePercentValuesEnabled() ? this.getFormattedValue(value) : this.mFormat.format(value);
    }
}
