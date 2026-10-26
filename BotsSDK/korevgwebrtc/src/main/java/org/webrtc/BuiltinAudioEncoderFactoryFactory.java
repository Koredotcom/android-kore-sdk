/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.AudioEncoderFactoryFactory;

public class BuiltinAudioEncoderFactoryFactory
implements AudioEncoderFactoryFactory {
    @Override
    public long createNativeAudioEncoderFactory() {
        return BuiltinAudioEncoderFactoryFactory.nativeCreateBuiltinAudioEncoderFactory();
    }

    private static native long nativeCreateBuiltinAudioEncoderFactory();
}

