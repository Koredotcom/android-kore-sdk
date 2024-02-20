package kore.botssdk.charts.jobs;

import android.animation.ValueAnimator;
import android.view.View;

import kore.botssdk.charts.utils.ObjectPool;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.ViewPortHandler;

public class AnimatedMoveViewJob extends AnimatedViewPortJob {
    private static final ObjectPool<AnimatedMoveViewJob> pool = ObjectPool.create(4, new AnimatedMoveViewJob(null, 0.0F, 0.0F, null, null, 0.0F, 0.0F, 0L));

    public static AnimatedMoveViewJob getInstance(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v, float xOrigin, float yOrigin, long duration) {
        AnimatedMoveViewJob result = pool.get();
        result.mViewPortHandler = viewPortHandler;
        result.xValue = xValue;
        result.yValue = yValue;
        result.mTrans = trans;
        result.view = v;
        result.xOrigin = xOrigin;
        result.yOrigin = yOrigin;
        result.animator.setDuration(duration);
        return result;
    }

    public static void recycleInstance(AnimatedMoveViewJob instance) {
        pool.recycle(instance);
    }

    public AnimatedMoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v, float xOrigin, float yOrigin, long duration) {
        super(viewPortHandler, xValue, yValue, trans, v, xOrigin, yOrigin, duration);
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        this.pts[0] = this.xOrigin + (this.xValue - this.xOrigin) * this.phase;
        this.pts[1] = this.yOrigin + (this.yValue - this.yOrigin) * this.phase;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
    }

    public void recycleSelf() {
        recycleInstance(this);
    }

    protected ObjectPool.Poolable instantiate() {
        return new AnimatedMoveViewJob(null, 0.0F, 0.0F, null, null, 0.0F, 0.0F, 0L);
    }

    static {
        pool.setReplenishPercentage(0.5F);
    }
}
