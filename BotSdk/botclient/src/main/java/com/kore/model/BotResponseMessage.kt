package com.kore.model

data class BotResponseMessage (
    val type: String? = null,
    val component: ComponentModel? = null,
    var cInfo: ComponentInfo?
)