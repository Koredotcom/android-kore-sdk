package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import java.util.List;

import kore.botssdk.charts.components.LimitLine;
import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.utils.FSize;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class XAxisRenderer extends AxisRenderer {
    protected final XAxis mXAxis;
    protected final Path mRenderGridLinesPath = new Path();
    protected float[] mRenderGridLinesBuffer = new float[2];
    protected final RectF mGridClippingRect = new RectF();
    protected final float[] mRenderLimitLinesBuffer = new float[2];
    protected final RectF mLimitLineClippingRect = new RectF();
    final float[] mLimitLineSegmentsBuffer = new float[4];
    private final Path mLimitLinePath = new Path();

    public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, trans, xAxis);
        this.mXAxis = xAxis;
        this.mAxisLabelPaint.setColor(-16777216);
        this.mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
        this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0F));
    }

    protected void setupGridPaint() {
        this.mGridPaint.setColor(this.mXAxis.getGridColor());
        this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
        this.mGridPaint.setPathEffect(this.mXAxis.getGridDashPathEffect());
    }

    public void computeAxis(float min, float max, boolean inverted) {
        if (this.mViewPortHandler.contentWidth() > 10.0F && !this.mViewPortHandler.isFullyZoomedOutX()) {
            MPPointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            MPPointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop());
            if (inverted) {
                min = (float)p2.x;
                max = (float)p1.x;
            } else {
                min = (float)p1.x;
                max = (float)p2.x;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        this.computeAxisValues(min, max);
    }

    protected void computeAxisValues(float min, float max) {
        super.computeAxisValues(min, max);
        this.computeSize();
    }

    protected void computeSize() {
        String longest = this.mXAxis.getLongestLabel();
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        FSize labelSize = Utils.calcTextSize(this.mAxisLabelPaint, longest);
        float labelWidth = labelSize.width;
        float labelHeight = (float)Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
        FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelWidth, labelHeight, this.mXAxis.getLabelRotationAngle());
        this.mXAxis.mLabelWidth = Math.round(labelWidth);
        this.mXAxis.mLabelHeight = Math.round(labelHeight);
        this.mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width);
        this.mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);
        FSize.recycleInstance(labelRotatedSize);
        FSize.recycleInstance(labelSize);
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            float yoffset = this.mXAxis.getYOffset();
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            MPPointF pointF = MPPointF.getInstance(0.0F, 0.0F);
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                pointF.x = 0.5F;
                pointF.y = 1.0F;
                this.drawLabels(c, this.mViewPortHandler.contentTop() - yoffset, pointF);
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
                pointF.x = 0.5F;
                pointF.y = 1.0F;
                this.drawLabels(c, this.mViewPortHandler.contentTop() + yoffset + (float)this.mXAxis.mLabelRotatedHeight, pointF);
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                pointF.x = 0.5F;
                pointF.y = 0.0F;
                this.drawLabels(c, this.mViewPortHandler.contentBottom() + yoffset, pointF);
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
                pointF.x = 0.5F;
                pointF.y = 0.0F;
                this.drawLabels(c, this.mViewPortHandler.contentBottom() - yoffset - (float)this.mXAxis.mLabelRotatedHeight, pointF);
            } else {
                pointF.x = 0.5F;
                pointF.y = 1.0F;
                this.drawLabels(c, this.mViewPortHandler.contentTop() - yoffset, pointF);
                pointF.x = 0.5F;
                pointF.y = 0.0F;
                this.drawLabels(c, this.mViewPortHandler.contentBottom() + yoffset, pointF);
            }

            MPPointF.recycleInstance(pointF);
        }
    }

    public void renderAxisLine(Canvas c) {
        if (this.mXAxis.isDrawAxisLineEnabled() && this.mXAxis.isEnabled()) {
            this.mAxisLinePaint.setColor(this.mXAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getAxisLineWidth());
            this.mAxisLinePaint.setPathEffect(this.mXAxis.getAxisLineDashPathEffect());
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP || this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
            }

            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }

        }
    }

    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = this.mXAxis.isCenterAxisLabelsEnabled();
        float[] positions = new float[this.mXAxis.mEntryCount * 2];

        int i;
        for(i = 0; i < positions.length; i += 2) {
            if (centeringEnabled) {
                positions[i] = this.mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = this.mXAxis.mEntries[i / 2];
            }
        }

        this.mTrans.pointValuesToPixel(positions);

        for(i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (this.mViewPortHandler.isInBoundsX(x)) {
                String label = this.mXAxis.getValueFormatter().getAxisLabel(this.mXAxis.mEntries[i / 2], this.mXAxis);
                if (this.mXAxis.isAvoidFirstLastClippingEnabled()) {
                    float width;
                    if (i / 2 == this.mXAxis.mEntryCount - 1 && this.mXAxis.mEntryCount > 1) {
                        width = (float)Utils.calcTextWidth(this.mAxisLabelPaint, label);
                        if (width > this.mViewPortHandler.offsetRight() * 2.0F && x + width > this.mViewPortHandler.getChartWidth()) {
                            x -= width / 2.0F;
                        }
                    } else if (i == 0) {
                        width = (float)Utils.calcTextWidth(this.mAxisLabelPaint, label);
                        x += width / 2.0F;
                    }
                }

                this.drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }

    }

    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        Utils.drawXAxisValue(c, formattedLabel, x, y, this.mAxisLabelPaint, anchor, angleDegrees);
    }

    public void renderGridLines(Canvas c) {
        if (this.mXAxis.isDrawGridLinesEnabled() && this.mXAxis.isEnabled()) {
            int clipRestoreCount = c.save();
            c.clipRect(this.getGridClippingRect());
            if (this.mRenderGridLinesBuffer.length != this.mAxis.mEntryCount * 2) {
                this.mRenderGridLinesBuffer = new float[this.mXAxis.mEntryCount * 2];
            }

            float[] positions = this.mRenderGridLinesBuffer;

            for(int i = 0; i < positions.length; i += 2) {
                positions[i] = this.mXAxis.mEntries[i / 2];
                positions[i + 1] = this.mXAxis.mEntries[i / 2];
            }

            this.mTrans.pointValuesToPixel(positions);
            this.setupGridPaint();
            Path gridLinePath = this.mRenderGridLinesPath;
            gridLinePath.reset();

            for(int i = 0; i < positions.length; i += 2) {
                this.drawGridLine(c, positions[i], positions[i + 1], gridLinePath);
            }

            c.restoreToCount(clipRestoreCount);
        }
    }

    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(-this.mAxis.getGridLineWidth(), 0.0F);
        return this.mGridClippingRect;
    }

    protected void drawGridLine(Canvas c, float x, float y, Path gridLinePath) {
        gridLinePath.moveTo(x, this.mViewPortHandler.contentBottom());
        gridLinePath.lineTo(x, this.mViewPortHandler.contentTop());
        c.drawPath(gridLinePath, this.mGridPaint);
        gridLinePath.reset();
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mXAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            float[] position = this.mRenderLimitLinesBuffer;
            position[0] = 0.0F;
            position[1] = 0.0F;

            for(int i = 0; i < limitLines.size(); ++i) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    int clipRestoreCount = c.save();
                    this.mLimitLineClippingRect.set(this.mViewPortHandler.getContentRect());
                    this.mLimitLineClippingRect.inset(-l.getLineWidth(), 0.0F);
                    c.clipRect(this.mLimitLineClippingRect);
                    position[0] = l.getLimit();
                    position[1] = 0.0F;
                    this.mTrans.pointValuesToPixel(position);
                    this.renderLimitLineLine(c, l, position);
                    this.renderLimitLineLabel(c, l, position, 2.0F + l.getYOffset());
                    c.restoreToCount(clipRestoreCount);
                }
            }

        }
    }

    public void renderLimitLineLine(Canvas c, LimitLine limitLine, float[] position) {
        this.mLimitLineSegmentsBuffer[0] = position[0];
        this.mLimitLineSegmentsBuffer[1] = this.mViewPortHandler.contentTop();
        this.mLimitLineSegmentsBuffer[2] = position[0];
        this.mLimitLineSegmentsBuffer[3] = this.mViewPortHandler.contentBottom();
        this.mLimitLinePath.reset();
        this.mLimitLinePath.moveTo(this.mLimitLineSegmentsBuffer[0], this.mLimitLineSegmentsBuffer[1]);
        this.mLimitLinePath.lineTo(this.mLimitLineSegmentsBuffer[2], this.mLimitLineSegmentsBuffer[3]);
        this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
        this.mLimitLinePaint.setColor(limitLine.getLineColor());
        this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        this.mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());
        c.drawPath(this.mLimitLinePath, this.mLimitLinePaint);
    }

    public void renderLimitLineLabel(Canvas c, LimitLine limitLine, float[] position, float yOffset) {
        String label = limitLine.getLabel();
        if (label != null && !label.equals("")) {
            this.mLimitLinePaint.setStyle(limitLine.getTextStyle());
            this.mLimitLinePaint.setPathEffect(null);
            this.mLimitLinePaint.setColor(limitLine.getTextColor());
            this.mLimitLinePaint.setStrokeWidth(0.5F);
            this.mLimitLinePaint.setTextSize(limitLine.getTextSize());
            float xOffset = limitLine.getLineWidth() + limitLine.getXOffset();
            LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();
            float labelLineHeight;
            if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                labelLineHeight = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                c.drawText(label, position[0] + xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                c.drawText(label, position[0] + xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                labelLineHeight = (float)Utils.calcTextHeight(this.mLimitLinePaint, label);
                c.drawText(label, position[0] - xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
            } else {
                this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                c.drawText(label, position[0] - xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
            }
        }

    }
}
