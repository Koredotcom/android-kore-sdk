package com.kore.extensions

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.verticalSmoothScrollTo(position: Int, snapPreference: Int) {
    if (position < 0) return
    layoutManager?.startSmoothScroll(
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference() = snapPreference
        }.apply { targetPosition = position }
    )
}

fun RecyclerView.verticalScrollTo(position: Int) {
    layoutManager?.scrollToPosition(position)
}

fun RecyclerView.clearItemDecorations() {
    repeat(itemDecorationCount) { removeItemDecorationAt(it) }
}