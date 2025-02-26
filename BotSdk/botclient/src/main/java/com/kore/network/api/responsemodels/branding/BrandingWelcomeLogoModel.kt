package com.kore.network.api.responsemodels.branding

import com.google.gson.annotations.SerializedName

data class BrandingWelcomeLogoModel(
    @SerializedName("logo_url")
    val logoUrl: String = ""
)

fun BrandingWelcomeLogoModel.updateWith(configModel: BrandingWelcomeLogoModel): BrandingWelcomeLogoModel {
    return this.copy(
        logoUrl = configModel.logoUrl.ifEmpty { logoUrl },
    )
}