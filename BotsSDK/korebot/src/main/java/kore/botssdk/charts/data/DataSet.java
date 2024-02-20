package kore.botssdk.charts.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DataSet<T extends Entry> extends BaseDataSet<T> {
    protected List<T> mValues = null;
    protected float mYMax = -3.4028235E38F;
    protected float mYMin = 3.4028235E38F;
    protected float mXMax = -3.4028235E38F;
    protected float mXMin = 3.4028235E38F;

    public DataSet(List<T> values, String label) {
        super(label);
        this.mValues = values;
        if (this.mValues == null) {
            this.mValues = new ArrayList();
        }

        this.calcMinMax();
    }

    public void calcMinMax() {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            this.mYMax = -3.4028235E38F;
            this.mYMin = 3.4028235E38F;
            this.mXMax = -3.4028235E38F;
            this.mXMin = 3.4028235E38F;
            Iterator var1 = this.mValues.iterator();

            while(var1.hasNext()) {
                T e = (T) var1.next();
                this.calcMinMax(e);
            }

        }
    }

    public void calcMinMaxY(float fromX, float toX) {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            this.mYMax = -3.4028235E38F;
            this.mYMin = 3.4028235E38F;
            int indexFrom = this.getEntryIndex(fromX, 0.0F / 0.0F, DataSet.Rounding.DOWN);
            int indexTo = this.getEntryIndex(toX, 0.0F / 0.0F, DataSet.Rounding.UP);

            for(int i = indexFrom; i <= indexTo; ++i) {
                this.calcMinMaxY(this.mValues.get(i));
            }

        }
    }

    protected void calcMinMax(T e) {
        if (e != null) {
            this.calcMinMaxX(e);
            this.calcMinMaxY(e);
        }
    }

    protected void calcMinMaxX(T e) {
        if (e.getX() < this.mXMin) {
            this.mXMin = e.getX();
        }

        if (e.getX() > this.mXMax) {
            this.mXMax = e.getX();
        }

    }

    protected void calcMinMaxY(T e) {
        if (e.getY() < this.mYMin) {
            this.mYMin = e.getY();
        }

        if (e.getY() > this.mYMax) {
            this.mYMax = e.getY();
        }

    }

    public int getEntryCount() {
        return this.mValues.size();
    }

    public List<T> getValues() {
        return this.mValues;
    }

    public void setValues(List<T> values) {
        this.mValues = values;
        this.notifyDataSetChanged();
    }

    public abstract kore.botssdk.charts.data.DataSet<T> copy();

    protected void copy(kore.botssdk.charts.data.DataSet dataSet) {
        super.copy(dataSet);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.toSimpleString());

        for(int i = 0; i < this.mValues.size(); ++i) {
            buffer.append(this.mValues.get(i).toString() + " ");
        }

        return buffer.toString();
    }

    public String toSimpleString() {
        return "DataSet, label: " + (this.getLabel() == null ? "" : this.getLabel()) + ", entries: " + this.mValues.size() + "\n";
    }

    public float getYMin() {
        return this.mYMin;
    }

    public float getYMax() {
        return this.mYMax;
    }

    public float getXMin() {
        return this.mXMin;
    }

    public float getXMax() {
        return this.mXMax;
    }

    public void addEntryOrdered(T e) {
        if (e != null) {
            if (this.mValues == null) {
                this.mValues = new ArrayList();
            }

            this.calcMinMax(e);
            if (this.mValues.size() > 0 && this.mValues.get(this.mValues.size() - 1).getX() > e.getX()) {
                int closestIndex = this.getEntryIndex(e.getX(), e.getY(), kore.botssdk.charts.data.DataSet.Rounding.UP);
                this.mValues.add(closestIndex, e);
            } else {
                this.mValues.add(e);
            }

        }
    }

    public void clear() {
        this.mValues.clear();
        this.notifyDataSetChanged();
    }

    public boolean addEntry(T e) {
        if (e == null) {
            return false;
        } else {
            List<T> values = this.getValues();
            if (values == null) {
                values = new ArrayList();
            }

            this.calcMinMax(e);
            return values.add(e);
        }
    }

    public boolean removeEntry(T e) {
        if (e == null) {
            return false;
        } else if (this.mValues == null) {
            return false;
        } else {
            boolean removed = this.mValues.remove(e);
            if (removed) {
                this.calcMinMax();
            }

            return removed;
        }
    }

    public int getEntryIndex(Entry e) {
        return this.mValues.indexOf(e);
    }

    public T getEntryForXValue(float xValue, float closestToY, kore.botssdk.charts.data.DataSet.Rounding rounding) {
        int index = this.getEntryIndex(xValue, closestToY, rounding);
        return index > -1 ? this.mValues.get(index) : null;
    }

    public T getEntryForXValue(float xValue, float closestToY) {
        return this.getEntryForXValue(xValue, closestToY, kore.botssdk.charts.data.DataSet.Rounding.CLOSEST);
    }

    public T getEntryForIndex(int index) {
        return this.mValues.get(index);
    }

    public int getEntryIndex(float xValue, float closestToY, kore.botssdk.charts.data.DataSet.Rounding rounding) {
        if (this.mValues != null && !this.mValues.isEmpty()) {
            int low = 0;
            int high = this.mValues.size() - 1;

            int closest;
            float closestYValue;
            for(closest = high; low < high; closest = high) {
                int m = (low + high) / 2;
                closestYValue = this.mValues.get(m).getX() - xValue;
                float d2 = this.mValues.get(m + 1).getX() - xValue;
                float ad1 = Math.abs(closestYValue);
                float ad2 = Math.abs(d2);
                if (ad2 < ad1) {
                    low = m + 1;
                } else if (ad1 < ad2) {
                    high = m;
                } else if ((double)closestYValue >= 0.0D) {
                    high = m;
                } else if ((double)closestYValue < 0.0D) {
                    low = m + 1;
                }
            }

            if (closest != -1) {
                float closestXValue = this.mValues.get(closest).getX();
                if (rounding == kore.botssdk.charts.data.DataSet.Rounding.UP) {
                    if (closestXValue < xValue && closest < this.mValues.size() - 1) {
                        ++closest;
                    }
                } else if (rounding == kore.botssdk.charts.data.DataSet.Rounding.DOWN && closestXValue > xValue && closest > 0) {
                    --closest;
                }

                if (!Float.isNaN(closestToY)) {
                    while(closest > 0 && this.mValues.get(closest - 1).getX() == closestXValue) {
                        --closest;
                    }

                    closestYValue = this.mValues.get(closest).getY();
                    int closestYIndex = closest;

                    while(true) {
                        ++closest;
                        if (closest >= this.mValues.size()) {
                            break;
                        }

                        Entry value = this.mValues.get(closest);
                        if (value.getX() != closestXValue) {
                            break;
                        }

                        if (Math.abs(value.getY() - closestToY) < Math.abs(closestYValue - closestToY)) {
                            closestYValue = closestToY;
                            closestYIndex = closest;
                        }
                    }

                    closest = closestYIndex;
                }
            }

            return closest;
        } else {
            return -1;
        }
    }

    public List<T> getEntriesForXValue(float xValue) {
        List<T> entries = new ArrayList();
        int low = 0;
        int high = this.mValues.size() - 1;

        while(low <= high) {
            int m = (high + low) / 2;
            T entry = this.mValues.get(m);
            if (xValue == entry.getX()) {
                while(m > 0 && this.mValues.get(m - 1).getX() == xValue) {
                    --m;
                }

                for(high = this.mValues.size(); m < high; ++m) {
                    entry = this.mValues.get(m);
                    if (entry.getX() != xValue) {
                        return entries;
                    }

                    entries.add(entry);
                }

                return entries;
            }

            if (xValue > entry.getX()) {
                low = m + 1;
            } else {
                high = m - 1;
            }
        }

        return entries;
    }

    public enum Rounding {
        UP,
        DOWN,
        CLOSEST;

        Rounding() {
        }
    }
}
