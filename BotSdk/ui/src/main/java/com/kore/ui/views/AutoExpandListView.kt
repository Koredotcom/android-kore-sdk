package com.kore.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

class AutoExpandListView : ListView {
    var isExpanded = true

    constructor(context: Context?, attrs: AttributeSet?, defaultStyle: Int) : super(context, attrs, defaultStyle)

    constructor(context: Context?) : super(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            val expandSpec = MeasureSpec.makeMeasureSpec(
                MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST
            )
            super.onMeasure(widthMeasureSpec, expandSpec)
            val params = layoutParams
            params.height = measuredHeight
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}