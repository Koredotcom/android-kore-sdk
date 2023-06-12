package com.kore.ai.widgetsdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IScatterDataSet;
import com.kore.ai.widgetsdk.charts.utils.Utils;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public class CircleShapeRenderer implements IShapeRenderer {
    public CircleShapeRenderer() {
    }

    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, float posX, float posY, Paint renderPaint) {
        float shapeSize = dataSet.getScatterShapeSize();
        float shapeHalf = shapeSize / 2.0F;
        float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        float shapeHoleSize = shapeHoleSizeHalf * 2.0F;
        float shapeStrokeSize = (shapeSize - shapeHoleSize) / 2.0F;
        float shapeStrokeSizeHalf = shapeStrokeSize / 2.0F;
        int shapeHoleColor = dataSet.getScatterShapeHoleColor();
        if ((double)shapeSize > 0.0D) {
            renderPaint.setStyle(Paint.Style.STROKE);
            renderPaint.setStrokeWidth(shapeStrokeSize);
            c.drawCircle(posX, posY, shapeHoleSizeHalf + shapeStrokeSizeHalf, renderPaint);
            if (shapeHoleColor != 1122867) {
                renderPaint.setStyle(Paint.Style.FILL);
                renderPaint.setColor(shapeHoleColor);
                c.drawCircle(posX, posY, shapeHoleSizeHalf, renderPaint);
            }
        } else {
            renderPaint.setStyle(Paint.Style.FILL);
            c.drawCircle(posX, posY, shapeHalf, renderPaint);
        }

    }
}

