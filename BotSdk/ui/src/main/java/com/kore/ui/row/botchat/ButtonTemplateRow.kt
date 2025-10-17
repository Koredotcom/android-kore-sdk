package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.adapters.BotButtonsTemplateAdapter
import com.kore.ui.databinding.RowButtonTemplateBinding
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_BUTTON_PROVIDER

class ButtonTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(ROW_BUTTON_PROVIDER)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ButtonTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ButtonTemplateRow) return false
        return otherRow.payload == payload && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowButtonTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            root.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

            payload[BotResponseConstants.BUTTON_VARIATION].let {
                when (payload[BotResponseConstants.BUTTON_VARIATION]) {
                    BotResponseConstants.PLAIN,
                    BotResponseConstants.TEXT_INVERTED,
                    BotResponseConstants.BACKGROUND_INVERTED -> {
                        root.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                    }

                    BotResponseConstants.FULL_WIDTH -> {
                        root.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
                    }

                    BotResponseConstants.BUTTON_STACKED -> {
                        val layoutManager = FlexboxLayoutManager(binding.root.context)
                        layoutManager.justifyContent = JustifyContent.FLEX_START
                        layoutManager.flexDirection = FlexDirection.ROW
                        root.layoutManager = layoutManager
                    }
                }
            }

            if (payload[BotResponseConstants.FULL_WIDTH_] != null && payload[BotResponseConstants.FULL_WIDTH_] as Boolean) {
                root.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            }

            if (payload[BotResponseConstants.BUTTON_STACKED] != null && payload[BotResponseConstants.BUTTON_STACKED] as Boolean) {
                val layoutManager = FlexboxLayoutManager(binding.root.context)
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.flexDirection = FlexDirection.ROW
                root.layoutManager = layoutManager
            }
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowButtonTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowButtonTemplateBinding.commonBind() {
        val items = (payload[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>)
        val variation = (payload[BotResponseConstants.BUTTON_VARIATION] as String?) ?: ""
        root.adapter = BotButtonsTemplateAdapter(root.context, items, payload, isLastItem, variation, actionEvent)
    }
}