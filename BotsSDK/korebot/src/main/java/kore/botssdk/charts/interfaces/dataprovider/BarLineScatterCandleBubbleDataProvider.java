package kore.botssdk.charts.interfaces.dataprovider;

import kore.botssdk.charts.components.YAxis;
import kore.botssdk.charts.data.BarLineScatterCandleBubbleData;
import kore.botssdk.charts.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {
    Transformer getTransformer(YAxis.AxisDependency var1);

    boolean isInverted(YAxis.AxisDependency var1);

    float getLowestVisibleX();

    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
