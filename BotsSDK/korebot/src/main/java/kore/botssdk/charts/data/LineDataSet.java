package kore.botssdk.charts.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.formatter.DefaultFillFormatter;
import kore.botssdk.charts.formatter.IFillFormatter;
import kore.botssdk.charts.interfaces.datasets.ILineDataSet;
import kore.botssdk.charts.utils.ColorTemplate;
import kore.botssdk.charts.utils.Utils;
import kore.botssdk.utils.LogUtils;

public class LineDataSet extends LineRadarDataSet<kore.botssdk.charts.data.Entry> implements ILineDataSet {
    private Mode mMode;
    private List<Integer> mCircleColors;
    private int mCircleHoleColor;
    private float mCircleRadius;
    private float mCircleHoleRadius;
    private float mCubicIntensity;
    private DashPathEffect mDashPathEffect;
    private IFillFormatter mFillFormatter;
    private boolean mDrawCircles;
    private boolean mDrawCircleHole;

    public LineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
        this.mMode = LineDataSet.Mode.LINEAR;
        this.mCircleColors = null;
        this.mCircleHoleColor = -1;
        this.mCircleRadius = 8.0F;
        this.mCircleHoleRadius = 4.0F;
        this.mCubicIntensity = 0.2F;
        this.mDashPathEffect = null;
        this.mFillFormatter = new DefaultFillFormatter();
        this.mDrawCircles = true;
        this.mDrawCircleHole = true;
        if (this.mCircleColors == null) {
            this.mCircleColors = new ArrayList();
        }

        this.mCircleColors.clear();
        this.mCircleColors.add(Color.rgb(140, 234, 255));
    }

    public DataSet<Entry> copy() {
        List<Entry> entries = new ArrayList();

        for(int i = 0; i < this.mValues.size(); ++i) {
            entries.add(this.mValues.get(i).copy());
        }

        LineDataSet copied = new LineDataSet(entries, this.getLabel());
        this.copy(copied);
        return copied;
    }

    protected void copy(LineDataSet lineDataSet) {
        super.copy(lineDataSet);
        lineDataSet.mCircleColors = this.mCircleColors;
        lineDataSet.mCircleHoleColor = this.mCircleHoleColor;
        lineDataSet.mCircleHoleRadius = this.mCircleHoleRadius;
        lineDataSet.mCircleRadius = this.mCircleRadius;
        lineDataSet.mCubicIntensity = this.mCubicIntensity;
        lineDataSet.mDashPathEffect = this.mDashPathEffect;
        lineDataSet.mDrawCircleHole = this.mDrawCircleHole;
        lineDataSet.mDrawCircles = this.mDrawCircleHole;
        lineDataSet.mFillFormatter = this.mFillFormatter;
        lineDataSet.mMode = this.mMode;
    }

    public Mode getMode() {
        return this.mMode;
    }

    public void setMode(LineDataSet.Mode mode) {
        this.mMode = mode;
    }

    public void setCubicIntensity(float intensity) {
        if (intensity > 1.0F) {
            intensity = 1.0F;
        }

        if (intensity < 0.05F) {
            intensity = 0.05F;
        }

        this.mCubicIntensity = intensity;
    }

    public float getCubicIntensity() {
        return this.mCubicIntensity;
    }

    public void setCircleRadius(float radius) {
        if (radius >= 1.0F) {
            this.mCircleRadius = Utils.convertDpToPixel(radius);
        } else {
            LogUtils.e("LineDataSet", "Circle radius cannot be < 1");
        }

    }

    public float getCircleRadius() {
        return this.mCircleRadius;
    }

    public void setCircleHoleRadius(float holeRadius) {
        if (holeRadius >= 0.5F) {
            this.mCircleHoleRadius = Utils.convertDpToPixel(holeRadius);
        } else {
            LogUtils.e("LineDataSet", "Circle radius cannot be < 0.5");
        }

    }

    public float getCircleHoleRadius() {
        return this.mCircleHoleRadius;
    }

    /** @deprecated */
    @Deprecated
    public void setCircleSize(float size) {
        this.setCircleRadius(size);
    }

    /** @deprecated */
    @Deprecated
    public float getCircleSize() {
        return this.getCircleRadius();
    }

    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        this.mDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }

    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }

    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }

    public void setDrawCircles(boolean enabled) {
        this.mDrawCircles = enabled;
    }

    public boolean isDrawCirclesEnabled() {
        return this.mDrawCircles;
    }

    /** @deprecated */
    @Deprecated
    public boolean isDrawCubicEnabled() {
        return this.mMode == LineDataSet.Mode.CUBIC_BEZIER;
    }

    /** @deprecated */
    @Deprecated
    public boolean isDrawSteppedEnabled() {
        return this.mMode == LineDataSet.Mode.STEPPED;
    }

    public List<Integer> getCircleColors() {
        return this.mCircleColors;
    }

    public int getCircleColor(int index) {
        return this.mCircleColors.get(index);
    }

    public int getCircleColorCount() {
        return this.mCircleColors.size();
    }

    public void setCircleColors(List<Integer> colors) {
        this.mCircleColors = colors;
    }

    public void setCircleColors(int... colors) {
        this.mCircleColors = ColorTemplate.createColors(colors);
    }

    public void setCircleColors(int[] colors, Context c) {
        List<Integer> clrs = this.mCircleColors;
        if (clrs == null) {
            clrs = new ArrayList();
        }

        clrs.clear();
        int[] var4 = colors;
        int var5 = colors.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            int color = var4[var6];
            clrs.add(c.getResources().getColor(color));
        }

        this.mCircleColors = clrs;
    }

    public void setCircleColor(int color) {
        this.resetCircleColors();
        this.mCircleColors.add(color);
    }

    public void resetCircleColors() {
        if (this.mCircleColors == null) {
            this.mCircleColors = new ArrayList();
        }

        this.mCircleColors.clear();
    }

    public void setCircleHoleColor(int color) {
        this.mCircleHoleColor = color;
    }

    public int getCircleHoleColor() {
        return this.mCircleHoleColor;
    }

    public void setDrawCircleHole(boolean enabled) {
        this.mDrawCircleHole = enabled;
    }

    public boolean isDrawCircleHoleEnabled() {
        return this.mDrawCircleHole;
    }

    public void setFillFormatter(IFillFormatter formatter) {
        if (formatter == null) {
            this.mFillFormatter = new DefaultFillFormatter();
        } else {
            this.mFillFormatter = formatter;
        }

    }

    public IFillFormatter getFillFormatter() {
        return this.mFillFormatter;
    }

    public enum Mode {
        LINEAR,
        STEPPED,
        CUBIC_BEZIER,
        HORIZONTAL_BEZIER;

        Mode() {
        }
    }
}
