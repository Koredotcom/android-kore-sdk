package kore.botssdk.view.row.chatbot.showmore

import androidx.viewbinding.ViewBinding
import kore.botssdk.databinding.RowBotShowMoreButtonBinding
import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.chatbot.ChatBotRowType

class BotShowMoreButtonRow(private val id: String) : SimpleListRow {
    override val type: SimpleListRow.SimpleListRowType = ChatBotRowType.ShowMore

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotShowMoreButtonRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotShowMoreButtonRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowBotShowMoreButtonBinding).apply {
            showMore.setOnClickListener { }
        }
    }
}