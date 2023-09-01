package kore.botssdk.charts.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;



public class ChartAnimator {
    private ValueAnimator.AnimatorUpdateListener mListener;
    protected float mPhaseY = 1.0F;
    protected float mPhaseX = 1.0F;

    public ChartAnimator() {
    }

    public ChartAnimator(ValueAnimator.AnimatorUpdateListener listener) {
        this.mListener = listener;
    }

    private ObjectAnimator xAnimator(int duration, Easing.EasingFunction easing) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0.0F, 1.0F);
        animatorX.setInterpolator(easing);
        animatorX.setDuration(duration);
        return animatorX;
    }

    private ObjectAnimator yAnimator(int duration, Easing.EasingFunction easing) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0.0F, 1.0F);
        animatorY.setInterpolator(easing);
        animatorY.setDuration(duration);
        return animatorY;
    }

    public void animateX(int durationMillis) {
        this.animateX(durationMillis, Easing.Linear);
    }

    public void animateX(int durationMillis, Easing.EasingFunction easing) {
        ObjectAnimator animatorX = this.xAnimator(durationMillis, easing);
        animatorX.addUpdateListener(this.mListener);
        animatorX.start();
    }

    public void animateXY(int durationMillisX, int durationMillisY) {
        this.animateXY(durationMillisX, durationMillisY, Easing.Linear, Easing.Linear);
    }

    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingFunction easing) {
        ObjectAnimator xAnimator = this.xAnimator(durationMillisX, easing);
        ObjectAnimator yAnimator = this.yAnimator(durationMillisY, easing);
        if (durationMillisX > durationMillisY) {
            xAnimator.addUpdateListener(this.mListener);
        } else {
            yAnimator.addUpdateListener(this.mListener);
        }

        xAnimator.start();
        yAnimator.start();
    }

    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingFunction easingX, Easing.EasingFunction easingY) {
        ObjectAnimator xAnimator = this.xAnimator(durationMillisX, easingX);
        ObjectAnimator yAnimator = this.yAnimator(durationMillisY, easingY);
        if (durationMillisX > durationMillisY) {
            xAnimator.addUpdateListener(this.mListener);
        } else {
            yAnimator.addUpdateListener(this.mListener);
        }

        xAnimator.start();
        yAnimator.start();
    }

    public void animateY(int durationMillis) {
        this.animateY(durationMillis, Easing.Linear);
    }

    public void animateY(int durationMillis, Easing.EasingFunction easing) {
        ObjectAnimator animatorY = this.yAnimator(durationMillis, easing);
        animatorY.addUpdateListener(this.mListener);
        animatorY.start();
    }

    public float getPhaseY() {
        return this.mPhaseY;
    }

    public void setPhaseY(float phase) {
        if (phase > 1.0F) {
            phase = 1.0F;
        } else if (phase < 0.0F) {
            phase = 0.0F;
        }

        this.mPhaseY = phase;
    }

    public float getPhaseX() {
        return this.mPhaseX;
    }

    public void setPhaseX(float phase) {
        if (phase > 1.0F) {
            phase = 1.0F;
        } else if (phase < 0.0F) {
            phase = 0.0F;
        }

        this.mPhaseX = phase;
    }
}
