package kore.botssdk.charts.highlight;

import java.util.List;

import kore.botssdk.charts.charts.RadarChart;
import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.data.RadarData;
import kore.botssdk.charts.interfaces.datasets.IDataSet;
import kore.botssdk.charts.utils.MPPointF;
import kore.botssdk.charts.utils.Utils;

public class RadarHighlighter extends PieRadarHighlighter<RadarChart> {
    public RadarHighlighter(RadarChart chart) {
        super(chart);
    }

    protected kore.botssdk.charts.highlight.Highlight getClosestHighlight(int index, float x, float y) {
        List<kore.botssdk.charts.highlight.Highlight> highlights = this.getHighlightsAtIndex(index);
        float distanceToCenter = this.mChart.distanceToCenter(x, y) / this.mChart.getFactor();
        kore.botssdk.charts.highlight.Highlight closest = null;
        float distance = 3.4028235E38F;

        for(int i = 0; i < highlights.size(); ++i) {
            kore.botssdk.charts.highlight.Highlight high = highlights.get(i);
            float cdistance = Math.abs(high.getY() - distanceToCenter);
            if (cdistance < distance) {
                closest = high;
                distance = cdistance;
            }
        }

        return closest;
    }

    protected List<kore.botssdk.charts.highlight.Highlight> getHighlightsAtIndex(int index) {
        this.mHighlightBuffer.clear();
        float phaseX = this.mChart.getAnimator().getPhaseX();
        float phaseY = this.mChart.getAnimator().getPhaseY();
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);

        for(int i = 0; i < this.mChart.getData().getDataSetCount(); ++i) {
            IDataSet<?> dataSet = this.mChart.getData().getDataSetByIndex(i);
            Entry entry = dataSet.getEntryForIndex(index);
            float y = entry.getY() - this.mChart.getYChartMin();
            Utils.getPosition(this.mChart.getCenterOffsets(), y * factor * phaseY, sliceangle * (float)index * phaseX + this.mChart.getRotationAngle(), pOut);
            this.mHighlightBuffer.add(new Highlight((float)index, entry.getY(), pOut.x, pOut.y, i, dataSet.getAxisDependency()));
        }

        return this.mHighlightBuffer;
    }
}
