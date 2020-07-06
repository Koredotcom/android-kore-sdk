package com.kore.ai.widgetsdk.listeners;

import com.kore.ai.widgetsdk.models.BaseBotMessage;

import java.util.ArrayList;

public interface KoraChatAdapterMediator {
    public void addMessageToChatAdapter(BaseBotMessage botResponse);
    public void addMessagesToChatAdapter(ArrayList<BaseBotMessage> msgList);
    public int getItemCount();
    public BaseBotMessage getItem(int position);
    public void actionScrollUp(int firstVisible, int lastVisible);
    public void deleteMessageFromAdapter();
    public void clearAllMessages();
    public ArrayList<BaseBotMessage> getItemRange(int start, int end);
    public void animateView(boolean selected);
    public boolean isEOD();
//    public void setEndOfDialog(boolean  isEOD);
}