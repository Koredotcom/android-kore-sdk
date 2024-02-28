package kore.botssdk.audiocodes.webrtcclient.General;

import android.content.Context;
import android.media.AudioManager;

import com.audiocodes.mv.webrtcsdk.session.ACCallStatistics;
import com.audiocodes.mv.webrtcsdk.session.DTMFOptions;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Structure.SipAccount;


public class Prefs extends BasePrefs {

    private static final String TAG = "Prefs";

    public static void setFirstLogin(boolean firstLogin)
    {
        Log.d(TAG, "setFirstLogin: "+firstLogin);
        putBoolean("FIRST_LOGIN",firstLogin);
    }

    public static boolean isFirstLogin()
    {
        Log.d(TAG, "get isFirstLogin: "+getBoolean("FIRST_LOGIN",true));
        return getBoolean("FIRST_LOGIN",true);
    }

    public static boolean isAppOpen() {
        return	getBoolean("APP_OPEN");
    }

    public static void setAppOpen(boolean state) {
        putBoolean("APP_OPEN", state);
    }

    public static void setSipAccount(SipAccount sipAccount)
    {
        putString("SipAccountDisplayName",sipAccount.getDisplayName());
        putString("SipAccountUsername",sipAccount.getUsername());
        putString("SipAccountPassword",sipAccount.getPassword());
        putString("SipAccountDomain",sipAccount.getDomain());
        putString("SipAccountProxy",sipAccount.getProxy());
        putInt("SipAccountPort",sipAccount.getPort());
        putString("SipAccountTransport",sipAccount.getTransport().toString());
    }

    public static SipAccount getSipAccount()
    {
        SipAccount sipAccount = new SipAccount();

        sipAccount.setDisplayName(getString("SipAccountDisplayName"));
        sipAccount.setUsername(getString("SipAccountUsername"));
        sipAccount.setPassword(getString("SipAccountPassword"));
        sipAccount.setDomain(getString("SipAccountDomain"));
        sipAccount.setProxy(getString("SipAccountProxy"));
        sipAccount.setPort(getInt("SipAccountPort"));
        //String tempTransport = getString("SipAccountTransport");
        //sipAccount.setTransport(Transport.valueOf(tempTransport));
        sipAccount.setTransport(AppUtils.getTransport(getString("SipAccountTransport")));

        return sipAccount;
    }

    public static void setCallStats(ACCallStatistics acCallStatistics)
    {
        putClass("ACCallStats", acCallStatistics);
    }

    public static ACCallStatistics getCallStats()
    {
        ACCallStatistics acCallStatistics = (ACCallStatistics)getClass("ACCallStats", ACCallStatistics.class);

        return acCallStatistics;
    }

    public static void setDTMFType(DTMFOptions.DTMFMethod dtmfType)
    {
        putClass("DTMFMethod", dtmfType);
    }

    public static DTMFOptions.DTMFMethod getDTMFType()
    {
        //the default value is DTMFOptions.DTMFMethod.WEBRTC
        DTMFOptions.DTMFMethod dtmfMethod = (DTMFOptions.DTMFMethod)getClass("DTMFMethod", DTMFOptions.DTMFMethod.class, DTMFOptions.DTMFMethod.WEBRTC);

        return dtmfMethod;
    }

    public static void setLogLevel(Log.LogLevel logLevel)
    {
        putClass("LogLevel", logLevel);
    }

    public static Log.LogLevel getLogLevel()
    {
        //the default value is in configuration file
        String defaultLogLevel = BotApplication.getGlobalContext().getString(R.string.log_level_default);
        Log.LogLevel logLevel = (Log.LogLevel)getClass("LogLevel", Log.LogLevel.class, Log.LogLevel.valueOf(defaultLogLevel));

        return logLevel;
    }

    public static boolean isAutoRedirect() {
        return	getBoolean("AutoRedirect");
    }

    public static void setAutoRedirect(boolean state) {
        putBoolean("AutoRedirect", state);
    }

    public static boolean isRedirectCall() {
        return	getBoolean("RedirectCall");
    }

    public static void setRedirectCall(boolean state) {
        putBoolean("RedirectCall", state);
    }

    public static String getRedirectCallUser() {
        return	getString("RedirectCallUser", BotApplication.getGlobalContext().getString(R.string.default_redirect_call_user));
    }

    public static void setRedirectCallUser(String redirectCallUser) {
        putString("RedirectCallUser", redirectCallUser);
    }
    public static boolean isVideoHardware() {
        return	getBoolean("VideoHardware");
    }

    public static void setVideoHardware(boolean state) {
        putBoolean("VideoHardware", state);
    }


    public static boolean getAutoLogin() {
        return	getBoolean("AutoLogin",false);
    }

    public static void setAutoLogin(boolean enable) {
        putBoolean("AutoLogin", enable);
    }

    public static boolean getDisconnectBrokenConnection() {
        return	getBoolean("DisconnectBrokenConnection",true);
    }

    public static void setDisconnectBrokenConnection(boolean enable) {
        putBoolean("DisconnectBrokenConnection", enable);
    }

    public static String getSecondCall() {
        return	getString("SecondCall", BotApplication.getGlobalContext().getString(R.string.sip_default_second_call));
    }

    public static void setSecondCall(String call) {
        putString("SecondCall", call);
    }

    public static String getTransferCall() {
        return	getString("TransferCall", BotApplication.getGlobalContext().getString(R.string.sip_default_transfer_call));
    }

    public static void setTransferCall(String call) {
        putString("TransferCall", call);
    }

    public static boolean usePush() {
        return	getBoolean("USE_PUSH");
    }

    public static void setUsePush(boolean usePush) {
        putBoolean("USE_PUSH", usePush);
    }

    //volume control
    public static int getRingVolume()
    {
        AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        //default value should be max
        int res = getInt("RING_VOLUME", maxVolume);
        Log.d(TAG,"get RING_VOLUME: "+res);
        return res;
    }
    public static void setRingVolume(int vol)
    {
        Log.d(TAG,"set RING_VOLUME: "+vol);
        putInt("RING_VOLUME", vol);
    }

    public static int getCallVolume()
    {
        AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        //default value should be max
        int res = getInt("CALL_VOLUME", maxVolume);
        Log.d(TAG,"get CALL_VOLUME: "+res);
        return res;
    }
    public static void setCallVolume(int vol)
    {
        Log.d(TAG,"set CALL_VOLUME: "+vol);
        putInt("CALL_VOLUME", vol);
    }

    public static int getPrevRingVolume()
    {
        AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        //default value should be max
        int res = getInt("PREV_RING_VOLUME", maxVolume);
        Log.d(TAG,"get PREV_RING_VOLUME: "+res);
        return res;
    }
    public static void setPrevRingVolume(int vol)
    {
        Log.d(TAG,"set PREV_RING_VOLUME: "+vol);
        putInt("PREV_RING_VOLUME", vol);
    }

    public static int getPrevCallVolume()
    {

        AudioManager audioManager = (AudioManager) BotApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        //default value should be max
        int res = getInt("PREV_CALL_VOLUME", maxVolume);
        Log.d(TAG,"get PREV_CALL_VOLUME: "+res);
        return res;
    }
    public static void setPrevCallVolume(int vol)
    {
        Log.d(TAG,"set PREV_CALL_VOLUME: "+vol);
        putInt("PREV_CALL_VOLUME", vol);
    }

//    public static int getRingtoneVolume()
//    {
//        AudioManager audioManager = (AudioManager)BotApplication.getGlobalContext().getSystemService("audio");
//        int maxVolume = audioManager.getStreamMaxVolume(0);
//        //default value should be max
//        getInt("RINGTONE_VOLUME", maxVolume);
//    }
//    public static void SetRingtoneVolume(int vol)
//    {
//        putInt("RINGTONE_VOLUME", vol);
//    }
}
