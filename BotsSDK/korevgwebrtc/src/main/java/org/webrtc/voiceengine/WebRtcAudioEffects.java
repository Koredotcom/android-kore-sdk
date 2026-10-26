/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.audiofx.AcousticEchoCanceler
 *  android.media.audiofx.AudioEffect
 *  android.media.audiofx.AudioEffect$Descriptor
 *  android.media.audiofx.NoiseSuppressor
 *  android.os.Build
 *  android.os.Build$VERSION
 *  androidx.annotation.Nullable
 */
package org.webrtc.voiceengine;

import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import org.webrtc.Logging;
import org.webrtc.voiceengine.WebRtcAudioUtils;

public class WebRtcAudioEffects {
    private static final boolean DEBUG = false;
    private static final String TAG = "WebRtcAudioEffects";
    private static final UUID AOSP_ACOUSTIC_ECHO_CANCELER = UUID.fromString("bb392ec0-8d4d-11e0-a896-0002a5d5c51b");
    private static final UUID AOSP_NOISE_SUPPRESSOR = UUID.fromString("c06c8400-8e06-11e0-9cb6-0002a5d5c51b");
    @Nullable
    private static AudioEffect.Descriptor[] cachedEffects;
    @Nullable
    private AcousticEchoCanceler aec;
    @Nullable
    private NoiseSuppressor ns;
    private boolean shouldEnableAec;
    private boolean shouldEnableNs;

    public static boolean isAcousticEchoCancelerSupported() {
        return WebRtcAudioEffects.isAcousticEchoCancelerEffectAvailable();
    }

    public static boolean isNoiseSuppressorSupported() {
        return WebRtcAudioEffects.isNoiseSuppressorEffectAvailable();
    }

    public static boolean isAcousticEchoCancelerBlacklisted() {
        List<String> blackListedModels = WebRtcAudioUtils.getBlackListedModelsForAecUsage();
        boolean isBlacklisted = blackListedModels.contains(Build.MODEL);
        if (isBlacklisted) {
            Logging.w(TAG, Build.MODEL + " is blacklisted for HW AEC usage!");
        }
        return isBlacklisted;
    }

    public static boolean isNoiseSuppressorBlacklisted() {
        List<String> blackListedModels = WebRtcAudioUtils.getBlackListedModelsForNsUsage();
        boolean isBlacklisted = blackListedModels.contains(Build.MODEL);
        if (isBlacklisted) {
            Logging.w(TAG, Build.MODEL + " is blacklisted for HW NS usage!");
        }
        return isBlacklisted;
    }

    private static boolean isAcousticEchoCancelerExcludedByUUID() {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        for (AudioEffect.Descriptor d : WebRtcAudioEffects.getAvailableEffects()) {
            if (!d.type.equals(AudioEffect.EFFECT_TYPE_AEC) || !d.uuid.equals(AOSP_ACOUSTIC_ECHO_CANCELER)) continue;
            return true;
        }
        return false;
    }

    private static boolean isNoiseSuppressorExcludedByUUID() {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        for (AudioEffect.Descriptor d : WebRtcAudioEffects.getAvailableEffects()) {
            if (!d.type.equals(AudioEffect.EFFECT_TYPE_NS) || !d.uuid.equals(AOSP_NOISE_SUPPRESSOR)) continue;
            return true;
        }
        return false;
    }

    private static boolean isAcousticEchoCancelerEffectAvailable() {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        return WebRtcAudioEffects.isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_AEC);
    }

    private static boolean isNoiseSuppressorEffectAvailable() {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        return WebRtcAudioEffects.isEffectTypeAvailable(AudioEffect.EFFECT_TYPE_NS);
    }

    public static boolean canUseAcousticEchoCanceler() {
        boolean canUseAcousticEchoCanceler = WebRtcAudioEffects.isAcousticEchoCancelerSupported() && !WebRtcAudioUtils.useWebRtcBasedAcousticEchoCanceler() && !WebRtcAudioEffects.isAcousticEchoCancelerBlacklisted() && !WebRtcAudioEffects.isAcousticEchoCancelerExcludedByUUID();
        Logging.d(TAG, "canUseAcousticEchoCanceler: " + canUseAcousticEchoCanceler);
        return canUseAcousticEchoCanceler;
    }

    public static boolean canUseNoiseSuppressor() {
        boolean canUseNoiseSuppressor = WebRtcAudioEffects.isNoiseSuppressorSupported() && !WebRtcAudioUtils.useWebRtcBasedNoiseSuppressor() && !WebRtcAudioEffects.isNoiseSuppressorBlacklisted() && !WebRtcAudioEffects.isNoiseSuppressorExcludedByUUID();
        Logging.d(TAG, "canUseNoiseSuppressor: " + canUseNoiseSuppressor);
        return canUseNoiseSuppressor;
    }

    public static WebRtcAudioEffects create() {
        return new WebRtcAudioEffects();
    }

    private WebRtcAudioEffects() {
        Logging.d(TAG, "ctor" + WebRtcAudioUtils.getThreadInfo());
    }

    public boolean setAEC(boolean enable) {
        Logging.d(TAG, "setAEC(" + enable + ")");
        if (!WebRtcAudioEffects.canUseAcousticEchoCanceler()) {
            Logging.w(TAG, "Platform AEC is not supported");
            this.shouldEnableAec = false;
            return false;
        }
        if (this.aec != null && enable != this.shouldEnableAec) {
            Logging.e(TAG, "Platform AEC state can't be modified while recording");
            return false;
        }
        this.shouldEnableAec = enable;
        return true;
    }

    public boolean setNS(boolean enable) {
        Logging.d(TAG, "setNS(" + enable + ")");
        if (!WebRtcAudioEffects.canUseNoiseSuppressor()) {
            Logging.w(TAG, "Platform NS is not supported");
            this.shouldEnableNs = false;
            return false;
        }
        if (this.ns != null && enable != this.shouldEnableNs) {
            Logging.e(TAG, "Platform NS state can't be modified while recording");
            return false;
        }
        this.shouldEnableNs = enable;
        return true;
    }

    public void enable(int audioSession) {
        boolean enable;
        boolean enabled;
        Logging.d(TAG, "enable(audioSession=" + audioSession + ")");
        WebRtcAudioEffects.assertTrue(this.aec == null);
        WebRtcAudioEffects.assertTrue(this.ns == null);
        if (WebRtcAudioEffects.isAcousticEchoCancelerSupported()) {
            this.aec = AcousticEchoCanceler.create((int)audioSession);
            if (this.aec != null) {
                enabled = this.aec.getEnabled();
                boolean bl = enable = this.shouldEnableAec && WebRtcAudioEffects.canUseAcousticEchoCanceler();
                if (this.aec.setEnabled(enable) != 0) {
                    Logging.e(TAG, "Failed to set the AcousticEchoCanceler state");
                }
                Logging.d(TAG, "AcousticEchoCanceler: was " + (enabled ? "enabled" : "disabled") + ", enable: " + enable + ", is now: " + (this.aec.getEnabled() ? "enabled" : "disabled"));
            } else {
                Logging.e(TAG, "Failed to create the AcousticEchoCanceler instance");
            }
        }
        if (WebRtcAudioEffects.isNoiseSuppressorSupported()) {
            this.ns = NoiseSuppressor.create((int)audioSession);
            if (this.ns != null) {
                enabled = this.ns.getEnabled();
                boolean bl = enable = this.shouldEnableNs && WebRtcAudioEffects.canUseNoiseSuppressor();
                if (this.ns.setEnabled(enable) != 0) {
                    Logging.e(TAG, "Failed to set the NoiseSuppressor state");
                }
                Logging.d(TAG, "NoiseSuppressor: was " + (enabled ? "enabled" : "disabled") + ", enable: " + enable + ", is now: " + (this.ns.getEnabled() ? "enabled" : "disabled"));
            } else {
                Logging.e(TAG, "Failed to create the NoiseSuppressor instance");
            }
        }
    }

    public void release() {
        Logging.d(TAG, "release");
        if (this.aec != null) {
            this.aec.release();
            this.aec = null;
        }
        if (this.ns != null) {
            this.ns.release();
            this.ns = null;
        }
    }

    private boolean effectTypeIsVoIP(UUID type) {
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        return AudioEffect.EFFECT_TYPE_AEC.equals(type) && WebRtcAudioEffects.isAcousticEchoCancelerSupported() || AudioEffect.EFFECT_TYPE_NS.equals(type) && WebRtcAudioEffects.isNoiseSuppressorSupported();
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError((Object)"Expected condition to be true");
        }
    }

    @Nullable
    private static AudioEffect.Descriptor[] getAvailableEffects() {
        if (cachedEffects != null) {
            return cachedEffects;
        }
        cachedEffects = AudioEffect.queryEffects();
        return cachedEffects;
    }

    private static boolean isEffectTypeAvailable(UUID effectType) {
        AudioEffect.Descriptor[] effects = WebRtcAudioEffects.getAvailableEffects();
        if (effects == null) {
            return false;
        }
        for (AudioEffect.Descriptor d : effects) {
            if (!d.type.equals(effectType)) continue;
            return true;
        }
        return false;
    }
}

