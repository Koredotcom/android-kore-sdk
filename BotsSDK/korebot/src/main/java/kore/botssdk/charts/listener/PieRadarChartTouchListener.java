package kore.botssdk.charts.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import kore.botssdk.charts.charts.PieRadarChartBase;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;

public class PieRadarChartTouchListener extends ChartTouchListener<PieRadarChartBase<?>> {
    private final MPPointF mTouchStartPoint = MPPointF.getInstance(0.0F, 0.0F);
    private float mStartAngle = 0.0F;
    private final ArrayList<PieRadarChartTouchListener.AngularVelocitySample> _velocitySamples = new ArrayList();
    private long mDecelerationLastTime = 0L;
    private float mDecelerationAngularVelocity = 0.0F;

    public PieRadarChartTouchListener(PieRadarChartBase<?> chart) {
        super(chart);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        if (this.mGestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            if (((PieRadarChartBase)this.mChart).isRotationEnabled()) {
                float x = event.getX();
                float y = event.getY();
                switch(event.getAction()) {
                    case 0:
                        this.startAction(event);
                        this.stopDeceleration();
                        this.resetVelocity();
                        if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                            this.sampleVelocity(x, y);
                        }

                        this.setGestureStartAngle(x, y);
                        this.mTouchStartPoint.x = x;
                        this.mTouchStartPoint.y = y;
                        break;
                    case 1:
                        if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                            this.stopDeceleration();
                            this.sampleVelocity(x, y);
                            this.mDecelerationAngularVelocity = this.calculateVelocity();
                            if (this.mDecelerationAngularVelocity != 0.0F) {
                                this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                                Utils.postInvalidateOnAnimation(this.mChart);
                            }
                        }

                        ((PieRadarChartBase)this.mChart).enableScroll();
                        this.mTouchMode = 0;
                        this.endAction(event);
                        break;
                    case 2:
                        if (((PieRadarChartBase)this.mChart).isDragDecelerationEnabled()) {
                            this.sampleVelocity(x, y);
                        }

                        if (this.mTouchMode == 0 && distance(x, this.mTouchStartPoint.x, y, this.mTouchStartPoint.y) > Utils.convertDpToPixel(8.0F)) {
                            this.mLastGesture = ChartGesture.ROTATE;
                            this.mTouchMode = 6;
                            ((PieRadarChartBase)this.mChart).disableScroll();
                        } else if (this.mTouchMode == 6) {
                            this.updateGestureRotation(x, y);
                            ((PieRadarChartBase)this.mChart).invalidate();
                        }

                        this.endAction(event);
                }
            }

            return true;
        }
    }

    public void onLongPress(MotionEvent me) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        OnChartGestureListener l = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartLongPressed(me);
        }

    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        OnChartGestureListener l = ((PieRadarChartBase)this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartSingleTapped(e);
        }

        if (!((PieRadarChartBase)this.mChart).isHighlightPerTapEnabled()) {
            return false;
        } else {
            Highlight high = ((PieRadarChartBase)this.mChart).getHighlightByTouchPoint(e.getX(), e.getY());
            this.performHighlight(high, e);
            return true;
        }
    }

    private void resetVelocity() {
        this._velocitySamples.clear();
    }

    private void sampleVelocity(float touchLocationX, float touchLocationY) {
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        this._velocitySamples.add(new PieRadarChartTouchListener.AngularVelocitySample(currentTime, ((PieRadarChartBase)this.mChart).getAngleForPoint(touchLocationX, touchLocationY)));
        int i = 0;

        for(int count = this._velocitySamples.size(); i < count - 2 && currentTime - ((PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(i)).time > 1000L; ++i) {
            this._velocitySamples.remove(0);
            --i;
            --count;
        }

    }

    private float calculateVelocity() {
        if (this._velocitySamples.isEmpty()) {
            return 0.0F;
        } else {
            PieRadarChartTouchListener.AngularVelocitySample firstSample = (PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(0);
            PieRadarChartTouchListener.AngularVelocitySample lastSample = (PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(this._velocitySamples.size() - 1);
            PieRadarChartTouchListener.AngularVelocitySample beforeLastSample = firstSample;

            for(int i = this._velocitySamples.size() - 1; i >= 0; --i) {
                beforeLastSample = (PieRadarChartTouchListener.AngularVelocitySample)this._velocitySamples.get(i);
                if (beforeLastSample.angle != lastSample.angle) {
                    break;
                }
            }

            float timeDelta = (float)(lastSample.time - firstSample.time) / 1000.0F;
            if (timeDelta == 0.0F) {
                timeDelta = 0.1F;
            }

            boolean clockwise = lastSample.angle >= beforeLastSample.angle;
            if ((double)Math.abs(lastSample.angle - beforeLastSample.angle) > 270.0D) {
                clockwise = !clockwise;
            }

            if ((double)(lastSample.angle - firstSample.angle) > 180.0D) {
                firstSample.angle = (float)((double)firstSample.angle + 360.0D);
            } else if ((double)(firstSample.angle - lastSample.angle) > 180.0D) {
                lastSample.angle = (float)((double)lastSample.angle + 360.0D);
            }

            float velocity = Math.abs((lastSample.angle - firstSample.angle) / timeDelta);
            if (!clockwise) {
                velocity = -velocity;
            }

            return velocity;
        }
    }

    public void setGestureStartAngle(float x, float y) {
        this.mStartAngle = ((PieRadarChartBase)this.mChart).getAngleForPoint(x, y) - ((PieRadarChartBase)this.mChart).getRawRotationAngle();
    }

    public void updateGestureRotation(float x, float y) {
        ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getAngleForPoint(x, y) - this.mStartAngle);
    }

    public void stopDeceleration() {
        this.mDecelerationAngularVelocity = 0.0F;
    }

    public void computeScroll() {
        if (this.mDecelerationAngularVelocity != 0.0F) {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDecelerationAngularVelocity *= ((PieRadarChartBase)this.mChart).getDragDecelerationFrictionCoef();
            float timeInterval = (float)(currentTime - this.mDecelerationLastTime) / 1000.0F;
            ((PieRadarChartBase)this.mChart).setRotationAngle(((PieRadarChartBase)this.mChart).getRotationAngle() + this.mDecelerationAngularVelocity * timeInterval);
            this.mDecelerationLastTime = currentTime;
            if ((double)Math.abs(this.mDecelerationAngularVelocity) >= 0.001D) {
                Utils.postInvalidateOnAnimation(this.mChart);
            } else {
                this.stopDeceleration();
            }

        }
    }

    private class AngularVelocitySample {
        public long time;
        public float angle;

        public AngularVelocitySample(long time, float angle) {
            this.time = time;
            this.angle = angle;
        }
    }
}
