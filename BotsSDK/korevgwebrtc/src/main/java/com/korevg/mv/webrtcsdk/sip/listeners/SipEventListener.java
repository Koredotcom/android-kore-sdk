/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip.listeners;

import com.korevg.mv.webrtcsdk.session.InfoAlert;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;

public interface SipEventListener {
    public void loginStateChanged(boolean var1, int var2, String var3);

    public void incomingCall(String var1, ACSipCall var2, InfoAlert var3);
}

