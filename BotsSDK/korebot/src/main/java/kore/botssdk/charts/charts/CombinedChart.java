package kore.botssdk.charts.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.BubbleData;
import kore.botssdk.charts.data.CandleData;
import kore.botssdk.charts.data.CombinedData;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.LineData;
import kore.botssdk.charts.data.ScatterData;
import kore.botssdk.charts.highlight.CombinedHighlighter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.dataprovider.CombinedDataProvider;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.renderer.CombinedChartRenderer;
import kore.botssdk.utils.LogUtils;

public class CombinedChart extends BarLineChartBase<CombinedData> implements CombinedDataProvider {
    private boolean mDrawValueAboveBar = true;
    protected boolean mHighlightFullBarEnabled = false;
    private boolean mDrawBarShadow = false;
    protected kore.botssdk.charts.charts.CombinedChart.DrawOrder[] mDrawOrder;

    public CombinedChart(Context context) {
        super(context);
    }

    public CombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mDrawOrder = new kore.botssdk.charts.charts.CombinedChart.DrawOrder[]{kore.botssdk.charts.charts.CombinedChart.DrawOrder.BAR, kore.botssdk.charts.charts.CombinedChart.DrawOrder.BUBBLE, kore.botssdk.charts.charts.CombinedChart.DrawOrder.LINE, kore.botssdk.charts.charts.CombinedChart.DrawOrder.CANDLE, kore.botssdk.charts.charts.CombinedChart.DrawOrder.SCATTER};
        this.setHighlighter(new CombinedHighlighter(this, this));
        this.setHighlightFullBarEnabled(true);
        this.mRenderer = new CombinedChartRenderer(this, this.mAnimator, this.mViewPortHandler);
    }

    public CombinedData getCombinedData() {
        return this.mData;
    }

    public void setData(CombinedData data) {
        super.setData(data);
        this.setHighlighter(new CombinedHighlighter(this, this));
        ((CombinedChartRenderer)this.mRenderer).createRenderers();
        this.mRenderer.initBuffers();
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData == null) {
            LogUtils.e("MPAndroidChart", "Can't select by touch. No data set.");
            return null;
        } else {
            Highlight h = this.getHighlighter().getHighlight(x, y);
            return h != null && this.isHighlightFullBarEnabled() ? new Highlight(h.getX(), h.getY(), h.getXPx(), h.getYPx(), h.getDataSetIndex(), -1, h.getAxis()) : h;
        }
    }

    public LineData getLineData() {
        return this.mData == null ? null : this.mData.getLineData();
    }

    public BarData getBarData() {
        return this.mData == null ? null : this.mData.getBarData();
    }

    public ScatterData getScatterData() {
        return this.mData == null ? null : this.mData.getScatterData();
    }

    public CandleData getCandleData() {
        return this.mData == null ? null : this.mData.getCandleData();
    }

    public BubbleData getBubbleData() {
        return this.mData == null ? null : this.mData.getBubbleData();
    }

    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }

    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }

    public void setDrawValueAboveBar(boolean enabled) {
        this.mDrawValueAboveBar = enabled;
    }

    public void setDrawBarShadow(boolean enabled) {
        this.mDrawBarShadow = enabled;
    }

    public void setHighlightFullBarEnabled(boolean enabled) {
        this.mHighlightFullBarEnabled = enabled;
    }

    public boolean isHighlightFullBarEnabled() {
        return this.mHighlightFullBarEnabled;
    }

    public kore.botssdk.charts.charts.CombinedChart.DrawOrder[] getDrawOrder() {
        return this.mDrawOrder;
    }

    public void setDrawOrder(kore.botssdk.charts.charts.CombinedChart.DrawOrder[] order) {
        if (order != null && order.length > 0) {
            this.mDrawOrder = order;
        }
    }

    protected void drawMarkers(Canvas canvas) {
        if (this.mMarker != null && this.isDrawMarkersEnabled() && this.valuesToHighlight()) {
            for(int i = 0; i < this.mIndicesToHighlight.length; ++i) {
                Highlight highlight = this.mIndicesToHighlight[i];
                IDataSet set = this.mData.getDataSetByHighlight(highlight);
                Entry e = this.mData.getEntryForHighlight(highlight);
                if (e != null) {
                    int entryIndex = set.getEntryIndex(e);
                    if (!((float)entryIndex > (float)set.getEntryCount() * this.mAnimator.getPhaseX())) {
                        float[] pos = this.getMarkerPosition(highlight);
                        if (this.mViewPortHandler.isInBounds(pos[0], pos[1])) {
                            this.mMarker.refreshContent(e, highlight);
                            this.mMarker.draw(canvas, pos[0], pos[1]);
                        }
                    }
                }
            }

        }
    }

    public enum DrawOrder {
        BAR,
        BUBBLE,
        LINE,
        CANDLE,
        SCATTER;

        DrawOrder() {
        }
    }
}
