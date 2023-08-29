package kore.botssdk.view.row.chatbot.listwidget.buttons

import androidx.viewbinding.ViewBinding
import kore.botssdk.databinding.WidgetButtonListItemBinding
import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.chatbot.listwidget.BotListWidgetRowType

class BotListWidgetButtonRow(
    private val id: String,
) : SimpleListRow {
    override val type: SimpleListRow.SimpleListRowType = BotListWidgetRowType.Buttons

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListWidgetButtonRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListWidgetButtonRow) return false
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as WidgetButtonListItemBinding).apply {

        }
    }
}