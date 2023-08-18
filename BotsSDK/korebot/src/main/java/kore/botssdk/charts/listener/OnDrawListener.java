package kore.botssdk.charts.listener;

import kore.botssdk.charts.data.DataSet;
import kore.botssdk.charts.data.Entry;

public interface OnDrawListener {
    void onEntryAdded(Entry var1);

    void onEntryMoved(Entry var1);

    void onDrawFinished(DataSet<?> var1);
}
