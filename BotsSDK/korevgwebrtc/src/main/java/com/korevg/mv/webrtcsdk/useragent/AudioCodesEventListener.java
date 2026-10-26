/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.useragent;

import com.korevg.mv.webrtcsdk.im.InstanceMessageStatus;
import com.korevg.mv.webrtcsdk.session.AudioCodesSession;
import com.korevg.mv.webrtcsdk.session.InfoAlert;
import com.korevg.mv.webrtcsdk.session.RemoteContact;

public interface AudioCodesEventListener {
    public void loginStateChanged(boolean var1, int var2, String var3);

    public void incomingCall(AudioCodesSession var1, InfoAlert var2);

    public void onIncomingInstantMessage(RemoteContact var1, String var2);

    public void onInstantMessageStatus(InstanceMessageStatus var1, long var2);
}

