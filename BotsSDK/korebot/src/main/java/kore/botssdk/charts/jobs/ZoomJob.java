package kore.botssdk.charts.jobs;

import android.graphics.Matrix;
import android.view.View;

import kore.botssdk.charts.charts.BarLineChartBase;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.utils.ObjectPool;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.ViewPortHandler;

public class ZoomJob extends ViewPortJob {
    private static final ObjectPool<ZoomJob> pool = ObjectPool.create(1, new ZoomJob(null, 0.0F, 0.0F, 0.0F, 0.0F, null, null, null));
    protected float scaleX;
    protected float scaleY;
    protected YAxis.AxisDependency axisDependency;
    protected final Matrix mRunMatrixBuffer = new Matrix();

    public static ZoomJob getInstance(ViewPortHandler viewPortHandler, float scaleX, float scaleY, float xValue, float yValue, Transformer trans, YAxis.AxisDependency axis, View v) {
        ZoomJob result = pool.get();
        result.xValue = xValue;
        result.yValue = yValue;
        result.scaleX = scaleX;
        result.scaleY = scaleY;
        result.mViewPortHandler = viewPortHandler;
        result.mTrans = trans;
        result.axisDependency = axis;
        result.view = v;
        return result;
    }

    public static void recycleInstance(ZoomJob instance) {
        pool.recycle(instance);
    }

    public ZoomJob(ViewPortHandler viewPortHandler, float scaleX, float scaleY, float xValue, float yValue, Transformer trans, YAxis.AxisDependency axis, View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.axisDependency = axis;
    }

    public void run() {
        Matrix save = this.mRunMatrixBuffer;
        this.mViewPortHandler.zoom(this.scaleX, this.scaleY, save);
        this.mViewPortHandler.refresh(save, this.view, false);
        float yValsInView = ((BarLineChartBase)this.view).getAxis(this.axisDependency).mAxisRange / this.mViewPortHandler.getScaleY();
        float xValsInView = ((BarLineChartBase)this.view).getXAxis().mAxisRange / this.mViewPortHandler.getScaleX();
        this.pts[0] = this.xValue - xValsInView / 2.0F;
        this.pts[1] = this.yValue + yValsInView / 2.0F;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.translate(this.pts, save);
        this.mViewPortHandler.refresh(save, this.view, false);
        ((BarLineChartBase)this.view).calculateOffsets();
        this.view.postInvalidate();
        recycleInstance(this);
    }

    protected ObjectPool.Poolable instantiate() {
        return new ZoomJob(null, 0.0F, 0.0F, 0.0F, 0.0F, null, null, null);
    }

    static {
        pool.setReplenishPercentage(0.5F);
    }
}
