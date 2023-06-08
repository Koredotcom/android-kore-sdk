package kore.botssdk.charts.interfaces.datasets;

import kore.botssdk.charts.data.RadarEntry;
import kore.botssdk.charts.interfaces.datasets.ILineRadarDataSet;

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
