package com.kore.ai.botsdk.row

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ai.botsdk.databinding.RowSampleTemplateBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.model.BotResponse

class SampleNewTemplateRow(
    override val type: SimpleListRowType,
    private val botResponse: BotResponse,
    private val isEnabled: Boolean,
    private val actionEvent: (actionEvent: UserActionEvent) -> Unit
) : SimpleListRow() {
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is SampleNewTemplateRow) return false
        return otherRow.botResponse.messageId == botResponse.messageId
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is SampleNewTemplateRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, "", true, true)
        val childBinding = RowSampleTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
        }
    }
}