/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc.audio;

public interface AudioDeviceModule {
    public long getNativeAudioDeviceModulePointer();

    public void release();

    public void setSpeakerMute(boolean var1);

    public void setMicrophoneMute(boolean var1);

    default public boolean setNoiseSuppressorEnabled(boolean enabled) {
        return false;
    }

    default public boolean setPreferredMicrophoneFieldDimension(float dimension) {
        return false;
    }
}

