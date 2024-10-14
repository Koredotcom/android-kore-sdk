package kore.botssdk.listener;

import java.util.ArrayList;

import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotRequest;

public interface BotContentFragmentUpdate extends BaseView {
    void updateContentListOnSend(BotRequest botRequest);

    void onChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean scrollToBottom);
    void onReconnectionChatHistory(ArrayList<BaseBotMessage> list, int offset, boolean isReconnectionHistory);
}
