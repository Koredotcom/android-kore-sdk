package kore.botssdk.charts.data;

import kore.botssdk.charts.highlight.Highlight;
import kore.botssdk.charts.interfaces.datasets.IPieDataSet;

public class PieData extends ChartData<IPieDataSet> {
    public PieData() {
    }

    public PieData(IPieDataSet dataSet) {
        super(new IPieDataSet[]{dataSet});
    }

    public void setDataSet(IPieDataSet dataSet) {
        this.mDataSets.clear();
        this.mDataSets.add(dataSet);
        this.notifyDataChanged();
    }

    public IPieDataSet getDataSet() {
        return (IPieDataSet)this.mDataSets.get(0);
    }

    public IPieDataSet getDataSetByIndex(int index) {
        return index == 0 ? this.getDataSet() : null;
    }

    public IPieDataSet getDataSetByLabel(String label, boolean ignorecase) {
        return ignorecase ? (label.equalsIgnoreCase(((IPieDataSet)this.mDataSets.get(0)).getLabel()) ? (IPieDataSet)this.mDataSets.get(0) : null) : (label.equals(((IPieDataSet)this.mDataSets.get(0)).getLabel()) ? (IPieDataSet)this.mDataSets.get(0) : null);
    }

    public Entry getEntryForHighlight(Highlight highlight) {
        return this.getDataSet().getEntryForIndex((int)highlight.getX());
    }

    public float getYValueSum() {
        float sum = 0.0F;

        for(int i = 0; i < this.getDataSet().getEntryCount(); ++i) {
            sum += ((PieEntry)this.getDataSet().getEntryForIndex(i)).getY();
        }

        return sum;
    }
}

