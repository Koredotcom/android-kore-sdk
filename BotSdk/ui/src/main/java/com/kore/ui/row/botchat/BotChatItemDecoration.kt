package com.kore.ui.row.botchat

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.extensions.dpToPx

open class BotChatItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val firstItemMarginTop = 12.dpToPx(context)
    private val commonVerticalMargin = 6.dpToPx(context)
    private val messageMargin = 58.dpToPx(context)
    private val messageAdvMargin = 28.dpToPx(context)
    private val requestMsgMarginRight = 12.dpToPx(context)
    private val responseMarginLeft = 6.dpToPx(context)
    private val noIconMarginLeft = 32.dpToPx(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter ?: return
        if (position < 0 || adapter.itemCount <= position) return

        fun nextType(): BotChatRowType.RowType? {
            val nextPosition = position + 1
            return if (adapter.itemCount <= nextPosition) null else BotChatRowType.find(adapter.getItemViewType(nextPosition))
        }

        fun prevType(): BotChatRowType.RowType? {
            val prevPosition = position - 1
            return if (adapter.itemCount >= 0) null else BotChatRowType.find(adapter.getItemViewType(prevPosition))
        }

        outRect.top = if (position == 0) firstItemMarginTop else commonVerticalMargin
        outRect.bottom = commonVerticalMargin

        when (BotChatRowType.find(adapter.getItemViewType(position))?.rowType) {
            BotChatRowType.ROW_REQUEST_MSG_PROVIDER -> {
                outRect.left = messageMargin
                outRect.right = requestMsgMarginRight
            }

            BotChatRowType.ROW_RESPONSE_MSG_PROVIDER -> {
                outRect.right = messageMargin
                outRect.left = responseMarginLeft
            }

            BotChatRowType.ROW_BUTTON_PROVIDER -> {
                outRect.right = messageMargin
                outRect.left = noIconMarginLeft
            }

            BotChatRowType.ROW_TIME_STAMP_PROVIDER -> {}

            else -> {
                outRect.right = messageAdvMargin
                outRect.left = responseMarginLeft
            }
        }
    }
}