package com.kore.ui.row.botchat.radiooptions.item

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.ui.databinding.RowRadioOptionsTemplateRowItemBinding
import com.kore.ui.row.botchat.radiooptions.RadioOptionsTemplateItemRowType

class RadioOptionsTemplateItemRow(
    private val id: String,
    private val index: Int,
    private val item: Map<String, Any>,
    private val isSelected: Boolean,
    private val isLastItem: Boolean = false,
    private val onRadioButtonSelect: (id: String, position: Int, key: String) -> Unit,
) : SimpleListRow() {

    override val type: SimpleListRowType = RadioOptionsTemplateItemRowType.Item

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is RadioOptionsTemplateItemRow) return false
        return otherRow.item == item
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is RadioOptionsTemplateItemRow) return false
        return otherRow.isLastItem == isLastItem && otherRow.isSelected == isSelected
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowRadioOptionsTemplateRowItemBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            item[KEY_TITLE]?.let { rbItem.text = it.toString() }
            item[VALUE]?.let { value.text = it.toString() }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowRadioOptionsTemplateRowItemBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowRadioOptionsTemplateRowItemBinding.commonBind() {
        rbItem.isChecked = isSelected
        rbItem.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (compoundButton.isPressed) {
                if (!isLastItem) {
                    compoundButton.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }
                onRadioButtonSelect(id, index, BotResponseConstants.SELECTED_POSITION)
            }
        }
    }
}