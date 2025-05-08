package com.kore.common.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import com.kore.model.constants.BotResponseConstants
import java.sql.Timestamp
import java.text.DateFormatSymbols
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
object DateUtils {
    private val yearFormat: Format = SimpleDateFormat("yyyy", Locale.ENGLISH)
    private val dateWeekMsg = SimpleDateFormat("EE, MMM dd", Locale.ENGLISH)
    private val dateWeekDay = SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH)
    private val dateMonthDay: Format = SimpleDateFormat("MMM dd", Locale.ENGLISH)
    private val dateWeekMsgBubble = SimpleDateFormat("EE MMM dd", Locale.ENGLISH)
    private val dateTime: Format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    private val date24Time: Format = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    private val dynamicDate = SimpleDateFormat(BotResponseConstants.DATE_FORMAT, Locale.ENGLISH)


    fun getTimeInAmPm(dateInMs: Long): String {
        return if (BotResponseConstants.TIME_FORMAT == 12) dateTime.format(Date(dateInMs))
        else date24Time.format(Date(dateInMs))
    }

    fun formattedSentDate(lastModified: Long): String {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        val symbols = DateFormatSymbols(Locale.getDefault())

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.amPmStrings = arrayOf("am", "pm")
        dateWeekDay.dateFormatSymbols = symbols
        yearFormat.format(Date(lastModified)).toInt()
        yearFormat.format(Date()).toInt()
        val time: String = if (DateUtils.isToday(lastModified)) {
            "Today"
        } else if (DateUtils.isToday(lastModified + DateUtils.DAY_IN_MILLIS)) {
            "Yesterday"
        } else if (DateUtils.isToday(lastModified - DateUtils.DAY_IN_MILLIS)) {
            "Tomorrow"
        } else {
            dynamicDate.format(Date(lastModified))
        }
        return time
    }

    fun formattedSentDateV6(lastModified: Long): String {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        val symbols = DateFormatSymbols(Locale.getDefault())

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.amPmStrings = arrayOf("am", "pm")
        dateWeekDay.dateFormatSymbols = symbols
        yearFormat.format(Date(lastModified)).toInt()
        yearFormat.format(Date()).toInt()
        val time: String = if (DateUtils.isToday(lastModified)) {
            "Today, " + dateMonthDay.format(Date(lastModified))
        } else if (DateUtils.isToday(lastModified + DateUtils.DAY_IN_MILLIS)) {
            "Yesterday, " + dateMonthDay.format(Date(lastModified))
        } else if (DateUtils.isToday(lastModified - DateUtils.DAY_IN_MILLIS)) {
            "Tomorrow, " + dateMonthDay.format(Date(lastModified))
        } else {
            dynamicDate.format(Date(lastModified))
        }
        return time
    }

    fun getDateFromFormat(date: String?, format: String, addDays: Int): Long {
        var date = date
        var format = format
        if (date.isNullOrEmpty()) return 0
        try {
            if (format.contains("YYYY")) {
                format = format.replace("YY", "yy")
            }

            if (format.contains("/") && date.contains("-")) date = date.replace("-".toRegex(), "/")

            val df = SimpleDateFormat(format, Locale.US)
            val ts = Timestamp(Objects.requireNonNull(df.parse(date)).time)
            return ts.time + (addDays.toLong() * 24 * 60 * 60 * 1000)
        } catch (e: ParseException) {
            val calendar = Calendar.getInstance()
            return calendar.timeInMillis - (addDays.toLong() * 24 * 60 * 60 * 1000)
        }
    }
}