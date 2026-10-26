/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.session;

import com.korevg.mv.webrtcsdk.session.AudioCodesSession;
import com.korevg.mv.webrtcsdk.session.NotifyEvent;
import com.korevg.mv.webrtcsdk.session.TerminationInfo;

public interface AudioCodesSessionEventListener {
    public void callTerminated(AudioCodesSession var1, TerminationInfo var2);

    public void callProgress(AudioCodesSession var1);

    public void cameraSwitched(boolean var1);

    public void reinviteWithVideoCallback(AudioCodesSession var1);

    public void mediaFailed(AudioCodesSession var1);

    public void incomingNotify(NotifyEvent var1, String var2);
}

