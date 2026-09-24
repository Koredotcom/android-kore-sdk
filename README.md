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
client.disconnect();
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
events and action frames are diagnostics-only.

## Build

From `BotsSDK`:

```bash
bash ./gradlew :app:assembleDebug :artemis_socket_sdk:test
```
