package com.kore.widgets.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.kore.widgets.R

open class WidgetCustomTextView : AppCompatTextView {

    private var context: Context
    private var defStyle : Int = 0
    private lateinit var attrs: AttributeSet

    constructor(context: Context) : super(context) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)
    {
        style(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        this.context = context
        if (attrs != null) {
            this.attrs = attrs
        }
        this.defStyle = defStyle

        style(context)
    }

    private fun style(context: Context)
    {
        val tfRegular = ResourcesCompat.getFont(context, R.font.latoregular)
        typeface = tfRegular
    }
}