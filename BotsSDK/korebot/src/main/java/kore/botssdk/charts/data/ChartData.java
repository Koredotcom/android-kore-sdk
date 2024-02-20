package kore.botssdk.charts.data;

import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.formatter.ValueFormatter;
import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.utils.LogUtils;

public abstract class ChartData<T extends IDataSet<? extends Entry>> {
    protected float mYMax = -3.4028235E38F;
    protected float mYMin = 3.4028235E38F;
    protected float mXMax = -3.4028235E38F;
    protected float mXMin = 3.4028235E38F;
    protected float mLeftAxisMax = -3.4028235E38F;
    protected float mLeftAxisMin = 3.4028235E38F;
    protected float mRightAxisMax = -3.4028235E38F;
    protected float mRightAxisMin = 3.4028235E38F;
    protected List<T> mDataSets;

    public ChartData() {
        this.mDataSets = new ArrayList();
    }

    public ChartData(T... dataSets) {
        this.mDataSets = this.arrayToList(dataSets);
        this.notifyDataChanged();
    }

    private List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList();
        IDataSet[] var3 = array;
        int var4 = array.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            T set = (T) var3[var5];
            list.add(set);
        }

        return list;
    }

    public ChartData(List<T> sets) {
        this.mDataSets = sets;
        this.notifyDataChanged();
    }

    public void notifyDataChanged() {
        this.calcMinMax();
    }

    public void calcMinMaxY(float fromX, float toX) {
        Iterator var3 = this.mDataSets.iterator();

        while(var3.hasNext()) {
            T set = (T) var3.next();
            set.calcMinMaxY(fromX, toX);
        }

        this.calcMinMax();
    }

    protected void calcMinMax() {
        if (this.mDataSets != null) {
            this.mYMax = -3.4028235E38F;
            this.mYMin = 3.4028235E38F;
            this.mXMax = -3.4028235E38F;
            this.mXMin = 3.4028235E38F;
            Iterator var1 = this.mDataSets.iterator();

            IDataSet firstRight;
            while(var1.hasNext()) {
                firstRight = (IDataSet)var1.next();
                this.calcMinMax((T) firstRight);
            }

            this.mLeftAxisMax = -3.4028235E38F;
            this.mLeftAxisMin = 3.4028235E38F;
            this.mRightAxisMax = -3.4028235E38F;
            this.mRightAxisMin = 3.4028235E38F;
            T firstLeft = this.getFirstLeft(this.mDataSets);
            if (firstLeft != null) {
                this.mLeftAxisMax = firstLeft.getYMax();
                this.mLeftAxisMin = firstLeft.getYMin();
                Iterator var6 = this.mDataSets.iterator();

                while(var6.hasNext()) {
                    T dataSet = (T) var6.next();
                    if (dataSet.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                        if (dataSet.getYMin() < this.mLeftAxisMin) {
                            this.mLeftAxisMin = dataSet.getYMin();
                        }

                        if (dataSet.getYMax() > this.mLeftAxisMax) {
                            this.mLeftAxisMax = dataSet.getYMax();
                        }
                    }
                }
            }

            firstRight = this.getFirstRight(this.mDataSets);
            if (firstRight != null) {
                this.mRightAxisMax = firstRight.getYMax();
                this.mRightAxisMin = firstRight.getYMin();
                Iterator var7 = this.mDataSets.iterator();

                while(var7.hasNext()) {
                    T dataSet = (T) var7.next();
                    if (dataSet.getAxisDependency() == YAxis.AxisDependency.RIGHT) {
                        if (dataSet.getYMin() < this.mRightAxisMin) {
                            this.mRightAxisMin = dataSet.getYMin();
                        }

                        if (dataSet.getYMax() > this.mRightAxisMax) {
                            this.mRightAxisMax = dataSet.getYMax();
                        }
                    }
                }
            }

        }
    }

    public int getDataSetCount() {
        return this.mDataSets == null ? 0 : this.mDataSets.size();
    }

    public float getYMin() {
        return this.mYMin;
    }

    public float getYMin(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisMin == 3.4028235E38F ? this.mRightAxisMin : this.mLeftAxisMin;
        } else {
            return this.mRightAxisMin == 3.4028235E38F ? this.mLeftAxisMin : this.mRightAxisMin;
        }
    }

    public float getYMax() {
        return this.mYMax;
    }

    public float getYMax(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisMax == -3.4028235E38F ? this.mRightAxisMax : this.mLeftAxisMax;
        } else {
            return this.mRightAxisMax == -3.4028235E38F ? this.mLeftAxisMax : this.mRightAxisMax;
        }
    }

    public float getXMin() {
        return this.mXMin;
    }

    public float getXMax() {
        return this.mXMax;
    }

    public List<T> getDataSets() {
        return this.mDataSets;
    }

    protected int getDataSetIndexByLabel(List<T> dataSets, String label, boolean ignorecase) {
        int i;
        if (ignorecase) {
            for(i = 0; i < dataSets.size(); ++i) {
                if (label.equalsIgnoreCase(dataSets.get(i).getLabel())) {
                    return i;
                }
            }
        } else {
            for(i = 0; i < dataSets.size(); ++i) {
                if (label.equals(dataSets.get(i).getLabel())) {
                    return i;
                }
            }
        }

        return -1;
    }

    public String[] getDataSetLabels() {
        String[] types = new String[this.mDataSets.size()];

        for(int i = 0; i < this.mDataSets.size(); ++i) {
            types[i] = this.mDataSets.get(i).getLabel();
        }

        return types;
    }

    public Entry getEntryForHighlight(Highlight highlight) {
        return highlight.getDataSetIndex() >= this.mDataSets.size() ? null : ((IDataSet)this.mDataSets.get(highlight.getDataSetIndex())).getEntryForXValue(highlight.getX(), highlight.getY());
    }

    public T getDataSetByLabel(String label, boolean ignorecase) {
        int index = this.getDataSetIndexByLabel(this.mDataSets, label, ignorecase);
        return index >= 0 && index < this.mDataSets.size() ? this.mDataSets.get(index) : null;
    }

    public T getDataSetByIndex(int index) {
        return this.mDataSets != null && index >= 0 && index < this.mDataSets.size() ? this.mDataSets.get(index) : null;
    }

    public void addDataSet(T d) {
        if (d != null) {
            this.calcMinMax(d);
            this.mDataSets.add(d);
        }
    }

    public boolean removeDataSet(T d) {
        if (d == null) {
            return false;
        } else {
            boolean removed = this.mDataSets.remove(d);
            if (removed) {
                this.calcMinMax();
            }

            return removed;
        }
    }

    public boolean removeDataSet(int index) {
        if (index < this.mDataSets.size() && index >= 0) {
            T set = this.mDataSets.get(index);
            return this.removeDataSet(set);
        } else {
            return false;
        }
    }

    public void addEntry(Entry e, int dataSetIndex) {
        if (this.mDataSets.size() > dataSetIndex && dataSetIndex >= 0) {
            IDataSet set = this.mDataSets.get(dataSetIndex);
            if (!set.addEntry(e)) {
                return;
            }

            this.calcMinMax(e,set.getAxisDependency());
        } else {
            LogUtils.e("addEntry", "Cannot add Entry because dataSetIndex too high or too low.");
        }

    }

    protected void calcMinMax(Entry e, YAxis.AxisDependency axis) {
        if (this.mYMax < e.getY()) {
            this.mYMax = e.getY();
        }

        if (this.mYMin > e.getY()) {
            this.mYMin = e.getY();
        }

        if (this.mXMax < e.getX()) {
            this.mXMax = e.getX();
        }

        if (this.mXMin > e.getX()) {
            this.mXMin = e.getX();
        }

        if (axis == YAxis.AxisDependency.LEFT) {
            if (this.mLeftAxisMax < e.getY()) {
                this.mLeftAxisMax = e.getY();
            }

            if (this.mLeftAxisMin > e.getY()) {
                this.mLeftAxisMin = e.getY();
            }
        } else {
            if (this.mRightAxisMax < e.getY()) {
                this.mRightAxisMax = e.getY();
            }

            if (this.mRightAxisMin > e.getY()) {
                this.mRightAxisMin = e.getY();
            }
        }

    }

    protected void calcMinMax(T d) {
        if (this.mYMax < d.getYMax()) {
            this.mYMax = d.getYMax();
        }

        if (this.mYMin > d.getYMin()) {
            this.mYMin = d.getYMin();
        }

        if (this.mXMax < d.getXMax()) {
            this.mXMax = d.getXMax();
        }

        if (this.mXMin > d.getXMin()) {
            this.mXMin = d.getXMin();
        }

        if (d.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            if (this.mLeftAxisMax < d.getYMax()) {
                this.mLeftAxisMax = d.getYMax();
            }

            if (this.mLeftAxisMin > d.getYMin()) {
                this.mLeftAxisMin = d.getYMin();
            }
        } else {
            if (this.mRightAxisMax < d.getYMax()) {
                this.mRightAxisMax = d.getYMax();
            }

            if (this.mRightAxisMin > d.getYMin()) {
                this.mRightAxisMin = d.getYMin();
            }
        }

    }

    public boolean removeEntry(Entry e, int dataSetIndex) {
        if (e != null && dataSetIndex < this.mDataSets.size()) {
            IDataSet set = this.mDataSets.get(dataSetIndex);
            if (set != null) {
                boolean removed = set.removeEntry(e);
                if (removed) {
                    this.calcMinMax();
                }

                return removed;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean removeEntry(float xValue, int dataSetIndex) {
        if (dataSetIndex >= this.mDataSets.size()) {
            return false;
        } else {
            IDataSet dataSet = this.mDataSets.get(dataSetIndex);
            Entry e = dataSet.getEntryForXValue(xValue, 0.0F / 0.0F);
            return e != null && this.removeEntry(e, dataSetIndex);
        }
    }

    public T getDataSetForEntry(Entry e) {
        if (e == null) {
            return null;
        } else {
            for(int i = 0; i < this.mDataSets.size(); ++i) {
                T set = this.mDataSets.get(i);

                for(int j = 0; j < set.getEntryCount(); ++j) {
                    if (e.equalTo(set.getEntryForXValue(e.getX(), e.getY()))) {
                        return set;
                    }
                }
            }

            return null;
        }
    }

    public int[] getColors() {
        if (this.mDataSets == null) {
            return null;
        } else {
            int clrcnt = 0;

            for(int i = 0; i < this.mDataSets.size(); ++i) {
                clrcnt += ((IDataSet)this.mDataSets.get(i)).getColors().size();
            }

            int[] colors = new int[clrcnt];
            int cnt = 0;

            for(int i = 0; i < this.mDataSets.size(); ++i) {
                List<Integer> clrs = ((IDataSet)this.mDataSets.get(i)).getColors();

                for(Iterator var6 = clrs.iterator(); var6.hasNext(); ++cnt) {
                    Integer clr = (Integer)var6.next();
                    colors[cnt] = clr;
                }
            }

            return colors;
        }
    }

    public int getIndexOfDataSet(T dataSet) {
        return this.mDataSets.indexOf(dataSet);
    }

    protected T getFirstLeft(List<T> sets) {
        Iterator var2 = sets.iterator();

        IDataSet dataSet;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            dataSet = (IDataSet)var2.next();
        } while(dataSet.getAxisDependency() != YAxis.AxisDependency.LEFT);

        return (T) dataSet;
    }

    public T getFirstRight(List<T> sets) {
        Iterator var2 = sets.iterator();

        IDataSet dataSet;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            dataSet = (IDataSet)var2.next();
        } while(dataSet.getAxisDependency() != YAxis.AxisDependency.RIGHT);

        return (T) dataSet;
    }

    public void setValueFormatter(ValueFormatter f) {
        if (f != null) {
            Iterator var2 = this.mDataSets.iterator();

            while(var2.hasNext()) {
                IDataSet set = (IDataSet)var2.next();
                set.setValueFormatter(f);
            }

        }
    }

    public void setValueTextColor(int color) {
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IDataSet set = (IDataSet)var2.next();
            set.setValueTextColor(color);
        }

    }

    public void setValueTextColors(List<Integer> colors) {
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IDataSet set = (IDataSet)var2.next();
            set.setValueTextColors(colors);
        }

    }

    public void setValueTypeface(Typeface tf) {
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IDataSet set = (IDataSet)var2.next();
            set.setValueTypeface(tf);
        }

    }

    public void setValueTextSize(float size) {
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IDataSet set = (IDataSet)var2.next();
            set.setValueTextSize(size);
        }

    }

    public void setDrawValues(boolean enabled) {
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IDataSet set = (IDataSet)var2.next();
            set.setDrawValues(enabled);
        }

    }

    public boolean isHighlightEnabled() {
        Iterator<T> var1 = this.mDataSets.iterator();

        IDataSet set;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            set = var1.next();
        } while(set.isHighlightEnabled());

        return false;
    }

    public void clearValues() {
        if (this.mDataSets != null) {
            this.mDataSets.clear();
        }

        this.notifyDataChanged();
    }

    public boolean contains(T dataSet) {
        Iterator var2 = this.mDataSets.iterator();

        IDataSet set;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            set = (IDataSet)var2.next();
        } while(!Objects.equals(set, dataSet));

        return true;
    }

    public int getEntryCount() {
        int count = 0;

        IDataSet set;
        for(Iterator var2 = this.mDataSets.iterator(); var2.hasNext(); count += set.getEntryCount()) {
            set = (IDataSet)var2.next();
        }

        return count;
    }

    public T getMaxEntryCountSet() {
        if (this.mDataSets != null && !this.mDataSets.isEmpty()) {
            T max = this.mDataSets.get(0);
            Iterator var2 = this.mDataSets.iterator();

            while(var2.hasNext()) {
                T set = (T) var2.next();
                if (set.getEntryCount() > max.getEntryCount()) {
                    max = set;
                }
            }

            return max;
        } else {
            return null;
        }
    }
}
