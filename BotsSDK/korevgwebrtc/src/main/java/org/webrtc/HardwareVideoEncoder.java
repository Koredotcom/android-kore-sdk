/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.media.MediaCodec$BufferInfo
 *  android.media.MediaFormat
 *  android.opengl.GLES20
 *  android.os.Bundle
 *  androidx.annotation.Nullable
 *  android.view.Surface
 */
package org.webrtc;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import org.webrtc.BitrateAdjuster;
import org.webrtc.EglBase;
import org.webrtc.EglBase14;
import org.webrtc.EncodedImage;
import org.webrtc.GlRectDrawer;
import org.webrtc.Logging;
import org.webrtc.MediaCodecWrapper;
import org.webrtc.MediaCodecWrapperFactory;
import org.webrtc.ThreadUtils;
import org.webrtc.VideoCodecMimeType;
import org.webrtc.VideoCodecStatus;
import org.webrtc.VideoEncoder;
import org.webrtc.VideoFrame;
import org.webrtc.VideoFrameDrawer;
import org.webrtc.YuvHelper;

@TargetApi(value=19)
class HardwareVideoEncoder
implements VideoEncoder {
    private static final String TAG = "HardwareVideoEncoder";
    private static final int VIDEO_ControlRateConstant = 2;
    private static final String KEY_BITRATE_MODE = "bitrate-mode";
    private static final int VIDEO_AVC_PROFILE_HIGH = 8;
    private static final int VIDEO_AVC_LEVEL_3 = 256;
    private static final int MAX_VIDEO_FRAMERATE = 30;
    private static final int MAX_ENCODER_Q_SIZE = 2;
    private static final int MEDIA_CODEC_RELEASE_TIMEOUT_MS = 5000;
    private static final int DEQUEUE_OUTPUT_BUFFER_TIMEOUT_US = 100000;
    private final MediaCodecWrapperFactory mediaCodecWrapperFactory;
    private final String codecName;
    private final VideoCodecMimeType codecType;
    private final Integer surfaceColorFormat;
    private final Integer yuvColorFormat;
    private final YuvFormat yuvFormat;
    private final Map<String, String> params;
    private final int keyFrameIntervalSec;
    private final long forcedKeyFrameNs;
    private final BitrateAdjuster bitrateAdjuster;
    private final EglBase14.Context sharedContext;
    private final GlRectDrawer textureDrawer = new GlRectDrawer();
    private final VideoFrameDrawer videoFrameDrawer = new VideoFrameDrawer();
    private final BlockingDeque<EncodedImage.Builder> outputBuilders = new LinkedBlockingDeque<EncodedImage.Builder>();
    private final ThreadUtils.ThreadChecker encodeThreadChecker = new ThreadUtils.ThreadChecker();
    private final ThreadUtils.ThreadChecker outputThreadChecker = new ThreadUtils.ThreadChecker();
    private final BusyCount outputBuffersBusyCount = new BusyCount();
    private VideoEncoder.Callback callback;
    private boolean automaticResizeOn;
    @Nullable
    private MediaCodecWrapper codec;
    @Nullable
    private ByteBuffer[] outputBuffers;
    @Nullable
    private Thread outputThread;
    @Nullable
    private EglBase14 textureEglBase;
    @Nullable
    private Surface textureInputSurface;
    private int width;
    private int height;
    private boolean useSurfaceMode;
    private long lastKeyFrameNs;
    @Nullable
    private ByteBuffer configBuffer;
    private int adjustedBitrate;
    private volatile boolean running;
    @Nullable
    private volatile Exception shutdownException;

    public HardwareVideoEncoder(MediaCodecWrapperFactory mediaCodecWrapperFactory, String codecName, VideoCodecMimeType codecType, Integer surfaceColorFormat, Integer yuvColorFormat, Map<String, String> params, int keyFrameIntervalSec, int forceKeyFrameIntervalMs, BitrateAdjuster bitrateAdjuster, EglBase14.Context sharedContext) {
        this.mediaCodecWrapperFactory = mediaCodecWrapperFactory;
        this.codecName = codecName;
        this.codecType = codecType;
        this.surfaceColorFormat = surfaceColorFormat;
        this.yuvColorFormat = yuvColorFormat;
        this.yuvFormat = YuvFormat.valueOf(yuvColorFormat);
        this.params = params;
        this.keyFrameIntervalSec = keyFrameIntervalSec;
        this.forcedKeyFrameNs = TimeUnit.MILLISECONDS.toNanos(forceKeyFrameIntervalMs);
        this.bitrateAdjuster = bitrateAdjuster;
        this.sharedContext = sharedContext;
        this.encodeThreadChecker.detachThread();
    }

    @Override
    public VideoCodecStatus initEncode(VideoEncoder.Settings settings, VideoEncoder.Callback callback) {
        this.encodeThreadChecker.checkIsOnValidThread();
        this.callback = callback;
        this.automaticResizeOn = settings.automaticResizeOn;
        this.width = settings.width;
        this.height = settings.height;
        this.useSurfaceMode = this.canUseSurface();
        if (settings.startBitrate != 0 && settings.maxFramerate != 0) {
            this.bitrateAdjuster.setTargets(settings.startBitrate * 1000, settings.maxFramerate);
        }
        this.adjustedBitrate = this.bitrateAdjuster.getAdjustedBitrateBps();
        Logging.d(TAG, "initEncode: " + this.width + " x " + this.height + ". @ " + settings.startBitrate + "kbps. Fps: " + settings.maxFramerate + " Use surface mode: " + this.useSurfaceMode);
        return this.initEncodeInternal();
    }

    private VideoCodecStatus initEncodeInternal() {
        this.encodeThreadChecker.checkIsOnValidThread();
        this.lastKeyFrameNs = -1L;
        try {
            this.codec = this.mediaCodecWrapperFactory.createByCodecName(this.codecName);
        }
        catch (IOException | IllegalArgumentException e) {
            Logging.e(TAG, "Cannot create media encoder " + this.codecName);
            return VideoCodecStatus.FALLBACK_SOFTWARE;
        }
        int colorFormat = this.useSurfaceMode ? this.surfaceColorFormat : this.yuvColorFormat;
        try {
            MediaFormat format = MediaFormat.createVideoFormat((String)this.codecType.mimeType(), (int)this.width, (int)this.height);
            format.setInteger("bitrate", this.adjustedBitrate);
            format.setInteger(KEY_BITRATE_MODE, 2);
            format.setInteger("color-format", colorFormat);
            format.setInteger("frame-rate", this.bitrateAdjuster.getCodecConfigFramerate());
            format.setInteger("i-frame-interval", this.keyFrameIntervalSec);
            if (this.codecType == VideoCodecMimeType.H264) {
                String profileLevelId = this.params.get("profile-level-id");
                if (profileLevelId == null) {
                    profileLevelId = "42e01f";
                }
                switch (profileLevelId) {
                    case "640c1f": {
                        format.setInteger("profile", 8);
                        format.setInteger("level", 256);
                        break;
                    }
                    case "42e01f": {
                        break;
                    }
                    default: {
                        Logging.w(TAG, "Unknown profile level id: " + profileLevelId);
                    }
                }
            }
            Logging.d(TAG, "Format: " + format);
            this.codec.configure(format, null, null, 1);
            if (this.useSurfaceMode) {
                this.textureEglBase = EglBase.createEgl14(this.sharedContext, EglBase.CONFIG_RECORDABLE);
                this.textureInputSurface = this.codec.createInputSurface();
                this.textureEglBase.createSurface(this.textureInputSurface);
                this.textureEglBase.makeCurrent();
            }
            this.codec.start();
            this.outputBuffers = this.codec.getOutputBuffers();
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "initEncodeInternal failed", e);
            this.release();
            return VideoCodecStatus.FALLBACK_SOFTWARE;
        }
        this.running = true;
        this.outputThreadChecker.detachThread();
        this.outputThread = this.createOutputThread();
        this.outputThread.start();
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoCodecStatus release() {
        VideoCodecStatus returnValue;
        this.encodeThreadChecker.checkIsOnValidThread();
        if (this.outputThread == null) {
            returnValue = VideoCodecStatus.OK;
        } else {
            this.running = false;
            if (!ThreadUtils.joinUninterruptibly(this.outputThread, 5000L)) {
                Logging.e(TAG, "Media encoder release timeout");
                returnValue = VideoCodecStatus.TIMEOUT;
            } else if (this.shutdownException != null) {
                Logging.e(TAG, "Media encoder release exception", this.shutdownException);
                returnValue = VideoCodecStatus.ERROR;
            } else {
                returnValue = VideoCodecStatus.OK;
            }
        }
        this.textureDrawer.release();
        this.videoFrameDrawer.release();
        if (this.textureEglBase != null) {
            this.textureEglBase.release();
            this.textureEglBase = null;
        }
        if (this.textureInputSurface != null) {
            this.textureInputSurface.release();
            this.textureInputSurface = null;
        }
        this.outputBuilders.clear();
        this.codec = null;
        this.outputBuffers = null;
        this.outputThread = null;
        this.encodeThreadChecker.detachThread();
        return returnValue;
    }

    @Override
    public VideoCodecStatus encode(VideoFrame videoFrame, VideoEncoder.EncodeInfo encodeInfo) {
        VideoCodecStatus status;
        boolean shouldUseSurfaceMode;
        this.encodeThreadChecker.checkIsOnValidThread();
        if (this.codec == null) {
            return VideoCodecStatus.UNINITIALIZED;
        }
        VideoFrame.Buffer videoFrameBuffer = videoFrame.getBuffer();
        boolean isTextureBuffer = videoFrameBuffer instanceof VideoFrame.TextureBuffer;
        int frameWidth = videoFrame.getBuffer().getWidth();
        int frameHeight = videoFrame.getBuffer().getHeight();
        boolean bl = shouldUseSurfaceMode = this.canUseSurface() && isTextureBuffer;
        if ((frameWidth != this.width || frameHeight != this.height || shouldUseSurfaceMode != this.useSurfaceMode) && (status = this.resetCodec(frameWidth, frameHeight, shouldUseSurfaceMode)) != VideoCodecStatus.OK) {
            return status;
        }
        if (this.outputBuilders.size() > 2) {
            Logging.e(TAG, "Dropped frame, encoder queue full");
            return VideoCodecStatus.NO_OUTPUT;
        }
        boolean requestedKeyFrame = false;
        for (EncodedImage.FrameType frameType : encodeInfo.frameTypes) {
            if (frameType != EncodedImage.FrameType.VideoFrameKey) continue;
            requestedKeyFrame = true;
        }
        if (requestedKeyFrame || this.shouldForceKeyFrame(videoFrame.getTimestampNs())) {
            this.requestKeyFrame(videoFrame.getTimestampNs());
        }
        int bufferSize = videoFrameBuffer.getHeight() * videoFrameBuffer.getWidth() * 3 / 2;
        EncodedImage.Builder builder = EncodedImage.builder().setCaptureTimeNs(videoFrame.getTimestampNs()).setEncodedWidth(videoFrame.getBuffer().getWidth()).setEncodedHeight(videoFrame.getBuffer().getHeight()).setRotation(videoFrame.getRotation());
        this.outputBuilders.offer(builder);
        VideoCodecStatus returnValue = this.useSurfaceMode ? this.encodeTextureBuffer(videoFrame) : this.encodeByteBuffer(videoFrame, videoFrameBuffer, bufferSize);
        if (returnValue != VideoCodecStatus.OK) {
            this.outputBuilders.pollLast();
        }
        return returnValue;
    }

    private VideoCodecStatus encodeTextureBuffer(VideoFrame videoFrame) {
        this.encodeThreadChecker.checkIsOnValidThread();
        try {
            GLES20.glClear((int)16384);
            VideoFrame derotatedFrame = new VideoFrame(videoFrame.getBuffer(), 0, videoFrame.getTimestampNs());
            this.videoFrameDrawer.drawFrame(derotatedFrame, this.textureDrawer, null);
            this.textureEglBase.swapBuffers(videoFrame.getTimestampNs());
        }
        catch (RuntimeException e) {
            Logging.e(TAG, "encodeTexture failed", e);
            return VideoCodecStatus.ERROR;
        }
        return VideoCodecStatus.OK;
    }

    private VideoCodecStatus encodeByteBuffer(VideoFrame videoFrame, VideoFrame.Buffer videoFrameBuffer, int bufferSize) {
        ByteBuffer buffer;
        int index;
        this.encodeThreadChecker.checkIsOnValidThread();
        long presentationTimestampUs = (videoFrame.getTimestampNs() + 500L) / 1000L;
        try {
            index = this.codec.dequeueInputBuffer(0L);
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "dequeueInputBuffer failed", e);
            return VideoCodecStatus.ERROR;
        }
        if (index == -1) {
            Logging.d(TAG, "Dropped frame, no input buffers available");
            return VideoCodecStatus.NO_OUTPUT;
        }
        try {
            buffer = this.codec.getInputBuffers()[index];
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "getInputBuffers failed", e);
            return VideoCodecStatus.ERROR;
        }
        this.fillInputBuffer(buffer, videoFrameBuffer);
        try {
            this.codec.queueInputBuffer(index, 0, bufferSize, presentationTimestampUs, 0);
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "queueInputBuffer failed", e);
            return VideoCodecStatus.ERROR;
        }
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoCodecStatus setRateAllocation(VideoEncoder.BitrateAllocation bitrateAllocation, int framerate) {
        this.encodeThreadChecker.checkIsOnValidThread();
        if (framerate > 30) {
            framerate = 30;
        }
        this.bitrateAdjuster.setTargets(bitrateAllocation.getSum(), framerate);
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoEncoder.ScalingSettings getScalingSettings() {
        this.encodeThreadChecker.checkIsOnValidThread();
        if (this.automaticResizeOn) {
            if (this.codecType == VideoCodecMimeType.VP8) {
                int kLowVp8QpThreshold = 29;
                int kHighVp8QpThreshold = 95;
                return new VideoEncoder.ScalingSettings(29, 95);
            }
            if (this.codecType == VideoCodecMimeType.H264) {
                int kLowH264QpThreshold = 24;
                int kHighH264QpThreshold = 37;
                return new VideoEncoder.ScalingSettings(24, 37);
            }
        }
        return VideoEncoder.ScalingSettings.OFF;
    }

    @Override
    public String getImplementationName() {
        return "HWEncoder";
    }

    private VideoCodecStatus resetCodec(int newWidth, int newHeight, boolean newUseSurfaceMode) {
        this.encodeThreadChecker.checkIsOnValidThread();
        VideoCodecStatus status = this.release();
        if (status != VideoCodecStatus.OK) {
            return status;
        }
        this.width = newWidth;
        this.height = newHeight;
        this.useSurfaceMode = newUseSurfaceMode;
        return this.initEncodeInternal();
    }

    private boolean shouldForceKeyFrame(long presentationTimestampNs) {
        this.encodeThreadChecker.checkIsOnValidThread();
        return this.forcedKeyFrameNs > 0L && presentationTimestampNs > this.lastKeyFrameNs + this.forcedKeyFrameNs;
    }

    private void requestKeyFrame(long presentationTimestampNs) {
        this.encodeThreadChecker.checkIsOnValidThread();
        try {
            Bundle b = new Bundle();
            b.putInt("request-sync", 0);
            this.codec.setParameters(b);
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "requestKeyFrame failed", e);
            return;
        }
        this.lastKeyFrameNs = presentationTimestampNs;
    }

    private Thread createOutputThread() {
        return new Thread(){

            @Override
            public void run() {
                while (HardwareVideoEncoder.this.running) {
                    HardwareVideoEncoder.this.deliverEncodedImage();
                }
                HardwareVideoEncoder.this.releaseCodecOnOutputThread();
            }
        };
    }

    protected void deliverEncodedImage() {
        this.outputThreadChecker.checkIsOnValidThread();
        try {
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            int index = this.codec.dequeueOutputBuffer(info, 100000L);
            if (index < 0) {
                if (index == -3) {
                    this.outputBuffersBusyCount.waitForZero();
                    this.outputBuffers = this.codec.getOutputBuffers();
                }
                return;
            }
            ByteBuffer codecOutputBuffer = this.outputBuffers[index];
            codecOutputBuffer.position(info.offset);
            codecOutputBuffer.limit(info.offset + info.size);
            if ((info.flags & 2) != 0) {
                Logging.d(TAG, "Config frame generated. Offset: " + info.offset + ". Size: " + info.size);
                this.configBuffer = ByteBuffer.allocateDirect(info.size);
                this.configBuffer.put(codecOutputBuffer);
            } else {
                ByteBuffer frameBuffer;
                boolean isKeyFrame;
                this.bitrateAdjuster.reportEncodedFrame(info.size);
                if (this.adjustedBitrate != this.bitrateAdjuster.getAdjustedBitrateBps()) {
                    this.updateBitrate();
                }
                boolean bl = isKeyFrame = (info.flags & 1) != 0;
                if (isKeyFrame) {
                    Logging.d(TAG, "Sync frame generated");
                }
                if (isKeyFrame && this.codecType == VideoCodecMimeType.H264) {
                    Logging.d(TAG, "Prepending config frame of size " + this.configBuffer.capacity() + " to output buffer with offset " + info.offset + ", size " + info.size);
                    frameBuffer = ByteBuffer.allocateDirect(info.size + this.configBuffer.capacity());
                    this.configBuffer.rewind();
                    frameBuffer.put(this.configBuffer);
                    frameBuffer.put(codecOutputBuffer);
                    frameBuffer.rewind();
                } else {
                    frameBuffer = codecOutputBuffer.slice();
                }
                EncodedImage.FrameType frameType = isKeyFrame ? EncodedImage.FrameType.VideoFrameKey : EncodedImage.FrameType.VideoFrameDelta;
                this.outputBuffersBusyCount.increment();
                EncodedImage.Builder builder = this.outputBuilders.poll();
                EncodedImage encodedImage = builder.setBuffer(frameBuffer, () -> {
                    try {
                        this.codec.releaseOutputBuffer(index, false);
                    }
                    catch (Exception e) {
                        Logging.e(TAG, "releaseOutputBuffer failed", e);
                    }
                    this.outputBuffersBusyCount.decrement();
                }).setFrameType(frameType).createEncodedImage();
                this.callback.onEncodedFrame(encodedImage, new VideoEncoder.CodecSpecificInfo());
                encodedImage.release();
            }
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "deliverOutput failed", e);
        }
    }

    private void releaseCodecOnOutputThread() {
        this.outputThreadChecker.checkIsOnValidThread();
        Logging.d(TAG, "Releasing MediaCodec on output thread");
        this.outputBuffersBusyCount.waitForZero();
        try {
            this.codec.stop();
        }
        catch (Exception e) {
            Logging.e(TAG, "Media encoder stop failed", e);
        }
        try {
            this.codec.release();
        }
        catch (Exception e) {
            Logging.e(TAG, "Media encoder release failed", e);
            this.shutdownException = e;
        }
        this.configBuffer = null;
        Logging.d(TAG, "Release on output thread done");
    }

    private VideoCodecStatus updateBitrate() {
        this.outputThreadChecker.checkIsOnValidThread();
        this.adjustedBitrate = this.bitrateAdjuster.getAdjustedBitrateBps();
        try {
            Bundle params = new Bundle();
            params.putInt("video-bitrate", this.adjustedBitrate);
            this.codec.setParameters(params);
            return VideoCodecStatus.OK;
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "updateBitrate failed", e);
            return VideoCodecStatus.ERROR;
        }
    }

    private boolean canUseSurface() {
        return this.sharedContext != null && this.surfaceColorFormat != null;
    }

    protected void fillInputBuffer(ByteBuffer buffer, VideoFrame.Buffer videoFrameBuffer) {
        this.yuvFormat.fillBuffer(buffer, videoFrameBuffer);
    }

    private static enum YuvFormat {
        I420{

            @Override
            void fillBuffer(ByteBuffer dstBuffer, VideoFrame.Buffer srcBuffer) {
                VideoFrame.I420Buffer i420 = srcBuffer.toI420();
                YuvHelper.I420Copy(i420.getDataY(), i420.getStrideY(), i420.getDataU(), i420.getStrideU(), i420.getDataV(), i420.getStrideV(), dstBuffer, i420.getWidth(), i420.getHeight());
                i420.release();
            }
        }
        ,
        NV12{

            @Override
            void fillBuffer(ByteBuffer dstBuffer, VideoFrame.Buffer srcBuffer) {
                VideoFrame.I420Buffer i420 = srcBuffer.toI420();
                YuvHelper.I420ToNV12(i420.getDataY(), i420.getStrideY(), i420.getDataU(), i420.getStrideU(), i420.getDataV(), i420.getStrideV(), dstBuffer, i420.getWidth(), i420.getHeight());
                i420.release();
            }
        };


        abstract void fillBuffer(ByteBuffer var1, VideoFrame.Buffer var2);

        static YuvFormat valueOf(int colorFormat) {
            switch (colorFormat) {
                case 19: {
                    return I420;
                }
                case 21: 
                case 2141391872: 
                case 2141391876: {
                    return NV12;
                }
            }
            throw new IllegalArgumentException("Unsupported colorFormat: " + colorFormat);
        }
    }

    private static class BusyCount {
        private final Object countLock = new Object();
        private int count;

        private BusyCount() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void increment() {
            Object object = this.countLock;
            synchronized (object) {
                ++this.count;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void decrement() {
            Object object = this.countLock;
            synchronized (object) {
                --this.count;
                if (this.count == 0) {
                    this.countLock.notifyAll();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void waitForZero() {
            boolean wasInterrupted = false;
            Object object = this.countLock;
            synchronized (object) {
                while (this.count > 0) {
                    try {
                        this.countLock.wait();
                    }
                    catch (InterruptedException e) {
                        Logging.e(HardwareVideoEncoder.TAG, "Interrupted while waiting on busy count", e);
                        wasInterrupted = true;
                    }
                }
            }
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

