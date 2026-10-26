/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.EGLConfig
 *  android.opengl.EGLContext
 *  android.opengl.EGLDisplay
 */
package org.webrtc;

import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import org.webrtc.EglBase;

public interface EglBase14
extends EglBase {

    public static interface EglConnection
    extends EglBase.EglConnection {
        public EGLContext getContext();

        public EGLDisplay getDisplay();

        public EGLConfig getConfig();
    }

    public static interface Context
    extends EglBase.Context {
        public EGLContext getRawContext();
    }
}

