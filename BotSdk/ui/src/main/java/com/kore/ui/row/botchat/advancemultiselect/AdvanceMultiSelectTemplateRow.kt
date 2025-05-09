package com.kore.ui.row.botchat.advancemultiselect

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
import com.kore.model.constants.BotResponseConstants.COLLECTION
import com.kore.model.constants.BotResponseConstants.COLLECTION_TITLE
import com.kore.model.constants.BotResponseConstants.HEADING
import com.kore.model.constants.BotResponseConstants.KEY_ELEMENTS
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.databinding.RowAdvanceMultiSelectBinding
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.advancemultiselect.category.AdvanceMultiSelectCategoryRow

class AdvanceMultiSelectTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val displayCount: Int,
    private val isLastItem: Boolean = false,
    private val selectedItems: ArrayList<Map<String, String>>,
    private val onSaveState: (msgId: String, value: Any?, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {

    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_ADVANCED_MULTI_SELECT_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AdvanceMultiSelectTemplateRow) return false
        return otherRow.payload == payload && otherRow.selectedItems == selectedItems &&
                otherRow.isLastItem == isLastItem && otherRow.displayCount == displayCount
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, false, true)
        val childBinding = RowAdvanceMultiSelectBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            if (payload[HEADING] != null) {
                advanceMultiSelectTitle.isVisible = true
                advanceMultiSelectTitle.text = payload[HEADING] as String
            }

            val adapter = SimpleListAdapter(AdvancedMultiSelectCategoryRowType.entries)
            categoryList.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            categoryList.adapter = adapter
            val sharedPrefs = PreferenceRepositoryImpl()
            val txtColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, "#FFFFFF")
            val bgColor = sharedPrefs.getStringValue(root.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
            done.setBackgroundColor(bgColor.toColorInt())
            done.setTextColor(txtColor.toColorInt())
            done.setRoundedCorner(6.dpToPx(root.context).toFloat())
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowAdvanceMultiSelectBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowAdvanceMultiSelectBinding.commonBind() {
        val adapter = categoryList.adapter as SimpleListAdapter
        adapter.submitList(createRows())
        done.isVisible = isLastItem && selectedItems.isNotEmpty()
        val actualCount = (payload[KEY_ELEMENTS] as List<Map<String, Any>>).size
        viewMore.isVisible = displayCount < actualCount
        viewMore.setOnClickListener { onSaveState(id, actualCount, BotResponseConstants.DISPLAY_LIMIT) }
        done.setOnClickListener {
            if (selectedItems.isEmpty()) return@setOnClickListener
            val titles = StringBuilder()
            val values = StringBuilder()
            titles.append("Here are the selected items : ")
            values.append("Here are the selected items : ")
            selectedItems.mapIndexed { index, item ->
                titles.append(item[BotResponseConstants.KEY_TITLE])
                values.append(item[BotResponseConstants.VALUE])
                if (index < selectedItems.size - 1){
                    titles.append(", ")
                    values.append(", ")
                }
            }
            actionEvent(BotChatEvent.SendMessage(titles.toString(), values.toString()))
        }
    }

    private fun createRows(): List<SimpleListRow> {
        val actualCategories = payload[KEY_ELEMENTS] as List<Map<String, Any>>
        val categories: List<Map<String, Any>> = if (displayCount < actualCategories.size) {
            actualCategories.subList(0, displayCount)
        } else {
            actualCategories
        }
        return categories.map { item ->
            AdvanceMultiSelectCategoryRow(
                id,
                item[COLLECTION_TITLE] as String,
                item[COLLECTION] as ArrayList<Map<String, String>>,
                isLastItem,
                selectedItems,
                onSaveState,
            )
        }
    }
}