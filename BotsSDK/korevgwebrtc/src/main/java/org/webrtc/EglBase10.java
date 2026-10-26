/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.khronos.egl.EGL10
 *  javax.microedition.khronos.egl.EGLConfig
 *  javax.microedition.khronos.egl.EGLContext
 *  javax.microedition.khronos.egl.EGLDisplay
 */
package org.webrtc;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import org.webrtc.EglBase;

public interface EglBase10
extends EglBase {

    public static interface EglConnection
    extends EglBase.EglConnection {
        public EGL10 getEgl();

        public EGLContext getContext();

        public EGLDisplay getDisplay();

        public EGLConfig getConfig();
    }

    public static interface Context
    extends EglBase.Context {
        public EGLContext getRawContext();
    }
}

