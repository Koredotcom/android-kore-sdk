/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.korevg.mv.webrtcsdk.R$string
 */
package com.korevg.mv.webrtcsdk.useragent;

import android.content.Context;
import com.korevg.mv.webrtcsdk.R;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.DTMFOptions;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.useragent.VideoConfiguration;

public final class ACConfiguration {
    private static String VERSION = "AudioCodes UA login not called";
    private boolean automaticRedirect = false;
    private int maxAudioBitRate = -1;
    private VideoConfiguration videoConfiguration = new VideoConfiguration();
    private int localSipPort = 6000;
    private RemoteContact redirectContact;
    private boolean enableRedirect = false;
    private DTMFOptions dtmfOptions = new DTMFOptions();
    private static ACConfiguration instance = null;
    private int iceCandidateWaitPeriod = 3000;

    private ACConfiguration() {
    }

    public void init(Context context) {
        VERSION = context.getString(R.string.version);
        Log.d("ContentValues", "WebRTC SDK version : " + VERSION);
    }

    public static String version() {
        return VERSION;
    }

    public int getIceCandidateWaitPeriod() {
        return this.iceCandidateWaitPeriod;
    }

    public void setIceCandidateWaitPeriod(int iceCandidateWaitPeriod) {
        this.iceCandidateWaitPeriod = iceCandidateWaitPeriod;
    }

    public static synchronized ACConfiguration getConfiguration() {
        if (instance == null) {
            instance = new ACConfiguration();
        }
        return instance;
    }

    private int getMaxAudioBitRate() {
        return this.maxAudioBitRate;
    }

    private void setMaxAudioBitRate(int maxAudioBitRate) {
        this.maxAudioBitRate = maxAudioBitRate;
    }

    public VideoConfiguration getVideoConfiguration() {
        return this.videoConfiguration;
    }

    public void setVideoConfiguration(VideoConfiguration videoConfiguration) {
        this.videoConfiguration = videoConfiguration;
    }

    public int getLocalServerPort() {
        return this.localSipPort;
    }

    public void setLocalServerPort(int localServerPort) {
        this.localSipPort = localServerPort;
    }

    public DTMFOptions getDtmfOptions() {
        return this.dtmfOptions;
    }

    public void setDtmfOptions(DTMFOptions dtmfOptions) {
        this.dtmfOptions = dtmfOptions;
    }

    public boolean getAutomaticCallOnRedirect() {
        return this.automaticRedirect;
    }

    public void setAutomaticCallOnRedirect(boolean automaticRedirect) {
        this.automaticRedirect = automaticRedirect;
    }

    public RemoteContact getRedirectContact() {
        return this.redirectContact;
    }

    public boolean getRedirectEnabled() {
        return this.enableRedirect;
    }

    public void setRedirect(boolean redirect, RemoteContact redirectContact) {
        this.enableRedirect = redirect;
        this.redirectContact = redirectContact;
    }
}

