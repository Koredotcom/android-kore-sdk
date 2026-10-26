/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.Loggable;
import org.webrtc.Logging;

class JNILogging {
    private final Loggable loggable;

    public JNILogging(Loggable loggable) {
        this.loggable = loggable;
    }

    @CalledByNative
    public void logToInjectable(String message, Integer severity, String tag) {
        this.loggable.onLogMessage(message, Logging.Severity.values()[severity], tag);
    }
}

