# AudioCodes WebRTC SDK patch

This Android library repackages `com.github.DocsInternal-Kore:WebRtcSdk:0.0.2` and replaces its `ACWebRTCCall` implementation. The AudioCodes implementation was reconstructed from the supplied AAR because the upstream source was not available.

The patch adds the local audio track to the peer connection before the local video track. With Unified Plan, this makes the SDK create audio before video, so an outgoing offer can use `m=audio` as MID 0 and `m=video` as MID 1. The original manifest, resources, assets, native libraries, and all other SDK classes are extracted from the original AAR at build time.

Build the replacement AAR with:

```sh
./gradlew :audiocodeswebrtc:assembleRelease
```

Build the SDK against this local module with:

```sh
./gradlew :korebot:compileReleaseJavaWithJavac -PuseLocalAudioCodesWebRtc=true
```

The default `korebot` dependency uses the JitPack version tagged `audiocodes-webrtc-0.0.3`.
