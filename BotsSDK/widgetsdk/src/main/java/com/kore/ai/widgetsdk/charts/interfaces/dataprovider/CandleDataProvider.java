package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {
    CandleData getCandleData();
}
