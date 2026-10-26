/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Looper
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraSession;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.CapturerObserver;
import org.webrtc.Logging;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoFrame;

abstract class CameraCapturer
implements CameraVideoCapturer {
    private static final String TAG = "CameraCapturer";
    private static final int MAX_OPEN_CAMERA_ATTEMPTS = 3;
    private static final int OPEN_CAMERA_DELAY_MS = 500;
    private static final int OPEN_CAMERA_TIMEOUT = 10000;
    private final CameraEnumerator cameraEnumerator;
    private final CameraVideoCapturer.CameraEventsHandler eventsHandler;
    private final Handler uiThreadHandler;
    @Nullable
    private final CameraSession.CreateSessionCallback createSessionCallback = new CameraSession.CreateSessionCallback(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onDone(CameraSession session) {
            CameraCapturer.this.checkIsOnCameraThread();
            Logging.d(CameraCapturer.TAG, "Create session done. Switch state: " + (Object)((Object)CameraCapturer.this.switchState));
            CameraCapturer.this.uiThreadHandler.removeCallbacks(CameraCapturer.this.openCameraTimeoutRunnable);
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                CameraCapturer.this.capturerObserver.onCapturerStarted(true);
                CameraCapturer.this.sessionOpening = false;
                CameraCapturer.this.currentSession = session;
                CameraCapturer.this.cameraStatistics = new CameraVideoCapturer.CameraStatistics(CameraCapturer.this.surfaceHelper, CameraCapturer.this.eventsHandler);
                CameraCapturer.this.firstFrameObserved = false;
                CameraCapturer.this.stateLock.notifyAll();
                if (CameraCapturer.this.switchState == SwitchState.IN_PROGRESS) {
                    CameraCapturer.this.switchState = SwitchState.IDLE;
                    if (CameraCapturer.this.switchEventsHandler != null) {
                        CameraCapturer.this.switchEventsHandler.onCameraSwitchDone(CameraCapturer.this.cameraEnumerator.isFrontFacing(CameraCapturer.this.cameraName));
                        CameraCapturer.this.switchEventsHandler = null;
                    }
                } else if (CameraCapturer.this.switchState == SwitchState.PENDING) {
                    String selectedCameraName = CameraCapturer.this.pendingCameraName;
                    CameraCapturer.this.pendingCameraName = null;
                    CameraCapturer.this.switchState = SwitchState.IDLE;
                    CameraCapturer.this.switchCameraInternal(CameraCapturer.this.switchEventsHandler, selectedCameraName);
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onFailure(CameraSession.FailureType failureType, String error) {
            CameraCapturer.this.checkIsOnCameraThread();
            CameraCapturer.this.uiThreadHandler.removeCallbacks(CameraCapturer.this.openCameraTimeoutRunnable);
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                CameraCapturer.this.capturerObserver.onCapturerStarted(false);
                CameraCapturer.this.openAttemptsRemaining--;
                if (CameraCapturer.this.openAttemptsRemaining <= 0) {
                    Logging.w(CameraCapturer.TAG, "Opening camera failed, passing: " + error);
                    CameraCapturer.this.sessionOpening = false;
                    CameraCapturer.this.stateLock.notifyAll();
                    if (CameraCapturer.this.switchState != SwitchState.IDLE) {
                        if (CameraCapturer.this.switchEventsHandler != null) {
                            CameraCapturer.this.switchEventsHandler.onCameraSwitchError(error);
                            CameraCapturer.this.switchEventsHandler = null;
                        }
                        CameraCapturer.this.switchState = SwitchState.IDLE;
                    }
                    if (failureType == CameraSession.FailureType.DISCONNECTED) {
                        CameraCapturer.this.eventsHandler.onCameraDisconnected();
                    } else {
                        CameraCapturer.this.eventsHandler.onCameraError(error);
                    }
                } else {
                    Logging.w(CameraCapturer.TAG, "Opening camera failed, retry: " + error);
                    CameraCapturer.this.createSessionInternal(500);
                }
            }
        }
    };
    @Nullable
    private final CameraSession.Events cameraSessionEventsHandler = new CameraSession.Events(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onCameraOpening() {
            CameraCapturer.this.checkIsOnCameraThread();
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                if (CameraCapturer.this.currentSession != null) {
                    Logging.w(CameraCapturer.TAG, "onCameraOpening while session was open.");
                    return;
                }
                CameraCapturer.this.eventsHandler.onCameraOpening(CameraCapturer.this.cameraName);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onCameraError(CameraSession session, String error) {
            CameraCapturer.this.checkIsOnCameraThread();
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                if (session != CameraCapturer.this.currentSession) {
                    Logging.w(CameraCapturer.TAG, "onCameraError from another session: " + error);
                    return;
                }
                CameraCapturer.this.eventsHandler.onCameraError(error);
                CameraCapturer.this.stopCapture();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onCameraDisconnected(CameraSession session) {
            CameraCapturer.this.checkIsOnCameraThread();
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                if (session != CameraCapturer.this.currentSession) {
                    Logging.w(CameraCapturer.TAG, "onCameraDisconnected from another session.");
                    return;
                }
                CameraCapturer.this.eventsHandler.onCameraDisconnected();
                CameraCapturer.this.stopCapture();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onCameraClosed(CameraSession session) {
            CameraCapturer.this.checkIsOnCameraThread();
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                if (session != CameraCapturer.this.currentSession && CameraCapturer.this.currentSession != null) {
                    Logging.d(CameraCapturer.TAG, "onCameraClosed from another session.");
                    return;
                }
                CameraCapturer.this.eventsHandler.onCameraClosed();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onFrameCaptured(CameraSession session, VideoFrame frame) {
            CameraCapturer.this.checkIsOnCameraThread();
            Object object = CameraCapturer.this.stateLock;
            synchronized (object) {
                if (session != CameraCapturer.this.currentSession) {
                    Logging.w(CameraCapturer.TAG, "onFrameCaptured from another session.");
                    return;
                }
                if (!CameraCapturer.this.firstFrameObserved) {
                    CameraCapturer.this.eventsHandler.onFirstFrameAvailable();
                    CameraCapturer.this.firstFrameObserved = true;
                }
                CameraCapturer.this.cameraStatistics.addFrame();
                CameraCapturer.this.capturerObserver.onFrameCaptured(frame);
            }
        }
    };
    private final Runnable openCameraTimeoutRunnable = new Runnable(){

        @Override
        public void run() {
            CameraCapturer.this.eventsHandler.onCameraError("Camera failed to start within timeout.");
        }
    };
    private Handler cameraThreadHandler;
    private Context applicationContext;
    private CapturerObserver capturerObserver;
    private SurfaceTextureHelper surfaceHelper;
    private final Object stateLock = new Object();
    private boolean sessionOpening;
    @Nullable
    private CameraSession currentSession;
    private String cameraName;
    private String pendingCameraName;
    private int width;
    private int height;
    private int framerate;
    private int openAttemptsRemaining;
    private SwitchState switchState = SwitchState.IDLE;
    @Nullable
    private CameraVideoCapturer.CameraSwitchHandler switchEventsHandler;
    @Nullable
    private CameraVideoCapturer.CameraStatistics cameraStatistics;
    private boolean firstFrameObserved;

    public CameraCapturer(String cameraName, @Nullable CameraVideoCapturer.CameraEventsHandler eventsHandler, CameraEnumerator cameraEnumerator) {
        if (eventsHandler == null) {
            eventsHandler = new CameraVideoCapturer.CameraEventsHandler(){

                @Override
                public void onCameraError(String errorDescription) {
                }

                @Override
                public void onCameraDisconnected() {
                }

                @Override
                public void onCameraFreezed(String errorDescription) {
                }

                @Override
                public void onCameraOpening(String cameraName) {
                }

                @Override
                public void onFirstFrameAvailable() {
                }

                @Override
                public void onCameraClosed() {
                }
            };
        }
        this.eventsHandler = eventsHandler;
        this.cameraEnumerator = cameraEnumerator;
        this.cameraName = cameraName;
        List<String> deviceNames = Arrays.asList(cameraEnumerator.getDeviceNames());
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
        if (deviceNames.isEmpty()) {
            throw new RuntimeException("No cameras attached.");
        }
        if (!deviceNames.contains(this.cameraName)) {
            throw new IllegalArgumentException("Camera name " + this.cameraName + " does not match any known camera device.");
        }
    }

    @Override
    public void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext, CapturerObserver capturerObserver) {
        this.applicationContext = applicationContext;
        this.capturerObserver = capturerObserver;
        this.surfaceHelper = surfaceTextureHelper;
        this.cameraThreadHandler = surfaceTextureHelper.getHandler();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void startCapture(int width, int height, int framerate) {
        Logging.d(TAG, "startCapture: " + width + "x" + height + "@" + framerate);
        if (this.applicationContext == null) {
            throw new RuntimeException("CameraCapturer must be initialized before calling startCapture.");
        }
        Object object = this.stateLock;
        synchronized (object) {
            if (this.sessionOpening || this.currentSession != null) {
                Logging.w(TAG, "Session already open");
                return;
            }
            this.width = width;
            this.height = height;
            this.framerate = framerate;
            this.sessionOpening = true;
            this.openAttemptsRemaining = 3;
            this.createSessionInternal(0);
        }
    }

    private void createSessionInternal(int delayMs) {
        this.uiThreadHandler.postDelayed(this.openCameraTimeoutRunnable, (long)(delayMs + 10000));
        this.cameraThreadHandler.postDelayed(new Runnable(){

            @Override
            public void run() {
                CameraCapturer.this.createCameraSession(CameraCapturer.this.createSessionCallback, CameraCapturer.this.cameraSessionEventsHandler, CameraCapturer.this.applicationContext, CameraCapturer.this.surfaceHelper, CameraCapturer.this.cameraName, CameraCapturer.this.width, CameraCapturer.this.height, CameraCapturer.this.framerate);
            }
        }, (long)delayMs);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void stopCapture() {
        Logging.d(TAG, "Stop capture");
        Object object = this.stateLock;
        synchronized (object) {
            while (this.sessionOpening) {
                Logging.d(TAG, "Stop capture: Waiting for session to open");
                try {
                    this.stateLock.wait();
                }
                catch (InterruptedException e) {
                    Logging.w(TAG, "Stop capture interrupted while waiting for the session to open.");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            if (this.currentSession != null) {
                Logging.d(TAG, "Stop capture: Nulling session");
                this.cameraStatistics.release();
                this.cameraStatistics = null;
                final CameraSession oldSession = this.currentSession;
                this.cameraThreadHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        oldSession.stop();
                    }
                });
                this.currentSession = null;
                this.capturerObserver.onCapturerStopped();
            } else {
                Logging.d(TAG, "Stop capture: No session open");
            }
        }
        Logging.d(TAG, "Stop capture done");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void changeCaptureFormat(int width, int height, int framerate) {
        Logging.d(TAG, "changeCaptureFormat: " + width + "x" + height + "@" + framerate);
        Object object = this.stateLock;
        synchronized (object) {
            this.stopCapture();
            this.startCapture(width, height, framerate);
        }
    }

    @Override
    public void dispose() {
        Logging.d(TAG, "dispose");
        this.stopCapture();
    }

    @Override
    public void switchCamera(final CameraVideoCapturer.CameraSwitchHandler switchEventsHandler) {
        Logging.d(TAG, "switchCamera");
        this.cameraThreadHandler.post(new Runnable(){

            @Override
            public void run() {
                List<String> deviceNames = Arrays.asList(CameraCapturer.this.cameraEnumerator.getDeviceNames());
                if (deviceNames.size() < 2) {
                    CameraCapturer.this.reportCameraSwitchError("No camera to switch to.", switchEventsHandler);
                    return;
                }
                int cameraNameIndex = deviceNames.indexOf(CameraCapturer.this.cameraName);
                String cameraName = deviceNames.get((cameraNameIndex + 1) % deviceNames.size());
                CameraCapturer.this.switchCameraInternal(switchEventsHandler, cameraName);
            }
        });
    }

    @Override
    public void switchCamera(final CameraVideoCapturer.CameraSwitchHandler switchEventsHandler, final String cameraName) {
        Logging.d(TAG, "switchCamera");
        this.cameraThreadHandler.post(new Runnable(){

            @Override
            public void run() {
                CameraCapturer.this.switchCameraInternal(switchEventsHandler, cameraName);
            }
        });
    }

    @Override
    public boolean isScreencast() {
        return false;
    }

    public void printStackTrace() {
        StackTraceElement[] cameraStackTrace;
        Thread cameraThread = null;
        if (this.cameraThreadHandler != null) {
            cameraThread = this.cameraThreadHandler.getLooper().getThread();
        }
        if (cameraThread != null && (cameraStackTrace = cameraThread.getStackTrace()).length > 0) {
            Logging.d(TAG, "CameraCapturer stack trace:");
            for (StackTraceElement traceElem : cameraStackTrace) {
                Logging.d(TAG, traceElem.toString());
            }
        }
    }

    private void reportCameraSwitchError(String error, @Nullable CameraVideoCapturer.CameraSwitchHandler switchEventsHandler) {
        Logging.e(TAG, error);
        if (switchEventsHandler != null) {
            switchEventsHandler.onCameraSwitchError(error);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void switchCameraInternal(@Nullable CameraVideoCapturer.CameraSwitchHandler switchEventsHandler, String selectedCameraName) {
        Logging.d(TAG, "switchCamera internal");
        List<String> deviceNames = Arrays.asList(this.cameraEnumerator.getDeviceNames());
        if (!deviceNames.contains(selectedCameraName)) {
            this.reportCameraSwitchError("Attempted to switch to unknown camera device " + selectedCameraName, switchEventsHandler);
            return;
        }
        Object object = this.stateLock;
        synchronized (object) {
            if (this.switchState != SwitchState.IDLE) {
                this.reportCameraSwitchError("Camera switch already in progress.", switchEventsHandler);
                return;
            }
            if (!this.sessionOpening && this.currentSession == null) {
                this.reportCameraSwitchError("switchCamera: camera is not running.", switchEventsHandler);
                return;
            }
            this.switchEventsHandler = switchEventsHandler;
            if (this.sessionOpening) {
                this.switchState = SwitchState.PENDING;
                this.pendingCameraName = selectedCameraName;
                return;
            }
            this.switchState = SwitchState.IN_PROGRESS;
            Logging.d(TAG, "switchCamera: Stopping session");
            this.cameraStatistics.release();
            this.cameraStatistics = null;
            final CameraSession oldSession = this.currentSession;
            this.cameraThreadHandler.post(new Runnable(){

                @Override
                public void run() {
                    oldSession.stop();
                }
            });
            this.currentSession = null;
            this.cameraName = selectedCameraName;
            this.sessionOpening = true;
            this.openAttemptsRemaining = 1;
            this.createSessionInternal(0);
        }
        Logging.d(TAG, "switchCamera done");
    }

    private void checkIsOnCameraThread() {
        if (Thread.currentThread() != this.cameraThreadHandler.getLooper().getThread()) {
            Logging.e(TAG, "Check is on camera thread failed.");
            throw new RuntimeException("Not on camera thread.");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected String getCameraName() {
        Object object = this.stateLock;
        synchronized (object) {
            return this.cameraName;
        }
    }

    protected abstract void createCameraSession(CameraSession.CreateSessionCallback var1, CameraSession.Events var2, Context var3, SurfaceTextureHelper var4, String var5, int var6, int var7, int var8);

    static enum SwitchState {
        IDLE,
        PENDING,
        IN_PROGRESS;

    }
}

