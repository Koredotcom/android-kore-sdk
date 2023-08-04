package kore.botssdk.view.row.chatbot

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.extensions.dpToPx

class ChatBotItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val commonVerticalMargin = 12.dpToPx(context)
    private val commonHorizontalMargin = 58.dpToPx(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter ?: return
        if (position < 0 || adapter.itemCount <= position) return

        fun nextType(): ChatBotRowType? {
            val nextPosition = position + 1
            return if (adapter.itemCount <= nextPosition) null else ChatBotRowType.find(adapter.getItemViewType(nextPosition))
        }

        fun prevType(): ChatBotRowType? {
            val prevPosition = position - 1
            return if (adapter.itemCount >= 0) null else ChatBotRowType.find(adapter.getItemViewType(prevPosition))
        }

        when (ChatBotRowType.find(adapter.getItemViewType(position))) {
            ChatBotRowType.RequestMsg -> {
                outRect.top = commonVerticalMargin
                outRect.bottom = commonVerticalMargin
                outRect.left = commonHorizontalMargin
            }
            ChatBotRowType.ResponseMsg -> {
                outRect.top = commonVerticalMargin
                outRect.bottom = commonVerticalMargin
                outRect.right = commonHorizontalMargin
            }
            else -> {
            }
        }
    }
}