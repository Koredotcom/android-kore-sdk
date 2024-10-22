package com.kore.ui.row.botchat.multiselect.item

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.ui.databinding.RowMultiSelectItemBinding
import com.kore.ui.row.botchat.multiselect.MultiSelectItemRowType

class MultiSelectTemplateItemRow(
    private val id: String,
    private val element: Map<String, String>,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val isLastItem: Boolean = false,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit,
) : SimpleListRow() {

    override val type: SimpleListRowType = MultiSelectItemRowType.Item

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is MultiSelectTemplateItemRow) return false
        return otherRow.element[KEY_TITLE] == element[KEY_TITLE]
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is MultiSelectTemplateItemRow) return false
        return otherRow.element == element && otherRow.selectedItems == selectedItems && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowMultiSelectItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            checkBox.text = element[KEY_TITLE].toString()
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowMultiSelectItemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowMultiSelectItemBinding.commonBind() {
        checkBox.isChecked = selectedItems.contains(element)
        checkBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed) {
                if (!isLastItem) {
                    checkBox.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }
                val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>
                if (isChecked) items.add(element) else items.remove(element)
                onSaveState(id, items, BotResponseConstants.SELECTED_ITEM)
            }
        }
    }
}