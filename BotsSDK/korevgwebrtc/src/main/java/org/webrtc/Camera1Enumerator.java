/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.hardware.Camera
 *  android.hardware.Camera$CameraInfo
 *  android.hardware.Camera$Parameters
 *  android.hardware.Camera$Size
 *  android.os.SystemClock
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.hardware.Camera;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.webrtc.Camera1Capturer;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.Logging;
import org.webrtc.Size;

public class Camera1Enumerator
implements CameraEnumerator {
    private static final String TAG = "Camera1Enumerator";
    private static List<List<CameraEnumerationAndroid.CaptureFormat>> cachedSupportedFormats;
    private final boolean captureToTexture;

    public Camera1Enumerator() {
        this(true);
    }

    public Camera1Enumerator(boolean captureToTexture) {
        this.captureToTexture = captureToTexture;
    }

    @Override
    public String[] getDeviceNames() {
        ArrayList<String> namesList = new ArrayList<String>();
        for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            String name = Camera1Enumerator.getDeviceName(i);
            if (name != null) {
                namesList.add(name);
                Logging.d(TAG, "Index: " + i + ". " + name);
                continue;
            }
            Logging.e(TAG, "Index: " + i + ". Failed to query camera name.");
        }
        String[] namesArray = new String[namesList.size()];
        return namesList.toArray(namesArray);
    }

    @Override
    public boolean isFrontFacing(String deviceName) {
        Camera.CameraInfo info = Camera1Enumerator.getCameraInfo(Camera1Enumerator.getCameraIndex(deviceName));
        return info != null && info.facing == 1;
    }

    @Override
    public boolean isBackFacing(String deviceName) {
        Camera.CameraInfo info = Camera1Enumerator.getCameraInfo(Camera1Enumerator.getCameraIndex(deviceName));
        return info != null && info.facing == 0;
    }

    @Override
    public List<CameraEnumerationAndroid.CaptureFormat> getSupportedFormats(String deviceName) {
        return Camera1Enumerator.getSupportedFormats(Camera1Enumerator.getCameraIndex(deviceName));
    }

    @Override
    public CameraVideoCapturer createCapturer(String deviceName, CameraVideoCapturer.CameraEventsHandler eventsHandler) {
        return new Camera1Capturer(deviceName, eventsHandler, this.captureToTexture);
    }

    @Nullable
    private static Camera.CameraInfo getCameraInfo(int index) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        try {
            Camera.getCameraInfo((int)index, (Camera.CameraInfo)info);
        }
        catch (Exception e) {
            Logging.e(TAG, "getCameraInfo failed on index " + index, e);
            return null;
        }
        return info;
    }

    static synchronized List<CameraEnumerationAndroid.CaptureFormat> getSupportedFormats(int cameraId) {
        if (cachedSupportedFormats == null) {
            cachedSupportedFormats = new ArrayList<List<CameraEnumerationAndroid.CaptureFormat>>();
            for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
                cachedSupportedFormats.add(Camera1Enumerator.enumerateFormats(i));
            }
        }
        return cachedSupportedFormats.get(cameraId);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static List<CameraEnumerationAndroid.CaptureFormat> enumerateFormats(int cameraId) {
        Camera.Parameters parameters;
        Logging.d(TAG, "Get supported formats for camera index " + cameraId + ".");
        long startTimeMs = SystemClock.elapsedRealtime();
        Camera camera = null;
        try {
            Logging.d(TAG, "Opening camera with index " + cameraId);
            camera = Camera.open((int)cameraId);
            parameters = camera.getParameters();
        }
        catch (RuntimeException e) {
            Logging.e(TAG, "Open camera failed on camera index " + cameraId, e);
            ArrayList<CameraEnumerationAndroid.CaptureFormat> arrayList = new ArrayList<CameraEnumerationAndroid.CaptureFormat>();
            return arrayList;
        }
        finally {
            if (camera != null) {
                camera.release();
            }
        }
        ArrayList<CameraEnumerationAndroid.CaptureFormat> formatList = new ArrayList<CameraEnumerationAndroid.CaptureFormat>();
        try {
            int minFps = 0;
            int maxFps = 0;
            List listFpsRange = parameters.getSupportedPreviewFpsRange();
            if (listFpsRange != null) {
                int[] range = (int[])listFpsRange.get(listFpsRange.size() - 1);
                minFps = range[0];
                maxFps = range[1];
            }
            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                formatList.add(new CameraEnumerationAndroid.CaptureFormat(size.width, size.height, minFps, maxFps));
            }
        }
        catch (Exception e) {
            Logging.e(TAG, "getSupportedFormats() failed on camera index " + cameraId, e);
        }
        long endTimeMs = SystemClock.elapsedRealtime();
        Logging.d(TAG, "Get supported formats for camera index " + cameraId + " done. Time spent: " + (endTimeMs - startTimeMs) + " ms.");
        return formatList;
    }

    static List<Size> convertSizes(List<Camera.Size> cameraSizes) {
        ArrayList<Size> sizes = new ArrayList<Size>();
        for (Camera.Size size : cameraSizes) {
            sizes.add(new Size(size.width, size.height));
        }
        return sizes;
    }

    static List<CameraEnumerationAndroid.CaptureFormat.FramerateRange> convertFramerates(List<int[]> arrayRanges) {
        ArrayList<CameraEnumerationAndroid.CaptureFormat.FramerateRange> ranges = new ArrayList<CameraEnumerationAndroid.CaptureFormat.FramerateRange>();
        for (int[] range : arrayRanges) {
            ranges.add(new CameraEnumerationAndroid.CaptureFormat.FramerateRange(range[0], range[1]));
        }
        return ranges;
    }

    static int getCameraIndex(String deviceName) {
        Logging.d(TAG, "getCameraIndex: " + deviceName);
        for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            if (!deviceName.equals(Camera1Enumerator.getDeviceName(i))) continue;
            return i;
        }
        throw new IllegalArgumentException("No such camera: " + deviceName);
    }

    @Nullable
    static String getDeviceName(int index) {
        Camera.CameraInfo info = Camera1Enumerator.getCameraInfo(index);
        if (info == null) {
            return null;
        }
        String facing = info.facing == 1 ? "front" : "back";
        return "Camera " + index + ", Facing " + facing + ", Orientation " + info.orientation;
    }
}

