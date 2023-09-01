package com.kore.ai.widgetsdk.charts.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.kore.ai.widgetsdk.charts.components.YAxis;
import com.kore.ai.widgetsdk.charts.data.BarData;
import com.kore.ai.widgetsdk.charts.data.BarEntry;
import com.kore.ai.widgetsdk.charts.highlight.BarHighlighter;
import com.kore.ai.widgetsdk.charts.highlight.Highlight;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.BarDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IBarDataSet;
import com.kore.ai.widgetsdk.charts.renderer.BarChartRenderer;

public class BarChart extends BarLineChartBase<BarData> implements BarDataProvider {
    protected boolean mHighlightFullBarEnabled = false;
    private boolean mDrawValueAboveBar = true;
    private boolean mDrawBarShadow = false;
    private boolean mFitBars = false;

    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mRenderer = new BarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.setHighlighter(new BarHighlighter(this));
        this.getXAxis().setSpaceMin(0.5F);
        this.getXAxis().setSpaceMax(0.5F);
    }

    protected void calcMinMax() {
        if (this.mFitBars) {
            this.mXAxis.calculate(this.mData.getXMin() - this.mData.getBarWidth() / 2.0F, this.mData.getXMax() + this.mData.getBarWidth() / 2.0F);
        } else {
            this.mXAxis.calculate(this.mData.getXMin(), this.mData.getXMax());
        }

        this.mAxisLeft.calculate(this.mData.getYMin(YAxis.AxisDependency.LEFT), this.mData.getYMax(YAxis.AxisDependency.LEFT));
        this.mAxisRight.calculate(this.mData.getYMin(YAxis.AxisDependency.RIGHT), this.mData.getYMax(YAxis.AxisDependency.RIGHT));
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData == null) {
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        } else {
            Highlight h = this.getHighlighter().getHighlight(x, y);
            return h != null && this.isHighlightFullBarEnabled() ? new Highlight(h.getX(), h.getY(), h.getXPx(), h.getYPx(), h.getDataSetIndex(), -1, h.getAxis()) : h;
        }
    }

    public RectF getBarBounds(BarEntry e) {
        RectF bounds = new RectF();
        this.getBarBounds(e, bounds);
        return bounds;
    }

    public void getBarBounds(BarEntry e, RectF outputRect) {
        IBarDataSet set = this.mData.getDataSetForEntry(e);
        if (set == null) {
            outputRect.set(1.4E-45F, 1.4E-45F, 1.4E-45F, 1.4E-45F);
        } else {
            float y = e.getY();
            float x = e.getX();
            float barWidth = this.mData.getBarWidth();
            float left = x - barWidth / 2.0F;
            float right = x + barWidth / 2.0F;
            float top = y >= 0.0F ? y : 0.0F;
            float bottom = y <= 0.0F ? y : 0.0F;
            outputRect.set(left, top, right, bottom);
            this.getTransformer(set.getAxisDependency()).rectValueToPixel(outputRect);
        }
    }

    public void setDrawValueAboveBar(boolean enabled) {
        this.mDrawValueAboveBar = enabled;
    }

    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }

    public void setDrawBarShadow(boolean enabled) {
        this.mDrawBarShadow = enabled;
    }

    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }

    public void setHighlightFullBarEnabled(boolean enabled) {
        this.mHighlightFullBarEnabled = enabled;
    }

    public boolean isHighlightFullBarEnabled() {
        return this.mHighlightFullBarEnabled;
    }

    public void highlightValue(float x, int dataSetIndex, int stackIndex) {
        this.highlightValue(new Highlight(x, dataSetIndex, stackIndex), false);
    }

    public BarData getBarData() {
        return this.mData;
    }

    public void setFitBars(boolean enabled) {
        this.mFitBars = enabled;
    }

    public void groupBars(float fromX, float groupSpace, float barSpace) {
        if (this.getBarData() == null) {
            throw new RuntimeException("You need to set data for the chart before grouping bars.");
        } else {
            this.getBarData().groupBars(fromX, groupSpace, barSpace);
            this.notifyDataSetChanged();
        }
    }
}
