package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.components.AxisBase;
import com.kore.ai.widgetsdk.charts.data.BarEntry;
import com.kore.ai.widgetsdk.charts.data.BubbleEntry;
import com.kore.ai.widgetsdk.charts.data.CandleEntry;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.data.PieEntry;
import com.kore.ai.widgetsdk.charts.data.RadarEntry;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

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