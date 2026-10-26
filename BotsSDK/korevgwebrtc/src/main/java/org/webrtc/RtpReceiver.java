/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CalledByNative;
import org.webrtc.FrameDecryptor;
import org.webrtc.JniCommon;
import org.webrtc.MediaStreamTrack;
import org.webrtc.RtpParameters;

public class RtpReceiver {
    private long nativeRtpReceiver;
    private long nativeObserver;
    @Nullable
    private MediaStreamTrack cachedTrack;

    @CalledByNative
    public RtpReceiver(long nativeRtpReceiver) {
        this.nativeRtpReceiver = nativeRtpReceiver;
        long nativeTrack = RtpReceiver.nativeGetTrack(nativeRtpReceiver);
        this.cachedTrack = MediaStreamTrack.createMediaStreamTrack(nativeTrack);
    }

    @Nullable
    public MediaStreamTrack track() {
        return this.cachedTrack;
    }

    public RtpParameters getParameters() {
        this.checkRtpReceiverExists();
        return RtpReceiver.nativeGetParameters(this.nativeRtpReceiver);
    }

    public String id() {
        this.checkRtpReceiverExists();
        return RtpReceiver.nativeGetId(this.nativeRtpReceiver);
    }

    long getNativeRtpReceiver() {
        this.checkRtpReceiverExists();
        return this.nativeRtpReceiver;
    }

    @CalledByNative
    public void dispose() {
        this.checkRtpReceiverExists();
        this.cachedTrack.dispose();
        if (this.nativeObserver != 0L) {
            RtpReceiver.nativeUnsetObserver(this.nativeRtpReceiver, this.nativeObserver);
            this.nativeObserver = 0L;
        }
        JniCommon.nativeReleaseRef(this.nativeRtpReceiver);
        this.nativeRtpReceiver = 0L;
    }

    public void SetObserver(Observer observer) {
        this.checkRtpReceiverExists();
        if (this.nativeObserver != 0L) {
            RtpReceiver.nativeUnsetObserver(this.nativeRtpReceiver, this.nativeObserver);
        }
        this.nativeObserver = RtpReceiver.nativeSetObserver(this.nativeRtpReceiver, observer);
    }

    public void setFrameDecryptor(FrameDecryptor frameDecryptor) {
        this.checkRtpReceiverExists();
        RtpReceiver.nativeSetFrameDecryptor(this.nativeRtpReceiver, frameDecryptor.getNativeFrameDecryptor());
    }

    private void checkRtpReceiverExists() {
        if (this.nativeRtpReceiver == 0L) {
            throw new IllegalStateException("RtpReceiver has been disposed.");
        }
    }

    private static native long nativeGetTrack(long var0);

    private static native RtpParameters nativeGetParameters(long var0);

    private static native String nativeGetId(long var0);

    private static native long nativeSetObserver(long var0, Observer var2);

    private static native void nativeUnsetObserver(long var0, long var2);

    private static native void nativeSetFrameDecryptor(long var0, long var2);

    public static interface Observer {
        @CalledByNative(value="Observer")
        public void onFirstPacketReceived(MediaStreamTrack.MediaType var1);
    }
}

