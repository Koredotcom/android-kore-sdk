package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import java.util.List;

import kore.botssdk.charts.charts.BarChart;
import kore.botssdk.charts.components.LimitLine;
import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.utils.FSize;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class XAxisRendererHorizontalBarChart extends XAxisRenderer {
    protected final BarChart mChart;
    protected final Path mRenderLimitLinesPathBuffer = new Path();

    public XAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans, BarChart chart) {
        super(viewPortHandler, xAxis, trans);
        this.mChart = chart;
    }

    public void computeAxis(float min, float max, boolean inverted) {
        if (this.mViewPortHandler.contentWidth() > 10.0F && !this.mViewPortHandler.isFullyZoomedOutY()) {
            MPPointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
            MPPointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            if (inverted) {
                min = (float)p2.y;
                max = (float)p1.y;
            } else {
                min = (float)p1.y;
                max = (float)p2.y;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        this.computeAxisValues(min, max);
    }

    protected void computeSize() {
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        String longest = this.mXAxis.getLongestLabel();
        FSize labelSize = Utils.calcTextSize(this.mAxisLabelPaint, longest);
        float labelWidth = (float)((int)(labelSize.width + this.mXAxis.getXOffset() * 3.5F));
        float labelHeight = labelSize.height;
        FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelSize.width, labelHeight, this.mXAxis.getLabelRotationAngle());
        this.mXAxis.mLabelWidth = Math.round(labelWidth);
        this.mXAxis.mLabelHeight = Math.round(labelHeight);
        this.mXAxis.mLabelRotatedWidth = (int)(labelRotatedSize.width + this.mXAxis.getXOffset() * 3.5F);
        this.mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);
        FSize.recycleInstance(labelRotatedSize);
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            float xoffset = this.mXAxis.getXOffset();
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            MPPointF pointF = MPPointF.getInstance(0.0F, 0.0F);
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                pointF.x = 0.0F;
                pointF.y = 0.5F;
                this.drawLabels(c, this.mViewPortHandler.contentRight() + xoffset, pointF);
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
                pointF.x = 1.0F;
                pointF.y = 0.5F;
                this.drawLabels(c, this.mViewPortHandler.contentRight() - xoffset, pointF);
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                pointF.x = 1.0F;
                pointF.y = 0.5F;
                this.drawLabels(c, this.mViewPortHandler.contentLeft() - xoffset, pointF);
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
                pointF.x = 1.0F;
                pointF.y = 0.5F;
                this.drawLabels(c, this.mViewPortHandler.contentLeft() + xoffset, pointF);
            } else {
                pointF.x = 0.0F;
                pointF.y = 0.5F;
                this.drawLabels(c, this.mViewPortHandler.contentRight() + xoffset, pointF);
                pointF.x = 1.0F;
                pointF.y = 0.5F;
                this.drawLabels(c, this.mViewPortHandler.contentLeft() - xoffset, pointF);
            }

            MPPointF.recycleInstance(pointF);
        }
    }

    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = this.mXAxis.isCenterAxisLabelsEnabled();
        float[] positions = new float[this.mXAxis.mEntryCount * 2];

        int i;
        for(i = 0; i < positions.length; i += 2) {
            if (centeringEnabled) {
                positions[i + 1] = this.mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i + 1] = this.mXAxis.mEntries[i / 2];
            }
        }

        this.mTrans.pointValuesToPixel(positions);

        for(i = 0; i < positions.length; i += 2) {
            float y = positions[i + 1];
            if (this.mViewPortHandler.isInBoundsY(y)) {
                String label = this.mXAxis.getValueFormatter().getAxisLabel(this.mXAxis.mEntries[i / 2], this.mXAxis);
                this.drawLabel(c, label, pos, y, anchor, labelRotationAngleDegrees);
            }
        }

    }

    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(0.0F, -this.mAxis.getGridLineWidth());
        return this.mGridClippingRect;
    }

    protected void drawGridLine(Canvas c, float x, float y, Path gridLinePath) {
        gridLinePath.moveTo(this.mViewPortHandler.contentRight(), y);
        gridLinePath.lineTo(this.mViewPortHandler.contentLeft(), y);
        c.drawPath(gridLinePath, this.mGridPaint);
        gridLinePath.reset();
    }

    public void renderAxisLine(Canvas c) {
        if (this.mXAxis.isDrawAxisLineEnabled() && this.mXAxis.isEnabled()) {
            this.mAxisLinePaint.setColor(this.mXAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getAxisLineWidth());
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP || this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                c.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }

            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }

        }
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mXAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            float[] pts = this.mRenderLimitLinesBuffer;
            pts[0] = 0.0F;
            pts[1] = 0.0F;
            Path limitLinePath = this.mRenderLimitLinesPathBuffer;
            limitLinePath.reset();

            for(int i = 0; i < limitLines.size(); ++i) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    int clipRestoreCount = c.save();
                    this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
                    this.mLimitLineClippingRect.inset(0.0F, -l.getLineWidth());
                    c.clipRect(this.mLimitLineClippingRect);
                    this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
                    this.mLimitLinePaint.setColor(l.getLineColor());
                    this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                    this.mLimitLinePaint.setPathEffect(l.getDashPathEffect());
                    pts[1] = l.getLimit();
                    this.mTrans.pointValuesToPixel(pts);
                    limitLinePath.moveTo(this.mViewPortHandler.contentLeft(), pts[1]);
                    limitLinePath.lineTo(this.mViewPortHandler.contentRight(), pts[1]);
                    c.drawPath(limitLinePath, this.mLimitLinePaint);
                    limitLinePath.reset();
                    String label = l.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(l.getTextStyle());
                        this.mLimitLinePaint.setPathEffect(null);
                        this.mLimitLinePaint.setColor(l.getTextColor());
                        this.mLimitLinePaint.setStrokeWidth(0.5F);
                        this.mLimitLinePaint.setTextSize(l.getTextSize());
                        float labelLineHeight = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                        float xOffset = Utils.convertDpToPixel(4.0F) + l.getXOffset();
                        float yOffset = l.getLineWidth() + labelLineHeight + l.getYOffset();
                        LimitLine.LimitLabelPosition position = l.getLabelPosition();
                        if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, this.mViewPortHandler.contentRight() - xOffset, pts[1] - yOffset + labelLineHeight, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, this.mViewPortHandler.contentRight() - xOffset, pts[1] + yOffset, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, this.mViewPortHandler.contentLeft() + xOffset, pts[1] - yOffset + labelLineHeight, this.mLimitLinePaint);
                        } else {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, this.mViewPortHandler.offsetLeft() + xOffset, pts[1] + yOffset, this.mLimitLinePaint);
                        }
                    }

                    c.restoreToCount(clipRestoreCount);
                }
            }

        }
    }
}
