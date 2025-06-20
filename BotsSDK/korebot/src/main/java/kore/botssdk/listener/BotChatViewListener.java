package kore.botssdk.listener;

import java.util.HashMap;

import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.EventModel;

@SuppressWarnings("UnKnownNullness")
public interface BotChatViewListener extends BaseView {
    void addMessageToAdapter(BotResponse baseBotMessage);

    void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection);

    void onBrandingDetails(BotBrandingModel brandingModel);

    void updateContentListOnSend(BotRequest botRequest);

    void showTypingStatus();

    void setIsAgentConnected(boolean isAgentConnected);

    void enableSendButton();

    void processPayload(String payload, BotResponse botResponse);

    void displayMessage(String text, String type, String messageId);

    void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE state);

    void showReconnectionStopped();

    void getBrandingDetails();
    void uploadBulkFile(String fileName, String filePath, String extn, String filePathThumbnail, String orientation);

    void showAlertDialog(EventModel eventModel);

    void hideAlertDialog();
    void loadOnConnectionHistory(boolean isReconnect);
}
