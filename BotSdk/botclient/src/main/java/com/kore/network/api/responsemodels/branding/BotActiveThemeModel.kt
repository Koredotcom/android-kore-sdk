package com.kore.network.api.responsemodels.branding

data class BotActiveThemeModel (
    val _id : String = "",
    val streamId : String = "",
    val activeTheme : Boolean,
    val createdBy : String = "",
    val createdOn : String = "",
    val defaultTheme : Boolean,
    val lastModifiedBy : String = "",
    val lastModifiedOn : String = "",
    val refId : String = "",
    val state : String = "",
    val themeName : String = "",
    val v3 : BotBrandingModel,
)