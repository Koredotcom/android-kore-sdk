/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.MediaSource;

public class AudioSource
extends MediaSource {
    public AudioSource(long nativeSource) {
        super(nativeSource);
    }

    long getNativeAudioSource() {
        return this.getNativeMediaSource();
    }
}

