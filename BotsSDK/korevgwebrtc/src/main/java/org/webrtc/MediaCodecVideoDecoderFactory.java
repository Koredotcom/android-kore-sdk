/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodecInfo
 *  android.media.MediaCodecInfo$CodecCapabilities
 *  android.media.MediaCodecList
 *  android.os.Build$VERSION
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import org.webrtc.AndroidVideoDecoder;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.MediaCodecUtils;
import org.webrtc.MediaCodecWrapperFactoryImpl;
import org.webrtc.Predicate;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoCodecMimeType;
import org.webrtc.VideoDecoder;
import org.webrtc.VideoDecoderFactory;

class MediaCodecVideoDecoderFactory
implements VideoDecoderFactory {
    private static final String TAG = "MediaCodecVideoDecoderFactory";
    @Nullable
    private final EglBase.Context sharedContext;
    @Nullable
    private final Predicate<MediaCodecInfo> codecAllowedPredicate;

    public MediaCodecVideoDecoderFactory(@Nullable EglBase.Context sharedContext, @Nullable Predicate<MediaCodecInfo> codecAllowedPredicate) {
        this.sharedContext = sharedContext;
        this.codecAllowedPredicate = codecAllowedPredicate;
    }

    @Override
    @Nullable
    public VideoDecoder createDecoder(VideoCodecInfo codecType) {
        VideoCodecMimeType type = VideoCodecMimeType.valueOf(codecType.getName());
        MediaCodecInfo info = this.findCodecForType(type);
        if (info == null) {
            return null;
        }
        MediaCodecInfo.CodecCapabilities capabilities = info.getCapabilitiesForType(type.mimeType());
        return new AndroidVideoDecoder(new MediaCodecWrapperFactoryImpl(), info.getName(), type, MediaCodecUtils.selectColorFormat(MediaCodecUtils.DECODER_COLOR_FORMATS, capabilities), this.sharedContext);
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
                Logging.e(TAG, "Cannot retrieve decoder codec info", e);
            }
            if (info == null || info.isEncoder() || !this.isSupportedCodec(info, type)) continue;
            return info;
        }
        return null;
    }

    private boolean isSupportedCodec(MediaCodecInfo info, VideoCodecMimeType type) {
        if (!MediaCodecUtils.codecSupportsType(info, type)) {
            return false;
        }
        if (MediaCodecUtils.selectColorFormat(MediaCodecUtils.DECODER_COLOR_FORMATS, info.getCapabilitiesForType(type.mimeType())) == null) {
            return false;
        }
        return this.isCodecAllowed(info);
    }

    private boolean isCodecAllowed(MediaCodecInfo info) {
        if (this.codecAllowedPredicate == null) {
            return true;
        }
        return this.codecAllowedPredicate.test(info);
    }

    private boolean isH264HighProfileSupported(MediaCodecInfo info) {
        String name = info.getName();
        if (name.startsWith("OMX.qcom.")) {
            return true;
        }
        return Build.VERSION.SDK_INT >= 23 && name.startsWith("OMX.Exynos.");
    }
}

