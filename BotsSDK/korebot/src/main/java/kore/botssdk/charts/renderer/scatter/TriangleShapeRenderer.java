package kore.botssdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class TriangleShapeRenderer implements IShapeRenderer {
    protected final Path mTrianglePathBuffer = new Path();

    public TriangleShapeRenderer() {
    }

    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, float posX, float posY, Paint renderPaint) {
        float shapeSize = dataSet.getScatterShapeSize();
        float shapeHalf = shapeSize / 2.0F;
        float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        float shapeHoleSize = shapeHoleSizeHalf * 2.0F;
        float shapeStrokeSize = (shapeSize - shapeHoleSize) / 2.0F;
        int shapeHoleColor = dataSet.getScatterShapeHoleColor();
        renderPaint.setStyle(Paint.Style.FILL);
        Path tri = this.mTrianglePathBuffer;
        tri.reset();
        tri.moveTo(posX, posY - shapeHalf);
        tri.lineTo(posX + shapeHalf, posY + shapeHalf);
        tri.lineTo(posX - shapeHalf, posY + shapeHalf);
        if ((double)shapeSize > 0.0D) {
            tri.lineTo(posX, posY - shapeHalf);
            tri.moveTo(posX - shapeHalf + shapeStrokeSize, posY + shapeHalf - shapeStrokeSize);
            tri.lineTo(posX + shapeHalf - shapeStrokeSize, posY + shapeHalf - shapeStrokeSize);
            tri.lineTo(posX, posY - shapeHalf + shapeStrokeSize);
            tri.lineTo(posX - shapeHalf + shapeStrokeSize, posY + shapeHalf - shapeStrokeSize);
        }

        tri.close();
        c.drawPath(tri, renderPaint);
        tri.reset();
        if ((double)shapeSize > 0.0D && shapeHoleColor != 1122867) {
            renderPaint.setColor(shapeHoleColor);
            tri.moveTo(posX, posY - shapeHalf + shapeStrokeSize);
            tri.lineTo(posX + shapeHalf - shapeStrokeSize, posY + shapeHalf - shapeStrokeSize);
            tri.lineTo(posX - shapeHalf + shapeStrokeSize, posY + shapeHalf - shapeStrokeSize);
            tri.close();
            c.drawPath(tri, renderPaint);
            tri.reset();
        }

    }
}
