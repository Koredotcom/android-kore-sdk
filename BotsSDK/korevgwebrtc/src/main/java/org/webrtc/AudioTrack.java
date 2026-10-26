/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.MediaStreamTrack;

public class AudioTrack
extends MediaStreamTrack {
    public AudioTrack(long nativeTrack) {
        super(nativeTrack);
    }

    public void setVolume(double volume) {
        AudioTrack.nativeSetVolume(this.getNativeAudioTrack(), volume);
    }

    long getNativeAudioTrack() {
        return this.getNativeMediaStreamTrack();
    }

    private static native void nativeSetVolume(long var0, double var2);
}

