package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.databinding.RowTimeStampTemplateBinding

class TimeStampTemplateRow(private val id: String, private val formattedTime: String) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_TIME_STAMP_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TimeStampTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TimeStampTemplateRow) return false
        return otherRow.formattedTime == formattedTime
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        val childBinding = RowTimeStampTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply { timeStamp.text = formattedTime }
    }
}