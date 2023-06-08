package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.data.CombinedData;
import kore.botssdk.charts.interfaces.dataprovider.BarDataProvider;
import kore.botssdk.charts.interfaces.dataprovider.BubbleDataProvider;
import kore.botssdk.charts.interfaces.dataprovider.CandleDataProvider;
import kore.botssdk.charts.interfaces.dataprovider.LineDataProvider;
import kore.botssdk.charts.interfaces.dataprovider.ScatterDataProvider;

public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {
    CombinedData getCombinedData();
}
