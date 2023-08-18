package kore.botssdk.charts.interfaces.datasets;

import android.graphics.DashPathEffect;

import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.LineDataSet;
import kore.botssdk.charts.formatter.IFillFormatter;

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
