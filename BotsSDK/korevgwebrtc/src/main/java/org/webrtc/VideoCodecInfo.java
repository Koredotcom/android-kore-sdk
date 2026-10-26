/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.webrtc.CalledByNative;

public class VideoCodecInfo {
    public static final String H264_FMTP_PROFILE_LEVEL_ID = "profile-level-id";
    public static final String H264_FMTP_LEVEL_ASYMMETRY_ALLOWED = "level-asymmetry-allowed";
    public static final String H264_FMTP_PACKETIZATION_MODE = "packetization-mode";
    public static final String H264_PROFILE_CONSTRAINED_BASELINE = "42e0";
    public static final String H264_PROFILE_CONSTRAINED_HIGH = "640c";
    public static final String H264_LEVEL_3_1 = "1f";
    public static final String H264_CONSTRAINED_HIGH_3_1 = "640c1f";
    public static final String H264_CONSTRAINED_BASELINE_3_1 = "42e01f";
    public final String name;
    public final Map<String, String> params;
    public final List<String> scalabilityModes;
    @Deprecated
    public final int payload;

    @CalledByNative
    public VideoCodecInfo(String name, Map<String, String> params, List<String> scalabilityModes) {
        this.payload = 0;
        this.name = name;
        this.params = params;
        this.scalabilityModes = scalabilityModes;
    }

    @Deprecated
    public VideoCodecInfo(int payload, String name, Map<String, String> params) {
        this.payload = payload;
        this.name = name;
        this.params = params;
        this.scalabilityModes = new ArrayList<String>();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VideoCodecInfo)) {
            return false;
        }
        VideoCodecInfo otherInfo = (VideoCodecInfo)obj;
        return this.name.equalsIgnoreCase(otherInfo.name) && this.params.equals(otherInfo.params);
    }

    public int hashCode() {
        Object[] values = new Object[]{this.name.toUpperCase(Locale.ROOT), this.params};
        return Arrays.hashCode(values);
    }

    public String toString() {
        return "VideoCodec{" + this.name + " " + this.params + "}";
    }

    @CalledByNative
    String getName() {
        return this.name;
    }

    @CalledByNative
    Map getParams() {
        return this.params;
    }

    @CalledByNative
    List<String> getScalabilityModes() {
        return this.scalabilityModes;
    }
}

