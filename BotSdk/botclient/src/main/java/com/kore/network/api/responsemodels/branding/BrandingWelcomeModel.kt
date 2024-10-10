package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BrandingWelcomeModel (
    @SerializedName("show")
    val show : Boolean,
    @SerializedName("layout")
    val layout : String = "",
    @SerializedName("logo")
    val logo : BrandingWelcomeLogoModel,
    @SerializedName("title")
    val title : BrandingTitleModel,
    @SerializedName("sub_title")
    val subTitle : BrandingTitleModel,
    @SerializedName("note")
    val note : BrandingTitleModel,
    @SerializedName("background")
    val background : BrandingTitleModel,
    @SerializedName("top_fonts")
    val topFonts : BrandingTitleModel,
    @SerializedName("bottom_background")
    val bottomBackground : BrandingTitleModel,
    @SerializedName("starter_box")
    val starterBox : BrandingStarterBoxModel,
    @SerializedName("promotional_content")
    val promotionalContent : PromotionalContentModel,
    @SerializedName("static_links")
    val staticLinks : StaticLinksModel
) : Serializable