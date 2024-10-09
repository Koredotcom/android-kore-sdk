package com.kore.widgets.extensions

import android.view.View
import androidx.viewpager.widget.ViewPager


fun ViewPager.getCurrentView(): View? {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        val layoutParams = child.layoutParams as ViewPager.LayoutParams
        if (!layoutParams.isDecor) {
            return child
        }
    }
    return null
}