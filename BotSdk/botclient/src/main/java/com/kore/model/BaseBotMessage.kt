package com.kore.model

import java.text.SimpleDateFormat
import java.util.Locale

interface BaseBotMessage {
    val from: String?
    val isSend: Boolean
    val createdOn: String
    val formattedTime: String
    val timeStamp: Long
    val messageId: String
    val messageDate: String
    val isoFormatter: SimpleDateFormat
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
}