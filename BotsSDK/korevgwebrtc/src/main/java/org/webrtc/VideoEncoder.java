/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import org.webrtc.CalledByNative;
import org.webrtc.EncodedImage;
import org.webrtc.VideoCodecStatus;
import org.webrtc.VideoFrame;

public interface VideoEncoder {
    @CalledByNative
    default public long createNative(long webrtcEnvRef) {
        return 0L;
    }

    @CalledByNative
    default public boolean isHardwareEncoder() {
        return true;
    }

    @CalledByNative
    public VideoCodecStatus initEncode(Settings var1, Callback var2);

    @CalledByNative
    public VideoCodecStatus release();

    @CalledByNative
    public VideoCodecStatus encode(VideoFrame var1, EncodeInfo var2);

    public VideoCodecStatus setRateAllocation(BitrateAllocation var1, int var2);

    @CalledByNative
    default public VideoCodecStatus setRates(RateControlParameters rcParameters) {
        int framerateFps = (int)Math.ceil(rcParameters.framerateFps);
        return this.setRateAllocation(rcParameters.bitrate, framerateFps);
    }

    @CalledByNative
    public ScalingSettings getScalingSettings();

    @CalledByNative
    default public ResolutionBitrateLimits[] getResolutionBitrateLimits() {
        ResolutionBitrateLimits[] bitrate_limits = new ResolutionBitrateLimits[]{};
        return bitrate_limits;
    }

    @CalledByNative
    public String getImplementationName();

    @CalledByNative
    default public EncoderInfo getEncoderInfo() {
        return new EncoderInfo(1, false);
    }

    public static class RateControlParameters {
        public final BitrateAllocation bitrate;
        public final double framerateFps;

        @CalledByNative(value="RateControlParameters")
        public RateControlParameters(BitrateAllocation bitrate, double framerateFps) {
            this.bitrate = bitrate;
            this.framerateFps = framerateFps;
        }
    }

    public static class BitrateAllocation {
        public final int[][] bitratesBbs;

        @CalledByNative(value="BitrateAllocation")
        public BitrateAllocation(int[][] bitratesBbs) {
            this.bitratesBbs = bitratesBbs;
        }

        public int getSum() {
            int sum = 0;
            int[][] nArray = this.bitratesBbs;
            int n = nArray.length;
            for (int i = 0; i < n; ++i) {
                int[] spatialLayer;
                for (int bitrate : spatialLayer = nArray[i]) {
                    sum += bitrate;
                }
            }
            return sum;
        }
    }

    public static class ResolutionBitrateLimits {
        public final int frameSizePixels;
        public final int minStartBitrateBps;
        public final int minBitrateBps;
        public final int maxBitrateBps;

        public ResolutionBitrateLimits(int frameSizePixels, int minStartBitrateBps, int minBitrateBps, int maxBitrateBps) {
            this.frameSizePixels = frameSizePixels;
            this.minStartBitrateBps = minStartBitrateBps;
            this.minBitrateBps = minBitrateBps;
            this.maxBitrateBps = maxBitrateBps;
        }

        @CalledByNative(value="ResolutionBitrateLimits")
        public int getFrameSizePixels() {
            return this.frameSizePixels;
        }

        @CalledByNative(value="ResolutionBitrateLimits")
        public int getMinStartBitrateBps() {
            return this.minStartBitrateBps;
        }

        @CalledByNative(value="ResolutionBitrateLimits")
        public int getMinBitrateBps() {
            return this.minBitrateBps;
        }

        @CalledByNative(value="ResolutionBitrateLimits")
        public int getMaxBitrateBps() {
            return this.maxBitrateBps;
        }
    }

    public static class EncoderInfo {
        public final int requestedResolutionAlignment;
        public final boolean applyAlignmentToAllSimulcastLayers;

        public EncoderInfo(int requestedResolutionAlignment, boolean applyAlignmentToAllSimulcastLayers) {
            this.requestedResolutionAlignment = requestedResolutionAlignment;
            this.applyAlignmentToAllSimulcastLayers = applyAlignmentToAllSimulcastLayers;
        }

        @CalledByNative(value="EncoderInfo")
        public int getRequestedResolutionAlignment() {
            return this.requestedResolutionAlignment;
        }

        @CalledByNative(value="EncoderInfo")
        public boolean getApplyAlignmentToAllSimulcastLayers() {
            return this.applyAlignmentToAllSimulcastLayers;
        }
    }

    public static interface Callback {
        public void onEncodedFrame(EncodedImage var1, CodecSpecificInfo var2);
    }

    public static class ScalingSettings {
        public final boolean on;
        @Nullable
        public final Integer low;
        @Nullable
        public final Integer high;
        public static final ScalingSettings OFF = new ScalingSettings();

        public ScalingSettings(int low, int high) {
            this.on = true;
            this.low = low;
            this.high = high;
        }

        private ScalingSettings() {
            this.on = false;
            this.low = null;
            this.high = null;
        }

        @Deprecated
        public ScalingSettings(boolean on) {
            this.on = on;
            this.low = null;
            this.high = null;
        }

        @Deprecated
        public ScalingSettings(boolean on, int low, int high) {
            this.on = on;
            this.low = low;
            this.high = high;
        }

        public String toString() {
            return this.on ? "[ " + this.low + ", " + this.high + " ]" : "OFF";
        }
    }

    public static class CodecSpecificInfoAV1
    extends CodecSpecificInfo {
    }

    public static class CodecSpecificInfoH264
    extends CodecSpecificInfo {
    }

    public static class CodecSpecificInfoVP9
    extends CodecSpecificInfo {
    }

    public static class CodecSpecificInfoVP8
    extends CodecSpecificInfo {
    }

    public static class CodecSpecificInfo {
    }

    public static class EncodeInfo {
        public final EncodedImage.FrameType[] frameTypes;

        @CalledByNative(value="EncodeInfo")
        public EncodeInfo(EncodedImage.FrameType[] frameTypes) {
            this.frameTypes = frameTypes;
        }
    }

    public static class Capabilities {
        public final boolean lossNotification;

        @CalledByNative(value="Capabilities")
        public Capabilities(boolean lossNotification) {
            this.lossNotification = lossNotification;
        }
    }

    public static class Settings {
        public final int numberOfCores;
        public final int width;
        public final int height;
        public final int startBitrate;
        public final int maxFramerate;
        public final int numberOfSimulcastStreams;
        public final boolean automaticResizeOn;
        public final Capabilities capabilities;

        @Deprecated
        public Settings(int numberOfCores, int width, int height, int startBitrate, int maxFramerate, int numberOfSimulcastStreams, boolean automaticResizeOn) {
            this(numberOfCores, width, height, startBitrate, maxFramerate, numberOfSimulcastStreams, automaticResizeOn, new Capabilities(false));
        }

        @CalledByNative(value="Settings")
        public Settings(int numberOfCores, int width, int height, int startBitrate, int maxFramerate, int numberOfSimulcastStreams, boolean automaticResizeOn, Capabilities capabilities) {
            this.numberOfCores = numberOfCores;
            this.width = width;
            this.height = height;
            this.startBitrate = startBitrate;
            this.maxFramerate = maxFramerate;
            this.numberOfSimulcastStreams = numberOfSimulcastStreams;
            this.automaticResizeOn = automaticResizeOn;
            this.capabilities = capabilities;
        }
    }
}

