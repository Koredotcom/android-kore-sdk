package kore.botssdk.charts.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.model.GradientColor;
import kore.botssdk.charts.utils.ColorTemplate;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;

public abstract class BaseDataSet<T extends Entry> implements IDataSet<T> {
    protected List<Integer> mColors;
    protected GradientColor mGradientColor;
    protected List<GradientColor> mGradientColors;
    protected List<Integer> mValueColors;
    private String mLabel;
    protected YAxis.AxisDependency mAxisDependency;
    protected boolean mHighlightEnabled;
    protected transient ValueFormatter mValueFormatter;
    protected Typeface mValueTypeface;
    private Legend.LegendForm mForm;
    private float mFormSize;
    private float mFormLineWidth;
    private DashPathEffect mFormLineDashEffect;
    protected boolean mDrawValues;
    protected boolean mDrawIcons;
    protected MPPointF mIconsOffset;
    protected float mValueTextSize;
    protected boolean mVisible;

    public BaseDataSet() {
        this.mColors = null;
        this.mGradientColor = null;
        this.mGradientColors = null;
        this.mValueColors = null;
        this.mLabel = "DataSet";
        this.mAxisDependency = YAxis.AxisDependency.LEFT;
        this.mHighlightEnabled = true;
        this.mForm = Legend.LegendForm.DEFAULT;
        this.mFormSize = 0.0F / 0.0F;
        this.mFormLineWidth = 0.0F / 0.0F;
        this.mFormLineDashEffect = null;
        this.mDrawValues = true;
        this.mDrawIcons = true;
        this.mIconsOffset = new MPPointF();
        this.mValueTextSize = 17.0F;
        this.mVisible = true;
        this.mColors = new ArrayList();
        this.mValueColors = new ArrayList();
        this.mColors.add(Color.rgb(140, 234, 255));
        this.mValueColors.add(-16777216);
    }

    public BaseDataSet(String label) {
        this();
        this.mLabel = label;
    }

    public void notifyDataSetChanged() {
        this.calcMinMax();
    }

    public List<Integer> getColors() {
        return this.mColors;
    }

    public List<Integer> getValueColors() {
        return this.mValueColors;
    }

    public int getColor() {
        return this.mColors.get(0);
    }

    public int getColor(int index) {
        return this.mColors.get(index % this.mColors.size());
    }

    public GradientColor getGradientColor() {
        return this.mGradientColor;
    }

    public List<GradientColor> getGradientColors() {
        return this.mGradientColors;
    }

    public GradientColor getGradientColor(int index) {
        return this.mGradientColors.get(index % this.mGradientColors.size());
    }

    public void setColors(List<Integer> colors) {
        this.mColors = colors;
    }

    public void setColors(int... colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }

    public void setColors(int[] colors, Context c) {
        if (this.mColors == null) {
            this.mColors = new ArrayList();
        }

        this.mColors.clear();
        int[] var3 = colors;
        int var4 = colors.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int color = var3[var5];
            this.mColors.add(c.getResources().getColor(color));
        }

    }

    public void addColor(int color) {
        if (this.mColors == null) {
            this.mColors = new ArrayList();
        }

        this.mColors.add(color);
    }

    public void setColor(int color) {
        this.resetColors();
        this.mColors.add(color);
    }

    public void setGradientColor(int startColor, int endColor) {
        this.mGradientColor = new GradientColor(startColor, endColor);
    }

    public void setGradientColors(List<GradientColor> gradientColors) {
        this.mGradientColors = gradientColors;
    }

    public void setColor(int color, int alpha) {
        this.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    public void setColors(int[] colors, int alpha) {
        this.resetColors();
        int[] var3 = colors;
        int var4 = colors.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int color = var3[var5];
            this.addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }

    }

    public void resetColors() {
        if (this.mColors == null) {
            this.mColors = new ArrayList();
        }

        this.mColors.clear();
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setHighlightEnabled(boolean enabled) {
        this.mHighlightEnabled = enabled;
    }

    public boolean isHighlightEnabled() {
        return this.mHighlightEnabled;
    }

    public void setValueFormatter(ValueFormatter f) {
        if (f != null) {
            this.mValueFormatter = f;
        }
    }

    public ValueFormatter getValueFormatter() {
        return this.needsFormatter() ? Utils.getDefaultValueFormatter() : this.mValueFormatter;
    }

    public boolean needsFormatter() {
        return this.mValueFormatter == null;
    }

    public void setValueTextColor(int color) {
        this.mValueColors.clear();
        this.mValueColors.add(color);
    }

    public void setValueTextColors(List<Integer> colors) {
        this.mValueColors = colors;
    }

    public void setValueTypeface(Typeface tf) {
        this.mValueTypeface = tf;
    }

    public void setValueTextSize(float size) {
        this.mValueTextSize = Utils.convertDpToPixel(size);
    }

    public int getValueTextColor() {
        return this.mValueColors.get(0);
    }

    public int getValueTextColor(int index) {
        return this.mValueColors.get(index % this.mValueColors.size());
    }

    public Typeface getValueTypeface() {
        return this.mValueTypeface;
    }

    public float getValueTextSize() {
        return this.mValueTextSize;
    }

    public void setForm(Legend.LegendForm form) {
        this.mForm = form;
    }

    public Legend.LegendForm getForm() {
        return this.mForm;
    }

    public void setFormSize(float formSize) {
        this.mFormSize = formSize;
    }

    public float getFormSize() {
        return this.mFormSize;
    }

    public void setFormLineWidth(float formLineWidth) {
        this.mFormLineWidth = formLineWidth;
    }

    public float getFormLineWidth() {
        return this.mFormLineWidth;
    }

    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        this.mFormLineDashEffect = dashPathEffect;
    }

    public DashPathEffect getFormLineDashEffect() {
        return this.mFormLineDashEffect;
    }

    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    public boolean isDrawValuesEnabled() {
        return this.mDrawValues;
    }

    public void setDrawIcons(boolean enabled) {
        this.mDrawIcons = enabled;
    }

    public boolean isDrawIconsEnabled() {
        return this.mDrawIcons;
    }

    public void setIconsOffset(MPPointF offsetDp) {
        this.mIconsOffset.x = offsetDp.x;
        this.mIconsOffset.y = offsetDp.y;
    }

    public MPPointF getIconsOffset() {
        return this.mIconsOffset;
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public YAxis.AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }

    public void setAxisDependency(YAxis.AxisDependency dependency) {
        this.mAxisDependency = dependency;
    }

    public int getIndexInEntries(int xIndex) {
        for(int i = 0; i < this.getEntryCount(); ++i) {
            if ((float)xIndex == this.getEntryForIndex(i).getX()) {
                return i;
            }
        }

        return -1;
    }

    public boolean removeFirst() {
        if (this.getEntryCount() > 0) {
            T entry = this.getEntryForIndex(0);
            return this.removeEntry(entry);
        } else {
            return false;
        }
    }

    public boolean removeLast() {
        if (this.getEntryCount() > 0) {
            T e = this.getEntryForIndex(this.getEntryCount() - 1);
            return this.removeEntry(e);
        } else {
            return false;
        }
    }

    public boolean removeEntryByXValue(float xValue) {
        T e = this.getEntryForXValue(xValue, 0.0F / 0.0F);
        return this.removeEntry(e);
    }

    public boolean removeEntry(int index) {
        T e = this.getEntryForIndex(index);
        return this.removeEntry(e);
    }

    public boolean contains(T e) {
        for(int i = 0; i < this.getEntryCount(); ++i) {
            if (Objects.equals(this.getEntryForIndex(i), e)) {
                return true;
            }
        }

        return false;
    }

    protected void copy(BaseDataSet baseDataSet) {
        baseDataSet.mAxisDependency = this.mAxisDependency;
        baseDataSet.mColors = this.mColors;
        baseDataSet.mDrawIcons = this.mDrawIcons;
        baseDataSet.mDrawValues = this.mDrawValues;
        baseDataSet.mForm = this.mForm;
        baseDataSet.mFormLineDashEffect = this.mFormLineDashEffect;
        baseDataSet.mFormLineWidth = this.mFormLineWidth;
        baseDataSet.mFormSize = this.mFormSize;
        baseDataSet.mGradientColor = this.mGradientColor;
        baseDataSet.mGradientColors = this.mGradientColors;
        baseDataSet.mHighlightEnabled = this.mHighlightEnabled;
        baseDataSet.mIconsOffset = this.mIconsOffset;
        baseDataSet.mValueColors = this.mValueColors;
        baseDataSet.mValueFormatter = this.mValueFormatter;
        baseDataSet.mValueColors = this.mValueColors;
        baseDataSet.mValueTextSize = this.mValueTextSize;
        baseDataSet.mVisible = this.mVisible;
    }
}
