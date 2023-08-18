package kore.botssdk.charts.interfaces.datasets;

import kore.botssdk.charts.data.BubbleEntry;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry> {
    void setHighlightCircleWidth(float var1);

    float getMaxSize();

    boolean isNormalizeSizeEnabled();

    float getHighlightCircleWidth();
}

