package com.kore.ui.audiocodes.webrtcclient.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.kore.common.utils.LogUtils

class PermissionManager : IPermissionManager {
    private var permissionRequestList: List<PermissionRequest>? = null
        private set

    /**
     * @param permissionType - the permission need to check
     * @return true -  if the permission is granted
     */
    override fun checkPermission(context: Context, permissionType: PermissionManagerType): Boolean {
        context.checkCallingOrSelfPermission(permissionType.typeName)
        val permissionCheck = ContextCompat.checkSelfPermission(context, permissionType.typeName)
        val permissionCheckState = permissionCheck == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "checkPermission  permissionType: " + permissionType + " state: " + if (permissionCheckState) "granted" else "revoked")
        return permissionCheckState
    }

    /**
     * @param activity          -  the activity to return the result too
     * @param permissionType    - contain the permission need
     * @param permissionRequest - contain and implementation of the execution method we need to trigger after the result
     */
    fun requestPermission(activity: Activity, permissionType: PermissionManagerType, permissionRequest: PermissionRequest) {
        val permissionTypeList: MutableList<PermissionManagerType> = ArrayList()
        permissionTypeList.add(permissionType)
        val permissionRequestList: MutableList<PermissionRequest> = ArrayList()
        permissionRequestList.add(permissionRequest)
        requestPermission(activity, permissionTypeList, permissionRequestList)
    }

    /**
     * @param activity              -  the activity to return the result too
     * @param permissionTypeList    - a list contating PermissionManagerType for each permison we need
     * @param permissionRequestList - a list of PermissionRequest for each execution method we need
     */
    private fun requestPermission(
        activity: Activity,
        permissionTypeList: MutableList<PermissionManagerType>,
        permissionRequestList: MutableList<PermissionRequest>
    ) {
        if (permissionTypeList.size != permissionRequestList.size) {
            LogUtils.e(TAG, "the permissionType list and the grantableRequest list must be the same size")
            return
        }

        //if the os is under 23 don't try to request any permission
        if (!isPermissionManagerMethodImplemented) {
            for (i in permissionTypeList.indices) {
                val permissionManagerType = permissionTypeList[i]
                val permissionRequest = permissionRequestList[i]

                // Pre-Marshmallow
                Log.d(TAG, "Pre-Marshmallow device we cant check permissions, automatically grant: $permissionManagerType")
                // boolean isLastPermission = i == (permissionTypeList.size()-1);
                permissionRequest.granted()
                permissionRequest.allResults(true)
            }
            return
        }
        run {
            var i = 0
            while (i < permissionTypeList.size) {
                val permissionManagerType = permissionTypeList[i]
                val permissionRequest = permissionRequestList[i]
                val isLastPermission = i == permissionTypeList.size - 1
                val permissionCheck = ContextCompat.checkSelfPermission(activity, permissionManagerType.typeName)
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    permissionRequest.granted()
                    //remove the current request because it already granted
                    permissionTypeList.removeAt(i)
                    permissionRequestList.removeAt(i)
                    i--
                    i++
                    continue
                }
                i++
            }
        }
        val permissionArray = arrayOfNulls<String>(permissionTypeList.size)
        this.permissionRequestList = permissionRequestList
        for (i in permissionTypeList.indices) {
            val permissionManagerType = permissionTypeList[i]
            permissionArray[i] = permissionManagerType.typeName
        }

        // List<String> permissionTypeList = new ArrayList<String>();
        Log.d(TAG, "checkPermission permissionType: " + this.permissionRequestList)
        activity.requestPermissions(permissionArray, REQUEST_CODE_ASK_PERMISSIONS)
    }

    fun requestAllPermissions(activity: Activity, permissionRequest: PermissionRequest) {
        val permissionTypeList: MutableList<PermissionManagerType> = ArrayList()
        for (permissionManagerType in PermissionManagerType.values()) {
            if (!checkPermission(activity, permissionManagerType)) {
                permissionTypeList.add(permissionManagerType)
            }
        }
        val permissionRequestList: MutableList<PermissionRequest> = ArrayList()
        val fakePermissionRequest: PermissionRequest = object : PermissionRequest {
            override fun granted() {}
            override fun revoked() {}
            override fun allResults(allGranted: Boolean) {}
        }
        for (permissionManagerType in permissionTypeList) {
            permissionRequestList.add(fakePermissionRequest)
        }
        if (permissionRequestList.size > 0) {
            permissionRequestList.removeAt(0)
        }
        if (permissionTypeList.size <= 0) {
            //return if there is no need for permission request
            permissionRequest.granted()
            permissionRequest.allResults(true)
            return
        }
        //only the last permission request need to be real all the other are empty
        permissionRequestList.add(permissionRequest)
        requestPermission(activity, permissionTypeList, permissionRequestList)
    }

    companion object {
        private const val TAG = "PermissionManager"
        var instance: PermissionManager = PermissionManager()
        const val REQUEST_CODE_ASK_PERMISSIONS = 3123
        val isPermissionManagerMethodImplemented: Boolean
            get() = true

        fun hasPermissionInManifest(context: Context, permission: String): Boolean {
            try {
                val info =
                    context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
                if (info.requestedPermissions != null) {
                    for (p in info.requestedPermissions) {
                        if (p == permission) {
                            return true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun askForSystemAlertWindowPermission(context: Context) { //}), int requestCode) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
            ///activity.startActivityForResult(intent, requestCode);
            context.startActivity(intent)
        }
    }
}