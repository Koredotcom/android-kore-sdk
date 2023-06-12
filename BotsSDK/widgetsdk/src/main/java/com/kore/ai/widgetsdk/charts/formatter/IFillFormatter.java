package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.LineDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.ILineDataSet;

public interface IFillFormatter {
    float getFillLinePosition(ILineDataSet var1, LineDataProvider var2);
}
