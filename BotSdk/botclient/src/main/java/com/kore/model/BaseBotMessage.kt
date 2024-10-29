package com.kore.model

interface BaseBotMessage {
    val from: String?
    val isSend: Boolean
    val createdOn: String
    val formattedTime: String
    val timeMillis: Long
    val messageId: String
    val messageDate: String
}