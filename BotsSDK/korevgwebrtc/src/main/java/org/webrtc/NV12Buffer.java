/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.nio.ByteBuffer;
import org.webrtc.JavaI420Buffer;
import org.webrtc.RefCountDelegate;
import org.webrtc.VideoFrame;

public class NV12Buffer
implements VideoFrame.Buffer {
    private final int width;
    private final int height;
    private final int stride;
    private final int sliceHeight;
    private final ByteBuffer buffer;
    private final RefCountDelegate refCountDelegate;

    public NV12Buffer(int width, int height, int stride, int sliceHeight, ByteBuffer buffer, @Nullable Runnable releaseCallback) {
        this.width = width;
        this.height = height;
        this.stride = stride;
        this.sliceHeight = sliceHeight;
        this.buffer = buffer;
        this.refCountDelegate = new RefCountDelegate(releaseCallback);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public VideoFrame.I420Buffer toI420() {
        return (VideoFrame.I420Buffer)this.cropAndScale(0, 0, this.width, this.height, this.width, this.height);
    }

    @Override
    public void retain() {
        this.refCountDelegate.retain();
    }

    @Override
    public void release() {
        this.refCountDelegate.release();
    }

    @Override
    public VideoFrame.Buffer cropAndScale(int cropX, int cropY, int cropWidth, int cropHeight, int scaleWidth, int scaleHeight) {
        JavaI420Buffer newBuffer = JavaI420Buffer.allocate(scaleWidth, scaleHeight);
        NV12Buffer.nativeCropAndScale(cropX, cropY, cropWidth, cropHeight, scaleWidth, scaleHeight, this.buffer, this.width, this.height, this.stride, this.sliceHeight, newBuffer.getDataY(), newBuffer.getStrideY(), newBuffer.getDataU(), newBuffer.getStrideU(), newBuffer.getDataV(), newBuffer.getStrideV());
        return newBuffer;
    }

    private static native void nativeCropAndScale(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6, int var7, int var8, int var9, int var10, ByteBuffer var11, int var12, ByteBuffer var13, int var14, ByteBuffer var15, int var16);
}

