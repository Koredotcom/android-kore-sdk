/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.List;
import java.util.Map;
import org.webrtc.CalledByNative;
import org.webrtc.MediaStreamTrack;

public class RtpCapabilities {
    public List<CodecCapability> codecs;
    public List<HeaderExtensionCapability> headerExtensions;

    @CalledByNative
    RtpCapabilities(List<CodecCapability> codecs, List<HeaderExtensionCapability> headerExtensions) {
        this.headerExtensions = headerExtensions;
        this.codecs = codecs;
    }

    @CalledByNative
    public List<HeaderExtensionCapability> getHeaderExtensions() {
        return this.headerExtensions;
    }

    @CalledByNative
    List<CodecCapability> getCodecs() {
        return this.codecs;
    }

    public static class HeaderExtensionCapability {
        private final String uri;
        private final int preferredId;
        private final boolean preferredEncrypted;

        @CalledByNative(value="HeaderExtensionCapability")
        HeaderExtensionCapability(String uri, int preferredId, boolean preferredEncrypted) {
            this.uri = uri;
            this.preferredId = preferredId;
            this.preferredEncrypted = preferredEncrypted;
        }

        @CalledByNative(value="HeaderExtensionCapability")
        public String getUri() {
            return this.uri;
        }

        @CalledByNative(value="HeaderExtensionCapability")
        public int getPreferredId() {
            return this.preferredId;
        }

        @CalledByNative(value="HeaderExtensionCapability")
        public boolean getPreferredEncrypted() {
            return this.preferredEncrypted;
        }
    }

    public static class CodecCapability {
        public int preferredPayloadType;
        public String name;
        public MediaStreamTrack.MediaType kind;
        public Integer clockRate;
        public Integer numChannels;
        public Map<String, String> parameters;
        public String mimeType;

        public CodecCapability() {
        }

        @CalledByNative(value="CodecCapability")
        CodecCapability(int preferredPayloadType, String name, MediaStreamTrack.MediaType kind, Integer clockRate, Integer numChannels, String mimeType, Map<String, String> parameters) {
            this.preferredPayloadType = preferredPayloadType;
            this.name = name;
            this.kind = kind;
            this.clockRate = clockRate;
            this.numChannels = numChannels;
            this.parameters = parameters;
            this.mimeType = mimeType;
        }

        @CalledByNative(value="CodecCapability")
        int getPreferredPayloadType() {
            return this.preferredPayloadType;
        }

        @CalledByNative(value="CodecCapability")
        String getName() {
            return this.name;
        }

        @CalledByNative(value="CodecCapability")
        MediaStreamTrack.MediaType getKind() {
            return this.kind;
        }

        @CalledByNative(value="CodecCapability")
        Integer getClockRate() {
            return this.clockRate;
        }

        @CalledByNative(value="CodecCapability")
        Integer getNumChannels() {
            return this.numChannels;
        }

        @CalledByNative(value="CodecCapability")
        Map getParameters() {
            return this.parameters;
        }
    }
}

