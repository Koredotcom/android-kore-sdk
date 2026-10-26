/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.log;

public enum LogLevel {
    VERBOSE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5),
    ASSERT(6),
    NONE(7);

    private final int value;

    private LogLevel(int newValue) {
        this.value = newValue;
    }

    public int getValue() {
        return this.value;
    }
}

