package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.data.CombinedData;

public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {
    CombinedData getCombinedData();
}
