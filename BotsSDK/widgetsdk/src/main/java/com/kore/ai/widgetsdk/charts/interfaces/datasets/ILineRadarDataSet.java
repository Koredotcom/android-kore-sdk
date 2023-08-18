package com.kore.ai.widgetsdk.charts.interfaces.datasets;

import android.graphics.drawable.Drawable;

import com.kore.ai.widgetsdk.charts.data.Entry;

public interface ILineRadarDataSet<T extends Entry> extends ILineScatterCandleRadarDataSet<T> {
    int getFillColor();

    Drawable getFillDrawable();

    int getFillAlpha();

    float getLineWidth();

    boolean isDrawFilledEnabled();

    void setDrawFilled(boolean var1);
}
