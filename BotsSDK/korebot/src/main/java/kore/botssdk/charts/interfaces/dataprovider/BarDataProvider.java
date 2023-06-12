package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {
    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isHighlightFullBarEnabled();
}

