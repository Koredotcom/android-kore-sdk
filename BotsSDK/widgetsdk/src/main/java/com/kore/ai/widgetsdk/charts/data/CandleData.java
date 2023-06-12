package com.kore.ai.widgetsdk.charts.data;

import com.kore.ai.widgetsdk.charts.interfaces.datasets.ICandleDataSet;

import java.util.List;

public class CandleData extends BarLineScatterCandleBubbleData<ICandleDataSet> {
    public CandleData() {
    }

    public CandleData(List<ICandleDataSet> dataSets) {
        super(dataSets);
    }

    public CandleData(ICandleDataSet... dataSets) {
        super(dataSets);
    }
}
