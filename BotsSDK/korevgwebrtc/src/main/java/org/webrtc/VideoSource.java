/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CapturerObserver;
import org.webrtc.MediaSource;
import org.webrtc.NativeAndroidVideoTrackSource;
import org.webrtc.VideoFrame;
import org.webrtc.VideoProcessor;

public class VideoSource
extends MediaSource {
    private final NativeAndroidVideoTrackSource nativeAndroidVideoTrackSource;
    private final Object videoProcessorLock = new Object();
    @Nullable
    private VideoProcessor videoProcessor;
    private boolean isCapturerRunning;
    private final CapturerObserver capturerObserver = new CapturerObserver(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onCapturerStarted(boolean success) {
            VideoSource.this.nativeAndroidVideoTrackSource.setState(success);
            Object object = VideoSource.this.videoProcessorLock;
            synchronized (object) {
                VideoSource.this.isCapturerRunning = success;
                if (VideoSource.this.videoProcessor != null) {
                    VideoSource.this.videoProcessor.onCapturerStarted(success);
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onCapturerStopped() {
            VideoSource.this.nativeAndroidVideoTrackSource.setState(false);
            Object object = VideoSource.this.videoProcessorLock;
            synchronized (object) {
                VideoSource.this.isCapturerRunning = false;
                if (VideoSource.this.videoProcessor != null) {
                    VideoSource.this.videoProcessor.onCapturerStopped();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onFrameCaptured(VideoFrame frame) {
            VideoProcessor.FrameAdaptationParameters parameters = VideoSource.this.nativeAndroidVideoTrackSource.adaptFrame(frame);
            Object object = VideoSource.this.videoProcessorLock;
            synchronized (object) {
                if (VideoSource.this.videoProcessor != null) {
                    VideoSource.this.videoProcessor.onFrameCaptured(frame, parameters);
                    return;
                }
            }
            VideoFrame adaptedFrame = VideoProcessor.applyFrameAdaptationParameters(frame, parameters);
            if (adaptedFrame != null) {
                VideoSource.this.nativeAndroidVideoTrackSource.onFrameCaptured(adaptedFrame);
                adaptedFrame.release();
            }
        }
    };

    public VideoSource(long nativeSource) {
        super(nativeSource);
        this.nativeAndroidVideoTrackSource = new NativeAndroidVideoTrackSource(nativeSource);
    }

    public void adaptOutputFormat(int width, int height, int fps) {
        int maxSide = Math.max(width, height);
        int minSide = Math.min(width, height);
        this.adaptOutputFormat(maxSide, minSide, minSide, maxSide, fps);
    }

    public void adaptOutputFormat(int landscapeWidth, int landscapeHeight, int portraitWidth, int portraitHeight, int fps) {
        this.adaptOutputFormat(new AspectRatio(landscapeWidth, landscapeHeight), (Integer)(landscapeWidth * landscapeHeight), new AspectRatio(portraitWidth, portraitHeight), (Integer)(portraitWidth * portraitHeight), (Integer)fps);
    }

    public void adaptOutputFormat(AspectRatio targetLandscapeAspectRatio, @Nullable Integer maxLandscapePixelCount, AspectRatio targetPortraitAspectRatio, @Nullable Integer maxPortraitPixelCount, @Nullable Integer maxFps) {
        this.nativeAndroidVideoTrackSource.adaptOutputFormat(targetLandscapeAspectRatio, maxLandscapePixelCount, targetPortraitAspectRatio, maxPortraitPixelCount, maxFps);
    }

    public void setIsScreencast(boolean isScreencast) {
        this.nativeAndroidVideoTrackSource.setIsScreencast(isScreencast);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setVideoProcessor(@Nullable VideoProcessor newVideoProcessor) {
        Object object = this.videoProcessorLock;
        synchronized (object) {
            if (this.videoProcessor != null) {
                this.videoProcessor.setSink(null);
                if (this.isCapturerRunning) {
                    this.videoProcessor.onCapturerStopped();
                }
            }
            this.videoProcessor = newVideoProcessor;
            if (newVideoProcessor != null) {
                newVideoProcessor.setSink(frame -> this.runWithReference(() -> this.nativeAndroidVideoTrackSource.onFrameCaptured(frame)));
                if (this.isCapturerRunning) {
                    newVideoProcessor.onCapturerStarted(true);
                }
            }
        }
    }

    public CapturerObserver getCapturerObserver() {
        return this.capturerObserver;
    }

    long getNativeVideoTrackSource() {
        return this.getNativeMediaSource();
    }

    @Override
    public void dispose() {
        this.setVideoProcessor(null);
        super.dispose();
    }

    public static class AspectRatio {
        public static final AspectRatio UNDEFINED = new AspectRatio(0, 0);
        public final int width;
        public final int height;

        public AspectRatio(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}

