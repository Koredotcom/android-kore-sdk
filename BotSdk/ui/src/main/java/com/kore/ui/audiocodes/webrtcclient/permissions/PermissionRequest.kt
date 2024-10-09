package com.kore.ui.audiocodes.webrtcclient.permissions

interface PermissionRequest {
    fun granted()
    fun revoked()
    fun allResults(allGranted: Boolean)
}
