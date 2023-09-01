package kore.botssdk.charts.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.List;

import kore.botssdk.charts.components.XAxis;
import kore.botssdk.charts.data.PieData;
import kore.botssdk.charts.data.PieEntry;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.highlight.PieHighlighter;
import kore.botssdk.charts.interfaces.datasets.IPieDataSet;
import kore.botssdk.charts.renderer.PieChartRenderer;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;

public class PieChart extends PieRadarChartBase<PieData> {
    private final RectF mCircleBox = new RectF();
    private boolean mDrawEntryLabels = true;
    private float[] mDrawAngles = new float[1];
    private float[] mAbsoluteAngles = new float[1];
    private boolean mDrawHole = true;
    private boolean mDrawSlicesUnderHole = false;
    private boolean mUsePercentValues = false;
    private boolean mDrawRoundedSlices = false;
    private CharSequence mCenterText = "";
    private final MPPointF mCenterTextOffset = MPPointF.getInstance(0.0F, 0.0F);
    private float mHoleRadiusPercent = 50.0F;
    protected float mTransparentCircleRadiusPercent = 55.0F;
    private boolean mDrawCenterText = true;
    private float mCenterTextRadiusPercent = 100.0F;
    protected float mMaxAngle = 360.0F;
    private float mMinAngleForSlices = 0.0F;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init() {
        super.init();
        this.mRenderer = new PieChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxis = null;
        this.mHighlighter = new PieHighlighter(this);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData != null) {
            this.mRenderer.drawData(canvas);
            if (this.valuesToHighlight()) {
                this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
            }

            this.mRenderer.drawExtras(canvas);
            this.mRenderer.drawValues(canvas);
            this.mLegendRenderer.renderLegend(canvas);
            this.drawDescription(canvas);
            this.drawMarkers(canvas);
        }
    }

    public void calculateOffsets() {
        super.calculateOffsets();
        if (this.mData != null) {
            float diameter = this.getDiameter();
            float radius = diameter / 2.0F;
            MPPointF c = this.getCenterOffsets();
            float shift = this.mData.getDataSet().getSelectionShift();
            this.mCircleBox.set(c.x - radius + shift, c.y - radius + shift, c.x + radius - shift, c.y + radius - shift);
            MPPointF.recycleInstance(c);
        }
    }

    protected void calcMinMax() {
        this.calcAngles();
    }

    protected float[] getMarkerPosition(Highlight highlight) {
        MPPointF center = this.getCenterCircleBox();
        float r = this.getRadius();
        float off = r / 10.0F * 3.6F;
        if (this.isDrawHoleEnabled()) {
            off = (r - r / 100.0F * this.getHoleRadius()) / 2.0F;
        }

        r -= off;
        float rotationAngle = this.getRotationAngle();
        int entryIndex = (int)highlight.getX();
        float offset = this.mDrawAngles[entryIndex] / 2.0F;
        float x = (float)((double)r * Math.cos(Math.toRadians((rotationAngle + this.mAbsoluteAngles[entryIndex] - offset) * this.mAnimator.getPhaseY())) + (double)center.x);
        float y = (float)((double)r * Math.sin(Math.toRadians((rotationAngle + this.mAbsoluteAngles[entryIndex] - offset) * this.mAnimator.getPhaseY())) + (double)center.y);
        MPPointF.recycleInstance(center);
        return new float[]{x, y};
    }

    private void calcAngles() {
        int entryCount = this.mData.getEntryCount();
        if (this.mDrawAngles.length != entryCount) {
            this.mDrawAngles = new float[entryCount];
        } else {
            for(int i = 0; i < entryCount; ++i) {
                this.mDrawAngles[i] = 0.0F;
            }
        }

        if (this.mAbsoluteAngles.length != entryCount) {
            this.mAbsoluteAngles = new float[entryCount];
        } else {
            for(int i = 0; i < entryCount; ++i) {
                this.mAbsoluteAngles[i] = 0.0F;
            }
        }

        float yValueSum = this.mData.getYValueSum();
        List<IPieDataSet> dataSets = this.mData.getDataSets();
        boolean hasMinAngle = this.mMinAngleForSlices != 0.0F && (float)entryCount * this.mMinAngleForSlices <= this.mMaxAngle;
        float[] minAngles = new float[entryCount];
        int cnt = 0;
        float offset = 0.0F;
        float diff = 0.0F;

        for(int i = 0; i < this.mData.getDataSetCount(); ++i) {
            IPieDataSet set = dataSets.get(i);

            for(int j = 0; j < set.getEntryCount(); ++j) {
                float drawAngle = this.calcAngle(Math.abs(set.getEntryForIndex(j).getY()), yValueSum);
                if (hasMinAngle) {
                    float temp = drawAngle - this.mMinAngleForSlices;
                    if (temp <= 0.0F) {
                        minAngles[cnt] = this.mMinAngleForSlices;
                        offset += -temp;
                    } else {
                        minAngles[cnt] = drawAngle;
                        diff += temp;
                    }
                }

                this.mDrawAngles[cnt] = drawAngle;
                if (cnt == 0) {
                    this.mAbsoluteAngles[cnt] = this.mDrawAngles[cnt];
                } else {
                    this.mAbsoluteAngles[cnt] = this.mAbsoluteAngles[cnt - 1] + this.mDrawAngles[cnt];
                }

                ++cnt;
            }
        }

        if (hasMinAngle) {
            for(int i = 0; i < entryCount; ++i) {
                minAngles[i] -= (minAngles[i] - this.mMinAngleForSlices) / diff * offset;
                if (i == 0) {
                    this.mAbsoluteAngles[0] = minAngles[0];
                } else {
                    this.mAbsoluteAngles[i] = this.mAbsoluteAngles[i - 1] + minAngles[i];
                }
            }

            this.mDrawAngles = minAngles;
        }

    }

    public boolean needsHighlight(int index) {
        if (!this.valuesToHighlight()) {
            return false;
        } else {
            for(int i = 0; i < this.mIndicesToHighlight.length; ++i) {
                if ((int)this.mIndicesToHighlight[i].getX() == index) {
                    return true;
                }
            }

            return false;
        }
    }

    private float calcAngle(float value) {
        return this.calcAngle(value, this.mData.getYValueSum());
    }

    private float calcAngle(float value, float yValueSum) {
        return value / yValueSum * this.mMaxAngle;
    }

    /** @deprecated */
    @Deprecated
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }

    public int getIndexForAngle(float angle) {
        float a = Utils.getNormalizedAngle(angle - this.getRotationAngle());

        for(int i = 0; i < this.mAbsoluteAngles.length; ++i) {
            if (this.mAbsoluteAngles[i] > a) {
                return i;
            }
        }

        return -1;
    }

    public int getDataSetIndexForIndex(int xIndex) {
        List<IPieDataSet> dataSets = this.mData.getDataSets();

        for(int i = 0; i < dataSets.size(); ++i) {
            if (dataSets.get(i).getEntryForXValue((float)xIndex, 0.0F) != null) {
                return i;
            }
        }

        return -1;
    }

    public float[] getDrawAngles() {
        return this.mDrawAngles;
    }

    public float[] getAbsoluteAngles() {
        return this.mAbsoluteAngles;
    }

    public void setHoleColor(int color) {
        ((PieChartRenderer)this.mRenderer).getPaintHole().setColor(color);
    }

    public void setDrawSlicesUnderHole(boolean enable) {
        this.mDrawSlicesUnderHole = enable;
    }

    public boolean isDrawSlicesUnderHoleEnabled() {
        return this.mDrawSlicesUnderHole;
    }

    public void setDrawHoleEnabled(boolean enabled) {
        this.mDrawHole = enabled;
    }

    public boolean isDrawHoleEnabled() {
        return this.mDrawHole;
    }

    public void setCenterText(CharSequence text) {
        if (text == null) {
            this.mCenterText = "";
        } else {
            this.mCenterText = text;
        }

    }

    public CharSequence getCenterText() {
        return this.mCenterText;
    }

    public void setDrawCenterText(boolean enabled) {
        this.mDrawCenterText = enabled;
    }

    public boolean isDrawCenterTextEnabled() {
        return this.mDrawCenterText;
    }

    protected float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 2.0F;
    }

    protected float getRequiredBaseOffset() {
        return 0.0F;
    }

    public float getRadius() {
        return this.mCircleBox == null ? 0.0F : Math.min(this.mCircleBox.width() / 2.0F, this.mCircleBox.height() / 2.0F);
    }

    public RectF getCircleBox() {
        return this.mCircleBox;
    }

    public MPPointF getCenterCircleBox() {
        return MPPointF.getInstance(this.mCircleBox.centerX(), this.mCircleBox.centerY());
    }

    public void setCenterTextTypeface(Typeface t) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTypeface(t);
    }

    public void setCenterTextSize(float sizeDp) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(Utils.convertDpToPixel(sizeDp));
    }

    public void setCenterTextSizePixels(float sizePixels) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setTextSize(sizePixels);
    }

    public void setCenterTextOffset(float x, float y) {
        this.mCenterTextOffset.x = Utils.convertDpToPixel(x);
        this.mCenterTextOffset.y = Utils.convertDpToPixel(y);
    }

    public MPPointF getCenterTextOffset() {
        return MPPointF.getInstance(this.mCenterTextOffset.x, this.mCenterTextOffset.y);
    }

    public void setCenterTextColor(int color) {
        ((PieChartRenderer)this.mRenderer).getPaintCenterText().setColor(color);
    }

    public void setHoleRadius(float percent) {
        this.mHoleRadiusPercent = percent;
    }

    public float getHoleRadius() {
        return this.mHoleRadiusPercent;
    }

    public void setTransparentCircleColor(int color) {
        Paint p = ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle();
        int alpha = p.getAlpha();
        p.setColor(color);
        p.setAlpha(alpha);
    }

    public void setTransparentCircleRadius(float percent) {
        this.mTransparentCircleRadiusPercent = percent;
    }

    public float getTransparentCircleRadius() {
        return this.mTransparentCircleRadiusPercent;
    }

    public void setTransparentCircleAlpha(int alpha) {
        ((PieChartRenderer)this.mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }

    /** @deprecated */
    @Deprecated
    public void setDrawSliceText(boolean enabled) {
        this.mDrawEntryLabels = enabled;
    }

    public void setDrawEntryLabels(boolean enabled) {
        this.mDrawEntryLabels = enabled;
    }

    public boolean isDrawEntryLabelsEnabled() {
        return this.mDrawEntryLabels;
    }

    public void setEntryLabelColor(int color) {
        ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setColor(color);
    }

    public void setEntryLabelTypeface(Typeface tf) {
        ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setTypeface(tf);
    }

    public void setEntryLabelTextSize(float size) {
        ((PieChartRenderer)this.mRenderer).getPaintEntryLabels().setTextSize(Utils.convertDpToPixel(size));
    }

    public void setDrawRoundedSlices(boolean enabled) {
        this.mDrawRoundedSlices = enabled;
    }

    public boolean isDrawRoundedSlicesEnabled() {
        return this.mDrawRoundedSlices;
    }

    public void setUsePercentValues(boolean enabled) {
        this.mUsePercentValues = enabled;
    }

    public boolean isUsePercentValuesEnabled() {
        return this.mUsePercentValues;
    }

    public void setCenterTextRadiusPercent(float percent) {
        this.mCenterTextRadiusPercent = percent;
    }

    public float getCenterTextRadiusPercent() {
        return this.mCenterTextRadiusPercent;
    }

    public float getMaxAngle() {
        return this.mMaxAngle;
    }

    public void setMaxAngle(float maxangle) {
        if (maxangle > 360.0F) {
            maxangle = 360.0F;
        }

        if (maxangle < 90.0F) {
            maxangle = 90.0F;
        }

        this.mMaxAngle = maxangle;
    }

    public float getMinAngleForSlices() {
        return this.mMinAngleForSlices;
    }

    public void setMinAngleForSlices(float minAngle) {
        if (minAngle > this.mMaxAngle / 2.0F) {
            minAngle = this.mMaxAngle / 2.0F;
        } else if (minAngle < 0.0F) {
            minAngle = 0.0F;
        }

        this.mMinAngleForSlices = minAngle;
    }

    protected void onDetachedFromWindow() {
        if (this.mRenderer != null && this.mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer)this.mRenderer).releaseBitmap();
        }

        super.onDetachedFromWindow();
    }
}
