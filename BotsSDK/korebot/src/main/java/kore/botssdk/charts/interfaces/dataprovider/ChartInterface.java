package kore.botssdk.charts.interfaces.dataprovider;

import android.graphics.RectF;

import kore.botssdk.charts.data.ChartData;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.utils.MPPointF;

public interface ChartInterface {
    float getXChartMin();

    float getXChartMax();

    float getXRange();

    float getYChartMin();

    float getYChartMax();

    float getMaxHighlightDistance();

    int getWidth();

    int getHeight();

    MPPointF getCenterOfView();

    MPPointF getCenterOffsets();

    RectF getContentRect();

    ValueFormatter getDefaultValueFormatter();

    ChartData getData();

    int getMaxVisibleCount();
}
