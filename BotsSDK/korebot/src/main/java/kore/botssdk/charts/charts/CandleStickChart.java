package kore.botssdk.charts.charts;

import android.content.Context;
import android.util.AttributeSet;

import kore.botssdk.charts.data.CandleData;
import kore.botssdk.charts.interfaces.dataprovider.CandleDataProvider;
import kore.botssdk.charts.renderer.CandleStickChartRenderer;

public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider {
    public CandleStickChart(Context context) {
        super(context);
    }

    public CandleStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mRenderer = new CandleStickChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.getXAxis().setSpaceMin(0.5F);
        this.getXAxis().setSpaceMax(0.5F);
    }

    public CandleData getCandleData() {
        return this.mData;
    }
}
