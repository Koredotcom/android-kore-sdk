/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.FrameCryptor;
import org.webrtc.FrameCryptorAlgorithm;
import org.webrtc.FrameCryptorKeyProvider;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpSender;

public class FrameCryptorFactory {
    public static FrameCryptorKeyProvider createFrameCryptorKeyProvider(boolean sharedKey, byte[] ratchetSalt, int ratchetWindowSize, byte[] uncryptedMagicBytes, int failureTolerance, int keyRingSize, boolean discardFrameWhenCryptorNotReady) {
        return FrameCryptorFactory.nativeCreateFrameCryptorKeyProvider(sharedKey, ratchetSalt, ratchetWindowSize, uncryptedMagicBytes, failureTolerance, keyRingSize, discardFrameWhenCryptorNotReady);
    }

    public static FrameCryptor createFrameCryptorForRtpSender(PeerConnectionFactory factory, RtpSender rtpSender, String participantId, FrameCryptorAlgorithm algorithm, FrameCryptorKeyProvider keyProvider) {
        return FrameCryptorFactory.nativeCreateFrameCryptorForRtpSender(factory.getNativeOwnedFactoryAndThreads(), rtpSender.getNativeRtpSender(), participantId, algorithm.ordinal(), keyProvider.getNativeKeyProvider());
    }

    public static FrameCryptor createFrameCryptorForRtpReceiver(PeerConnectionFactory factory, RtpReceiver rtpReceiver, String participantId, FrameCryptorAlgorithm algorithm, FrameCryptorKeyProvider keyProvider) {
        return FrameCryptorFactory.nativeCreateFrameCryptorForRtpReceiver(factory.getNativeOwnedFactoryAndThreads(), rtpReceiver.getNativeRtpReceiver(), participantId, algorithm.ordinal(), keyProvider.getNativeKeyProvider());
    }

    private static native FrameCryptor nativeCreateFrameCryptorForRtpSender(long var0, long var2, String var4, int var5, long var6);

    private static native FrameCryptor nativeCreateFrameCryptorForRtpReceiver(long var0, long var2, String var4, int var5, long var6);

    private static native FrameCryptorKeyProvider nativeCreateFrameCryptorKeyProvider(boolean var0, byte[] var1, int var2, byte[] var3, int var4, int var5, boolean var6);
}

