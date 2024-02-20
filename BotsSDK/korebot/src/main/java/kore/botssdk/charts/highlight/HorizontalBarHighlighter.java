package kore.botssdk.charts.highlight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.DataSet;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.interfaces.dataprovider.BarDataProvider;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.utils.MPPointD;

public class HorizontalBarHighlighter extends BarHighlighter {
    public HorizontalBarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    public kore.botssdk.charts.highlight.Highlight getHighlight(float x, float y) {
        BarData barData = this.mChart.getBarData();
        MPPointD pos = this.getValsForTouch(y, x);
        kore.botssdk.charts.highlight.Highlight high = this.getHighlightForX((float)pos.y, y, x);
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

    protected List<kore.botssdk.charts.highlight.Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {
        ArrayList<kore.botssdk.charts.highlight.Highlight> highlights = new ArrayList<>();
        List<Entry> entries = set.getEntriesForXValue(xVal);
        if (entries.size() == 0) {
            Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);
            if (closest != null) {
                entries = set.getEntriesForXValue(closest.getX());
            }
        }

        if (entries.size() == 0) {
            return highlights;
        } else {

            for (Entry e : entries) {
                MPPointD pixels = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getY(), e.getX());
                highlights.add(new Highlight(e.getX(), e.getY(), (float) pixels.x, (float) pixels.y, dataSetIndex, set.getAxisDependency()));
            }

            return highlights;
        }
    }

    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(y1 - y2);
    }
}
