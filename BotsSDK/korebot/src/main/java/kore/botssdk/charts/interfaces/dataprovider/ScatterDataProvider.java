package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {
    ScatterData getScatterData();
}
