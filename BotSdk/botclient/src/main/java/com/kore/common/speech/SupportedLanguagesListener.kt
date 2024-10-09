package com.kore.common.speech

interface SupportedLanguagesListener {
    fun onSupportedLanguages(supportedLanguages: List<String?>?)
    fun onNotSupported(reason: UnsupportedReason?)
}
