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

    public void setShouldReceive(boolean shouldReceive) {
        VideoTrack.nativeSetShouldReceive(this.getNativeMediaStreamTrack(), shouldReceive);
    }

    public boolean shouldReceive() {
        return VideoTrack.nativeGetShouldReceive(this.getNativeMediaStreamTrack());
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

    public long getNativeVideoTrack() {
        return this.getNativeMediaStreamTrack();
    }

    private static native void nativeAddSink(long var0, long var2);

    private static native void nativeRemoveSink(long var0, long var2);

    private static native long nativeWrapSink(VideoSink var0);

    private static native void nativeFreeSink(long var0);

    private static native void nativeSetShouldReceive(long var0, boolean var2);

    private static native boolean nativeGetShouldReceive(long var0);
}

