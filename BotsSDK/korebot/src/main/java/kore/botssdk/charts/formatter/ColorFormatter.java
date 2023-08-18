package kore.botssdk.charts.formatter;

import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.interfaces.datasets.IDataSet;

public interface ColorFormatter {
    int getColor(int var1, Entry var2, IDataSet var3);
}
