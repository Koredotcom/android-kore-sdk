package kore.botssdk.view.row.chatbot

import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.view.row.botrequestresponse.BotRequestResponseProvider
import kore.botssdk.view.row.listwidget.BotListWidgetProvider
import kore.botssdk.view.row.listwidget.BotListWidgetRow

enum class ChatBotRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    RequestMsg(BotRequestResponseProvider()),
    ResponseMsg(BotRequestResponseProvider()),
    ListWidget(BotListWidgetProvider());

    companion object {
        fun find(type: Int): ChatBotRowType? = values().find { it.ordinal == type }
    }
}