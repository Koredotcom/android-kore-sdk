/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.opengl.EGL14
 *  android.opengl.EGLConfig
 *  android.opengl.EGLContext
 *  android.opengl.EGLDisplay
 *  android.opengl.EGLExt
 *  android.opengl.EGLSurface
 *  android.opengl.GLException
 *  android.view.Surface
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLException;
import android.view.Surface;
import androidx.annotation.Nullable;
import org.webrtc.EglBase;
import org.webrtc.EglBase14;
import org.webrtc.Logging;
import org.webrtc.RefCountDelegate;

class EglBase14Impl
implements EglBase14 {
    private static final String TAG = "EglBase14Impl";
    private static final EglConnection EGL_NO_CONNECTION = new EglConnection();
    private EGLSurface eglSurface = EGL14.EGL_NO_SURFACE;
    private EglConnection eglConnection;

    public EglBase14Impl(EGLContext sharedContext, int[] configAttributes) {
        this.eglConnection = new EglConnection(sharedContext, configAttributes);
    }

    public EglBase14Impl(EglConnection eglConnection) {
        this.eglConnection = eglConnection;
        this.eglConnection.retain();
    }

    @Override
    public void createSurface(Surface surface) {
        this.createSurfaceInternal(surface);
    }

    @Override
    public void createSurface(SurfaceTexture surfaceTexture) {
        this.createSurfaceInternal(surfaceTexture);
    }

    private void createSurfaceInternal(Object surface) {
        if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)) {
            throw new IllegalStateException("Input must be either a Surface or SurfaceTexture");
        }
        this.checkIsNotReleased();
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("Already has an EGLSurface");
        }
        int[] surfaceAttribs = new int[]{12344};
        this.eglSurface = EGL14.eglCreateWindowSurface((EGLDisplay)this.eglConnection.getDisplay(), (EGLConfig)this.eglConnection.getConfig(), (Object)surface, (int[])surfaceAttribs, (int)0);
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new GLException(EGL14.eglGetError(), "Failed to create window surface: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
    }

    @Override
    public void createDummyPbufferSurface() {
        this.createPbufferSurface(1, 1);
    }

    @Override
    public void createPbufferSurface(int width, int height) {
        this.checkIsNotReleased();
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("Already has an EGLSurface");
        }
        int[] surfaceAttribs = new int[]{12375, width, 12374, height, 12344};
        this.eglSurface = EGL14.eglCreatePbufferSurface((EGLDisplay)this.eglConnection.getDisplay(), (EGLConfig)this.eglConnection.getConfig(), (int[])surfaceAttribs, (int)0);
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new GLException(EGL14.eglGetError(), "Failed to create pixel buffer surface with size " + width + "x" + height + ": 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
    }

    @Override
    public Context getEglBaseContext() {
        return new Context(this.eglConnection.getContext());
    }

    @Override
    public boolean hasSurface() {
        return this.eglSurface != EGL14.EGL_NO_SURFACE;
    }

    @Override
    public int surfaceWidth() {
        int[] widthArray = new int[1];
        EGL14.eglQuerySurface((EGLDisplay)this.eglConnection.getDisplay(), (EGLSurface)this.eglSurface, (int)12375, (int[])widthArray, (int)0);
        return widthArray[0];
    }

    @Override
    public int surfaceHeight() {
        int[] heightArray = new int[1];
        EGL14.eglQuerySurface((EGLDisplay)this.eglConnection.getDisplay(), (EGLSurface)this.eglSurface, (int)12374, (int[])heightArray, (int)0);
        return heightArray[0];
    }

    @Override
    public void releaseSurface() {
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroySurface((EGLDisplay)this.eglConnection.getDisplay(), (EGLSurface)this.eglSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
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
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
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
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface - can't swap buffers");
        }
        Object object = EglBase.lock;
        synchronized (object) {
            EGL14.eglSwapBuffers((EGLDisplay)this.eglConnection.getDisplay(), (EGLSurface)this.eglSurface);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void swapBuffers(long timeStampNs) {
        this.checkIsNotReleased();
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface - can't swap buffers");
        }
        Object object = EglBase.lock;
        synchronized (object) {
            EGLExt.eglPresentationTimeANDROID((EGLDisplay)this.eglConnection.getDisplay(), (EGLSurface)this.eglSurface, (long)timeStampNs);
            EGL14.eglSwapBuffers((EGLDisplay)this.eglConnection.getDisplay(), (EGLSurface)this.eglSurface);
        }
    }

    private static EGLDisplay getEglDisplay() {
        EGLDisplay eglDisplay = EGL14.eglGetDisplay((int)0);
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new GLException(EGL14.eglGetError(), "Unable to get EGL14 display: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize((EGLDisplay)eglDisplay, (int[])version, (int)0, (int[])version, (int)1)) {
            throw new GLException(EGL14.eglGetError(), "Unable to initialize EGL14: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        return eglDisplay;
    }

    private static EGLConfig getEglConfig(EGLDisplay eglDisplay, int[] configAttributes) {
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig((EGLDisplay)eglDisplay, (int[])configAttributes, (int)0, (EGLConfig[])configs, (int)0, (int)configs.length, (int[])numConfigs, (int)0)) {
            throw new GLException(EGL14.eglGetError(), "eglChooseConfig failed: 0x" + Integer.toHexString(EGL14.eglGetError()));
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
    private static EGLContext createEglContext(@Nullable EGLContext sharedContext, EGLDisplay eglDisplay, EGLConfig eglConfig, int openGlesVersion) {
        EGLContext eglContext;
        if (sharedContext != null && sharedContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("Invalid sharedContext");
        }
        int[] contextAttributes = new int[]{12440, openGlesVersion, 12344};
        EGLContext rootContext = sharedContext == null ? EGL14.EGL_NO_CONTEXT : sharedContext;
        Object object = EglBase.lock;
        synchronized (object) {
            eglContext = EGL14.eglCreateContext((EGLDisplay)eglDisplay, (EGLConfig)eglConfig, (EGLContext)rootContext, (int[])contextAttributes, (int)0);
        }
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            throw new GLException(EGL14.eglGetError(), "Failed to create EGL context: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        return eglContext;
    }

    public static class EglConnection
    implements EglBase14.EglConnection {
        private final EGLContext eglContext;
        private final EGLDisplay eglDisplay;
        private final EGLConfig eglConfig;
        private final RefCountDelegate refCountDelegate;
        private EGLSurface currentSurface = EGL14.EGL_NO_SURFACE;

        public EglConnection(EGLContext sharedContext, int[] configAttributes) {
            this.eglDisplay = EglBase14Impl.getEglDisplay();
            this.eglConfig = EglBase14Impl.getEglConfig(this.eglDisplay, configAttributes);
            int openGlesVersion = EglBase.getOpenGlesVersionFromConfig(configAttributes);
            Logging.d(EglBase14Impl.TAG, "Using OpenGL ES version " + openGlesVersion);
            this.eglContext = EglBase14Impl.createEglContext(sharedContext, this.eglDisplay, this.eglConfig, openGlesVersion);
            this.refCountDelegate = new RefCountDelegate(() -> {
                Object object = EglBase.lock;
                synchronized (object) {
                    EGL14.eglMakeCurrent((EGLDisplay)this.eglDisplay, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLContext)EGL14.EGL_NO_CONTEXT);
                    EGL14.eglDestroyContext((EGLDisplay)this.eglDisplay, (EGLContext)this.eglContext);
                }
                EGL14.eglReleaseThread();
                EGL14.eglTerminate((EGLDisplay)this.eglDisplay);
                this.currentSurface = EGL14.EGL_NO_SURFACE;
            });
        }

        private EglConnection() {
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
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
            if (EGL14.eglGetCurrentContext() == this.eglContext && this.currentSurface == eglSurface) {
                return;
            }
            Object object = EglBase.lock;
            synchronized (object) {
                if (!EGL14.eglMakeCurrent((EGLDisplay)this.eglDisplay, (EGLSurface)eglSurface, (EGLSurface)eglSurface, (EGLContext)this.eglContext)) {
                    throw new GLException(EGL14.eglGetError(), "eglMakeCurrent failed: 0x" + Integer.toHexString(EGL14.eglGetError()));
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
                if (!EGL14.eglMakeCurrent((EGLDisplay)this.eglDisplay, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLSurface)EGL14.EGL_NO_SURFACE, (EGLContext)EGL14.EGL_NO_CONTEXT)) {
                    throw new GLException(EGL14.eglGetError(), "eglDetachCurrent failed: 0x" + Integer.toHexString(EGL14.eglGetError()));
                }
            }
            this.currentSurface = EGL14.EGL_NO_SURFACE;
        }
    }

    public static class Context
    implements EglBase14.Context {
        private final EGLContext egl14Context;

        @Override
        public EGLContext getRawContext() {
            return this.egl14Context;
        }

        @Override
        public long getNativeEglContext() {
            return this.egl14Context.getNativeHandle();
        }

        public Context(EGLContext eglContext) {
            this.egl14Context = eglContext;
        }
    }
}

