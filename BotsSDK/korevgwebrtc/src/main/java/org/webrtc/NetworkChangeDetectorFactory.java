/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package org.webrtc;

import android.content.Context;
import org.webrtc.NetworkChangeDetector;

public interface NetworkChangeDetectorFactory {
    public NetworkChangeDetector create(NetworkChangeDetector.Observer var1, Context var2);
}

