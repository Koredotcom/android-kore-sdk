package kore.botssdk.audiocodes.webrtcclient.Permissions;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import kore.botssdk.application.BotApplication;


public class PermissionManager implements IPermissionManager
{
    private static final String TAG = "PermissionManager";

    private List<PermissionRequest> currentPermissionRequestList;

    private static PermissionManager instance;

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 3123;



    public static PermissionManager getInstance()
    {
        if (instance == null)
        {
            instance = new PermissionManager();
        }
        return instance;
    }

    public static boolean isPermissionManagerMethodImplemented()
    {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * @param permissionType - the permission need to check
     * @return true -  if the permission is granted
     */
    @Override
    public boolean checkPermission(PermissionManagerType permissionType)
    {
        BotApplication.getGlobalContext().checkCallingOrSelfPermission(permissionType.getTypeName());
        int permissionCheck = ContextCompat.checkSelfPermission(BotApplication.getGlobalContext(), permissionType.getTypeName());
        boolean permissionCheckState = permissionCheck == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "checkPermission  permissionType: "+permissionType+" state: "+(permissionCheckState?"granted":"revoked"));
        return permissionCheckState;
    }

    /**
     * @param activity          -  the activity to return the result too
     * @param permissionType    - contain the permission need
     * @param permissionRequest - contain and implementation of the execution method we need to trigger after the result
     */
    public void requestPermission(Activity activity, PermissionManagerType permissionType, PermissionRequest permissionRequest)
    {
        List<PermissionManagerType> permissionTypeList = new ArrayList<PermissionManagerType>();
        permissionTypeList.add(permissionType);
        List<PermissionRequest> permissionRequestList = new ArrayList<PermissionRequest>();
        permissionRequestList.add(permissionRequest);
        requestPermission(activity, permissionTypeList, permissionRequestList);
    }

    /**
     * @param activity              -  the activity to return the result too
     * @param permissionTypeList    - a list contating PermissionManagerType for each permison we need
     * @param permissionRequestList - a list of PermissionRequest for each execution method we need
     */
    public void requestPermission(Activity activity, List<PermissionManagerType> permissionTypeList, List<PermissionRequest> permissionRequestList)
    {
        if (permissionTypeList.size() != permissionRequestList.size())
        {
            Log.e(TAG, "the permissionType list and the grantableRequest list must be the same size");
            return;
        }

        //if the os is under 23 don't try to request any permission
        if (!isPermissionManagerMethodImplemented())
        {
            for (int i = 0; i < permissionTypeList.size(); i++)
            {
                PermissionManagerType permissionManagerType = permissionTypeList.get(i);
                PermissionRequest permissionRequest = permissionRequestList.get(i);

                // Pre-Marshmallow
                Log.d(TAG, "Pre-Marshmallow device we cant check permissions, automatically grant: " + permissionManagerType.toString());
               // boolean isLastPermission = i == (permissionTypeList.size()-1);
                permissionRequest.granted();
                permissionRequest.allResults(true);
            }
            return;
        }

        for (int i = 0; i < permissionTypeList.size(); i++)
        {
            PermissionManagerType permissionManagerType = permissionTypeList.get(i);
            PermissionRequest permissionRequest = permissionRequestList.get(i);

            boolean isLastPermission = i == (permissionTypeList.size()-1);
            int permissionCheck = ContextCompat.checkSelfPermission(BotApplication.getGlobalContext(), permissionManagerType.getTypeName());
            if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            {

                permissionRequest.granted();
                //remove the current request because it already granted
                permissionTypeList.remove(i);
                permissionRequestList.remove(i);
                i--;
                continue;
            }
            if (activity == null)
            {
                //call_activity the cancel action if we can't trigger the request from an Activity
                permissionRequest.revoked();
            }
        }

        String[] permissionArray = new String[permissionTypeList.size()];
        currentPermissionRequestList = permissionRequestList;
        for (int i = 0; i < permissionTypeList.size(); i++)
        {
            PermissionManagerType permissionManagerType = permissionTypeList.get(i);
            permissionArray[i] = permissionManagerType.getTypeName();

        }

        // List<String> permissionTypeList = new ArrayList<String>();

        Log.d(TAG, "checkPermission permissionType: " + currentPermissionRequestList);

        activity.requestPermissions(permissionArray, REQUEST_CODE_ASK_PERMISSIONS);
    }

    public void requestAllPermissions(Activity activity, PermissionRequest permissionRequest)
    {


        List<PermissionManagerType> permissionTypeList = new ArrayList<PermissionManagerType>();
        for (PermissionManagerType permissionManagerType: PermissionManagerType.values())
        {
           if (!checkPermission(permissionManagerType)) {
               permissionTypeList.add(permissionManagerType);
           }
        }

        List<PermissionRequest> permissionRequestList = new ArrayList<PermissionRequest>();
        PermissionRequest fakePermissionRequest = new PermissionRequest()
        {
            @Override
            public void granted() {
            }

            @Override
            public void revoked() {
            }

            @Override
            public void allResults(boolean allGranted) {
            }
        };


        for (PermissionManagerType permissionManagerType : permissionTypeList)
        {
            permissionRequestList.add(fakePermissionRequest);
        }
        if (permissionRequestList.size() > 0)
        {
            permissionRequestList.remove(0);
        }

        if (permissionTypeList.size()<=0)
        {
            //return if there is no need for permission request
            permissionRequest.granted();
            permissionRequest.allResults(true);
            return;
        }
        //only the last permission request need to be real all the other are empty
        permissionRequestList.add(permissionRequest);
        requestPermission(activity, permissionTypeList, permissionRequestList);
    }

    public List<PermissionRequest> getPermissionRequestList()
    {
        return currentPermissionRequestList;
    }

    public static boolean hasPermissionInManifest(String permission)
    {
        try {
            PackageInfo info = BotApplication.getGlobalContext().getPackageManager().getPackageInfo
                    (BotApplication.getGlobalContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void askForSystemAlertWindowPermission() {//}), int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BotApplication.getGlobalContext().getPackageName()));
        ///activity.startActivityForResult(intent, requestCode);
        BotApplication.getGlobalContext().startActivity(intent);
    }
}