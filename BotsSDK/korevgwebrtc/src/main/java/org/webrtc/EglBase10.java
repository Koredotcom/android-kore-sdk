/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.khronos.egl.EGLContext
 */
package org.webrtc;

import javax.microedition.khronos.egl.EGLContext;
import org.webrtc.EglBase;

public interface EglBase10
extends EglBase {

    public static interface Context
    extends EglBase.Context {
        public EGLContext getRawContext();
    }
}

