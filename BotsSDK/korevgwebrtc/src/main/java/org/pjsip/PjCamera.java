/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.hardware.Camera
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$PreviewCallback
 *  android.util.Log
 *  android.view.SurfaceHolder
 *  android.view.SurfaceHolder$Callback
 *  android.view.SurfaceView
 */
package org.pjsip;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

public class PjCamera
implements Camera.PreviewCallback,
SurfaceHolder.Callback {
    private final String TAG = "PjCamera";
    private Camera camera = null;
    private boolean isRunning = false;
    private int camIdx;
    private long userData;
    private Param param = null;
    private SurfaceView surfaceView = null;
    private SurfaceHolder surfaceHolder = null;
    private SurfaceTexture surfaceTexture = null;

    public PjCamera(int idx, int w, int h, int fmt, int fps, long userData_, SurfaceView surface) {
        this.camIdx = idx;
        this.userData = userData_;
        this.param = new Param();
        this.param.width = w;
        this.param.height = h;
        this.param.format = fmt;
        this.param.fps1000 = fps;
        this.SetSurfaceView(surface);
    }

    public void SetSurfaceView(SurfaceView surface) {
        boolean isCaptureRunning = this.isRunning;
        if (isCaptureRunning) {
            this.Stop();
        }
        if (surface != null) {
            this.surfaceView = surface;
            this.surfaceHolder = this.surfaceView.getHolder();
        } else {
            this.surfaceHolder = null;
            this.surfaceView = null;
            if (this.surfaceTexture == null) {
                this.surfaceTexture = new SurfaceTexture(10);
            }
        }
        if (isCaptureRunning) {
            this.Start();
        }
    }

    public int SwitchDevice(int idx) {
        int ret;
        boolean isCaptureRunning = this.isRunning;
        int oldIdx = this.camIdx;
        if (isCaptureRunning) {
            this.Stop();
        }
        this.camIdx = idx;
        if (isCaptureRunning && (ret = this.Start()) != 0) {
            this.camIdx = oldIdx;
            this.Start();
            return ret;
        }
        return 0;
    }

    public int Start() {
        try {
            this.camera = Camera.open((int)this.camIdx);
        }
        catch (Exception e) {
            Log.d((String)"IOException", (String)e.getMessage());
            return -10;
        }
        try {
            if (this.surfaceHolder != null) {
                this.camera.setPreviewDisplay(this.surfaceHolder);
                this.surfaceHolder.addCallback((SurfaceHolder.Callback)this);
            } else {
                this.camera.setPreviewTexture(this.surfaceTexture);
            }
        }
        catch (IOException e) {
            Log.d((String)"IOException", (String)e.getMessage());
            return -20;
        }
        Camera.Parameters cp = this.camera.getParameters();
        cp.setPreviewSize(this.param.width, this.param.height);
        cp.setPreviewFormat(this.param.format);
        try {
            this.camera.setParameters(cp);
        }
        catch (RuntimeException e) {
            Log.d((String)"RuntimeException", (String)e.getMessage());
            return -30;
        }
        this.camera.setPreviewCallback((Camera.PreviewCallback)this);
        this.camera.startPreview();
        this.isRunning = true;
        return 0;
    }

    public void Stop() {
        this.isRunning = false;
        if (this.camera == null) {
            return;
        }
        if (this.surfaceHolder != null) {
            this.surfaceHolder.removeCallback((SurfaceHolder.Callback)this);
        }
        this.camera.setPreviewCallback(null);
        this.camera.stopPreview();
        this.camera.release();
        this.camera = null;
    }

    native void PushFrame(byte[] var1, int var2, long var3);

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (this.isRunning) {
            this.PushFrame(data, data.length, this.userData);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d((String)"PjCamera", (String)"VideoCaptureAndroid::surfaceChanged");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d((String)"PjCamera", (String)"VideoCaptureAndroid::surfaceCreated");
        try {
            if (this.camera != null) {
                this.camera.setPreviewDisplay(holder);
            }
        }
        catch (IOException e) {
            Log.e((String)"PjCamera", (String)"Failed to set preview surface!", (Throwable)e);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d((String)"PjCamera", (String)"VideoCaptureAndroid::surfaceDestroyed");
        try {
            if (this.camera != null) {
                this.camera.setPreviewDisplay(null);
            }
        }
        catch (IOException e) {
            Log.e((String)"PjCamera", (String)"Failed to clear preview surface!", (Throwable)e);
        }
        catch (RuntimeException e) {
            Log.w((String)"PjCamera", (String)"Clear preview surface useless", (Throwable)e);
        }
    }

    public class Param {
        public int width;
        public int height;
        public int format;
        public int fps1000;
    }
}

