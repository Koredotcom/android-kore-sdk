package kore.botssdk.view.row.chatbot

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.extensions.dpToPx

class ChatBotItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val firstItemMarginTop = 12.dpToPx(context)
    private val commonVerticalMargin = 6.dpToPx(context)
    private val messageMargin = 58.dpToPx(context)
    private val commonHorizontalMargin = 12.dpToPx(context)

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

        outRect.top = if (position == 0) firstItemMarginTop else commonVerticalMargin
        outRect.bottom = commonVerticalMargin

        when (ChatBotRowType.find(adapter.getItemViewType(position))) {
            ChatBotRowType.RequestMsg -> {
                outRect.left = messageMargin
                outRect.right = commonHorizontalMargin
            }

            ChatBotRowType.ResponseMsg -> {
                outRect.right = messageMargin
                outRect.left = commonHorizontalMargin
            }

            else -> {
            }
        }
    }
}