package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.CardTemplateAdapter
import com.kore.ui.databinding.CardTemplateViewBinding

class CardTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val iconUrl: String?,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType =
        BotChatRowType.getRowType(BotChatRowType.ROW_CARD_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is CardTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is CardTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = CardTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            rvCardTemplate.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)

            rvCardTemplate.addItemDecoration(SimpleListRow.VerticalSpaceItemDecoration(10))

            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        CardTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun CardTemplateViewBinding.commonBind() {
        rvCardTemplate.adapter = CardTemplateAdapter(
            root.context,
            payload[BotResponseConstants.CARDS] as List<Map<String, *>>,
            isLastItem,
            actionEvent
        )
    }
}