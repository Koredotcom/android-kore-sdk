/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Trace
 *  android.view.Choreographer
 *  androidx.annotation.GuardedBy
 */
package org.webrtc;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import android.view.Choreographer;
import androidx.annotation.GuardedBy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.webrtc.Logging;

public final class RenderSynchronizer {
    private static final String TAG = "RenderSynchronizer";
    private static final float DEFAULT_TARGET_FPS = 30.0f;
    private final Object lock = new Object();
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private final long targetFrameIntervalNanos;
    private final Handler mainThreadHandler;
    private Choreographer choreographer;
    @GuardedBy(value="lock")
    private boolean isListening;
    private boolean renderWindowOpen;
    private long lastRefreshTimeNanos;
    private long lastOpenedTimeNanos;

    public RenderSynchronizer(float targetFrameRateFps) {
        this.targetFrameIntervalNanos = Math.round((float)TimeUnit.SECONDS.toNanos(1L) / targetFrameRateFps);
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
        this.mainThreadHandler.post(() -> {
            this.choreographer = Choreographer.getInstance();
        });
        Logging.d(TAG, "Created");
    }

    public RenderSynchronizer() {
        this(30.0f);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void registerListener(Listener listener) {
        this.listeners.add(listener);
        Object object = this.lock;
        synchronized (object) {
            if (!this.isListening) {
                Logging.d(TAG, "First listener, subscribing to frame callbacks");
                this.isListening = true;
                this.mainThreadHandler.post(() -> this.choreographer.postFrameCallback(this::onDisplayRefreshCycleBegin));
            }
        }
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void onDisplayRefreshCycleBegin(long refreshTimeNanos) {
        Object object = this.lock;
        synchronized (object) {
            if (this.listeners.isEmpty()) {
                Logging.d(TAG, "No listeners, unsubscribing to frame callbacks");
                this.isListening = false;
                return;
            }
        }
        this.choreographer.postFrameCallback(this::onDisplayRefreshCycleBegin);
        long lastOpenDeltaNanos = refreshTimeNanos - this.lastOpenedTimeNanos;
        long refreshDeltaNanos = refreshTimeNanos - this.lastRefreshTimeNanos;
        this.lastRefreshTimeNanos = refreshTimeNanos;
        if (Math.abs(lastOpenDeltaNanos - this.targetFrameIntervalNanos) < Math.abs(lastOpenDeltaNanos - this.targetFrameIntervalNanos + refreshDeltaNanos)) {
            this.lastOpenedTimeNanos = refreshTimeNanos;
            this.openRenderWindow();
        } else if (this.renderWindowOpen) {
            this.closeRenderWindow();
        }
    }

    private void traceRenderWindowChange() {
        if (Build.VERSION.SDK_INT >= 29) {
            Trace.setCounter((String)"RenderWindow", (long)(this.renderWindowOpen ? 1L : 0L));
        }
    }

    private void openRenderWindow() {
        this.renderWindowOpen = true;
        this.traceRenderWindowChange();
        for (Listener listener : this.listeners) {
            listener.onRenderWindowOpen();
        }
    }

    private void closeRenderWindow() {
        this.renderWindowOpen = false;
        this.traceRenderWindowChange();
        for (Listener listener : this.listeners) {
            listener.onRenderWindowClose();
        }
    }

    public static interface Listener {
        public void onRenderWindowOpen();

        public void onRenderWindowClose();
    }
}

