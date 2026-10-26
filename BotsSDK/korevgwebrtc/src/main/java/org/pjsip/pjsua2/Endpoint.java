/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AudioMediaVector2;
import org.pjsip.pjsua2.CodecInfoVector2;
import org.pjsip.pjsua2.CodecParam;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.IntVector;
import org.pjsip.pjsua2.IpChangeParam;
import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.OnIpChangeProgressParam;
import org.pjsip.pjsua2.OnMediaEventParam;
import org.pjsip.pjsua2.OnNatCheckStunServersCompleteParam;
import org.pjsip.pjsua2.OnNatDetectionCompleteParam;
import org.pjsip.pjsua2.OnSelectAccountParam;
import org.pjsip.pjsua2.OnTimerParam;
import org.pjsip.pjsua2.OnTransportStateParam;
import org.pjsip.pjsua2.PendingJob;
import org.pjsip.pjsua2.SWIGTYPE_p_void;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.TransportInfo;
import org.pjsip.pjsua2.Version;
import org.pjsip.pjsua2.VidCodecParam;
import org.pjsip.pjsua2.VidDevManager;
import org.pjsip.pjsua2.VideoMediaVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class Endpoint {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Endpoint(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Endpoint obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_Endpoint(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        this.delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        pjsua2JNI.Endpoint_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        pjsua2JNI.Endpoint_change_ownership(this, this.swigCPtr, true);
    }

    public void libDestroy(long prmFlags) throws Exception {
        Runtime.getRuntime().gc();
        this.libDestroy_(prmFlags);
    }

    public void libDestroy() throws Exception {
        Runtime.getRuntime().gc();
        this.libDestroy_();
    }

    public static Endpoint instance() throws Exception {
        return new Endpoint(pjsua2JNI.Endpoint_instance(), false);
    }

    public Endpoint() {
        this(pjsua2JNI.new_Endpoint(), true);
        pjsua2JNI.Endpoint_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public Version libVersion() {
        return new Version(pjsua2JNI.Endpoint_libVersion(this.swigCPtr, this), true);
    }

    public void libCreate() throws Exception {
        pjsua2JNI.Endpoint_libCreate(this.swigCPtr, this);
    }

    public int libGetState() {
        return pjsua2JNI.Endpoint_libGetState(this.swigCPtr, this);
    }

    public void libInit(EpConfig prmEpConfig) throws Exception {
        pjsua2JNI.Endpoint_libInit(this.swigCPtr, this, EpConfig.getCPtr(prmEpConfig), prmEpConfig);
    }

    public void libStart() throws Exception {
        pjsua2JNI.Endpoint_libStart(this.swigCPtr, this);
    }

    public void libRegisterThread(String name) throws Exception {
        pjsua2JNI.Endpoint_libRegisterThread(this.swigCPtr, this, name);
    }

    public boolean libIsThreadRegistered() {
        return pjsua2JNI.Endpoint_libIsThreadRegistered(this.swigCPtr, this);
    }

    public void libStopWorkerThreads() {
        pjsua2JNI.Endpoint_libStopWorkerThreads(this.swigCPtr, this);
    }

    public int libHandleEvents(long msec_timeout) {
        return pjsua2JNI.Endpoint_libHandleEvents(this.swigCPtr, this, msec_timeout);
    }

    public void libDestroy_(long prmFlags) throws Exception {
        pjsua2JNI.Endpoint_libDestroy___SWIG_0(this.swigCPtr, this, prmFlags);
    }

    public void libDestroy_() throws Exception {
        pjsua2JNI.Endpoint_libDestroy___SWIG_1(this.swigCPtr, this);
    }

    public String utilStrError(int prmErr) {
        return pjsua2JNI.Endpoint_utilStrError(this.swigCPtr, this, prmErr);
    }

    public void utilLogWrite(int prmLevel, String prmSender, String prmMsg) {
        pjsua2JNI.Endpoint_utilLogWrite__SWIG_0(this.swigCPtr, this, prmLevel, prmSender, prmMsg);
    }

    public void utilLogWrite(LogEntry e) {
        pjsua2JNI.Endpoint_utilLogWrite__SWIG_1(this.swigCPtr, this, LogEntry.getCPtr(e), e);
    }

    public int utilVerifySipUri(String prmUri) {
        return pjsua2JNI.Endpoint_utilVerifySipUri(this.swigCPtr, this, prmUri);
    }

    public int utilVerifyUri(String prmUri) {
        return pjsua2JNI.Endpoint_utilVerifyUri(this.swigCPtr, this, prmUri);
    }

    public SWIGTYPE_p_void utilTimerSchedule(long prmMsecDelay, SWIGTYPE_p_void prmUserData) throws Exception {
        long cPtr = pjsua2JNI.Endpoint_utilTimerSchedule(this.swigCPtr, this, prmMsecDelay, SWIGTYPE_p_void.getCPtr(prmUserData));
        return cPtr == 0L ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void utilTimerCancel(SWIGTYPE_p_void prmToken) {
        pjsua2JNI.Endpoint_utilTimerCancel(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(prmToken));
    }

    public void utilAddPendingJob(PendingJob job) {
        pjsua2JNI.Endpoint_utilAddPendingJob(this.swigCPtr, this, PendingJob.getCPtr(job), job);
    }

    public IntVector utilSslGetAvailableCiphers() throws Exception {
        return new IntVector(pjsua2JNI.Endpoint_utilSslGetAvailableCiphers(this.swigCPtr, this), true);
    }

    public void natDetectType() throws Exception {
        pjsua2JNI.Endpoint_natDetectType(this.swigCPtr, this);
    }

    public int natGetType() throws Exception {
        return pjsua2JNI.Endpoint_natGetType(this.swigCPtr, this);
    }

    public void natUpdateStunServers(StringVector prmServers, boolean prmWait) throws Exception {
        pjsua2JNI.Endpoint_natUpdateStunServers(this.swigCPtr, this, StringVector.getCPtr(prmServers), prmServers, prmWait);
    }

    public void natCheckStunServers(StringVector prmServers, boolean prmWait, SWIGTYPE_p_void prmUserData) throws Exception {
        pjsua2JNI.Endpoint_natCheckStunServers(this.swigCPtr, this, StringVector.getCPtr(prmServers), prmServers, prmWait, SWIGTYPE_p_void.getCPtr(prmUserData));
    }

    public void natCancelCheckStunServers(SWIGTYPE_p_void token, boolean notify_cb) throws Exception {
        pjsua2JNI.Endpoint_natCancelCheckStunServers__SWIG_0(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(token), notify_cb);
    }

    public void natCancelCheckStunServers(SWIGTYPE_p_void token) throws Exception {
        pjsua2JNI.Endpoint_natCancelCheckStunServers__SWIG_1(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(token));
    }

    public int transportCreate(int type, TransportConfig cfg) throws Exception {
        return pjsua2JNI.Endpoint_transportCreate(this.swigCPtr, this, type, TransportConfig.getCPtr(cfg), cfg);
    }

    public IntVector transportEnum() throws Exception {
        return new IntVector(pjsua2JNI.Endpoint_transportEnum(this.swigCPtr, this), true);
    }

    public TransportInfo transportGetInfo(int id2) throws Exception {
        return new TransportInfo(pjsua2JNI.Endpoint_transportGetInfo(this.swigCPtr, this, id2), true);
    }

    public void transportSetEnable(int id2, boolean enabled) throws Exception {
        pjsua2JNI.Endpoint_transportSetEnable(this.swigCPtr, this, id2, enabled);
    }

    public void transportClose(int id2) throws Exception {
        pjsua2JNI.Endpoint_transportClose(this.swigCPtr, this, id2);
    }

    public void transportShutdown(SWIGTYPE_p_void tp) throws Exception {
        pjsua2JNI.Endpoint_transportShutdown(this.swigCPtr, this, SWIGTYPE_p_void.getCPtr(tp));
    }

    public void hangupAllCalls() {
        pjsua2JNI.Endpoint_hangupAllCalls(this.swigCPtr, this);
    }

    public void mediaAdd(AudioMedia media) {
        pjsua2JNI.Endpoint_mediaAdd(this.swigCPtr, this, AudioMedia.getCPtr(media), media);
    }

    public void mediaRemove(AudioMedia media) {
        pjsua2JNI.Endpoint_mediaRemove(this.swigCPtr, this, AudioMedia.getCPtr(media), media);
    }

    public boolean mediaExists(AudioMedia media) {
        return pjsua2JNI.Endpoint_mediaExists(this.swigCPtr, this, AudioMedia.getCPtr(media), media);
    }

    public long mediaMaxPorts() {
        return pjsua2JNI.Endpoint_mediaMaxPorts(this.swigCPtr, this);
    }

    public long mediaActivePorts() {
        return pjsua2JNI.Endpoint_mediaActivePorts(this.swigCPtr, this);
    }

    public AudioMediaVector2 mediaEnumPorts2() throws Exception {
        return new AudioMediaVector2(pjsua2JNI.Endpoint_mediaEnumPorts2(this.swigCPtr, this), true);
    }

    public VideoMediaVector mediaEnumVidPorts() throws Exception {
        return new VideoMediaVector(pjsua2JNI.Endpoint_mediaEnumVidPorts(this.swigCPtr, this), true);
    }

    public AudDevManager audDevManager() {
        return new AudDevManager(pjsua2JNI.Endpoint_audDevManager(this.swigCPtr, this), false);
    }

    public VidDevManager vidDevManager() {
        return new VidDevManager(pjsua2JNI.Endpoint_vidDevManager(this.swigCPtr, this), false);
    }

    public CodecInfoVector2 codecEnum2() throws Exception {
        return new CodecInfoVector2(pjsua2JNI.Endpoint_codecEnum2(this.swigCPtr, this), true);
    }

    public void codecSetPriority(String codec_id, short priority) throws Exception {
        pjsua2JNI.Endpoint_codecSetPriority(this.swigCPtr, this, codec_id, priority);
    }

    public CodecParam codecGetParam(String codec_id) throws Exception {
        return new CodecParam(pjsua2JNI.Endpoint_codecGetParam(this.swigCPtr, this, codec_id), true);
    }

    public void codecSetParam(String codec_id, CodecParam param) throws Exception {
        pjsua2JNI.Endpoint_codecSetParam(this.swigCPtr, this, codec_id, CodecParam.getCPtr(param), param);
    }

    public CodecInfoVector2 videoCodecEnum2() throws Exception {
        return new CodecInfoVector2(pjsua2JNI.Endpoint_videoCodecEnum2(this.swigCPtr, this), true);
    }

    public void videoCodecSetPriority(String codec_id, short priority) throws Exception {
        pjsua2JNI.Endpoint_videoCodecSetPriority(this.swigCPtr, this, codec_id, priority);
    }

    public VidCodecParam getVideoCodecParam(String codec_id) throws Exception {
        return new VidCodecParam(pjsua2JNI.Endpoint_getVideoCodecParam(this.swigCPtr, this, codec_id), true);
    }

    public void setVideoCodecParam(String codec_id, VidCodecParam param) throws Exception {
        pjsua2JNI.Endpoint_setVideoCodecParam(this.swigCPtr, this, codec_id, VidCodecParam.getCPtr(param), param);
    }

    public void resetVideoCodecParam(String codec_id) throws Exception {
        pjsua2JNI.Endpoint_resetVideoCodecParam(this.swigCPtr, this, codec_id);
    }

    public StringVector srtpCryptoEnum() throws Exception {
        return new StringVector(pjsua2JNI.Endpoint_srtpCryptoEnum(this.swigCPtr, this), true);
    }

    public void handleIpChange(IpChangeParam param) throws Exception {
        pjsua2JNI.Endpoint_handleIpChange(this.swigCPtr, this, IpChangeParam.getCPtr(param), param);
    }

    public void onNatDetectionComplete(OnNatDetectionCompleteParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onNatDetectionComplete(this.swigCPtr, this, OnNatDetectionCompleteParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onNatDetectionCompleteSwigExplicitEndpoint(this.swigCPtr, this, OnNatDetectionCompleteParam.getCPtr(prm), prm);
        }
    }

    public void onNatCheckStunServersComplete(OnNatCheckStunServersCompleteParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onNatCheckStunServersComplete(this.swigCPtr, this, OnNatCheckStunServersCompleteParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onNatCheckStunServersCompleteSwigExplicitEndpoint(this.swigCPtr, this, OnNatCheckStunServersCompleteParam.getCPtr(prm), prm);
        }
    }

    public void onTransportState(OnTransportStateParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onTransportState(this.swigCPtr, this, OnTransportStateParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onTransportStateSwigExplicitEndpoint(this.swigCPtr, this, OnTransportStateParam.getCPtr(prm), prm);
        }
    }

    public void onTimer(OnTimerParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onTimer(this.swigCPtr, this, OnTimerParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onTimerSwigExplicitEndpoint(this.swigCPtr, this, OnTimerParam.getCPtr(prm), prm);
        }
    }

    public void onSelectAccount(OnSelectAccountParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onSelectAccount(this.swigCPtr, this, OnSelectAccountParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onSelectAccountSwigExplicitEndpoint(this.swigCPtr, this, OnSelectAccountParam.getCPtr(prm), prm);
        }
    }

    public void onIpChangeProgress(OnIpChangeProgressParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onIpChangeProgress(this.swigCPtr, this, OnIpChangeProgressParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onIpChangeProgressSwigExplicitEndpoint(this.swigCPtr, this, OnIpChangeProgressParam.getCPtr(prm), prm);
        }
    }

    public void onMediaEvent(OnMediaEventParam prm) {
        if (this.getClass() == Endpoint.class) {
            pjsua2JNI.Endpoint_onMediaEvent(this.swigCPtr, this, OnMediaEventParam.getCPtr(prm), prm);
        } else {
            pjsua2JNI.Endpoint_onMediaEventSwigExplicitEndpoint(this.swigCPtr, this, OnMediaEventParam.getCPtr(prm), prm);
        }
    }
}

