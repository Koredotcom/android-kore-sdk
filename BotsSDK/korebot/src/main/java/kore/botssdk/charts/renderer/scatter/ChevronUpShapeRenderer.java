package kore.botssdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class ChevronUpShapeRenderer implements IShapeRenderer {
    public ChevronUpShapeRenderer() {
    }

    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, float posX, float posY, Paint renderPaint) {
        float shapeHalf = dataSet.getScatterShapeSize() / 2.0F;
        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0F));
        c.drawLine(posX, posY - 2.0F * shapeHalf, posX + 2.0F * shapeHalf, posY, renderPaint);
        c.drawLine(posX, posY - 2.0F * shapeHalf, posX - 2.0F * shapeHalf, posY, renderPaint);
    }
}
