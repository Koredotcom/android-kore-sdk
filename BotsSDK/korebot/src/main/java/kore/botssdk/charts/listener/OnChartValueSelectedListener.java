package kore.botssdk.charts.listener;

import kore.botssdk.charts.data.Entry;
import kore.botssdk.charts.highlight.Highlight;

public interface OnChartValueSelectedListener {
    void onValueSelected(Entry var1, Highlight var2);

    void onNothingSelected();
}
