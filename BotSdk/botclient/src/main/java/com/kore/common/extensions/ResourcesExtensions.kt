package com.kore.common.extensions

import android.content.Context
import kotlin.math.floor

fun Int.dpToPx(context: Context) =
    floor(this * (context.resources?.displayMetrics?.density ?: 1f)).toInt()