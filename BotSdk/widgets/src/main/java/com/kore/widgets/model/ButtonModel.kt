package com.kore.widgets.model

data class ButtonModel(
    val title: String? = null,
    val theme: String?,
    val type: String? = null,
    val utterance: String? = null,
    val url: String? = null,
    val image: ImageModel? = null,
    val payload: String? = null
) {
    fun getThemeValue(): String = if (theme.isNullOrEmpty()) "#4741fa" else theme
}