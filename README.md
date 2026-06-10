# Kore WebRTC / Voice Mode SDK (Android)

Standalone Android project containing the **voicemodesdk** library from BotSDK, plus a sample app that launches the voice call UI.

## Project structure

| Module | Description |
|--------|-------------|
| `voicemodesdk` | WebRTC voice SDK (SIP signaling, JWT auth, `SampleVoiceActivity`) |
| `app` | Sample launcher (`com.kore.voicemode`) that opens `SampleVoiceActivity` |

## Prerequisites

- Android Studio (Ladybug or newer recommended)
- JDK 17
- Android SDK with API 34

Create `local.properties` at the project root (or open in Android Studio, which creates it automatically):

```properties
sdk.dir=/path/to/Android/sdk
```

## Build

```bash
./gradlew assembleDebug
```

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`

## Run

1. Open this folder in Android Studio.
2. Select the **app** configuration and a device/emulator.
3. Run, or install the APK and launch **Kore Voice Mode**.

The app opens `SampleVoiceActivity` with the WebRTC call UI (call, mute, speaker, status).

## Configure credentials

Pass credentials via Intent extras (see `voicemodesdk/README.md`) or subclass `SampleVoiceActivity` and override `getWebRTCConfig()`.

Default test credentials are defined in `SampleVoiceActivity` in the source module.

## Library usage

Add `voicemodesdk` to your own app:

```gradle
// settings.gradle
include ':voicemodesdk'

// app/build.gradle
dependencies {
    implementation project(':voicemodesdk')
}
// To launch the sample voice call UI:
Intent intent = new Intent(getApplicationContext(), SampleVoiceActivity.class);
intent.putExtra(SampleVoiceActivity.EXTRA_BOT_ID, "BOT_ID");
intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_ID, "CLIENT_ID");
intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_SECRET, "CLIENT_SECRET");
intent.putExtra(SampleVoiceActivity.EXTRA_IDENTITY, "user1@user.com");
intent.putExtra(SampleVoiceActivity.EXTRA_WEBSOCKET_URL, "WEB_SOCKET_URL");// wss://aaaa-aaa1.aaa.aa:<port number>/
intent.putExtra(SampleVoiceActivity.EXTRA_JWT_SERVICE_URL, "JWT_SERVICE_URL");
intent.putExtra(SampleVoiceActivity.EXTRA_SERVER_URL, "SERVER_URL");
intent.putExtra(SampleVoiceActivity.EXTRA_SIP_DOMAIN, "SIP_DOMAIN"); //xxxxxxxx-prod-xxxx.xxxx.xx
startActivity(intent);
```

Full API and integration details: [voicemodesdk/README.md](voicemodesdk/README.md).

## Publish `voicemodesdk` to Maven Central

Coordinates (defaults in `gradle.properties`):

- **groupId:** `ai.kore.voicemode`
- **artifactId:** `voicemodesdk`
- **version:** `1.0.0`

1. Register at [Sonatype Central](https://central.sonatype.com) and claim the `ai.kore.voicemode` group (or change `VOICE_MODE_SDK_GROUP`).
2. Copy [gradle/publish.properties.example](gradle/publish.properties.example) into `~/.gradle/gradle.properties` with your Sonatype username/token and GPG signing keys.
3. Publish:

```bash
./gradlew :voicemodesdk:publishToMavenCentral
```

Test locally first:

```bash
./gradlew :voicemodesdk:publishVoiceModeSdkToMavenLocal
```

Consumers add:

```gradle
implementation 'ai.kore.voicemode:voicemodesdk:1.0.0'
```

## Source

Copied from `BotSDK/android-kore-sdk/BotsSDK/korewebrtc` (BotSDK repo).
