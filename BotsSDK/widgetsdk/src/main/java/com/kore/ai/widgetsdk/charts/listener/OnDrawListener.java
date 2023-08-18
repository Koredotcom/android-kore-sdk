package com.kore.ai.widgetsdk.charts.listener;

import com.kore.ai.widgetsdk.charts.data.DataSet;
import com.kore.ai.widgetsdk.charts.data.Entry;

public interface OnDrawListener {
    void onEntryAdded(Entry var1);

    void onEntryMoved(Entry var1);

    void onDrawFinished(DataSet<?> var1);
}
