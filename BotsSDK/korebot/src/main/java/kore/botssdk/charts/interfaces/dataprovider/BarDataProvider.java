package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {
    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isHighlightFullBarEnabled();
}

