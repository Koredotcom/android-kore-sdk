package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public interface IValueFormatter {
    String getFormattedValue(float var1, Entry var2, int var3, ViewPortHandler var4);
}

