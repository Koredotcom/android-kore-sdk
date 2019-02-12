package kore.botssdk.listener;

import android.os.Bundle;

import kore.botssdk.models.FormActionTemplate;

public interface ComposeFooterInterface {
    /**
     * @param message : Title and payload, Both are same
     */
    void onSendClick(String message);

    /**
     * @param message : Title of the button
     * @param payload : Payload to be send
     */
    void onSendClick(String message, String payload);

    void onFormActionButtonClicked(FormActionTemplate fTemplate);

    void launchActivityWithBundle(int type,Bundle payload);

    void sendWithSomeDelay(String message,String payload,long time);
    void copyMessageToComposer(String text);
}