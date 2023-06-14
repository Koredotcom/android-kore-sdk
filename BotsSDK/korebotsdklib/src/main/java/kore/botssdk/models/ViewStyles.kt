package kore.botssdk.models

import com.google.gson.annotations.SerializedName

class ViewStyles(
    @SerializedName("box-shadow")
    val boxShadow: Int? = null,

    @SerializedName("font-size")
    val fontSize: Float? = null,

    @SerializedName("font-family")
    val fontFamily: String? = null,

    @SerializedName("font-style")
    val fontStyle: String? = null,

    @SerializedName("font-weight")
    val fontWeight: Int? = null,

    @SerializedName("font-color")
    val fontColor: String? = null,

    @SerializedName("background-color")
    val backgroundColor: String? = null,

    @SerializedName("border-width")
    val borderWidth: Int? = null,

    @SerializedName("border-radius")
    val borderRadius: Int? = null,

    @SerializedName("border-color")
    val borderColor: String? = null
)