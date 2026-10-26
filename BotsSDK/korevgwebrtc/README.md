# KoreVG WebRTC module

This module contains the AudioCodes Java sources from `WebRtcSdk:0.0.2` and the WebRTC Java bindings from `io.github.webrtc-sdk:android:125.6422.07`, arranged under their package paths in `src/main/java`. The patched `ACWebRTCCall` source is in the same tree and adds the local audio track before local video, producing outgoing offers with audio before video.

The module's manifest, resources, assets, and native libraries are included under `src/main`. `libjingle_peerconnection_so.so` and the WebRTC Java bindings are kept from the same upstream build to preserve their JNI compatibility. The native library has 16 KB ELF `LOAD` alignment. The module compiles Java sources locally and does not declare the original `WebRtcSdk` AAR as a Gradle dependency.

The sample app uses compressed native libraries because this project currently builds with Android Gradle Plugin 8.1.4. Apps consuming this AAR should use Android Gradle Plugin 8.5.1 or newer, or configure `packagingOptions.jniLibs.useLegacyPackaging = true` in the consuming application module to ensure compatible APK/AAB packaging. Compressed libraries are extracted during installation and use more installed storage; upgrading AGP avoids that tradeoff.

Build the replacement AAR with:

```sh
./gradlew :korevgwebrtc:assembleRelease
```

The `korebot` module consumes this project as a local Gradle module:

```sh
./gradlew :korebot:compileReleaseJavaWithJavac
```
