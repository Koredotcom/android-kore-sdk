package com.kore.ui.row.botchat.advancemultiselect.category.subitem

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.IMAGE_URL
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.SELECTED_ITEM
import com.kore.ui.R
import com.kore.ui.databinding.RowAdvanceMultiSelectCategorySubitemBinding
import com.kore.ui.row.botchat.advancemultiselect.category.AdvancedMultiSelectCategorySubItemRowType

class AdvanceMultiSelectCategorySubItemRow(
    private val id: String,
    private val item: Map<String, String>,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val isLastItem: Boolean = false,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit,
) : SimpleListRow() {

    override val type: SimpleListRowType = AdvancedMultiSelectCategorySubItemRowType.SubItem

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectCategorySubItemRow) return false
        return otherRow.item[KEY_TITLE] == item[KEY_TITLE]
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectCategorySubItemRow) return false
        return otherRow.item == item && otherRow.selectedItems == selectedItems && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowAdvanceMultiSelectCategorySubitemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val title = item[KEY_TITLE].toString()
            textView.text = title
            val imageUrl = item[IMAGE_URL]
            ivAdvMultiSelect.isVisible = !imageUrl.isNullOrEmpty()
            imageUrl?.let {
                Glide.with(root.context).load(it).error(R.drawable.transparant_image)
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(ivAdvMultiSelect))
            }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowAdvanceMultiSelectCategorySubitemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowAdvanceMultiSelectCategorySubitemBinding.commonBind() {
        checkBox.isChecked = selectedItems.contains(item)
        checkBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed) {
                if (!isLastItem) {
                    checkBox.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }
                val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>
                if (isChecked) items.add(item) else items.remove(item)

                onSaveState(id, items, SELECTED_ITEM)
            }
        }
    }
}