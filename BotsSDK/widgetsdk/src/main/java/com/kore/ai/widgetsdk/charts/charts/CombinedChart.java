package com.kore.ai.widgetsdk.charts.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import com.kore.ai.widgetsdk.charts.data.BarData;
import com.kore.ai.widgetsdk.charts.data.BubbleData;
import com.kore.ai.widgetsdk.charts.data.CandleData;
import com.kore.ai.widgetsdk.charts.data.CombinedData;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.data.LineData;
import com.kore.ai.widgetsdk.charts.data.ScatterData;
import com.kore.ai.widgetsdk.charts.highlight.CombinedHighlighter;
import com.kore.ai.widgetsdk.charts.highlight.Highlight;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.CombinedDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;
import com.kore.ai.widgetsdk.charts.renderer.CombinedChartRenderer;

public class CombinedChart extends BarLineChartBase<CombinedData> implements CombinedDataProvider {
    private boolean mDrawValueAboveBar = true;
    protected boolean mHighlightFullBarEnabled = false;
    private boolean mDrawBarShadow = false;
    protected com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder[] mDrawOrder;

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
        this.mDrawOrder = new com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder[]{com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder.BAR, com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder.BUBBLE, com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder.LINE, com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder.CANDLE, com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder.SCATTER};
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
            Log.e("MPAndroidChart", "Can't select by touch. No data set.");
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

    public com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder[] getDrawOrder() {
        return this.mDrawOrder;
    }

    public void setDrawOrder(com.kore.ai.widgetsdk.charts.charts.CombinedChart.DrawOrder[] order) {
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
