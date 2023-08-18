package kore.botssdk.charts.listener;

import android.view.MotionEvent;

public interface OnChartGestureListener {
    void onChartGestureStart(MotionEvent var1, ChartTouchListener.ChartGesture var2);

    void onChartGestureEnd(MotionEvent var1, ChartTouchListener.ChartGesture var2);

    void onChartLongPressed(MotionEvent var1);

    void onChartDoubleTapped(MotionEvent var1);

    void onChartSingleTapped(MotionEvent var1);

    void onChartFling(MotionEvent var1, MotionEvent var2, float var3, float var4);

    void onChartScale(MotionEvent var1, float var2, float var3);

    void onChartTranslate(MotionEvent var1, float var2, float var3);
}
