/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

enum VideoCodecMimeType {
    VP8("video/x-vnd.on2.vp8"),
    VP9("video/x-vnd.on2.vp9"),
    H264("video/avc"),
    AV1("video/av01");

    private final String mimeType;

    private VideoCodecMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    String mimeType() {
        return this.mimeType;
    }

    static VideoCodecMimeType fromSdpCodecName(String codecName) {
        return codecName.equals("AV1X") ? AV1 : VideoCodecMimeType.valueOf(codecName);
    }

    String toSdpCodecName() {
        return this == AV1 ? "AV1X" : this.name();
    }
}

