package kore.botssdk.charts.jobs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;

import kore.botssdk.charts.utils.Transformer;
import kore.botssdk.charts.utils.ViewPortHandler;

public abstract class AnimatedViewPortJob extends ViewPortJob implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    protected final ObjectAnimator animator;
    protected float phase;
    protected float xOrigin;
    protected float yOrigin;

    public AnimatedViewPortJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v, float xOrigin, float yOrigin, long duration) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.animator = ObjectAnimator.ofFloat(this, "phase", 0.0F, 1.0F);
        this.animator.setDuration(duration);
        this.animator.addUpdateListener(this);
        this.animator.addListener(this);
    }

    @SuppressLint({"NewApi"})
    public void run() {
        this.animator.start();
    }

    public float getPhase() {
        return this.phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    public float getXOrigin() {
        return this.xOrigin;
    }

    public float getYOrigin() {
        return this.yOrigin;
    }

    public abstract void recycleSelf();

    protected void resetAnimator() {
        this.animator.removeAllListeners();
        this.animator.removeAllUpdateListeners();
        this.animator.reverse();
        this.animator.addUpdateListener(this);
        this.animator.addListener(this);
    }

    public void onAnimationStart(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        try {
            this.recycleSelf();
        } catch (IllegalArgumentException var3) {
        }

    }

    public void onAnimationCancel(Animator animation) {
        try {
            this.recycleSelf();
        } catch (IllegalArgumentException var3) {
        }

    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationUpdate(ValueAnimator animation) {
    }
}
