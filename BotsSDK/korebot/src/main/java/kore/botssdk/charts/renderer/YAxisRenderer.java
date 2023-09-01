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

public class YAxisRenderer extends AxisRenderer {
    protected final YAxis mYAxis;
    protected Paint mZeroLinePaint;
    protected final Path mRenderGridLinesPath = new Path();
    protected final RectF mGridClippingRect = new RectF();
    protected float[] mGetTransformedPositionsBuffer = new float[2];
    protected final Path mDrawZeroLinePath = new Path();
    protected final RectF mZeroLineClippingRect = new RectF();
    protected final Path mRenderLimitLines = new Path();
    protected final float[] mRenderLimitLinesBuffer = new float[2];
    protected final RectF mLimitLineClippingRect = new RectF();

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans, yAxis);
        this.mYAxis = yAxis;
        if (this.mViewPortHandler != null) {
            this.mAxisLabelPaint.setColor(-16777216);
            this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0F));
            this.mZeroLinePaint = new Paint(1);
            this.mZeroLinePaint.setColor(-7829368);
            this.mZeroLinePaint.setStrokeWidth(1.0F);
            this.mZeroLinePaint.setStyle(Paint.Style.STROKE);
        }

    }

    public void renderAxisLabels(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            float[] positions = this.getTransformedPositions();
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            float xoffset = this.mYAxis.getXOffset();
            float yoffset = (float)Utils.calcTextHeight(this.mAxisLabelPaint, "A") / 2.5F + this.mYAxis.getYOffset();
            YAxis.AxisDependency dependency = this.mYAxis.getAxisDependency();
            YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
            float xPos = 0.0F;
            if (dependency == YAxis.AxisDependency.LEFT) {
                if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                    xPos = this.mViewPortHandler.offsetLeft() - xoffset;
                } else {
                    this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                    xPos = this.mViewPortHandler.offsetLeft() + xoffset;
                }
            } else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                xPos = this.mViewPortHandler.contentRight() + xoffset;
            } else {
                this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                xPos = this.mViewPortHandler.contentRight() - xoffset;
            }

            this.drawYLabels(c, xPos, positions, yoffset);
        }
    }

    public void renderAxisLine(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
            this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
            if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            } else {
                c.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }

        }
    }

    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        int from = this.mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        int to = this.mYAxis.isDrawTopYLabelEntryEnabled() ? this.mYAxis.mEntryCount : this.mYAxis.mEntryCount - 1;

        for(int i = from; i < to; ++i) {
            String text = this.mYAxis.getFormattedLabel(i);
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, this.mAxisLabelPaint);
        }

    }

    public void renderGridLines(Canvas c) {
        if (this.mYAxis.isEnabled()) {
            if (this.mYAxis.isDrawGridLinesEnabled()) {
                int clipRestoreCount = c.save();
                c.clipRect(this.getGridClippingRect());
                float[] positions = this.getTransformedPositions();
                this.mGridPaint.setColor(this.mYAxis.getGridColor());
                this.mGridPaint.setStrokeWidth(this.mYAxis.getGridLineWidth());
                this.mGridPaint.setPathEffect(this.mYAxis.getGridDashPathEffect());
                Path gridLinePath = this.mRenderGridLinesPath;
                gridLinePath.reset();

                for(int i = 0; i < positions.length; i += 2) {
                    c.drawPath(this.linePath(gridLinePath, i, positions), this.mGridPaint);
                    gridLinePath.reset();
                }

                c.restoreToCount(clipRestoreCount);
            }

            if (this.mYAxis.isDrawZeroLineEnabled()) {
                this.drawZeroLine(c);
            }

        }
    }

    public RectF getGridClippingRect() {
        this.mGridClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mGridClippingRect.inset(0.0F, -this.mAxis.getGridLineWidth());
        return this.mGridClippingRect;
    }

    protected Path linePath(Path p, int i, float[] positions) {
        p.moveTo(this.mViewPortHandler.offsetLeft(), positions[i + 1]);
        p.lineTo(this.mViewPortHandler.contentRight(), positions[i + 1]);
        return p;
    }

    protected float[] getTransformedPositions() {
        if (this.mGetTransformedPositionsBuffer.length != this.mYAxis.mEntryCount * 2) {
            this.mGetTransformedPositionsBuffer = new float[this.mYAxis.mEntryCount * 2];
        }

        float[] positions = this.mGetTransformedPositionsBuffer;

        for(int i = 0; i < positions.length; i += 2) {
            positions[i + 1] = this.mYAxis.mEntries[i / 2];
        }

        this.mTrans.pointValuesToPixel(positions);
        return positions;
    }

    protected void drawZeroLine(Canvas c) {
        int clipRestoreCount = c.save();
        this.mZeroLineClippingRect.set(this.mViewPortHandler.getContentRect());
        this.mZeroLineClippingRect.inset(0.0F, -this.mYAxis.getZeroLineWidth());
        c.clipRect(this.mZeroLineClippingRect);
        MPPointD pos = this.mTrans.getPixelForValues(0.0F, 0.0F);
        this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
        this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
        Path zeroLinePath = this.mDrawZeroLinePath;
        zeroLinePath.reset();
        zeroLinePath.moveTo(this.mViewPortHandler.contentLeft(), (float)pos.y);
        zeroLinePath.lineTo(this.mViewPortHandler.contentRight(), (float)pos.y);
        c.drawPath(zeroLinePath, this.mZeroLinePaint);
        c.restoreToCount(clipRestoreCount);
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            float[] pts = this.mRenderLimitLinesBuffer;
            pts[0] = 0.0F;
            pts[1] = 0.0F;
            Path limitLinePath = this.mRenderLimitLines;
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
                        this.mLimitLinePaint.setTypeface(l.getTypeface());
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
