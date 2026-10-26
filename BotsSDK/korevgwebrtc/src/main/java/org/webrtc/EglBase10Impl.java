/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.SurfaceTexture
 *  androidx.annotation.Nullable
 *  android.view.Surface
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
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
import androidx.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import org.webrtc.EglBase;
import org.webrtc.EglBase10;
import org.webrtc.Logging;

class EglBase10Impl
implements EglBase10 {
    private static final String TAG = "EglBase10Impl";
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private final EGL10 egl;
    private EGLContext eglContext;
    @Nullable
    private EGLConfig eglConfig;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface = EGL10.EGL_NO_SURFACE;

    public EglBase10Impl(EGLContext sharedContext, int[] configAttributes) {
        this.egl = (EGL10)EGLContext.getEGL();
        this.eglDisplay = this.getEglDisplay();
        this.eglConfig = EglBase10Impl.getEglConfig(this.egl, this.eglDisplay, configAttributes);
        int openGlesVersion = EglBase.getOpenGlesVersionFromConfig(configAttributes);
        Logging.d(TAG, "Using OpenGL ES version " + openGlesVersion);
        this.eglContext = this.createEglContext(sharedContext, this.eglDisplay, this.eglConfig, openGlesVersion);
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
        int[] surfaceAttribs = new int[]{12344};
        this.eglSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, nativeWindow, surfaceAttribs);
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("Failed to create window surface: 0x" + Integer.toHexString(this.egl.eglGetError()));
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
        int[] surfaceAttribs = new int[]{12375, width, 12374, height, 12344};
        this.eglSurface = this.egl.eglCreatePbufferSurface(this.eglDisplay, this.eglConfig, surfaceAttribs);
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("Failed to create pixel buffer surface with size " + width + "x" + height + ": 0x" + Integer.toHexString(this.egl.eglGetError()));
        }
    }

    @Override
    public EglBase.Context getEglBaseContext() {
        return new Context(this.egl, this.eglContext, this.eglConfig);
    }

    @Override
    public boolean hasSurface() {
        return this.eglSurface != EGL10.EGL_NO_SURFACE;
    }

    @Override
    public int surfaceWidth() {
        int[] widthArray = new int[1];
        this.egl.eglQuerySurface(this.eglDisplay, this.eglSurface, 12375, widthArray);
        return widthArray[0];
    }

    @Override
    public int surfaceHeight() {
        int[] heightArray = new int[1];
        this.egl.eglQuerySurface(this.eglDisplay, this.eglSurface, 12374, heightArray);
        return heightArray[0];
    }

    @Override
    public void releaseSurface() {
        if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
            this.egl.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    private void checkIsNotReleased() {
        if (this.eglDisplay == EGL10.EGL_NO_DISPLAY || this.eglContext == EGL10.EGL_NO_CONTEXT || this.eglConfig == null) {
            throw new RuntimeException("This object has been released");
        }
    }

    @Override
    public void release() {
        this.checkIsNotReleased();
        this.releaseSurface();
        this.detachCurrent();
        this.egl.eglDestroyContext(this.eglDisplay, this.eglContext);
        this.egl.eglTerminate(this.eglDisplay);
        this.eglContext = EGL10.EGL_NO_CONTEXT;
        this.eglDisplay = EGL10.EGL_NO_DISPLAY;
        this.eglConfig = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void makeCurrent() {
        this.checkIsNotReleased();
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface - can't make current");
        }
        Object object = EglBase.lock;
        synchronized (object) {
            if (!this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
                throw new RuntimeException("eglMakeCurrent failed: 0x" + Integer.toHexString(this.egl.eglGetError()));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void detachCurrent() {
        Object object = EglBase.lock;
        synchronized (object) {
            if (!this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
                throw new RuntimeException("eglDetachCurrent failed: 0x" + Integer.toHexString(this.egl.eglGetError()));
            }
        }
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
            this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface);
        }
    }

    @Override
    public void swapBuffers(long timeStampNs) {
        this.swapBuffers();
    }

    private EGLDisplay getEglDisplay() {
        EGLDisplay eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("Unable to get EGL10 display: 0x" + Integer.toHexString(this.egl.eglGetError()));
        }
        int[] version = new int[2];
        if (!this.egl.eglInitialize(eglDisplay, version)) {
            throw new RuntimeException("Unable to initialize EGL10: 0x" + Integer.toHexString(this.egl.eglGetError()));
        }
        return eglDisplay;
    }

    private static EGLConfig getEglConfig(EGL10 egl, EGLDisplay eglDisplay, int[] configAttributes) {
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!egl.eglChooseConfig(eglDisplay, configAttributes, configs, configs.length, numConfigs)) {
            throw new RuntimeException("eglChooseConfig failed: 0x" + Integer.toHexString(egl.eglGetError()));
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
    private EGLContext createEglContext(@Nullable EGLContext sharedContext, EGLDisplay eglDisplay, EGLConfig eglConfig, int openGlesVersion) {
        EGLContext eglContext;
        if (sharedContext != null && sharedContext == EGL10.EGL_NO_CONTEXT) {
            throw new RuntimeException("Invalid sharedContext");
        }
        int[] contextAttributes = new int[]{12440, openGlesVersion, 12344};
        EGLContext rootContext = sharedContext == null ? EGL10.EGL_NO_CONTEXT : sharedContext;
        Object object = EglBase.lock;
        synchronized (object) {
            eglContext = this.egl.eglCreateContext(eglDisplay, eglConfig, rootContext, contextAttributes);
        }
        if (eglContext == EGL10.EGL_NO_CONTEXT) {
            throw new RuntimeException("Failed to create EGL context: 0x" + Integer.toHexString(this.egl.eglGetError()));
        }
        return eglContext;
    }

    private static native long nativeGetCurrentNativeEGLContext();

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
                    throw new RuntimeException("Failed to make temporary EGL surface active: " + this.egl.eglGetError());
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

