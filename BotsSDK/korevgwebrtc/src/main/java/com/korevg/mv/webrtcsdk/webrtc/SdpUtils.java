/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.webrtc;

public class SdpUtils {
    public static boolean sdpHasVideo(String sdp) {
        if (sdp == null) {
            return true;
        }
        String[] lines = sdp.split("\r\n");
        String mediaDescription = "m=video ";
        for (String line : lines) {
            if (!line.startsWith(mediaDescription)) continue;
            return true;
        }
        return false;
    }

    public static boolean sdpIsHold(String sdp) {
        String[] lines = sdp.split("\r\n");
        String inactive = "a=inactive";
        String sendOnly = "a=sendonly";
        String recvOnly = "a=recvonly";
        boolean videoMedia = false;
        for (String line : lines) {
            if (line.startsWith("m=")) {
                if (line.contains("video")) {
                    videoMedia = true;
                } else if (line.contains("audio")) {
                    videoMedia = false;
                }
            }
            if (videoMedia) continue;
            if (line.startsWith(inactive)) {
                return true;
            }
            if (line.startsWith(sendOnly)) {
                return true;
            }
            if (!line.startsWith(recvOnly)) continue;
            return true;
        }
        return false;
    }
}

