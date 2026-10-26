/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Point
 *  android.opengl.GLES20
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.graphics.Matrix;
import android.graphics.Point;
import android.opengl.GLES20;
import androidx.annotation.Nullable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.webrtc.GlUtil;
import org.webrtc.Logging;
import org.webrtc.RendererCommon;
import org.webrtc.VideoFrame;
import org.webrtc.YuvHelper;

public class VideoFrameDrawer {
    public static final String TAG = "VideoFrameDrawer";
    static final float[] srcPoints = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f};
    private final float[] dstPoints = new float[6];
    private final Point renderSize = new Point();
    private int renderWidth;
    private int renderHeight;
    private final YuvUploader yuvUploader = new YuvUploader();
    @Nullable
    private VideoFrame lastI420Frame;
    private final Matrix renderMatrix = new Matrix();

    public static void drawTexture(RendererCommon.GlDrawer drawer, VideoFrame.TextureBuffer buffer, Matrix renderMatrix, int frameWidth, int frameHeight, int viewportX, int viewportY, int viewportWidth, int viewportHeight) {
        Matrix finalMatrix = new Matrix(buffer.getTransformMatrix());
        finalMatrix.preConcat(renderMatrix);
        float[] finalGlMatrix = RendererCommon.convertMatrixFromAndroidGraphicsMatrix(finalMatrix);
        switch (buffer.getType()) {
            case OES: {
                drawer.drawOes(buffer.getTextureId(), finalGlMatrix, frameWidth, frameHeight, viewportX, viewportY, viewportWidth, viewportHeight);
                break;
            }
            case RGB: {
                drawer.drawRgb(buffer.getTextureId(), finalGlMatrix, frameWidth, frameHeight, viewportX, viewportY, viewportWidth, viewportHeight);
                break;
            }
            default: {
                throw new RuntimeException("Unknown texture type.");
            }
        }
    }

    private static int distance(float x0, float y0, float x1, float y1) {
        return (int)Math.round(Math.hypot(x1 - x0, y1 - y0));
    }

    private void calculateTransformedRenderSize(int frameWidth, int frameHeight, @Nullable Matrix renderMatrix) {
        if (renderMatrix == null) {
            this.renderWidth = frameWidth;
            this.renderHeight = frameHeight;
            return;
        }
        renderMatrix.mapPoints(this.dstPoints, srcPoints);
        for (int i = 0; i < 3; ++i) {
            int n = i * 2 + 0;
            this.dstPoints[n] = this.dstPoints[n] * (float)frameWidth;
            int n2 = i * 2 + 1;
            this.dstPoints[n2] = this.dstPoints[n2] * (float)frameHeight;
        }
        this.renderWidth = VideoFrameDrawer.distance(this.dstPoints[0], this.dstPoints[1], this.dstPoints[2], this.dstPoints[3]);
        this.renderHeight = VideoFrameDrawer.distance(this.dstPoints[0], this.dstPoints[1], this.dstPoints[4], this.dstPoints[5]);
    }

    public void drawFrame(VideoFrame frame, RendererCommon.GlDrawer drawer) {
        this.drawFrame(frame, drawer, null);
    }

    public void drawFrame(VideoFrame frame, RendererCommon.GlDrawer drawer, Matrix additionalRenderMatrix) {
        this.drawFrame(frame, drawer, additionalRenderMatrix, 0, 0, frame.getRotatedWidth(), frame.getRotatedHeight());
    }

    public void drawFrame(VideoFrame frame, RendererCommon.GlDrawer drawer, @Nullable Matrix additionalRenderMatrix, int viewportX, int viewportY, int viewportWidth, int viewportHeight) {
        int width = frame.getRotatedWidth();
        int height = frame.getRotatedHeight();
        this.calculateTransformedRenderSize(width, height, additionalRenderMatrix);
        if (this.renderWidth <= 0 || this.renderHeight <= 0) {
            Logging.w(TAG, "Illegal frame size: " + this.renderWidth + "x" + this.renderHeight);
            return;
        }
        boolean isTextureFrame = frame.getBuffer() instanceof VideoFrame.TextureBuffer;
        this.renderMatrix.reset();
        this.renderMatrix.preTranslate(0.5f, 0.5f);
        if (!isTextureFrame) {
            this.renderMatrix.preScale(1.0f, -1.0f);
        }
        this.renderMatrix.preRotate((float)frame.getRotation());
        this.renderMatrix.preTranslate(-0.5f, -0.5f);
        if (additionalRenderMatrix != null) {
            this.renderMatrix.preConcat(additionalRenderMatrix);
        }
        if (isTextureFrame) {
            this.lastI420Frame = null;
            VideoFrameDrawer.drawTexture(drawer, (VideoFrame.TextureBuffer)frame.getBuffer(), this.renderMatrix, this.renderWidth, this.renderHeight, viewportX, viewportY, viewportWidth, viewportHeight);
        } else {
            if (frame != this.lastI420Frame) {
                this.lastI420Frame = frame;
                VideoFrame.I420Buffer i420Buffer = frame.getBuffer().toI420();
                this.yuvUploader.uploadFromBuffer(i420Buffer);
                i420Buffer.release();
            }
            drawer.drawYuv(this.yuvUploader.getYuvTextures(), RendererCommon.convertMatrixFromAndroidGraphicsMatrix(this.renderMatrix), this.renderWidth, this.renderHeight, viewportX, viewportY, viewportWidth, viewportHeight);
        }
    }

    public VideoFrame.Buffer prepareBufferForViewportSize(VideoFrame.Buffer buffer, int width, int height) {
        buffer.retain();
        return buffer;
    }

    public void release() {
        this.yuvUploader.release();
        this.lastI420Frame = null;
    }

    private static class YuvUploader {
        @Nullable
        private ByteBuffer copyBuffer;
        @Nullable
        private int[] yuvTextures;

        private YuvUploader() {
        }

        @Nullable
        public int[] uploadYuvData(int width, int height, int[] strides, ByteBuffer[] planes) {
            int i;
            int[] planeWidths = new int[]{width, width / 2, width / 2};
            int[] planeHeights = new int[]{height, height / 2, height / 2};
            int copyCapacityNeeded = 0;
            for (i = 0; i < 3; ++i) {
                if (strides[i] <= planeWidths[i]) continue;
                copyCapacityNeeded = Math.max(copyCapacityNeeded, planeWidths[i] * planeHeights[i]);
            }
            if (copyCapacityNeeded > 0 && (this.copyBuffer == null || this.copyBuffer.capacity() < copyCapacityNeeded)) {
                this.copyBuffer = ByteBuffer.allocateDirect(copyCapacityNeeded);
            }
            if (this.yuvTextures == null) {
                this.yuvTextures = new int[3];
                for (i = 0; i < 3; ++i) {
                    this.yuvTextures[i] = GlUtil.generateTexture(3553);
                }
            }
            for (i = 0; i < 3; ++i) {
                ByteBuffer packedByteBuffer;
                GLES20.glActiveTexture((int)(33984 + i));
                GLES20.glBindTexture((int)3553, (int)this.yuvTextures[i]);
                if (strides[i] == planeWidths[i]) {
                    packedByteBuffer = planes[i];
                } else {
                    YuvHelper.copyPlane(planes[i], strides[i], this.copyBuffer, planeWidths[i], planeWidths[i], planeHeights[i]);
                    packedByteBuffer = this.copyBuffer;
                }
                GLES20.glTexImage2D((int)3553, (int)0, (int)6409, (int)planeWidths[i], (int)planeHeights[i], (int)0, (int)6409, (int)5121, (Buffer)packedByteBuffer);
            }
            return this.yuvTextures;
        }

        @Nullable
        public int[] uploadFromBuffer(VideoFrame.I420Buffer buffer) {
            int[] strides = new int[]{buffer.getStrideY(), buffer.getStrideU(), buffer.getStrideV()};
            ByteBuffer[] planes = new ByteBuffer[]{buffer.getDataY(), buffer.getDataU(), buffer.getDataV()};
            return this.uploadYuvData(buffer.getWidth(), buffer.getHeight(), strides, planes);
        }

        @Nullable
        public int[] getYuvTextures() {
            return this.yuvTextures;
        }

        public void release() {
            this.copyBuffer = null;
            if (this.yuvTextures != null) {
                GLES20.glDeleteTextures((int)3, (int[])this.yuvTextures, (int)0);
                this.yuvTextures = null;
            }
        }
    }
}

