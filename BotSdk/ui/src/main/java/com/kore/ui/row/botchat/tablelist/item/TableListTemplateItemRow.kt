package com.kore.ui.row.botchat.tablelist.item

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListAdapter
import com.kore.common.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.ROW_ITEMS
import com.kore.model.constants.BotResponseConstants.SECTION_HEADER
import com.kore.model.constants.BotResponseConstants.SECTION_HEADER_DESC
import com.kore.ui.databinding.RowTableListTemplateItemBinding
import com.kore.ui.row.botchat.tablelist.TableListTemplateItemRowType
import com.kore.ui.row.botchat.tablelist.item.subitem.TableListTemplateSubItemRow

class TableListTemplateItemRow(
    private val element: Map<String, Any>,
    private val isLastItem: Boolean = false,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = TableListTemplateItemRowType.Item

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableListTemplateItemRow) return false
        return otherRow.element[ROW_ITEMS] == element[ROW_ITEMS]
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableListTemplateItemRow) return false
        return otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowTableListTemplateItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val sectionHeader = element[SECTION_HEADER]
            val sectionHeaderDesc = element[SECTION_HEADER_DESC]
            title.isVisible = sectionHeader != null && sectionHeader.toString().isNotEmpty()
            description.isVisible = sectionHeaderDesc != null && sectionHeaderDesc.toString().isNotEmpty()
            sectionHeader?.let { title.text = it.toString() }
            sectionHeaderDesc?.let { description.text = it.toString() }
            list.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            list.adapter = SimpleListAdapter(TableListTemplateSubItemRowType.values().asList())
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowTableListTemplateItemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowTableListTemplateItemBinding.commonBind() {
        (list.adapter as SimpleListAdapter).submitList(createRows())
    }

    private fun createRows(): List<SimpleListRow> {
        val rowItems = element[ROW_ITEMS] as List<Map<String, Any>>
        return rowItems.map { TableListTemplateSubItemRow(it, isLastItem, actionEvent) }
    }
}