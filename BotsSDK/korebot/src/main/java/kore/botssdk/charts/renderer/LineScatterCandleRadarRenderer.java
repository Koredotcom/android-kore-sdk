package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import kore.botssdk.charts.animation.ChartAnimator;
import kore.botssdk.charts.interfaces.datasets.ILineScatterCandleRadarDataSet;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer {
    private final Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {
        this.mHighlightPaint.setColor(set.getHighLightColor());
        this.mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());
        this.mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());
        if (set.isVerticalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(x, this.mViewPortHandler.contentTop());
            this.mHighlightLinePath.lineTo(x, this.mViewPortHandler.contentBottom());
            c.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }

        if (set.isHorizontalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(this.mViewPortHandler.contentLeft(), y);
            this.mHighlightLinePath.lineTo(this.mViewPortHandler.contentRight(), y);
            c.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }

    }
}
