package kore.botssdk.charts.formatter;

import kore.botssdk.charts.interfaces.dataprovider.LineDataProvider;
import kore.botssdk.charts.interfaces.datasets.ILineDataSet;

public interface IFillFormatter {
    float getFillLinePosition(ILineDataSet var1, LineDataProvider var2);
}
