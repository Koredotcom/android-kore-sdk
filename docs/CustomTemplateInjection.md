# BotSDK: Customizing Chat Message Templates

---

## Overview

BotSDK provides a set of built-in UI templates for rendering chat messages. You have the flexibility to tailor these templates to meet your specific UI and functional requirements. This includes the ability to either replace existing templates with your own custom implementations or introduce entirely new templates to handle unique message types.

---

## Key Component: BaseViewHolder

The `BaseViewHolder` class is the fundamental building block for rendering each message within the chat interface. To create a custom template, you will:

1.  **Extend `BaseViewHolder`:** Create a new class that inherits from the `BaseViewHolder` class provided by the BotSDK.

2.  **Override Core Methods:** Implement the necessary methods from `BaseViewHolder` to define how your custom template displays data and handles any associated functionality.

3.  **Implement `getInstance(ViewGroup parent)`:** You **must** define a static method named `getInstance(ViewGroup parent)` within your custom template class. This method is responsible for inflating the layout for your template and returning a new instance of your custom ViewHolder.

    ```java
    public static LinkTemplateHolder getInstance(ViewGroup parent) {
        return new LinkTemplateHolder(createView(R.layout.pdf_download_view, parent));
    }

    private LinkTemplateHolder(@NonNull View view) {
        super(view, view.getContext());

        this.tvPdfName = view.findViewById(kore.botssdk.R.id.tv_pdf_item_title);
        this.ivPdfDownload = view.findViewById(kore.botssdk.R.id.ivPdfDownload);
        this.pbDownload = view.findViewById(kore.botssdk.R.id.pbDownload);
    }
    ```

4.  **Implement `bind(BaseBotMessage baseBotMessage)`:** Override this method to handle the process of binding the data from a `BaseBotMessage` object to the UI elements within your custom template.

    ```java
    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) {
            return;
        }
        // Implement your custom logic here to extract data from 'payloadInner'
        // and update the UI elements of your template accordingly.
        // For example:
        // this.tvPdfName.setText(payloadInner.getTitle());
        // this.ivPdfDownload.setOnClickListener(v -> openPdf(payloadInner.getUrl()));
    }
    ```

5.  **Example:** The BotSDK sample application includes a custom template named `LinkTemplateHolder` which serves as a practical example.

---

## Persisting UI State in Custom Templates

To save and retrieve the dynamic state of UI elements within your custom templates (e.g., the checked status of a checkbox or the expanded state of a collapsible section), follow these steps:

1.  **Retrieving State:** Access the `contentState` map associated with the `BotResponse` object of the message. Check if the key corresponding to the UI element's state exists and retrieve its value.

    ```java
    // Example: Retrieving if a "show details" button was previously clicked.
    Map<String, Object> contentState = ((BotResponse) baseBotMessage).getContentState();
    if (contentState != null && contentState.containsKey(BotResponse.SHOW_DETAILS)) {
        boolean showDetails = (Boolean) contentState.get(BotResponse.SHOW_DETAILS);
        // Use the 'showDetails' value to update the UI (e.g., expand the details section).
    }
    ```

2.  **Saving State:** Use the `contentStateListener` to persist the current state of your UI element.

    ```java
    // Example: Saving the state when a "show details" button is clicked.
    contentStateListener.onSaveState(msgId, true, BotResponse.SHOW_DETAILS);
    ```

---

## Sending Messages to the Bot from Custom Templates

To enable user interactions within your custom template to trigger sending a message back to the bot, utilize the `composeFooterInterface`:

```java
composeFooterInterface.onSendClick(
    messageToDisplay, // The text to be displayed in the input field (can be null or empty).
    payload,          // The data or message payload to be sent to the bot.
    false             // Indicates whether it's a direct user utterance (typically false for template interactions).
);
```

## Injecting Custom Templates into BotSDK

To register your custom template ViewHolder with the BotSDK and make it available for rendering specific message types, use the SDKConfig. Replace "template_type" with the unique identifier corresponding to the type of message your custom template is designed to handle.

```java
    SDKConfig.setCustomTemplateViewHolder("template_type", CustomTemplateHolder.class);
```

**Note**: For detailed implementation examples and further guidance, please refer the BotSDK sample application.
