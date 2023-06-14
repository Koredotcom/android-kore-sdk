package kore.botssdk.extensions

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import kore.botssdk.models.ViewStyles

fun LayerDrawable.applyStyles(style: ViewStyles?, drawableId: Int) {
    style?.let {
        val background = findDrawableByLayerId(drawableId) as GradientDrawable
        if (it.backgroundColor != null) {
            background.setColor(Color.parseColor(it.backgroundColor))
        }
        it.borderRadius?.let { radius ->
            background.cornerRadius = radius.toFloat()
        }
        if (it.borderColor != null && it.borderWidth != null) {
            background.setStroke(it.borderWidth!!, Color.parseColor(it.borderColor))
        }
    }
}