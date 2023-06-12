package com.kore.ai.widgetsdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IScatterDataSet;
import com.kore.ai.widgetsdk.charts.utils.Utils;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public class CrossShapeRenderer implements IShapeRenderer {
    public CrossShapeRenderer() {
    }

    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, float posX, float posY, Paint renderPaint) {
        float shapeHalf = dataSet.getScatterShapeSize() / 2.0F;
        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
        c.drawLine(posX - shapeHalf, posY, posX + shapeHalf, posY, renderPaint);
        c.drawLine(posX, posY - shapeHalf, posX, posY + shapeHalf, renderPaint);
    }
}
