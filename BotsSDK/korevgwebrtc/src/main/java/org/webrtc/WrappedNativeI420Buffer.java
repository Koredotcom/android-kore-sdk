/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.nio.ByteBuffer;
import org.webrtc.CalledByNative;
import org.webrtc.JavaI420Buffer;
import org.webrtc.JniCommon;
import org.webrtc.VideoFrame;

class WrappedNativeI420Buffer
implements VideoFrame.I420Buffer {
    private final int width;
    private final int height;
    private final ByteBuffer dataY;
    private final int strideY;
    private final ByteBuffer dataU;
    private final int strideU;
    private final ByteBuffer dataV;
    private final int strideV;
    private final long nativeBuffer;

    @CalledByNative
    WrappedNativeI420Buffer(int width, int height, ByteBuffer dataY, int strideY, ByteBuffer dataU, int strideU, ByteBuffer dataV, int strideV, long nativeBuffer) {
        this.width = width;
        this.height = height;
        this.dataY = dataY;
        this.strideY = strideY;
        this.dataU = dataU;
        this.strideU = strideU;
        this.dataV = dataV;
        this.strideV = strideV;
        this.nativeBuffer = nativeBuffer;
        this.retain();
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
    public ByteBuffer getDataY() {
        return this.dataY.slice();
    }

    @Override
    public ByteBuffer getDataU() {
        return this.dataU.slice();
    }

    @Override
    public ByteBuffer getDataV() {
        return this.dataV.slice();
    }

    @Override
    public int getStrideY() {
        return this.strideY;
    }

    @Override
    public int getStrideU() {
        return this.strideU;
    }

    @Override
    public int getStrideV() {
        return this.strideV;
    }

    @Override
    public VideoFrame.I420Buffer toI420() {
        this.retain();
        return this;
    }

    @Override
    public void retain() {
        JniCommon.nativeAddRef(this.nativeBuffer);
    }

    @Override
    public void release() {
        JniCommon.nativeReleaseRef(this.nativeBuffer);
    }

    @Override
    public VideoFrame.Buffer cropAndScale(int cropX, int cropY, int cropWidth, int cropHeight, int scaleWidth, int scaleHeight) {
        return JavaI420Buffer.cropAndScaleI420(this, cropX, cropY, cropWidth, cropHeight, scaleWidth, scaleHeight);
    }
}

