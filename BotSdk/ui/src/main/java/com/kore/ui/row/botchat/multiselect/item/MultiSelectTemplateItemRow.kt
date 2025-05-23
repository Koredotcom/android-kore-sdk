package com.kore.ui.row.botchat.multiselect.item

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.viewbinding.ViewBinding
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_LEFT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.databinding.RowMultiSelectItemBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.multiselect.MultiSelectItemRowType

class MultiSelectTemplateItemRow(
    private val id: String,
    private val element: Map<String, String>,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val isLastItem: Boolean = false,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit,
) : SimpleListRow() {

    override val type: SimpleListRowType = MultiSelectItemRowType.Item
    private val preferenceRepository = PreferenceRepositoryImpl()

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
            textView.text = element[KEY_TITLE].toString()
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowMultiSelectItemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowMultiSelectItemBinding.commonBind() {
        llCheck.isSelected = selectedItems.contains(element)
        val checkedDrawable: GradientDrawable = llCheck.background as GradientDrawable
        val checkedColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR).toColorInt()
        val uncheckedColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR).toColorInt()
        checkedDrawable.setColor(if (llCheck.isSelected) checkedColor else Color.WHITE)
        checkedDrawable.setStroke(1.dpToPx(root.context), if (llCheck.isSelected) checkedColor else uncheckedColor)
        val drawable = root.background.mutate() as GradientDrawable
        drawable.setStroke(2, uncheckedColor)
        llCheck.setOnClickListener {
            if (!isLastItem) {
                return@setOnClickListener
            }
            llCheck.isSelected = !llCheck.isSelected
            val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>
            if (llCheck.isSelected) items.add(element) else items.remove(element)
            onSaveState(id, items, BotResponseConstants.SELECTED_ITEM)
        }
    }
}