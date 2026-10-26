/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 */
package org.webrtc;

import android.view.SurfaceHolder;
import java.util.concurrent.CountDownLatch;
import org.webrtc.EglBase;
import org.webrtc.EglRenderer;
import org.webrtc.Logging;
import org.webrtc.RendererCommon;
import org.webrtc.ThreadUtils;
import org.webrtc.VideoFrame;

public class SurfaceEglRenderer
extends EglRenderer
implements SurfaceHolder.Callback {
    private static final String TAG = "SurfaceEglRenderer";
    private RendererCommon.RendererEvents rendererEvents;
    private final Object layoutLock = new Object();
    private boolean isRenderingPaused;
    private boolean isFirstFrameRendered;
    private int rotatedFrameWidth;
    private int rotatedFrameHeight;
    private int frameRotation;

    public SurfaceEglRenderer(String name) {
        super(name);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void init(EglBase.Context sharedContext, RendererCommon.RendererEvents rendererEvents, int[] configAttributes, RendererCommon.GlDrawer drawer) {
        ThreadUtils.checkIsOnMainThread();
        this.rendererEvents = rendererEvents;
        Object object = this.layoutLock;
        synchronized (object) {
            this.isFirstFrameRendered = false;
            this.rotatedFrameWidth = 0;
            this.rotatedFrameHeight = 0;
            this.frameRotation = 0;
        }
        super.init(sharedContext, configAttributes, drawer);
    }

    @Override
    public void init(EglBase.Context sharedContext, int[] configAttributes, RendererCommon.GlDrawer drawer) {
        this.init(sharedContext, null, configAttributes, drawer);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setFpsReduction(float fps) {
        Object object = this.layoutLock;
        synchronized (object) {
            this.isRenderingPaused = fps == 0.0f;
        }
        super.setFpsReduction(fps);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void disableFpsReduction() {
        Object object = this.layoutLock;
        synchronized (object) {
            this.isRenderingPaused = false;
        }
        super.disableFpsReduction();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void pauseVideo() {
        Object object = this.layoutLock;
        synchronized (object) {
            this.isRenderingPaused = true;
        }
        super.pauseVideo();
    }

    @Override
    public void onFrame(VideoFrame frame) {
        this.updateFrameDimensionsAndReportEvents(frame);
        super.onFrame(frame);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        ThreadUtils.checkIsOnMainThread();
        this.createEglSurface(holder.getSurface());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        ThreadUtils.checkIsOnMainThread();
        CountDownLatch completionLatch = new CountDownLatch(1);
        this.releaseEglSurface(completionLatch::countDown);
        ThreadUtils.awaitUninterruptibly(completionLatch);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ThreadUtils.checkIsOnMainThread();
        this.logD("surfaceChanged: format: " + format + " size: " + width + "x" + height);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateFrameDimensionsAndReportEvents(VideoFrame frame) {
        Object object = this.layoutLock;
        synchronized (object) {
            if (this.isRenderingPaused) {
                return;
            }
            if (!this.isFirstFrameRendered) {
                this.isFirstFrameRendered = true;
                this.logD("Reporting first rendered frame.");
                if (this.rendererEvents != null) {
                    this.rendererEvents.onFirstFrameRendered();
                }
            }
            if (this.rotatedFrameWidth != frame.getRotatedWidth() || this.rotatedFrameHeight != frame.getRotatedHeight() || this.frameRotation != frame.getRotation()) {
                this.logD("Reporting frame resolution changed to " + frame.getBuffer().getWidth() + "x" + frame.getBuffer().getHeight() + " with rotation " + frame.getRotation());
                if (this.rendererEvents != null) {
                    this.rendererEvents.onFrameResolutionChanged(frame.getBuffer().getWidth(), frame.getBuffer().getHeight(), frame.getRotation());
                }
                this.rotatedFrameWidth = frame.getRotatedWidth();
                this.rotatedFrameHeight = frame.getRotatedHeight();
                this.frameRotation = frame.getRotation();
            }
        }
    }

    private void logD(String string) {
        Logging.d(TAG, this.name + ": " + string);
    }
}

