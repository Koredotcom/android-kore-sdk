package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {
    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency var1);
}
