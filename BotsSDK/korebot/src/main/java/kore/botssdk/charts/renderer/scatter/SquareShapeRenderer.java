package kore.botssdk.charts.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import kore.botssdk.charts.interfaces.datasets.IScatterDataSet;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class SquareShapeRenderer implements IShapeRenderer {
    public SquareShapeRenderer() {
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
            c.drawRect(posX - shapeHoleSizeHalf - shapeStrokeSizeHalf, posY - shapeHoleSizeHalf - shapeStrokeSizeHalf, posX + shapeHoleSizeHalf + shapeStrokeSizeHalf, posY + shapeHoleSizeHalf + shapeStrokeSizeHalf, renderPaint);
            if (shapeHoleColor != 1122867) {
                renderPaint.setStyle(Paint.Style.FILL);
                renderPaint.setColor(shapeHoleColor);
                c.drawRect(posX - shapeHoleSizeHalf, posY - shapeHoleSizeHalf, posX + shapeHoleSizeHalf, posY + shapeHoleSizeHalf, renderPaint);
            }
        } else {
            renderPaint.setStyle(Paint.Style.FILL);
            c.drawRect(posX - shapeHalf, posY - shapeHalf, posX + shapeHalf, posY + shapeHalf, renderPaint);
        }

    }
}
