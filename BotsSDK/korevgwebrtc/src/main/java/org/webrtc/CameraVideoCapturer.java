/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaRecorder
 */
package org.webrtc;

import android.media.MediaRecorder;
import org.webrtc.Logging;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;

public interface CameraVideoCapturer
extends VideoCapturer {
    public void switchCamera(CameraSwitchHandler var1);

    public void switchCamera(CameraSwitchHandler var1, String var2);

    @Deprecated
    default public void addMediaRecorderToCamera(MediaRecorder mediaRecorder, MediaRecorderHandler resultHandler) {
        throw new UnsupportedOperationException("Deprecated and not implemented.");
    }

    @Deprecated
    default public void removeMediaRecorderFromCamera(MediaRecorderHandler resultHandler) {
        throw new UnsupportedOperationException("Deprecated and not implemented.");
    }

    public static class CameraStatistics {
        private static final String TAG = "CameraStatistics";
        private static final int CAMERA_OBSERVER_PERIOD_MS = 2000;
        private static final int CAMERA_FREEZE_REPORT_TIMOUT_MS = 4000;
        private final SurfaceTextureHelper surfaceTextureHelper;
        private final CameraEventsHandler eventsHandler;
        private int frameCount;
        private int freezePeriodCount;
        private final Runnable cameraObserver = new Runnable(){

            @Override
            public void run() {
                int cameraFps = Math.round((float)frameCount * 1000.0f / 2000.0f);
                Logging.d(CameraStatistics.TAG, "Camera fps: " + cameraFps + ".");
                if (frameCount == 0) {
                    ++freezePeriodCount;
                    if (2000 * freezePeriodCount >= 4000 && eventsHandler != null) {
                        Logging.e(CameraStatistics.TAG, "Camera freezed.");
                        if (surfaceTextureHelper.isTextureInUse()) {
                            eventsHandler.onCameraFreezed("Camera failure. Client must return video buffers.");
                        } else {
                            eventsHandler.onCameraFreezed("Camera failure.");
                        }
                        return;
                    }
                } else {
                    freezePeriodCount = 0;
                }
                frameCount = 0;
                surfaceTextureHelper.getHandler().postDelayed((Runnable)this, 2000L);
            }
        };

        public CameraStatistics(SurfaceTextureHelper surfaceTextureHelper, CameraEventsHandler eventsHandler) {
            if (surfaceTextureHelper == null) {
                throw new IllegalArgumentException("SurfaceTextureHelper is null");
            }
            this.surfaceTextureHelper = surfaceTextureHelper;
            this.eventsHandler = eventsHandler;
            this.frameCount = 0;
            this.freezePeriodCount = 0;
            surfaceTextureHelper.getHandler().postDelayed(this.cameraObserver, 2000L);
        }

        private void checkThread() {
            if (Thread.currentThread() != this.surfaceTextureHelper.getHandler().getLooper().getThread()) {
                throw new IllegalStateException("Wrong thread");
            }
        }

        public void addFrame() {
            this.checkThread();
            ++this.frameCount;
        }

        public void release() {
            this.surfaceTextureHelper.getHandler().removeCallbacks(this.cameraObserver);
        }
    }

    @Deprecated
    public static interface MediaRecorderHandler {
        public void onMediaRecorderSuccess();

        public void onMediaRecorderError(String var1);
    }

    public static interface CameraSwitchHandler {
        public void onCameraSwitchDone(boolean var1);

        public void onCameraSwitchError(String var1);
    }

    public static interface CameraEventsHandler {
        public void onCameraError(String var1);

        public void onCameraDisconnected();

        public void onCameraFreezed(String var1);

        public void onCameraOpening(String var1);

        public void onFirstFrameAvailable();

        public void onCameraClosed();
    }
}

