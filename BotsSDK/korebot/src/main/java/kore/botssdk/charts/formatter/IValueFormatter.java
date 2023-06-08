package kore.botssdk.charts.formatter;

import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.utils.ViewPortHandler;

public interface IValueFormatter {
    String getFormattedValue(float var1, Entry var2, int var3, ViewPortHandler var4);
}

