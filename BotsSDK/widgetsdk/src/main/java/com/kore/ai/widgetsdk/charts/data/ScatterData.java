package com.kore.ai.widgetsdk.charts.data;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IScatterDataSet;

import java.util.Iterator;
import java.util.List;

public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet> {
    public ScatterData() {
    }

    public ScatterData(List<IScatterDataSet> dataSets) {
        super(dataSets);
    }

    public ScatterData(IScatterDataSet... dataSets) {
        super(dataSets);
    }

    public float getGreatestShapeSize() {
        float max = 0.0F;
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IScatterDataSet set = (IScatterDataSet)var2.next();
            float size = set.getScatterShapeSize();
            if (size > max) {
                max = size;
            }
        }

        return max;
    }
}
