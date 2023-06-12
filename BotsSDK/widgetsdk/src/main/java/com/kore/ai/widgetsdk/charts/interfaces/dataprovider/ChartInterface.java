package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import android.graphics.RectF;

import com.kore.ai.widgetsdk.charts.data.ChartData;
import com.kore.ai.widgetsdk.charts.formatter.ValueFormatter;
import com.kore.ai.widgetsdk.charts.utils.MPPointF;

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
