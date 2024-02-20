package kore.botssdk.charts.charts;

import android.content.Context;
import android.util.AttributeSet;

import kore.botssdk.charts.data.BubbleData;
import kore.botssdk.charts.interfaces.dataprovider.BubbleDataProvider;
import kore.botssdk.charts.renderer.BubbleChartRenderer;

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
