package kore.botssdk.extensions

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.verticalSmoothScrollTo(position: Int, snapPreference: Int) {
    layoutManager?.startSmoothScroll(
        object : LinearSmoothScroller(context) {

            override fun getVerticalSnapPreference() = snapPreference
        }.apply {
            targetPosition = position
        }
    )
}

fun RecyclerView.clearItemDecorations() {
    repeat(itemDecorationCount) { removeItemDecorationAt(it) }
}