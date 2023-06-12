package com.kore.ai.widgetsdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IScatterDataSet;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public interface IShapeRenderer {
    void renderShape(Canvas var1, IScatterDataSet var2, ViewPortHandler var3, float var4, float var5, Paint var6);
}
