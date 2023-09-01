package kore.botssdk.charts.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import kore.botssdk.utils.LogUtils;

public class CombinedData extends BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<? extends Entry>> {
    private LineData mLineData;
    private kore.botssdk.charts.data.BarData mBarData;
    private ScatterData mScatterData;
    private kore.botssdk.charts.data.CandleData mCandleData;
    private kore.botssdk.charts.data.BubbleData mBubbleData;

    public CombinedData() {
    }

    public void setData(LineData data) {
        this.mLineData = data;
        this.notifyDataChanged();
    }

    public void setData(kore.botssdk.charts.data.BarData data) {
        this.mBarData = data;
        this.notifyDataChanged();
    }

    public void setData(ScatterData data) {
        this.mScatterData = data;
        this.notifyDataChanged();
    }

    public void setData(kore.botssdk.charts.data.CandleData data) {
        this.mCandleData = data;
        this.notifyDataChanged();
    }

    public void setData(kore.botssdk.charts.data.BubbleData data) {
        this.mBubbleData = data;
        this.notifyDataChanged();
    }

    public void calcMinMax() {
        if (this.mDataSets == null) {
            this.mDataSets = new ArrayList();
        }

        this.mDataSets.clear();
        this.mYMax = -3.4028235E38F;
        this.mYMin = 3.4028235E38F;
        this.mXMax = -3.4028235E38F;
        this.mXMin = 3.4028235E38F;
        this.mLeftAxisMax = -3.4028235E38F;
        this.mLeftAxisMin = 3.4028235E38F;
        this.mRightAxisMax = -3.4028235E38F;
        this.mRightAxisMin = 3.4028235E38F;
        List<BarLineScatterCandleBubbleData> allData = this.getAllData();
        Iterator var2 = allData.iterator();

        while(var2.hasNext()) {
            ChartData data = (ChartData)var2.next();
            data.calcMinMax();
            List<IBarLineScatterCandleBubbleDataSet<? extends Entry>> sets = data.getDataSets();
            this.mDataSets.addAll(sets);
            if (data.getYMax() > this.mYMax) {
                this.mYMax = data.getYMax();
            }

            if (data.getYMin() < this.mYMin) {
                this.mYMin = data.getYMin();
            }

            if (data.getXMax() > this.mXMax) {
                this.mXMax = data.getXMax();
            }

            if (data.getXMin() < this.mXMin) {
                this.mXMin = data.getXMin();
            }

            if (data.mLeftAxisMax > this.mLeftAxisMax) {
                this.mLeftAxisMax = data.mLeftAxisMax;
            }

            if (data.mLeftAxisMin < this.mLeftAxisMin) {
                this.mLeftAxisMin = data.mLeftAxisMin;
            }

            if (data.mRightAxisMax > this.mRightAxisMax) {
                this.mRightAxisMax = data.mRightAxisMax;
            }

            if (data.mRightAxisMin < this.mRightAxisMin) {
                this.mRightAxisMin = data.mRightAxisMin;
            }
        }

    }

    public BubbleData getBubbleData() {
        return this.mBubbleData;
    }

    public LineData getLineData() {
        return this.mLineData;
    }

    public BarData getBarData() {
        return this.mBarData;
    }

    public ScatterData getScatterData() {
        return this.mScatterData;
    }

    public CandleData getCandleData() {
        return this.mCandleData;
    }

    public List<BarLineScatterCandleBubbleData> getAllData() {
        List<BarLineScatterCandleBubbleData> data = new ArrayList();
        if (this.mLineData != null) {
            data.add(this.mLineData);
        }

        if (this.mBarData != null) {
            data.add(this.mBarData);
        }

        if (this.mScatterData != null) {
            data.add(this.mScatterData);
        }

        if (this.mCandleData != null) {
            data.add(this.mCandleData);
        }

        if (this.mBubbleData != null) {
            data.add(this.mBubbleData);
        }

        return data;
    }

    public BarLineScatterCandleBubbleData getDataByIndex(int index) {
        return this.getAllData().get(index);
    }

    public void notifyDataChanged() {
        if (this.mLineData != null) {
            this.mLineData.notifyDataChanged();
        }

        if (this.mBarData != null) {
            this.mBarData.notifyDataChanged();
        }

        if (this.mCandleData != null) {
            this.mCandleData.notifyDataChanged();
        }

        if (this.mScatterData != null) {
            this.mScatterData.notifyDataChanged();
        }

        if (this.mBubbleData != null) {
            this.mBubbleData.notifyDataChanged();
        }

        this.calcMinMax();
    }

    public Entry getEntryForHighlight(Highlight highlight) {
        if (highlight.getDataIndex() >= this.getAllData().size()) {
            return null;
        } else {
            ChartData data = this.getDataByIndex(highlight.getDataIndex());
            if (highlight.getDataSetIndex() >= data.getDataSetCount()) {
                return null;
            } else {
                List<Entry> entries = data.getDataSetByIndex(highlight.getDataSetIndex()).getEntriesForXValue(highlight.getX());
                Iterator var4 = entries.iterator();

                Entry entry;
                do {
                    if (!var4.hasNext()) {
                        return null;
                    }

                    entry = (Entry)var4.next();
                } while(entry.getY() != highlight.getY() && !Float.isNaN(highlight.getY()));

                return entry;
            }
        }
    }

    public IBarLineScatterCandleBubbleDataSet<? extends Entry> getDataSetByHighlight(Highlight highlight) {
        if (highlight.getDataIndex() >= this.getAllData().size()) {
            return null;
        } else {
            BarLineScatterCandleBubbleData data = this.getDataByIndex(highlight.getDataIndex());
            return highlight.getDataSetIndex() >= data.getDataSetCount() ? null : (IBarLineScatterCandleBubbleDataSet)data.getDataSets().get(highlight.getDataSetIndex());
        }
    }

    public int getDataIndex(ChartData data) {
        return this.getAllData().indexOf(data);
    }

    public boolean removeDataSet(IBarLineScatterCandleBubbleDataSet<? extends Entry> d) {
        List<BarLineScatterCandleBubbleData> datas = this.getAllData();
        boolean success = false;
        Iterator var4 = datas.iterator();

        while(var4.hasNext()) {
            ChartData data = (ChartData)var4.next();
            success = data.removeDataSet(d);
            if (success) {
                break;
            }
        }

        return success;
    }

    /** @deprecated */
    @Deprecated
    public boolean removeDataSet(int index) {
        LogUtils.e("MPAndroidChart", "removeDataSet(int index) not supported for CombinedData");
        return false;
    }

    /** @deprecated */
    @Deprecated
    public boolean removeEntry(Entry e, int dataSetIndex) {
        LogUtils.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
        return false;
    }

    /** @deprecated */
    @Deprecated
    public boolean removeEntry(float xValue, int dataSetIndex) {
        LogUtils.e("MPAndroidChart", "removeEntry(...) not supported for CombinedData");
        return false;
    }
}
