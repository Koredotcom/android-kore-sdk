package kore.botssdk.listener;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.CalEventsTemplateModel.Duration;
import kore.botssdk.models.FormActionTemplate;

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

    void onFormActionButtonClicked(FormActionTemplate fTemplate);

    void launchActivityWithBundle(String type,Bundle payload);

    void sendWithSomeDelay(String message,String payload,long time, boolean isScrollUpNeeded);
    void copyMessageToComposer(String text);
    void showMentionNarratorContainer(boolean show, String natxt, String cotext, String handFocus);
    void openFullView(String templateType, String data, Duration duration);
    void updateActionbar(boolean selected,String templateType,ArrayList<BotButtonModel> buttonModels);

    void lauchMeetingNotesAction(Context context,String mid, String eid);
}