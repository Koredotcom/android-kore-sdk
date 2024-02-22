package kore.botssdk.charts.highlight;

import android.annotation.SuppressLint;

import kore.botssdk.charts.data.BarData;
import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.data.BarLineScatterCandleBubbleData;
import kore.botssdk.charts.interfaces.dataprovider.BarDataProvider;
import kore.botssdk.charts.interfaces.datasets.IBarDataSet;
import kore.botssdk.charts.utils.MPPointD;
@SuppressLint("UnknownNullness")
public class BarHighlighter extends ChartHighlighter<BarDataProvider> {
    public BarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    public Highlight getHighlight(float x, float y) {
        Highlight high = super.getHighlight(x, y);
        if (high == null) {
            return null;
        } else {
            MPPointD pos = this.getValsForTouch(x, y);
            BarData barData = this.mChart.getBarData();
            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
            if (set.isStacked()) {
                return this.getStackedHighlight(high, set, (float)pos.x, (float)pos.y);
            } else {
                MPPointD.recycleInstance(pos);
                return high;
            }
        }
    }

    public Highlight getStackedHighlight(Highlight high, IBarDataSet set, float xVal, float yVal) {
        BarEntry entry = set.getEntryForXValue(xVal, yVal);
        if (entry == null) {
            return null;
        } else {
            entry.getYVals();
            Range[] ranges = entry.getmRanges();
            if (ranges.length > 0) {
                int stackIndex = this.getClosestStackIndex(ranges, yVal);
                MPPointD pixels = this.mChart.getTransformer(set.getAxisDependency()).getPixelForValues(high.getX(), ranges[stackIndex].to);
                Highlight stackedHigh = new Highlight(entry.getX(), entry.getY(), (float)pixels.x, (float)pixels.y, high.getDataSetIndex(), stackIndex, high.getAxis());
                MPPointD.recycleInstance(pixels);
                return stackedHigh;
            } else {
                return null;
            }
        }
    }

    protected int getClosestStackIndex(Range[] ranges, float value) {
        if (ranges != null && ranges.length != 0) {
            int stackIndex = 0;

            for (Range range : ranges) {
                if (range.contains(value)) {
                    return stackIndex;
                }

                ++stackIndex;
            }

            int length = Math.max(ranges.length - 1, 0);
            return value > ranges[length].to ? length : 0;
        } else {
            return 0;
        }
    }

    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(x1 - x2);
    }

    protected BarLineScatterCandleBubbleData getData() {
        return this.mChart.getBarData();
    }
}
