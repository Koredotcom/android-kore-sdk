package kore.botssdk.charts.data;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.List;

import kore.botssdk.charts.interfaces.datasets.ILineRadarDataSet;
import kore.botssdk.charts.utils.Utils;

public abstract class LineRadarDataSet<T extends Entry> extends LineScatterCandleRadarDataSet<T> implements ILineRadarDataSet<T> {
    private int mFillColor = Color.rgb(140, 234, 255);
    protected Drawable mFillDrawable;
    private int mFillAlpha = 85;
    private float mLineWidth = 2.5F;
    private boolean mDrawFilled = false;

    public LineRadarDataSet(List<T> yVals, String label) {
        super(yVals, label);
    }

    public int getFillColor() {
        return this.mFillColor;
    }

    public void setFillColor(int color) {
        this.mFillColor = color;
        this.mFillDrawable = null;
    }

    public Drawable getFillDrawable() {
        return this.mFillDrawable;
    }

    @TargetApi(18)
    public void setFillDrawable(Drawable drawable) {
        this.mFillDrawable = drawable;
    }

    public int getFillAlpha() {
        return this.mFillAlpha;
    }

    public void setFillAlpha(int alpha) {
        this.mFillAlpha = alpha;
    }

    public void setLineWidth(float width) {
        if (width < 0.0F) {
            width = 0.0F;
        }

        if (width > 10.0F) {
            width = 10.0F;
        }

        this.mLineWidth = Utils.convertDpToPixel(width);
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public void setDrawFilled(boolean filled) {
        this.mDrawFilled = filled;
    }

    public boolean isDrawFilledEnabled() {
        return this.mDrawFilled;
    }

    protected void copy(LineRadarDataSet lineRadarDataSet) {
        super.copy(lineRadarDataSet);
        lineRadarDataSet.mDrawFilled = this.mDrawFilled;
        lineRadarDataSet.mFillAlpha = this.mFillAlpha;
        lineRadarDataSet.mFillColor = this.mFillColor;
        lineRadarDataSet.mFillDrawable = this.mFillDrawable;
        lineRadarDataSet.mLineWidth = this.mLineWidth;
    }
}
