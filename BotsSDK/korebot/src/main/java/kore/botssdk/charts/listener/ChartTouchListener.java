package kore.botssdk.charts.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import kore.botssdk.charts.charts.Chart;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.listener.OnChartGestureListener;

public abstract class ChartTouchListener<T extends Chart<?>> extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    protected kore.botssdk.charts.listener.ChartTouchListener.ChartGesture mLastGesture;
    protected static final int NONE = 0;
    protected static final int DRAG = 1;
    protected static final int X_ZOOM = 2;
    protected static final int Y_ZOOM = 3;
    protected static final int PINCH_ZOOM = 4;
    protected static final int POST_ZOOM = 5;
    protected static final int ROTATE = 6;
    protected int mTouchMode;
    protected Highlight mLastHighlighted;
    protected GestureDetector mGestureDetector;
    protected T mChart;

    public ChartTouchListener(T chart) {
        this.mLastGesture = kore.botssdk.charts.listener.ChartTouchListener.ChartGesture.NONE;
        this.mTouchMode = 0;
        this.mChart = chart;
        this.mGestureDetector = new GestureDetector(chart.getContext(), this);
    }

    public void startAction(MotionEvent me) {
        OnChartGestureListener l = this.mChart.getOnChartGestureListener();
        if (l != null) {
            l.onChartGestureStart(me, this.mLastGesture);
        }

    }

    public void endAction(MotionEvent me) {
        OnChartGestureListener l = this.mChart.getOnChartGestureListener();
        if (l != null) {
            l.onChartGestureEnd(me, this.mLastGesture);
        }

    }

    public void setLastHighlighted(Highlight high) {
        this.mLastHighlighted = high;
    }

    public int getTouchMode() {
        return this.mTouchMode;
    }

    public kore.botssdk.charts.listener.ChartTouchListener.ChartGesture getLastGesture() {
        return this.mLastGesture;
    }

    protected void performHighlight(Highlight h, MotionEvent e) {
        if (h != null && !h.equalTo(this.mLastHighlighted)) {
            this.mChart.highlightValue(h, true);
            this.mLastHighlighted = h;
        } else {
            this.mChart.highlightValue((Highlight)null, true);
            this.mLastHighlighted = null;
        }

    }

    protected static float distance(float eventX, float startX, float eventY, float startY) {
        float dx = eventX - startX;
        float dy = eventY - startY;
        return (float)Math.sqrt((double)(dx * dx + dy * dy));
    }

    public static enum ChartGesture {
        NONE,
        DRAG,
        X_ZOOM,
        Y_ZOOM,
        PINCH_ZOOM,
        ROTATE,
        SINGLE_TAP,
        DOUBLE_TAP,
        LONG_PRESS,
        FLING;

        private ChartGesture() {
        }
    }
}
