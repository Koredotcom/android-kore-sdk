package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {
    BubbleData getBubbleData();
}
