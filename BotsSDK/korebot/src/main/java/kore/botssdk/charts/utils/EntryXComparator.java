package kore.botssdk.charts.utils;

import java.util.Comparator;

import kore.botssdk.charts.data.Entry;

public class EntryXComparator implements Comparator<Entry> {
    public EntryXComparator() {
    }

    public int compare(Entry entry1, Entry entry2) {
        float diff = entry1.getX() - entry2.getX();
        if (diff == 0.0F) {
            return 0;
        } else {
            return diff > 0.0F ? 1 : -1;
        }
    }
}
