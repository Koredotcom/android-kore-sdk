package com.kore.ai.widgetsdk.charts.highlight;

import com.kore.ai.widgetsdk.charts.data.BarData;
import com.kore.ai.widgetsdk.charts.data.DataSet;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.BarDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IBarDataSet;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;
import com.kore.ai.widgetsdk.charts.utils.MPPointD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HorizontalBarHighlighter extends BarHighlighter {
    public HorizontalBarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    public com.kore.ai.widgetsdk.charts.highlight.Highlight getHighlight(float x, float y) {
        BarData barData = this.mChart.getBarData();
        MPPointD pos = this.getValsForTouch(y, x);
        com.kore.ai.widgetsdk.charts.highlight.Highlight high = this.getHighlightForX((float)pos.y, y, x);
        if (high == null) {
            return null;
        } else {
            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
            if (set.isStacked()) {
                return this.getStackedHighlight(high, set, (float)pos.y, (float)pos.x);
            } else {
                MPPointD.recycleInstance(pos);
                return high;
            }
        }
    }

    protected List<com.kore.ai.widgetsdk.charts.highlight.Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {
        ArrayList<com.kore.ai.widgetsdk.charts.highlight.Highlight> highlights = new ArrayList();
        List<Entry> entries = set.getEntriesForXValue(xVal);
        if (entries.size() == 0) {
            Entry closest = set.getEntryForXValue(xVal, 0.0F / 0.0F, rounding);
            if (closest != null) {
                entries = set.getEntriesForXValue(closest.getX());
            }
        }

        if (entries.size() == 0) {
            return highlights;
        } else {
            Iterator var10 = entries.iterator();

            while(var10.hasNext()) {
                Entry e = (Entry)var10.next();
                MPPointD pixels = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getY(), e.getX());
                highlights.add(new Highlight(e.getX(), e.getY(), (float)pixels.x, (float)pixels.y, dataSetIndex, set.getAxisDependency()));
            }

            return highlights;
        }
    }

    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(y1 - y2);
    }
}
