package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.data.LineDataSet;
import com.kore.ai.widgetsdk.charts.formatter.IFillFormatter;

public interface ILineDataSet extends ILineRadarDataSet<Entry> {
    LineDataSet.Mode getMode();

    float getCubicIntensity();

    /** @deprecated */
    @Deprecated
    boolean isDrawCubicEnabled();

    /** @deprecated */
    @Deprecated
    boolean isDrawSteppedEnabled();

    float getCircleRadius();

    float getCircleHoleRadius();

    int getCircleColor(int var1);

    int getCircleColorCount();

    boolean isDrawCirclesEnabled();

    int getCircleHoleColor();

    boolean isDrawCircleHoleEnabled();

    DashPathEffect getDashPathEffect();

    boolean isDashedLineEnabled();

    IFillFormatter getFillFormatter();
}
