# KoreVG WebRTC module

This module contains the Java sources decompiled from `WebRtcSdk:0.0.2`, arranged under their original package paths in `src/main/java`. The patched `ACWebRTCCall` source is in the same tree and adds the local audio track before local video, producing outgoing offers with audio before video.

The original AAR's manifest, resources, assets, and native libraries are included under `src/main`. Java classes are compiled from this module; the module does not declare the original `WebRtcSdk` AAR as a Gradle dependency. The vendor package namespace and ABI names remain unchanged because the source and callers rely on them.

Build the replacement AAR with:

```sh
./gradlew :korevgwebrtc:assembleRelease
```

The `korebot` module consumes this project as a local Gradle module:

```sh
./gradlew :korebot:compileReleaseJavaWithJavac
```
