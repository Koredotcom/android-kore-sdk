package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import java.util.List;

import kore.botssdk.charts.components.LimitLine;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class YAxisRendererHorizontalBarChart extends YAxisRenderer {
    protected final Path mDrawZeroLinePathBuffer = new Path();
    protected final Path mRenderLimitLinesPathBuffer = new Path();
    protected final float[] mRenderLimitLinesBuffer = new float[4];

    public YAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
    }

    public void computeAxis(float yMin, float yMax, boolean inverted) {
        if (this.mViewPortHandler.contentHeight() > 10.0F && !this.mViewPortHandler.isFullyZoomedOutX()) {
            MPPointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            MPPointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop());
            if (!inverted) {
                yMin = (float)p1.x;
                yMax = (float)p2.x;
            } else {
                yMin = (float)p2.x;
                yMax = (float)p1.x;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        this.computeAxisValues(yMin, yMax);
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            float[] positions = this.getTransformedPositions();
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            this.mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
            float baseYOffset = Utils.convertDpToPixel(2.5F);
            float textHeight = (float)Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
            YAxis.AxisDependency dependency = this.mYAxis.getAxisDependency();
            YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
            float yPos = 0.0F;
            if (dependency == YAxis.AxisDependency.LEFT) {
                if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    yPos = this.mViewPortHandler.contentTop() - baseYOffset;
                } else {
                    yPos = this.mViewPortHandler.contentTop() - baseYOffset;
                }
            } else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                yPos = this.mViewPortHandler.contentBottom() + textHeight + baseYOffset;
            } else {
                yPos = this.mViewPortHandler.contentBottom() + textHeight + baseYOffset;
            }

            this.drawYLabels(c, yPos, positions, this.mYAxis.getYOffset());
        }
    }

    public void renderAxisLine(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
            this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
            if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
            } else {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }

        }
    }

    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
        int from = this.mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        int to = this.mYAxis.isDrawTopYLabelEntryEnabled() ? this.mYAxis.mEntryCount : this.mYAxis.mEntryCount - 1;

        for(int i = from; i < to; ++i) {
            String text = this.mYAxis.getFormattedLabel(i);
            c.drawText(text, positions[i * 2], fixedPosition - offset, this.mAxisLabelPaint);
        }

    }

    protected float[] getTransformedPositions() {
        if (this.mGetTransformedPositionsBuffer.length != this.mYAxis.mEntryCount * 2) {
            this.mGetTransformedPositionsBuffer = new float[this.mYAxis.mEntryCount * 2];
        }

        float[] positions = this.mGetTransformedPositionsBuffer;

        for(int i = 0; i < positions.length; i += 2) {
            positions[i] = this.mYAxis.mEntries[i / 2];
        }

        this.mTrans.pointValuesToPixel(positions);
        return positions;
    }

    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(-this.mAxis.getGridLineWidth(), 0.0F);
        return this.mGridClippingRect;
    }

    protected Path linePath(Path p, int i, float[] positions) {
        p.moveTo(positions[i], this.mViewPortHandler.contentTop());
        p.lineTo(positions[i], this.mViewPortHandler.contentBottom());
        return p;
    }

    protected void drawZeroLine(Canvas c) {
        int clipRestoreCount = c.save();
        this.mZeroLineClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mZeroLineClippingRect.inset(-this.mYAxis.getZeroLineWidth(), 0.0F);
        c.clipRect(this.mLimitLineClippingRect);
        MPPointD pos = this.mTrans.getPixelForValues(0.0F, 0.0F);
        this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
        this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
        Path zeroLinePath = this.mDrawZeroLinePathBuffer;
        zeroLinePath.reset();
        zeroLinePath.moveTo((float)pos.x - 1.0F, this.mViewPortHandler.contentTop());
        zeroLinePath.lineTo((float)pos.x - 1.0F, this.mViewPortHandler.contentBottom());
        c.drawPath(zeroLinePath, this.mZeroLinePaint);
        c.restoreToCount(clipRestoreCount);
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            float[] pts = this.mRenderLimitLinesBuffer;
            pts[0] = 0.0F;
            pts[1] = 0.0F;
            pts[2] = 0.0F;
            pts[3] = 0.0F;
            Path limitLinePath = this.mRenderLimitLinesPathBuffer;
            limitLinePath.reset();

            for(int i = 0; i < limitLines.size(); ++i) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    int clipRestoreCount = c.save();
                    this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
                    this.mLimitLineClippingRect.inset(-l.getLineWidth(), 0.0F);
                    c.clipRect(this.mLimitLineClippingRect);
                    pts[0] = l.getLimit();
                    pts[2] = l.getLimit();
                    this.mTrans.pointValuesToPixel(pts);
                    pts[1] = this.mViewPortHandler.contentTop();
                    pts[3] = this.mViewPortHandler.contentBottom();
                    limitLinePath.moveTo(pts[0], pts[1]);
                    limitLinePath.lineTo(pts[2], pts[3]);
                    this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
                    this.mLimitLinePaint.setColor(l.getLineColor());
                    this.mLimitLinePaint.setPathEffect(l.getDashPathEffect());
                    this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                    c.drawPath(limitLinePath, this.mLimitLinePaint);
                    limitLinePath.reset();
                    String label = l.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(l.getTextStyle());
                        this.mLimitLinePaint.setPathEffect(null);
                        this.mLimitLinePaint.setColor(l.getTextColor());
                        this.mLimitLinePaint.setTypeface(l.getTypeface());
                        this.mLimitLinePaint.setStrokeWidth(0.5F);
                        this.mLimitLinePaint.setTextSize(l.getTextSize());
                        float xOffset = l.getLineWidth() + l.getXOffset();
                        float yOffset = Utils.convertDpToPixel(2.0F) + l.getYOffset();
                        LimitLine.LimitLabelPosition position = l.getLabelPosition();
                        float labelLineHeight;
                        if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                            labelLineHeight = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, pts[0] + xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, pts[0] + xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            labelLineHeight = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                            c.drawText(label, pts[0] - xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
                        } else {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, pts[0] - xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
                        }
                    }

                    c.restoreToCount(clipRestoreCount);
                }
            }

        }
    }
}
