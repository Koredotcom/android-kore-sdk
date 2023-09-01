package kore.botssdk.charts.jobs;

import android.view.View;

import kore.botssdk.charts.utils.ObjectPool;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class ViewPortJob extends ObjectPool.Poolable implements Runnable {
    protected final float[] pts = new float[2];
    protected ViewPortHandler mViewPortHandler;
    protected float xValue = 0.0F;
    protected float yValue = 0.0F;
    protected Transformer mTrans;
    protected View view;

    public ViewPortJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v) {
        this.mViewPortHandler = viewPortHandler;
        this.xValue = xValue;
        this.yValue = yValue;
        this.mTrans = trans;
        this.view = v;
    }

    public float getXValue() {
        return this.xValue;
    }

    public float getYValue() {
        return this.yValue;
    }
}
