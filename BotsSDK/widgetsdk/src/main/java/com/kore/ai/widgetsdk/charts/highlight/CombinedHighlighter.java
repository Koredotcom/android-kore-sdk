package com.kore.ai.widgetsdk.charts.highlight;

import com.kore.ai.widgetsdk.charts.data.BarData;
import com.kore.ai.widgetsdk.charts.data.BarLineScatterCandleBubbleData;
import com.kore.ai.widgetsdk.charts.data.ChartData;
import com.kore.ai.widgetsdk.charts.data.DataSet;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.BarDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.CombinedDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;

import java.util.Iterator;
import java.util.List;

public class CombinedHighlighter extends ChartHighlighter<CombinedDataProvider> implements IHighlighter {
    protected final com.kore.ai.widgetsdk.charts.highlight.BarHighlighter barHighlighter;

    public CombinedHighlighter(CombinedDataProvider chart, BarDataProvider barChart) {
        super(chart);
        this.barHighlighter = barChart.getBarData() == null ? null : new BarHighlighter(barChart);
    }

    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {
        this.mHighlightBuffer.clear();
        List<BarLineScatterCandleBubbleData> dataObjects = this.mChart.getCombinedData().getAllData();

        for(int i = 0; i < dataObjects.size(); ++i) {
            ChartData dataObject = dataObjects.get(i);
            if (this.barHighlighter != null && dataObject instanceof BarData) {
                Highlight high = this.barHighlighter.getHighlight(x, y);
                if (high != null) {
                    high.setDataIndex(i);
                    this.mHighlightBuffer.add(high);
                }
            } else {
                int j = 0;

                for(int dataSetCount = dataObject.getDataSetCount(); j < dataSetCount; ++j) {
                    IDataSet dataSet = dataObjects.get(i).getDataSetByIndex(j);
                    if (dataSet.isHighlightEnabled()) {
                        List<Highlight> highs = this.buildHighlights(dataSet, j, xVal, DataSet.Rounding.CLOSEST);
                        Iterator var11 = highs.iterator();

                        while(var11.hasNext()) {
                            Highlight high = (Highlight)var11.next();
                            high.setDataIndex(i);
                            this.mHighlightBuffer.add(high);
                        }
                    }
                }
            }
        }

        return this.mHighlightBuffer;
    }
}
