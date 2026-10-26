/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;

public class StatsReport {
    public final String id;
    public final String type;
    public final double timestamp;
    public final Value[] values;

    @CalledByNative
    public StatsReport(String id, String type, double timestamp, Value[] values) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.values = values;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(this.id).append(", type: ").append(this.type).append(", timestamp: ").append(this.timestamp).append(", values: ");
        for (int i = 0; i < this.values.length; ++i) {
            builder.append(this.values[i].toString()).append(", ");
        }
        return builder.toString();
    }

    public static class Value {
        public final String name;
        public final String value;

        @CalledByNative(value="Value")
        public Value(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[").append(this.name).append(": ").append(this.value).append("]");
            return builder.toString();
        }
    }
}

