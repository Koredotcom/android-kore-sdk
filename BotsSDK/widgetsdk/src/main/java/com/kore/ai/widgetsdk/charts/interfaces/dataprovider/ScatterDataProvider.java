package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {
    ScatterData getScatterData();
}
