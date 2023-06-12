package com.kore.ai.widgetsdk.charts.interfaces.dataprovider;

import com.kore.ai.widgetsdk.charts.components.YAxis;
import com.kore.ai.widgetsdk.charts.data.BarLineScatterCandleBubbleData;
import com.kore.ai.widgetsdk.charts.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {
    Transformer getTransformer(YAxis.AxisDependency var1);

    boolean isInverted(YAxis.AxisDependency var1);

    float getLowestVisibleX();

    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
