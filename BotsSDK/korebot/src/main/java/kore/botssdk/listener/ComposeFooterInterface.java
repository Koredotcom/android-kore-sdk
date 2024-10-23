package kore.botssdk.listener;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.models.KnowledgeCollectionModel;

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

    void sendWithSomeDelay(String message, String payload, long time, boolean isScrollUpNeeded);

    void copyMessageToComposer(String text, boolean isForOnboard);

    void externalReadWritePermission(String fileUrl);

    void onDeepLinkClicked(String url);
    void sendImage(String fP, String fN, String fPT);
}