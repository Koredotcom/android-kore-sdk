package com.kore.botclient

import com.kore.model.BotRequest

interface BotConnectionListener {
    fun onBotResponse(response: String?)
    fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean)
    fun onBotRequest(code: BotRequestState, botRequest: BotRequest)
    suspend fun onJwtTokenGenerated(token: String)
}