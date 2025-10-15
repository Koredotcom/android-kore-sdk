package kore.botssdk.listener;

import java.util.HashMap;

import kore.botssdk.models.BotRequest;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingModel;

@SuppressWarnings("UnKnownNullness")
public interface BotChatViewListener extends BaseView {
    void addMessageToAdapter(BotResponse baseBotMessage);

    void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection);

    void onBrandingDetails(BrandingModel brandingModel);

    void updateContentListOnSend(BotRequest botRequest);

    void showTypingStatus();

    void stopTypingStatus();

    void setIsAgentConnected(boolean isAgentConnected);

    void enableSendButton();

    void processPayload(String payload, BotResponse botResponse);

    void displayMessage(String text, String type, String messageId);

    void updateTitleBar(BaseSocketConnectionManager.CONNECTION_STATE state);

    void showReconnectionStopped();

    void getBrandingDetails();
    void loadOnConnectionHistory(boolean isReconnect);
    void addAttachmentToAdapter(HashMap<String, String> attachmentKey);
    void uploadBulkFile(String fileName, String filePath, String extn, String filePathThumbnail, String orientation);
}
