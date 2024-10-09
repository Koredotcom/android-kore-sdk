package com.kore.common.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

class DimensionUtils(context: Context?) {

    val context: Context? = null

    companion object {
        fun convertDpToPixel(context: Context, i: Float): Float {
            var dp = i
            val metrics = context.resources?.displayMetrics
            if (metrics?.densityDpi!! <= DisplayMetrics.DENSITY_HIGH) {
                dp = 1.4f
            }
            return dp * (metrics.densityDpi / 160f)
        }

        var dp1 = 0f
        var screenHeight = 0f
        var screenWidth = 0f
        var density = 0f
    }


    init {
        dp1 = convertDpToPixel(context, 1f)
        screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
        screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        density = Resources.getSystem().displayMetrics.density
    }

    fun convertDpToPixel(context: Context?, dp1: Float): Float {
        var dp = dp1
        val metrics = context?.resources?.displayMetrics
        if (metrics?.densityDpi!! <= DisplayMetrics.DENSITY_HIGH) {
            dp = 1.4f
        }
        return dp * (metrics.densityDpi / 160f)
    }


}