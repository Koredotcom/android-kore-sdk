package com.kore.ai.widgetsdk.charts.listener;

import com.kore.ai.widgetsdk.charts.data.Entry;
import com.kore.ai.widgetsdk.charts.highlight.Highlight;

public interface OnChartValueSelectedListener {
    void onValueSelected(Entry var1, Highlight var2);

    void onNothingSelected();
}
