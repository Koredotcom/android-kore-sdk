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

    ```kotlin
        override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
            this.onActionEvent = onActionEvent
        }

        override fun setBrandingHeader(brandingModel: BrandingHeaderModel?) {
            this.brandingModel = brandingModel
        }
    ```

3.  **Reference Implementation:** A sample custom header fragment, `CustomHeaderFragment`, is available in the sample application for your guidance.

4.  **Injection:** Use the following code snippet to integrate your custom header fragment into the BotSDK:

    ```kotlin
        SDKConfig.addCustomHeaderFragment(CustomHeaderFragment());
    ```

---

### BaseContentFragment

This component enables the customization of the main chat content area within the BotSDK. To implement a custom content fragment:

1.  **Extend `BaseContentFragment`:** Create a new Fragment class that extends the `BaseContentFragment` class.
2.  **Override Mandatory Functions:** Implement the following methods according to your custom requirements:

    ```kotlin
        override fun onChatHistory(list: List<BaseBotMessage>, isReconnection: Boolean) {
            addMessagesToAdapter(list, !isMinimized(), isReconnection)
        }

        override fun onFileDownloadProgress(msgId: String, progress: Int, downloadBytes: Int) {
            chatAdapter.onDownloadProgress(msgId, progress, downloadBytes)
        }

        override fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit) {
            this.actionEvent = actionEvent
            chatAdapter.setActionEvent(actionEvent)
        }

        override fun getAdapterLastItem(): BaseBotMessage? {
            return chatAdapter.getAdapterLastItem()
        }

        override fun showTypingIndicator(icon: String?, enable: Boolean) {

        }

        override fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?) {

        }

        override fun hideQuickReplies() {
            binding.quickReplyView.isVisible = false
        }

        override fun addMessagesToAdapter(messages: List<BaseBotMessage>, isHistory: Boolean, isReconnection: Boolean) {

        }

        override fun onLoadingHistory() {
            binding.swipeContainerChat.isRefreshing = true
        }

        override fun onLoadHistory(isReconnect: Boolean) {
            contentViewModel.loadChatHistory(isReconnect)
        }

        override fun getAdapterCount(): Int = chatAdapter.itemCount

        override fun quickRepliesClicked(quickReplyTemplate: Map<String, *>) {

        }

        override fun onBrandingDetails() {
            chatAdapter.onBrandingDetails()
            binding.swipeContainerChat.isEnabled = SDKConfiguration.OverrideKoreConfig.paginatedScrollEnable
        }
    ```

3.  **Reference Implementation:** A sample custom content fragment, `CustomContentFragment`, is provided in the sample application.

4.  **Injection:** Integrate your custom content fragment into BotSDK using the following statement:

    ```kotlin
    SDKConfig.addCustomContentFragment(CustomContentFragment());
    ```

---

### BaseFooterFragment

This component allows for the customization of the bottom input/action area of the BotSDK interface. To create a custom footer fragment:

1.  **Extend `BaseFooterFragment`:** Create a new Fragment class that extends the `BaseFooterFragment` class.
2.  **Override Mandatory Functions:** Implement the following methods based on your desired footer behavior:

    ```kotlin
        override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
            actionEvent = onActionEvent
        }

        override fun enableSendButton(enable: Boolean) {
            isEnabled = enable
            if (isAttachedToWindow) {
                binding.llSend.isVisible = enable && binding.edtTxtMessage.text.trim().isNotEmpty()
                binding.recAudioImg.isVisible = !enable
            }
        }

        override fun setMessage(message: String) {

        }

        override fun showAttachmentActionSheet() {

        }
    ```

3.  **Reference Implementation:** A sample custom footer fragment, `CustomFooterFragment`, is available in the sample application.

4.  **Injection:** Integrate your custom footer fragment into BotSDK using the following statement:

    ```kotlin
        SDKConfig.addCustomFooterFragment(CustomFooterFragment());
    ```

**Note**: For detailed implementation examples and further guidance, please refer the BotSDK sample application.
