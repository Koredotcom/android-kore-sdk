package kore.botssdk.charts.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import java.util.List;

import kore.botssdk.charts.charts.RadarChart;
import kore.botssdk.charts.components.LimitLine;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.RadarData;
import kore.botssdk.charts.interfaces.datasets.IRadarDataSet;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.charts.utils.ViewPortHandler;

public class YAxisRendererRadarChart extends YAxisRenderer {
    private final RadarChart mChart;
    private final Path mRenderLimitLinesPathBuffer = new Path();

    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart chart) {
        super(viewPortHandler, yAxis, null);
        this.mChart = chart;
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

            boolean centeringEnabled = this.mAxis.isCenterAxisLabelsEnabled();
            int n = centeringEnabled ? 1 : 0;
            float offset;
            if (this.mAxis.isForceLabelsEnabled()) {
                offset = (float)range / (float)(labelCount - 1);
                this.mAxis.mEntryCount = labelCount;
                if (this.mAxis.mEntries.length < labelCount) {
                    this.mAxis.mEntries = new float[labelCount];
                }

                float v = min;

                for(int i = 0; i < labelCount; ++i) {
                    this.mAxis.mEntries[i] = v;
                    v += offset;
                }

                n = labelCount;
            } else {
                double first = interval == 0.0D ? 0.0D : Math.ceil((double)min / interval) * interval;
                if (centeringEnabled) {
                    first -= interval;
                }

                double last = interval == 0.0D ? 0.0D : Utils.nextUp(Math.floor((double)max / interval) * interval);
                double f;
                if (interval != 0.0D) {
                    for(f = first; f <= last; f += interval) {
                        ++n;
                    }
                }

                ++n;
                this.mAxis.mEntryCount = n;
                if (this.mAxis.mEntries.length < n) {
                    this.mAxis.mEntries = new float[n];
                }

                f = first;

                for(int i = 0; i < n; ++i) {
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

            if (centeringEnabled) {
                if (this.mAxis.mCenteredEntries.length < n) {
                    this.mAxis.mCenteredEntries = new float[n];
                }

                offset = (this.mAxis.mEntries[1] - this.mAxis.mEntries[0]) / 2.0F;

                for(int i = 0; i < n; ++i) {
                    this.mAxis.mCenteredEntries[i] = this.mAxis.mEntries[i] + offset;
                }
            }

            this.mAxis.mAxisMinimum = this.mAxis.mEntries[0];
            this.mAxis.mAxisMaximum = this.mAxis.mEntries[n - 1];
            this.mAxis.mAxisRange = Math.abs(this.mAxis.mAxisMaximum - this.mAxis.mAxisMinimum);
        } else {
            this.mAxis.mEntries = new float[0];
            this.mAxis.mCenteredEntries = new float[0];
            this.mAxis.mEntryCount = 0;
        }
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            MPPointF center = this.mChart.getCenterOffsets();
            MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);
            float factor = this.mChart.getFactor();
            int from = this.mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
            int to = this.mYAxis.isDrawTopYLabelEntryEnabled() ? this.mYAxis.mEntryCount : this.mYAxis.mEntryCount - 1;

            for(int j = from; j < to; ++j) {
                float r = (this.mYAxis.mEntries[j] - this.mYAxis.mAxisMinimum) * factor;
                Utils.getPosition(center, r, this.mChart.getRotationAngle(), pOut);
                String label = this.mYAxis.getFormattedLabel(j);
                c.drawText(label, pOut.x + 10.0F, pOut.y, this.mAxisLabelPaint);
            }

            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(pOut);
        }
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null) {
            float sliceangle = this.mChart.getSliceAngle();
            float factor = this.mChart.getFactor();
            MPPointF center = this.mChart.getCenterOffsets();
            MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);

            for(int i = 0; i < limitLines.size(); ++i) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    this.mLimitLinePaint.setColor(l.getLineColor());
                    this.mLimitLinePaint.setPathEffect(l.getDashPathEffect());
                    this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                    float r = (l.getLimit() - this.mChart.getYChartMin()) * factor;
                    Path limitPath = this.mRenderLimitLinesPathBuffer;
                    limitPath.reset();

                    for(int j = 0; j < this.mChart.getData().getMaxEntryCountSet().getEntryCount(); ++j) {
                        Utils.getPosition(center, r, sliceangle * (float)j + this.mChart.getRotationAngle(), pOut);
                        if (j == 0) {
                            limitPath.moveTo(pOut.x, pOut.y);
                        } else {
                            limitPath.lineTo(pOut.x, pOut.y);
                        }
                    }

                    limitPath.close();
                    c.drawPath(limitPath, this.mLimitLinePaint);
                }
            }

            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(pOut);
        }
    }
}
