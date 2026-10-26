/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.AudioDecoderFactoryFactory;

public class BuiltinAudioDecoderFactoryFactory
implements AudioDecoderFactoryFactory {
    @Override
    public long createNativeAudioDecoderFactory() {
        return BuiltinAudioDecoderFactoryFactory.nativeCreateBuiltinAudioDecoderFactory();
    }

    private static native long nativeCreateBuiltinAudioDecoderFactory();
}

