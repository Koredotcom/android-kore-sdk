/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.IdentityHashMap;
import org.webrtc.MediaStreamTrack;
import org.webrtc.VideoSink;

public class VideoTrack
extends MediaStreamTrack {
    private final IdentityHashMap<VideoSink, Long> sinks = new IdentityHashMap();

    public VideoTrack(long nativeTrack) {
        super(nativeTrack);
    }

    public void addSink(VideoSink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("The VideoSink is not allowed to be null");
        }
        if (!this.sinks.containsKey(sink)) {
            long nativeSink = VideoTrack.nativeWrapSink(sink);
            this.sinks.put(sink, nativeSink);
            VideoTrack.nativeAddSink(this.getNativeMediaStreamTrack(), nativeSink);
        }
    }

    public void removeSink(VideoSink sink) {
        Long nativeSink = this.sinks.remove(sink);
        if (nativeSink != null) {
            VideoTrack.nativeRemoveSink(this.getNativeMediaStreamTrack(), nativeSink);
            VideoTrack.nativeFreeSink(nativeSink);
        }
    }

    @Override
    public void dispose() {
        for (long nativeSink : this.sinks.values()) {
            VideoTrack.nativeRemoveSink(this.getNativeMediaStreamTrack(), nativeSink);
            VideoTrack.nativeFreeSink(nativeSink);
        }
        this.sinks.clear();
        super.dispose();
    }

    long getNativeVideoTrack() {
        return this.getNativeMediaStreamTrack();
    }

    private static native void nativeAddSink(long var0, long var2);

    private static native void nativeRemoveSink(long var0, long var2);

    private static native long nativeWrapSink(VideoSink var0);

    private static native void nativeFreeSink(long var0);
}

