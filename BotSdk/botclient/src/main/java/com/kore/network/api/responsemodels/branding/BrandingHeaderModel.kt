package com.kore.network.api.responsemodels.branding

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class BrandingHeaderModel(
    @SerializedName("bg_color")
    var bgColor : String?,
    @SerializedName("size")
    val size : String?,
    @SerializedName("style")
    val style : String?,
    @SerializedName("icons_color")
    var iconsColor : String?,
    @SerializedName("avatar_bg_color")
    var avatarBgColor : String?,
    @SerializedName("icon")
    val icon : BrandingIconModel?,
    @SerializedName("title")
    val title : BrandingTitleModel?,
    @SerializedName("sub_title")
    val subTitle : BrandingTitleModel?,
    @SerializedName("buttons")
    val buttons : BrandingHeaderButtonsModel?
) : Parcelable