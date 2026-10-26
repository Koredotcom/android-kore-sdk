/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.graphics.Matrix;
import androidx.annotation.Nullable;
import java.nio.ByteBuffer;
import org.webrtc.CalledByNative;
import org.webrtc.RefCounted;

public class VideoFrame
implements RefCounted {
    private final Buffer buffer;
    private final int rotation;
    private final long timestampNs;

    @CalledByNative
    public VideoFrame(Buffer buffer, int rotation, long timestampNs) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer not allowed to be null");
        }
        if (rotation % 90 != 0) {
            throw new IllegalArgumentException("rotation must be a multiple of 90");
        }
        this.buffer = buffer;
        this.rotation = rotation;
        this.timestampNs = timestampNs;
    }

    @CalledByNative
    public Buffer getBuffer() {
        return this.buffer;
    }

    @CalledByNative
    public int getRotation() {
        return this.rotation;
    }

    @CalledByNative
    public long getTimestampNs() {
        return this.timestampNs;
    }

    public int getRotatedWidth() {
        if (this.rotation % 180 == 0) {
            return this.buffer.getWidth();
        }
        return this.buffer.getHeight();
    }

    public int getRotatedHeight() {
        if (this.rotation % 180 == 0) {
            return this.buffer.getHeight();
        }
        return this.buffer.getWidth();
    }

    @Override
    public void retain() {
        this.buffer.retain();
    }

    @Override
    @CalledByNative
    public void release() {
        this.buffer.release();
    }

    public static interface Buffer
    extends RefCounted {
        @CalledByNative(value="Buffer")
        default public int getBufferType() {
            return 0;
        }

        @CalledByNative(value="Buffer")
        public int getWidth();

        @CalledByNative(value="Buffer")
        public int getHeight();

        @Nullable
        @CalledByNative(value="Buffer")
        public I420Buffer toI420();

        @Override
        @CalledByNative(value="Buffer")
        public void retain();

        @Override
        @CalledByNative(value="Buffer")
        public void release();

        @CalledByNative(value="Buffer")
        public Buffer cropAndScale(int var1, int var2, int var3, int var4, int var5, int var6);
    }

    public static interface TextureBuffer
    extends Buffer {
        public Type getType();

        public int getTextureId();

        public Matrix getTransformMatrix();

        default public TextureBuffer applyTransformMatrix(Matrix transformMatrix, int newWidth, int newHeight) {
            throw new UnsupportedOperationException("Not implemented");
        }

        default public int getUnscaledWidth() {
            return this.getWidth();
        }

        default public int getUnscaledHeight() {
            return this.getHeight();
        }

        public static enum Type {
            OES(36197),
            RGB(3553);

            private final int glTarget;

            private Type(int glTarget) {
                this.glTarget = glTarget;
            }

            public int getGlTarget() {
                return this.glTarget;
            }
        }
    }

    public static interface I420Buffer
    extends Buffer {
        @Override
        default public int getBufferType() {
            return 1;
        }

        @CalledByNative(value="I420Buffer")
        public ByteBuffer getDataY();

        @CalledByNative(value="I420Buffer")
        public ByteBuffer getDataU();

        @CalledByNative(value="I420Buffer")
        public ByteBuffer getDataV();

        @CalledByNative(value="I420Buffer")
        public int getStrideY();

        @CalledByNative(value="I420Buffer")
        public int getStrideU();

        @CalledByNative(value="I420Buffer")
        public int getStrideV();
    }
}

