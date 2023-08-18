package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import com.kore.ai.widgetsdk.charts.data.BubbleEntry;

public interface IBubbleDataSet extends IBarLineScatterCandleBubbleDataSet<BubbleEntry> {
    void setHighlightCircleWidth(float var1);

    float getMaxSize();

    boolean isNormalizeSizeEnabled();

    float getHighlightCircleWidth();
}

