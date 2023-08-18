package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.components.YAxis;
import com.kore.ai.widgetsdk.charts.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {
    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency var1);
}
