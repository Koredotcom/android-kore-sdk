/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.SOURCE)
public @interface VideoFrameBufferType {
    public static final int NATIVE = 0;
    public static final int I420 = 1;
    public static final int I420A = 2;
    public static final int I444 = 3;
    public static final int I010 = 4;
    public static final int NV12 = 5;
}

