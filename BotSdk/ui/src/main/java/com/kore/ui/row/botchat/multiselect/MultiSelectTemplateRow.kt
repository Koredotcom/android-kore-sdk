package com.kore.ui.row.botchat.multiselect

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListAdapter
import com.kore.common.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.databinding.RowMultiSelectBinding
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.multiselect.item.MultiSelectTemplateItemRow

class MultiSelectTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val elements: ArrayList<Map<String, String>>,
    private val isLastItem: Boolean = false,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {

    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_MULTI_SELECT_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is MultiSelectTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is MultiSelectTemplateRow) return false
        return otherRow.elements == elements && otherRow.selectedItems == selectedItems && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowMultiSelectBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            list.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            list.addItemDecoration(VerticalSpaceItemDecoration(10))
            val adapter = SimpleListAdapter(MultiSelectItemRowType.values().asList())
            list.adapter = adapter
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowMultiSelectBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowMultiSelectBinding.commonBind() {
        done.isVisible = isLastItem && !selectedItems.isNullOrEmpty()
        val adapter = list.adapter as SimpleListAdapter
        adapter.submitList(createRows())
        selectAll.isChecked = selectedItems != null && selectedItems.containsAll(elements)
        selectAll.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed) {
                if (!isLastItem) {
                    checkBox.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }
                val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>
                items.removeAll(elements)
                if (isChecked) items.addAll(elements)
                onSaveState(id, items, BotResponseConstants.SELECTED_ITEM)
            }
        }
        done.setOnClickListener {
            if (selectedItems.isNullOrEmpty()) return@setOnClickListener
            val stringValues = StringBuilder()
            val stringKeys = StringBuilder()
            selectedItems.mapIndexed { index, item ->
                stringValues.append(item[BotResponseConstants.VALUE])
                stringKeys.append(item[BotResponseConstants.KEY_TITLE])
                if (index < selectedItems.size - 1) {
                    stringValues.append(" ")
                    stringKeys.append(" ")
                }
            }
            actionEvent(BotChatEvent.SendMessage(stringKeys.toString(), stringValues.toString()))
        }
    }

    private fun createRows(): List<SimpleListRow> {
        return elements.map { item ->
            MultiSelectTemplateItemRow(
                id,
                item,
                selectedItems,
                isLastItem,
                onSaveState,
            )
        }
    }
}