package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.AdvancedListAdapter
import com.kore.ui.databinding.AdvancelistViewBinding

class AdvancedListTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val iconUrl: String?,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_ADVANCE_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvancedListTemplateRow) return false
        return otherRow.id == id
    }


    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvancedListTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = AdvancelistViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            advanceRecyclerView.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)

            if (payload[BotResponseConstants.KEY_TITLE] != null) {
                botListViewTitle.isVisible = true
                botListViewTitle.text = payload[BotResponseConstants.KEY_TITLE] as String
            }

            if (payload[BotResponseConstants.DESCRIPTION] != null) {
                tvDescription.isVisible = true
                tvDescription.text = payload[BotResponseConstants.DESCRIPTION] as String
            }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        AdvancelistViewBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun AdvancelistViewBinding.commonBind() {
        advanceRecyclerView.adapter = AdvancedListAdapter(
            root.context,
            payload[BotResponseConstants.LIST_ITEMS] as List<Map<String, *>>,
            isLastItem,
            actionEvent
        )
    }
}