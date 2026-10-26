/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.webrtc.AudioTrack;
import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;
import org.webrtc.Logging;
import org.webrtc.MediaStreamTrack;
import org.webrtc.VideoTrack;

public class MediaStream {
    private static final String TAG = "MediaStream";
    public final List<AudioTrack> audioTracks = new ArrayList<AudioTrack>();
    public final List<VideoTrack> videoTracks = new ArrayList<VideoTrack>();
    public final List<VideoTrack> preservedVideoTracks = new ArrayList<VideoTrack>();
    private long nativeStream;

    @CalledByNative
    public MediaStream(long nativeStream) {
        this.nativeStream = nativeStream;
    }

    public boolean addTrack(AudioTrack track) {
        this.checkMediaStreamExists();
        if (MediaStream.nativeAddAudioTrackToNativeStream(this.nativeStream, track.getNativeAudioTrack())) {
            this.audioTracks.add(track);
            return true;
        }
        return false;
    }

    public boolean addTrack(VideoTrack track) {
        this.checkMediaStreamExists();
        if (MediaStream.nativeAddVideoTrackToNativeStream(this.nativeStream, track.getNativeVideoTrack())) {
            this.videoTracks.add(track);
            return true;
        }
        return false;
    }

    public boolean addPreservedTrack(VideoTrack track) {
        this.checkMediaStreamExists();
        if (MediaStream.nativeAddVideoTrackToNativeStream(this.nativeStream, track.getNativeVideoTrack())) {
            this.preservedVideoTracks.add(track);
            return true;
        }
        return false;
    }

    public boolean removeTrack(AudioTrack track) {
        this.checkMediaStreamExists();
        this.audioTracks.remove(track);
        return MediaStream.nativeRemoveAudioTrack(this.nativeStream, track.getNativeAudioTrack());
    }

    public boolean removeTrack(VideoTrack track) {
        this.checkMediaStreamExists();
        this.videoTracks.remove(track);
        this.preservedVideoTracks.remove(track);
        return MediaStream.nativeRemoveVideoTrack(this.nativeStream, track.getNativeVideoTrack());
    }

    @CalledByNative
    public void dispose() {
        MediaStreamTrack track;
        this.checkMediaStreamExists();
        while (!this.audioTracks.isEmpty()) {
            track = this.audioTracks.get(0);
            this.removeTrack((AudioTrack)track);
            ((AudioTrack)track).dispose();
        }
        while (!this.videoTracks.isEmpty()) {
            track = this.videoTracks.get(0);
            this.removeTrack((VideoTrack)track);
            ((VideoTrack)track).dispose();
        }
        while (!this.preservedVideoTracks.isEmpty()) {
            this.removeTrack(this.preservedVideoTracks.get(0));
        }
        JniCommon.nativeReleaseRef(this.nativeStream);
        this.nativeStream = 0L;
    }

    public String getId() {
        this.checkMediaStreamExists();
        return MediaStream.nativeGetId(this.nativeStream);
    }

    public String toString() {
        return "[" + this.getId() + ":A=" + this.audioTracks.size() + ":V=" + this.videoTracks.size() + "]";
    }

    @CalledByNative
    void addNativeAudioTrack(long nativeTrack) {
        this.audioTracks.add(new AudioTrack(nativeTrack));
    }

    @CalledByNative
    void addNativeVideoTrack(long nativeTrack) {
        this.videoTracks.add(new VideoTrack(nativeTrack));
    }

    @CalledByNative
    void removeAudioTrack(long nativeTrack) {
        MediaStream.removeMediaStreamTrack(this.audioTracks, nativeTrack);
    }

    @CalledByNative
    void removeVideoTrack(long nativeTrack) {
        MediaStream.removeMediaStreamTrack(this.videoTracks, nativeTrack);
    }

    long getNativeMediaStream() {
        this.checkMediaStreamExists();
        return this.nativeStream;
    }

    private void checkMediaStreamExists() {
        if (this.nativeStream == 0L) {
            throw new IllegalStateException("MediaStream has been disposed.");
        }
    }

    private static void removeMediaStreamTrack(List<? extends MediaStreamTrack> tracks, long nativeTrack) {
        Iterator<? extends MediaStreamTrack> it = tracks.iterator();
        while (it.hasNext()) {
            MediaStreamTrack track = it.next();
            if (track.getNativeMediaStreamTrack() != nativeTrack) continue;
            track.dispose();
            it.remove();
            return;
        }
        Logging.e(TAG, "Couldn't not find track");
    }

    private static native boolean nativeAddAudioTrackToNativeStream(long var0, long var2);

    private static native boolean nativeAddVideoTrackToNativeStream(long var0, long var2);

    private static native boolean nativeRemoveAudioTrack(long var0, long var2);

    private static native boolean nativeRemoveVideoTrack(long var0, long var2);

    private static native String nativeGetId(long var0);
}

