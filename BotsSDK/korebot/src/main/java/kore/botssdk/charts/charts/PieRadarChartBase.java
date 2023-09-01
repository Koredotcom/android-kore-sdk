package kore.botssdk.charts.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import kore.botssdk.charts.animation.Easing;
import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.data.ChartData;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.listener.PieRadarChartTouchListener;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.utils.LogUtils;

public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>> extends Chart<T> {
    private float mRotationAngle = 270.0F;
    private float mRawRotationAngle = 270.0F;
    protected boolean mRotateEnabled = true;
    protected float mMinOffset = 0.0F;

    public PieRadarChartBase(Context context) {
        super(context);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mChartTouchListener = new PieRadarChartTouchListener(this);
    }

    protected void calcMinMax() {
    }

    public int getMaxVisibleCount() {
        return this.mData.getEntryCount();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mTouchEnabled && this.mChartTouchListener != null ? this.mChartTouchListener.onTouch(this, event) : super.onTouchEvent(event);
    }

    public void computeScroll() {
        if (this.mChartTouchListener instanceof PieRadarChartTouchListener) {
            ((PieRadarChartTouchListener)this.mChartTouchListener).computeScroll();
        }

    }

    public void notifyDataSetChanged() {
        if (this.mData != null) {
            this.calcMinMax();
            if (this.mLegend != null) {
                this.mLegendRenderer.computeLegend(this.mData);
            }

            this.calculateOffsets();
        }
    }

    public void calculateOffsets() {
        float legendLeft = 0.0F;
        float legendRight = 0.0F;
        float legendBottom = 0.0F;
        float legendTop = 0.0F;
        float fullLegendWidth;
        float yLegendOffset;
        float spacing;
        float legendWidth;
        float legendHeight;
        if (this.mLegend != null && this.mLegend.isEnabled() && !this.mLegend.isDrawInsideEnabled()) {
            fullLegendWidth = Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent());
            label62:
            switch(this.mLegend.getOrientation()) {
                case VERTICAL:
                    yLegendOffset = 0.0F;
                    if (this.mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.LEFT || this.mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT) {
                        if (this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.CENTER) {
                            spacing = Utils.convertDpToPixel(13.0F);
                            yLegendOffset = fullLegendWidth + spacing;
                        } else {
                            spacing = Utils.convertDpToPixel(8.0F);
                            legendWidth = fullLegendWidth + spacing;
                            legendHeight = this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax;
                            MPPointF center = this.getCenter();
                            float bottomX = this.mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT ? (float)this.getWidth() - legendWidth + 15.0F : legendWidth - 15.0F;
                            float bottomY = legendHeight + 15.0F;
                            float distLegend = this.distanceToCenter(bottomX, bottomY);
                            MPPointF reference = this.getPosition(center, this.getRadius(), this.getAngleForPoint(bottomX, bottomY));
                            float distReference = this.distanceToCenter(reference.x, reference.y);
                            float minOffset = Utils.convertDpToPixel(5.0F);
                            if (bottomY >= center.y && (float)this.getHeight() - legendWidth > (float)this.getWidth()) {
                                yLegendOffset = legendWidth;
                            } else if (distLegend < distReference) {
                                float diff = distReference - distLegend;
                                yLegendOffset = minOffset + diff;
                            }

                            MPPointF.recycleInstance(center);
                            MPPointF.recycleInstance(reference);
                        }
                    }

                    switch(this.mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            legendLeft = yLegendOffset;
                            break label62;
                        case RIGHT:
                            legendRight = yLegendOffset;
                            break label62;
                        case CENTER:
                            switch(this.mLegend.getVerticalAlignment()) {
                                case TOP:
                                    legendTop = Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                                    break label62;
                                case BOTTOM:
                                    legendBottom = Math.min(this.mLegend.mNeededHeight, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                                    break label62;
                            }
                    }
                case HORIZONTAL:
                    yLegendOffset = 0.0F;
                    if (this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.TOP || this.mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.BOTTOM) {
                        spacing = this.getRequiredLegendOffset();
                        yLegendOffset = Math.min(this.mLegend.mNeededHeight + spacing, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                        switch(this.mLegend.getVerticalAlignment()) {
                            case TOP:
                                legendTop = yLegendOffset;
                                break;
                            case BOTTOM:
                                legendBottom = yLegendOffset;
                        }
                    }
            }

            legendLeft += this.getRequiredBaseOffset();
            legendRight += this.getRequiredBaseOffset();
            legendTop += this.getRequiredBaseOffset();
            legendBottom += this.getRequiredBaseOffset();
        }

        fullLegendWidth = Utils.convertDpToPixel(this.mMinOffset);
        if (this instanceof RadarChart) {
            XAxis x = this.getXAxis();
            if (x.isEnabled() && x.isDrawLabelsEnabled()) {
                fullLegendWidth = Math.max(fullLegendWidth, (float)x.mLabelRotatedWidth);
            }
        }

        legendTop += this.getExtraTopOffset();
        legendRight += this.getExtraRightOffset();
        legendBottom += this.getExtraBottomOffset();
        legendLeft += this.getExtraLeftOffset();
        yLegendOffset = Math.max(fullLegendWidth, legendLeft);
        spacing = Math.max(fullLegendWidth, legendTop);
        legendWidth = Math.max(fullLegendWidth, legendRight);
        legendHeight = Math.max(fullLegendWidth, Math.max(this.getRequiredBaseOffset(), legendBottom));
        this.mViewPortHandler.restrainViewPort(yLegendOffset, spacing, legendWidth, legendHeight);
        if (this.mLogEnabled) {
            LogUtils.i("MPAndroidChart", "offsetLeft: " + yLegendOffset + ", offsetTop: " + spacing + ", offsetRight: " + legendWidth + ", offsetBottom: " + legendHeight);
        }

    }

    public float getAngleForPoint(float x, float y) {
        MPPointF c = this.getCenterOffsets();
        double tx = x - c.x;
        double ty = y - c.y;
        double length = Math.sqrt(tx * tx + ty * ty);
        double r = Math.acos(ty / length);
        float angle = (float)Math.toDegrees(r);
        if (x > c.x) {
            angle = 360.0F - angle;
        }

        angle += 90.0F;
        if (angle > 360.0F) {
            angle -= 360.0F;
        }

        MPPointF.recycleInstance(c);
        return angle;
    }

    public MPPointF getPosition(MPPointF center, float dist, float angle) {
        MPPointF p = MPPointF.getInstance(0.0F, 0.0F);
        this.getPosition(center, dist, angle, p);
        return p;
    }

    public void getPosition(MPPointF center, float dist, float angle, MPPointF outputPoint) {
        outputPoint.x = (float)((double)center.x + (double)dist * Math.cos(Math.toRadians(angle)));
        outputPoint.y = (float)((double)center.y + (double)dist * Math.sin(Math.toRadians(angle)));
    }

    public float distanceToCenter(float x, float y) {
        MPPointF c = this.getCenterOffsets();
        float dist = 0.0F;
        float xDist = 0.0F;
        float yDist = 0.0F;
        if (x > c.x) {
            xDist = x - c.x;
        } else {
            xDist = c.x - x;
        }

        if (y > c.y) {
            yDist = y - c.y;
        } else {
            yDist = c.y - y;
        }

        dist = (float)Math.sqrt(Math.pow(xDist, 2.0D) + Math.pow(yDist, 2.0D));
        MPPointF.recycleInstance(c);
        return dist;
    }

    public abstract int getIndexForAngle(float var1);

    public void setRotationAngle(float angle) {
        this.mRawRotationAngle = angle;
        this.mRotationAngle = Utils.getNormalizedAngle(this.mRawRotationAngle);
    }

    public float getRawRotationAngle() {
        return this.mRawRotationAngle;
    }

    public float getRotationAngle() {
        return this.mRotationAngle;
    }

    public void setRotationEnabled(boolean enabled) {
        this.mRotateEnabled = enabled;
    }

    public boolean isRotationEnabled() {
        return this.mRotateEnabled;
    }

    public float getMinOffset() {
        return this.mMinOffset;
    }

    public void setMinOffset(float minOffset) {
        this.mMinOffset = minOffset;
    }

    public float getDiameter() {
        RectF content = this.mViewPortHandler.getContentRect();
        content.left += this.getExtraLeftOffset();
        content.top += this.getExtraTopOffset();
        content.right -= this.getExtraRightOffset();
        content.bottom -= this.getExtraBottomOffset();
        return Math.min(content.width(), content.height());
    }

    public abstract float getRadius();

    protected abstract float getRequiredLegendOffset();

    protected abstract float getRequiredBaseOffset();

    public float getYChartMax() {
        return 0.0F;
    }

    public float getYChartMin() {
        return 0.0F;
    }

    @SuppressLint({"NewApi"})
    public void spin(int durationmillis, float fromangle, float toangle, Easing.EasingFunction easing) {
        this.setRotationAngle(fromangle);
        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(this, "rotationAngle", fromangle, toangle);
        spinAnimator.setDuration(durationmillis);
        spinAnimator.setInterpolator(easing);
        spinAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                PieRadarChartBase.this.postInvalidate();
            }
        });
        spinAnimator.start();
    }
}
