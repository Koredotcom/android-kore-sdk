/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.SurfaceTexture
 *  android.opengl.GLException
 *  android.view.Surface
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 *  androidx.annotation.Nullable
 *  javax.microedition.khronos.egl.EGL10
 *  javax.microedition.khronos.egl.EGLConfig
 *  javax.microedition.khronos.egl.EGLContext
 *  javax.microedition.khronos.egl.EGLDisplay
 *  javax.microedition.khronos.egl.EGLSurface
 */
package org.webrtc;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLException;
import android.view.Surface;
import android.view.SurfaceHolder;
import androidx.annotation.Nullable;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import org.webrtc.EglBase;
import org.webrtc.EglBase10;
import org.webrtc.Logging;
import org.webrtc.RefCountDelegate;

class EglBase10Impl
implements EglBase10 {
    private static final String TAG = "EglBase10Impl";
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static final EglConnection EGL_NO_CONNECTION = new EglConnection();
    private EGLSurface eglSurface = EGL10.EGL_NO_SURFACE;
    private EglConnection eglConnection;

    public EglBase10Impl(EGLContext sharedContext, int[] configAttributes) {
        this.eglConnection = new EglConnection(sharedContext, configAttributes);
    }

    public EglBase10Impl(EglConnection eglConnection) {
        this.eglConnection = eglConnection;
        this.eglConnection.retain();
    }

    @Override
    public void createSurface(Surface surface) {
        class FakeSurfaceHolder
        implements SurfaceHolder {
            private final Surface surface;

            FakeSurfaceHolder(Surface surface) {
                this.surface = surface;
            }

            public void addCallback(SurfaceHolder.Callback callback) {
            }

            public void removeCallback(SurfaceHolder.Callback callback) {
            }

            public boolean isCreating() {
                return false;
            }

            @Deprecated
            public void setType(int i) {
            }

            public void setFixedSize(int i, int i2) {
            }

            public void setSizeFromLayout() {
            }

            public void setFormat(int i) {
            }

            public void setKeepScreenOn(boolean b) {
            }

            @Nullable
            public Canvas lockCanvas() {
                return null;
            }

            @Nullable
            public Canvas lockCanvas(Rect rect) {
                return null;
            }

            public void unlockCanvasAndPost(Canvas canvas) {
            }

            @Nullable
            public Rect getSurfaceFrame() {
                return null;
            }

            public Surface getSurface() {
                return this.surface;
            }
        }
        this.createSurfaceInternal(new FakeSurfaceHolder(surface));
    }

    @Override
    public void createSurface(SurfaceTexture surfaceTexture) {
        this.createSurfaceInternal(surfaceTexture);
    }

    private void createSurfaceInternal(Object nativeWindow) {
        if (!(nativeWindow instanceof SurfaceHolder) && !(nativeWindow instanceof SurfaceTexture)) {
            throw new IllegalStateException("Input must be either a SurfaceHolder or SurfaceTexture");
        }
        this.checkIsNotReleased();
        if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("Already has an EGLSurface");
        }
        EGL10 egl = this.eglConnection.getEgl();
        int[] surfaceAttribs = new int[]{12344};
        this.eglSurface = egl.eglCreateWindowSurface(this.eglConnection.getDisplay(), this.eglConnection.getConfig(), nativeWindow, surfaceAttribs);
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new GLException(egl.eglGetError(), "Failed to create window surface: 0x" + Integer.toHexString(egl.eglGetError()));
        }
    }

    @Override
    public void createDummyPbufferSurface() {
        this.createPbufferSurface(1, 1);
    }

    @Override
    public void createPbufferSurface(int width, int height) {
        this.checkIsNotReleased();
        if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("Already has an EGLSurface");
        }
        EGL10 egl = this.eglConnection.getEgl();
        int[] surfaceAttribs = new int[]{12375, width, 12374, height, 12344};
        this.eglSurface = egl.eglCreatePbufferSurface(this.eglConnection.getDisplay(), this.eglConnection.getConfig(), surfaceAttribs);
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new GLException(egl.eglGetError(), "Failed to create pixel buffer surface with size " + width + "x" + height + ": 0x" + Integer.toHexString(egl.eglGetError()));
        }
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return new Context(this.eglConnection.getEgl(), this.eglConnection.getContext(), this.eglConnection.getConfig());
    }

    @Override
    public boolean hasSurface() {
        return this.eglSurface != EGL10.EGL_NO_SURFACE;
    }

    @Override
    public int surfaceWidth() {
        int[] widthArray = new int[1];
        this.eglConnection.getEgl().eglQuerySurface(this.eglConnection.getDisplay(), this.eglSurface, 12375, widthArray);
        return widthArray[0];
    }

    @Override
    public int surfaceHeight() {
        int[] heightArray = new int[1];
        this.eglConnection.getEgl().eglQuerySurface(this.eglConnection.getDisplay(), this.eglSurface, 12374, heightArray);
        return heightArray[0];
    }

    @Override
    public void releaseSurface() {
        if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
            this.eglConnection.getEgl().eglDestroySurface(this.eglConnection.getDisplay(), this.eglSurface);
            this.eglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    private void checkIsNotReleased() {
        if (this.eglConnection == EGL_NO_CONNECTION) {
            throw new RuntimeException("This object has been released");
        }
    }

    @Override
    public void release() {
        this.checkIsNotReleased();
        this.releaseSurface();
        this.eglConnection.release();
        this.eglConnection = EGL_NO_CONNECTION;
    }

    @Override
    public void makeCurrent() {
        this.checkIsNotReleased();
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface - can't make current");
        }
        this.eglConnection.makeCurrent(this.eglSurface);
    }

    @Override
    public void detachCurrent() {
        this.eglConnection.detachCurrent();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void swapBuffers() {
        this.checkIsNotReleased();
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface - can't swap buffers");
        }
        Object object = EglBase.lock;
        synchronized (object) {
            this.eglConnection.getEgl().eglSwapBuffers(this.eglConnection.getDisplay(), this.eglSurface);
        }
    }

    @Override
    public void swapBuffers(long timeStampNs) {
        this.swapBuffers();
    }

    private static EGLDisplay getEglDisplay(EGL10 egl) {
        EGLDisplay eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new GLException(egl.eglGetError(), "Unable to get EGL10 display: 0x" + Integer.toHexString(egl.eglGetError()));
        }
        int[] version = new int[2];
        if (!egl.eglInitialize(eglDisplay, version)) {
            throw new GLException(egl.eglGetError(), "Unable to initialize EGL10: 0x" + Integer.toHexString(egl.eglGetError()));
        }
        return eglDisplay;
    }

    private static EGLConfig getEglConfig(EGL10 egl, EGLDisplay eglDisplay, int[] configAttributes) {
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!egl.eglChooseConfig(eglDisplay, configAttributes, configs, configs.length, numConfigs)) {
            throw new GLException(egl.eglGetError(), "eglChooseConfig failed: 0x" + Integer.toHexString(egl.eglGetError()));
        }
        if (numConfigs[0] <= 0) {
            throw new RuntimeException("Unable to find any matching EGL config");
        }
        EGLConfig eglConfig = configs[0];
        if (eglConfig == null) {
            throw new RuntimeException("eglChooseConfig returned null");
        }
        return eglConfig;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static EGLContext createEglContext(EGL10 egl, @Nullable EGLContext sharedContext, EGLDisplay eglDisplay, EGLConfig eglConfig, int openGlesVersion) {
        EGLContext eglContext;
        if (sharedContext != null && sharedContext == EGL10.EGL_NO_CONTEXT) {
            throw new RuntimeException("Invalid sharedContext");
        }
        int[] contextAttributes = new int[]{12440, openGlesVersion, 12344};
        EGLContext rootContext = sharedContext == null ? EGL10.EGL_NO_CONTEXT : sharedContext;
        Object object = EglBase.lock;
        synchronized (object) {
            eglContext = egl.eglCreateContext(eglDisplay, eglConfig, rootContext, contextAttributes);
        }
        if (eglContext == EGL10.EGL_NO_CONTEXT) {
            throw new GLException(egl.eglGetError(), "Failed to create EGL context: 0x" + Integer.toHexString(egl.eglGetError()));
        }
        return eglContext;
    }

    private static native long nativeGetCurrentNativeEGLContext();

    public static class EglConnection
    implements EglBase10.EglConnection {
        private final EGL10 egl;
        private final EGLContext eglContext;
        private final EGLDisplay eglDisplay;
        private final EGLConfig eglConfig;
        private final RefCountDelegate refCountDelegate;
        private EGLSurface currentSurface = EGL10.EGL_NO_SURFACE;

        public EglConnection(EGLContext sharedContext, int[] configAttributes) {
            this.egl = (EGL10)EGLContext.getEGL();
            this.eglDisplay = EglBase10Impl.getEglDisplay(this.egl);
            this.eglConfig = EglBase10Impl.getEglConfig(this.egl, this.eglDisplay, configAttributes);
            int openGlesVersion = EglBase.getOpenGlesVersionFromConfig(configAttributes);
            Logging.d(EglBase10Impl.TAG, "Using OpenGL ES version " + openGlesVersion);
            this.eglContext = EglBase10Impl.createEglContext(this.egl, sharedContext, this.eglDisplay, this.eglConfig, openGlesVersion);
            this.refCountDelegate = new RefCountDelegate(() -> {
                Object object = EglBase.lock;
                synchronized (object) {
                    this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                }
                this.egl.eglDestroyContext(this.eglDisplay, this.eglContext);
                this.egl.eglTerminate(this.eglDisplay);
                this.currentSurface = EGL10.EGL_NO_SURFACE;
            });
        }

        private EglConnection() {
            this.egl = (EGL10)EGLContext.getEGL();
            this.eglContext = EGL10.EGL_NO_CONTEXT;
            this.eglDisplay = EGL10.EGL_NO_DISPLAY;
            this.eglConfig = null;
            this.refCountDelegate = new RefCountDelegate(() -> {});
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
        public EGL10 getEgl() {
            return this.egl;
        }

        @Override
        public EGLContext getContext() {
            return this.eglContext;
        }

        @Override
        public EGLDisplay getDisplay() {
            return this.eglDisplay;
        }

        @Override
        public EGLConfig getConfig() {
            return this.eglConfig;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void makeCurrent(EGLSurface eglSurface) {
            if (this.egl.eglGetCurrentContext() == this.eglContext && this.currentSurface == eglSurface) {
                return;
            }
            Object object = EglBase.lock;
            synchronized (object) {
                if (!this.egl.eglMakeCurrent(this.eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                    throw new GLException(this.egl.eglGetError(), "eglMakeCurrent failed: 0x" + Integer.toHexString(this.egl.eglGetError()));
                }
            }
            this.currentSurface = eglSurface;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void detachCurrent() {
            Object object = EglBase.lock;
            synchronized (object) {
                if (!this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
                    throw new GLException(this.egl.eglGetError(), "eglDetachCurrent failed: 0x" + Integer.toHexString(this.egl.eglGetError()));
                }
            }
            this.currentSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    private static class Context
    implements EglBase10.Context {
        private final EGL10 egl;
        private final EGLContext eglContext;
        private final EGLConfig eglContextConfig;

        @Override
        public EGLContext getRawContext() {
            return this.eglContext;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long getNativeEglContext() {
            EGLContext previousContext = this.egl.eglGetCurrentContext();
            EGLDisplay currentDisplay = this.egl.eglGetCurrentDisplay();
            EGLSurface previousDrawSurface = this.egl.eglGetCurrentSurface(12377);
            EGLSurface previousReadSurface = this.egl.eglGetCurrentSurface(12378);
            EGLSurface tempEglSurface = null;
            if (currentDisplay == EGL10.EGL_NO_DISPLAY) {
                currentDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            }
            try {
                int[] surfaceAttribs;
                if (previousContext != this.eglContext && !this.egl.eglMakeCurrent(currentDisplay, tempEglSurface = this.egl.eglCreatePbufferSurface(currentDisplay, this.eglContextConfig, surfaceAttribs = new int[]{12375, 1, 12374, 1, 12344}), tempEglSurface, this.eglContext)) {
                    throw new GLException(this.egl.eglGetError(), "Failed to make temporary EGL surface active: " + this.egl.eglGetError());
                }
                long l = EglBase10Impl.nativeGetCurrentNativeEGLContext();
                return l;
            }
            finally {
                if (tempEglSurface != null) {
                    this.egl.eglMakeCurrent(currentDisplay, previousDrawSurface, previousReadSurface, previousContext);
                    this.egl.eglDestroySurface(currentDisplay, tempEglSurface);
                }
            }
        }

        public Context(EGL10 egl, EGLContext eglContext, EGLConfig eglContextConfig) {
            this.egl = egl;
            this.eglContext = eglContext;
            this.eglContextConfig = eglContextConfig;
        }
    }
}

