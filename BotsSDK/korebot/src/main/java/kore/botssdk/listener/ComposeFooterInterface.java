package kore.botssdk.listener;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.net.RestResponse;

@SuppressWarnings("UnKnownNullness")
public interface ComposeFooterInterface {
    /**
     * @param message : Title and payload, Both are same
     */
    void onSendClick(String message, boolean isFromUtterance);

    /**
     * @param message : Title of the button
     * @param payload : Payload to be send
     */
    void onSendClick(String message, String payload, boolean isFromUtterance);

    void onSendClick(String message, ArrayList<HashMap<String, String>> attachments, boolean isFromUtterance);

    void onSendClick(BaseBotMessage message, boolean isFromUtterance);

    void copyMessageToComposer(String text, boolean isForOnboard);

    void onDeepLinkClicked(String url);

    void onDeleteClick(BaseBotMessage message);

    /** Returns whether a human agent is currently connected to this chat. */
    default boolean isHumanAgentConnected() {
        return false;
    }

    /** Sends the agent-close event and leaves the chat window open. */
    default void sendAgentCloseEvent() {
    }


}
