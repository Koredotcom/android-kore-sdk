package kore.botssdk.charts.highlight;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.charts.charts.PieChart;
import kore.botssdk.charts.charts.PieRadarChartBase;

public abstract class PieRadarHighlighter<T extends PieRadarChartBase> implements IHighlighter {
    protected final T mChart;
    protected final List<kore.botssdk.charts.highlight.Highlight> mHighlightBuffer = new ArrayList();

    public PieRadarHighlighter(T chart) {
        this.mChart = chart;
    }

    public kore.botssdk.charts.highlight.Highlight getHighlight(float x, float y) {
        float touchDistanceToCenter = this.mChart.distanceToCenter(x, y);
        if (touchDistanceToCenter > this.mChart.getRadius()) {
            return null;
        } else {
            float angle = this.mChart.getAngleForPoint(x, y);
            if (this.mChart instanceof PieChart) {
                angle /= this.mChart.getAnimator().getPhaseY();
            }

            int index = this.mChart.getIndexForAngle(angle);
            return index >= 0 && index < this.mChart.getData().getMaxEntryCountSet().getEntryCount() ? this.getClosestHighlight(index, x, y) : null;
        }
    }

    protected abstract Highlight getClosestHighlight(int var1, float var2, float var3);
}
