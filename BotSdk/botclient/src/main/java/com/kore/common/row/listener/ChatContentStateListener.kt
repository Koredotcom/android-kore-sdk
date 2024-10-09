package com.kore.common.row.listener

interface ChatContentStateListener {
    fun onSaveState(messageId: String, value: Any?, key: String)
}