# Artemis Android Socket SDK

This branch contains a standalone Android socket library and a small example
application used to verify the API-key bootstrap, WebSocket session, and chat
message flow.

## Modules

The library module is `BotsSDK/artemis_socket_sdk` and its public Java package
is `artemis.socket`.

The example application module is `BotsSDK/app` and its package/application ID
is `com.artemis.socket`.

```groovy
include ':artemis_socket_sdk', ':app'
```

```groovy
implementation(project(':artemis_socket_sdk'))
```

## Headless socket API

```java
ArtemisSocketConfiguration configuration = ArtemisSocketConfiguration.builder()
    .endpoint(endpoint)
    .projectId(projectId)
    .apiKey(apiKey)
    .channelId(channelId)
    .channelName(channelName)
    .build();

ArtemisSocketClient client = new ArtemisSocketClient(configuration, listener);
client.connect();
client.sendMessage("Hello");
client.submitAction("select-plan", "basic", null, "render-1");
client.submitFeedback("assistant-message-id", "star", 5, null, null,
    new ArtemisFeedbackCallback() {
        @Override
        public void onSuccess(String feedbackId) {
            // Runtime acknowledged this rating.
        }

        @Override
        public void onFailure(String code, String message) {
            // Rejected, disconnected before acknowledgement, or timed out.
        }
    });
client.disconnect();
client.shutdown();
```

`submitAction` sends an `action_submit` frame and throws if the action is
invalid, the client is disconnected, or the socket rejects it. Optional value,
form data, and render ID are omitted when null. Action submission is a transport
send; it does not provide a server acknowledgement.

`submitFeedback` sends a `feedback.submit` frame and correlates a matching
`feedback.ack` by message ID and optional action render ID. The default timeout
is 10 seconds; an overload accepts a custom positive timeout in milliseconds.
Success and failure callbacks run on the Android main thread. A successful ack
must include a nonblank `feedbackId`. A rejection may provide `error.code` and
`error.message`. Failure codes include `FEEDBACK_TIMEOUT`, `DISCONNECTED`,
`SEND_REJECTED` and `FEEDBACK_REJECTED`. Only one feedback submission for a
message/render ID may be pending at a time.

Accepted rating types are `thumbs` (values 0 or 1) and `star` (values 1–10).
Feedback frames use this shape:

```json
{"type":"feedback.submit","messageId":"m1","ratingType":"star","ratingValue":5}
```

The matching acknowledgement uses this shape:

```json
{"type":"feedback.ack","messageId":"m1","success":true,"feedbackId":"f1"}
```

## Example application

The example reads `endpoint`, `project_id`, `channel_id`, `channel_name`, and
`api_key` from the asset configuration file. It automatically connects when
opened and provides the same chat-focused experience as the Flutter example:

- App bar with reconnect action
- Connection status strip
- User and assistant message bubbles with timestamps
- Message composer and send button
- API and socket diagnostics available by long-pressing the status strip

Only actual user and assistant message text is rendered in the chat. Socket
events and action frames are diagnostics-only; the example does not yet expose
a UI for submitting actions or feedback.

## Build

From `BotsSDK`:

```bash
bash ./gradlew :app:assembleDebug :artemis_socket_sdk:test
```
