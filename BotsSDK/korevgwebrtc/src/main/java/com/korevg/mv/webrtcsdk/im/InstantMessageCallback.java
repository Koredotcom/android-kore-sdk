/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.im;

import com.korevg.mv.webrtcsdk.session.RemoteContact;

public interface InstantMessageCallback {
    public void onInstantMessage(RemoteContact var1, String var2);

    public void onInstantMessageStatus(int var1, long var2);
}

