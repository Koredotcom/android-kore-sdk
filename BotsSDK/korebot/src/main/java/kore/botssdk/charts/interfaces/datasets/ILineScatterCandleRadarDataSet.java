package kore.botssdk.charts.interfaces.datasets;

import android.graphics.DashPathEffect;

import kore.botssdk.charts.data.Entry;

public interface ILineScatterCandleRadarDataSet<T extends Entry> extends IBarLineScatterCandleBubbleDataSet<T> {
    boolean isVerticalHighlightIndicatorEnabled();

    boolean isHorizontalHighlightIndicatorEnabled();

    float getHighlightLineWidth();

    DashPathEffect getDashPathEffectHighlight();
}
