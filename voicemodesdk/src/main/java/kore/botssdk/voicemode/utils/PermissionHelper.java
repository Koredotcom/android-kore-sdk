package kore.botssdk.voicemode.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing permissions required by WebRTC.
 */
public class PermissionHelper {

    public static final int PERMISSION_REQUEST_CODE = 1001;

    public static final String[] AUDIO_CALL_PERMISSIONS = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    public static final String[] VIDEO_CALL_PERMISSIONS = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    /**
     * Check if all audio call permissions are granted.
     * @param context The context
     * @return true if all permissions are granted
     */
    public static boolean hasAudioPermissions(Context context) {
        return hasPermissions(context, AUDIO_CALL_PERMISSIONS);
    }

    /**
     * Check if all video call permissions are granted.
     * @param context The context
     * @return true if all permissions are granted
     */
    public static boolean hasVideoPermissions(Context context) {
        return hasPermissions(context, VIDEO_CALL_PERMISSIONS);
    }

    /**
     * Check if specific permissions are granted.
     * @param context The context
     * @param permissions Array of permission strings
     * @return true if all are granted
     */
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Request audio call permissions.
     * @param activity The activity
     */
    public static void requestAudioPermissions(Activity activity) {
        requestPermissions(activity, AUDIO_CALL_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    /**
     * Request video call permissions.
     * @param activity The activity
     */
    public static void requestVideoPermissions(Activity activity) {
        requestPermissions(activity, VIDEO_CALL_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    /**
     * Request specific permissions.
     * @param activity The activity
     * @param permissions Array of permission strings
     * @param requestCode Request code for result
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        List<String> permissionsNeeded = new ArrayList<>();
        
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                    activity,
                    permissionsNeeded.toArray(new String[0]),
                    requestCode
            );
        }
    }

    /**
     * Get missing permissions from required permissions.
     * @param context The context
     * @param permissions Required permissions
     * @return Array of missing permissions
     */
    public static String[] getMissingPermissions(Context context, String[] permissions) {
        List<String> missing = new ArrayList<>();
        
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                missing.add(permission);
            }
        }
        
        return missing.toArray(new String[0]);
    }

    /**
     * Check if should show permission rationale for any of the permissions.
     * @param activity The activity
     * @param permissions Permissions to check
     * @return true if rationale should be shown
     */
    public static boolean shouldShowRationale(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check permission results and return if all were granted.
     * @param grantResults Grant results from onRequestPermissionsResult
     * @return true if all permissions were granted
     */
    public static boolean allPermissionsGranted(int[] grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get required Bluetooth permissions for Android 12+.
     * @return Array of Bluetooth permissions
     */
    public static String[] getBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[] {
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        }
        return new String[] {
                Manifest.permission.BLUETOOTH
        };
    }

    /**
     * Check if Bluetooth permissions are granted.
     * @param context The context
     * @return true if granted
     */
    public static boolean hasBluetoothPermission(Context context) {
        return hasPermissions(context, getBluetoothPermissions());
    }
}
