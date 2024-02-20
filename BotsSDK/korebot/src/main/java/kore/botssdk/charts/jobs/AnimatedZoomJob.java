package kore.botssdk.charts.jobs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.view.View;

import kore.botssdk.charts.charts.BarLineChartBase;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.utils.ObjectPool;
import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.ViewPortHandler;

public class AnimatedZoomJob extends AnimatedViewPortJob implements Animator.AnimatorListener {
    private static final ObjectPool<AnimatedZoomJob> pool = ObjectPool.create(8, new AnimatedZoomJob(null, null, null, null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0L));
    protected final float zoomOriginX;
    protected final float zoomOriginY;
    protected final float zoomCenterX;
    protected final float zoomCenterY;
    protected YAxis yAxis;
    protected float xAxisRange;
    protected final Matrix mOnAnimationUpdateMatrixBuffer = new Matrix();

    public static AnimatedZoomJob getInstance(ViewPortHandler viewPortHandler, View v, Transformer trans, YAxis axis, float xAxisRange, float scaleX, float scaleY, float xOrigin, float yOrigin, float zoomCenterX, float zoomCenterY, float zoomOriginX, float zoomOriginY, long duration) {
        AnimatedZoomJob result = pool.get();
        result.mViewPortHandler = viewPortHandler;
        result.xValue = scaleX;
        result.yValue = scaleY;
        result.mTrans = trans;
        result.view = v;
        result.xOrigin = xOrigin;
        result.yOrigin = yOrigin;
        result.yAxis = axis;
        result.xAxisRange = xAxisRange;
        result.resetAnimator();
        result.animator.setDuration(duration);
        return result;
    }

    @SuppressLint({"NewApi"})
    public AnimatedZoomJob(ViewPortHandler viewPortHandler, View v, Transformer trans, YAxis axis, float xAxisRange, float scaleX, float scaleY, float xOrigin, float yOrigin, float zoomCenterX, float zoomCenterY, float zoomOriginX, float zoomOriginY, long duration) {
        super(viewPortHandler, scaleX, scaleY, trans, v, xOrigin, yOrigin, duration);
        this.zoomCenterX = zoomCenterX;
        this.zoomCenterY = zoomCenterY;
        this.zoomOriginX = zoomOriginX;
        this.zoomOriginY = zoomOriginY;
        this.animator.addListener(this);
        this.yAxis = axis;
        this.xAxisRange = xAxisRange;
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        float scaleX = this.xOrigin + (this.xValue - this.xOrigin) * this.phase;
        float scaleY = this.yOrigin + (this.yValue - this.yOrigin) * this.phase;
        Matrix save = this.mOnAnimationUpdateMatrixBuffer;
        this.mViewPortHandler.setZoom(scaleX, scaleY, save);
        this.mViewPortHandler.refresh(save, this.view, false);
        float valsInView = this.yAxis.mAxisRange / this.mViewPortHandler.getScaleY();
        float xsInView = this.xAxisRange / this.mViewPortHandler.getScaleX();
        this.pts[0] = this.zoomOriginX + (this.zoomCenterX - xsInView / 2.0F - this.zoomOriginX) * this.phase;
        this.pts[1] = this.zoomOriginY + (this.zoomCenterY + valsInView / 2.0F - this.zoomOriginY) * this.phase;
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.translate(this.pts, save);
        this.mViewPortHandler.refresh(save, this.view, true);
    }

    public void onAnimationEnd(Animator animation) {
        ((BarLineChartBase)this.view).calculateOffsets();
        this.view.postInvalidate();
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void recycleSelf() {
    }

    public void onAnimationStart(Animator animation) {
    }

    protected ObjectPool.Poolable instantiate() {
        return new AnimatedZoomJob(null, null, null, null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0L);
    }
}
