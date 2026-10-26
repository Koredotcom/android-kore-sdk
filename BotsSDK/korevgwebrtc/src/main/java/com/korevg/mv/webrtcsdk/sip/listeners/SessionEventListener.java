/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip.listeners;

import com.korevg.mv.webrtcsdk.session.ACCall;
import com.korevg.mv.webrtcsdk.session.CallState;
import com.korevg.mv.webrtcsdk.session.NotifyEvent;
import com.korevg.mv.webrtcsdk.session.TerminationInfo;

public interface SessionEventListener {
    public void callTerminated(ACCall var1, TerminationInfo var2);

    public void callProgress(ACCall var1, CallState var2);

    public void reinviteWithVideoCallback();

    public void incomingNotify(NotifyEvent var1, String var2);
}

