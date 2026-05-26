# Kore WebRTC SDK for Android

A native Android Java SDK for WebRTC-based voice calls with SIP signaling. This module is a conversion of the React Native WebRTC SDK (`ksavg-rn-web-rtc-sdk`).

## Features

- SIP-based signaling over WebSocket
- WebRTC audio calls
- JWT authentication with Kore platform
- Audio session management (speaker, mute, volume)
- Full call control (make, hangup, mute/unmute)
- Bluetooth and wired headset support

## Installation

### From Maven Central

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'ai.kore.voicemode:voicemodesdk:1.0.0'
}
```

### As a project module

Add the module to your `settings.gradle`:

```gradle
include ':voicemodesdk'
```

Add the dependency in your app's `build.gradle`:

```gradle
dependencies {
    implementation project(':voicemodesdk')
}
```

## Publishing (maintainers)

Maven coordinates are set in the root `gradle.properties` (`VOICE_MODE_SDK_GROUP`, `VOICE_MODE_SDK_VERSION`).

1. Add Sonatype and GPG credentials to `~/.gradle/gradle.properties` (see `gradle/publish.properties.example`).
2. Run `./gradlew :voicemodesdk:publishToMavenCentral` from the project root.
3. Complete release on [Sonatype Central](https://central.sonatype.com) (close and release the staging repository).

Local test: `./gradlew :voicemodesdk:publishVoiceModeSdkToMavenLocal`

## Permissions

Add the following permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

## Usage

### Basic Usage with Credentials

```java
import kore.botssdk.voicemode.WebRTCClient;
import kore.botssdk.voicemode.WebRTCConfig;
import kore.botssdk.voicemode.WebRTCListenerAdapter;

// Create configuration
WebRTCConfig config = new WebRTCConfig.Builder()
    .botId("st-xxxxx-xxxxx-xxxxx-xxxxx")
    .clientId("cs-xxxxx-xxxxx-xxxxx-xxxxx")
    .clientSecret("your-client-secret")
    .identity("user@example.com")
    .webSocketUrl("wss://savg-sbc1.kore.ai:8443/")
    .jwtServiceUrl("https://your-jwt-service/token")
    .serverUrl("https://platform.kore.ai")
    .sipDomain("unifiedxo-prod-savg.kore.ai")
    .debug(true)
    .autoRegister(true)
    .autoCall(true)
    .build();

// Create client
WebRTCClient client = new WebRTCClient(context);

// Set listener
client.setListener(new WebRTCListenerAdapter() {
    @Override
    public void onConnected() {
        Log.d("WebRTC", "Connected to server");
    }

    @Override
    public void onRegistered() {
        Log.d("WebRTC", "Registered with SIP server");
    }

    @Override
    public void onCallAccepted() {
        Log.d("WebRTC", "Call accepted");
    }

    @Override
    public void onCallConfirmed() {
        Log.d("WebRTC", "Call media established");
    }

    @Override
    public void onCallEnded(String cause) {
        Log.d("WebRTC", "Call ended: " + cause);
    }

    @Override
    public void onCallFailed(String cause) {
        Log.e("WebRTC", "Call failed: " + cause);
    }

    @Override
    public void onError(String error) {
        Log.e("WebRTC", "Error: " + error);
    }
});

// Initialize (this will auto-connect, register, and call)
client.initWithCredentials(config);
```

### Manual Call Control

```java
// Make a call manually (if autoCall is false)
CallSession session = client.call();

// Or call a specific target
CallSession session = client.call("sip:target@domain.com");

// Hangup
client.hangup();

// Mute/Unmute
client.mute();
client.unmute();
boolean isMuted = client.toggleMute();

// Speaker control
client.setSpeakerEnabled(true);
boolean speakerOn = client.toggleSpeaker();

// Disconnect when done
client.disconnect();
```

### Permission Handling

```java
import kore.botssdk.voicemode.utils.PermissionHelper;

// Check permissions
if (!PermissionHelper.hasAudioPermissions(context)) {
    PermissionHelper.requestAudioPermissions(activity);
}

// Handle permission result in Activity
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == PermissionHelper.PERMISSION_REQUEST_CODE) {
        if (PermissionHelper.allPermissionsGranted(grantResults)) {
            // Permissions granted, initialize WebRTC
            client.initWithCredentials(config);
        } else {
            // Handle permission denied
        }
    }
}
```

### Audio Session Management

```java
import kore.botssdk.voicemode.audio.AudioSessionManager;

AudioSessionManager audioManager = client.getAudioSessionManager();

// Volume control
audioManager.setVolume(10);
int currentVolume = audioManager.getVolume();
int maxVolume = audioManager.getMaxVolume();

// Check audio devices
boolean hasHeadset = audioManager.isWiredHeadsetConnected();
boolean hasBluetooth = audioManager.isBluetoothConnected();

// Bluetooth
audioManager.startBluetoothSco();
audioManager.stopBluetoothSco();
```

## API Reference

### WebRTCClient

| Method | Description |
|--------|-------------|
| `setListener(WebRTCListener)` | Set callback listener |
| `initWithCredentials(WebRTCConfig)` | Initialize with full configuration |
| `init(webSocketUrl, sipUri, autoRegister)` | Initialize with SIP URI directly |
| `register()` | Register with SIP server |
| `unregister()` | Unregister from SIP server |
| `call()` | Make call to configured target |
| `call(target)` | Make call to specific target |
| `hangup()` | Hangup active call |
| `mute()` / `unmute()` | Mute/unmute audio |
| `toggleMute()` | Toggle mute state |
| `toggleSpeaker()` | Toggle speaker |
| `setSpeakerEnabled(enabled)` | Set speaker state |
| `disconnect()` | Disconnect and cleanup |

### WebRTCListener

| Callback | Description |
|----------|-------------|
| `onConnected()` | WebSocket connected |
| `onDisconnected()` | WebSocket disconnected |
| `onRegistered()` | SIP registration successful |
| `onRegistrationFailed(error)` | SIP registration failed |
| `onCallProgress()` | Call is ringing |
| `onCallAccepted()` | Call was answered |
| `onCallConfirmed()` | Media established |
| `onCallEnded(cause)` | Call ended |
| `onCallFailed(cause)` | Call failed |
| `onIncomingCall(session)` | Incoming call received |
| `onError(error)` | Error occurred |

## Sample Voice Activity

The SDK includes a ready-to-use `SampleVoiceActivity` that provides a complete voice call UI with:
- Dark theme matching the React Native ExpSample app
- Call/Hangup button
- Mute/Unmute control
- Speaker toggle
- Connection status display

### Launching SampleVoiceActivity

#### Option 1: Launch directly with default credentials

```java
Intent intent = new Intent(context, SampleVoiceActivity.class);
startActivity(intent);
```

#### Option 2: Pass custom credentials via Intent extras

```java
Intent intent = new Intent(context, SampleVoiceActivity.class);
intent.putExtra(SampleVoiceActivity.EXTRA_BOT_ID, "your-bot-id");
intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_ID, "your-client-id");
intent.putExtra(SampleVoiceActivity.EXTRA_CLIENT_SECRET, "your-client-secret");
intent.putExtra(SampleVoiceActivity.EXTRA_IDENTITY, "user@example.com");
intent.putExtra(SampleVoiceActivity.EXTRA_WEBSOCKET_URL, "wss://your-sip-server:8443/");
intent.putExtra(SampleVoiceActivity.EXTRA_JWT_SERVICE_URL, "https://your-jwt-service/token");
intent.putExtra(SampleVoiceActivity.EXTRA_SERVER_URL, "https://your-platform-server");
intent.putExtra(SampleVoiceActivity.EXTRA_SIP_DOMAIN, "your-sip-domain.com");
startActivity(intent);
```

#### Option 3: Subclass and override getWebRTCConfig()

```java
public class MyVoiceActivity extends SampleVoiceActivity {
    
    @Override
    protected WebRTCConfig getWebRTCConfig() {
        return new WebRTCConfig.Builder()
                .botId("your-bot-id")
                .clientId("your-client-id")
                .clientSecret("your-client-secret")
                .identity("user@example.com")
                .webSocketUrl("wss://your-sip-server:8443/")
                .jwtServiceUrl("https://your-jwt-service/token")
                .serverUrl("https://your-platform-server")
                .sipDomain("your-sip-domain.com")
                .debug(true)
                .autoCall(true)
                .build();
    }
}
```

### UI Components

The SampleVoiceActivity includes:

| Component | Description |
|-----------|-------------|
| Header | "WebRTC Call" title |
| Status Indicator | Green dot when in call, gray when idle |
| Status Text | Shows current state (Ready, Calling, Connected, etc.) |
| Avatar | Bot icon with active/inactive border |
| Mute Button | Toggle microphone mute |
| Speaker Button | Toggle speaker/earpiece |
| Call Button | Start call (green) / Hang up (red) / Connecting (orange) |

## Dependencies

- WebRTC: `io.getstream:stream-webrtc-android:1.1.1`
- OkHttp: `com.squareup.okhttp3:okhttp:4.12.0`
- Gson: `com.google.code.gson:gson:2.10.1`
- ConstraintLayout: `androidx.constraintlayout:constraintlayout:2.1.4`

## License

Copyright (c) Kore Inc. All rights reserved.
