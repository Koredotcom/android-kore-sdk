package kore.botssdk.charts.interfaces.datasets;

import android.graphics.DashPathEffect;
import android.graphics.Typeface;

import java.util.List;

import kore.botssdk.charts.components.Legend;
import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.DataSet;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.model.GradientColor;
import kore.botssdk.charts.utils.MPPointF;

public interface IDataSet<T extends Entry> {
    float getYMin();

    float getYMax();

    float getXMin();

    float getXMax();

    int getEntryCount();

    void calcMinMax();

    void calcMinMaxY(float var1, float var2);

    T getEntryForXValue(float var1, float var2, DataSet.Rounding var3);

    T getEntryForXValue(float var1, float var2);

    List<T> getEntriesForXValue(float var1);

    T getEntryForIndex(int var1);

    int getEntryIndex(float var1, float var2, DataSet.Rounding var3);

    int getEntryIndex(T var1);

    int getIndexInEntries(int var1);

    boolean addEntry(T var1);

    void addEntryOrdered(T var1);

    boolean removeFirst();

    boolean removeLast();

    boolean removeEntry(T var1);

    boolean removeEntryByXValue(float var1);

    boolean removeEntry(int var1);

    boolean contains(T var1);

    void clear();

    String getLabel();

    void setLabel(String var1);

    YAxis.AxisDependency getAxisDependency();

    void setAxisDependency(YAxis.AxisDependency var1);

    List<Integer> getColors();

    int getColor();

    GradientColor getGradientColor();

    List<GradientColor> getGradientColors();

    GradientColor getGradientColor(int var1);

    int getColor(int var1);

    boolean isHighlightEnabled();

    void setHighlightEnabled(boolean var1);

    void setValueFormatter(ValueFormatter var1);

    ValueFormatter getValueFormatter();

    boolean needsFormatter();

    void setValueTextColor(int var1);

    void setValueTextColors(List<Integer> var1);

    void setValueTypeface(Typeface var1);

    void setValueTextSize(float var1);

    int getValueTextColor();

    int getValueTextColor(int var1);

    Typeface getValueTypeface();

    float getValueTextSize();

    Legend.LegendForm getForm();

    float getFormSize();

    float getFormLineWidth();

    DashPathEffect getFormLineDashEffect();

    void setDrawValues(boolean var1);

    boolean isDrawValuesEnabled();

    void setDrawIcons(boolean var1);

    boolean isDrawIconsEnabled();

    void setIconsOffset(MPPointF var1);

    MPPointF getIconsOffset();

    void setVisible(boolean var1);

    boolean isVisible();
}
