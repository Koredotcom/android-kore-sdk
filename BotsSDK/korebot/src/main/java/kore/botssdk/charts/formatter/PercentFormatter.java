package kore.botssdk.charts.formatter;

import java.text.DecimalFormat;

import kore.botssdk.charts.charts.PieChart;
import kore.botssdk.charts.data.PieEntry;

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
