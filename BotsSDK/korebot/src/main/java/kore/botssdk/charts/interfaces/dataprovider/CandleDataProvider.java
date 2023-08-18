package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {
    CandleData getCandleData();
}
