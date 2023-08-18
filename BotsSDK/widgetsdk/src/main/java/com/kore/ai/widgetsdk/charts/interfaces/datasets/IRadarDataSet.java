package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import com.kore.ai.widgetsdk.charts.data.RadarEntry;

public interface IRadarDataSet extends ILineRadarDataSet<RadarEntry> {
    boolean isDrawHighlightCircleEnabled();

    void setDrawHighlightCircleEnabled(boolean var1);

    int getHighlightCircleFillColor();

    int getHighlightCircleStrokeColor();

    int getHighlightCircleStrokeAlpha();

    float getHighlightCircleInnerRadius();

    float getHighlightCircleOuterRadius();

    float getHighlightCircleStrokeWidth();
}
