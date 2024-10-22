package com.kore.speech

interface SupportedLanguagesListener {
    fun onSupportedLanguages(supportedLanguages: List<String?>?)
    fun onNotSupported(reason: UnsupportedReason?)
}
