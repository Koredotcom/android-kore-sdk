/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.Logging;

public class CallSessionFileRotatingLogSink {
    private long nativeSink;

    public static byte[] getLogData(String dirPath) {
        if (dirPath == null) {
            throw new IllegalArgumentException("dirPath may not be null.");
        }
        return CallSessionFileRotatingLogSink.nativeGetLogData(dirPath);
    }

    public CallSessionFileRotatingLogSink(String dirPath, int maxFileSize, Logging.Severity severity) {
        if (dirPath == null) {
            throw new IllegalArgumentException("dirPath may not be null.");
        }
        this.nativeSink = CallSessionFileRotatingLogSink.nativeAddSink(dirPath, maxFileSize, severity.ordinal());
    }

    public void dispose() {
        if (this.nativeSink != 0L) {
            CallSessionFileRotatingLogSink.nativeDeleteSink(this.nativeSink);
            this.nativeSink = 0L;
        }
    }

    private static native long nativeAddSink(String var0, int var1, int var2);

    private static native void nativeDeleteSink(long var0);

    private static native byte[] nativeGetLogData(String var0);
}

