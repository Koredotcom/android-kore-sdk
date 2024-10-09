package com.kore.widgets.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Visibility(
    @SerializedName("namespace")
    val namespace: String? = null,
    @SerializedName("namespaceIds")
    val namespaceIds: Array<String>
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Visibility

        if (namespace != other.namespace) return false
        if (!namespaceIds.contentEquals(other.namespaceIds)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace?.hashCode() ?: 0
        result = 31 * result + namespaceIds.contentHashCode()
        return result
    }
}