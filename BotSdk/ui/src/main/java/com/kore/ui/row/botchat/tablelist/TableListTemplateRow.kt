package com.kore.ui.row.botchat.tablelist

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.ELEMENTS
import com.kore.ui.databinding.RowTableListTemplateBinding
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_TABLE_LIST_PROVIDER
import com.kore.ui.row.botchat.tablelist.item.TableListTemplateItemRow

class TableListTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: Map<String, Any>,
    private val isLastItem: Boolean = false,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(ROW_TABLE_LIST_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableListTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableListTemplateRow) return false
        return otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl,  isShow = false, isTemplate = true)
        val childBinding = RowTableListTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            list.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            list.adapter = SimpleListAdapter(TableListTemplateItemRowType.values().asList())
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowTableListTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowTableListTemplateBinding.commonBind() {
        (list.adapter as SimpleListAdapter).submitList(createRows())
    }

    private fun createRows(): List<SimpleListRow> {
        val elements = payload[ELEMENTS] as List<Map<String, Any>>
        return elements.map { element -> TableListTemplateItemRow(element, isLastItem, actionEvent) }
    }
}