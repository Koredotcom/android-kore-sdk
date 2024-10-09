package com.kore.ui.audiocodes.webrtcclient.db

import java.util.Locale

data class NativeDBObject(
    var id: String? = null,
    var displayName: String = "",
    var photoURI: String? = null,
    var photoThumbnailURI: String? = null,
    @JvmField
    var phones: MutableList<NativeDBPhones?>? = null
) : Comparable<NativeDBObject> {

    override fun compareTo(other: NativeDBObject): Int {
        return displayName.lowercase(Locale.getDefault())
            .compareTo(other.displayName.lowercase(Locale.getDefault()))
    }
}
