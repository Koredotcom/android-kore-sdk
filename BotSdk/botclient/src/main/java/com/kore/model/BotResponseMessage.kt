package com.kore.model

data class BotResponseMessage (
    val type: String? = null,
    var component: ComponentModel? = null,
    var cInfo: ComponentInfo?
)