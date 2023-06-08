package kore.botssdk.charts.formatter;

import kore.botssdk.charts.components.AxisBase;
import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.data.BubbleEntry;
import kore.botssdk.charts.data.CandleEntry;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.charts.data.RadarEntry;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class ValueFormatter implements IAxisValueFormatter, IValueFormatter {
    public ValueFormatter() {
    }

    /** @deprecated */
    @Deprecated
    public String getFormattedValue(float value, AxisBase axis) {
        return this.getFormattedValue(value);
    }

    /** @deprecated */
    @Deprecated
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return this.getFormattedValue(value);
    }

    public String getFormattedValue(float value) {
        return String.valueOf(value);
    }

    public String getAxisLabel(float value, AxisBase axis) {
        return this.getFormattedValue(value);
    }

    public String getBarLabel(BarEntry barEntry) {
        return this.getFormattedValue(barEntry.getY());
    }

    public String getBarStackedLabel(float value, BarEntry stackedEntry) {
        return this.getFormattedValue(value);
    }

    public String getPointLabel(Entry entry) {
        return this.getFormattedValue(entry.getY());
    }

    public String getPieLabel(float value, PieEntry pieEntry) {
        return this.getFormattedValue(value);
    }

    public String getRadarLabel(RadarEntry radarEntry) {
        return this.getFormattedValue(radarEntry.getY());
    }

    public String getBubbleLabel(BubbleEntry bubbleEntry) {
        return this.getFormattedValue(bubbleEntry.getSize());
    }

    public String getCandleLabel(CandleEntry candleEntry) {
        return this.getFormattedValue(candleEntry.getHigh());
    }
}