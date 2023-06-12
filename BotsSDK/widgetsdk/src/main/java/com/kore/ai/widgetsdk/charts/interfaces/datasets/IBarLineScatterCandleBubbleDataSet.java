package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import com.kore.ai.widgetsdk.charts.data.Entry;

public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {
    int getHighLightColor();
}
