package com.kore.common.utils

import java.util.concurrent.TimeUnit

class StringUtils {
    companion object {
        fun getInitials(fn: String?, ln: String?): String {
            val stringBuilder = (if (!fn.isNullOrEmpty()) fn[0] else "").toString() +
                    if (!ln.isNullOrEmpty()) ln[0] else ""
            return stringBuilder.trim()
        }

        fun timeConversion(value: Long): String {
            val songTime: String
            val hours = TimeUnit.MILLISECONDS.toHours(value)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(value) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(value) % 60
            songTime = if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
            return songTime
        }

        fun getFileNameFromUrl(url: String): String {
            val index = url.lastIndexOf('/')
            return if (index != -1) url.substring(url.lastIndexOf('/') + 1) else ""
        }
    }
}