package kore.botssdk.charts.interfaces.datasets;

import kore.botssdk.charts.data.BarEntry;

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {
    boolean isStacked();

    int getStackSize();

    int getBarShadowColor();

    float getBarBorderWidth();

    int getBarBorderColor();

    int getHighLightAlpha();

    String[] getStackLabels();
}
