package com.kore.ui.audiocodes.webrtcclient.permissions

enum class PermissionManagerType(@JvmField val typeName: String) {
    CONTACTS(PermissionWrapper.READ_CONTACTS),
    PHONE(PermissionWrapper.READ_PHONE_STATE),
    CAMERA(PermissionWrapper.CAMERA),
    MICROPHONE(PermissionWrapper.RECORD_AUDIO)

}
