/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.AudioTrack;
import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;
import org.webrtc.VideoTrack;

public class MediaStreamTrack {
    public static final String AUDIO_TRACK_KIND = "audio";
    public static final String VIDEO_TRACK_KIND = "video";
    private long nativeTrack;

    @Nullable
    static MediaStreamTrack createMediaStreamTrack(long nativeTrack) {
        if (nativeTrack == 0L) {
            return null;
        }
        String trackKind = MediaStreamTrack.nativeGetKind(nativeTrack);
        if (trackKind.equals(AUDIO_TRACK_KIND)) {
            return new AudioTrack(nativeTrack);
        }
        if (trackKind.equals(VIDEO_TRACK_KIND)) {
            return new VideoTrack(nativeTrack);
        }
        return null;
    }

    public MediaStreamTrack(long nativeTrack) {
        if (nativeTrack == 0L) {
            throw new IllegalArgumentException("nativeTrack may not be null");
        }
        this.nativeTrack = nativeTrack;
    }

    public String id() {
        this.checkMediaStreamTrackExists();
        return MediaStreamTrack.nativeGetId(this.nativeTrack);
    }

    public String kind() {
        this.checkMediaStreamTrackExists();
        return MediaStreamTrack.nativeGetKind(this.nativeTrack);
    }

    public boolean enabled() {
        this.checkMediaStreamTrackExists();
        return MediaStreamTrack.nativeGetEnabled(this.nativeTrack);
    }

    public boolean setEnabled(boolean enable) {
        this.checkMediaStreamTrackExists();
        return MediaStreamTrack.nativeSetEnabled(this.nativeTrack, enable);
    }

    public State state() {
        this.checkMediaStreamTrackExists();
        return MediaStreamTrack.nativeGetState(this.nativeTrack);
    }

    public void dispose() {
        this.checkMediaStreamTrackExists();
        JniCommon.nativeReleaseRef(this.nativeTrack);
        this.nativeTrack = 0L;
    }

    long getNativeMediaStreamTrack() {
        this.checkMediaStreamTrackExists();
        return this.nativeTrack;
    }

    private void checkMediaStreamTrackExists() {
        if (this.nativeTrack == 0L) {
            throw new IllegalStateException("MediaStreamTrack has been disposed.");
        }
    }

    public boolean isDisposed() {
        return this.nativeTrack == 0L;
    }

    private static native String nativeGetId(long var0);

    private static native String nativeGetKind(long var0);

    private static native boolean nativeGetEnabled(long var0);

    private static native boolean nativeSetEnabled(long var0, boolean var2);

    private static native State nativeGetState(long var0);

    public static enum State {
        LIVE,
        ENDED;


        @CalledByNative(value="State")
        static State fromNativeIndex(int nativeIndex) {
            return State.values()[nativeIndex];
        }
    }

    public static enum MediaType {
        MEDIA_TYPE_AUDIO(0),
        MEDIA_TYPE_VIDEO(1);

        private final int nativeIndex;

        private MediaType(int nativeIndex) {
            this.nativeIndex = nativeIndex;
        }

        @CalledByNative(value="MediaType")
        int getNative() {
            return this.nativeIndex;
        }

        @CalledByNative(value="MediaType")
        static MediaType fromNativeIndex(int nativeIndex) {
            for (MediaType type : MediaType.values()) {
                if (type.getNative() != nativeIndex) continue;
                return type;
            }
            throw new IllegalArgumentException("Unknown native media type: " + nativeIndex);
        }
    }
}

