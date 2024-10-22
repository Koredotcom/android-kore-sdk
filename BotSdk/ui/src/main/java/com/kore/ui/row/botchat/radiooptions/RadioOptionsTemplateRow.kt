package com.kore.ui.row.botchat.radiooptions

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants.HEADING
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.POSTBACK
import com.kore.model.constants.BotResponseConstants.RADIO_OPTIONS
import com.kore.model.constants.BotResponseConstants.VALUE
import com.kore.ui.databinding.RowRadioOptionsTemplateBinding
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.radiooptions.item.RadioOptionsTemplateItemRow

class RadioOptionsTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: Map<String, Any>,
    private val selPosition: Int,
    private val isLastItem: Boolean = false,
    private val onRadioButtonSelect: (id: String, position: Int?, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {

    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_RADIO_OPTIONS_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is RadioOptionsTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is RadioOptionsTemplateRow) return false
        return otherRow.isLastItem == isLastItem && otherRow.selPosition == selPosition && otherRow.payload == payload
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowRadioOptionsTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            title.text = payload[HEADING].toString()
            list.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            list.adapter = SimpleListAdapter(RadioOptionsTemplateItemRowType.values().asList())
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowRadioOptionsTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowRadioOptionsTemplateBinding.commonBind() {
        (list.adapter as SimpleListAdapter).submitList(createRows())
        confirm.setOnClickListener {
            if (!isLastItem || selPosition == -1) return@setOnClickListener
            val message = ((payload[RADIO_OPTIONS] as List<Map<String, Any>>)[selPosition][POSTBACK] as Map<String, String>)[KEY_TITLE]
            val payload = ((payload[RADIO_OPTIONS] as List<Map<String, Any>>)[selPosition][POSTBACK] as Map<String, String>)[VALUE]
            actionEvent(BotChatEvent.SendMessage(message.toString(), payload))
        }
    }

    private fun createRows(): List<SimpleListRow> {
        return (payload[RADIO_OPTIONS] as List<Map<String, Any>>).mapIndexed { index, item ->
            RadioOptionsTemplateItemRow(id, index, item, selPosition == index, isLastItem, onRadioButtonSelect)
        }
    }
}