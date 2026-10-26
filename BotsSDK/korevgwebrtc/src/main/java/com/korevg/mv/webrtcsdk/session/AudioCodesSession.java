/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  org.webrtc.SurfaceViewRenderer
 */
package com.korevg.mv.webrtcsdk.session;

import android.app.Activity;
import com.korevg.mv.webrtcsdk.session.ACCallStatistics;
import com.korevg.mv.webrtcsdk.session.AudioCodesSessionEventListener;
import com.korevg.mv.webrtcsdk.session.CallState;
import com.korevg.mv.webrtcsdk.session.CallTermination;
import com.korevg.mv.webrtcsdk.session.CallTransferState;
import com.korevg.mv.webrtcsdk.session.DTMF;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.useragent.WebRTCException;
import java.util.Hashtable;
import org.webrtc.SurfaceViewRenderer;

public interface AudioCodesSession {
    public void addSessionEventListener(AudioCodesSessionEventListener var1);

    public void removeSessionEventListener(AudioCodesSessionEventListener var1);

    public CallState getCallState();

    public int getSessionID();

    public void setLocaLRenderPosition(int var1, int var2);

    public void answer(Hashtable<String, String> var1, boolean var2);

    public void reject(Hashtable<String, String> var1);

    public void redirect(RemoteContact var1, Hashtable<String, String> var2);

    public void terminate();

    public void muteAudio(boolean var1);

    public void muteVideo(boolean var1);

    public boolean isAudioMuted();

    public boolean isVideoMuted();

    public void sendDTMF(DTMF var1);

    public void sendInfo(String var1);

    public boolean isOutgoing();

    public boolean hasVideo();

    public RemoteContact getRemoteNumber();

    public void setWithVideo(boolean var1);

    public void showVideo(Activity var1) throws WebRTCException;

    public void showVideo(SurfaceViewRenderer var1, SurfaceViewRenderer var2) throws WebRTCException;

    public void switchCamera();

    public boolean hold(boolean var1);

    public void reinviteWithVideo();

    public void stopVideo();

    public CallTermination getTermination();

    public int duration();

    public long getCallStartTime();

    public void setUserData(Object var1);

    public Object getUserData();

    public boolean isLocalHold();

    public boolean isDelayedOffer();

    public boolean isRemoteHold();

    public ACCallStatistics getStats();

    public RemoteContact getRedirectContact();

    public boolean transferCall(RemoteContact var1);

    public boolean transferCall(AudioCodesSession var1);

    public RemoteContact getTransferContact();

    public CallTransferState getTransferState();
}

