package kore.botssdk.charts.interfaces.datasets;

import android.graphics.drawable.Drawable;

import kore.botssdk.charts.data.Entry;

public interface ILineRadarDataSet<T extends Entry> extends ILineScatterCandleRadarDataSet<T> {
    int getFillColor();

    Drawable getFillDrawable();

    int getFillAlpha();

    float getLineWidth();

    boolean isDrawFilledEnabled();

    void setDrawFilled(boolean var1);
}
