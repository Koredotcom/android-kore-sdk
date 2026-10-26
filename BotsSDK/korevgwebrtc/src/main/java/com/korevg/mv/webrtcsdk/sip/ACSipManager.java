/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip;

import com.korevg.mv.webrtcsdk.im.InstantMessageCallback;
import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.log.LogLevel;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.sip.ACSipAccount;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;
import com.korevg.mv.webrtcsdk.sip.enums.Transport;
import com.korevg.mv.webrtcsdk.sip.listeners.SipEventListener;
import com.korevg.mv.webrtcsdk.sip.pjsip.ACPjSipManager;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ACSipManager {
    private static final String TAG = "ACSipManager";
    protected int contactRewrite = 1;
    private boolean initialized;
    protected LogLevel logLevel = Log.getLogLevel();
    protected SipEventListener sipEventListener;
    protected InstantMessageCallback instantMessageCallback;
    protected Hashtable<String, String> registerHeaders;
    public ACSipAccount sipAccount;

    public abstract void setContactRewrite(boolean var1);

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setRegisterHeaders(Hashtable<String, String> registerHeaders) {
        this.registerHeaders = registerHeaders;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        this.setLogLevel();
    }

    protected abstract void setLogLevel();

    public ACSipAccount getSipAccount() {
        return this.sipAccount;
    }

    public abstract void deinit();

    public abstract void handleNetworkChange();

    public abstract ACSipCall call(RemoteContact var1, boolean var2, ACSipCall.IRequestSDP var3, ACSipCall.ICallEventListener var4, Hashtable<String, String> var5) throws WebRTCException;

    public static synchronized ACSipManager getSipManager() {
        return ACPjSipManager.getInstance();
    }

    public void setSipEventListener(SipEventListener sipEventListener) {
        this.sipEventListener = sipEventListener;
    }

    public void setInstantMessageCallback(InstantMessageCallback instantMessageCallback) {
        this.instantMessageCallback = instantMessageCallback;
    }

    public abstract void setServerConfig(String var1, int var2, String var3, Transport var4);

    public abstract void setAccount(String var1, String var2, String var3, String var4);

    public abstract void init(boolean var1);

    public abstract boolean register() throws WebRTCException;

    public abstract void setUseSessionTimer(boolean var1);

    public abstract void sipHeadersChanged();

    public abstract long sendInstantMessage(RemoteContact var1, String var2);

    public abstract void setTlsVerifyServer(boolean var1);

    public abstract void setCaCertFilePath(String var1);

    public abstract void setSessionExpiresTimeout(int var1);

    public abstract void setNameServers(ArrayList<String> var1, boolean var2);

    public abstract void setAllowHeader(String[] var1);
}

