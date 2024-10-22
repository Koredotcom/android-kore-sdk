package com.kore.ui.row.listener

interface ChatContentStateListener {
    fun onSaveState(messageId: String, value: Any?, key: String)
}