/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.graphics.Rect
 *  android.graphics.SurfaceTexture
 *  android.hardware.camera2.CameraCharacteristics
 *  android.hardware.camera2.CameraManager
 *  android.hardware.camera2.params.StreamConfigurationMap
 *  android.os.Build$VERSION
 *  android.os.SystemClock
 *  androidx.annotation.Nullable
 *  android.util.AndroidException
 *  android.util.Range
 *  android.util.Size
 */
package org.webrtc;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.util.AndroidException;
import android.util.Range;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.webrtc.Camera2Capturer;
import org.webrtc.CameraEnumerationAndroid;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.Logging;
import org.webrtc.Size;

@TargetApi(value=21)
public class Camera2Enumerator
implements CameraEnumerator {
    private static final String TAG = "Camera2Enumerator";
    private static final double NANO_SECONDS_PER_SECOND = 1.0E9;
    private static final Map<String, List<CameraEnumerationAndroid.CaptureFormat>> cachedSupportedFormats = new HashMap<String, List<CameraEnumerationAndroid.CaptureFormat>>();
    final Context context;
    @Nullable
    final CameraManager cameraManager;

    public Camera2Enumerator(Context context) {
        this.context = context;
        this.cameraManager = (CameraManager)context.getSystemService("camera");
    }

    @Override
    public String[] getDeviceNames() {
        try {
            return this.cameraManager.getCameraIdList();
        }
        catch (AndroidException e) {
            Logging.e(TAG, "Camera access exception", e);
            return new String[0];
        }
    }

    @Override
    public boolean isFrontFacing(String deviceName) {
        CameraCharacteristics characteristics = this.getCameraCharacteristics(deviceName);
        return characteristics != null && (Integer)characteristics.get(CameraCharacteristics.LENS_FACING) == 0;
    }

    @Override
    public boolean isBackFacing(String deviceName) {
        CameraCharacteristics characteristics = this.getCameraCharacteristics(deviceName);
        return characteristics != null && (Integer)characteristics.get(CameraCharacteristics.LENS_FACING) == 1;
    }

    @Override
    @Nullable
    public List<CameraEnumerationAndroid.CaptureFormat> getSupportedFormats(String deviceName) {
        return Camera2Enumerator.getSupportedFormats(this.context, deviceName);
    }

    @Override
    public CameraVideoCapturer createCapturer(String deviceName, CameraVideoCapturer.CameraEventsHandler eventsHandler) {
        return new Camera2Capturer(this.context, deviceName, eventsHandler);
    }

    @Nullable
    private CameraCharacteristics getCameraCharacteristics(String deviceName) {
        try {
            return this.cameraManager.getCameraCharacteristics(deviceName);
        }
        catch (AndroidException e) {
            Logging.e(TAG, "Camera access exception", e);
            return null;
        }
    }

    public static boolean isSupported(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        CameraManager cameraManager = (CameraManager)context.getSystemService("camera");
        try {
            String[] cameraIds;
            for (String id : cameraIds = cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                if ((Integer)characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) != 2) continue;
                return false;
            }
        }
        catch (AndroidException | RuntimeException e) {
            Logging.e(TAG, "Failed to check if camera2 is supported", e);
            return false;
        }
        return true;
    }

    static int getFpsUnitFactor(Range<Integer>[] fpsRanges) {
        if (fpsRanges.length == 0) {
            return 1000;
        }
        return (Integer)fpsRanges[0].getUpper() < 1000 ? 1000 : 1;
    }

    static List<Size> getSupportedSizes(CameraCharacteristics cameraCharacteristics) {
        StreamConfigurationMap streamMap = (StreamConfigurationMap)cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        int supportLevel = (Integer)cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        android.util.Size[] nativeSizes = streamMap.getOutputSizes(SurfaceTexture.class);
        List<Size> sizes = Camera2Enumerator.convertSizes(nativeSizes);
        if (Build.VERSION.SDK_INT < 22 && supportLevel == 2) {
            Rect activeArraySize = (Rect)cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            ArrayList<Size> filteredSizes = new ArrayList<Size>();
            for (Size size : sizes) {
                if (activeArraySize.width() * size.height != activeArraySize.height() * size.width) continue;
                filteredSizes.add(size);
            }
            return filteredSizes;
        }
        return sizes;
    }

    @Nullable
    static List<CameraEnumerationAndroid.CaptureFormat> getSupportedFormats(Context context, String cameraId) {
        return Camera2Enumerator.getSupportedFormats((CameraManager)context.getSystemService("camera"), cameraId);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    static List<CameraEnumerationAndroid.CaptureFormat> getSupportedFormats(CameraManager cameraManager, String cameraId) {
        Map<String, List<CameraEnumerationAndroid.CaptureFormat>> map = cachedSupportedFormats;
        synchronized (map) {
            CameraCharacteristics cameraCharacteristics;
            if (cachedSupportedFormats.containsKey(cameraId)) {
                return cachedSupportedFormats.get(cameraId);
            }
            Logging.d(TAG, "Get supported formats for camera index " + cameraId + ".");
            long startTimeMs = SystemClock.elapsedRealtime();
            try {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            }
            catch (Exception ex) {
                Logging.e(TAG, "getCameraCharacteristics()", ex);
                return new ArrayList<CameraEnumerationAndroid.CaptureFormat>();
            }
            StreamConfigurationMap streamMap = (StreamConfigurationMap)cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Range[] fpsRanges = (Range[])cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            List<CameraEnumerationAndroid.CaptureFormat.FramerateRange> framerateRanges = Camera2Enumerator.convertFramerates(fpsRanges, Camera2Enumerator.getFpsUnitFactor(fpsRanges));
            List<Size> sizes = Camera2Enumerator.getSupportedSizes(cameraCharacteristics);
            int defaultMaxFps = 0;
            for (CameraEnumerationAndroid.CaptureFormat.FramerateRange framerateRange : framerateRanges) {
                defaultMaxFps = Math.max(defaultMaxFps, framerateRange.max);
            }
            ArrayList<CameraEnumerationAndroid.CaptureFormat> formatList = new ArrayList<CameraEnumerationAndroid.CaptureFormat>();
            for (Size size : sizes) {
                long minFrameDurationNs = 0L;
                try {
                    minFrameDurationNs = streamMap.getOutputMinFrameDuration(SurfaceTexture.class, new android.util.Size(size.width, size.height));
                }
                catch (Exception exception) {
                    // empty catch block
                }
                int maxFps = minFrameDurationNs == 0L ? defaultMaxFps : (int)Math.round(1.0E9 / (double)minFrameDurationNs) * 1000;
                formatList.add(new CameraEnumerationAndroid.CaptureFormat(size.width, size.height, 0, maxFps));
                Logging.d(TAG, "Format: " + size.width + "x" + size.height + "@" + maxFps);
            }
            cachedSupportedFormats.put(cameraId, formatList);
            long l = SystemClock.elapsedRealtime();
            Logging.d(TAG, "Get supported formats for camera index " + cameraId + " done. Time spent: " + (l - startTimeMs) + " ms.");
            return formatList;
        }
    }

    private static List<Size> convertSizes(android.util.Size[] cameraSizes) {
        ArrayList<Size> sizes = new ArrayList<Size>();
        for (android.util.Size size : cameraSizes) {
            sizes.add(new Size(size.getWidth(), size.getHeight()));
        }
        return sizes;
    }

    static List<CameraEnumerationAndroid.CaptureFormat.FramerateRange> convertFramerates(Range<Integer>[] arrayRanges, int unitFactor) {
        ArrayList<CameraEnumerationAndroid.CaptureFormat.FramerateRange> ranges = new ArrayList<CameraEnumerationAndroid.CaptureFormat.FramerateRange>();
        for (Range<Integer> range : arrayRanges) {
            ranges.add(new CameraEnumerationAndroid.CaptureFormat.FramerateRange((Integer)range.getLower() * unitFactor, (Integer)range.getUpper() * unitFactor));
        }
        return ranges;
    }
}

