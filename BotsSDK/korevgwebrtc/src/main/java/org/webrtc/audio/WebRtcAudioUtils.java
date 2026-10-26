/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.media.AudioDeviceInfo
 *  android.media.AudioManager
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package org.webrtc.audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import java.util.Arrays;
import org.webrtc.Logging;

public final class WebRtcAudioUtils {
    private static final String TAG = "WebRtcAudioUtilsExternal";

    public static String getThreadInfo() {
        Thread current = Thread.currentThread();
        return "@[name=" + current.getName() + ", id=" + current.getId() + "]";
    }

    public static boolean runningOnEmulator() {
        return Build.HARDWARE.equals("goldfish") || Build.HARDWARE.equals("ranchu");
    }

    private static void logDeviceInfo(String tag) {
        Logging.d(tag, "Android SDK: " + Build.VERSION.SDK_INT + ", Release: " + Build.VERSION.RELEASE + ", Brand: " + Build.BRAND + ", Device: " + Build.DEVICE + ", Id: " + Build.ID + ", Hardware: " + Build.HARDWARE + ", Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Product: " + Build.PRODUCT);
    }

    public static void logAudioState(String tag, Context context, AudioManager audioManager) {
        WebRtcAudioUtils.logDeviceInfo(tag);
        WebRtcAudioUtils.logAudioStateBasic(tag, context, audioManager);
        WebRtcAudioUtils.logAudioStateVolume(tag, audioManager);
        WebRtcAudioUtils.logAudioDeviceInfo(tag, audioManager);
    }

    public static String deviceTypeToString(int type) {
        switch (type) {
            case 19: {
                return "TYPE_AUX_LINE";
            }
            case 30: {
                return "TYPE_BLE_BROADCAST";
            }
            case 26: {
                return "TYPE_BLE_HEADSET";
            }
            case 27: {
                return "TYPE_BLE_SPEAKER";
            }
            case 8: {
                return "TYPE_BLUETOOTH_A2DP";
            }
            case 7: {
                return "TYPE_BLUETOOTH_SCO";
            }
            case 1: {
                return "TYPE_BUILTIN_EARPIECE";
            }
            case 15: {
                return "TYPE_BUILTIN_MIC";
            }
            case 2: {
                return "TYPE_BUILTIN_SPEAKER";
            }
            case 24: {
                return "TYPE_BUILTIN_SPEAKER_SAFE";
            }
            case 21: {
                return "TYPE_BUS";
            }
            case 13: {
                return "TYPE_DOCK";
            }
            case 31: {
                return "TYPE_DOCK_ANALOG";
            }
            case 14: {
                return "TYPE_FM";
            }
            case 16: {
                return "TYPE_FM_TUNER";
            }
            case 9: {
                return "TYPE_HDMI";
            }
            case 10: {
                return "TYPE_HDMI_ARC";
            }
            case 29: {
                return "TYPE_HDMI_EARC";
            }
            case 23: {
                return "TYPE_HEARING_AID";
            }
            case 20: {
                return "TYPE_IP";
            }
            case 5: {
                return "TYPE_LINE_ANALOG";
            }
            case 6: {
                return "TYPE_LINE_DIGITAL";
            }
            case 25: {
                return "TYPE_REMOTE_SUBMIX";
            }
            case 18: {
                return "TYPE_TELEPHONY";
            }
            case 17: {
                return "TYPE_TV_TUNER";
            }
            case 0: {
                return "TYPE_UNKNOWN";
            }
            case 12: {
                return "TYPE_USB_ACCESSORY";
            }
            case 11: {
                return "TYPE_USB_DEVICE";
            }
            case 22: {
                return "TYPE_USB_HEADSET";
            }
            case 4: {
                return "TYPE_WIRED_HEADPHONES";
            }
            case 3: {
                return "TYPE_WIRED_HEADSET";
            }
        }
        return "TYPE_UNKNOWN(" + type + ")";
    }

    public static String audioSourceToString(int source) {
        switch (source) {
            case 0: {
                return "DEFAULT";
            }
            case 1: {
                return "MIC";
            }
            case 2: {
                return "VOICE_UPLINK";
            }
            case 3: {
                return "VOICE_DOWNLINK";
            }
            case 4: {
                return "VOICE_CALL";
            }
            case 5: {
                return "CAMCORDER";
            }
            case 6: {
                return "VOICE_RECOGNITION";
            }
            case 7: {
                return "VOICE_COMMUNICATION";
            }
            case 9: {
                return "UNPROCESSED";
            }
            case 10: {
                return "VOICE_PERFORMANCE";
            }
        }
        return "INVALID";
    }

    public static String channelMaskToString(int mask) {
        switch (mask) {
            case 12: {
                return "IN_STEREO";
            }
            case 16: {
                return "IN_MONO";
            }
        }
        return "INVALID";
    }

    @TargetApi(value=24)
    public static String audioEncodingToString(int enc) {
        switch (enc) {
            case 0: {
                return "INVALID";
            }
            case 2: {
                return "PCM_16BIT";
            }
            case 3: {
                return "PCM_8BIT";
            }
            case 4: {
                return "PCM_FLOAT";
            }
            case 5: {
                return "AC3";
            }
            case 6: {
                return "AC3";
            }
            case 7: {
                return "DTS";
            }
            case 8: {
                return "DTS_HD";
            }
            case 9: {
                return "MP3";
            }
        }
        return "Invalid encoding: " + enc;
    }

    private static void logAudioStateBasic(String tag, Context context, AudioManager audioManager) {
        Logging.d(tag, "Audio State: audio mode: " + WebRtcAudioUtils.modeToString(audioManager.getMode()) + ", has mic: " + WebRtcAudioUtils.hasMicrophone(context) + ", mic muted: " + audioManager.isMicrophoneMute() + ", music active: " + audioManager.isMusicActive() + ", speakerphone: " + audioManager.isSpeakerphoneOn() + ", BT SCO: " + audioManager.isBluetoothScoOn());
    }

    private static void logAudioStateVolume(String tag, AudioManager audioManager) {
        int[] streams = new int[]{0, 3, 2, 4, 5, 1};
        Logging.d(tag, "Audio State: ");
        boolean fixedVolume = audioManager.isVolumeFixed();
        Logging.d(tag, "  fixed volume=" + fixedVolume);
        if (!fixedVolume) {
            for (int stream : streams) {
                StringBuilder info = new StringBuilder();
                info.append("  " + WebRtcAudioUtils.streamTypeToString(stream) + ": ");
                info.append("volume=").append(audioManager.getStreamVolume(stream));
                info.append(", max=").append(audioManager.getStreamMaxVolume(stream));
                if (Build.VERSION.SDK_INT >= 23) {
                    info.append(", muted=").append(audioManager.isStreamMute(stream));
                }
                Logging.d(tag, info.toString());
            }
        }
    }

    private static void logAudioDeviceInfo(String tag, AudioManager audioManager) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        AudioDeviceInfo[] devices = audioManager.getDevices(3);
        if (devices.length == 0) {
            return;
        }
        Logging.d(tag, "Audio Devices: ");
        for (AudioDeviceInfo device : devices) {
            StringBuilder info = new StringBuilder();
            info.append("  ").append(WebRtcAudioUtils.deviceTypeToString(device.getType()));
            info.append(device.isSource() ? "(in): " : "(out): ");
            if (device.getChannelCounts().length > 0) {
                info.append("channels=").append(Arrays.toString(device.getChannelCounts()));
                info.append(", ");
            }
            if (device.getEncodings().length > 0) {
                info.append("encodings=").append(Arrays.toString(device.getEncodings()));
                info.append(", ");
            }
            if (device.getSampleRates().length > 0) {
                info.append("sample rates=").append(Arrays.toString(device.getSampleRates()));
                info.append(", ");
            }
            info.append("id=").append(device.getId());
            Logging.d(tag, info.toString());
        }
    }

    static String modeToString(int mode) {
        switch (mode) {
            case 2: {
                return "MODE_IN_CALL";
            }
            case 3: {
                return "MODE_IN_COMMUNICATION";
            }
            case 0: {
                return "MODE_NORMAL";
            }
            case 1: {
                return "MODE_RINGTONE";
            }
        }
        return "MODE_INVALID";
    }

    private static String streamTypeToString(int stream) {
        switch (stream) {
            case 0: {
                return "STREAM_VOICE_CALL";
            }
            case 3: {
                return "STREAM_MUSIC";
            }
            case 2: {
                return "STREAM_RING";
            }
            case 4: {
                return "STREAM_ALARM";
            }
            case 5: {
                return "STREAM_NOTIFICATION";
            }
            case 1: {
                return "STREAM_SYSTEM";
            }
        }
        return "STREAM_INVALID";
    }

    private static boolean hasMicrophone(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.microphone");
    }
}

