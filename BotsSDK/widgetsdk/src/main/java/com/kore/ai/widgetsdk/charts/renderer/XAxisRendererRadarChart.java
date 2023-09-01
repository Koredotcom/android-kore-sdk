package com.kore.ai.widgetsdk.charts.renderer;

import android.graphics.Canvas;

import com.kore.ai.widgetsdk.charts.charts.RadarChart;
import com.kore.ai.widgetsdk.charts.components.XAxis;
import com.kore.ai.widgetsdk.charts.data.RadarData;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.IRadarDataSet;
import com.kore.ai.widgetsdk.charts.utils.MPPointF;
import com.kore.ai.widgetsdk.charts.utils.Transformer;
import com.kore.ai.widgetsdk.charts.utils.Utils;
import com.kore.ai.widgetsdk.charts.utils.ViewPortHandler;

public class XAxisRendererRadarChart extends XAxisRenderer {
    private final RadarChart mChart;

    public XAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);
        this.mChart = chart;
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
            MPPointF drawLabelAnchor = MPPointF.getInstance(0.5F, 0.25F);
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            float sliceangle = this.mChart.getSliceAngle();
            float factor = this.mChart.getFactor();
            MPPointF center = this.mChart.getCenterOffsets();
            MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);

            for(int i = 0; i < this.mChart.getData().getMaxEntryCountSet().getEntryCount(); ++i) {
                String label = this.mXAxis.getValueFormatter().getAxisLabel((float)i, this.mXAxis);
                float angle = (sliceangle * (float)i + this.mChart.getRotationAngle()) % 360.0F;
                Utils.getPosition(center, this.mChart.getYRange() * factor + (float)this.mXAxis.mLabelRotatedWidth / 2.0F, angle, pOut);
                this.drawLabel(c, label, pOut.x, pOut.y - (float)this.mXAxis.mLabelRotatedHeight / 2.0F, drawLabelAnchor, labelRotationAngleDegrees);
            }

            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(pOut);
            MPPointF.recycleInstance(drawLabelAnchor);
        }
    }

    public void renderLimitLines(Canvas c) {
    }
}
