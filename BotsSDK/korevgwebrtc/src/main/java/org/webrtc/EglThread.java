/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  androidx.annotation.GuardedBy
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.RenderSynchronizer;
import org.webrtc.ThreadUtils;

public class EglThread
implements RenderSynchronizer.Listener {
    private final ReleaseMonitor releaseMonitor;
    private final HandlerWithExceptionCallbacks handler;
    private final EglBase.EglConnection eglConnection;
    private final RenderSynchronizer renderSynchronizer;
    private final List<RenderUpdate> pendingRenderUpdates = new ArrayList<RenderUpdate>();
    private boolean renderWindowOpen = true;

    public static EglThread create(@Nullable ReleaseMonitor releaseMonitor, @Nullable EglBase.Context sharedContext, int[] configAttributes, @Nullable RenderSynchronizer renderSynchronizer) {
        HandlerThread renderThread = new HandlerThread("EglThread");
        renderThread.start();
        HandlerWithExceptionCallbacks handler = new HandlerWithExceptionCallbacks(renderThread.getLooper());
        EglBase.EglConnection eglConnection = ThreadUtils.invokeAtFrontUninterruptibly((Handler)handler, () -> {
            if (sharedContext == null) {
                return EglBase.EglConnection.createEgl10(configAttributes);
            }
            return EglBase.EglConnection.create(sharedContext, configAttributes);
        });
        return new EglThread(releaseMonitor != null ? releaseMonitor : eglThread -> true, handler, eglConnection, renderSynchronizer);
    }

    public static EglThread create(@Nullable ReleaseMonitor releaseMonitor, @Nullable EglBase.Context sharedContext, int[] configAttributes) {
        return EglThread.create(releaseMonitor, sharedContext, configAttributes, null);
    }

    private EglThread(ReleaseMonitor releaseMonitor, HandlerWithExceptionCallbacks handler, EglBase.EglConnection eglConnection, RenderSynchronizer renderSynchronizer) {
        this.releaseMonitor = releaseMonitor;
        this.handler = handler;
        this.eglConnection = eglConnection;
        this.renderSynchronizer = renderSynchronizer;
        if (renderSynchronizer != null) {
            renderSynchronizer.registerListener(this);
        }
    }

    public void release() {
        if (!this.releaseMonitor.onRelease(this)) {
            return;
        }
        if (this.renderSynchronizer != null) {
            this.renderSynchronizer.removeListener(this);
        }
        this.handler.post(this.eglConnection::release);
        this.handler.getLooper().quitSafely();
    }

    public EglBase createEglBaseWithSharedConnection() {
        return EglBase.create(this.eglConnection);
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void addExceptionCallback(Runnable callback) {
        this.handler.addExceptionCallback(callback);
    }

    public void removeExceptionCallback(Runnable callback) {
        this.handler.removeExceptionCallback(callback);
    }

    public void scheduleRenderUpdate(RenderUpdate update) {
        if (this.renderWindowOpen) {
            update.update(true);
        } else {
            this.pendingRenderUpdates.add(update);
        }
    }

    @Override
    public void onRenderWindowOpen() {
        this.handler.post(() -> {
            this.renderWindowOpen = true;
            for (RenderUpdate update : this.pendingRenderUpdates) {
                update.update(false);
            }
            this.pendingRenderUpdates.clear();
        });
    }

    @Override
    public void onRenderWindowClose() {
        this.handler.post(() -> {
            this.renderWindowOpen = false;
        });
    }

    private static class HandlerWithExceptionCallbacks
    extends Handler {
        private final Object callbackLock = new Object();
        @GuardedBy(value="callbackLock")
        private final List<Runnable> exceptionCallbacks = new ArrayList<Runnable>();

        public HandlerWithExceptionCallbacks(Looper looper) {
            super(looper);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            }
            catch (Exception e) {
                Logging.e("EglThread", "Exception on EglThread", e);
                Object object = this.callbackLock;
                synchronized (object) {
                    for (Runnable callback : this.exceptionCallbacks) {
                        callback.run();
                    }
                }
                throw e;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void addExceptionCallback(Runnable callback) {
            Object object = this.callbackLock;
            synchronized (object) {
                this.exceptionCallbacks.add(callback);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void removeExceptionCallback(Runnable callback) {
            Object object = this.callbackLock;
            synchronized (object) {
                this.exceptionCallbacks.remove(callback);
            }
        }
    }

    public static interface ReleaseMonitor {
        public boolean onRelease(EglThread var1);
    }

    public static interface RenderUpdate {
        public void update(boolean var1);
    }
}

