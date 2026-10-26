/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodecInfo
 *  android.media.MediaCodecList
 *  android.os.Build
 *  android.os.Build$VERSION
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.webrtc.BaseBitrateAdjuster;
import org.webrtc.BitrateAdjuster;
import org.webrtc.DynamicBitrateAdjuster;
import org.webrtc.EglBase;
import org.webrtc.EglBase14;
import org.webrtc.FramerateBitrateAdjuster;
import org.webrtc.H264Utils;
import org.webrtc.HardwareVideoEncoder;
import org.webrtc.Logging;
import org.webrtc.MediaCodecUtils;
import org.webrtc.MediaCodecWrapperFactoryImpl;
import org.webrtc.Predicate;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoCodecMimeType;
import org.webrtc.VideoEncoder;
import org.webrtc.VideoEncoderFactory;

public class HardwareVideoEncoderFactory
implements VideoEncoderFactory {
    private static final String TAG = "HardwareVideoEncoderFactory";
    private static final int PERIODIC_KEY_FRAME_INTERVAL_S = 3600;
    private static final int QCOM_VP8_KEY_FRAME_INTERVAL_ANDROID_L_MS = 15000;
    private static final int QCOM_VP8_KEY_FRAME_INTERVAL_ANDROID_M_MS = 20000;
    private static final int QCOM_VP8_KEY_FRAME_INTERVAL_ANDROID_N_MS = 15000;
    private static final List<String> H264_HW_EXCEPTION_MODELS = Arrays.asList("SAMSUNG-SGH-I337", "Nexus 7", "Nexus 4");
    @Nullable
    private final EglBase14.Context sharedContext;
    private final boolean enableIntelVp8Encoder;
    private final boolean enableH264HighProfile;
    @Nullable
    private final Predicate<MediaCodecInfo> codecAllowedPredicate;

    public HardwareVideoEncoderFactory(EglBase.Context sharedContext, boolean enableIntelVp8Encoder, boolean enableH264HighProfile) {
        this(sharedContext, enableIntelVp8Encoder, enableH264HighProfile, null);
    }

    public HardwareVideoEncoderFactory(EglBase.Context sharedContext, boolean enableIntelVp8Encoder, boolean enableH264HighProfile, @Nullable Predicate<MediaCodecInfo> codecAllowedPredicate) {
        if (sharedContext instanceof EglBase14.Context) {
            this.sharedContext = (EglBase14.Context)sharedContext;
        } else {
            Logging.w(TAG, "No shared EglBase.Context.  Encoders will not use texture mode.");
            this.sharedContext = null;
        }
        this.enableIntelVp8Encoder = enableIntelVp8Encoder;
        this.enableH264HighProfile = enableH264HighProfile;
        this.codecAllowedPredicate = codecAllowedPredicate;
    }

    @Deprecated
    public HardwareVideoEncoderFactory(boolean enableIntelVp8Encoder, boolean enableH264HighProfile) {
        this(null, enableIntelVp8Encoder, enableH264HighProfile);
    }

    @Override
    @Nullable
    public VideoEncoder createEncoder(VideoCodecInfo input) {
        VideoCodecMimeType type = VideoCodecMimeType.valueOf(input.getName());
        MediaCodecInfo info = this.findCodecForType(type);
        if (info == null) {
            return null;
        }
        String codecName = info.getName();
        String mime = type.mimeType();
        Integer surfaceColorFormat = MediaCodecUtils.selectColorFormat(MediaCodecUtils.TEXTURE_COLOR_FORMATS, info.getCapabilitiesForType(mime));
        Integer yuvColorFormat = MediaCodecUtils.selectColorFormat(MediaCodecUtils.ENCODER_COLOR_FORMATS, info.getCapabilitiesForType(mime));
        if (type == VideoCodecMimeType.H264) {
            boolean isHighProfile = H264Utils.isSameH264Profile(input.params, MediaCodecUtils.getCodecProperties(type, true));
            boolean isBaselineProfile = H264Utils.isSameH264Profile(input.params, MediaCodecUtils.getCodecProperties(type, false));
            if (!isHighProfile && !isBaselineProfile) {
                return null;
            }
            if (isHighProfile && !this.isH264HighProfileSupported(info)) {
                return null;
            }
        }
        return new HardwareVideoEncoder(new MediaCodecWrapperFactoryImpl(), codecName, type, surfaceColorFormat, yuvColorFormat, input.params, 3600, this.getForcedKeyFrameIntervalMs(type, codecName), this.createBitrateAdjuster(type, codecName), this.sharedContext);
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        ArrayList<VideoCodecInfo> supportedCodecInfos = new ArrayList<VideoCodecInfo>();
        for (VideoCodecMimeType type : new VideoCodecMimeType[]{VideoCodecMimeType.VP8, VideoCodecMimeType.VP9, VideoCodecMimeType.H264, VideoCodecMimeType.AV1, VideoCodecMimeType.H265}) {
            MediaCodecInfo codec = this.findCodecForType(type);
            if (codec == null) continue;
            String name = type.name();
            if (type == VideoCodecMimeType.H264 && this.isH264HighProfileSupported(codec)) {
                supportedCodecInfos.add(new VideoCodecInfo(name, MediaCodecUtils.getCodecProperties(type, true), new ArrayList<String>()));
            }
            supportedCodecInfos.add(new VideoCodecInfo(name, MediaCodecUtils.getCodecProperties(type, false), new ArrayList<String>()));
        }
        return supportedCodecInfos.toArray(new VideoCodecInfo[supportedCodecInfos.size()]);
    }

    @Nullable
    private MediaCodecInfo findCodecForType(VideoCodecMimeType type) {
        for (int i = 0; i < MediaCodecList.getCodecCount(); ++i) {
            MediaCodecInfo info = null;
            try {
                info = MediaCodecList.getCodecInfoAt((int)i);
            }
            catch (IllegalArgumentException e) {
                Logging.e(TAG, "Cannot retrieve encoder codec info", e);
            }
            if (info == null || !info.isEncoder() || !this.isSupportedCodec(info, type)) continue;
            return info;
        }
        return null;
    }

    private boolean isSupportedCodec(MediaCodecInfo info, VideoCodecMimeType type) {
        if (!MediaCodecUtils.codecSupportsType(info, type)) {
            return false;
        }
        if (MediaCodecUtils.selectColorFormat(MediaCodecUtils.ENCODER_COLOR_FORMATS, info.getCapabilitiesForType(type.mimeType())) == null) {
            return false;
        }
        return this.isHardwareSupportedInCurrentSdk(info, type) && this.isMediaCodecAllowed(info);
    }

    private boolean isHardwareSupportedInCurrentSdk(MediaCodecInfo info, VideoCodecMimeType type) {
        if (Build.VERSION.SDK_INT >= 29) {
            return info.isHardwareAccelerated();
        }
        switch (type) {
            case VP8: {
                return this.isHardwareSupportedInCurrentSdkVp8(info);
            }
            case VP9: {
                return this.isHardwareSupportedInCurrentSdkVp9(info);
            }
            case H264: {
                return this.isHardwareSupportedInCurrentSdkH264(info);
            }
            case H265: 
            case AV1: {
                return false;
            }
        }
        return false;
    }

    private boolean isHardwareSupportedInCurrentSdkVp8(MediaCodecInfo info) {
        String name = info.getName();
        return name.startsWith("OMX.qcom.") || name.startsWith("OMX.Exynos.") && Build.VERSION.SDK_INT >= 23 || name.startsWith("OMX.Intel.") && this.enableIntelVp8Encoder;
    }

    private boolean isHardwareSupportedInCurrentSdkVp9(MediaCodecInfo info) {
        String name = info.getName();
        return (name.startsWith("OMX.qcom.") || name.startsWith("OMX.Exynos.")) && Build.VERSION.SDK_INT >= 24;
    }

    private boolean isHardwareSupportedInCurrentSdkH264(MediaCodecInfo info) {
        if (H264_HW_EXCEPTION_MODELS.contains(Build.MODEL)) {
            return false;
        }
        String name = info.getName();
        return name.startsWith("OMX.qcom.") || name.startsWith("OMX.Exynos.");
    }

    private boolean isMediaCodecAllowed(MediaCodecInfo info) {
        if (this.codecAllowedPredicate == null) {
            return true;
        }
        return this.codecAllowedPredicate.test(info);
    }

    private int getForcedKeyFrameIntervalMs(VideoCodecMimeType type, String codecName) {
        if (type == VideoCodecMimeType.VP8 && codecName.startsWith("OMX.qcom.")) {
            if (Build.VERSION.SDK_INT < 23) {
                return 15000;
            }
            if (Build.VERSION.SDK_INT == 23) {
                return 20000;
            }
            return 15000;
        }
        return 0;
    }

    private BitrateAdjuster createBitrateAdjuster(VideoCodecMimeType type, String codecName) {
        if (codecName.startsWith("OMX.Exynos.")) {
            if (type == VideoCodecMimeType.VP8) {
                return new DynamicBitrateAdjuster();
            }
            return new FramerateBitrateAdjuster();
        }
        return new BaseBitrateAdjuster();
    }

    private boolean isH264HighProfileSupported(MediaCodecInfo info) {
        return this.enableH264HighProfile && Build.VERSION.SDK_INT > 23 && info.getName().startsWith("OMX.Exynos.");
    }
}

