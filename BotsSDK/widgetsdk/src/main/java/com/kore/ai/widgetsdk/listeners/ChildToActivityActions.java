package com.kore.ai.widgetsdk.listeners;

import com.kore.ai.widgetsdk.models.PayloadInner;

public interface ChildToActivityActions {
    void doTheNeedFul();
    void toggleTypingStatusVisibility(boolean b, String url);
    void setQuickRepliesIntoFooter(PayloadInner payloadInner);
    void setFormActionsIntoFooter(PayloadInner payloadInner);
    void setPlaceHolderText(String text);
    void subscribeToParent(boolean subscribe);
    void shootUtterance(String msg, String payload, String body, boolean scrollUpNeeded);
    void pasteUtteranceToComposeBar(String msg);
    void sessionExpired(String type);
    void handleSkillsFromChild(PayloadInner payloadInner, boolean skill, boolean loadedhistory);
    void disableOrEnableButtons(boolean disable);
}
