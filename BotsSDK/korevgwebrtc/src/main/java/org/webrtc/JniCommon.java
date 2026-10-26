/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.nio.ByteBuffer;

public class JniCommon {
    public static native void nativeAddRef(long var0);

    public static native void nativeReleaseRef(long var0);

    public static native ByteBuffer nativeAllocateByteBuffer(int var0);

    public static native void nativeFreeByteBuffer(ByteBuffer var0);
}

