package com.kore.network.api.responsemodels.branding

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class BrandingHeaderModel(
    @SerializedName("bg_color")
    var bgColor: String? = null,
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("style")
    val style: String? = null,
    @SerializedName("icons_color")
    var iconsColor: String? = null,
    @SerializedName("avatar_bg_color")
    var avatarBgColor: String? = null,
    @SerializedName("icon")
    val icon: BrandingIconModel? = null,
    @SerializedName("title")
    val title: BrandingTitleModel? = null,
    @SerializedName("sub_title")
    val subTitle: BrandingTitleModel? = null,
    @SerializedName("buttons")
    val buttons: BrandingHeaderButtonsModel? = null
) : Parcelable