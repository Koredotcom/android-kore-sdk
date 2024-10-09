package com.kore.ui.audiocodes.webrtcclient.general

import android.content.Context
import android.media.AudioManager
import com.audiocodes.mv.webrtcsdk.session.ACCallStatistics
import com.audiocodes.mv.webrtcsdk.session.DTMFOptions.DTMFMethod
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils.getTransport
import com.kore.ui.audiocodes.webrtcclient.general.Log.d
import com.kore.ui.audiocodes.webrtcclient.structure.SipAccount

object Prefs : BasePrefs() {
    private const val TAG = "Prefs"
    fun getIsFirstLogin(context: Context): Boolean {
        d(TAG, "get isFirstLogin: " + getBoolean(context, "FIRST_LOGIN", true))
        return getBoolean(context, "FIRST_LOGIN", true)
    }

    fun setIsFirstLogin(context: Context, firstLogin: Boolean) {
        d(TAG, "setFirstLogin: $firstLogin")
        putBoolean(context, "FIRST_LOGIN", firstLogin)
    }

    fun getIsAppOpen(context: Context): Boolean = getBoolean(context, "APP_OPEN")
    fun setIsAppOpen(context: Context, state: Boolean) {
        putBoolean(context, "APP_OPEN", state)
    }

    fun getSipAccount(context: Context): SipAccount {
        val sipAccount = SipAccount(context)
        sipAccount.displayName = getString(context, "SipAccountDisplayName")
        sipAccount.username = getString(context, "SipAccountUsername")
        sipAccount.password = getString(context, "SipAccountPassword")
        sipAccount.domain = getString(context, "SipAccountDomain")
        sipAccount.proxy = getString(context, "SipAccountProxy")
        sipAccount.port = getInt(context, "SipAccountPort")
        //String tempTransport = getString(context, "SipAccountTransport");
        //sipAccount.setTransport(Transport.valueOf(tempTransport));
        sipAccount.transport = getTransport(context, getString(context, "SipAccountTransport"))
        return sipAccount
    }

    fun setSipAccount(context: Context, sipAccount: SipAccount) {
        putString(context, "SipAccountDisplayName", sipAccount.displayName)
        putString(context, "SipAccountUsername", sipAccount.username)
        putString(context, "SipAccountPassword", sipAccount.password)
        putString(context, "SipAccountDomain", sipAccount.domain)
        putString(context, "SipAccountProxy", sipAccount.proxy)
        putInt(context, "SipAccountPort", sipAccount.port)
        putString(context, "SipAccountTransport", sipAccount.transport.toString())
    }

    fun getCallStats(context: Context): ACCallStatistics? = getClass(context, "ACCallStats", ACCallStatistics::class.java) as ACCallStatistics?
    fun setCallState(context: Context, acCallStatistics: ACCallStatistics) {
        putClass(context, "ACCallStats", acCallStatistics)
    }

    fun getDTMFType(context: Context) =//the default value is DTMFOptions.DTMFMethod.WEBRTC
        getClass(context, "DTMFMethod", DTMFMethod::class.java, DTMFMethod.WEBRTC) as DTMFMethod?

    fun setDTMFType(context: Context, dtmfType: DTMFMethod) {
        putClass(context, "DTMFMethod", dtmfType)
    }

    fun getLogLevel(context: Context): Log.LogLevel? {
        //the default value is in configuration file
        val defaultLogLevel = context.getString(R.string.log_level_default)
        return getClass(context, "LogLevel", Log.LogLevel::class.java, Log.LogLevel.valueOf(defaultLogLevel)) as Log.LogLevel?
    }

    fun setLogLevel(context: Context, logLevel: Log.LogLevel) {
        putClass(context, "LogLevel", logLevel)
    }

    fun getIsAutoRedirect(context: Context): Boolean = getBoolean(context, "AutoRedirect")
    fun setIsAutoRedirect(context: Context, state: Boolean) {
        putBoolean(context, "AutoRedirect", state)
    }

    fun getIsRedirectCall(context: Context): Boolean = getBoolean(context, "RedirectCall")
    fun setIsRedirectCall(context: Context, state: Boolean) {
        putBoolean(context, "RedirectCall", state)
    }

    fun getRedirectCallUser(context: Context): String? {
        return getString(context, "RedirectCallUser", context.getString(R.string.default_redirect_call_user))
    }

    fun setRedirectCallUser(context: Context, redirectCallUser: String) {
        putString(context, "RedirectCallUser", redirectCallUser)
    }

    fun getIsVideoHardware(context: Context): Boolean = getBoolean(context, "VideoHardware")
    fun setIsVideoHardware(context: Context, state: Boolean) {
        putBoolean(context, "VideoHardware", state)
    }

    fun getAutoLogin(context: Context): Boolean = getBoolean(context, "AutoLogin", false)
    fun setAutoLogin(context: Context, enable: Boolean) {
        putBoolean(context, "AutoLogin", enable)
    }

    fun getDisconnectBrokenConnection(context: Context) = getBoolean(context, "DisconnectBrokenConnection", true)
    fun setDisconnectBrokenConnection(context: Context, enable: Boolean) {
        putBoolean(context, "DisconnectBrokenConnection", enable)
    }

    fun getSecondCall(context: Context): String? {
        return getString(context, "SecondCall", context.getString(R.string.sip_default_second_call))
    }

    fun setSecondCall(context: Context, call: String) {
        putString(context, "SecondCall", call)
    }

    fun getTransferCall(context: Context): String? {
        return getString(context, "TransferCall", context.getString(R.string.sip_default_transfer_call))
    }

    fun setTransferCall(context: Context, call: String) {
        putString(context, "TransferCall", call)
    }

    fun usePush(context: Context): Boolean {
        return getBoolean(context, "USE_PUSH")
    }

    fun setUsePush(context: Context, usePush: Boolean) {
        putBoolean(context, "USE_PUSH", usePush)
    }

    //volume control
    fun getRingVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        //default value should be max
        val volume = getInt(context, "RING_VOLUME", maxVolume)
        d(TAG, "get RING_VOLUME: $volume")
        return volume
    }

    fun setRingVolume(context: Context, vol: Int) {
        d(TAG, "set RING_VOLUME: $vol")
        putInt(context, "RING_VOLUME", vol)
    }

    fun getCallVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        //default value should be max
        val volume = getInt(context, "CALL_VOLUME", maxVolume)
        d(TAG, "get CALL_VOLUME: $volume")
        return volume
    }

    fun setCallVolume(context: Context, vol: Int) {
        d(TAG, "set CALL_VOLUME: $vol")
        putInt(context, "CALL_VOLUME", vol)
    }

    private var prevRingVolume: Int = 0
    fun getPrevRingVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        //default value should be max
        val volume = getInt(context, "PREV_RING_VOLUME", maxVolume)
        d(TAG, "get PREV_RING_VOLUME: $volume")
        return volume
    }

    fun setPrevRingVolume(context: Context, vol: Int) {
        d(TAG, "set PREV_RING_VOLUME: $vol")
        putInt(context, "PREV_RING_VOLUME", vol)
    }

    fun getPrevCallVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
        //default value should be max
        val volume = getInt(context, "PREV_CALL_VOLUME", maxVolume)
        d(TAG, "get PREV_CALL_VOLUME: $volume")
        return volume
    }

    fun setPrevCallVolume(context: Context, vol: Int) {
        d(TAG, "set PREV_CALL_VOLUME: $vol")
        putInt(context, "PREV_CALL_VOLUME", vol)
    }

//    public static int getRingtoneVolume()
    //    {
    //        AudioManager audioManager = (AudioManager)BotApplication.Companion.context.getSystemService("audio");
    //        int maxVolume = audioManager.getStreamMaxVolume(0);
    //        //default value should be max
    //        getInt("RINGTONE_VOLUME", maxVolume);
    //    }
    //    public static void SetRingtoneVolume(int vol)
    //    {
    //        putInt("RINGTONE_VOLUME", vol);
    //    }
}
