package kore.botssdk.charts.data;

import java.util.List;

import kore.botssdk.charts.interfaces.datasets.ILineDataSet;

public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet> {
    public LineData() {
    }

    public LineData(ILineDataSet... dataSets) {
        super(dataSets);
    }

    public LineData(List<ILineDataSet> dataSets) {
        super(dataSets);
    }
}
