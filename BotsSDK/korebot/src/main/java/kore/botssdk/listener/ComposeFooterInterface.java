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

    void onFormActionButtonClicked(FormActionTemplate fTemplate);

    void launchActivityWithBundle(String type, Bundle payload);

    void sendWithSomeDelay(String message, String payload, long time, boolean isScrollUpNeeded);

    void copyMessageToComposer(String text, boolean isForOnboard);

    void showMentionNarratorContainer(boolean show, String natxt, String cotext, String handFocus, boolean isEnd, boolean showOverlay, String templateType);

    void openFullView(String templateType, String data, CalEventsTemplateModel.Duration duration, int position);

    void updateActionbar(boolean selected, String templateType, ArrayList<BotButtonModel> buttonModels);

    void lauchMeetingNotesAction(Context context, String mid, String eid);

    void showAfterOnboard(boolean isDiscardClicked);

    void onPanelClicked(Object pModel, boolean isFirstLaunch);

    void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id);

    void externalReadWritePermission(String fileUrl);
    void onDeepLinkClicked(String url);
}