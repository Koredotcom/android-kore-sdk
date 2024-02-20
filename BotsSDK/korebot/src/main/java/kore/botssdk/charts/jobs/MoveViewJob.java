package kore.botssdk.charts.jobs;

import android.view.View;

import kore.botssdk.charts.utils.ObjectPool;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.ViewPortHandler;

public class MoveViewJob extends ViewPortJob {
    private static final ObjectPool<MoveViewJob> pool = ObjectPool.create(2, new MoveViewJob(null, 0.0F, 0.0F, null, null));

    public static MoveViewJob getInstance(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v) {
        MoveViewJob result = pool.get();
        result.mViewPortHandler = viewPortHandler;
        result.xValue = xValue;
        result.yValue = yValue;
        result.mTrans = trans;
        result.view = v;
        return result;
    }

    public static void recycleInstance(MoveViewJob instance) {
        pool.recycle(instance);
    }

    public MoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
    }

    public void run() {
        this.pts[0] = this.xValue;
        this.pts[1] = this.yValue;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
        recycleInstance(this);
    }

    protected ObjectPool.Poolable instantiate() {
        return new MoveViewJob(this.mViewPortHandler, this.xValue, this.yValue, this.mTrans, this.view);
    }

    static {
        pool.setReplenishPercentage(0.5F);
    }
}
