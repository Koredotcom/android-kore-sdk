package com.kore.ui.audiocodes.webrtcclient.permissions

import android.content.Context

interface IPermissionManager {
    fun checkPermission(context: Context, permissionType: PermissionManagerType): Boolean
}
