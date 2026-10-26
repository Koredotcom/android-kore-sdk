/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Matrix
 *  android.view.WindowManager
 */
package org.webrtc;

import android.content.Context;
import android.graphics.Matrix;
import android.view.WindowManager;
import org.webrtc.TextureBufferImpl;
import org.webrtc.VideoFrame;

interface CameraSession {
    public void stop();

    public static int getDeviceOrientation(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        switch (wm.getDefaultDisplay().getRotation()) {
            case 1: {
                return 90;
            }
            case 2: {
                return 180;
            }
            case 3: {
                return 270;
            }
        }
        return 0;
    }

    public static VideoFrame.TextureBuffer createTextureBufferWithModifiedTransformMatrix(TextureBufferImpl buffer, boolean mirror, int rotation) {
        Matrix transformMatrix = new Matrix();
        transformMatrix.preTranslate(0.5f, 0.5f);
        if (mirror) {
            transformMatrix.preScale(-1.0f, 1.0f);
        }
        transformMatrix.preRotate((float)rotation);
        transformMatrix.preTranslate(-0.5f, -0.5f);
        return buffer.applyTransformMatrix(transformMatrix, buffer.getWidth(), buffer.getHeight());
    }

    public static interface Events {
        public void onCameraOpening();

        public void onCameraError(CameraSession var1, String var2);

        public void onCameraDisconnected(CameraSession var1);

        public void onCameraClosed(CameraSession var1);

        public void onFrameCaptured(CameraSession var1, VideoFrame var2);
    }

    public static interface CreateSessionCallback {
        public void onDone(CameraSession var1);

        public void onFailure(FailureType var1, String var2);
    }

    public static enum FailureType {
        ERROR,
        DISCONNECTED;

    }
}

