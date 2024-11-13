package kore.botssdk.audiocodes.webrtcclient.General;

import android.content.Context;
import android.media.AudioManager;

import com.audiocodes.mv.webrtcsdk.session.ACCallStatistics;
import com.audiocodes.mv.webrtcsdk.session.DTMFOptions;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.Structure.SipAccount;


public class Prefs extends BasePrefs {

    private static final String TAG = "Prefs";

    public static void setFirstLogin(Context context, boolean firstLogin) {
        Log.d(TAG, "setFirstLogin: " + firstLogin);
        putBoolean(context, "FIRST_LOGIN", firstLogin);
    }

    public static boolean isFirstLogin(Context context) {
        Log.d(TAG, "get isFirstLogin: " + getBoolean(context, "FIRST_LOGIN", true));
        return getBoolean(context, "FIRST_LOGIN", true);
    }

    public static boolean isAppOpen(Context context) {
        return getBoolean(context, "APP_OPEN");
    }

    public static void setAppOpen(Context context, boolean state) {
        putBoolean(context, "APP_OPEN", state);
    }

    public static void setSipAccount(Context context, SipAccount sipAccount) {
        putString(context, "SipAccountDisplayName", sipAccount.getDisplayName());
        putString(context, "SipAccountUsername", sipAccount.getUsername());
        putString(context, "SipAccountPassword", sipAccount.getPassword());
        putString(context, "SipAccountDomain", sipAccount.getDomain());
        putString(context, "SipAccountProxy", sipAccount.getProxy());
        putInt(context, "SipAccountPort", sipAccount.getPort());
        putString(context, "SipAccountTransport", sipAccount.getTransport().toString());
    }

    public static SipAccount getSipAccount(Context context) {
        SipAccount sipAccount = new SipAccount(context);

        sipAccount.setDisplayName(getString(context, "SipAccountDisplayName"));
        sipAccount.setUsername(getString(context, "SipAccountUsername"));
        sipAccount.setPassword(getString(context, "SipAccountPassword"));
        sipAccount.setDomain(getString(context, "SipAccountDomain"));
        sipAccount.setProxy(getString(context, "SipAccountProxy"));
        sipAccount.setPort(getInt(context, "SipAccountPort"));
        //String tempTransport = getString("SipAccountTransport");
        //sipAccount.setTransport(Transport.valueOf(tempTransport));
        sipAccount.setTransport(AppUtils.getTransport(context, getString(context, "SipAccountTransport")));

        return sipAccount;
    }

    public static void setCallStats(Context context, ACCallStatistics acCallStatistics) {
        putClass(context, "ACCallStats", acCallStatistics);
    }

    public static ACCallStatistics getCallStats(Context context) {
        ACCallStatistics acCallStatistics = (ACCallStatistics) getClass(context, "ACCallStats", ACCallStatistics.class);

        return acCallStatistics;
    }

    public static void setDTMFType(Context context, DTMFOptions.DTMFMethod dtmfType) {
        putClass(context, "DTMFMethod", dtmfType);
    }

    public static DTMFOptions.DTMFMethod getDTMFType(Context context) {
        //the default value is DTMFOptions.DTMFMethod.WEBRTC
        DTMFOptions.DTMFMethod dtmfMethod = (DTMFOptions.DTMFMethod) getClass(context, "DTMFMethod", DTMFOptions.DTMFMethod.class, DTMFOptions.DTMFMethod.WEBRTC);

        return dtmfMethod;
    }

    public static void setLogLevel(Context context, Log.LogLevel logLevel) {
        putClass(context, "LogLevel", logLevel);
    }

    public static Log.LogLevel getLogLevel(Context context) {
        //the default value is in configuration file
        String defaultLogLevel = context.getString(R.string.log_level_default);
        Log.LogLevel logLevel = (Log.LogLevel) getClass(context, "LogLevel", Log.LogLevel.class, Log.LogLevel.valueOf(defaultLogLevel));

        return logLevel;
    }

    public static boolean isAutoRedirect(Context context) {
        return getBoolean(context, "AutoRedirect");
    }

    public static void setAutoRedirect(Context context, boolean state) {
        putBoolean(context, "AutoRedirect", state);
    }

    public static boolean isRedirectCall(Context context) {
        return getBoolean(context, "RedirectCall");
    }

    public static void setRedirectCall(Context context, boolean state) {
        putBoolean(context, "RedirectCall", state);
    }

    public static String getRedirectCallUser(Context context) {
        return getString(context, "RedirectCallUser", context.getString(R.string.default_redirect_call_user));
    }

    public static void setRedirectCallUser(Context context, String redirectCallUser) {
        putString(context, "RedirectCallUser", redirectCallUser);
    }

    public static boolean isVideoHardware(Context context) {
        return getBoolean(context, "VideoHardware");
    }

    public static void setVideoHardware(Context context, boolean state) {
        putBoolean(context, "VideoHardware", state);
    }


    public static boolean getAutoLogin(Context context) {
        return getBoolean(context, "AutoLogin", false);
    }

    public static void setAutoLogin(Context context, boolean enable) {
        putBoolean(context, "AutoLogin", enable);
    }

    public static boolean getDisconnectBrokenConnection(Context context) {
        return getBoolean(context, "DisconnectBrokenConnection", true);
    }

    public static void setDisconnectBrokenConnection(Context context, boolean enable) {
        putBoolean(context, "DisconnectBrokenConnection", enable);
    }

    public static String getSecondCall(Context context) {
        return getString(context, "SecondCall", context.getString(R.string.sip_default_second_call));
    }

    public static void setSecondCall(Context context, String call) {
        putString(context, "SecondCall", call);
    }

    public static String getTransferCall(Context context) {
        return getString(context, "TransferCall", context.getString(R.string.sip_default_transfer_call));
    }

    public static void setTransferCall(Context context, String call) {
        putString(context, "TransferCall", call);
    }

    public static boolean usePush(Context context) {
        return getBoolean(context, "USE_PUSH");
    }

    public static void setUsePush(Context context, boolean usePush) {
        putBoolean(context, "USE_PUSH", usePush);
    }

    //volume control
    public static int getRingVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        //default value should be max
        int res = getInt(context, "RING_VOLUME", maxVolume);
        Log.d(TAG, "get RING_VOLUME: " + res);
        return res;
    }

    public static void setRingVolume(Context context, int vol) {
        Log.d(TAG, "set RING_VOLUME: " + vol);
        putInt(context, "RING_VOLUME", vol);
    }

    public static int getCallVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        //default value should be max
        int res = getInt(context, "CALL_VOLUME", maxVolume);
        Log.d(TAG, "get CALL_VOLUME: " + res);
        return res;
    }

    public static void setCallVolume(Context context, int vol) {
        Log.d(TAG, "set CALL_VOLUME: " + vol);
        putInt(context, "CALL_VOLUME", vol);
    }

    public static int getPrevRingVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        //default value should be max
        int res = getInt(context, "PREV_RING_VOLUME", maxVolume);
        Log.d(TAG, "get PREV_RING_VOLUME: " + res);
        return res;
    }

    public static void setPrevRingVolume(Context context, int vol) {
        Log.d(TAG, "set PREV_RING_VOLUME: " + vol);
        putInt(context, "PREV_RING_VOLUME", vol);
    }

    public static int getPrevCallVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        //default value should be max
        int res = getInt(context, "PREV_CALL_VOLUME", maxVolume);
        Log.d(TAG, "get PREV_CALL_VOLUME: " + res);
        return res;
    }

    public static void setPrevCallVolume(Context context, int vol) {
        Log.d(TAG, "set PREV_CALL_VOLUME: " + vol);
        putInt(context, "PREV_CALL_VOLUME", vol);
    }

//    public static int getRingtoneVolume()
//    {
//        AudioManager audioManager = (AudioManager)context.getSystemService("audio");
//        int maxVolume = audioManager.getStreamMaxVolume(0);
//        //default value should be max
//        getInt("RINGTONE_VOLUME", maxVolume);
//    }
//    public static void SetRingtoneVolume(int vol)
//    {
//        putInt("RINGTONE_VOLUME", vol);
//    }
}
