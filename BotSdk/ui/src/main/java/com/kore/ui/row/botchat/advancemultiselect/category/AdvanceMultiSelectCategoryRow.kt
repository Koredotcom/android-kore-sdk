package com.kore.ui.row.botchat.advancemultiselect.category

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.extensions.clearItemDecorations
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.SELECTED_ITEM
import com.kore.ui.databinding.RowAdvancedMultiSelectCategoryBinding
import com.kore.ui.row.botchat.advancemultiselect.AdvancedMultiSelectCategoryRowType
import com.kore.ui.row.botchat.advancemultiselect.category.subitem.AdvanceMultiSelectCategorySubItemRow

class AdvanceMultiSelectCategoryRow(
    private val id: String,
    private val categoryName: String,
    private val collection: ArrayList<Map<String, String>>,
    private val isLastItem: Boolean = false,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit
) : SimpleListRow() {

    override val type: SimpleListRowType = AdvancedMultiSelectCategoryRowType.Category

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectCategoryRow) return false
        return otherRow.categoryName == categoryName
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectCategoryRow) return false
        return otherRow.collection == collection && otherRow.selectedItems == selectedItems && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowAdvancedMultiSelectCategoryBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            tvMultiSelectTitle.text = categoryName
            list.clearItemDecorations()
            list.addItemDecoration(VerticalSpaceItemDecoration(10))
            list.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            val adapter = SimpleListAdapter(AdvancedMultiSelectCategorySubItemRowType.entries)
            list.adapter = adapter
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowAdvancedMultiSelectCategoryBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowAdvancedMultiSelectCategoryBinding.commonBind() {
        val adapter = list.adapter as SimpleListAdapter
        adapter.submitList(createRows())
        selectAll.isChecked = selectedItems.containsAll(collection)
        selectAll.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed) {
                if (!isLastItem) {
                    checkBox.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }
                val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>

                items.removeAll(collection)
                if (isChecked) items.addAll(collection)
                onSaveState(id, items, SELECTED_ITEM)
            }
        }
    }

    private fun createRows(): List<SimpleListRow> {
        return collection.map { item ->
            AdvanceMultiSelectCategorySubItemRow(id, item, selectedItems, isLastItem, onSaveState)
        }
    }
}