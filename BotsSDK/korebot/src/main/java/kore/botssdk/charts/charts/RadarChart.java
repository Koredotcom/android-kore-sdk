package kore.botssdk.charts.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.RadarData;
import kore.botssdk.charts.highlight.RadarHighlighter;
import kore.botssdk.charts.interfaces.datasets.IRadarDataSet;
import kore.botssdk.charts.renderer.RadarChartRenderer;
import kore.botssdk.charts.renderer.XAxisRendererRadarChart;
import kore.botssdk.charts.renderer.YAxisRendererRadarChart;
import kore.botssdk.charts.utils.Utils;

public class RadarChart extends PieRadarChartBase<RadarData> {
    private float mWebLineWidth = 2.5F;
    private float mInnerWebLineWidth = 1.5F;
    private int mWebColor = Color.rgb(122, 122, 122);
    private int mWebColorInner = Color.rgb(122, 122, 122);
    private int mWebAlpha = 150;
    private boolean mDrawWeb = true;
    private int mSkipWebLineCount = 0;
    private YAxis mYAxis;
    protected YAxisRendererRadarChart mYAxisRenderer;
    protected XAxisRendererRadarChart mXAxisRenderer;

    public RadarChart(Context context) {
        super(context);
    }

    public RadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mYAxis = new YAxis(YAxis.AxisDependency.LEFT);
        this.mWebLineWidth = Utils.convertDpToPixel(1.5F);
        this.mInnerWebLineWidth = Utils.convertDpToPixel(0.75F);
        this.mRenderer = new RadarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mYAxisRenderer = new YAxisRendererRadarChart(this.mViewPortHandler, this.mYAxis, this);
        this.mXAxisRenderer = new XAxisRendererRadarChart(this.mViewPortHandler, this.mXAxis, this);
        this.mHighlighter = new RadarHighlighter(this);
    }

    protected void calcMinMax() {
        super.calcMinMax();
        this.mYAxis.calculate(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        this.mXAxis.calculate(0.0F, (float) this.mData.getMaxEntryCountSet().getEntryCount());
    }

    public void notifyDataSetChanged() {
        if (this.mData != null) {
            this.calcMinMax();
            this.mYAxisRenderer.computeAxis(this.mYAxis.mAxisMinimum, this.mYAxis.mAxisMaximum, this.mYAxis.isInverted());
            this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
            if (this.mLegend != null && !this.mLegend.isLegendCustom()) {
                this.mLegendRenderer.computeLegend(this.mData);
            }

            this.calculateOffsets();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData != null) {
            if (this.mXAxis.isEnabled()) {
                this.mXAxisRenderer.computeAxis(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisMaximum, false);
            }

            this.mXAxisRenderer.renderAxisLabels(canvas);
            if (this.mDrawWeb) {
                this.mRenderer.drawExtras(canvas);
            }

            if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLimitLinesBehindDataEnabled()) {
                this.mYAxisRenderer.renderLimitLines(canvas);
            }

            this.mRenderer.drawData(canvas);
            if (this.valuesToHighlight()) {
                this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
            }

            if (this.mYAxis.isEnabled() && !this.mYAxis.isDrawLimitLinesBehindDataEnabled()) {
                this.mYAxisRenderer.renderLimitLines(canvas);
            }

            this.mYAxisRenderer.renderAxisLabels(canvas);
            this.mRenderer.drawValues(canvas);
            this.mLegendRenderer.renderLegend(canvas);
            this.drawDescription(canvas);
            this.drawMarkers(canvas);
        }
    }

    public float getFactor() {
        RectF content = this.mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2.0F, content.height() / 2.0F) / this.mYAxis.mAxisRange;
    }

    public float getSliceAngle() {
        return 360.0F / (float) this.mData.getMaxEntryCountSet().getEntryCount();
    }

    public int getIndexForAngle(float angle) {
        float a = Utils.getNormalizedAngle(angle - this.getRotationAngle());
        float sliceangle = this.getSliceAngle();
        int max = this.mData.getMaxEntryCountSet().getEntryCount();
        int index = 0;

        for(int i = 0; i < max; ++i) {
            float referenceAngle = sliceangle * (float)(i + 1) - sliceangle / 2.0F;
            if (referenceAngle > a) {
                index = i;
                break;
            }
        }

        return index;
    }

    public YAxis getYAxis() {
        return this.mYAxis;
    }

    public void setWebLineWidth(float width) {
        this.mWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidth() {
        return this.mWebLineWidth;
    }

    public void setWebLineWidthInner(float width) {
        this.mInnerWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidthInner() {
        return this.mInnerWebLineWidth;
    }

    public void setWebAlpha(int alpha) {
        this.mWebAlpha = alpha;
    }

    public int getWebAlpha() {
        return this.mWebAlpha;
    }

    public void setWebColor(int color) {
        this.mWebColor = color;
    }

    public int getWebColor() {
        return this.mWebColor;
    }

    public void setWebColorInner(int color) {
        this.mWebColorInner = color;
    }

    public int getWebColorInner() {
        return this.mWebColorInner;
    }

    public void setDrawWeb(boolean enabled) {
        this.mDrawWeb = enabled;
    }

    public void setSkipWebLineCount(int count) {
        this.mSkipWebLineCount = Math.max(0, count);
    }

    public int getSkipWebLineCount() {
        return this.mSkipWebLineCount;
    }

    protected float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 4.0F;
    }

    protected float getRequiredBaseOffset() {
        return this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled() ? (float)this.mXAxis.mLabelRotatedWidth : Utils.convertDpToPixel(10.0F);
    }

    public float getRadius() {
        RectF content = this.mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2.0F, content.height() / 2.0F);
    }

    public float getYChartMax() {
        return this.mYAxis.mAxisMaximum;
    }

    public float getYChartMin() {
        return this.mYAxis.mAxisMinimum;
    }

    public float getYRange() {
        return this.mYAxis.mAxisRange;
    }
}
