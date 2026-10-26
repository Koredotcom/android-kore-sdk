/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.Arrays;
import org.webrtc.CalledByNative;
import org.webrtc.PeerConnection;

public class IceCandidate {
    public final String sdpMid;
    public final int sdpMLineIndex;
    public final String sdp;
    public final String serverUrl;
    public final PeerConnection.AdapterType adapterType;

    public IceCandidate(String sdpMid, int sdpMLineIndex, String sdp) {
        this.sdpMid = sdpMid;
        this.sdpMLineIndex = sdpMLineIndex;
        this.sdp = sdp;
        this.serverUrl = "";
        this.adapterType = PeerConnection.AdapterType.UNKNOWN;
    }

    @CalledByNative
    IceCandidate(String sdpMid, int sdpMLineIndex, String sdp, String serverUrl, PeerConnection.AdapterType adapterType) {
        this.sdpMid = sdpMid;
        this.sdpMLineIndex = sdpMLineIndex;
        this.sdp = sdp;
        this.serverUrl = serverUrl;
        this.adapterType = adapterType;
    }

    public String toString() {
        return this.sdpMid + ":" + this.sdpMLineIndex + ":" + this.sdp + ":" + this.serverUrl + ":" + this.adapterType.toString();
    }

    @CalledByNative
    String getSdpMid() {
        return this.sdpMid;
    }

    @CalledByNative
    String getSdp() {
        return this.sdp;
    }

    public boolean equals(@Nullable Object object) {
        if (!(object instanceof IceCandidate)) {
            return false;
        }
        IceCandidate that = (IceCandidate)object;
        return IceCandidate.objectEquals(this.sdpMid, that.sdpMid) && this.sdpMLineIndex == that.sdpMLineIndex && IceCandidate.objectEquals(this.sdp, that.sdp);
    }

    public int hashCode() {
        Object[] values = new Object[]{this.sdpMid, this.sdpMLineIndex, this.sdp};
        return Arrays.hashCode(values);
    }

    private static boolean objectEquals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }
}

