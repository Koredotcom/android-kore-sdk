# Artemis Socket SDK demo app

This Android application is a small harness for exercising the public
Artemis Socket SDK connection and chat APIs.

## Run

From `BotsSDK`:

```bash
bash ./gradlew :app:installDebug
```

The tracked asset
`src/main/assets/artemis_example_config.properties` contains placeholders only.
For local verification, create the ignored file
`src/main/assets/artemis_example_config.local.properties` with the same keys
and your environment values:

```properties
environment=dev
endpoint=YOUR_ARTEMIS_ENDPOINT
project_id=YOUR_ARTEMIS_PROJECT_ID
channel_id=YOUR_ARTEMIS_CHANNEL_ID
channel_name=YOUR_ARTEMIS_CHANNEL_NAME
api_key=YOUR_ARTEMIS_API_KEY
```

The local file is ignored and must never be committed. The app automatically
connects on launch and provides the Flutter-style status bar, message bubbles,
timestamps, composer, and reconnect action. Only actual user and assistant
messages are rendered in the chat. Long-press the status bar to inspect the
sanitized API and WebSocket diagnostics.
