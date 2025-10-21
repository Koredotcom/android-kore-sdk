package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.FORM_DATA
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.ui.adapters.BotButtonsTemplateAdapter
import com.kore.ui.databinding.RowDigitalFormTemplateBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_DIGITAL_FORM

class DigitalFormTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(ROW_DIGITAL_FORM)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DigitalFormTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DigitalFormTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowDigitalFormTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            title.text = (payload[KEY_TEXT] as String?)
            buttonsList.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowDigitalFormTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowDigitalFormTemplateBinding.commonBind() {
        val items = (payload[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>)
        buttonsList.adapter = BotButtonsTemplateAdapter(root.context, items, payload, isLastItem, FORM_DATA, actionEvent)
    }
}