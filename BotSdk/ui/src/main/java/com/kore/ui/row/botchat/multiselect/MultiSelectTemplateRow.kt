package com.kore.ui.row.botchat.multiselect

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_LEFT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.databinding.RowMultiSelectBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
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
    private val preferenceRepository = PreferenceRepositoryImpl()

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
            val adapter = SimpleListAdapter(MultiSelectItemRowType.entries)
            list.adapter = adapter
            val sharedPrefs = PreferenceRepositoryImpl()
            val bgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
            val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF").toColorInt()
            done.setRoundedCorner(6.dpToPx(root.context).toFloat())
            done.setBackgroundColor(bgColor)
            done.setTextColor(txtColor)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowMultiSelectBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowMultiSelectBinding.commonBind() {
        done.isVisible = isLastItem && selectedItems.isNotEmpty()
        val adapter = list.adapter as SimpleListAdapter
        adapter.submitList(createRows())
        llCheck.isSelected = selectedItems.containsAll(elements)
        val checkedDrawable: GradientDrawable = llCheck.background as GradientDrawable
        val checkedColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_RIGHT_BG_COLOR).toColorInt()
        val uncheckedColor = preferenceRepository.getStringValue(root.context, THEME_NAME, BUBBLE_LEFT_BG_COLOR).toColorInt()
        checkedDrawable.setColor(if (llCheck.isSelected) checkedColor else Color.WHITE)
        checkedDrawable.setStroke(1.dpToPx(root.context), if (llCheck.isSelected) checkedColor else uncheckedColor)
        llCheck.setOnClickListener {
            if (!isLastItem) {
                return@setOnClickListener
            }
            llCheck.isSelected = !llCheck.isSelected
            val items: ArrayList<Map<String, String>> = selectedItems.clone() as ArrayList<Map<String, String>>
            items.removeAll(elements)
            if (llCheck.isSelected) items.addAll(elements)
            onSaveState(id, items, BotResponseConstants.SELECTED_ITEM)
        }
        done.setOnClickListener {
            if (selectedItems.isEmpty()) return@setOnClickListener
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
            MultiSelectTemplateItemRow(id, item, selectedItems, isLastItem, onSaveState)
        }
    }
}