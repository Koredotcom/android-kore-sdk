package kore.botssdk.view.row.chatbot.listwidget

import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.view.row.chatbot.listwidget.buttons.BotListWidgetButtonProvider
import kore.botssdk.view.row.chatbot.listwidget.detail.BotListWidgetDetailProvider

enum class BotListWidgetRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    Buttons(BotListWidgetButtonProvider()),
    Details(BotListWidgetDetailProvider());

    companion object {
        fun find(type: Int): BotListWidgetRowType? = values().find { it.ordinal == type }
    }
}