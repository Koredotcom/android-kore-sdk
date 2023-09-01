package com.kore.ai.widgetsdk.charts.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.kore.ai.widgetsdk.charts.data.LineData;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.LineDataProvider;
import com.kore.ai.widgetsdk.charts.renderer.LineChartRenderer;

public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {
    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mRenderer = new LineChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }

    public LineData getLineData() {
        return this.mData;
    }

    protected void onDetachedFromWindow() {
        if (this.mRenderer != null && this.mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer)this.mRenderer).releaseBitmap();
        }

        super.onDetachedFromWindow();
    }
}