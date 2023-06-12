package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {
    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isHighlightFullBarEnabled();
}

