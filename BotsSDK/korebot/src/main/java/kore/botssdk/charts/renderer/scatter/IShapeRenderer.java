package kore.botssdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.utils.ViewPortHandler;

public interface IShapeRenderer {
    void renderShape(Canvas var1, IScatterDataSet var2, ViewPortHandler var3, float var4, float var5, Paint var6);
}
