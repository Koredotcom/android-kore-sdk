/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.IdentityHashMap;
import org.webrtc.AudioTrackSink;
import org.webrtc.MediaStreamTrack;

public class AudioTrack
extends MediaStreamTrack {
    private final IdentityHashMap<AudioTrackSink, Long> sinks = new IdentityHashMap();

    public AudioTrack(long nativeTrack) {
        super(nativeTrack);
    }

    public void setVolume(double volume) {
        AudioTrack.nativeSetVolume(this.getNativeAudioTrack(), volume);
    }

    public void addSink(AudioTrackSink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("The AudioTrackSink is not allowed to be null");
        }
        if (!this.sinks.containsKey(sink)) {
            long nativeSink = AudioTrack.nativeWrapSink(sink);
            this.sinks.put(sink, nativeSink);
            AudioTrack.nativeAddSink(this.getNativeMediaStreamTrack(), nativeSink);
        }
    }

    public void removeSink(AudioTrackSink sink) {
        Long nativeSink = this.sinks.remove(sink);
        if (nativeSink != null) {
            AudioTrack.nativeRemoveSink(this.getNativeMediaStreamTrack(), nativeSink);
            AudioTrack.nativeFreeSink(nativeSink);
        }
    }

    @Override
    public void dispose() {
        for (long nativeSink : this.sinks.values()) {
            AudioTrack.nativeRemoveSink(this.getNativeMediaStreamTrack(), nativeSink);
            AudioTrack.nativeFreeSink(nativeSink);
        }
        this.sinks.clear();
        super.dispose();
    }

    long getNativeAudioTrack() {
        return this.getNativeMediaStreamTrack();
    }

    private static native void nativeSetVolume(long var0, double var2);

    private static native void nativeAddSink(long var0, long var2);

    private static native void nativeRemoveSink(long var0, long var2);

    private static native long nativeWrapSink(AudioTrackSink var0);

    private static native void nativeFreeSink(long var0);
}

