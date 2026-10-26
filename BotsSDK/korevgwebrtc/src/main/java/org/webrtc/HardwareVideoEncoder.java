/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec$BufferInfo
 *  android.media.MediaCodecInfo
 *  android.media.MediaCodecInfo$CodecCapabilities
 *  android.media.MediaFormat
 *  android.opengl.GLES20
 *  android.os.Bundle
 *  android.view.Surface
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.Surface;
import androidx.annotation.Nullable;
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

class HardwareVideoEncoder
implements VideoEncoder {
    private static final String TAG = "HardwareVideoEncoder";
    private static final int MAX_VIDEO_FRAMERATE = 30;
    private static final int MAX_ENCODER_Q_SIZE = 2;
    private static final int MEDIA_CODEC_RELEASE_TIMEOUT_MS = 5000;
    private static final int DEQUEUE_OUTPUT_BUFFER_TIMEOUT_US = 100000;
    private static final int REQUIRED_RESOLUTION_ALIGNMENT = 2;
    private final MediaCodecWrapperFactory mediaCodecWrapperFactory;
    private final String codecName;
    private final VideoCodecMimeType codecType;
    private final Integer surfaceColorFormat;
    private final Integer yuvColorFormat;
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
    private Thread outputThread;
    @Nullable
    private EglBase14 textureEglBase;
    @Nullable
    private Surface textureInputSurface;
    private int width;
    private int height;
    private int stride;
    private int sliceHeight;
    private boolean isSemiPlanar;
    private int frameSizeBytes;
    private boolean useSurfaceMode;
    private long nextPresentationTimestampUs;
    private long lastKeyFrameNs;
    @Nullable
    private ByteBuffer configBuffer;
    private int adjustedBitrate;
    private volatile boolean running;
    @Nullable
    private volatile Exception shutdownException;
    private boolean isEncodingStatisticsEnabled;

    public HardwareVideoEncoder(MediaCodecWrapperFactory mediaCodecWrapperFactory, String codecName, VideoCodecMimeType codecType, Integer surfaceColorFormat, Integer yuvColorFormat, Map<String, String> params, int keyFrameIntervalSec, int forceKeyFrameIntervalMs, BitrateAdjuster bitrateAdjuster, EglBase14.Context sharedContext) {
        this.mediaCodecWrapperFactory = mediaCodecWrapperFactory;
        this.codecName = codecName;
        this.codecType = codecType;
        this.surfaceColorFormat = surfaceColorFormat;
        this.yuvColorFormat = yuvColorFormat;
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
        if (settings.width % 2 != 0 || settings.height % 2 != 0) {
            Logging.e(TAG, "MediaCodec requires 2x2 alignment.");
            return VideoCodecStatus.ERR_SIZE;
        }
        this.width = settings.width;
        this.height = settings.height;
        this.useSurfaceMode = this.canUseSurface();
        if (settings.startBitrate != 0 && settings.maxFramerate != 0) {
            this.bitrateAdjuster.setTargets(settings.startBitrate * 1000, settings.maxFramerate);
        }
        this.adjustedBitrate = this.bitrateAdjuster.getAdjustedBitrateBps();
        Logging.d(TAG, "initEncode name: " + this.codecName + " type: " + this.codecType + " width: " + this.width + " height: " + this.height + " framerate_fps: " + settings.maxFramerate + " bitrate_kbps: " + settings.startBitrate + " surface mode: " + this.useSurfaceMode);
        return this.initEncodeInternal();
    }

    private VideoCodecStatus initEncodeInternal() {
        this.encodeThreadChecker.checkIsOnValidThread();
        this.nextPresentationTimestampUs = 0L;
        this.lastKeyFrameNs = -1L;
        this.isEncodingStatisticsEnabled = false;
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
            format.setInteger("bitrate-mode", 2);
            format.setInteger("color-format", colorFormat);
            format.setFloat("frame-rate", (float)this.bitrateAdjuster.getAdjustedFramerateFps());
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
            if (this.codecName.equals("c2.google.av1.encoder")) {
                format.setInteger("vendor.google-av1enc.encoding-preset.int32.value", 1);
            }
            if (this.isEncodingStatisticsSupported()) {
                format.setInteger("video-encoding-statistics-level", 1);
                this.isEncodingStatisticsEnabled = true;
            }
            Logging.d(TAG, "Format: " + format);
            this.codec.configure(format, null, null, 1);
            if (this.useSurfaceMode) {
                this.textureEglBase = EglBase.createEgl14(this.sharedContext, EglBase.CONFIG_RECORDABLE);
                this.textureInputSurface = this.codec.createInputSurface();
                this.textureEglBase.createSurface(this.textureInputSurface);
                this.textureEglBase.makeCurrent();
            }
            this.updateInputFormat(this.codec.getInputFormat());
            this.codec.start();
        }
        catch (IllegalArgumentException | IllegalStateException e) {
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
        boolean isTextureBuffer = videoFrame.getBuffer() instanceof VideoFrame.TextureBuffer;
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
        EncodedImage.Builder builder = EncodedImage.builder().setCaptureTimeNs(videoFrame.getTimestampNs()).setEncodedWidth(videoFrame.getBuffer().getWidth()).setEncodedHeight(videoFrame.getBuffer().getHeight()).setRotation(videoFrame.getRotation());
        this.outputBuilders.offer(builder);
        long presentationTimestampUs = this.nextPresentationTimestampUs;
        long frameDurationUs = (long)((double)TimeUnit.SECONDS.toMicros(1L) / this.bitrateAdjuster.getAdjustedFramerateFps());
        this.nextPresentationTimestampUs += frameDurationUs;
        VideoCodecStatus returnValue = this.useSurfaceMode ? this.encodeTextureBuffer(videoFrame, presentationTimestampUs) : this.encodeByteBuffer(videoFrame, presentationTimestampUs);
        if (returnValue != VideoCodecStatus.OK) {
            this.outputBuilders.pollLast();
        }
        return returnValue;
    }

    private VideoCodecStatus encodeTextureBuffer(VideoFrame videoFrame, long presentationTimestampUs) {
        this.encodeThreadChecker.checkIsOnValidThread();
        try {
            GLES20.glClear((int)16384);
            VideoFrame derotatedFrame = new VideoFrame(videoFrame.getBuffer(), 0, videoFrame.getTimestampNs());
            this.videoFrameDrawer.drawFrame(derotatedFrame, this.textureDrawer, null);
            this.textureEglBase.swapBuffers(TimeUnit.MICROSECONDS.toNanos(presentationTimestampUs));
        }
        catch (RuntimeException e) {
            Logging.e(TAG, "encodeTexture failed", e);
            return VideoCodecStatus.ERROR;
        }
        return VideoCodecStatus.OK;
    }

    private VideoCodecStatus encodeByteBuffer(VideoFrame videoFrame, long presentationTimestampUs) {
        ByteBuffer buffer;
        int index;
        this.encodeThreadChecker.checkIsOnValidThread();
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
            buffer = this.codec.getInputBuffer(index);
        }
        catch (IllegalStateException e) {
            Logging.e(TAG, "getInputBuffer with index=" + index + " failed", e);
            return VideoCodecStatus.ERROR;
        }
        if (buffer.capacity() < this.frameSizeBytes) {
            Logging.e(TAG, "Input buffer size: " + buffer.capacity() + " is smaller than frame size: " + this.frameSizeBytes);
            return VideoCodecStatus.ERROR;
        }
        this.fillInputBuffer(buffer, videoFrame.getBuffer());
        try {
            this.codec.queueInputBuffer(index, 0, this.frameSizeBytes, presentationTimestampUs, 0);
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
    public VideoCodecStatus setRates(VideoEncoder.RateControlParameters rcParameters) {
        this.encodeThreadChecker.checkIsOnValidThread();
        this.bitrateAdjuster.setTargets(rcParameters.bitrate.getSum(), rcParameters.framerateFps);
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoEncoder.ScalingSettings getScalingSettings() {
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
        return this.codecName;
    }

    @Override
    public VideoEncoder.EncoderInfo getEncoderInfo() {
        return new VideoEncoder.EncoderInfo(2, false);
    }

    private VideoCodecStatus resetCodec(int newWidth, int newHeight, boolean newUseSurfaceMode) {
        this.encodeThreadChecker.checkIsOnValidThread();
        VideoCodecStatus status = this.release();
        if (status != VideoCodecStatus.OK) {
            return status;
        }
        if (newWidth % 2 != 0 || newHeight % 2 != 0) {
            Logging.e(TAG, "MediaCodec requires 2x2 alignment.");
            return VideoCodecStatus.ERR_SIZE;
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
            Runnable releaseCallback;
            ByteBuffer frameBuffer;
            MediaFormat format;
            boolean isKeyFrame;
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            int index = this.codec.dequeueOutputBuffer(info, 100000L);
            if (index < 0) {
                if (index == -3) {
                    this.outputBuffersBusyCount.waitForZero();
                }
                return;
            }
            ByteBuffer outputBuffer = this.codec.getOutputBuffer(index);
            outputBuffer.position(info.offset);
            outputBuffer.limit(info.offset + info.size);
            if ((info.flags & 2) != 0) {
                Logging.d(TAG, "Config frame generated. Offset: " + info.offset + ". Size: " + info.size);
                if (info.size > 0 && (this.codecType == VideoCodecMimeType.H264 || this.codecType == VideoCodecMimeType.H265)) {
                    this.configBuffer = ByteBuffer.allocateDirect(info.size);
                    this.configBuffer.put(outputBuffer);
                }
                return;
            }
            this.bitrateAdjuster.reportEncodedFrame(info.size);
            if (this.adjustedBitrate != this.bitrateAdjuster.getAdjustedBitrateBps()) {
                this.updateBitrate();
            }
            boolean bl = isKeyFrame = (info.flags & 1) != 0;
            if (isKeyFrame) {
                Logging.d(TAG, "Sync frame generated");
            }
            Integer qp = null;
            if (this.isEncodingStatisticsEnabled && (format = this.codec.getOutputFormat(index)) != null && format.containsKey("video-qp-average")) {
                qp = format.getInteger("video-qp-average");
            }
            if (isKeyFrame && this.configBuffer != null) {
                Logging.d(TAG, "Prepending config buffer of size " + this.configBuffer.capacity() + " to output buffer with offset " + info.offset + ", size " + info.size);
                frameBuffer = ByteBuffer.allocateDirect(info.size + this.configBuffer.capacity());
                this.configBuffer.rewind();
                frameBuffer.put(this.configBuffer);
                frameBuffer.put(outputBuffer);
                frameBuffer.rewind();
                this.codec.releaseOutputBuffer(index, false);
                releaseCallback = null;
            } else {
                frameBuffer = outputBuffer.slice();
                this.outputBuffersBusyCount.increment();
                releaseCallback = () -> {
                    try {
                        this.codec.releaseOutputBuffer(index, false);
                    }
                    catch (Exception e) {
                        Logging.e(TAG, "releaseOutputBuffer failed", e);
                    }
                    this.outputBuffersBusyCount.decrement();
                };
            }
            EncodedImage.FrameType frameType = isKeyFrame ? EncodedImage.FrameType.VideoFrameKey : EncodedImage.FrameType.VideoFrameDelta;
            EncodedImage.Builder builder = this.outputBuilders.poll();
            builder.setBuffer(frameBuffer, releaseCallback);
            builder.setFrameType(frameType);
            builder.setQp(qp);
            EncodedImage encodedImage = builder.createEncodedImage();
            this.callback.onEncodedFrame(encodedImage, new VideoEncoder.CodecSpecificInfo());
            encodedImage.release();
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

    private void updateInputFormat(MediaFormat format) {
        this.stride = this.width;
        this.sliceHeight = this.height;
        if (format != null) {
            if (format.containsKey("stride")) {
                this.stride = format.getInteger("stride");
                this.stride = Math.max(this.stride, this.width);
            }
            if (format.containsKey("slice-height")) {
                this.sliceHeight = format.getInteger("slice-height");
                this.sliceHeight = Math.max(this.sliceHeight, this.height);
            }
        }
        this.isSemiPlanar = this.isSemiPlanar(this.yuvColorFormat);
        if (this.isSemiPlanar) {
            int chromaHeight = (this.height + 1) / 2;
            this.frameSizeBytes = this.sliceHeight * this.stride + chromaHeight * this.stride;
        } else {
            int chromaStride = (this.stride + 1) / 2;
            int chromaSliceHeight = (this.sliceHeight + 1) / 2;
            this.frameSizeBytes = this.sliceHeight * this.stride + chromaSliceHeight * chromaStride * 2;
        }
        Logging.d(TAG, "updateInputFormat format: " + format + " stride: " + this.stride + " sliceHeight: " + this.sliceHeight + " isSemiPlanar: " + this.isSemiPlanar + " frameSizeBytes: " + this.frameSizeBytes);
    }

    protected boolean isEncodingStatisticsSupported() {
        if (this.codecType == VideoCodecMimeType.VP8 || this.codecType == VideoCodecMimeType.VP9) {
            return false;
        }
        MediaCodecInfo codecInfo = this.codec.getCodecInfo();
        if (codecInfo == null) {
            return false;
        }
        MediaCodecInfo.CodecCapabilities codecCaps = codecInfo.getCapabilitiesForType(this.codecType.mimeType());
        if (codecCaps == null) {
            return false;
        }
        return codecCaps.isFeatureSupported("encoding-statistics");
    }

    protected void fillInputBuffer(ByteBuffer buffer, VideoFrame.Buffer frame) {
        VideoFrame.I420Buffer i420 = frame.toI420();
        if (this.isSemiPlanar) {
            YuvHelper.I420ToNV12(i420.getDataY(), i420.getStrideY(), i420.getDataU(), i420.getStrideU(), i420.getDataV(), i420.getStrideV(), buffer, i420.getWidth(), i420.getHeight(), this.stride, this.sliceHeight);
        } else {
            YuvHelper.I420Copy(i420.getDataY(), i420.getStrideY(), i420.getDataU(), i420.getStrideU(), i420.getDataV(), i420.getStrideV(), buffer, i420.getWidth(), i420.getHeight(), this.stride, this.sliceHeight);
        }
        i420.release();
    }

    protected boolean isSemiPlanar(int colorFormat) {
        switch (colorFormat) {
            case 19: {
                return false;
            }
            case 21: 
            case 2141391872: 
            case 2141391876: {
                return true;
            }
        }
        throw new IllegalArgumentException("Unsupported colorFormat: " + colorFormat);
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

