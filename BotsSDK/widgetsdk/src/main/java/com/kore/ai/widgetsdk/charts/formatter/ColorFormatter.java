package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;

public interface ColorFormatter {
    int getColor(int var1, Entry var2, IDataSet var3);
}
