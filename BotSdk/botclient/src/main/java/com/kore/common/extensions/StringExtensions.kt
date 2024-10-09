package com.kore.common.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatEmojis(): String {
    return this.replace(":)", "\uD83D\uDE42")
        .replace(":thumbsup", "\uD83D\uDC4D")
        .replace(":(", "☹️")
}

fun String.stringToDate(formatType: String): Date? {
    val format = SimpleDateFormat(formatType, Locale.ENGLISH)
    try {
        return format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}