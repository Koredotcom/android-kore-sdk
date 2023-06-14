package kore.botssdk.extensions

import android.content.Context
import kotlin.math.floor

fun Int.dpToPx(context: Context) =
    floor(this * (context.resources?.displayMetrics?.density ?: 1f)).toInt()