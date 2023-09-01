package com.kore.ai.widgetsdk.charts.highlight;

import com.kore.ai.widgetsdk.charts.charts.PieChart;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.data.PieData;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IPieDataSet;

public class PieHighlighter extends PieRadarHighlighter<PieChart> {
    public PieHighlighter(PieChart chart) {
        super(chart);
    }

    protected com.kore.ai.widgetsdk.charts.highlight.Highlight getClosestHighlight(int index, float x, float y) {
        IPieDataSet set = this.mChart.getData().getDataSet();
        Entry entry = set.getEntryForIndex(index);
        return new Highlight((float)index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}
