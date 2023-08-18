package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer {
    public LineRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    protected void drawFilledPath(Canvas c, Path filledPath, Drawable drawable) {
        if (this.clipPathSupported()) {
            int save = c.save();
            c.clipPath(filledPath);
            drawable.setBounds((int)this.mViewPortHandler.contentLeft(), (int)this.mViewPortHandler.contentTop(), (int)this.mViewPortHandler.contentRight(), (int)this.mViewPortHandler.contentBottom());
            drawable.draw(c);
            c.restoreToCount(save);
        } else {
            throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, this code was run on API level " + Utils.getSDKInt() + ".");
        }
    }

    protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {
        int color = fillAlpha << 24 | fillColor & 16777215;
        if (this.clipPathSupported()) {
            int save = c.save();
            c.clipPath(filledPath);
            c.drawColor(color);
            c.restoreToCount(save);
        } else {
            Paint.Style previous = this.mRenderPaint.getStyle();
            int previousColor = this.mRenderPaint.getColor();
            this.mRenderPaint.setStyle(Paint.Style.FILL);
            this.mRenderPaint.setColor(color);
            c.drawPath(filledPath, this.mRenderPaint);
            this.mRenderPaint.setColor(previousColor);
            this.mRenderPaint.setStyle(previous);
        }

    }

    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }
}
