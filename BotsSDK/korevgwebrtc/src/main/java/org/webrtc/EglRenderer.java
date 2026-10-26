/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Matrix
 *  android.graphics.SurfaceTexture
 *  android.opengl.GLES20
 *  android.view.Surface
 *  androidx.annotation.GuardedBy
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.Surface;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.webrtc.EglBase;
import org.webrtc.EglThread;
import org.webrtc.GlTextureFrameBuffer;
import org.webrtc.GlUtil;
import org.webrtc.Logging;
import org.webrtc.RendererCommon;
import org.webrtc.ThreadUtils;
import org.webrtc.VideoFrame;
import org.webrtc.VideoFrameDrawer;
import org.webrtc.VideoSink;

public class EglRenderer
implements VideoSink {
    private static final String TAG = "EglRenderer";
    private static final long LOG_INTERVAL_SEC = 4L;
    protected final String name;
    private final Object threadLock = new Object();
    @GuardedBy(value="threadLock")
    @Nullable
    private EglThread eglThread;
    private final Runnable eglExceptionCallback = new Runnable(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            Object object = EglRenderer.this.threadLock;
            synchronized (object) {
                EglRenderer.this.eglThread = null;
            }
        }
    };
    private final ArrayList<FrameListenerAndParams> frameListeners = new ArrayList();
    private volatile ErrorCallback errorCallback;
    private final Object fpsReductionLock = new Object();
    private long nextFrameTimeNs;
    private long minRenderPeriodNs;
    @Nullable
    private EglBase eglBase;
    private final VideoFrameDrawer frameDrawer;
    @Nullable
    private RendererCommon.GlDrawer drawer;
    private boolean usePresentationTimeStamp;
    private final Matrix drawMatrix = new Matrix();
    private final Object frameLock = new Object();
    @Nullable
    private VideoFrame pendingFrame;
    private final Object layoutLock = new Object();
    private float layoutAspectRatio;
    private boolean mirrorHorizontally;
    private boolean mirrorVertically;
    private final Object statisticsLock = new Object();
    private int framesReceived;
    private int framesDropped;
    private int framesRendered;
    private long statisticsStartTimeNs;
    private long renderTimeNs;
    private long renderSwapBufferTimeNs;
    private final GlTextureFrameBuffer bitmapTextureFramebuffer = new GlTextureFrameBuffer(6408);
    private final Runnable logStatisticsRunnable = new Runnable(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            EglRenderer.this.logStatistics();
            Object object = EglRenderer.this.threadLock;
            synchronized (object) {
                if (EglRenderer.this.eglThread != null) {
                    EglRenderer.this.eglThread.getHandler().removeCallbacks(EglRenderer.this.logStatisticsRunnable);
                    EglRenderer.this.eglThread.getHandler().postDelayed(EglRenderer.this.logStatisticsRunnable, TimeUnit.SECONDS.toMillis(4L));
                }
            }
        }
    };
    private final EglSurfaceCreation eglSurfaceCreationRunnable = new EglSurfaceCreation();

    public EglRenderer(String name) {
        this(name, new VideoFrameDrawer());
    }

    public EglRenderer(String name, VideoFrameDrawer videoFrameDrawer) {
        this.name = name;
        this.frameDrawer = videoFrameDrawer;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void init(EglThread eglThread, RendererCommon.GlDrawer drawer, boolean usePresentationTimeStamp) {
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread != null) {
                throw new IllegalStateException(this.name + "Already initialized");
            }
            this.logD("Initializing EglRenderer");
            this.eglThread = eglThread;
            this.drawer = drawer;
            this.usePresentationTimeStamp = usePresentationTimeStamp;
            eglThread.addExceptionCallback(this.eglExceptionCallback);
            this.eglBase = eglThread.createEglBaseWithSharedConnection();
            eglThread.getHandler().post((Runnable)this.eglSurfaceCreationRunnable);
            long currentTimeNs = System.nanoTime();
            this.resetStatistics(currentTimeNs);
            eglThread.getHandler().postDelayed(this.logStatisticsRunnable, TimeUnit.SECONDS.toMillis(4L));
        }
    }

    public void init(@Nullable EglBase.Context sharedContext, int[] configAttributes, RendererCommon.GlDrawer drawer, boolean usePresentationTimeStamp) {
        EglThread thread = EglThread.create(null, sharedContext, configAttributes);
        this.init(thread, drawer, usePresentationTimeStamp);
    }

    public void init(@Nullable EglBase.Context sharedContext, int[] configAttributes, RendererCommon.GlDrawer drawer) {
        this.init(sharedContext, configAttributes, drawer, false);
    }

    public void createEglSurface(Surface surface) {
        this.createEglSurfaceInternal(surface);
    }

    public void createEglSurface(SurfaceTexture surfaceTexture) {
        this.createEglSurfaceInternal(surfaceTexture);
    }

    private void createEglSurfaceInternal(Object surface) {
        this.eglSurfaceCreationRunnable.setSurface(surface);
        this.postToRenderThread(this.eglSurfaceCreationRunnable);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void release() {
        this.logD("Releasing.");
        CountDownLatch eglCleanupBarrier = new CountDownLatch(1);
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread == null) {
                this.logD("Already released");
                return;
            }
            this.eglThread.getHandler().removeCallbacks(this.logStatisticsRunnable);
            this.eglThread.removeExceptionCallback(this.eglExceptionCallback);
            this.eglThread.getHandler().postAtFrontOfQueue(() -> {
                Object eglLock = EglBase.lock;
                synchronized (eglLock) {
                    GLES20.glUseProgram((int)0);
                }
                if (this.drawer != null) {
                    this.drawer.release();
                    this.drawer = null;
                }
                this.frameDrawer.release();
                this.bitmapTextureFramebuffer.release();
                if (this.eglBase != null) {
                    this.logD("eglBase detach and release.");
                    this.eglBase.detachCurrent();
                    this.eglBase.release();
                    this.eglBase = null;
                }
                this.frameListeners.clear();
                eglCleanupBarrier.countDown();
            });
            this.eglThread.release();
            this.eglThread = null;
        }
        ThreadUtils.awaitUninterruptibly(eglCleanupBarrier);
        object = this.frameLock;
        synchronized (object) {
            if (this.pendingFrame != null) {
                this.pendingFrame.release();
                this.pendingFrame = null;
            }
        }
        this.logD("Releasing done.");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void resetStatistics(long currentTimeNs) {
        Object object = this.statisticsLock;
        synchronized (object) {
            this.statisticsStartTimeNs = currentTimeNs;
            this.framesReceived = 0;
            this.framesDropped = 0;
            this.framesRendered = 0;
            this.renderTimeNs = 0L;
            this.renderSwapBufferTimeNs = 0L;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void printStackTrace() {
        Object object = this.threadLock;
        synchronized (object) {
            StackTraceElement[] renderStackTrace;
            Thread renderThread;
            Thread thread = renderThread = this.eglThread == null ? null : this.eglThread.getHandler().getLooper().getThread();
            if (renderThread != null && (renderStackTrace = renderThread.getStackTrace()).length > 0) {
                this.logW("EglRenderer stack trace:");
                for (StackTraceElement traceElem : renderStackTrace) {
                    this.logW(traceElem.toString());
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setMirror(boolean mirror) {
        Object object = this.layoutLock;
        synchronized (object) {
            this.mirrorHorizontally = mirror;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setMirrorVertically(boolean mirrorVertically) {
        Object object = this.layoutLock;
        synchronized (object) {
            this.mirrorVertically = mirrorVertically;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setLayoutAspectRatio(float layoutAspectRatio) {
        Object object = this.layoutLock;
        synchronized (object) {
            this.layoutAspectRatio = layoutAspectRatio;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setFpsReduction(float fps) {
        Object object = this.fpsReductionLock;
        synchronized (object) {
            long previousRenderPeriodNs = this.minRenderPeriodNs;
            this.minRenderPeriodNs = fps <= 0.0f ? Long.MAX_VALUE : (long)((float)TimeUnit.SECONDS.toNanos(1L) / fps);
            if (this.minRenderPeriodNs != previousRenderPeriodNs) {
                this.nextFrameTimeNs = System.nanoTime();
            }
        }
    }

    public void disableFpsReduction() {
        this.setFpsReduction(Float.POSITIVE_INFINITY);
    }

    public void pauseVideo() {
        this.setFpsReduction(0.0f);
    }

    public void addFrameListener(FrameListener listener, float scale) {
        this.addFrameListener(listener, scale, null, false);
    }

    public void addFrameListener(FrameListener listener, float scale, RendererCommon.GlDrawer drawerParam) {
        this.addFrameListener(listener, scale, drawerParam, false);
    }

    public void addFrameListener(FrameListener listener, float scale, @Nullable RendererCommon.GlDrawer drawerParam, boolean applyFpsReduction) {
        this.postToRenderThread(() -> {
            RendererCommon.GlDrawer listenerDrawer = drawerParam == null ? this.drawer : drawerParam;
            this.frameListeners.add(new FrameListenerAndParams(listener, scale, listenerDrawer, applyFpsReduction));
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeFrameListener(FrameListener listener) {
        CountDownLatch latch = new CountDownLatch(1);
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread == null) {
                return;
            }
            if (Thread.currentThread() == this.eglThread.getHandler().getLooper().getThread()) {
                throw new RuntimeException("removeFrameListener must not be called on the render thread.");
            }
            this.postToRenderThread(() -> {
                latch.countDown();
                Iterator<FrameListenerAndParams> iter = this.frameListeners.iterator();
                while (iter.hasNext()) {
                    if (iter.next().listener != listener) continue;
                    iter.remove();
                }
            });
        }
        ThreadUtils.awaitUninterruptibly(latch);
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onFrame(VideoFrame frame) {
        boolean dropOldFrame;
        Object object = this.statisticsLock;
        synchronized (object) {
            ++this.framesReceived;
        }
        Object object2 = this.threadLock;
        synchronized (object2) {
            if (this.eglThread == null) {
                this.logD("Dropping frame - Not initialized or already released.");
                return;
            }
            Object object3 = this.frameLock;
            synchronized (object3) {
                boolean bl = dropOldFrame = this.pendingFrame != null;
                if (dropOldFrame) {
                    this.pendingFrame.release();
                }
                this.pendingFrame = frame;
                this.pendingFrame.retain();
                this.eglThread.getHandler().post(this::renderFrameOnRenderThread);
            }
        }
        if (dropOldFrame) {
            object2 = this.statisticsLock;
            synchronized (object2) {
                ++this.framesDropped;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseEglSurface(Runnable completionCallback) {
        this.eglSurfaceCreationRunnable.setSurface(null);
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread != null) {
                this.eglThread.getHandler().removeCallbacks((Runnable)this.eglSurfaceCreationRunnable);
                this.eglThread.getHandler().postAtFrontOfQueue(() -> {
                    if (this.eglBase != null) {
                        this.eglBase.detachCurrent();
                        this.eglBase.releaseSurface();
                    }
                    completionCallback.run();
                });
                return;
            }
        }
        completionCallback.run();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void postToRenderThread(Runnable runnable) {
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread != null) {
                this.eglThread.getHandler().post(runnable);
            }
        }
    }

    private void clearSurfaceOnRenderThread(float r, float g, float b, float a) {
        if (this.eglBase != null && this.eglBase.hasSurface()) {
            this.logD("clearSurface");
            this.eglBase.makeCurrent();
            GLES20.glClearColor((float)r, (float)g, (float)b, (float)a);
            GLES20.glClear((int)16384);
            this.eglBase.swapBuffers();
        }
    }

    public void clearImage() {
        this.clearImage(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clearImage(float r, float g, float b, float a) {
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread == null) {
                return;
            }
            this.eglThread.getHandler().postAtFrontOfQueue(() -> this.clearSurfaceOnRenderThread(r, g, b, a));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void swapBuffersOnRenderThread(VideoFrame frame, long swapBuffersStartTimeNs) {
        Object object = this.threadLock;
        synchronized (object) {
            if (this.eglThread != null) {
                this.eglThread.scheduleRenderUpdate(runsInline -> {
                    if (!runsInline) {
                        if (this.eglBase == null || !this.eglBase.hasSurface()) {
                            return;
                        }
                        this.eglBase.makeCurrent();
                    }
                    if (this.usePresentationTimeStamp) {
                        this.eglBase.swapBuffers(frame.getTimestampNs());
                    } else {
                        this.eglBase.swapBuffers();
                    }
                    Object statisticsLock = this.statisticsLock;
                    synchronized (statisticsLock) {
                        this.renderSwapBufferTimeNs += System.nanoTime() - swapBuffersStartTimeNs;
                    }
                });
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void renderFrameOnRenderThread() {
        float scaleY;
        float scaleX;
        float drawnAspectRatio;
        boolean shouldRenderFrame;
        VideoFrame frame;
        Object object = this.frameLock;
        synchronized (object) {
            if (this.pendingFrame == null) {
                return;
            }
            frame = this.pendingFrame;
            this.pendingFrame = null;
        }
        if (this.eglBase == null || !this.eglBase.hasSurface()) {
            this.logD("Dropping frame - No surface");
            frame.release();
            return;
        }
        this.eglBase.makeCurrent();
        Object object2 = this.fpsReductionLock;
        synchronized (object2) {
            if (this.minRenderPeriodNs == Long.MAX_VALUE) {
                shouldRenderFrame = false;
            } else if (this.minRenderPeriodNs <= 0L) {
                shouldRenderFrame = true;
            } else {
                long currentTimeNs = System.nanoTime();
                if (currentTimeNs < this.nextFrameTimeNs) {
                    this.logD("Skipping frame rendering - fps reduction is active.");
                    shouldRenderFrame = false;
                } else {
                    this.nextFrameTimeNs += this.minRenderPeriodNs;
                    this.nextFrameTimeNs = Math.max(this.nextFrameTimeNs, currentTimeNs);
                    shouldRenderFrame = true;
                }
            }
        }
        long startTimeNs = System.nanoTime();
        float frameAspectRatio = (float)frame.getRotatedWidth() / (float)frame.getRotatedHeight();
        Object object3 = this.layoutLock;
        synchronized (object3) {
            drawnAspectRatio = this.layoutAspectRatio != 0.0f ? this.layoutAspectRatio : frameAspectRatio;
        }
        if (frameAspectRatio > drawnAspectRatio) {
            scaleX = drawnAspectRatio / frameAspectRatio;
            scaleY = 1.0f;
        } else {
            scaleX = 1.0f;
            scaleY = frameAspectRatio / drawnAspectRatio;
        }
        this.drawMatrix.reset();
        this.drawMatrix.preTranslate(0.5f, 0.5f);
        this.drawMatrix.preScale(this.mirrorHorizontally ? -1.0f : 1.0f, this.mirrorVertically ? -1.0f : 1.0f);
        this.drawMatrix.preScale(scaleX, scaleY);
        this.drawMatrix.preTranslate(-0.5f, -0.5f);
        try {
            if (shouldRenderFrame) {
                GLES20.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                GLES20.glClear((int)16384);
                this.frameDrawer.drawFrame(frame, this.drawer, this.drawMatrix, 0, 0, this.eglBase.surfaceWidth(), this.eglBase.surfaceHeight());
                long swapBuffersStartTimeNs = System.nanoTime();
                this.swapBuffersOnRenderThread(frame, swapBuffersStartTimeNs);
                Object object4 = this.statisticsLock;
                synchronized (object4) {
                    ++this.framesRendered;
                    this.renderTimeNs += swapBuffersStartTimeNs - startTimeNs;
                }
            }
            this.notifyCallbacks(frame, shouldRenderFrame);
        }
        catch (GlUtil.GlOutOfMemoryException e) {
            this.logE("Error while drawing frame", (Throwable)((Object)e));
            ErrorCallback errorCallback = this.errorCallback;
            if (errorCallback != null) {
                errorCallback.onGlOutOfMemory();
            }
            this.drawer.release();
            this.frameDrawer.release();
            this.bitmapTextureFramebuffer.release();
        }
        finally {
            frame.release();
        }
    }

    private void notifyCallbacks(VideoFrame frame, boolean wasRendered) {
        if (this.frameListeners.isEmpty()) {
            return;
        }
        this.drawMatrix.reset();
        this.drawMatrix.preTranslate(0.5f, 0.5f);
        this.drawMatrix.preScale(this.mirrorHorizontally ? -1.0f : 1.0f, this.mirrorVertically ? -1.0f : 1.0f);
        this.drawMatrix.preScale(1.0f, -1.0f);
        this.drawMatrix.preTranslate(-0.5f, -0.5f);
        Iterator<FrameListenerAndParams> it = this.frameListeners.iterator();
        while (it.hasNext()) {
            FrameListenerAndParams listenerAndParams = it.next();
            if (!wasRendered && listenerAndParams.applyFpsReduction) continue;
            it.remove();
            int scaledWidth = (int)(listenerAndParams.scale * (float)frame.getRotatedWidth());
            int scaledHeight = (int)(listenerAndParams.scale * (float)frame.getRotatedHeight());
            if (scaledWidth == 0 || scaledHeight == 0) {
                listenerAndParams.listener.onFrame(null);
                continue;
            }
            this.bitmapTextureFramebuffer.setSize(scaledWidth, scaledHeight);
            GLES20.glBindFramebuffer((int)36160, (int)this.bitmapTextureFramebuffer.getFrameBufferId());
            GLES20.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.bitmapTextureFramebuffer.getTextureId(), (int)0);
            GLES20.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            GLES20.glClear((int)16384);
            this.frameDrawer.drawFrame(frame, listenerAndParams.drawer, this.drawMatrix, 0, 0, scaledWidth, scaledHeight);
            ByteBuffer bitmapBuffer = ByteBuffer.allocateDirect(scaledWidth * scaledHeight * 4);
            GLES20.glViewport((int)0, (int)0, (int)scaledWidth, (int)scaledHeight);
            GLES20.glReadPixels((int)0, (int)0, (int)scaledWidth, (int)scaledHeight, (int)6408, (int)5121, (Buffer)bitmapBuffer);
            GLES20.glBindFramebuffer((int)36160, (int)0);
            GlUtil.checkNoGLES2Error("EglRenderer.notifyCallbacks");
            Bitmap bitmap = Bitmap.createBitmap((int)scaledWidth, (int)scaledHeight, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer((Buffer)bitmapBuffer);
            listenerAndParams.listener.onFrame(bitmap);
        }
    }

    private String averageTimeAsString(long sumTimeNs, int count) {
        return count <= 0 ? "NA" : TimeUnit.NANOSECONDS.toMicros(sumTimeNs / (long)count) + " us";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void logStatistics() {
        DecimalFormat fpsFormat = new DecimalFormat("#.0");
        long currentTimeNs = System.nanoTime();
        Object object = this.statisticsLock;
        synchronized (object) {
            long elapsedTimeNs = currentTimeNs - this.statisticsStartTimeNs;
            if (elapsedTimeNs <= 0L || this.minRenderPeriodNs == Long.MAX_VALUE && this.framesReceived == 0) {
                return;
            }
            float renderFps = (float)((long)this.framesRendered * TimeUnit.SECONDS.toNanos(1L)) / (float)elapsedTimeNs;
            this.logD("Duration: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeNs) + " ms. Frames received: " + this.framesReceived + ". Dropped: " + this.framesDropped + ". Rendered: " + this.framesRendered + ". Render fps: " + fpsFormat.format(renderFps) + ". Average render time: " + this.averageTimeAsString(this.renderTimeNs, this.framesRendered) + ". Average swapBuffer time: " + this.averageTimeAsString(this.renderSwapBufferTimeNs, this.framesRendered) + ".");
            this.resetStatistics(currentTimeNs);
        }
    }

    private void logE(String string, Throwable e) {
        Logging.e(TAG, this.name + string, e);
    }

    private void logD(String string) {
        Logging.d(TAG, this.name + string);
    }

    private void logW(String string) {
        Logging.w(TAG, this.name + string);
    }

    private class EglSurfaceCreation
    implements Runnable {
        private Object surface;

        private EglSurfaceCreation() {
        }

        public synchronized void setSurface(Object surface) {
            this.surface = surface;
        }

        @Override
        public synchronized void run() {
            if (this.surface != null && EglRenderer.this.eglBase != null && !EglRenderer.this.eglBase.hasSurface()) {
                if (this.surface instanceof Surface) {
                    EglRenderer.this.eglBase.createSurface((Surface)this.surface);
                } else if (this.surface instanceof SurfaceTexture) {
                    EglRenderer.this.eglBase.createSurface((SurfaceTexture)this.surface);
                } else {
                    throw new IllegalStateException("Invalid surface: " + this.surface);
                }
                EglRenderer.this.eglBase.makeCurrent();
                GLES20.glPixelStorei((int)3317, (int)1);
            }
        }
    }

    public static interface FrameListener {
        public void onFrame(Bitmap var1);
    }

    public static interface ErrorCallback {
        public void onGlOutOfMemory();
    }

    private static class FrameListenerAndParams {
        public final FrameListener listener;
        public final float scale;
        public final RendererCommon.GlDrawer drawer;
        public final boolean applyFpsReduction;

        public FrameListenerAndParams(FrameListener listener, float scale, RendererCommon.GlDrawer drawer, boolean applyFpsReduction) {
            this.listener = listener;
            this.scale = scale;
            this.drawer = drawer;
            this.applyFpsReduction = applyFpsReduction;
        }
    }
}

