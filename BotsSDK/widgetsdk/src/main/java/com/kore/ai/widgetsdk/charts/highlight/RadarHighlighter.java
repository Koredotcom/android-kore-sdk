package com.kore.ai.widgetsdk.charts.highlight;

import com.kore.ai.widgetsdk.charts.charts.RadarChart;
import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.data.RadarData;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IDataSet;
import com.kore.ai.widgetsdk.charts.utils.MPPointF;
import com.kore.ai.widgetsdk.charts.utils.Utils;

import java.util.List;

public class RadarHighlighter extends PieRadarHighlighter<RadarChart> {
    public RadarHighlighter(RadarChart chart) {
        super(chart);
    }

    protected com.kore.ai.widgetsdk.charts.highlight.Highlight getClosestHighlight(int index, float x, float y) {
        List<com.kore.ai.widgetsdk.charts.highlight.Highlight> highlights = this.getHighlightsAtIndex(index);
        float distanceToCenter = this.mChart.distanceToCenter(x, y) / this.mChart.getFactor();
        com.kore.ai.widgetsdk.charts.highlight.Highlight closest = null;
        float distance = 3.4028235E38F;

        for(int i = 0; i < highlights.size(); ++i) {
            com.kore.ai.widgetsdk.charts.highlight.Highlight high = highlights.get(i);
            float cdistance = Math.abs(high.getY() - distanceToCenter);
            if (cdistance < distance) {
                closest = high;
                distance = cdistance;
            }
        }

        return closest;
    }

    protected List<com.kore.ai.widgetsdk.charts.highlight.Highlight> getHighlightsAtIndex(int index) {
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
