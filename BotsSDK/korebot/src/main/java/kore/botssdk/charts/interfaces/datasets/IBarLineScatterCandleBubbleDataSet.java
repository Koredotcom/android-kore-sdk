package kore.botssdk.charts.interfaces.datasets;

import kore.botssdk.charts.data.Entry;

public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {
    int getHighLightColor();
}
