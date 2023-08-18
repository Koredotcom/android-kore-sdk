package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.kore.ai.widgetsdk.charts.data.Entry;

public interface ILineScatterCandleRadarDataSet<T extends Entry> extends IBarLineScatterCandleBubbleDataSet<T> {
    boolean isVerticalHighlightIndicatorEnabled();

    boolean isHorizontalHighlightIndicatorEnabled();

    float getHighlightLineWidth();

    DashPathEffect getDashPathEffectHighlight();
}
