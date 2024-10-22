package com.kore.ui.row.actionsheet

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.utils.FontUtils
import com.kore.ui.databinding.RowActionSheetItemBinding

class ActionSheetRow(
    override val type: SimpleListRowType,
    private val title: String,
    private val onItemClick: (option: String) -> Unit
) : SimpleListRow() {
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ActionSheetRow) return false
        return otherRow.title == title
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowActionSheetItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            FontUtils.applyCustomFont(root.context, root, false)
            textView.text = title
            textView.setOnClickListener { onItemClick(title) }
        }
    }
}