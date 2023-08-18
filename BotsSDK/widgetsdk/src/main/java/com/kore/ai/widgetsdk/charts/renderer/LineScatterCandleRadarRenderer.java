package com.kore.ai.widgetsdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import com.kore.ai.widgetsdk.charts.animation.ChartAnimator;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

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
