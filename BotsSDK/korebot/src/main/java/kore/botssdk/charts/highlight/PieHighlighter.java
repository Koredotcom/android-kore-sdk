package kore.botssdk.charts.highlight;

import kore.botssdk.charts.charts.PieChart;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.PieData;
import kore.botssdk.charts.interfaces.datasets.IPieDataSet;

public class PieHighlighter extends PieRadarHighlighter<PieChart> {
    public PieHighlighter(PieChart chart) {
        super(chart);
    }

    protected kore.botssdk.charts.highlight.Highlight getClosestHighlight(int index, float x, float y) {
        IPieDataSet set = this.mChart.getData().getDataSet();
        Entry entry = set.getEntryForIndex(index);
        return new Highlight((float)index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}
