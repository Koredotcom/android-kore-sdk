package com.kore.model

data class BotRequest(
    val message: BotMessage?,
    private val botInfo: BotInfoModel?,
    private val id: Long,
    override val messageId: String = "",
    override val timeMillis: Long = 0,
    override val isSend: Boolean = true,
    override val from: String? = null,
    override val createdOn: String = "",
    override val formattedTime: String = "",
    override val messageDate: String = "",
    private val resourceId: String? = "/bot.message"
) : BaseBotMessage