package kore.botssdk.charts.data;

import kore.botssdk.charts.data.BarLineScatterCandleBubbleData;
import kore.botssdk.charts.interfaces.datasets.ICandleDataSet;

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
