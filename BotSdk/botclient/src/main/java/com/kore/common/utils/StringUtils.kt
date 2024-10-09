package com.kore.common.utils

class StringUtils {
    companion object {
        fun getInitials(fn: String?, ln: String?): String {
            val stringBuilder = (if (!fn.isNullOrEmpty()) fn[0] else "").toString() +
                    if (!ln.isNullOrEmpty()) ln[0] else ""
            return stringBuilder.trim()
        }

        fun timeConversion(value: Long): String {
            val songTime: String
            val dur = value.toInt()
            val hrs = dur / 3600000
            val mns = dur / 60000 % 60000
            val scs = dur % 60000 / 1000
            songTime = if (hrs > 0) {
                String.format("%02d:%02d:%02d", hrs, mns, scs)
            } else {
                String.format("%02d:%02d", mns, scs)
            }
            return songTime
        }

        fun getFileNameFromUrl(url: String): String {
            return try {
                url.substring(url.lastIndexOf('/') + 1)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}