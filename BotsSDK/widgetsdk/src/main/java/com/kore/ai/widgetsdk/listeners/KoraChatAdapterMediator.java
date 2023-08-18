package com.kore.ai.widgetsdk.listeners;

import com.kore.ai.widgetsdk.models.BaseBotMessage;

import java.util.ArrayList;

public interface KoraChatAdapterMediator {
    void addMessageToChatAdapter(BaseBotMessage botResponse);
    void addMessagesToChatAdapter(ArrayList<BaseBotMessage> msgList);
    int getItemCount();
    BaseBotMessage getItem(int position);
    void actionScrollUp(int firstVisible, int lastVisible);
    void deleteMessageFromAdapter();
    void clearAllMessages();
    ArrayList<BaseBotMessage> getItemRange(int start, int end);
    void animateView(boolean selected);
    boolean isEOD();
//    public void setEndOfDialog(boolean  isEOD);
}