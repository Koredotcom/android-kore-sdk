package com.kore.ai.widgetsdk.charts.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.kore.ai.widgetsdk.charts.data.BubbleData;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.BubbleDataProvider;
import com.kore.ai.widgetsdk.charts.renderer.BubbleChartRenderer;

public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider {
    public BubbleChart(Context context) {
        super(context);
    }

    public BubbleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mRenderer = new BubbleChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }

    public BubbleData getBubbleData() {
        return this.mData;
    }
}
