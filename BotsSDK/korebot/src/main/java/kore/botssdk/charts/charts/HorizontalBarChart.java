package kore.botssdk.charts.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.highlight.HorizontalBarHighlighter;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.renderer.HorizontalBarChartRenderer;
import kore.botssdk.charts.renderer.XAxisRendererHorizontalBarChart;
import kore.botssdk.charts.renderer.YAxisRendererHorizontalBarChart;
import kore.botssdk.charts.utils.HorizontalViewPortHandler;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.TransformerHorizontalBarChart;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.utils.LogUtils;

public class HorizontalBarChart extends BarChart {
    private final RectF mOffsetsBuffer = new RectF();
    protected final float[] mGetPositionBuffer = new float[2];

    public HorizontalBarChart(Context context) {
        super(context);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        this.mViewPortHandler = new HorizontalViewPortHandler();
        super.init();
        this.mLeftAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRightAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRenderer = new HorizontalBarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.setHighlighter(new HorizontalBarHighlighter(this));
        this.mAxisRendererLeft = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
    }

    public void calculateOffsets() {
        float offsetLeft = 0.0F;
        float offsetRight = 0.0F;
        float offsetTop = 0.0F;
        float offsetBottom = 0.0F;
        this.calculateLegendOffsets(this.mOffsetsBuffer);
        offsetLeft += this.mOffsetsBuffer.left;
        offsetTop += this.mOffsetsBuffer.top;
        offsetRight += this.mOffsetsBuffer.right;
        offsetBottom += this.mOffsetsBuffer.bottom;
        if (this.mAxisLeft.needsOffset()) {
            offsetTop += this.mAxisLeft.getRequiredHeightSpace(this.mAxisRendererLeft.getPaintAxisLabels());
        }

        if (this.mAxisRight.needsOffset()) {
            offsetBottom += this.mAxisRight.getRequiredHeightSpace(this.mAxisRendererRight.getPaintAxisLabels());
        }

        float xlabelwidth = (float)this.mXAxis.mLabelRotatedWidth;
        if (this.mXAxis.isEnabled()) {
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                offsetLeft += xlabelwidth;
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                offsetRight += xlabelwidth;
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                offsetLeft += xlabelwidth;
                offsetRight += xlabelwidth;
            }
        }

        offsetTop += this.getExtraTopOffset();
        offsetRight += this.getExtraRightOffset();
        offsetBottom += this.getExtraBottomOffset();
        offsetLeft += this.getExtraLeftOffset();
        float minOffset = Utils.convertDpToPixel(this.mMinOffset);
        this.mViewPortHandler.restrainViewPort(Math.max(minOffset, offsetLeft), Math.max(minOffset, offsetTop), Math.max(minOffset, offsetRight), Math.max(minOffset, offsetBottom));
        if (this.mLogEnabled) {
            LogUtils.i("MPAndroidChart", "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
            LogUtils.i("MPAndroidChart", "Content: " + this.mViewPortHandler.getContentRect().toString());
        }

        this.prepareOffsetMatrix();
        this.prepareValuePxMatrix();
    }

    protected void prepareValuePxMatrix() {
        this.mRightAxisTransformer.prepareMatrixValuePx(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
    }

    protected float[] getMarkerPosition(Highlight high) {
        return new float[]{high.getDrawY(), high.getDrawX()};
    }

    public void getBarBounds(BarEntry e, RectF outputRect) {
        IBarDataSet set = this.mData.getDataSetForEntry(e);
        if (set == null) {
            outputRect.set(1.4E-45F, 1.4E-45F, 1.4E-45F, 1.4E-45F);
        } else {
            float y = e.getY();
            float x = e.getX();
            float barWidth = this.mData.getBarWidth();
            float top = x - barWidth / 2.0F;
            float bottom = x + barWidth / 2.0F;
            float left = y >= 0.0F ? y : 0.0F;
            float right = y <= 0.0F ? y : 0.0F;
            outputRect.set(left, top, right, bottom);
            this.getTransformer(set.getAxisDependency()).rectValueToPixel(outputRect);
        }
    }

    public MPPointF getPosition(Entry e, YAxis.AxisDependency axis) {
        if (e == null) {
            return null;
        } else {
            float[] vals = this.mGetPositionBuffer;
            vals[0] = e.getY();
            vals[1] = e.getX();
            this.getTransformer(axis).pointValuesToPixel(vals);
            return MPPointF.getInstance(vals[0], vals[1]);
        }
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData == null) {
            if (this.mLogEnabled) {
                LogUtils.e("MPAndroidChart", "Can't select by touch. No data set.");
            }

            return null;
        } else {
            return this.getHighlighter().getHighlight(y, x);
        }
    }

    public float getLowestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.posForGetLowestVisibleX);
        float result = (float)Math.max(this.mXAxis.mAxisMinimum, this.posForGetLowestVisibleX.y);
        return result;
    }

    public float getHighestVisibleX() {
        this.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.posForGetHighestVisibleX);
        float result = (float)Math.min(this.mXAxis.mAxisMaximum, this.posForGetHighestVisibleX.y);
        return result;
    }

    public void setVisibleXRangeMaximum(float maxXRange) {
        float xScale = this.mXAxis.mAxisRange / maxXRange;
        this.mViewPortHandler.setMinimumScaleY(xScale);
    }

    public void setVisibleXRangeMinimum(float minXRange) {
        float xScale = this.mXAxis.mAxisRange / minXRange;
        this.mViewPortHandler.setMaximumScaleY(xScale);
    }

    public void setVisibleXRange(float minXRange, float maxXRange) {
        float minScale = this.mXAxis.mAxisRange / minXRange;
        float maxScale = this.mXAxis.mAxisRange / maxXRange;
        this.mViewPortHandler.setMinMaxScaleY(minScale, maxScale);
    }

    public void setVisibleYRangeMaximum(float maxYRange, YAxis.AxisDependency axis) {
        float yScale = this.getAxisRange(axis) / maxYRange;
        this.mViewPortHandler.setMinimumScaleX(yScale);
    }

    public void setVisibleYRangeMinimum(float minYRange, YAxis.AxisDependency axis) {
        float yScale = this.getAxisRange(axis) / minYRange;
        this.mViewPortHandler.setMaximumScaleX(yScale);
    }

    public void setVisibleYRange(float minYRange, float maxYRange, YAxis.AxisDependency axis) {
        float minScale = this.getAxisRange(axis) / minYRange;
        float maxScale = this.getAxisRange(axis) / maxYRange;
        this.mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }
}

