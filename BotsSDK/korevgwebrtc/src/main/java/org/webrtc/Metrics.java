/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.HashMap;
import java.util.Map;
import org.webrtc.CalledByNative;

public class Metrics {
    private static final String TAG = "Metrics";
    public final Map<String, HistogramInfo> map = new HashMap<String, HistogramInfo>();

    @CalledByNative
    Metrics() {
    }

    @CalledByNative
    private void add(String name, HistogramInfo info) {
        this.map.put(name, info);
    }

    public static void enable() {
        Metrics.nativeEnable();
    }

    public static Metrics getAndReset() {
        return Metrics.nativeGetAndReset();
    }

    private static native void nativeEnable();

    private static native Metrics nativeGetAndReset();

    public static class HistogramInfo {
        public final int min;
        public final int max;
        public final int bucketCount;
        public final Map<Integer, Integer> samples = new HashMap<Integer, Integer>();

        @CalledByNative(value="HistogramInfo")
        public HistogramInfo(int min, int max, int bucketCount) {
            this.min = min;
            this.max = max;
            this.bucketCount = bucketCount;
        }

        @CalledByNative(value="HistogramInfo")
        public void addSample(int value, int numEvents) {
            this.samples.put(value, numEvents);
        }
    }
}

