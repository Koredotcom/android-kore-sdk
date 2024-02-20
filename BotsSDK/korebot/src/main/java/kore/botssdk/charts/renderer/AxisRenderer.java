package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;

import kore.botssdk.charts.components.AxisBase;
import kore.botssdk.charts.utils.MPPointD;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {
    protected final AxisBase mAxis;
    protected final Transformer mTrans;
    protected Paint mGridPaint;
    protected Paint mAxisLabelPaint;
    protected Paint mAxisLinePaint;
    protected Paint mLimitLinePaint;

    public AxisRenderer(ViewPortHandler viewPortHandler, Transformer trans, AxisBase axis) {
        super(viewPortHandler);
        this.mTrans = trans;
        this.mAxis = axis;
        if (this.mViewPortHandler != null) {
            this.mAxisLabelPaint = new Paint(1);
            this.mGridPaint = new Paint();
            this.mGridPaint.setColor(-7829368);
            this.mGridPaint.setStrokeWidth(1.0F);
            this.mGridPaint.setStyle(Paint.Style.STROKE);
            this.mGridPaint.setAlpha(90);
            this.mAxisLinePaint = new Paint();
            this.mAxisLinePaint.setColor(-16777216);
            this.mAxisLinePaint.setStrokeWidth(1.0F);
            this.mAxisLinePaint.setStyle(Paint.Style.STROKE);
            this.mLimitLinePaint = new Paint(1);
            this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
        }

    }

    public Paint getPaintAxisLabels() {
        return this.mAxisLabelPaint;
    }

    public Paint getPaintGrid() {
        return this.mGridPaint;
    }

    public Paint getPaintAxisLine() {
        return this.mAxisLinePaint;
    }

    public Transformer getTransformer() {
        return this.mTrans;
    }

    public void computeAxis(float min, float max, boolean inverted) {
        if (this.mViewPortHandler != null && this.mViewPortHandler.contentWidth() > 10.0F && !this.mViewPortHandler.isFullyZoomedOutY()) {
            MPPointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            MPPointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
            if (!inverted) {
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

    protected void computeAxisValues(float min, float max) {
        int labelCount = this.mAxis.getLabelCount();
        double range = Math.abs(max - min);
        if (labelCount != 0 && !(range <= 0.0D) && !Double.isInfinite(range)) {
            double rawInterval = range / (double)labelCount;
            double interval = Utils.roundToNextSignificant(rawInterval);
            if (this.mAxis.isGranularityEnabled()) {
                interval = interval < (double)this.mAxis.getGranularity() ? (double)this.mAxis.getGranularity() : interval;
            }

            double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10.0D, (int)Math.log10(interval)));
            int intervalSigDigit = (int)(interval / intervalMagnitude);
            if (intervalSigDigit > 5) {
                interval = Math.floor(10.0D * intervalMagnitude);
            }

            int n = this.mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
            float offset;
            int i;
            if (this.mAxis.isForceLabelsEnabled()) {
                interval = (float)range / (float)(labelCount - 1);
                this.mAxis.mEntryCount = labelCount;
                if (this.mAxis.mEntries.length < labelCount) {
                    this.mAxis.mEntries = new float[labelCount];
                }

                offset = min;

                for(i = 0; i < labelCount; ++i) {
                    this.mAxis.mEntries[i] = offset;
                    offset = (float)((double)offset + interval);
                }

                n = labelCount;
            } else {
                double first = interval == 0.0D ? 0.0D : Math.ceil((double)min / interval) * interval;
                if (this.mAxis.isCenterAxisLabelsEnabled()) {
                    first -= interval;
                }

                double last = interval == 0.0D ? 0.0D : Utils.nextUp(Math.floor((double)max / interval) * interval);
                double f;
                if (interval != 0.0D) {
                    for(f = first; f <= last; f += interval) {
                        ++n;
                    }
                }

                this.mAxis.mEntryCount = n;
                if (this.mAxis.mEntries.length < n) {
                    this.mAxis.mEntries = new float[n];
                }

                f = first;

                for(i = 0; i < n; ++i) {
                    if (f == 0.0D) {
                        f = 0.0D;
                    }

                    this.mAxis.mEntries[i] = (float)f;
                    f += interval;
                }
            }

            if (interval < 1.0D) {
                this.mAxis.mDecimals = (int)Math.ceil(-Math.log10(interval));
            } else {
                this.mAxis.mDecimals = 0;
            }

            if (this.mAxis.isCenterAxisLabelsEnabled()) {
                if (this.mAxis.mCenteredEntries.length < n) {
                    this.mAxis.mCenteredEntries = new float[n];
                }

                offset = (float)interval / 2.0F;

                for(i = 0; i < n; ++i) {
                    this.mAxis.mCenteredEntries[i] = this.mAxis.mEntries[i] + offset;
                }
            }

        } else {
            this.mAxis.mEntries = new float[0];
            this.mAxis.mCenteredEntries = new float[0];
            this.mAxis.mEntryCount = 0;
        }
    }

    public abstract void renderAxisLabels(Canvas var1);

    public abstract void renderGridLines(Canvas var1);

    public abstract void renderAxisLine(Canvas var1);

    public abstract void renderLimitLines(Canvas var1);
}
