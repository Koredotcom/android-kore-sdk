/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package org.webrtc;

import android.content.Context;
import org.webrtc.CalledByNative;
import org.webrtc.ContextUtils;

public class ApplicationContextProvider {
    @CalledByNative
    public static Context getApplicationContext() {
        return ContextUtils.getApplicationContext();
    }
}

