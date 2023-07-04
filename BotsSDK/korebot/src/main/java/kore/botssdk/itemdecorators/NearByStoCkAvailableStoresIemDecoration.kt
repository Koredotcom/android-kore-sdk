package kore.botssdk.itemdecorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.extensions.dpToPx

class NearByStoCkAvailableStoresIemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val itemBottomMargin = 10.dpToPx(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter ?: return
        if (position !in 0 until adapter.itemCount) return
        if (position != adapter.itemCount - 1) outRect.bottom = itemBottomMargin
    }
}