/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.EGLContext
 */
package org.webrtc;

import android.opengl.EGLContext;
import org.webrtc.EglBase;

public interface EglBase14
extends EglBase {

    public static interface Context
    extends EglBase.Context {
        public EGLContext getRawContext();
    }
}

