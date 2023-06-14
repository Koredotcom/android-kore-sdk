package kore.botssdk.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import kore.botssdk.models.ViewStyles

fun TextView.applyStyles(style: ViewStyles?) {
    style?.let {
        if (it.fontStyle != null) {
            val typeface = Typeface.createFromAsset(context.assets, "fonts/" + it.fontFamily)
            setTypeface(typeface)
        }
        if (it.fontColor != null) {
            setTextColor(Color.parseColor(it.fontColor))
        }
        it.fontSize?.let { fontSize ->
            textSize = fontSize
        }
    }
}