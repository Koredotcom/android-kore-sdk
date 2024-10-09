package com.kore.ui.audiocodes.webrtcclient.permissions

import android.content.Context

class PermissionWrapper {
    private var iPermissionManager: IPermissionManager? = null

    fun checkPermission(context: Context, permissionType: PermissionManagerType): Boolean {
        return if (iPermissionManager == null) {
            true
        } else iPermissionManager!!.checkPermission(context, permissionType)
    }

    companion object {
        private const val TAG = "PermissionWrapper"
        const val READ_CONTACTS = "android.permission.READ_CONTACTS"
        const val READ_PHONE_STATE = "android.permission.READ_PHONE_STATE"
        const val BODY_SENSORS = "android.permission.BODY_SENSORS"
        const val WRITE_CALENDAR = "android.permission.WRITE_CALENDAR"
        const val CAMERA = "android.permission.CAMERA"

        //  public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
        const val RECORD_AUDIO = "android.permission.RECORD_AUDIO"
        const val READ_SMS = "android.permission.READ_SMS"
        const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
    }
}
