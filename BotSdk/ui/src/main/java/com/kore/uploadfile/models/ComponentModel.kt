package com.kore.uploadfile.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ComponentModel(
    @SerializedName("componentType")
    val mediaType: String? = null,

    @SerializedName("componentId")
    val mediaFileName: String? = null,
    val mediaFilePath: String? = null,
    val mediaThumbnail: String? = null,
    val componentBody: String? = null,
    val componentData: HashMap<String, Any?>,
    val componentDataJson: String? = null,

    @SerializedName("componentFileId")
    val mediaFileId: String? = null,

    @Expose(serialize = false)
    val dropboxFileId: String? = null,

    @SerializedName("componentLength")
    val mediaDuration: String? = null,
    val referenceId: String? = null,

    @Expose(serialize = false)
    val messageID: String? = null,
    val componentTitle: String? = null,
    val componentDescription: String? = null,

    @Expose(serialize = false)
    val isDelete: Boolean = false,

    @Expose(serialize = false)
    val imageId: Int = 0,

    @Expose(serialize = false)
    val mediaKey: String? = null,

    @SerializedName("templateData")
    val templateData: HashMap<String, Any>? = null,
    val isShowLoader: Boolean = false,
    val fileSize: String? = null
) : Serializable