package com.kore.ai.widgetsdk.charts.data;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.IBubbleDataSet;

import java.util.Iterator;
import java.util.List;

public class BubbleData extends BarLineScatterCandleBubbleData<IBubbleDataSet> {
    public BubbleData() {
    }

    public BubbleData(IBubbleDataSet... dataSets) {
        super(dataSets);
    }

    public BubbleData(List<IBubbleDataSet> dataSets) {
        super(dataSets);
    }

    public void setHighlightCircleWidth(float width) {
        Iterator var2 = this.mDataSets.iterator();

        while(var2.hasNext()) {
            IBubbleDataSet set = (IBubbleDataSet)var2.next();
            set.setHighlightCircleWidth(width);
        }

    }
}

