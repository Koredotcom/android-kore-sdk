package com.kore.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.stringToDate(formatType: String): Date? {
    val format = SimpleDateFormat(formatType, Locale.ENGLISH)
    try {
        return format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.getDotMessage(): String {
    val strDots = StringBuilder()
    for (i in this.indices) {
        strDots.append("•")
    }
    return strDots.toString()
}