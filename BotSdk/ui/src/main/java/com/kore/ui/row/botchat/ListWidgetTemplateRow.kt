package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.ListWidgetAdapter
import com.kore.ui.databinding.BotListWidgetTemplateViewBinding
import com.kore.ui.row.SimpleListRow

class ListWidgetTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_LIST_WIDGET_PROVIDER)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ListWidgetTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ListWidgetTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = BotListWidgetTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val sharedPreferences = PreferenceRepositoryImpl().getSharedPreference(root.context, BotResponseConstants.THEME_NAME)
            if (payload[BotResponseConstants.KEY_TITLE] != null) {
                meetingHeader.isVisible = true
                meetingHeader.text = payload[BotResponseConstants.KEY_TITLE] as String
                meetingHeader.setTextColor(
                    sharedPreferences.getString(BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, "#000000")!!.toColorInt()
                )
            }

            if (payload[BotResponseConstants.DESCRIPTION] != null) {
                meetingDesc.isVisible = true
                meetingDesc.text = payload[BotResponseConstants.DESCRIPTION] as String
                sharedPreferences.getString(BotResponseConstants.BUTTON_ACTIVE_TXT_COLOR, "#000000")?.toColorInt()?.let {
                    meetingDesc.setTextColor(it)
                }
            }

            botCustomListView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        BotListWidgetTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun BotListWidgetTemplateViewBinding.commonBind() {
        val entries = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
        botCustomListView.adapter = ListWidgetAdapter(root.context, entries, isLastItem, actionEvent)
    }
}