# Customizing UI Fragments in BotSDK

---

## Overview

BotSDK provides a modular UI structure with predefined fragments for the Header, Content, and Footer. This allows developers to easily customize the look and feel, as well as the functionality, of these key UI elements according to their specific application requirements.

---

## Main Components

### BaseHeaderFragment

This component allows you to implement a custom header for the BotSDK interface. To do this:

1.  **Extend `BaseHeaderFragment`:** Create a new Fragment class that extends the `BaseHeaderFragment` class provided by the SDK.
2.  **Override Mandatory Functions:** Implement the required methods inherited from `BaseHeaderFragment`.

    ```java
    @Override
    public void setBrandingDetails(BrandingModel brandingModel) {
        this.brandingModel = brandingModel;
        updateUI(); // Implement your custom UI update logic here.
    }
    ```

3.  **Reference Implementation:** A sample custom header fragment, `CustomHeaderFragment`, is available in the sample application for your guidance.

4.  **Injection:** Use the following code snippet to integrate your custom header fragment into the BotSDK:

    ```java
    SDKConfig.addCustomHeaderFragment(new CustomHeaderFragment());
    ```

---

### BaseContentFragment

This component enables the customization of the main chat content area within the BotSDK. To implement a custom content fragment:

1.  **Extend `BaseContentFragment`:** Create a new Fragment class that extends the `BaseContentFragment` class.
2.  **Override Mandatory Functions:** Implement the following methods according to your custom requirements:

    ```java
    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipeContainerChat);
    }

    @Override
    protected ChatAdapter getChatAdapter() {
        return new ChatAdapter(); // Return your custom ChatAdapter implementation.
    }

    @Override
    public void changeThemeBackGround(String bgColor, String textBgColor, String textColor, String botName) {
        // Implement your theme change logic.
    }

    @Override
    public void showTypingStatus() {
        // Implement how the typing status is displayed.
    }

    @Override
    public void setQuickRepliesIntoFooter(BotResponse botResponse) {
        // Implement how quick replies are handled and displayed.
    }

    @Override
    public void addMessageToBotChatAdapter(BotResponse botResponse) {
        // Implement how a single bot message is added to the chat.
    }

    @Override
    public void addMessagesToBotChatAdapter(ArrayList<BaseBotMessage> list, boolean scrollToBottom) {
        // Implement how a list of messages is added to the chat.
    }

    @Override
    public void addMessagesToBotChatAdapter(@NonNull ArrayList<BaseBotMessage> list, boolean scrollToBottom, boolean isFirst) {
        // Implement how a list of messages is added, indicating if it's the first set.
    }

    @Override
    public void updateContentListOnSend(BotRequest botRequest) {
        // Implement how the content list is updated after sending a message.
    }
    ```

3.  **Reference Implementation:** A sample custom content fragment, `CustomContentFragment`, is provided in the sample application.

4.  **Injection:** Integrate your custom content fragment into BotSDK using the following statement:

    ```java
    SDKConfig.addCustomContentFragment(new CustomContentFragment());
    ```

---

### BaseFooterFragment

This component allows for the customization of the bottom input/action area of the BotSDK interface. To create a custom footer fragment:

1.  **Extend `BaseFooterFragment`:** Create a new Fragment class that extends the `BaseFooterFragment` class.
2.  **Override Mandatory Functions:** Implement the following methods based on your desired footer behavior:

    ```java
    @Override
    public void setDisabled(boolean disabled) {
        // Implement how the footer is visually disabled/enabled.
    }

    @Override
    public void updateUI() {
        // Implement any UI updates required in the footer.
    }

    @Override
    public void setBottomOptionData(BotOptionsModel botOptionsModel) {
        // Implement how bottom options (e.g., buttons) are displayed.
    }

     @Override
    public void setTtsUpdate(TTSUpdate ttsUpdate) {
        // Implement how Text-to-Speech updates are handled.
    }

    @Override
    public void enableSendButton() {
        // Implement the logic to enable the send button.
    }

    @Override
    public boolean isTTSEnabled() {
        // Return the current state of Text-to-Speech.
        return false; // Modify as needed.
    }

    @Override
    protected void onRecordAudioPermissionGranted() {
        // Implement actions to take when audio recording permission is granted.
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        // Implement handling of partial speech recognition results.
    }

    @Override
    public void onSpeechResult(String result) {
        // Implement handling of the final speech recognition result.
    }

    @Override
    public void setComposeText(String text) {
        // Implement how the input text field is set.
    }

    /**
     * this method update the ui of send button based on enable/disable
     */
    @Override
    public void enableOrDisableSendButton(boolean enable) {
        // Implement the logic to enable or disable the send button.
    }

    @Override
    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey) {
        // Implement how attachments are added to the input area.
    }
    ```

3.  **Reference Implementation:** A sample custom footer fragment, `CustomFooterFragment`, is available in the sample application.

4.  **Injection:** Integrate your custom footer fragment into BotSDK using the following statement:

    ```java
        SDKConfig.addCustomFooterFragment(new CustomFooterFragment());
    ```

**Note**: For detailed implementation examples and further guidance, please refer the BotSDK sample application.
