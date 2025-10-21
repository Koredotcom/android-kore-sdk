# BotSDK: Customizing Chat Message Templates

---

## Overview

BotSDK provides a set of built-in UI templates for rendering chat messages. You have the flexibility to tailor these templates to meet your specific UI and functional requirements. This includes the ability to either replace existing templates with your own custom implementations or introduce entirely new templates to handle unique message types.

---

## Key Component: SimpleListRow

The `SimpleListRow` class is the fundamental building block for rendering each message within the chat interface. To create a custom template, you will:

1.  **Extend `SimpleListRow`:** Create a new class that inherits from the `SimpleListRow` class provided by the BotSDK.

2.  **Class Definition:** Custom template row class defination should be as follows.

**Note**: Your class must have the constructor number and order of parms should be as follows only. Please don't change the order and add new params.

```java
    class DownloadLinkTemplateRow(
        override val type: SimpleListRowType,
        private val botResponse: BotResponse,
        private val isEnabled: Boolean,
        private val actionEvent: (actionEvent: UserActionEvent) -> Unit
    ) : SimpleListRow()
```

3.  **Implement:** You must override following functions

```java
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DownloadLinkTemplateRow) return false
        return otherRow.botResponse.messageId == botResponse.messageId && otherRow.isEnabled == isEnabled // If no UI update later once it is displayed then return false
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DownloadLinkTemplateRow) return false
        return false
    }

    // This need to implement only when in case areItemsTheSame() returns true
    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, "", true, true)
        val childBinding = RowDownloadLinkTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            if (payload == null) return
            val fileName = payload?.get(FILE_NAME) as String?
            tvPdfItemTitle.text = fileName
            commonBind()
        }
    }

    // This need to implement only when in case areItemsTheSame() returns true
    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowDownloadLinkTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowDownloadLinkTemplateBinding.commonBind() {
        // Write your common functionality which updates the UI on user action
        val isProgressVisible = downloadProgress in 0..99
        ivPdfDownload.isVisible = downloadProgress == -1 || downloadProgress == 100
        pbDownload.isVisible = isProgressVisible
        if (downloadProgress > 0) pbDownload.progress = downloadProgress
        if (downloadProgress != 0) pbDownload.clearAnimation()

        ivPdfDownload.setOnClickListener {
            if (payload != null && payload?.get(URL) != null) {
                pbDownload.isVisible = true
                ivPdfDownload.isVisible = false
                pbDownload.progress = 75
                pbDownload.startAnimation(AnimationUtils.loadAnimation(root.context, com.kore.ui.R.anim.rotate_indefinitely))
                actionEvent(BotChatEvent.DownloadLink(botResponse.messageId, payload?.get(URL).toString(), payload?.get(FILE_NAME) as String?))
            }
        }
    }
```

4.  **Example:** The BotSDK sample application includes a custom template named `DownloadLinkTemplateRow` which serves as a practical example.

---

## Persisting UI State in Custom Templates

To save and retrieve the dynamic state of UI elements within your custom templates (e.g., the checked status of a checkbox or the expanded state of a collapsible section), follow these steps:

1.  **Retrieving State:** Get the saved state value from `payload`.

    ```java
    // Example: Retrieving if a "download progress" button was previously clicked.
        private val isChecked: Int = payload?.get(BotResponseContants.SELECTED_ITEM) as Boolean? ?: false
    ```

2.  **Saving State:** Use the `onSaveState()` to persist the current state of your UI element.

    ```java
    // Example: Saving the state when a "show details" button is clicked.
    onSaveState(id, isChecked, BotResponseContants.SELECTED_ITEM)
    ```

---

## Key Component: SimpleListViewHolderProvider

The `SimpleListViewHolderProvider` class is the fundamental building block for rendering each message within the chat interface. To create a custom template, you will:

1.  **Extend `SimpleListViewHolderProvider<Binding>`:** Create a new class that inherits from the `SimpleListViewHolderProvider<Binding>` class provided by the BotSDK.

2.  **Class Definition:** Custom template provider class defination should be as follows.

```java
    class DownloadLinkTemplateProvider : SimpleListViewHolderProvider<Binding>() {
        override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
            Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
```

---

3. **Action Event** Send message to the Bot from Custom Templates

To enable user interactions within your custom template to trigger sending a message back to the bot, utilize the `actionEvent`:

```java
    actionEvent(BotChatEvent.SendMessage(message = "hi", payload=null))
    actionEvent(BotChatEvent.SendMessage(message = "Submit", payload="12345")) // Here `message` will display on UI and `payload` will be sent to bot server
    actionEvent(BotChatEvent.SendMessage(message = "Submit", payload=null, attachments=attachments)) // Here `message` will display on UI , `payload` and 'attachments' will be sent to bot server.
```

**Note**: Many other events available in `BotChatEvent` class. Based on your requirement you can use.

## Injecting Custom Templates into BotSDK

To register your custom template ViewHolder with the BotSDK and make it available for rendering specific message types, use the SDKConfig.

For Example:

```java
    SDKConfig.addCustomTemplate(providerName="link", templateType="link", provider=DownloadLinkTemplateProvider(),templateRow=DownloadLinkTemplateRow::class)
```

**Note**: For detailed implementation examples and further guidance, please refer the BotSDK sample application.
