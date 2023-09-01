package kore.botssdk.charts.highlight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.BarLineScatterCandleBubbleData;
import kore.botssdk.charts.data.DataSet;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.utils.MPPointD;

public class ChartHighlighter<T extends BarLineScatterCandleBubbleDataProvider> implements IHighlighter {
    protected final T mChart;
    protected final List<Highlight> mHighlightBuffer = new ArrayList();

    public ChartHighlighter(T chart) {
        this.mChart = chart;
    }

    public Highlight getHighlight(float x, float y) {
        MPPointD pos = this.getValsForTouch(x, y);
        float xVal = (float)pos.x;
        MPPointD.recycleInstance(pos);
        Highlight high = this.getHighlightForX(xVal, x, y);
        return high;
    }

    protected MPPointD getValsForTouch(float x, float y) {
        MPPointD pos = this.mChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(x, y);
        return pos;
    }

    protected Highlight getHighlightForX(float xVal, float x, float y) {
        List<Highlight> closestValues = this.getHighlightsAtXValue(xVal, x, y);
        if (closestValues.isEmpty()) {
            return null;
        } else {
            float leftAxisMinDist = this.getMinimumDistance(closestValues, y, YAxis.AxisDependency.LEFT);
            float rightAxisMinDist = this.getMinimumDistance(closestValues, y, YAxis.AxisDependency.RIGHT);
            YAxis.AxisDependency axis = leftAxisMinDist < rightAxisMinDist ? YAxis.AxisDependency.LEFT : YAxis.AxisDependency.RIGHT;
            Highlight detail = this.getClosestHighlightByPixel(closestValues, x, y, axis, this.mChart.getMaxHighlightDistance());
            return detail;
        }
    }

    protected float getMinimumDistance(List<Highlight> closestValues, float pos, YAxis.AxisDependency axis) {
        float distance = 3.4028235E38F;

        for(int i = 0; i < closestValues.size(); ++i) {
            Highlight high = closestValues.get(i);
            if (high.getAxis() == axis) {
                float tempDistance = Math.abs(this.getHighlightPos(high) - pos);
                if (tempDistance < distance) {
                    distance = tempDistance;
                }
            }
        }

        return distance;
    }

    protected float getHighlightPos(Highlight h) {
        return h.getYPx();
    }

    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {
        this.mHighlightBuffer.clear();
        BarLineScatterCandleBubbleData data = this.getData();
        if (data == null) {
            return this.mHighlightBuffer;
        } else {
            int i = 0;

            for(int dataSetCount = data.getDataSetCount(); i < dataSetCount; ++i) {
                IDataSet dataSet = data.getDataSetByIndex(i);
                if (dataSet.isHighlightEnabled()) {
                    this.mHighlightBuffer.addAll(this.buildHighlights(dataSet, i, xVal, DataSet.Rounding.CLOSEST));
                }
            }

            return this.mHighlightBuffer;
        }
    }

    protected List<Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {
        ArrayList<Highlight> highlights = new ArrayList();
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
                MPPointD pixels = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY());
                highlights.add(new Highlight(e.getX(), e.getY(), (float)pixels.x, (float)pixels.y, dataSetIndex, set.getAxisDependency()));
            }

            return highlights;
        }
    }

    public Highlight getClosestHighlightByPixel(List<Highlight> closestValues, float x, float y, YAxis.AxisDependency axis, float minSelectionDistance) {
        Highlight closest = null;
        float distance = minSelectionDistance;

        for(int i = 0; i < closestValues.size(); ++i) {
            Highlight high = closestValues.get(i);
            if (axis == null || high.getAxis() == axis) {
                float cDistance = this.getDistance(x, y, high.getXPx(), high.getYPx());
                if (cDistance < distance) {
                    closest = high;
                    distance = cDistance;
                }
            }
        }

        return closest;
    }

    protected float getDistance(float x1, float y1, float x2, float y2) {
        return (float)Math.hypot(x1 - x2, y1 - y2);
    }

    protected BarLineScatterCandleBubbleData getData() {
        return this.mChart.getData();
    }
}
