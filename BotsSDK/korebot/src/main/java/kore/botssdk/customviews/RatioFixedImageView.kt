package kore.botssdk.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kore.botssdk.R
import kore.botssdk.common.AspectRatio

open class RatioFixedImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private var widthHint = 0

    private var heightHint = 0

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioFixedImageView)
        widthHint = typedArray.getInteger(R.styleable.RatioFixedImageView_widthHint, 0)
        heightHint = typedArray.getInteger(R.styleable.RatioFixedImageView_heightHint, 0)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (widthHint == 0 || heightHint == 0) {
            return
        }

        val width = measuredWidth
        val height = width * heightHint / widthHint

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    fun setWidthHint(widthHint: Int) {
        val changed = this.widthHint != widthHint
        this.widthHint = widthHint
        if (changed) {
            requestLayout()
        }
    }

    fun setHeightHint(heightHint: Int) {
        val changed = this.heightHint != heightHint
        this.heightHint = heightHint
        if (changed) {
            requestLayout()
        }
    }

    fun setAspectRatio(aspectRatio: AspectRatio) {
        val changed = widthHint != aspectRatio.width || heightHint != aspectRatio.height
        widthHint = aspectRatio.width
        heightHint = aspectRatio.height
        if (changed) {
            requestLayout()
        }
    }
}