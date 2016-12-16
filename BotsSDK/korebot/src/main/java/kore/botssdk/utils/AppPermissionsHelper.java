package kore.botssdk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;


/**
 * Created by Ramachandra on 03/12/16.
 */
public class AppPermissionsHelper {
   /* public static boolean hasPermission(String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionLength = permission.length;
            for (int i=0;i<permissionLength;i++) {
                shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                        ContextCompat.checkSelfPermission(KoreAppControl.getInstance().getContext(), permission[i]) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return shouldShowRequestPermissionRationale;
    }*/

    public static void requestForPermission(Activity activity, String permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission},
                    requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission},
                    requestCode);
        }
    }

    /**
     *
     * @param activity : For which activity
     * @param requestCode : The user's request code
     * @param permission : List of desired permissions.
     */
    public static void requestForPermission(Activity activity, int requestCode, String... permission) {
        boolean shouldShowRequestPermissionRationale = shouldShowRationale(activity, permission);

        if (shouldShowRequestPermissionRationale) {
            ActivityCompat.requestPermissions(activity, permission,
                    requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permission,
                    requestCode);
        }
    }

    public static boolean shouldShowRationale(Activity activity, String... permission) {
        boolean shouldShowRequestPermissionRationale = false;
        int permissionLength = permission.length;
        for (int i=0;i<permissionLength;i++) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[i]);
        }
        return shouldShowRequestPermissionRationale;
    }

    public static void requestForPermission(Activity activity, String permission[], int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            ActivityCompat.requestPermissions(activity, permission,
                    requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permission,
                    requestCode);
        }
    }

    public static void requestForPermission(Fragment fragment, String permission, int requestCode) {
        //TODO: check for all permissions shouldShowRequestPermissionRationale
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            //TODO: need to show a dialog with a message to the user why should he grant permission
            //call the below method on click OK of the dialog. For now if, else block code remains same
            fragment.requestPermissions(new String[]{permission},
                    requestCode);
        } else {
            fragment.requestPermissions(new String[]{permission},
                    requestCode);
        }
    }

    public static void requestForPermission(Fragment fragment, String[] permissions, int requestCode) {
        //TODO: check for all permissions shouldShowRequestPermissionRationale
        if (fragment.shouldShowRequestPermissionRationale(permissions[0])) {
            //TODO: need to show a dialog with a message to the user why should he grant permission
            //call the below method on click OK of the dialog. For now if, else block code remains same
            fragment.requestPermissions(permissions,
                    requestCode);

        } else {
            fragment.requestPermissions(permissions,
                    requestCode);
        }
    }

    public static boolean shouldShowRationale(Fragment fragment, String... permission) {
        boolean shouldShowRequestPermissionRationale = false;
        int permissionLength = permission.length;
        for (int i=0;i<permissionLength;i++) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || fragment.shouldShowRequestPermissionRationale(permission[i]);
        }
        return shouldShowRequestPermissionRationale;
    }

   /* public static boolean hasPermissionForLocation() {
        return AppPermissionsHelper.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }*/

    public static void startInstalledAppDetailsActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivity(intent);
    }
}
