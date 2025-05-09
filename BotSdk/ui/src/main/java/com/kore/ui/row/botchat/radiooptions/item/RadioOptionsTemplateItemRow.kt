package com.kore.ui.row.botchat.radiooptions.item

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.widget.CompoundButtonCompat
import androidx.viewbinding.ViewBinding
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_LEFT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.ui.databinding.RowRadioOptionsTemplateRowItemBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.radiooptions.RadioOptionsTemplateItemRowType
import androidx.core.graphics.toColorInt

class RadioOptionsTemplateItemRow(
    private val id: String,
    private val index: Int,
    private val item: Map<String, Any>,
    private val isSelected: Boolean,
    private val isLastItem: Boolean = false,
    private val onRadioButtonSelect: (id: String, position: Int, key: String) -> Unit,
) : SimpleListRow() {

    override val type: SimpleListRowType = RadioOptionsTemplateItemRowType.Item
    private val preferenceRepository = PreferenceRepositoryImpl()

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
            setTintColor(rbItem)
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

    private fun setTintColor(radioButton: AppCompatRadioButton) {
        val colorUnchecked = preferenceRepository.getStringValue(radioButton.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR, "#FFFFFF")
        val colorChecked: String = preferenceRepository.getStringValue(radioButton.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#FFFFFF")

        val tintList = ColorStateList(
            arrayOf(
                intArrayOf(R.attr.state_checked),
                intArrayOf(-R.attr.state_checked)
            ),
            intArrayOf(
                colorChecked.toColorInt(),
                colorUnchecked.toColorInt()
            )
        )

        CompoundButtonCompat.setButtonTintList(radioButton, tintList)
        radioButton.buttonDrawable!!.setTintList(tintList)
        radioButton.invalidate()
    }
}