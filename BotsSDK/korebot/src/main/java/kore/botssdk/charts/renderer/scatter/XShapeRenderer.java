package kore.botssdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class XShapeRenderer implements IShapeRenderer {
    public XShapeRenderer() {
    }

    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, float posX, float posY, Paint renderPaint) {
        float shapeHalf = dataSet.getScatterShapeSize() / 2.0F;
        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
        c.drawLine(posX - shapeHalf, posY - shapeHalf, posX + shapeHalf, posY + shapeHalf, renderPaint);
        c.drawLine(posX + shapeHalf, posY - shapeHalf, posX - shapeHalf, posY + shapeHalf, renderPaint);
    }
}

