package com.kore.ai.widgetsdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IScatterDataSet;
import com.kore.ai.widgetsdk.charts.utils.Utils;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public class ChevronDownShapeRenderer implements IShapeRenderer {
    public ChevronDownShapeRenderer() {
    }

    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, float posX, float posY, Paint renderPaint) {
        float shapeHalf = dataSet.getScatterShapeSize() / 2.0F;
        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
        c.drawLine(posX, posY + 2.0F * shapeHalf, posX + 2.0F * shapeHalf, posY, renderPaint);
        c.drawLine(posX, posY + 2.0F * shapeHalf, posX - 2.0F * shapeHalf, posY, renderPaint);
    }
}
