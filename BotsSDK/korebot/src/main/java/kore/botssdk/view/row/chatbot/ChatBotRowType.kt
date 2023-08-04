package kore.botssdk.view.row.chatbot

import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.SimpleListViewHolderProvider
import kore.botssdk.view.row.botrequestresponse.BotRequestResponseProvider

enum class ChatBotRowType(
    override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
    RequestMsg(BotRequestResponseProvider()),
    ResponseMsg(BotRequestResponseProvider());

    companion object {
        fun find(type: Int): ChatBotRowType? = values().find { it.ordinal == type }
    }
}