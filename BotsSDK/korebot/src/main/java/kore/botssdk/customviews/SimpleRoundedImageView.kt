package kore.botssdk.customviews

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import kore.botssdk.R
import kotlin.math.min

class SimpleRoundedImageView : AppCompatImageView {

    private var radius = 0f

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleRoundedImageView, defStyleAttr, 0)
        radius = typedArray.getDimension(R.styleable.SimpleRoundedImageView_radius, 0f)
        typedArray.recycle()
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    0,
                    0,
                    view.measuredWidth,
                    view.measuredHeight,
                    min(radius, view.measuredHeight / 2f)
                )
            }
        }
        clipToOutline = true
    }
}