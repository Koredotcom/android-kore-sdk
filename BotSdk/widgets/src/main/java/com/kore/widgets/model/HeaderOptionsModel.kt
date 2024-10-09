package com.kore.widgets.model

data class HeaderOptionsModel(
    var type: String?,
    var text: String? = null,
    var url: UrlModel? = null,
    var button: ButtonModel? = null,
    var menu: List<ButtonModel>? = null,
    var image: ImageModel? = null
)