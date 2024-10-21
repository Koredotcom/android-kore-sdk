package com.kore.ai.botsdk.row

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ai.botsdk.databinding.RowSampleTemplateBinding
import com.kore.common.event.UserActionEvent
import com.kore.common.row.SimpleListRow
import com.kore.model.BotResponse
import com.kore.ui.databinding.RowAdvanceMultiSelectBinding

class SampleTemplateRow(
    override val type: SimpleListRowType,
    private val botResponse: BotResponse,
    private val isEnabled: Boolean,
    private val actionEvent: (actionEvent: UserActionEvent) -> Unit
) : SimpleListRow() {
    private val id: String = botResponse.messageId
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is SampleTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is SampleTemplateRow) return false
        return otherRow.botResponse.messageId == botResponse.messageId && otherRow.isEnabled == isEnabled
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, "", false, true)
        val childBinding = RowSampleTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            // write the general functionality and call commonBind() for common functionality.
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        // This function would get called when any attribute of botResponse is changed
        val childBinding = RowSampleTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            commonBind()
        }
    }

    private fun RowSampleTemplateBinding.commonBind() {
        // write the common functionality
    }
}