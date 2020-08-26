package com.kore.ai.widgetsdk.listeners;

public interface WidgetComposeFooterInterface
{
    /**
     * @param message : Title and payload, Both are same
     */
    void onPanelSendClick(String message, boolean isFromUtterance);

    /**
     * @param message : Title of the button
     * @param payload : Payload to be send
     */
    void onPanelSendClick(String message, String payload, boolean isFromUtterance);

}
