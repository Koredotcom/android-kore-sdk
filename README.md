## Kore SDK
Kore offers Bots SDKs as a set of platform-specific client libraries that provide a quick and convenient way to integrate Kore Bots chat capability into custom applications.

With just a few lines of code, you can embed our Kore chat widget into your applications to enable end-users to interact with your applications using Natural Language. For more information, refer to https://developer.kore.com/docs/bots/kore-web-sdk/

## Kore Android SDK for developers

Kore SDK for Android enables you to talk to Kore bots over a web socket. This repo also comes with the code for sample applications that developers can modify according to their Bot configuration.

# Setting up

## Prerequisites
 Service to generate JWT (JSON Web Tokens)- this service will be used in the assertion function injected to obtain the connection.
 SDK app credentials 
 Login to the Bots platform
Navigate to the Bot builder
Search and click on the bot 
Enable *Web / Mobile Client* channel against the bot as shown in the screen below.
		Add bot to Web/Mobile Client  channel
create new or use existing SDK app to obtain client id and client secret Obtain Client id and Client secret

## Instructions

# Configuration changes
Kore is providing botclient library to communicate with the bot and ui to display content and widgets.
"BotClient" with "ui" and "widgets" -> Which can be used to communicate with the bot and can utilize the predefined templates, activities, fragments and widgets.

Create your own app and add the dependency(implementation 'com.github.Koredotcom.android-kore-sdk:botclient:11.0.2') for botclient and create following classes in your app module.

Following are the steps needed to follow to integrate the libraries.
To use these libraries we must configure the bot credentials as follows.

#### Config-1:
```
    val botConfigModel = BotConfigModel(
        "<botname>",
        "<bot_id>",
        "<bot_client_id>",
        "<bot_client_secret>",
        "<bot_base_url>",
        "<identity>",  -> Identity should be **unique for each device**. Don't use "UUID.randomUUID().toString()". It's better to use your device id. If we use the same identity for multiple devices then we will face the session contamination issue.
        <isWebHook -> true/false>, -> if You want to use webhook then put "true" otherwise "false".
        "<jwt_token_url>", -> Jwt token generation url(If you want to use your own jwt token url).
        <enablePanel -> true/false>  - > To use the widgets in your application and display then this value should be "true". Otherwise "false". To use this feature then need to use the "widgets" library using "implementation ''" in your gradle file.
    )

 SDKConfiguration.initialize(botConfigModel)

```
#### Config-2:

To use the widgets library, you need to make the following configuration.

```
val widgetConfigModel = WidgetConfigModel(
        "<botname>",
        "<bot_id>",
        "<bot_client_id>",
        "<bot_client_secret>",
        "<bot_base_url>",
        "<identity>",  -> Identity should be **unique for each device**. Don't use "UUID.randomUUID().toString()". It's better to use your device id. If we use the same identity for multiple devices then we will face the session contamination issue.
        "<jwt_token_url>", -> Jwt token generation url(If you want to use your own jwt token url).
    )

WidgetSDKConfiguration.initialize(widgetConfigModel)

```

# Running the Demo app
*	Create a new application.
*	Add library dependency in your gradle file as follows.
*	Do the configuration as mentioned above.
  
### How to integrate BotSdk through gradle implementation for demo app

**1**. Add below snippet in project/build.gradle
```
 maven { url 'https://www.jitpack.io' }
```
**2**. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom.android-kore-sdk:botclient:11.0.2'
```
**3**. You can initialize the bot by providing the bot config like mentioned in Config-1 and Config-2

**4**. You can navigate to the bot chat window through Intent as below snippet
```
Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
startActivity(intent);
```
**5**. You can have your customized templates without touching the SDK code can check it in below provided branch
   
  https://github.com/Koredotcom/android-kore-sdk/tree/master_gradle

### How to integrate BotSdk through gradle implementation to customize

### Step-1. Add below snippet in project/build.gradle
```   
maven { url 'https://www.jitpack.io' }
```
### Step-2. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom.android-kore-sdk:botclient:11.0.2'
```
### Step-3. You can change the bot config and widget config like below
```

val botConfigModel = BotConfigModel(
        "SDK Demo",
        "st-f59fda8f-e42c-5c6a-bc55-3395c109862a",
        "cs-8fa81912-0b49-544a-848e-1ce84e7d2df6",
        "DnY4BIXBR0Ytmvdb3yI3Lvfri/iDc/UOsxY2tChs7SY=",
        "https://platform.kore.ai/",
        "123456789078643234567",
        false,
        "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts",
        false
    )

 SDKConfiguration.initialize(botConfigModel)

```

```
val widgetConfigModel = WidgetConfigModel(
        "SDK Demo",
            "st-7716ac36-2786-5bdf-b2cc-0d50474b6f87",
            "cs-ff66f2cb-440b-5f37-8a7a-680e109066a8",
            "GHCir4+c3mKkqpwiTRipaXzPj5ykFUWkKMPAGhc1m3U=",
            "https://sit-xo.kore.ai/",
            "123456789078643234567",
            "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts",
    )

WidgetSDKConfiguration.initialize(widgetConfigModel)

```
### Step-4. You can initialize and Set the callback listener for BotClient class.

```
private val botClient: BotClient = BotClient.getInstance()

botClient.setListener(object : BotConnectionListener {
	
		// This will trigger when received a response bot.
    override fun onBotResponse(response: String?) {
		}
		
		// This will trigger when bot connection state is changed.
		override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
		}
		
		// This will trigger once request(utterance) is sent to Bot.
		override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
		}
		
		// This will trigger when a JWT token is created.
		override suspend fun onJwtTokenGenerated(token: String) {
		}
```

### Step-6. Connect to Bot using the following apis.

```
botClient.connectToBot(isFirstTime: Boolean) -> This api is to connect bot using default jwt token generation mechanism.
```
```
botClient.connectToBot(isFirstTime: Boolean, headers: HashMap<String, Any>, body: HashMap<String, Any>) -> This api is to connect bot using own Jwt token generation url. Important note is here "headers" must not be null.
```
```
botClient.connecToBot(isFirstTime: Boolean, jwtToken: String) -> This api is to connect bot using its own Jwt token which is created separately.
```

### Step-7. Send a message to Bot.

```
botClient.sendMessage(msg: String, payload: String?)

msg -> Message to display in the chat ui.
payload -> message to send to the bot.
```

If "msg" and "payload" are same then pass "msg" param as string and "payload" as null.

Following Api is to send the message with attachments.

 ```
botClient.sendAttachmentMessage(msg: String, attachments: List<Map<String, *>>?)

msg -> Message to display in the chat ui.
attachments -> List of attachment file names which are uploaded to server.
```

### Step-8. Disconnect the bot:
Invoke to disconnect previous socket connection upon closing Activity/Fragment or upon destroying view based on requirement.
```
botClient.disconnectBot()
```

### Step-9. Create user own custom templates:
Kore is providing predefined templates. You can use these templates as is or you can override existing templates and/or can add new templates. Following is the procedure to override existing templates and add new templates.

Make sure to add overridden templates and new templates to BotChatRowType class **before creating the BotChatAdapter object**

```
BotChatRowType.prepareDefaultRowTypes() This should trigger before calling following statement

val chatAdapter = BotChatAdapter(requireActivity(),BotChatRowType.getAllRowTypes())

```
Following are the details about params used in the above code snippet of adding overridden and new templates.

BotChatAdapter.addCustomTemplate(BotChatRowType.ROW_BUTTON_PROVIDER, BotResponseConstants.TEMPLATE_TYPE_BUTTON, SampleTemplateProvider(), SampleTemplateRow::class)
BotChatAdapter.addCustomTemplate("link", "link", DownloadLinkTemplateProvider(), DownloadLinkTemplateRow::class)


```
BotChatRowType.ROW_BUTTON_PROVIDER -> Existing row provider name to override. Note: We must use the existing provider name and provider class to override.

SampleTemplateProvider() -> It is the provider which is created to override the existing one.

SampleTemplateRow::class -> Template row class which uses UI to display by overriding existing template row class.

"link" -> New provider name to use for the new template.

DownloadLinkTemplateProvider() -> New Template provider to add and show.

"link" -> This should be the same as in the bot response inner payload "template_type" value.

DownloadLinkTemplateRow::class -> New row class to display the new template row.

```

To create the new Template Row classes, you need to create custom provider and Row classes by extending SimpleListViewHolderProvider and SimpleListRow classes respectively of botclient lib.
Please create your custom Row classes as follows.

```
class DownloadLinkTemplateProvider : SimpleListViewHolderProvider<Binding>() {
    override fun inflateBinding(parent: ViewGroup, viewType: Int): Binding =
        Binding.inflate(LayoutInflater.from(parent.context), parent, false)
}

class DownloadLinkTemplateRow(
    override val type: SimpleListRowType,
    private val botResponse: BotResponse,
    private val isEnabled: Boolean,
    private val actionEvent: (actionEvent: UserActionEvent) -> Unit
) : SimpleListRow() {
    private var payload: Map<String, Any>? = (botResponse.message[0].cInfo?.body as PayloadOuter).payload
    private val downloadProgress: Int = payload?.get(PROGRESS) as Int? ?: -1

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DownloadLinkTemplateRow) return false
        return otherRow.botResponse.messageId == botResponse.messageId && otherRow.isEnabled == isEnabled
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DownloadLinkTemplateRow) return false
        return false
    }

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

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowDownloadLinkTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowDownloadLinkTemplateBinding.commonBind() {
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
                // actionEvent(BotChatEvent.DownloadLink(botResponse.messageId, payload?.get(URL).toString(), payload?.get(FILE_NAME) as String?))
                actionEvent(BotChatEvent.DownloadLink(botResponse.messageId, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "BigBuckBunnnnnnyyyy.mp4"))
            }
        }
    }
}
```

### Step-10:
Please create the RowType class as follows and add rows to the recycler view.

**1**. If you want to use the existing BotChatAdapter then the following is the code snippet.

Ex: 
```
val chatAdapter = BotChatAdapter(requireActivity(),BotChatRowType.getAllRowTypes())
chatAdapter.setActionEvent(actionEvent)

recyclerView.adapter = chatAdapter
chatAdapter.submitList(chatAdapter.addAndCreateRows(messages, isHistory))

messages -> list of messages(Bot request and responses)
isHistory -> If the “messages” is a list of chat history then it should be true otherwise false.
```
**2**. If you would like to use your own RowType class then following is the code snippet.

You have to create your own Row class and Provider class or you can use predefined row and provider classes and use the following steps.
```
enum class MyRowType(
   override val provider: SimpleListViewHolderProvider<*>
) : SimpleListRow.SimpleListRowType {
   Item(MyTemplateItemProvider()),
   Details(DetailsProvider());
}

val chatAdapter = SimpleListAdapter(MyRowType.values().asList())

recyclerView.adapter = chatAdapter
val rows = createRows(messages)
chatAdapter.submitList(rows)

private fun createRows(messages: List<BaseBotMessage>): List<SimpleListRow>{
   return messages.map{ it ->
     if(it.type == “abc”) MyTemplateItemRow(it,.....)
     else DetailsRow(it,.....)
   }
}
```
### How to enable API based (webhook channel) message communication
----
1. Enable the webhook channel by following the below link
	https://developer.kore.ai/docs/bots/channel-enablement/adding-webhook-channel/
	
2. Follow Config-1, Config-2 and Step-1 to Step-10.

### How to integrate widget panel

Add BottomPanelFragment to your view and call the following functions.
```
bottomPanelFragment.setActionEvent(this::widgetActionEvent)
widgetActionEvent -> is an inline function to pass to the fragment to get the user action events.
```
```
bottomPanelFragment.closePanel() to close the bottom sheet panel.
```

### License
Copyright © Kore, Inc. MIT License; see LICENSE for further details.

