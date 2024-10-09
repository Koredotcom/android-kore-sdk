package com.kore.model

class BotHistory(
    val total: Int = 0,
    val moreAvailable: Boolean = false,
    val icon: String,
    val messages: List<BotHistoryMessage> = emptyList()
)