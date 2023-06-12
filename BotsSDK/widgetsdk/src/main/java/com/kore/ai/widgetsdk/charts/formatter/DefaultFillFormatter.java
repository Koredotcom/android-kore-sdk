package com.kore.ai.widgetsdk.charts.formatter;

import com.kore.ai.widgetsdk.charts.data.LineData;
import com.kore.ai.widgetsdk.charts.interfaces.dataprovider.LineDataProvider;
import com.kore.ai.widgetsdk.charts.interfaces.datasets.ILineDataSet;

public class DefaultFillFormatter implements IFillFormatter {
    public DefaultFillFormatter() {
    }

    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
        float fillMin = 0.0F;
        float chartMaxY = dataProvider.getYChartMax();
        float chartMinY = dataProvider.getYChartMin();
        LineData data = dataProvider.getLineData();
        if (dataSet.getYMax() > 0.0F && dataSet.getYMin() < 0.0F) {
            fillMin = 0.0F;
        } else {
            float max;
            if (data.getYMax() > 0.0F) {
                max = 0.0F;
            } else {
                max = chartMaxY;
            }

            float min;
            if (data.getYMin() < 0.0F) {
                min = 0.0F;
            } else {
                min = chartMinY;
            }

            fillMin = dataSet.getYMin() >= 0.0F ? min : max;
        }

        return fillMin;
    }
}
