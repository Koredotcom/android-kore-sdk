package kore.botssdk.view.row.chatbot

import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.view.row.chatbot.botrequestresponse.BotRequestResponseProvider
import kore.botssdk.view.row.chatbot.listwidget.BotListWidgetProvider
import kore.botssdk.view.row.chatbot.listwidget.BotListWidgetRow
import kore.botssdk.view.row.chatbot.showmore.BotShowMoreButtonProvider
import kore.botssdk.view.row.chatbot.showmore.BotShowMoreButtonRow

enum class ChatBotRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    RequestMsg(BotRequestResponseProvider()),
    ResponseMsg(BotRequestResponseProvider()),
    ListWidget(BotListWidgetProvider()),
    ShowMore(BotShowMoreButtonProvider());

    companion object {
        fun find(type: Int): ChatBotRowType? = values().find { it.ordinal == type }
    }
}