package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {
    BubbleData getBubbleData();
}
