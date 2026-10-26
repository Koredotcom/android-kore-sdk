/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountCallConfig;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AccountIpChangeConfig;
import org.pjsip.pjsua2.AccountMediaConfig;
import org.pjsip.pjsua2.AccountMwiConfig;
import org.pjsip.pjsua2.AccountNatConfig;
import org.pjsip.pjsua2.AccountPresConfig;
import org.pjsip.pjsua2.AccountRegConfig;
import org.pjsip.pjsua2.AccountSipConfig;
import org.pjsip.pjsua2.AccountVideoConfig;
import org.pjsip.pjsua2.AudDevErrorEvent;
import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioDevInfo;
import org.pjsip.pjsua2.AudioDevInfoVector;
import org.pjsip.pjsua2.AudioDevInfoVector2;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AudioMediaPlayer;
import org.pjsip.pjsua2.AudioMediaPlayerInfo;
import org.pjsip.pjsua2.AudioMediaRecorder;
import org.pjsip.pjsua2.AudioMediaTransmitParam;
import org.pjsip.pjsua2.AudioMediaVector;
import org.pjsip.pjsua2.AudioMediaVector2;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;
import org.pjsip.pjsua2.BuddyVector;
import org.pjsip.pjsua2.BuddyVector2;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSendDtmfParam;
import org.pjsip.pjsua2.CallSendRequestParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.CallVidSetStreamParam;
import org.pjsip.pjsua2.CodecFmtp;
import org.pjsip.pjsua2.CodecFmtpVector;
import org.pjsip.pjsua2.CodecInfo;
import org.pjsip.pjsua2.CodecInfoVector;
import org.pjsip.pjsua2.CodecInfoVector2;
import org.pjsip.pjsua2.CodecOpusConfig;
import org.pjsip.pjsua2.CodecParam;
import org.pjsip.pjsua2.CodecParamInfo;
import org.pjsip.pjsua2.CodecParamSetting;
import org.pjsip.pjsua2.ConfPortInfo;
import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.Error;
import org.pjsip.pjsua2.ExtraAudioDevice;
import org.pjsip.pjsua2.FindBuddyMatch;
import org.pjsip.pjsua2.IntVector;
import org.pjsip.pjsua2.IpChangeParam;
import org.pjsip.pjsua2.JbufState;
import org.pjsip.pjsua2.JsonDocument;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;
import org.pjsip.pjsua2.LossType;
import org.pjsip.pjsua2.MathStat;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.MediaConfig;
import org.pjsip.pjsua2.MediaCoordinate;
import org.pjsip.pjsua2.MediaEvent;
import org.pjsip.pjsua2.MediaEventData;
import org.pjsip.pjsua2.MediaFmtChangedEvent;
import org.pjsip.pjsua2.MediaFormat;
import org.pjsip.pjsua2.MediaFormatAudio;
import org.pjsip.pjsua2.MediaFormatAudioVector;
import org.pjsip.pjsua2.MediaFormatVideo;
import org.pjsip.pjsua2.MediaFormatVideoVector;
import org.pjsip.pjsua2.MediaSize;
import org.pjsip.pjsua2.MediaTransportInfo;
import org.pjsip.pjsua2.OnBuddyEvSubStateParam;
import org.pjsip.pjsua2.OnCallAnsweredParam;
import org.pjsip.pjsua2.OnCallMediaEventParam;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallMediaTransportStateParam;
import org.pjsip.pjsua2.OnCallRedirectedParam;
import org.pjsip.pjsua2.OnCallReplaceRequestParam;
import org.pjsip.pjsua2.OnCallReplacedParam;
import org.pjsip.pjsua2.OnCallRxOfferParam;
import org.pjsip.pjsua2.OnCallRxReinviteParam;
import org.pjsip.pjsua2.OnCallSdpCreatedParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnCallTransferRequestParam;
import org.pjsip.pjsua2.OnCallTransferStatusParam;
import org.pjsip.pjsua2.OnCallTsxStateParam;
import org.pjsip.pjsua2.OnCallTxOfferParam;
import org.pjsip.pjsua2.OnCreateMediaTransportParam;
import org.pjsip.pjsua2.OnCreateMediaTransportSrtpParam;
import org.pjsip.pjsua2.OnDtmfDigitParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnIncomingSubscribeParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnIpChangeProgressParam;
import org.pjsip.pjsua2.OnMediaEventParam;
import org.pjsip.pjsua2.OnMwiInfoParam;
import org.pjsip.pjsua2.OnNatCheckStunServersCompleteParam;
import org.pjsip.pjsua2.OnNatDetectionCompleteParam;
import org.pjsip.pjsua2.OnRegStartedParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.OnSelectAccountParam;
import org.pjsip.pjsua2.OnStreamCreatedParam;
import org.pjsip.pjsua2.OnStreamDestroyedParam;
import org.pjsip.pjsua2.OnTimerParam;
import org.pjsip.pjsua2.OnTransportStateParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.PendingJob;
import org.pjsip.pjsua2.PersistentDocument;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.PresNotifyParam;
import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.RegProgressParam;
import org.pjsip.pjsua2.RtcpFbCap;
import org.pjsip.pjsua2.RtcpFbCapVector;
import org.pjsip.pjsua2.RtcpFbConfig;
import org.pjsip.pjsua2.RtcpSdes;
import org.pjsip.pjsua2.RtcpStat;
import org.pjsip.pjsua2.RtcpStreamStat;
import org.pjsip.pjsua2.RxMsgEvent;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.SendTypingIndicationParam;
import org.pjsip.pjsua2.SipEvent;
import org.pjsip.pjsua2.SipEventBody;
import org.pjsip.pjsua2.SipHeader;
import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.SipMediaType;
import org.pjsip.pjsua2.SipMultipartPart;
import org.pjsip.pjsua2.SipMultipartPartVector;
import org.pjsip.pjsua2.SipRxData;
import org.pjsip.pjsua2.SipTransaction;
import org.pjsip.pjsua2.SipTxData;
import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.SrtpCrypto;
import org.pjsip.pjsua2.SrtpCryptoVector;
import org.pjsip.pjsua2.SrtpOpt;
import org.pjsip.pjsua2.SslCertInfo;
import org.pjsip.pjsua2.SslCertName;
import org.pjsip.pjsua2.SslCertNameVector;
import org.pjsip.pjsua2.StreamInfo;
import org.pjsip.pjsua2.StreamStat;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.TimeVal;
import org.pjsip.pjsua2.TimerEvent;
import org.pjsip.pjsua2.TlsConfig;
import org.pjsip.pjsua2.TlsInfo;
import org.pjsip.pjsua2.ToneDesc;
import org.pjsip.pjsua2.ToneDescVector;
import org.pjsip.pjsua2.ToneDigit;
import org.pjsip.pjsua2.ToneDigitMapDigit;
import org.pjsip.pjsua2.ToneDigitMapVector;
import org.pjsip.pjsua2.ToneDigitVector;
import org.pjsip.pjsua2.ToneGenerator;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.TransportInfo;
import org.pjsip.pjsua2.TsxStateEvent;
import org.pjsip.pjsua2.TsxStateEventSrc;
import org.pjsip.pjsua2.TxErrorEvent;
import org.pjsip.pjsua2.TxMsgEvent;
import org.pjsip.pjsua2.UaConfig;
import org.pjsip.pjsua2.UserEvent;
import org.pjsip.pjsua2.Version;
import org.pjsip.pjsua2.VidCodecParam;
import org.pjsip.pjsua2.VidConfPortInfo;
import org.pjsip.pjsua2.VidDevManager;
import org.pjsip.pjsua2.VideoDevInfo;
import org.pjsip.pjsua2.VideoDevInfoVector;
import org.pjsip.pjsua2.VideoDevInfoVector2;
import org.pjsip.pjsua2.VideoMedia;
import org.pjsip.pjsua2.VideoMediaTransmitParam;
import org.pjsip.pjsua2.VideoMediaVector;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoSwitchParam;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.VideoWindowInfo;
import org.pjsip.pjsua2.WindowHandle;
import org.pjsip.pjsua2.pj_qos_params;
import org.pjsip.pjsua2.pjmedia_tone_desc;
import org.pjsip.pjsua2.pjmedia_tone_digit;
import org.pjsip.pjsua2.pjmedia_tone_digit_map;

public class pjsua2JNI {
    public static final native void pj_qos_params_flags_set(long var0, pj_qos_params var2, short var3);

    public static final native short pj_qos_params_flags_get(long var0, pj_qos_params var2);

    public static final native void pj_qos_params_dscp_val_set(long var0, pj_qos_params var2, short var3);

    public static final native short pj_qos_params_dscp_val_get(long var0, pj_qos_params var2);

    public static final native void pj_qos_params_so_prio_set(long var0, pj_qos_params var2, short var3);

    public static final native short pj_qos_params_so_prio_get(long var0, pj_qos_params var2);

    public static final native void pj_qos_params_wmm_prio_set(long var0, pj_qos_params var2, int var3);

    public static final native int pj_qos_params_wmm_prio_get(long var0, pj_qos_params var2);

    public static final native long new_pj_qos_params();

    public static final native void delete_pj_qos_params(long var0);

    public static final native void pjmedia_tone_digit_digit_set(long var0, pjmedia_tone_digit var2, char var3);

    public static final native char pjmedia_tone_digit_digit_get(long var0, pjmedia_tone_digit var2);

    public static final native void pjmedia_tone_digit_on_msec_set(long var0, pjmedia_tone_digit var2, short var3);

    public static final native short pjmedia_tone_digit_on_msec_get(long var0, pjmedia_tone_digit var2);

    public static final native void pjmedia_tone_digit_off_msec_set(long var0, pjmedia_tone_digit var2, short var3);

    public static final native short pjmedia_tone_digit_off_msec_get(long var0, pjmedia_tone_digit var2);

    public static final native void pjmedia_tone_digit_volume_set(long var0, pjmedia_tone_digit var2, short var3);

    public static final native short pjmedia_tone_digit_volume_get(long var0, pjmedia_tone_digit var2);

    public static final native long new_pjmedia_tone_digit();

    public static final native void delete_pjmedia_tone_digit(long var0);

    public static final native void pjmedia_tone_digit_map_count_set(long var0, pjmedia_tone_digit_map var2, long var3);

    public static final native long pjmedia_tone_digit_map_count_get(long var0, pjmedia_tone_digit_map var2);

    public static final native long new_pjmedia_tone_digit_map();

    public static final native void delete_pjmedia_tone_digit_map(long var0);

    public static final native void pjmedia_tone_desc_freq1_set(long var0, pjmedia_tone_desc var2, short var3);

    public static final native short pjmedia_tone_desc_freq1_get(long var0, pjmedia_tone_desc var2);

    public static final native void pjmedia_tone_desc_freq2_set(long var0, pjmedia_tone_desc var2, short var3);

    public static final native short pjmedia_tone_desc_freq2_get(long var0, pjmedia_tone_desc var2);

    public static final native void pjmedia_tone_desc_on_msec_set(long var0, pjmedia_tone_desc var2, short var3);

    public static final native short pjmedia_tone_desc_on_msec_get(long var0, pjmedia_tone_desc var2);

    public static final native void pjmedia_tone_desc_off_msec_set(long var0, pjmedia_tone_desc var2, short var3);

    public static final native short pjmedia_tone_desc_off_msec_get(long var0, pjmedia_tone_desc var2);

    public static final native void pjmedia_tone_desc_volume_set(long var0, pjmedia_tone_desc var2, short var3);

    public static final native short pjmedia_tone_desc_volume_get(long var0, pjmedia_tone_desc var2);

    public static final native void pjmedia_tone_desc_flags_set(long var0, pjmedia_tone_desc var2, short var3);

    public static final native short pjmedia_tone_desc_flags_get(long var0, pjmedia_tone_desc var2);

    public static final native long new_pjmedia_tone_desc();

    public static final native void delete_pjmedia_tone_desc(long var0);

    public static final native long new_StringVector__SWIG_0();

    public static final native long new_StringVector__SWIG_1(long var0);

    public static final native long StringVector_size(long var0, StringVector var2);

    public static final native long StringVector_capacity(long var0, StringVector var2);

    public static final native void StringVector_reserve(long var0, StringVector var2, long var3);

    public static final native boolean StringVector_isEmpty(long var0, StringVector var2);

    public static final native void StringVector_clear(long var0, StringVector var2);

    public static final native void StringVector_add(long var0, StringVector var2, String var3);

    public static final native String StringVector_get(long var0, StringVector var2, int var3);

    public static final native void StringVector_set(long var0, StringVector var2, int var3, String var4);

    public static final native void delete_StringVector(long var0);

    public static final native long new_IntVector__SWIG_0();

    public static final native long new_IntVector__SWIG_1(long var0);

    public static final native long IntVector_size(long var0, IntVector var2);

    public static final native long IntVector_capacity(long var0, IntVector var2);

    public static final native void IntVector_reserve(long var0, IntVector var2, long var3);

    public static final native boolean IntVector_isEmpty(long var0, IntVector var2);

    public static final native void IntVector_clear(long var0, IntVector var2);

    public static final native void IntVector_add(long var0, IntVector var2, int var3);

    public static final native int IntVector_get(long var0, IntVector var2, int var3);

    public static final native void IntVector_set(long var0, IntVector var2, int var3, int var4);

    public static final native void delete_IntVector(long var0);

    public static final native void Error_status_set(long var0, Error var2, int var3);

    public static final native int Error_status_get(long var0, Error var2);

    public static final native void Error_title_set(long var0, Error var2, String var3);

    public static final native String Error_title_get(long var0, Error var2);

    public static final native void Error_reason_set(long var0, Error var2, String var3);

    public static final native String Error_reason_get(long var0, Error var2);

    public static final native void Error_srcFile_set(long var0, Error var2, String var3);

    public static final native String Error_srcFile_get(long var0, Error var2);

    public static final native void Error_srcLine_set(long var0, Error var2, int var3);

    public static final native int Error_srcLine_get(long var0, Error var2);

    public static final native String Error_info__SWIG_0(long var0, Error var2, boolean var3);

    public static final native String Error_info__SWIG_1(long var0, Error var2);

    public static final native long new_Error__SWIG_0();

    public static final native long new_Error__SWIG_1(int var0, String var1, String var2, String var3, int var4);

    public static final native void delete_Error(long var0);

    public static final native void Version_major_set(long var0, Version var2, int var3);

    public static final native int Version_major_get(long var0, Version var2);

    public static final native void Version_minor_set(long var0, Version var2, int var3);

    public static final native int Version_minor_get(long var0, Version var2);

    public static final native void Version_rev_set(long var0, Version var2, int var3);

    public static final native int Version_rev_get(long var0, Version var2);

    public static final native void Version_suffix_set(long var0, Version var2, String var3);

    public static final native String Version_suffix_get(long var0, Version var2);

    public static final native void Version_full_set(long var0, Version var2, String var3);

    public static final native String Version_full_get(long var0, Version var2);

    public static final native void Version_numeric_set(long var0, Version var2, long var3);

    public static final native long Version_numeric_get(long var0, Version var2);

    public static final native long new_Version();

    public static final native void delete_Version(long var0);

    public static final native void TimeVal_sec_set(long var0, TimeVal var2, int var3);

    public static final native int TimeVal_sec_get(long var0, TimeVal var2);

    public static final native void TimeVal_msec_set(long var0, TimeVal var2, int var3);

    public static final native int TimeVal_msec_get(long var0, TimeVal var2);

    public static final native long new_TimeVal();

    public static final native void delete_TimeVal(long var0);

    public static final native void delete_PersistentObject(long var0);

    public static final native void PersistentObject_readObject(long var0, PersistentObject var2, long var3, ContainerNode var5) throws Exception;

    public static final native void PersistentObject_writeObject(long var0, PersistentObject var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_PersistentDocument(long var0);

    public static final native void PersistentDocument_loadFile(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native void PersistentDocument_loadString(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native void PersistentDocument_saveFile(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native String PersistentDocument_saveString(long var0, PersistentDocument var2) throws Exception;

    public static final native long PersistentDocument_getRootContainer(long var0, PersistentDocument var2);

    public static final native boolean PersistentDocument_hasUnread(long var0, PersistentDocument var2);

    public static final native String PersistentDocument_unreadName(long var0, PersistentDocument var2) throws Exception;

    public static final native int PersistentDocument_readInt__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native int PersistentDocument_readInt__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native float PersistentDocument_readNumber__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native float PersistentDocument_readNumber__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native boolean PersistentDocument_readBool__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native boolean PersistentDocument_readBool__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native String PersistentDocument_readString__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native String PersistentDocument_readString__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native long PersistentDocument_readStringVector__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native long PersistentDocument_readStringVector__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native void PersistentDocument_readObject(long var0, PersistentDocument var2, long var3, PersistentObject var5) throws Exception;

    public static final native long PersistentDocument_readContainer__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native long PersistentDocument_readContainer__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native long PersistentDocument_readArray__SWIG_0(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native long PersistentDocument_readArray__SWIG_1(long var0, PersistentDocument var2) throws Exception;

    public static final native void PersistentDocument_writeNumber(long var0, PersistentDocument var2, String var3, float var4) throws Exception;

    public static final native void PersistentDocument_writeInt(long var0, PersistentDocument var2, String var3, int var4) throws Exception;

    public static final native void PersistentDocument_writeBool(long var0, PersistentDocument var2, String var3, boolean var4) throws Exception;

    public static final native void PersistentDocument_writeString(long var0, PersistentDocument var2, String var3, String var4) throws Exception;

    public static final native void PersistentDocument_writeStringVector(long var0, PersistentDocument var2, String var3, long var4, StringVector var6) throws Exception;

    public static final native void PersistentDocument_writeObject(long var0, PersistentDocument var2, long var3, PersistentObject var5) throws Exception;

    public static final native long PersistentDocument_writeNewContainer(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native long PersistentDocument_writeNewArray(long var0, PersistentDocument var2, String var3) throws Exception;

    public static final native boolean ContainerNode_hasUnread(long var0, ContainerNode var2);

    public static final native String ContainerNode_unreadName(long var0, ContainerNode var2) throws Exception;

    public static final native int ContainerNode_readInt__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native int ContainerNode_readInt__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native float ContainerNode_readNumber__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native float ContainerNode_readNumber__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native boolean ContainerNode_readBool__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native boolean ContainerNode_readBool__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native String ContainerNode_readString__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native String ContainerNode_readString__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native long ContainerNode_readStringVector__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native long ContainerNode_readStringVector__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native void ContainerNode_readObject(long var0, ContainerNode var2, long var3, PersistentObject var5) throws Exception;

    public static final native long ContainerNode_readContainer__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native long ContainerNode_readContainer__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native long ContainerNode_readArray__SWIG_0(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native long ContainerNode_readArray__SWIG_1(long var0, ContainerNode var2) throws Exception;

    public static final native void ContainerNode_writeNumber(long var0, ContainerNode var2, String var3, float var4) throws Exception;

    public static final native void ContainerNode_writeInt(long var0, ContainerNode var2, String var3, int var4) throws Exception;

    public static final native void ContainerNode_writeBool(long var0, ContainerNode var2, String var3, boolean var4) throws Exception;

    public static final native void ContainerNode_writeString(long var0, ContainerNode var2, String var3, String var4) throws Exception;

    public static final native void ContainerNode_writeStringVector(long var0, ContainerNode var2, String var3, long var4, StringVector var6) throws Exception;

    public static final native void ContainerNode_writeObject(long var0, ContainerNode var2, long var3, PersistentObject var5) throws Exception;

    public static final native long ContainerNode_writeNewContainer(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native long ContainerNode_writeNewArray(long var0, ContainerNode var2, String var3) throws Exception;

    public static final native long new_ContainerNode();

    public static final native void delete_ContainerNode(long var0);

    public static final native void AuthCredInfo_scheme_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_scheme_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_realm_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_realm_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_username_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_username_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_dataType_set(long var0, AuthCredInfo var2, int var3);

    public static final native int AuthCredInfo_dataType_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_data_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_data_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_akaK_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_akaK_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_akaOp_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_akaOp_get(long var0, AuthCredInfo var2);

    public static final native void AuthCredInfo_akaAmf_set(long var0, AuthCredInfo var2, String var3);

    public static final native String AuthCredInfo_akaAmf_get(long var0, AuthCredInfo var2);

    public static final native long new_AuthCredInfo__SWIG_0();

    public static final native long new_AuthCredInfo__SWIG_1(String var0, String var1, String var2, int var3, String var4);

    public static final native void AuthCredInfo_readObject(long var0, AuthCredInfo var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AuthCredInfo_writeObject(long var0, AuthCredInfo var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_AuthCredInfo(long var0);

    public static final native void TlsConfig_CaListFile_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_CaListFile_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_certFile_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_certFile_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_privKeyFile_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_privKeyFile_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_password_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_password_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_CaBuf_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_CaBuf_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_certBuf_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_certBuf_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_privKeyBuf_set(long var0, TlsConfig var2, String var3);

    public static final native String TlsConfig_privKeyBuf_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_method_set(long var0, TlsConfig var2, int var3);

    public static final native int TlsConfig_method_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_proto_set(long var0, TlsConfig var2, long var3);

    public static final native long TlsConfig_proto_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_ciphers_set(long var0, TlsConfig var2, long var3, IntVector var5);

    public static final native long TlsConfig_ciphers_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_verifyServer_set(long var0, TlsConfig var2, boolean var3);

    public static final native boolean TlsConfig_verifyServer_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_verifyClient_set(long var0, TlsConfig var2, boolean var3);

    public static final native boolean TlsConfig_verifyClient_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_requireClientCert_set(long var0, TlsConfig var2, boolean var3);

    public static final native boolean TlsConfig_requireClientCert_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_msecTimeout_set(long var0, TlsConfig var2, long var3);

    public static final native long TlsConfig_msecTimeout_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_qosType_set(long var0, TlsConfig var2, int var3);

    public static final native int TlsConfig_qosType_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_qosParams_set(long var0, TlsConfig var2, long var3, pj_qos_params var5);

    public static final native long TlsConfig_qosParams_get(long var0, TlsConfig var2);

    public static final native void TlsConfig_qosIgnoreError_set(long var0, TlsConfig var2, boolean var3);

    public static final native boolean TlsConfig_qosIgnoreError_get(long var0, TlsConfig var2);

    public static final native long new_TlsConfig();

    public static final native void TlsConfig_readObject(long var0, TlsConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void TlsConfig_writeObject(long var0, TlsConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_TlsConfig(long var0);

    public static final native void TransportConfig_port_set(long var0, TransportConfig var2, long var3);

    public static final native long TransportConfig_port_get(long var0, TransportConfig var2);

    public static final native void TransportConfig_portRange_set(long var0, TransportConfig var2, long var3);

    public static final native long TransportConfig_portRange_get(long var0, TransportConfig var2);

    public static final native void TransportConfig_publicAddress_set(long var0, TransportConfig var2, String var3);

    public static final native String TransportConfig_publicAddress_get(long var0, TransportConfig var2);

    public static final native void TransportConfig_boundAddress_set(long var0, TransportConfig var2, String var3);

    public static final native String TransportConfig_boundAddress_get(long var0, TransportConfig var2);

    public static final native void TransportConfig_tlsConfig_set(long var0, TransportConfig var2, long var3, TlsConfig var5);

    public static final native long TransportConfig_tlsConfig_get(long var0, TransportConfig var2);

    public static final native void TransportConfig_qosType_set(long var0, TransportConfig var2, int var3);

    public static final native int TransportConfig_qosType_get(long var0, TransportConfig var2);

    public static final native void TransportConfig_qosParams_set(long var0, TransportConfig var2, long var3, pj_qos_params var5);

    public static final native long TransportConfig_qosParams_get(long var0, TransportConfig var2);

    public static final native long new_TransportConfig();

    public static final native void TransportConfig_readObject(long var0, TransportConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void TransportConfig_writeObject(long var0, TransportConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_TransportConfig(long var0);

    public static final native void TransportInfo_id_set(long var0, TransportInfo var2, int var3);

    public static final native int TransportInfo_id_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_type_set(long var0, TransportInfo var2, int var3);

    public static final native int TransportInfo_type_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_typeName_set(long var0, TransportInfo var2, String var3);

    public static final native String TransportInfo_typeName_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_info_set(long var0, TransportInfo var2, String var3);

    public static final native String TransportInfo_info_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_flags_set(long var0, TransportInfo var2, long var3);

    public static final native long TransportInfo_flags_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_localAddress_set(long var0, TransportInfo var2, String var3);

    public static final native String TransportInfo_localAddress_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_localName_set(long var0, TransportInfo var2, String var3);

    public static final native String TransportInfo_localName_get(long var0, TransportInfo var2);

    public static final native void TransportInfo_usageCount_set(long var0, TransportInfo var2, long var3);

    public static final native long TransportInfo_usageCount_get(long var0, TransportInfo var2);

    public static final native long new_TransportInfo();

    public static final native void delete_TransportInfo(long var0);

    public static final native void SipRxData_info_set(long var0, SipRxData var2, String var3);

    public static final native String SipRxData_info_get(long var0, SipRxData var2);

    public static final native void SipRxData_wholeMsg_set(long var0, SipRxData var2, String var3);

    public static final native String SipRxData_wholeMsg_get(long var0, SipRxData var2);

    public static final native void SipRxData_srcAddress_set(long var0, SipRxData var2, String var3);

    public static final native String SipRxData_srcAddress_get(long var0, SipRxData var2);

    public static final native void SipRxData_pjRxData_set(long var0, SipRxData var2, long var3);

    public static final native long SipRxData_pjRxData_get(long var0, SipRxData var2);

    public static final native long new_SipRxData();

    public static final native void delete_SipRxData(long var0);

    public static final native void SipTxData_info_set(long var0, SipTxData var2, String var3);

    public static final native String SipTxData_info_get(long var0, SipTxData var2);

    public static final native void SipTxData_wholeMsg_set(long var0, SipTxData var2, String var3);

    public static final native String SipTxData_wholeMsg_get(long var0, SipTxData var2);

    public static final native void SipTxData_dstAddress_set(long var0, SipTxData var2, String var3);

    public static final native String SipTxData_dstAddress_get(long var0, SipTxData var2);

    public static final native void SipTxData_pjTxData_set(long var0, SipTxData var2, long var3);

    public static final native long SipTxData_pjTxData_get(long var0, SipTxData var2);

    public static final native long new_SipTxData();

    public static final native void delete_SipTxData(long var0);

    public static final native void SipTransaction_role_set(long var0, SipTransaction var2, int var3);

    public static final native int SipTransaction_role_get(long var0, SipTransaction var2);

    public static final native void SipTransaction_method_set(long var0, SipTransaction var2, String var3);

    public static final native String SipTransaction_method_get(long var0, SipTransaction var2);

    public static final native void SipTransaction_statusCode_set(long var0, SipTransaction var2, int var3);

    public static final native int SipTransaction_statusCode_get(long var0, SipTransaction var2);

    public static final native void SipTransaction_statusText_set(long var0, SipTransaction var2, String var3);

    public static final native String SipTransaction_statusText_get(long var0, SipTransaction var2);

    public static final native void SipTransaction_state_set(long var0, SipTransaction var2, int var3);

    public static final native int SipTransaction_state_get(long var0, SipTransaction var2);

    public static final native void SipTransaction_lastTx_set(long var0, SipTransaction var2, long var3, SipTxData var5);

    public static final native long SipTransaction_lastTx_get(long var0, SipTransaction var2);

    public static final native void SipTransaction_pjTransaction_set(long var0, SipTransaction var2, long var3);

    public static final native long SipTransaction_pjTransaction_get(long var0, SipTransaction var2);

    public static final native long new_SipTransaction();

    public static final native void delete_SipTransaction(long var0);

    public static final native void TimerEvent_entry_set(long var0, TimerEvent var2, long var3);

    public static final native long TimerEvent_entry_get(long var0, TimerEvent var2);

    public static final native long new_TimerEvent();

    public static final native void delete_TimerEvent(long var0);

    public static final native void TsxStateEventSrc_rdata_set(long var0, TsxStateEventSrc var2, long var3, SipRxData var5);

    public static final native long TsxStateEventSrc_rdata_get(long var0, TsxStateEventSrc var2);

    public static final native void TsxStateEventSrc_tdata_set(long var0, TsxStateEventSrc var2, long var3, SipTxData var5);

    public static final native long TsxStateEventSrc_tdata_get(long var0, TsxStateEventSrc var2);

    public static final native void TsxStateEventSrc_timer_set(long var0, TsxStateEventSrc var2, long var3);

    public static final native long TsxStateEventSrc_timer_get(long var0, TsxStateEventSrc var2);

    public static final native void TsxStateEventSrc_status_set(long var0, TsxStateEventSrc var2, int var3);

    public static final native int TsxStateEventSrc_status_get(long var0, TsxStateEventSrc var2);

    public static final native void TsxStateEventSrc_data_set(long var0, TsxStateEventSrc var2, long var3);

    public static final native long TsxStateEventSrc_data_get(long var0, TsxStateEventSrc var2);

    public static final native long new_TsxStateEventSrc();

    public static final native void delete_TsxStateEventSrc(long var0);

    public static final native void TsxStateEvent_src_set(long var0, TsxStateEvent var2, long var3, TsxStateEventSrc var5);

    public static final native long TsxStateEvent_src_get(long var0, TsxStateEvent var2);

    public static final native void TsxStateEvent_tsx_set(long var0, TsxStateEvent var2, long var3, SipTransaction var5);

    public static final native long TsxStateEvent_tsx_get(long var0, TsxStateEvent var2);

    public static final native void TsxStateEvent_prevState_set(long var0, TsxStateEvent var2, int var3);

    public static final native int TsxStateEvent_prevState_get(long var0, TsxStateEvent var2);

    public static final native void TsxStateEvent_type_set(long var0, TsxStateEvent var2, int var3);

    public static final native int TsxStateEvent_type_get(long var0, TsxStateEvent var2);

    public static final native long new_TsxStateEvent();

    public static final native void delete_TsxStateEvent(long var0);

    public static final native void TxMsgEvent_tdata_set(long var0, TxMsgEvent var2, long var3, SipTxData var5);

    public static final native long TxMsgEvent_tdata_get(long var0, TxMsgEvent var2);

    public static final native long new_TxMsgEvent();

    public static final native void delete_TxMsgEvent(long var0);

    public static final native void TxErrorEvent_tdata_set(long var0, TxErrorEvent var2, long var3, SipTxData var5);

    public static final native long TxErrorEvent_tdata_get(long var0, TxErrorEvent var2);

    public static final native void TxErrorEvent_tsx_set(long var0, TxErrorEvent var2, long var3, SipTransaction var5);

    public static final native long TxErrorEvent_tsx_get(long var0, TxErrorEvent var2);

    public static final native long new_TxErrorEvent();

    public static final native void delete_TxErrorEvent(long var0);

    public static final native void RxMsgEvent_rdata_set(long var0, RxMsgEvent var2, long var3, SipRxData var5);

    public static final native long RxMsgEvent_rdata_get(long var0, RxMsgEvent var2);

    public static final native long new_RxMsgEvent();

    public static final native void delete_RxMsgEvent(long var0);

    public static final native void UserEvent_user1_set(long var0, UserEvent var2, long var3);

    public static final native long UserEvent_user1_get(long var0, UserEvent var2);

    public static final native void UserEvent_user2_set(long var0, UserEvent var2, long var3);

    public static final native long UserEvent_user2_get(long var0, UserEvent var2);

    public static final native void UserEvent_user3_set(long var0, UserEvent var2, long var3);

    public static final native long UserEvent_user3_get(long var0, UserEvent var2);

    public static final native void UserEvent_user4_set(long var0, UserEvent var2, long var3);

    public static final native long UserEvent_user4_get(long var0, UserEvent var2);

    public static final native long new_UserEvent();

    public static final native void delete_UserEvent(long var0);

    public static final native void SipEventBody_timer_set(long var0, SipEventBody var2, long var3, TimerEvent var5);

    public static final native long SipEventBody_timer_get(long var0, SipEventBody var2);

    public static final native void SipEventBody_tsxState_set(long var0, SipEventBody var2, long var3, TsxStateEvent var5);

    public static final native long SipEventBody_tsxState_get(long var0, SipEventBody var2);

    public static final native void SipEventBody_txMsg_set(long var0, SipEventBody var2, long var3, TxMsgEvent var5);

    public static final native long SipEventBody_txMsg_get(long var0, SipEventBody var2);

    public static final native void SipEventBody_txError_set(long var0, SipEventBody var2, long var3, TxErrorEvent var5);

    public static final native long SipEventBody_txError_get(long var0, SipEventBody var2);

    public static final native void SipEventBody_rxMsg_set(long var0, SipEventBody var2, long var3, RxMsgEvent var5);

    public static final native long SipEventBody_rxMsg_get(long var0, SipEventBody var2);

    public static final native void SipEventBody_user_set(long var0, SipEventBody var2, long var3, UserEvent var5);

    public static final native long SipEventBody_user_get(long var0, SipEventBody var2);

    public static final native long new_SipEventBody();

    public static final native void delete_SipEventBody(long var0);

    public static final native void SipEvent_type_set(long var0, SipEvent var2, int var3);

    public static final native int SipEvent_type_get(long var0, SipEvent var2);

    public static final native void SipEvent_body_set(long var0, SipEvent var2, long var3, SipEventBody var5);

    public static final native long SipEvent_body_get(long var0, SipEvent var2);

    public static final native void SipEvent_pjEvent_set(long var0, SipEvent var2, long var3);

    public static final native long SipEvent_pjEvent_get(long var0, SipEvent var2);

    public static final native long new_SipEvent();

    public static final native void delete_SipEvent(long var0);

    public static final native void SipMediaType_type_set(long var0, SipMediaType var2, String var3);

    public static final native String SipMediaType_type_get(long var0, SipMediaType var2);

    public static final native void SipMediaType_subType_set(long var0, SipMediaType var2, String var3);

    public static final native String SipMediaType_subType_get(long var0, SipMediaType var2);

    public static final native long new_SipMediaType();

    public static final native void delete_SipMediaType(long var0);

    public static final native void SipHeader_hName_set(long var0, SipHeader var2, String var3);

    public static final native String SipHeader_hName_get(long var0, SipHeader var2);

    public static final native void SipHeader_hValue_set(long var0, SipHeader var2, String var3);

    public static final native String SipHeader_hValue_get(long var0, SipHeader var2);

    public static final native long new_SipHeader();

    public static final native void delete_SipHeader(long var0);

    public static final native void SipMultipartPart_headers_set(long var0, SipMultipartPart var2, long var3, SipHeaderVector var5);

    public static final native long SipMultipartPart_headers_get(long var0, SipMultipartPart var2);

    public static final native void SipMultipartPart_contentType_set(long var0, SipMultipartPart var2, long var3, SipMediaType var5);

    public static final native long SipMultipartPart_contentType_get(long var0, SipMultipartPart var2);

    public static final native void SipMultipartPart_body_set(long var0, SipMultipartPart var2, String var3);

    public static final native String SipMultipartPart_body_get(long var0, SipMultipartPart var2);

    public static final native long new_SipMultipartPart();

    public static final native void delete_SipMultipartPart(long var0);

    public static final native void SipTxOption_targetUri_set(long var0, SipTxOption var2, String var3);

    public static final native String SipTxOption_targetUri_get(long var0, SipTxOption var2);

    public static final native void SipTxOption_headers_set(long var0, SipTxOption var2, long var3, SipHeaderVector var5);

    public static final native long SipTxOption_headers_get(long var0, SipTxOption var2);

    public static final native void SipTxOption_contentType_set(long var0, SipTxOption var2, String var3);

    public static final native String SipTxOption_contentType_get(long var0, SipTxOption var2);

    public static final native void SipTxOption_msgBody_set(long var0, SipTxOption var2, String var3);

    public static final native String SipTxOption_msgBody_get(long var0, SipTxOption var2);

    public static final native void SipTxOption_multipartContentType_set(long var0, SipTxOption var2, long var3, SipMediaType var5);

    public static final native long SipTxOption_multipartContentType_get(long var0, SipTxOption var2);

    public static final native void SipTxOption_multipartParts_set(long var0, SipTxOption var2, long var3, SipMultipartPartVector var5);

    public static final native long SipTxOption_multipartParts_get(long var0, SipTxOption var2);

    public static final native boolean SipTxOption_isEmpty(long var0, SipTxOption var2);

    public static final native long new_SipTxOption();

    public static final native void delete_SipTxOption(long var0);

    public static final native void SendInstantMessageParam_contentType_set(long var0, SendInstantMessageParam var2, String var3);

    public static final native String SendInstantMessageParam_contentType_get(long var0, SendInstantMessageParam var2);

    public static final native void SendInstantMessageParam_content_set(long var0, SendInstantMessageParam var2, String var3);

    public static final native String SendInstantMessageParam_content_get(long var0, SendInstantMessageParam var2);

    public static final native void SendInstantMessageParam_txOption_set(long var0, SendInstantMessageParam var2, long var3, SipTxOption var5);

    public static final native long SendInstantMessageParam_txOption_get(long var0, SendInstantMessageParam var2);

    public static final native void SendInstantMessageParam_userData_set(long var0, SendInstantMessageParam var2, long var3);

    public static final native long SendInstantMessageParam_userData_get(long var0, SendInstantMessageParam var2);

    public static final native long new_SendInstantMessageParam();

    public static final native void delete_SendInstantMessageParam(long var0);

    public static final native void SendTypingIndicationParam_isTyping_set(long var0, SendTypingIndicationParam var2, boolean var3);

    public static final native boolean SendTypingIndicationParam_isTyping_get(long var0, SendTypingIndicationParam var2);

    public static final native void SendTypingIndicationParam_txOption_set(long var0, SendTypingIndicationParam var2, long var3, SipTxOption var5);

    public static final native long SendTypingIndicationParam_txOption_get(long var0, SendTypingIndicationParam var2);

    public static final native long new_SendTypingIndicationParam();

    public static final native void delete_SendTypingIndicationParam(long var0);

    public static final native long new_SipHeaderVector__SWIG_0();

    public static final native long new_SipHeaderVector__SWIG_1(long var0);

    public static final native long SipHeaderVector_size(long var0, SipHeaderVector var2);

    public static final native long SipHeaderVector_capacity(long var0, SipHeaderVector var2);

    public static final native void SipHeaderVector_reserve(long var0, SipHeaderVector var2, long var3);

    public static final native boolean SipHeaderVector_isEmpty(long var0, SipHeaderVector var2);

    public static final native void SipHeaderVector_clear(long var0, SipHeaderVector var2);

    public static final native void SipHeaderVector_add(long var0, SipHeaderVector var2, long var3, SipHeader var5);

    public static final native long SipHeaderVector_get(long var0, SipHeaderVector var2, int var3);

    public static final native void SipHeaderVector_set(long var0, SipHeaderVector var2, int var3, long var4, SipHeader var6);

    public static final native void delete_SipHeaderVector(long var0);

    public static final native long new_AuthCredInfoVector__SWIG_0();

    public static final native long new_AuthCredInfoVector__SWIG_1(long var0);

    public static final native long AuthCredInfoVector_size(long var0, AuthCredInfoVector var2);

    public static final native long AuthCredInfoVector_capacity(long var0, AuthCredInfoVector var2);

    public static final native void AuthCredInfoVector_reserve(long var0, AuthCredInfoVector var2, long var3);

    public static final native boolean AuthCredInfoVector_isEmpty(long var0, AuthCredInfoVector var2);

    public static final native void AuthCredInfoVector_clear(long var0, AuthCredInfoVector var2);

    public static final native void AuthCredInfoVector_add(long var0, AuthCredInfoVector var2, long var3, AuthCredInfo var5);

    public static final native long AuthCredInfoVector_get(long var0, AuthCredInfoVector var2, int var3);

    public static final native void AuthCredInfoVector_set(long var0, AuthCredInfoVector var2, int var3, long var4, AuthCredInfo var6);

    public static final native void delete_AuthCredInfoVector(long var0);

    public static final native long new_SrtpCryptoVector__SWIG_0();

    public static final native long new_SrtpCryptoVector__SWIG_1(long var0);

    public static final native long SrtpCryptoVector_size(long var0, SrtpCryptoVector var2);

    public static final native long SrtpCryptoVector_capacity(long var0, SrtpCryptoVector var2);

    public static final native void SrtpCryptoVector_reserve(long var0, SrtpCryptoVector var2, long var3);

    public static final native boolean SrtpCryptoVector_isEmpty(long var0, SrtpCryptoVector var2);

    public static final native void SrtpCryptoVector_clear(long var0, SrtpCryptoVector var2);

    public static final native void SrtpCryptoVector_add(long var0, SrtpCryptoVector var2, long var3, SrtpCrypto var5);

    public static final native long SrtpCryptoVector_get(long var0, SrtpCryptoVector var2, int var3);

    public static final native void SrtpCryptoVector_set(long var0, SrtpCryptoVector var2, int var3, long var4, SrtpCrypto var6);

    public static final native void delete_SrtpCryptoVector(long var0);

    public static final native long new_SipMultipartPartVector__SWIG_0();

    public static final native long new_SipMultipartPartVector__SWIG_1(long var0);

    public static final native long SipMultipartPartVector_size(long var0, SipMultipartPartVector var2);

    public static final native long SipMultipartPartVector_capacity(long var0, SipMultipartPartVector var2);

    public static final native void SipMultipartPartVector_reserve(long var0, SipMultipartPartVector var2, long var3);

    public static final native boolean SipMultipartPartVector_isEmpty(long var0, SipMultipartPartVector var2);

    public static final native void SipMultipartPartVector_clear(long var0, SipMultipartPartVector var2);

    public static final native void SipMultipartPartVector_add(long var0, SipMultipartPartVector var2, long var3, SipMultipartPart var5);

    public static final native long SipMultipartPartVector_get(long var0, SipMultipartPartVector var2, int var3);

    public static final native void SipMultipartPartVector_set(long var0, SipMultipartPartVector var2, int var3, long var4, SipMultipartPart var6);

    public static final native void delete_SipMultipartPartVector(long var0);

    public static final native long new_BuddyVector__SWIG_0();

    public static final native long new_BuddyVector__SWIG_1(long var0);

    public static final native long BuddyVector_size(long var0, BuddyVector var2);

    public static final native long BuddyVector_capacity(long var0, BuddyVector var2);

    public static final native void BuddyVector_reserve(long var0, BuddyVector var2, long var3);

    public static final native boolean BuddyVector_isEmpty(long var0, BuddyVector var2);

    public static final native void BuddyVector_clear(long var0, BuddyVector var2);

    public static final native void BuddyVector_add(long var0, BuddyVector var2, long var3, Buddy var5);

    public static final native long BuddyVector_get(long var0, BuddyVector var2, int var3);

    public static final native void BuddyVector_set(long var0, BuddyVector var2, int var3, long var4, Buddy var6);

    public static final native void delete_BuddyVector(long var0);

    public static final native long new_BuddyVector2__SWIG_0();

    public static final native long new_BuddyVector2__SWIG_1(long var0);

    public static final native long BuddyVector2_size(long var0, BuddyVector2 var2);

    public static final native long BuddyVector2_capacity(long var0, BuddyVector2 var2);

    public static final native void BuddyVector2_reserve(long var0, BuddyVector2 var2, long var3);

    public static final native boolean BuddyVector2_isEmpty(long var0, BuddyVector2 var2);

    public static final native void BuddyVector2_clear(long var0, BuddyVector2 var2);

    public static final native void BuddyVector2_add(long var0, BuddyVector2 var2, long var3, Buddy var5);

    public static final native long BuddyVector2_get(long var0, BuddyVector2 var2, int var3);

    public static final native void BuddyVector2_set(long var0, BuddyVector2 var2, int var3, long var4, Buddy var6);

    public static final native void delete_BuddyVector2(long var0);

    public static final native long new_AudioMediaVector__SWIG_0();

    public static final native long new_AudioMediaVector__SWIG_1(long var0);

    public static final native long AudioMediaVector_size(long var0, AudioMediaVector var2);

    public static final native long AudioMediaVector_capacity(long var0, AudioMediaVector var2);

    public static final native void AudioMediaVector_reserve(long var0, AudioMediaVector var2, long var3);

    public static final native boolean AudioMediaVector_isEmpty(long var0, AudioMediaVector var2);

    public static final native void AudioMediaVector_clear(long var0, AudioMediaVector var2);

    public static final native void AudioMediaVector_add(long var0, AudioMediaVector var2, long var3, AudioMedia var5);

    public static final native long AudioMediaVector_get(long var0, AudioMediaVector var2, int var3);

    public static final native void AudioMediaVector_set(long var0, AudioMediaVector var2, int var3, long var4, AudioMedia var6);

    public static final native void delete_AudioMediaVector(long var0);

    public static final native long new_AudioMediaVector2__SWIG_0();

    public static final native long new_AudioMediaVector2__SWIG_1(long var0);

    public static final native long AudioMediaVector2_size(long var0, AudioMediaVector2 var2);

    public static final native long AudioMediaVector2_capacity(long var0, AudioMediaVector2 var2);

    public static final native void AudioMediaVector2_reserve(long var0, AudioMediaVector2 var2, long var3);

    public static final native boolean AudioMediaVector2_isEmpty(long var0, AudioMediaVector2 var2);

    public static final native void AudioMediaVector2_clear(long var0, AudioMediaVector2 var2);

    public static final native void AudioMediaVector2_add(long var0, AudioMediaVector2 var2, long var3, AudioMedia var5);

    public static final native long AudioMediaVector2_get(long var0, AudioMediaVector2 var2, int var3);

    public static final native void AudioMediaVector2_set(long var0, AudioMediaVector2 var2, int var3, long var4, AudioMedia var6);

    public static final native void delete_AudioMediaVector2(long var0);

    public static final native long new_VideoMediaVector__SWIG_0();

    public static final native long new_VideoMediaVector__SWIG_1(long var0);

    public static final native long VideoMediaVector_size(long var0, VideoMediaVector var2);

    public static final native long VideoMediaVector_capacity(long var0, VideoMediaVector var2);

    public static final native void VideoMediaVector_reserve(long var0, VideoMediaVector var2, long var3);

    public static final native boolean VideoMediaVector_isEmpty(long var0, VideoMediaVector var2);

    public static final native void VideoMediaVector_clear(long var0, VideoMediaVector var2);

    public static final native void VideoMediaVector_add(long var0, VideoMediaVector var2, long var3, VideoMedia var5);

    public static final native long VideoMediaVector_get(long var0, VideoMediaVector var2, int var3);

    public static final native void VideoMediaVector_set(long var0, VideoMediaVector var2, int var3, long var4, VideoMedia var6);

    public static final native void delete_VideoMediaVector(long var0);

    public static final native long new_ToneDescVector__SWIG_0();

    public static final native long new_ToneDescVector__SWIG_1(long var0);

    public static final native long ToneDescVector_size(long var0, ToneDescVector var2);

    public static final native long ToneDescVector_capacity(long var0, ToneDescVector var2);

    public static final native void ToneDescVector_reserve(long var0, ToneDescVector var2, long var3);

    public static final native boolean ToneDescVector_isEmpty(long var0, ToneDescVector var2);

    public static final native void ToneDescVector_clear(long var0, ToneDescVector var2);

    public static final native void ToneDescVector_add(long var0, ToneDescVector var2, long var3, ToneDesc var5);

    public static final native long ToneDescVector_get(long var0, ToneDescVector var2, int var3);

    public static final native void ToneDescVector_set(long var0, ToneDescVector var2, int var3, long var4, ToneDesc var6);

    public static final native void delete_ToneDescVector(long var0);

    public static final native long new_ToneDigitVector__SWIG_0();

    public static final native long new_ToneDigitVector__SWIG_1(long var0);

    public static final native long ToneDigitVector_size(long var0, ToneDigitVector var2);

    public static final native long ToneDigitVector_capacity(long var0, ToneDigitVector var2);

    public static final native void ToneDigitVector_reserve(long var0, ToneDigitVector var2, long var3);

    public static final native boolean ToneDigitVector_isEmpty(long var0, ToneDigitVector var2);

    public static final native void ToneDigitVector_clear(long var0, ToneDigitVector var2);

    public static final native void ToneDigitVector_add(long var0, ToneDigitVector var2, long var3, ToneDigit var5);

    public static final native long ToneDigitVector_get(long var0, ToneDigitVector var2, int var3);

    public static final native void ToneDigitVector_set(long var0, ToneDigitVector var2, int var3, long var4, ToneDigit var6);

    public static final native void delete_ToneDigitVector(long var0);

    public static final native long new_ToneDigitMapVector__SWIG_0();

    public static final native long new_ToneDigitMapVector__SWIG_1(long var0);

    public static final native long ToneDigitMapVector_size(long var0, ToneDigitMapVector var2);

    public static final native long ToneDigitMapVector_capacity(long var0, ToneDigitMapVector var2);

    public static final native void ToneDigitMapVector_reserve(long var0, ToneDigitMapVector var2, long var3);

    public static final native boolean ToneDigitMapVector_isEmpty(long var0, ToneDigitMapVector var2);

    public static final native void ToneDigitMapVector_clear(long var0, ToneDigitMapVector var2);

    public static final native void ToneDigitMapVector_add(long var0, ToneDigitMapVector var2, long var3, ToneDigitMapDigit var5);

    public static final native long ToneDigitMapVector_get(long var0, ToneDigitMapVector var2, int var3);

    public static final native void ToneDigitMapVector_set(long var0, ToneDigitMapVector var2, int var3, long var4, ToneDigitMapDigit var6);

    public static final native void delete_ToneDigitMapVector(long var0);

    public static final native long new_AudioDevInfoVector__SWIG_0();

    public static final native long new_AudioDevInfoVector__SWIG_1(long var0);

    public static final native long AudioDevInfoVector_size(long var0, AudioDevInfoVector var2);

    public static final native long AudioDevInfoVector_capacity(long var0, AudioDevInfoVector var2);

    public static final native void AudioDevInfoVector_reserve(long var0, AudioDevInfoVector var2, long var3);

    public static final native boolean AudioDevInfoVector_isEmpty(long var0, AudioDevInfoVector var2);

    public static final native void AudioDevInfoVector_clear(long var0, AudioDevInfoVector var2);

    public static final native void AudioDevInfoVector_add(long var0, AudioDevInfoVector var2, long var3, AudioDevInfo var5);

    public static final native long AudioDevInfoVector_get(long var0, AudioDevInfoVector var2, int var3);

    public static final native void AudioDevInfoVector_set(long var0, AudioDevInfoVector var2, int var3, long var4, AudioDevInfo var6);

    public static final native void delete_AudioDevInfoVector(long var0);

    public static final native long new_AudioDevInfoVector2__SWIG_0();

    public static final native long new_AudioDevInfoVector2__SWIG_1(long var0);

    public static final native long AudioDevInfoVector2_size(long var0, AudioDevInfoVector2 var2);

    public static final native long AudioDevInfoVector2_capacity(long var0, AudioDevInfoVector2 var2);

    public static final native void AudioDevInfoVector2_reserve(long var0, AudioDevInfoVector2 var2, long var3);

    public static final native boolean AudioDevInfoVector2_isEmpty(long var0, AudioDevInfoVector2 var2);

    public static final native void AudioDevInfoVector2_clear(long var0, AudioDevInfoVector2 var2);

    public static final native void AudioDevInfoVector2_add(long var0, AudioDevInfoVector2 var2, long var3, AudioDevInfo var5);

    public static final native long AudioDevInfoVector2_get(long var0, AudioDevInfoVector2 var2, int var3);

    public static final native void AudioDevInfoVector2_set(long var0, AudioDevInfoVector2 var2, int var3, long var4, AudioDevInfo var6);

    public static final native void delete_AudioDevInfoVector2(long var0);

    public static final native long new_CodecInfoVector__SWIG_0();

    public static final native long new_CodecInfoVector__SWIG_1(long var0);

    public static final native long CodecInfoVector_size(long var0, CodecInfoVector var2);

    public static final native long CodecInfoVector_capacity(long var0, CodecInfoVector var2);

    public static final native void CodecInfoVector_reserve(long var0, CodecInfoVector var2, long var3);

    public static final native boolean CodecInfoVector_isEmpty(long var0, CodecInfoVector var2);

    public static final native void CodecInfoVector_clear(long var0, CodecInfoVector var2);

    public static final native void CodecInfoVector_add(long var0, CodecInfoVector var2, long var3, CodecInfo var5);

    public static final native long CodecInfoVector_get(long var0, CodecInfoVector var2, int var3);

    public static final native void CodecInfoVector_set(long var0, CodecInfoVector var2, int var3, long var4, CodecInfo var6);

    public static final native void delete_CodecInfoVector(long var0);

    public static final native long new_CodecInfoVector2__SWIG_0();

    public static final native long new_CodecInfoVector2__SWIG_1(long var0);

    public static final native long CodecInfoVector2_size(long var0, CodecInfoVector2 var2);

    public static final native long CodecInfoVector2_capacity(long var0, CodecInfoVector2 var2);

    public static final native void CodecInfoVector2_reserve(long var0, CodecInfoVector2 var2, long var3);

    public static final native boolean CodecInfoVector2_isEmpty(long var0, CodecInfoVector2 var2);

    public static final native void CodecInfoVector2_clear(long var0, CodecInfoVector2 var2);

    public static final native void CodecInfoVector2_add(long var0, CodecInfoVector2 var2, long var3, CodecInfo var5);

    public static final native long CodecInfoVector2_get(long var0, CodecInfoVector2 var2, int var3);

    public static final native void CodecInfoVector2_set(long var0, CodecInfoVector2 var2, int var3, long var4, CodecInfo var6);

    public static final native void delete_CodecInfoVector2(long var0);

    public static final native long new_VideoDevInfoVector__SWIG_0();

    public static final native long new_VideoDevInfoVector__SWIG_1(long var0);

    public static final native long VideoDevInfoVector_size(long var0, VideoDevInfoVector var2);

    public static final native long VideoDevInfoVector_capacity(long var0, VideoDevInfoVector var2);

    public static final native void VideoDevInfoVector_reserve(long var0, VideoDevInfoVector var2, long var3);

    public static final native boolean VideoDevInfoVector_isEmpty(long var0, VideoDevInfoVector var2);

    public static final native void VideoDevInfoVector_clear(long var0, VideoDevInfoVector var2);

    public static final native void VideoDevInfoVector_add(long var0, VideoDevInfoVector var2, long var3, VideoDevInfo var5);

    public static final native long VideoDevInfoVector_get(long var0, VideoDevInfoVector var2, int var3);

    public static final native void VideoDevInfoVector_set(long var0, VideoDevInfoVector var2, int var3, long var4, VideoDevInfo var6);

    public static final native void delete_VideoDevInfoVector(long var0);

    public static final native long new_VideoDevInfoVector2__SWIG_0();

    public static final native long new_VideoDevInfoVector2__SWIG_1(long var0);

    public static final native long VideoDevInfoVector2_size(long var0, VideoDevInfoVector2 var2);

    public static final native long VideoDevInfoVector2_capacity(long var0, VideoDevInfoVector2 var2);

    public static final native void VideoDevInfoVector2_reserve(long var0, VideoDevInfoVector2 var2, long var3);

    public static final native boolean VideoDevInfoVector2_isEmpty(long var0, VideoDevInfoVector2 var2);

    public static final native void VideoDevInfoVector2_clear(long var0, VideoDevInfoVector2 var2);

    public static final native void VideoDevInfoVector2_add(long var0, VideoDevInfoVector2 var2, long var3, VideoDevInfo var5);

    public static final native long VideoDevInfoVector2_get(long var0, VideoDevInfoVector2 var2, int var3);

    public static final native void VideoDevInfoVector2_set(long var0, VideoDevInfoVector2 var2, int var3, long var4, VideoDevInfo var6);

    public static final native void delete_VideoDevInfoVector2(long var0);

    public static final native long new_CodecFmtpVector__SWIG_0();

    public static final native long new_CodecFmtpVector__SWIG_1(long var0);

    public static final native long CodecFmtpVector_size(long var0, CodecFmtpVector var2);

    public static final native long CodecFmtpVector_capacity(long var0, CodecFmtpVector var2);

    public static final native void CodecFmtpVector_reserve(long var0, CodecFmtpVector var2, long var3);

    public static final native boolean CodecFmtpVector_isEmpty(long var0, CodecFmtpVector var2);

    public static final native void CodecFmtpVector_clear(long var0, CodecFmtpVector var2);

    public static final native void CodecFmtpVector_add(long var0, CodecFmtpVector var2, long var3, CodecFmtp var5);

    public static final native long CodecFmtpVector_get(long var0, CodecFmtpVector var2, int var3);

    public static final native void CodecFmtpVector_set(long var0, CodecFmtpVector var2, int var3, long var4, CodecFmtp var6);

    public static final native void delete_CodecFmtpVector(long var0);

    public static final native long new_MediaFormatAudioVector__SWIG_0();

    public static final native long new_MediaFormatAudioVector__SWIG_1(long var0);

    public static final native long MediaFormatAudioVector_size(long var0, MediaFormatAudioVector var2);

    public static final native long MediaFormatAudioVector_capacity(long var0, MediaFormatAudioVector var2);

    public static final native void MediaFormatAudioVector_reserve(long var0, MediaFormatAudioVector var2, long var3);

    public static final native boolean MediaFormatAudioVector_isEmpty(long var0, MediaFormatAudioVector var2);

    public static final native void MediaFormatAudioVector_clear(long var0, MediaFormatAudioVector var2);

    public static final native void MediaFormatAudioVector_add(long var0, MediaFormatAudioVector var2, long var3, MediaFormatAudio var5);

    public static final native long MediaFormatAudioVector_get(long var0, MediaFormatAudioVector var2, int var3);

    public static final native void MediaFormatAudioVector_set(long var0, MediaFormatAudioVector var2, int var3, long var4, MediaFormatAudio var6);

    public static final native void delete_MediaFormatAudioVector(long var0);

    public static final native long new_MediaFormatVideoVector__SWIG_0();

    public static final native long new_MediaFormatVideoVector__SWIG_1(long var0);

    public static final native long MediaFormatVideoVector_size(long var0, MediaFormatVideoVector var2);

    public static final native long MediaFormatVideoVector_capacity(long var0, MediaFormatVideoVector var2);

    public static final native void MediaFormatVideoVector_reserve(long var0, MediaFormatVideoVector var2, long var3);

    public static final native boolean MediaFormatVideoVector_isEmpty(long var0, MediaFormatVideoVector var2);

    public static final native void MediaFormatVideoVector_clear(long var0, MediaFormatVideoVector var2);

    public static final native void MediaFormatVideoVector_add(long var0, MediaFormatVideoVector var2, long var3, MediaFormatVideo var5);

    public static final native long MediaFormatVideoVector_get(long var0, MediaFormatVideoVector var2, int var3);

    public static final native void MediaFormatVideoVector_set(long var0, MediaFormatVideoVector var2, int var3, long var4, MediaFormatVideo var6);

    public static final native void delete_MediaFormatVideoVector(long var0);

    public static final native long new_CallMediaInfoVector__SWIG_0();

    public static final native long new_CallMediaInfoVector__SWIG_1(long var0);

    public static final native long CallMediaInfoVector_size(long var0, CallMediaInfoVector var2);

    public static final native long CallMediaInfoVector_capacity(long var0, CallMediaInfoVector var2);

    public static final native void CallMediaInfoVector_reserve(long var0, CallMediaInfoVector var2, long var3);

    public static final native boolean CallMediaInfoVector_isEmpty(long var0, CallMediaInfoVector var2);

    public static final native void CallMediaInfoVector_clear(long var0, CallMediaInfoVector var2);

    public static final native void CallMediaInfoVector_add(long var0, CallMediaInfoVector var2, long var3, CallMediaInfo var5);

    public static final native long CallMediaInfoVector_get(long var0, CallMediaInfoVector var2, int var3);

    public static final native void CallMediaInfoVector_set(long var0, CallMediaInfoVector var2, int var3, long var4, CallMediaInfo var6);

    public static final native void delete_CallMediaInfoVector(long var0);

    public static final native long new_RtcpFbCapVector__SWIG_0();

    public static final native long new_RtcpFbCapVector__SWIG_1(long var0);

    public static final native long RtcpFbCapVector_size(long var0, RtcpFbCapVector var2);

    public static final native long RtcpFbCapVector_capacity(long var0, RtcpFbCapVector var2);

    public static final native void RtcpFbCapVector_reserve(long var0, RtcpFbCapVector var2, long var3);

    public static final native boolean RtcpFbCapVector_isEmpty(long var0, RtcpFbCapVector var2);

    public static final native void RtcpFbCapVector_clear(long var0, RtcpFbCapVector var2);

    public static final native void RtcpFbCapVector_add(long var0, RtcpFbCapVector var2, long var3, RtcpFbCap var5);

    public static final native long RtcpFbCapVector_get(long var0, RtcpFbCapVector var2, int var3);

    public static final native void RtcpFbCapVector_set(long var0, RtcpFbCapVector var2, int var3, long var4, RtcpFbCap var6);

    public static final native void delete_RtcpFbCapVector(long var0);

    public static final native long new_SslCertNameVector__SWIG_0();

    public static final native long new_SslCertNameVector__SWIG_1(long var0);

    public static final native long SslCertNameVector_size(long var0, SslCertNameVector var2);

    public static final native long SslCertNameVector_capacity(long var0, SslCertNameVector var2);

    public static final native void SslCertNameVector_reserve(long var0, SslCertNameVector var2, long var3);

    public static final native boolean SslCertNameVector_isEmpty(long var0, SslCertNameVector var2);

    public static final native void SslCertNameVector_clear(long var0, SslCertNameVector var2);

    public static final native void SslCertNameVector_add(long var0, SslCertNameVector var2, long var3, SslCertName var5);

    public static final native long SslCertNameVector_get(long var0, SslCertNameVector var2, int var3);

    public static final native void SslCertNameVector_set(long var0, SslCertNameVector var2, int var3, long var4, SslCertName var6);

    public static final native void delete_SslCertNameVector(long var0);

    public static final native void MediaFormat_id_set(long var0, MediaFormat var2, long var3);

    public static final native long MediaFormat_id_get(long var0, MediaFormat var2);

    public static final native void MediaFormat_type_set(long var0, MediaFormat var2, int var3);

    public static final native int MediaFormat_type_get(long var0, MediaFormat var2);

    public static final native long new_MediaFormat();

    public static final native void delete_MediaFormat(long var0);

    public static final native void MediaFormatAudio_clockRate_set(long var0, MediaFormatAudio var2, long var3);

    public static final native long MediaFormatAudio_clockRate_get(long var0, MediaFormatAudio var2);

    public static final native void MediaFormatAudio_channelCount_set(long var0, MediaFormatAudio var2, long var3);

    public static final native long MediaFormatAudio_channelCount_get(long var0, MediaFormatAudio var2);

    public static final native void MediaFormatAudio_frameTimeUsec_set(long var0, MediaFormatAudio var2, long var3);

    public static final native long MediaFormatAudio_frameTimeUsec_get(long var0, MediaFormatAudio var2);

    public static final native void MediaFormatAudio_bitsPerSample_set(long var0, MediaFormatAudio var2, long var3);

    public static final native long MediaFormatAudio_bitsPerSample_get(long var0, MediaFormatAudio var2);

    public static final native void MediaFormatAudio_avgBps_set(long var0, MediaFormatAudio var2, long var3);

    public static final native long MediaFormatAudio_avgBps_get(long var0, MediaFormatAudio var2);

    public static final native void MediaFormatAudio_maxBps_set(long var0, MediaFormatAudio var2, long var3);

    public static final native long MediaFormatAudio_maxBps_get(long var0, MediaFormatAudio var2);

    public static final native long new_MediaFormatAudio();

    public static final native void delete_MediaFormatAudio(long var0);

    public static final native void MediaFormatVideo_width_set(long var0, MediaFormatVideo var2, long var3);

    public static final native long MediaFormatVideo_width_get(long var0, MediaFormatVideo var2);

    public static final native void MediaFormatVideo_height_set(long var0, MediaFormatVideo var2, long var3);

    public static final native long MediaFormatVideo_height_get(long var0, MediaFormatVideo var2);

    public static final native void MediaFormatVideo_fpsNum_set(long var0, MediaFormatVideo var2, int var3);

    public static final native int MediaFormatVideo_fpsNum_get(long var0, MediaFormatVideo var2);

    public static final native void MediaFormatVideo_fpsDenum_set(long var0, MediaFormatVideo var2, int var3);

    public static final native int MediaFormatVideo_fpsDenum_get(long var0, MediaFormatVideo var2);

    public static final native void MediaFormatVideo_avgBps_set(long var0, MediaFormatVideo var2, long var3);

    public static final native long MediaFormatVideo_avgBps_get(long var0, MediaFormatVideo var2);

    public static final native void MediaFormatVideo_maxBps_set(long var0, MediaFormatVideo var2, long var3);

    public static final native long MediaFormatVideo_maxBps_get(long var0, MediaFormatVideo var2);

    public static final native long new_MediaFormatVideo();

    public static final native void delete_MediaFormatVideo(long var0);

    public static final native void ConfPortInfo_portId_set(long var0, ConfPortInfo var2, int var3);

    public static final native int ConfPortInfo_portId_get(long var0, ConfPortInfo var2);

    public static final native void ConfPortInfo_name_set(long var0, ConfPortInfo var2, String var3);

    public static final native String ConfPortInfo_name_get(long var0, ConfPortInfo var2);

    public static final native void ConfPortInfo_format_set(long var0, ConfPortInfo var2, long var3, MediaFormatAudio var5);

    public static final native long ConfPortInfo_format_get(long var0, ConfPortInfo var2);

    public static final native void ConfPortInfo_txLevelAdj_set(long var0, ConfPortInfo var2, float var3);

    public static final native float ConfPortInfo_txLevelAdj_get(long var0, ConfPortInfo var2);

    public static final native void ConfPortInfo_rxLevelAdj_set(long var0, ConfPortInfo var2, float var3);

    public static final native float ConfPortInfo_rxLevelAdj_get(long var0, ConfPortInfo var2);

    public static final native void ConfPortInfo_listeners_set(long var0, ConfPortInfo var2, long var3, IntVector var5);

    public static final native long ConfPortInfo_listeners_get(long var0, ConfPortInfo var2);

    public static final native long new_ConfPortInfo();

    public static final native void delete_ConfPortInfo(long var0);

    public static final native void delete_Media(long var0);

    public static final native int Media_getType(long var0, Media var2);

    public static final native void AudioMediaTransmitParam_level_set(long var0, AudioMediaTransmitParam var2, float var3);

    public static final native float AudioMediaTransmitParam_level_get(long var0, AudioMediaTransmitParam var2);

    public static final native long new_AudioMediaTransmitParam();

    public static final native void delete_AudioMediaTransmitParam(long var0);

    public static final native long AudioMedia_getPortInfo(long var0, AudioMedia var2) throws Exception;

    public static final native int AudioMedia_getPortId(long var0, AudioMedia var2);

    public static final native long AudioMedia_getPortInfoFromId(int var0) throws Exception;

    public static final native void AudioMedia_startTransmit(long var0, AudioMedia var2, long var3, AudioMedia var5) throws Exception;

    public static final native void AudioMedia_startTransmit2(long var0, AudioMedia var2, long var3, AudioMedia var5, long var6, AudioMediaTransmitParam var8) throws Exception;

    public static final native void AudioMedia_stopTransmit(long var0, AudioMedia var2, long var3, AudioMedia var5) throws Exception;

    public static final native void AudioMedia_adjustRxLevel(long var0, AudioMedia var2, float var3) throws Exception;

    public static final native void AudioMedia_adjustTxLevel(long var0, AudioMedia var2, float var3) throws Exception;

    public static final native long AudioMedia_getRxLevel(long var0, AudioMedia var2) throws Exception;

    public static final native long AudioMedia_getTxLevel(long var0, AudioMedia var2) throws Exception;

    public static final native long AudioMedia_typecastFromMedia(long var0, Media var2);

    public static final native long new_AudioMedia();

    public static final native void delete_AudioMedia(long var0);

    public static final native void AudioMediaPlayerInfo_formatId_set(long var0, AudioMediaPlayerInfo var2, int var3);

    public static final native int AudioMediaPlayerInfo_formatId_get(long var0, AudioMediaPlayerInfo var2);

    public static final native void AudioMediaPlayerInfo_payloadBitsPerSample_set(long var0, AudioMediaPlayerInfo var2, long var3);

    public static final native long AudioMediaPlayerInfo_payloadBitsPerSample_get(long var0, AudioMediaPlayerInfo var2);

    public static final native void AudioMediaPlayerInfo_sizeBytes_set(long var0, AudioMediaPlayerInfo var2, long var3);

    public static final native long AudioMediaPlayerInfo_sizeBytes_get(long var0, AudioMediaPlayerInfo var2);

    public static final native void AudioMediaPlayerInfo_sizeSamples_set(long var0, AudioMediaPlayerInfo var2, long var3);

    public static final native long AudioMediaPlayerInfo_sizeSamples_get(long var0, AudioMediaPlayerInfo var2);

    public static final native long new_AudioMediaPlayerInfo();

    public static final native void delete_AudioMediaPlayerInfo(long var0);

    public static final native long new_AudioMediaPlayer();

    public static final native void AudioMediaPlayer_createPlayer__SWIG_0(long var0, AudioMediaPlayer var2, String var3, long var4) throws Exception;

    public static final native void AudioMediaPlayer_createPlayer__SWIG_1(long var0, AudioMediaPlayer var2, String var3) throws Exception;

    public static final native void AudioMediaPlayer_createPlaylist__SWIG_0(long var0, AudioMediaPlayer var2, long var3, StringVector var5, String var6, long var7) throws Exception;

    public static final native void AudioMediaPlayer_createPlaylist__SWIG_1(long var0, AudioMediaPlayer var2, long var3, StringVector var5, String var6) throws Exception;

    public static final native void AudioMediaPlayer_createPlaylist__SWIG_2(long var0, AudioMediaPlayer var2, long var3, StringVector var5) throws Exception;

    public static final native long AudioMediaPlayer_getInfo(long var0, AudioMediaPlayer var2) throws Exception;

    public static final native long AudioMediaPlayer_getPos(long var0, AudioMediaPlayer var2) throws Exception;

    public static final native void AudioMediaPlayer_setPos(long var0, AudioMediaPlayer var2, long var3) throws Exception;

    public static final native long AudioMediaPlayer_typecastFromAudioMedia(long var0, AudioMedia var2);

    public static final native void delete_AudioMediaPlayer(long var0);

    public static final native void AudioMediaPlayer_onEof2(long var0, AudioMediaPlayer var2);

    public static final native void AudioMediaPlayer_onEof2SwigExplicitAudioMediaPlayer(long var0, AudioMediaPlayer var2);

    public static final native void AudioMediaPlayer_director_connect(AudioMediaPlayer var0, long var1, boolean var3, boolean var4);

    public static final native void AudioMediaPlayer_change_ownership(AudioMediaPlayer var0, long var1, boolean var3);

    public static final native long new_AudioMediaRecorder();

    public static final native void AudioMediaRecorder_createRecorder__SWIG_0(long var0, AudioMediaRecorder var2, String var3, long var4, int var6, long var7) throws Exception;

    public static final native void AudioMediaRecorder_createRecorder__SWIG_1(long var0, AudioMediaRecorder var2, String var3, long var4, int var6) throws Exception;

    public static final native void AudioMediaRecorder_createRecorder__SWIG_2(long var0, AudioMediaRecorder var2, String var3, long var4) throws Exception;

    public static final native void AudioMediaRecorder_createRecorder__SWIG_3(long var0, AudioMediaRecorder var2, String var3) throws Exception;

    public static final native long AudioMediaRecorder_typecastFromAudioMedia(long var0, AudioMedia var2);

    public static final native void delete_AudioMediaRecorder(long var0);

    public static final native long new_ToneDesc();

    public static final native void delete_ToneDesc(long var0);

    public static final native long new_ToneDigit();

    public static final native void delete_ToneDigit(long var0);

    public static final native void ToneDigitMapDigit_digit_set(long var0, ToneDigitMapDigit var2, String var3);

    public static final native String ToneDigitMapDigit_digit_get(long var0, ToneDigitMapDigit var2);

    public static final native void ToneDigitMapDigit_freq1_set(long var0, ToneDigitMapDigit var2, int var3);

    public static final native int ToneDigitMapDigit_freq1_get(long var0, ToneDigitMapDigit var2);

    public static final native void ToneDigitMapDigit_freq2_set(long var0, ToneDigitMapDigit var2, int var3);

    public static final native int ToneDigitMapDigit_freq2_get(long var0, ToneDigitMapDigit var2);

    public static final native long new_ToneDigitMapDigit();

    public static final native void delete_ToneDigitMapDigit(long var0);

    public static final native long new_ToneGenerator();

    public static final native void delete_ToneGenerator(long var0);

    public static final native void ToneGenerator_createToneGenerator__SWIG_0(long var0, ToneGenerator var2, long var3, long var5) throws Exception;

    public static final native void ToneGenerator_createToneGenerator__SWIG_1(long var0, ToneGenerator var2, long var3) throws Exception;

    public static final native void ToneGenerator_createToneGenerator__SWIG_2(long var0, ToneGenerator var2) throws Exception;

    public static final native boolean ToneGenerator_isBusy(long var0, ToneGenerator var2);

    public static final native void ToneGenerator_stop(long var0, ToneGenerator var2) throws Exception;

    public static final native void ToneGenerator_rewind(long var0, ToneGenerator var2) throws Exception;

    public static final native void ToneGenerator_play__SWIG_0(long var0, ToneGenerator var2, long var3, ToneDescVector var5, boolean var6) throws Exception;

    public static final native void ToneGenerator_play__SWIG_1(long var0, ToneGenerator var2, long var3, ToneDescVector var5) throws Exception;

    public static final native void ToneGenerator_playDigits__SWIG_0(long var0, ToneGenerator var2, long var3, ToneDigitVector var5, boolean var6) throws Exception;

    public static final native void ToneGenerator_playDigits__SWIG_1(long var0, ToneGenerator var2, long var3, ToneDigitVector var5) throws Exception;

    public static final native long ToneGenerator_getDigitMap(long var0, ToneGenerator var2) throws Exception;

    public static final native void ToneGenerator_setDigitMap(long var0, ToneGenerator var2, long var3, ToneDigitMapVector var5) throws Exception;

    public static final native void AudioDevInfo_name_set(long var0, AudioDevInfo var2, String var3);

    public static final native String AudioDevInfo_name_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_inputCount_set(long var0, AudioDevInfo var2, long var3);

    public static final native long AudioDevInfo_inputCount_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_outputCount_set(long var0, AudioDevInfo var2, long var3);

    public static final native long AudioDevInfo_outputCount_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_defaultSamplesPerSec_set(long var0, AudioDevInfo var2, long var3);

    public static final native long AudioDevInfo_defaultSamplesPerSec_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_driver_set(long var0, AudioDevInfo var2, String var3);

    public static final native String AudioDevInfo_driver_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_caps_set(long var0, AudioDevInfo var2, long var3);

    public static final native long AudioDevInfo_caps_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_routes_set(long var0, AudioDevInfo var2, long var3);

    public static final native long AudioDevInfo_routes_get(long var0, AudioDevInfo var2);

    public static final native void AudioDevInfo_extFmt_set(long var0, AudioDevInfo var2, long var3, MediaFormatAudioVector var5);

    public static final native long AudioDevInfo_extFmt_get(long var0, AudioDevInfo var2);

    public static final native void delete_AudioDevInfo(long var0);

    public static final native long new_AudioDevInfo();

    public static final native int AudDevManager_getCaptureDev(long var0, AudDevManager var2) throws Exception;

    public static final native long AudDevManager_getCaptureDevMedia(long var0, AudDevManager var2) throws Exception;

    public static final native int AudDevManager_getPlaybackDev(long var0, AudDevManager var2) throws Exception;

    public static final native long AudDevManager_getPlaybackDevMedia(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setCaptureDev(long var0, AudDevManager var2, int var3) throws Exception;

    public static final native void AudDevManager_setPlaybackDev(long var0, AudDevManager var2, int var3) throws Exception;

    public static final native long AudDevManager_enumDev2(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setNullDev(long var0, AudDevManager var2) throws Exception;

    public static final native long AudDevManager_setNoDev(long var0, AudDevManager var2);

    public static final native void AudDevManager_setSndDevMode(long var0, AudDevManager var2, long var3) throws Exception;

    public static final native void AudDevManager_setEcOptions(long var0, AudDevManager var2, long var3, long var5) throws Exception;

    public static final native long AudDevManager_getEcTail(long var0, AudDevManager var2) throws Exception;

    public static final native boolean AudDevManager_sndIsActive(long var0, AudDevManager var2);

    public static final native void AudDevManager_refreshDevs(long var0, AudDevManager var2) throws Exception;

    public static final native long AudDevManager_getDevCount(long var0, AudDevManager var2);

    public static final native long AudDevManager_getDevInfo(long var0, AudDevManager var2, int var3) throws Exception;

    public static final native int AudDevManager_lookupDev(long var0, AudDevManager var2, String var3, String var4) throws Exception;

    public static final native String AudDevManager_capName(long var0, AudDevManager var2, int var3);

    public static final native void AudDevManager_setExtFormat__SWIG_0(long var0, AudDevManager var2, long var3, MediaFormatAudio var5, boolean var6) throws Exception;

    public static final native void AudDevManager_setExtFormat__SWIG_1(long var0, AudDevManager var2, long var3, MediaFormatAudio var5) throws Exception;

    public static final native long AudDevManager_getExtFormat(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setInputLatency__SWIG_0(long var0, AudDevManager var2, long var3, boolean var5) throws Exception;

    public static final native void AudDevManager_setInputLatency__SWIG_1(long var0, AudDevManager var2, long var3) throws Exception;

    public static final native long AudDevManager_getInputLatency(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setOutputLatency__SWIG_0(long var0, AudDevManager var2, long var3, boolean var5) throws Exception;

    public static final native void AudDevManager_setOutputLatency__SWIG_1(long var0, AudDevManager var2, long var3) throws Exception;

    public static final native long AudDevManager_getOutputLatency(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setInputVolume__SWIG_0(long var0, AudDevManager var2, long var3, boolean var5) throws Exception;

    public static final native void AudDevManager_setInputVolume__SWIG_1(long var0, AudDevManager var2, long var3) throws Exception;

    public static final native long AudDevManager_getInputVolume(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setOutputVolume__SWIG_0(long var0, AudDevManager var2, long var3, boolean var5) throws Exception;

    public static final native void AudDevManager_setOutputVolume__SWIG_1(long var0, AudDevManager var2, long var3) throws Exception;

    public static final native long AudDevManager_getOutputVolume(long var0, AudDevManager var2) throws Exception;

    public static final native long AudDevManager_getInputSignal(long var0, AudDevManager var2) throws Exception;

    public static final native long AudDevManager_getOutputSignal(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setInputRoute__SWIG_0(long var0, AudDevManager var2, int var3, boolean var4) throws Exception;

    public static final native void AudDevManager_setInputRoute__SWIG_1(long var0, AudDevManager var2, int var3) throws Exception;

    public static final native int AudDevManager_getInputRoute(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setOutputRoute__SWIG_0(long var0, AudDevManager var2, int var3, boolean var4) throws Exception;

    public static final native void AudDevManager_setOutputRoute__SWIG_1(long var0, AudDevManager var2, int var3) throws Exception;

    public static final native int AudDevManager_getOutputRoute(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setVad__SWIG_0(long var0, AudDevManager var2, boolean var3, boolean var4) throws Exception;

    public static final native void AudDevManager_setVad__SWIG_1(long var0, AudDevManager var2, boolean var3) throws Exception;

    public static final native boolean AudDevManager_getVad(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setCng__SWIG_0(long var0, AudDevManager var2, boolean var3, boolean var4) throws Exception;

    public static final native void AudDevManager_setCng__SWIG_1(long var0, AudDevManager var2, boolean var3) throws Exception;

    public static final native boolean AudDevManager_getCng(long var0, AudDevManager var2) throws Exception;

    public static final native void AudDevManager_setPlc__SWIG_0(long var0, AudDevManager var2, boolean var3, boolean var4) throws Exception;

    public static final native void AudDevManager_setPlc__SWIG_1(long var0, AudDevManager var2, boolean var3) throws Exception;

    public static final native boolean AudDevManager_getPlc(long var0, AudDevManager var2) throws Exception;

    public static final native long new_ExtraAudioDevice(int var0, int var1);

    public static final native void delete_ExtraAudioDevice(long var0);

    public static final native void ExtraAudioDevice_open(long var0, ExtraAudioDevice var2);

    public static final native void ExtraAudioDevice_close(long var0, ExtraAudioDevice var2);

    public static final native boolean ExtraAudioDevice_isOpened(long var0, ExtraAudioDevice var2);

    public static final native void MediaCoordinate_x_set(long var0, MediaCoordinate var2, int var3);

    public static final native int MediaCoordinate_x_get(long var0, MediaCoordinate var2);

    public static final native void MediaCoordinate_y_set(long var0, MediaCoordinate var2, int var3);

    public static final native int MediaCoordinate_y_get(long var0, MediaCoordinate var2);

    public static final native long new_MediaCoordinate();

    public static final native void delete_MediaCoordinate(long var0);

    public static final native void MediaSize_w_set(long var0, MediaSize var2, long var3);

    public static final native long MediaSize_w_get(long var0, MediaSize var2);

    public static final native void MediaSize_h_set(long var0, MediaSize var2, long var3);

    public static final native long MediaSize_h_get(long var0, MediaSize var2);

    public static final native long new_MediaSize();

    public static final native void delete_MediaSize(long var0);

    public static final native void VidConfPortInfo_portId_set(long var0, VidConfPortInfo var2, int var3);

    public static final native int VidConfPortInfo_portId_get(long var0, VidConfPortInfo var2);

    public static final native void VidConfPortInfo_name_set(long var0, VidConfPortInfo var2, String var3);

    public static final native String VidConfPortInfo_name_get(long var0, VidConfPortInfo var2);

    public static final native void VidConfPortInfo_format_set(long var0, VidConfPortInfo var2, long var3, MediaFormatVideo var5);

    public static final native long VidConfPortInfo_format_get(long var0, VidConfPortInfo var2);

    public static final native void VidConfPortInfo_listeners_set(long var0, VidConfPortInfo var2, long var3, IntVector var5);

    public static final native long VidConfPortInfo_listeners_get(long var0, VidConfPortInfo var2);

    public static final native void VidConfPortInfo_transmitters_set(long var0, VidConfPortInfo var2, long var3, IntVector var5);

    public static final native long VidConfPortInfo_transmitters_get(long var0, VidConfPortInfo var2);

    public static final native long new_VidConfPortInfo();

    public static final native void delete_VidConfPortInfo(long var0);

    public static final native long new_VideoMediaTransmitParam();

    public static final native void delete_VideoMediaTransmitParam(long var0);

    public static final native long VideoMedia_getPortInfo(long var0, VideoMedia var2) throws Exception;

    public static final native int VideoMedia_getPortId(long var0, VideoMedia var2);

    public static final native long VideoMedia_getPortInfoFromId(int var0) throws Exception;

    public static final native void VideoMedia_startTransmit(long var0, VideoMedia var2, long var3, VideoMedia var5, long var6, VideoMediaTransmitParam var8) throws Exception;

    public static final native void VideoMedia_stopTransmit(long var0, VideoMedia var2, long var3, VideoMedia var5) throws Exception;

    public static final native long new_VideoMedia();

    public static final native void delete_VideoMedia(long var0);

    public static final native void WindowHandle_setWindow(long var0, WindowHandle var2, Object var3);

    public static final native long new_WindowHandle();

    public static final native void delete_WindowHandle(long var0);

    public static final native void VideoWindowHandle_type_set(long var0, VideoWindowHandle var2, long var3);

    public static final native long VideoWindowHandle_type_get(long var0, VideoWindowHandle var2);

    public static final native void VideoWindowHandle_handle_set(long var0, VideoWindowHandle var2, long var3, WindowHandle var5);

    public static final native long VideoWindowHandle_handle_get(long var0, VideoWindowHandle var2);

    public static final native long new_VideoWindowHandle();

    public static final native void delete_VideoWindowHandle(long var0);

    public static final native void VideoWindowInfo_isNative_set(long var0, VideoWindowInfo var2, boolean var3);

    public static final native boolean VideoWindowInfo_isNative_get(long var0, VideoWindowInfo var2);

    public static final native void VideoWindowInfo_winHandle_set(long var0, VideoWindowInfo var2, long var3, VideoWindowHandle var5);

    public static final native long VideoWindowInfo_winHandle_get(long var0, VideoWindowInfo var2);

    public static final native void VideoWindowInfo_renderDeviceId_set(long var0, VideoWindowInfo var2, int var3);

    public static final native int VideoWindowInfo_renderDeviceId_get(long var0, VideoWindowInfo var2);

    public static final native void VideoWindowInfo_show_set(long var0, VideoWindowInfo var2, boolean var3);

    public static final native boolean VideoWindowInfo_show_get(long var0, VideoWindowInfo var2);

    public static final native void VideoWindowInfo_pos_set(long var0, VideoWindowInfo var2, long var3, MediaCoordinate var5);

    public static final native long VideoWindowInfo_pos_get(long var0, VideoWindowInfo var2);

    public static final native void VideoWindowInfo_size_set(long var0, VideoWindowInfo var2, long var3, MediaSize var5);

    public static final native long VideoWindowInfo_size_get(long var0, VideoWindowInfo var2);

    public static final native long new_VideoWindowInfo();

    public static final native void delete_VideoWindowInfo(long var0);

    public static final native long new_VideoWindow(int var0);

    public static final native long VideoWindow_getInfo(long var0, VideoWindow var2) throws Exception;

    public static final native long VideoWindow_getVideoMedia(long var0, VideoWindow var2) throws Exception;

    public static final native void VideoWindow_Show(long var0, VideoWindow var2, boolean var3) throws Exception;

    public static final native void VideoWindow_setPos(long var0, VideoWindow var2, long var3, MediaCoordinate var5) throws Exception;

    public static final native void VideoWindow_setSize(long var0, VideoWindow var2, long var3, MediaSize var5) throws Exception;

    public static final native void VideoWindow_rotate(long var0, VideoWindow var2, int var3) throws Exception;

    public static final native void VideoWindow_setWindow(long var0, VideoWindow var2, long var3, VideoWindowHandle var5) throws Exception;

    public static final native void VideoWindow_setFullScreen(long var0, VideoWindow var2, boolean var3) throws Exception;

    public static final native void delete_VideoWindow(long var0);

    public static final native void VideoPreviewOpParam_rendId_set(long var0, VideoPreviewOpParam var2, int var3);

    public static final native int VideoPreviewOpParam_rendId_get(long var0, VideoPreviewOpParam var2);

    public static final native void VideoPreviewOpParam_show_set(long var0, VideoPreviewOpParam var2, boolean var3);

    public static final native boolean VideoPreviewOpParam_show_get(long var0, VideoPreviewOpParam var2);

    public static final native void VideoPreviewOpParam_windowFlags_set(long var0, VideoPreviewOpParam var2, long var3);

    public static final native long VideoPreviewOpParam_windowFlags_get(long var0, VideoPreviewOpParam var2);

    public static final native void VideoPreviewOpParam_format_set(long var0, VideoPreviewOpParam var2, long var3, MediaFormat var5);

    public static final native long VideoPreviewOpParam_format_get(long var0, VideoPreviewOpParam var2);

    public static final native void VideoPreviewOpParam_window_set(long var0, VideoPreviewOpParam var2, long var3, VideoWindowHandle var5);

    public static final native long VideoPreviewOpParam_window_get(long var0, VideoPreviewOpParam var2);

    public static final native long new_VideoPreviewOpParam();

    public static final native void delete_VideoPreviewOpParam(long var0);

    public static final native long new_VideoPreview(int var0);

    public static final native boolean VideoPreview_hasNative(long var0, VideoPreview var2);

    public static final native void VideoPreview_start(long var0, VideoPreview var2, long var3, VideoPreviewOpParam var5) throws Exception;

    public static final native void VideoPreview_stop(long var0, VideoPreview var2) throws Exception;

    public static final native long VideoPreview_getVideoWindow(long var0, VideoPreview var2);

    public static final native long VideoPreview_getVideoMedia(long var0, VideoPreview var2) throws Exception;

    public static final native void delete_VideoPreview(long var0);

    public static final native void VideoDevInfo_id_set(long var0, VideoDevInfo var2, int var3);

    public static final native int VideoDevInfo_id_get(long var0, VideoDevInfo var2);

    public static final native void VideoDevInfo_name_set(long var0, VideoDevInfo var2, String var3);

    public static final native String VideoDevInfo_name_get(long var0, VideoDevInfo var2);

    public static final native void VideoDevInfo_driver_set(long var0, VideoDevInfo var2, String var3);

    public static final native String VideoDevInfo_driver_get(long var0, VideoDevInfo var2);

    public static final native void VideoDevInfo_dir_set(long var0, VideoDevInfo var2, int var3);

    public static final native int VideoDevInfo_dir_get(long var0, VideoDevInfo var2);

    public static final native void VideoDevInfo_caps_set(long var0, VideoDevInfo var2, long var3);

    public static final native long VideoDevInfo_caps_get(long var0, VideoDevInfo var2);

    public static final native void VideoDevInfo_fmt_set(long var0, VideoDevInfo var2, long var3, MediaFormatVideoVector var5);

    public static final native long VideoDevInfo_fmt_get(long var0, VideoDevInfo var2);

    public static final native long new_VideoDevInfo();

    public static final native void delete_VideoDevInfo(long var0);

    public static final native void VideoSwitchParam_target_id_set(long var0, VideoSwitchParam var2, int var3);

    public static final native int VideoSwitchParam_target_id_get(long var0, VideoSwitchParam var2);

    public static final native long new_VideoSwitchParam();

    public static final native void delete_VideoSwitchParam(long var0);

    public static final native void VidDevManager_refreshDevs(long var0, VidDevManager var2) throws Exception;

    public static final native long VidDevManager_getDevCount(long var0, VidDevManager var2);

    public static final native long VidDevManager_getDevInfo(long var0, VidDevManager var2, int var3) throws Exception;

    public static final native long VidDevManager_enumDev2(long var0, VidDevManager var2) throws Exception;

    public static final native int VidDevManager_lookupDev(long var0, VidDevManager var2, String var3, String var4) throws Exception;

    public static final native String VidDevManager_capName(long var0, VidDevManager var2, int var3);

    public static final native void VidDevManager_setFormat(long var0, VidDevManager var2, int var3, long var4, MediaFormatVideo var6, boolean var7) throws Exception;

    public static final native long VidDevManager_getFormat(long var0, VidDevManager var2, int var3) throws Exception;

    public static final native void VidDevManager_setInputScale(long var0, VidDevManager var2, int var3, long var4, MediaSize var6, boolean var7) throws Exception;

    public static final native long VidDevManager_getInputScale(long var0, VidDevManager var2, int var3) throws Exception;

    public static final native void VidDevManager_setOutputWindowFlags(long var0, VidDevManager var2, int var3, int var4, boolean var5) throws Exception;

    public static final native int VidDevManager_getOutputWindowFlags(long var0, VidDevManager var2, int var3) throws Exception;

    public static final native void VidDevManager_switchDev(long var0, VidDevManager var2, int var3, long var4, VideoSwitchParam var6) throws Exception;

    public static final native boolean VidDevManager_isCaptureActive(long var0, VidDevManager var2, int var3);

    public static final native void VidDevManager_setCaptureOrient__SWIG_0(long var0, VidDevManager var2, int var3, int var4, boolean var5) throws Exception;

    public static final native void VidDevManager_setCaptureOrient__SWIG_1(long var0, VidDevManager var2, int var3, int var4) throws Exception;

    public static final native void CodecInfo_codecId_set(long var0, CodecInfo var2, String var3);

    public static final native String CodecInfo_codecId_get(long var0, CodecInfo var2);

    public static final native void CodecInfo_priority_set(long var0, CodecInfo var2, short var3);

    public static final native short CodecInfo_priority_get(long var0, CodecInfo var2);

    public static final native void CodecInfo_desc_set(long var0, CodecInfo var2, String var3);

    public static final native String CodecInfo_desc_get(long var0, CodecInfo var2);

    public static final native long new_CodecInfo();

    public static final native void delete_CodecInfo(long var0);

    public static final native void CodecFmtp_name_set(long var0, CodecFmtp var2, String var3);

    public static final native String CodecFmtp_name_get(long var0, CodecFmtp var2);

    public static final native void CodecFmtp_val_set(long var0, CodecFmtp var2, String var3);

    public static final native String CodecFmtp_val_get(long var0, CodecFmtp var2);

    public static final native long new_CodecFmtp();

    public static final native void delete_CodecFmtp(long var0);

    public static final native void CodecParamInfo_clockRate_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_clockRate_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_channelCnt_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_channelCnt_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_avgBps_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_avgBps_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_maxBps_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_maxBps_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_maxRxFrameSize_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_maxRxFrameSize_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_frameLen_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_frameLen_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_pcmBitsPerSample_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_pcmBitsPerSample_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_pt_set(long var0, CodecParamInfo var2, long var3);

    public static final native long CodecParamInfo_pt_get(long var0, CodecParamInfo var2);

    public static final native void CodecParamInfo_fmtId_set(long var0, CodecParamInfo var2, int var3);

    public static final native int CodecParamInfo_fmtId_get(long var0, CodecParamInfo var2);

    public static final native long new_CodecParamInfo();

    public static final native void delete_CodecParamInfo(long var0);

    public static final native void CodecParamSetting_frmPerPkt_set(long var0, CodecParamSetting var2, long var3);

    public static final native long CodecParamSetting_frmPerPkt_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_vad_set(long var0, CodecParamSetting var2, boolean var3);

    public static final native boolean CodecParamSetting_vad_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_cng_set(long var0, CodecParamSetting var2, boolean var3);

    public static final native boolean CodecParamSetting_cng_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_penh_set(long var0, CodecParamSetting var2, boolean var3);

    public static final native boolean CodecParamSetting_penh_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_plc_set(long var0, CodecParamSetting var2, boolean var3);

    public static final native boolean CodecParamSetting_plc_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_reserved_set(long var0, CodecParamSetting var2, boolean var3);

    public static final native boolean CodecParamSetting_reserved_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_encFmtp_set(long var0, CodecParamSetting var2, long var3, CodecFmtpVector var5);

    public static final native long CodecParamSetting_encFmtp_get(long var0, CodecParamSetting var2);

    public static final native void CodecParamSetting_decFmtp_set(long var0, CodecParamSetting var2, long var3, CodecFmtpVector var5);

    public static final native long CodecParamSetting_decFmtp_get(long var0, CodecParamSetting var2);

    public static final native long new_CodecParamSetting();

    public static final native void delete_CodecParamSetting(long var0);

    public static final native void CodecParam_info_set(long var0, CodecParam var2, long var3, CodecParamInfo var5);

    public static final native long CodecParam_info_get(long var0, CodecParam var2);

    public static final native void CodecParam_setting_set(long var0, CodecParam var2, long var3, CodecParamSetting var5);

    public static final native long CodecParam_setting_get(long var0, CodecParam var2);

    public static final native long new_CodecParam();

    public static final native void delete_CodecParam(long var0);

    public static final native void CodecOpusConfig_sample_rate_set(long var0, CodecOpusConfig var2, long var3);

    public static final native long CodecOpusConfig_sample_rate_get(long var0, CodecOpusConfig var2);

    public static final native void CodecOpusConfig_channel_cnt_set(long var0, CodecOpusConfig var2, long var3);

    public static final native long CodecOpusConfig_channel_cnt_get(long var0, CodecOpusConfig var2);

    public static final native void CodecOpusConfig_frm_ptime_set(long var0, CodecOpusConfig var2, long var3);

    public static final native long CodecOpusConfig_frm_ptime_get(long var0, CodecOpusConfig var2);

    public static final native void CodecOpusConfig_bit_rate_set(long var0, CodecOpusConfig var2, long var3);

    public static final native long CodecOpusConfig_bit_rate_get(long var0, CodecOpusConfig var2);

    public static final native void CodecOpusConfig_packet_loss_set(long var0, CodecOpusConfig var2, long var3);

    public static final native long CodecOpusConfig_packet_loss_get(long var0, CodecOpusConfig var2);

    public static final native void CodecOpusConfig_complexity_set(long var0, CodecOpusConfig var2, long var3);

    public static final native long CodecOpusConfig_complexity_get(long var0, CodecOpusConfig var2);

    public static final native void CodecOpusConfig_cbr_set(long var0, CodecOpusConfig var2, boolean var3);

    public static final native boolean CodecOpusConfig_cbr_get(long var0, CodecOpusConfig var2);

    public static final native long new_CodecOpusConfig();

    public static final native void delete_CodecOpusConfig(long var0);

    public static final native void VidCodecParam_dir_set(long var0, VidCodecParam var2, int var3);

    public static final native int VidCodecParam_dir_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_packing_set(long var0, VidCodecParam var2, int var3);

    public static final native int VidCodecParam_packing_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_encFmt_set(long var0, VidCodecParam var2, long var3, MediaFormatVideo var5);

    public static final native long VidCodecParam_encFmt_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_encFmtp_set(long var0, VidCodecParam var2, long var3, CodecFmtpVector var5);

    public static final native long VidCodecParam_encFmtp_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_encMtu_set(long var0, VidCodecParam var2, long var3);

    public static final native long VidCodecParam_encMtu_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_decFmt_set(long var0, VidCodecParam var2, long var3, MediaFormatVideo var5);

    public static final native long VidCodecParam_decFmt_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_decFmtp_set(long var0, VidCodecParam var2, long var3, CodecFmtpVector var5);

    public static final native long VidCodecParam_decFmtp_get(long var0, VidCodecParam var2);

    public static final native void VidCodecParam_ignoreFmtp_set(long var0, VidCodecParam var2, boolean var3);

    public static final native boolean VidCodecParam_ignoreFmtp_get(long var0, VidCodecParam var2);

    public static final native long new_VidCodecParam();

    public static final native void delete_VidCodecParam(long var0);

    public static final native void MediaFmtChangedEvent_newWidth_set(long var0, MediaFmtChangedEvent var2, long var3);

    public static final native long MediaFmtChangedEvent_newWidth_get(long var0, MediaFmtChangedEvent var2);

    public static final native void MediaFmtChangedEvent_newHeight_set(long var0, MediaFmtChangedEvent var2, long var3);

    public static final native long MediaFmtChangedEvent_newHeight_get(long var0, MediaFmtChangedEvent var2);

    public static final native long new_MediaFmtChangedEvent();

    public static final native void delete_MediaFmtChangedEvent(long var0);

    public static final native void AudDevErrorEvent_dir_set(long var0, AudDevErrorEvent var2, int var3);

    public static final native int AudDevErrorEvent_dir_get(long var0, AudDevErrorEvent var2);

    public static final native void AudDevErrorEvent_id_set(long var0, AudDevErrorEvent var2, int var3);

    public static final native int AudDevErrorEvent_id_get(long var0, AudDevErrorEvent var2);

    public static final native void AudDevErrorEvent_status_set(long var0, AudDevErrorEvent var2, int var3);

    public static final native int AudDevErrorEvent_status_get(long var0, AudDevErrorEvent var2);

    public static final native long new_AudDevErrorEvent();

    public static final native void delete_AudDevErrorEvent(long var0);

    public static final native void MediaEventData_fmtChanged_set(long var0, MediaEventData var2, long var3, MediaFmtChangedEvent var5);

    public static final native long MediaEventData_fmtChanged_get(long var0, MediaEventData var2);

    public static final native void MediaEventData_audDevError_set(long var0, MediaEventData var2, long var3, AudDevErrorEvent var5);

    public static final native long MediaEventData_audDevError_get(long var0, MediaEventData var2);

    public static final native void MediaEventData_ptr_set(long var0, MediaEventData var2, long var3);

    public static final native long MediaEventData_ptr_get(long var0, MediaEventData var2);

    public static final native long new_MediaEventData();

    public static final native void delete_MediaEventData(long var0);

    public static final native void MediaEvent_type_set(long var0, MediaEvent var2, int var3);

    public static final native int MediaEvent_type_get(long var0, MediaEvent var2);

    public static final native void MediaEvent_data_set(long var0, MediaEvent var2, long var3, MediaEventData var5);

    public static final native long MediaEvent_data_get(long var0, MediaEvent var2);

    public static final native void MediaEvent_pjMediaEvent_set(long var0, MediaEvent var2, long var3);

    public static final native long MediaEvent_pjMediaEvent_get(long var0, MediaEvent var2);

    public static final native long new_MediaEvent();

    public static final native void delete_MediaEvent(long var0);

    public static final native void PresenceStatus_status_set(long var0, PresenceStatus var2, int var3);

    public static final native int PresenceStatus_status_get(long var0, PresenceStatus var2);

    public static final native void PresenceStatus_statusText_set(long var0, PresenceStatus var2, String var3);

    public static final native String PresenceStatus_statusText_get(long var0, PresenceStatus var2);

    public static final native void PresenceStatus_activity_set(long var0, PresenceStatus var2, int var3);

    public static final native int PresenceStatus_activity_get(long var0, PresenceStatus var2);

    public static final native void PresenceStatus_note_set(long var0, PresenceStatus var2, String var3);

    public static final native String PresenceStatus_note_get(long var0, PresenceStatus var2);

    public static final native void PresenceStatus_rpidId_set(long var0, PresenceStatus var2, String var3);

    public static final native String PresenceStatus_rpidId_get(long var0, PresenceStatus var2);

    public static final native long new_PresenceStatus();

    public static final native void delete_PresenceStatus(long var0);

    public static final native void BuddyConfig_uri_set(long var0, BuddyConfig var2, String var3);

    public static final native String BuddyConfig_uri_get(long var0, BuddyConfig var2);

    public static final native void BuddyConfig_subscribe_set(long var0, BuddyConfig var2, boolean var3);

    public static final native boolean BuddyConfig_subscribe_get(long var0, BuddyConfig var2);

    public static final native void BuddyConfig_readObject(long var0, BuddyConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void BuddyConfig_writeObject(long var0, BuddyConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_BuddyConfig();

    public static final native void delete_BuddyConfig(long var0);

    public static final native void BuddyInfo_uri_set(long var0, BuddyInfo var2, String var3);

    public static final native String BuddyInfo_uri_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_contact_set(long var0, BuddyInfo var2, String var3);

    public static final native String BuddyInfo_contact_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_presMonitorEnabled_set(long var0, BuddyInfo var2, boolean var3);

    public static final native boolean BuddyInfo_presMonitorEnabled_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_subState_set(long var0, BuddyInfo var2, int var3);

    public static final native int BuddyInfo_subState_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_subStateName_set(long var0, BuddyInfo var2, String var3);

    public static final native String BuddyInfo_subStateName_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_subTermCode_set(long var0, BuddyInfo var2, int var3);

    public static final native int BuddyInfo_subTermCode_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_subTermReason_set(long var0, BuddyInfo var2, String var3);

    public static final native String BuddyInfo_subTermReason_get(long var0, BuddyInfo var2);

    public static final native void BuddyInfo_presStatus_set(long var0, BuddyInfo var2, long var3, PresenceStatus var5);

    public static final native long BuddyInfo_presStatus_get(long var0, BuddyInfo var2);

    public static final native long new_BuddyInfo();

    public static final native void delete_BuddyInfo(long var0);

    public static final native void OnBuddyEvSubStateParam_e_set(long var0, OnBuddyEvSubStateParam var2, long var3, SipEvent var5);

    public static final native long OnBuddyEvSubStateParam_e_get(long var0, OnBuddyEvSubStateParam var2);

    public static final native long new_OnBuddyEvSubStateParam();

    public static final native void delete_OnBuddyEvSubStateParam(long var0);

    public static final native long new_Buddy();

    public static final native void delete_Buddy(long var0);

    public static final native void Buddy_create(long var0, Buddy var2, long var3, Account var5, long var6, BuddyConfig var8) throws Exception;

    public static final native boolean Buddy_isValid(long var0, Buddy var2);

    public static final native long Buddy_getInfo(long var0, Buddy var2) throws Exception;

    public static final native void Buddy_subscribePresence(long var0, Buddy var2, boolean var3) throws Exception;

    public static final native void Buddy_updatePresence(long var0, Buddy var2) throws Exception;

    public static final native void Buddy_sendInstantMessage(long var0, Buddy var2, long var3, SendInstantMessageParam var5) throws Exception;

    public static final native void Buddy_sendTypingIndication(long var0, Buddy var2, long var3, SendTypingIndicationParam var5) throws Exception;

    public static final native void Buddy_onBuddyState(long var0, Buddy var2);

    public static final native void Buddy_onBuddyStateSwigExplicitBuddy(long var0, Buddy var2);

    public static final native void Buddy_onBuddyEvSubState(long var0, Buddy var2, long var3, OnBuddyEvSubStateParam var5);

    public static final native void Buddy_onBuddyEvSubStateSwigExplicitBuddy(long var0, Buddy var2, long var3, OnBuddyEvSubStateParam var5);

    public static final native void Buddy_director_connect(Buddy var0, long var1, boolean var3, boolean var4);

    public static final native void Buddy_change_ownership(Buddy var0, long var1, boolean var3);

    public static final native void AccountRegConfig_registrarUri_set(long var0, AccountRegConfig var2, String var3);

    public static final native String AccountRegConfig_registrarUri_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_registerOnAdd_set(long var0, AccountRegConfig var2, boolean var3);

    public static final native boolean AccountRegConfig_registerOnAdd_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_headers_set(long var0, AccountRegConfig var2, long var3, SipHeaderVector var5);

    public static final native long AccountRegConfig_headers_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_contactParams_set(long var0, AccountRegConfig var2, String var3);

    public static final native String AccountRegConfig_contactParams_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_timeoutSec_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_timeoutSec_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_retryIntervalSec_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_retryIntervalSec_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_firstRetryIntervalSec_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_firstRetryIntervalSec_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_randomRetryIntervalSec_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_randomRetryIntervalSec_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_delayBeforeRefreshSec_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_delayBeforeRefreshSec_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_dropCallsOnFail_set(long var0, AccountRegConfig var2, boolean var3);

    public static final native boolean AccountRegConfig_dropCallsOnFail_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_unregWaitMsec_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_unregWaitMsec_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_proxyUse_set(long var0, AccountRegConfig var2, long var3);

    public static final native long AccountRegConfig_proxyUse_get(long var0, AccountRegConfig var2);

    public static final native void AccountRegConfig_readObject(long var0, AccountRegConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountRegConfig_writeObject(long var0, AccountRegConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_AccountRegConfig();

    public static final native void delete_AccountRegConfig(long var0);

    public static final native void AccountSipConfig_authCreds_set(long var0, AccountSipConfig var2, long var3, AuthCredInfoVector var5);

    public static final native long AccountSipConfig_authCreds_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_proxies_set(long var0, AccountSipConfig var2, long var3, StringVector var5);

    public static final native long AccountSipConfig_proxies_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_contactForced_set(long var0, AccountSipConfig var2, String var3);

    public static final native String AccountSipConfig_contactForced_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_contactParams_set(long var0, AccountSipConfig var2, String var3);

    public static final native String AccountSipConfig_contactParams_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_contactUriParams_set(long var0, AccountSipConfig var2, String var3);

    public static final native String AccountSipConfig_contactUriParams_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_authInitialEmpty_set(long var0, AccountSipConfig var2, boolean var3);

    public static final native boolean AccountSipConfig_authInitialEmpty_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_authInitialAlgorithm_set(long var0, AccountSipConfig var2, String var3);

    public static final native String AccountSipConfig_authInitialAlgorithm_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_transportId_set(long var0, AccountSipConfig var2, int var3);

    public static final native int AccountSipConfig_transportId_get(long var0, AccountSipConfig var2);

    public static final native void AccountSipConfig_readObject(long var0, AccountSipConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountSipConfig_writeObject(long var0, AccountSipConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_AccountSipConfig();

    public static final native void delete_AccountSipConfig(long var0);

    public static final native void AccountCallConfig_holdType_set(long var0, AccountCallConfig var2, int var3);

    public static final native int AccountCallConfig_holdType_get(long var0, AccountCallConfig var2);

    public static final native void AccountCallConfig_prackUse_set(long var0, AccountCallConfig var2, int var3);

    public static final native int AccountCallConfig_prackUse_get(long var0, AccountCallConfig var2);

    public static final native void AccountCallConfig_timerUse_set(long var0, AccountCallConfig var2, int var3);

    public static final native int AccountCallConfig_timerUse_get(long var0, AccountCallConfig var2);

    public static final native void AccountCallConfig_timerMinSESec_set(long var0, AccountCallConfig var2, long var3);

    public static final native long AccountCallConfig_timerMinSESec_get(long var0, AccountCallConfig var2);

    public static final native void AccountCallConfig_timerSessExpiresSec_set(long var0, AccountCallConfig var2, long var3);

    public static final native long AccountCallConfig_timerSessExpiresSec_get(long var0, AccountCallConfig var2);

    public static final native long new_AccountCallConfig();

    public static final native void AccountCallConfig_readObject(long var0, AccountCallConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountCallConfig_writeObject(long var0, AccountCallConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_AccountCallConfig(long var0);

    public static final native void AccountPresConfig_headers_set(long var0, AccountPresConfig var2, long var3, SipHeaderVector var5);

    public static final native long AccountPresConfig_headers_get(long var0, AccountPresConfig var2);

    public static final native void AccountPresConfig_publishEnabled_set(long var0, AccountPresConfig var2, boolean var3);

    public static final native boolean AccountPresConfig_publishEnabled_get(long var0, AccountPresConfig var2);

    public static final native void AccountPresConfig_publishQueue_set(long var0, AccountPresConfig var2, boolean var3);

    public static final native boolean AccountPresConfig_publishQueue_get(long var0, AccountPresConfig var2);

    public static final native void AccountPresConfig_publishShutdownWaitMsec_set(long var0, AccountPresConfig var2, long var3);

    public static final native long AccountPresConfig_publishShutdownWaitMsec_get(long var0, AccountPresConfig var2);

    public static final native void AccountPresConfig_pidfTupleId_set(long var0, AccountPresConfig var2, String var3);

    public static final native String AccountPresConfig_pidfTupleId_get(long var0, AccountPresConfig var2);

    public static final native void AccountPresConfig_readObject(long var0, AccountPresConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountPresConfig_writeObject(long var0, AccountPresConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_AccountPresConfig();

    public static final native void delete_AccountPresConfig(long var0);

    public static final native void AccountMwiConfig_enabled_set(long var0, AccountMwiConfig var2, boolean var3);

    public static final native boolean AccountMwiConfig_enabled_get(long var0, AccountMwiConfig var2);

    public static final native void AccountMwiConfig_expirationSec_set(long var0, AccountMwiConfig var2, long var3);

    public static final native long AccountMwiConfig_expirationSec_get(long var0, AccountMwiConfig var2);

    public static final native void AccountMwiConfig_readObject(long var0, AccountMwiConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountMwiConfig_writeObject(long var0, AccountMwiConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_AccountMwiConfig();

    public static final native void delete_AccountMwiConfig(long var0);

    public static final native void AccountNatConfig_sipStunUse_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_sipStunUse_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_mediaStunUse_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_mediaStunUse_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_nat64Opt_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_nat64Opt_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceEnabled_set(long var0, AccountNatConfig var2, boolean var3);

    public static final native boolean AccountNatConfig_iceEnabled_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceMaxHostCands_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_iceMaxHostCands_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceAggressiveNomination_set(long var0, AccountNatConfig var2, boolean var3);

    public static final native boolean AccountNatConfig_iceAggressiveNomination_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceNominatedCheckDelayMsec_set(long var0, AccountNatConfig var2, long var3);

    public static final native long AccountNatConfig_iceNominatedCheckDelayMsec_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceWaitNominationTimeoutMsec_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_iceWaitNominationTimeoutMsec_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceNoRtcp_set(long var0, AccountNatConfig var2, boolean var3);

    public static final native boolean AccountNatConfig_iceNoRtcp_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_iceAlwaysUpdate_set(long var0, AccountNatConfig var2, boolean var3);

    public static final native boolean AccountNatConfig_iceAlwaysUpdate_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_turnEnabled_set(long var0, AccountNatConfig var2, boolean var3);

    public static final native boolean AccountNatConfig_turnEnabled_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_turnServer_set(long var0, AccountNatConfig var2, String var3);

    public static final native String AccountNatConfig_turnServer_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_turnConnType_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_turnConnType_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_turnUserName_set(long var0, AccountNatConfig var2, String var3);

    public static final native String AccountNatConfig_turnUserName_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_turnPasswordType_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_turnPasswordType_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_turnPassword_set(long var0, AccountNatConfig var2, String var3);

    public static final native String AccountNatConfig_turnPassword_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_contactRewriteUse_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_contactRewriteUse_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_contactRewriteMethod_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_contactRewriteMethod_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_contactUseSrcPort_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_contactUseSrcPort_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_viaRewriteUse_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_viaRewriteUse_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_sdpNatRewriteUse_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_sdpNatRewriteUse_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_sipOutboundUse_set(long var0, AccountNatConfig var2, int var3);

    public static final native int AccountNatConfig_sipOutboundUse_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_sipOutboundInstanceId_set(long var0, AccountNatConfig var2, String var3);

    public static final native String AccountNatConfig_sipOutboundInstanceId_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_sipOutboundRegId_set(long var0, AccountNatConfig var2, String var3);

    public static final native String AccountNatConfig_sipOutboundRegId_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_udpKaIntervalSec_set(long var0, AccountNatConfig var2, long var3);

    public static final native long AccountNatConfig_udpKaIntervalSec_get(long var0, AccountNatConfig var2);

    public static final native void AccountNatConfig_udpKaData_set(long var0, AccountNatConfig var2, String var3);

    public static final native String AccountNatConfig_udpKaData_get(long var0, AccountNatConfig var2);

    public static final native long new_AccountNatConfig();

    public static final native void AccountNatConfig_readObject(long var0, AccountNatConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountNatConfig_writeObject(long var0, AccountNatConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_AccountNatConfig(long var0);

    public static final native void SrtpCrypto_key_set(long var0, SrtpCrypto var2, String var3);

    public static final native String SrtpCrypto_key_get(long var0, SrtpCrypto var2);

    public static final native void SrtpCrypto_name_set(long var0, SrtpCrypto var2, String var3);

    public static final native String SrtpCrypto_name_get(long var0, SrtpCrypto var2);

    public static final native void SrtpCrypto_flags_set(long var0, SrtpCrypto var2, long var3);

    public static final native long SrtpCrypto_flags_get(long var0, SrtpCrypto var2);

    public static final native long new_SrtpCrypto();

    public static final native void delete_SrtpCrypto(long var0);

    public static final native void SrtpOpt_cryptos_set(long var0, SrtpOpt var2, long var3, SrtpCryptoVector var5);

    public static final native long SrtpOpt_cryptos_get(long var0, SrtpOpt var2);

    public static final native void SrtpOpt_keyings_set(long var0, SrtpOpt var2, long var3, IntVector var5);

    public static final native long SrtpOpt_keyings_get(long var0, SrtpOpt var2);

    public static final native long new_SrtpOpt();

    public static final native void SrtpOpt_readObject(long var0, SrtpOpt var2, long var3, ContainerNode var5) throws Exception;

    public static final native void SrtpOpt_writeObject(long var0, SrtpOpt var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_SrtpOpt(long var0);

    public static final native void RtcpFbCap_codecId_set(long var0, RtcpFbCap var2, String var3);

    public static final native String RtcpFbCap_codecId_get(long var0, RtcpFbCap var2);

    public static final native void RtcpFbCap_type_set(long var0, RtcpFbCap var2, int var3);

    public static final native int RtcpFbCap_type_get(long var0, RtcpFbCap var2);

    public static final native void RtcpFbCap_typeName_set(long var0, RtcpFbCap var2, String var3);

    public static final native String RtcpFbCap_typeName_get(long var0, RtcpFbCap var2);

    public static final native void RtcpFbCap_param_set(long var0, RtcpFbCap var2, String var3);

    public static final native String RtcpFbCap_param_get(long var0, RtcpFbCap var2);

    public static final native long new_RtcpFbCap();

    public static final native void delete_RtcpFbCap(long var0);

    public static final native void RtcpFbConfig_dontUseAvpf_set(long var0, RtcpFbConfig var2, boolean var3);

    public static final native boolean RtcpFbConfig_dontUseAvpf_get(long var0, RtcpFbConfig var2);

    public static final native void RtcpFbConfig_caps_set(long var0, RtcpFbConfig var2, long var3, RtcpFbCapVector var5);

    public static final native long RtcpFbConfig_caps_get(long var0, RtcpFbConfig var2);

    public static final native long new_RtcpFbConfig();

    public static final native void RtcpFbConfig_readObject(long var0, RtcpFbConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void RtcpFbConfig_writeObject(long var0, RtcpFbConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_RtcpFbConfig(long var0);

    public static final native void AccountMediaConfig_transportConfig_set(long var0, AccountMediaConfig var2, long var3, TransportConfig var5);

    public static final native long AccountMediaConfig_transportConfig_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_lockCodecEnabled_set(long var0, AccountMediaConfig var2, boolean var3);

    public static final native boolean AccountMediaConfig_lockCodecEnabled_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_streamKaEnabled_set(long var0, AccountMediaConfig var2, boolean var3);

    public static final native boolean AccountMediaConfig_streamKaEnabled_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_srtpUse_set(long var0, AccountMediaConfig var2, int var3);

    public static final native int AccountMediaConfig_srtpUse_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_srtpSecureSignaling_set(long var0, AccountMediaConfig var2, int var3);

    public static final native int AccountMediaConfig_srtpSecureSignaling_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_srtpOpt_set(long var0, AccountMediaConfig var2, long var3, SrtpOpt var5);

    public static final native long AccountMediaConfig_srtpOpt_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_ipv6Use_set(long var0, AccountMediaConfig var2, int var3);

    public static final native int AccountMediaConfig_ipv6Use_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_rtcpMuxEnabled_set(long var0, AccountMediaConfig var2, boolean var3);

    public static final native boolean AccountMediaConfig_rtcpMuxEnabled_get(long var0, AccountMediaConfig var2);

    public static final native void AccountMediaConfig_rtcpFbConfig_set(long var0, AccountMediaConfig var2, long var3, RtcpFbConfig var5);

    public static final native long AccountMediaConfig_rtcpFbConfig_get(long var0, AccountMediaConfig var2);

    public static final native long new_AccountMediaConfig();

    public static final native void AccountMediaConfig_readObject(long var0, AccountMediaConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountMediaConfig_writeObject(long var0, AccountMediaConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_AccountMediaConfig(long var0);

    public static final native void AccountVideoConfig_autoShowIncoming_set(long var0, AccountVideoConfig var2, boolean var3);

    public static final native boolean AccountVideoConfig_autoShowIncoming_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_autoTransmitOutgoing_set(long var0, AccountVideoConfig var2, boolean var3);

    public static final native boolean AccountVideoConfig_autoTransmitOutgoing_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_windowFlags_set(long var0, AccountVideoConfig var2, long var3);

    public static final native long AccountVideoConfig_windowFlags_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_defaultCaptureDevice_set(long var0, AccountVideoConfig var2, int var3);

    public static final native int AccountVideoConfig_defaultCaptureDevice_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_defaultRenderDevice_set(long var0, AccountVideoConfig var2, int var3);

    public static final native int AccountVideoConfig_defaultRenderDevice_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_rateControlMethod_set(long var0, AccountVideoConfig var2, int var3);

    public static final native int AccountVideoConfig_rateControlMethod_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_rateControlBandwidth_set(long var0, AccountVideoConfig var2, long var3);

    public static final native long AccountVideoConfig_rateControlBandwidth_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_startKeyframeCount_set(long var0, AccountVideoConfig var2, long var3);

    public static final native long AccountVideoConfig_startKeyframeCount_get(long var0, AccountVideoConfig var2);

    public static final native void AccountVideoConfig_startKeyframeInterval_set(long var0, AccountVideoConfig var2, long var3);

    public static final native long AccountVideoConfig_startKeyframeInterval_get(long var0, AccountVideoConfig var2);

    public static final native long new_AccountVideoConfig();

    public static final native void AccountVideoConfig_readObject(long var0, AccountVideoConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountVideoConfig_writeObject(long var0, AccountVideoConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_AccountVideoConfig(long var0);

    public static final native void AccountIpChangeConfig_shutdownTp_set(long var0, AccountIpChangeConfig var2, boolean var3);

    public static final native boolean AccountIpChangeConfig_shutdownTp_get(long var0, AccountIpChangeConfig var2);

    public static final native void AccountIpChangeConfig_hangupCalls_set(long var0, AccountIpChangeConfig var2, boolean var3);

    public static final native boolean AccountIpChangeConfig_hangupCalls_get(long var0, AccountIpChangeConfig var2);

    public static final native void AccountIpChangeConfig_reinviteFlags_set(long var0, AccountIpChangeConfig var2, long var3);

    public static final native long AccountIpChangeConfig_reinviteFlags_get(long var0, AccountIpChangeConfig var2);

    public static final native void delete_AccountIpChangeConfig(long var0);

    public static final native void AccountIpChangeConfig_readObject(long var0, AccountIpChangeConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountIpChangeConfig_writeObject(long var0, AccountIpChangeConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_AccountIpChangeConfig();

    public static final native void AccountConfig_priority_set(long var0, AccountConfig var2, int var3);

    public static final native int AccountConfig_priority_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_idUri_set(long var0, AccountConfig var2, String var3);

    public static final native String AccountConfig_idUri_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_regConfig_set(long var0, AccountConfig var2, long var3, AccountRegConfig var5);

    public static final native long AccountConfig_regConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_sipConfig_set(long var0, AccountConfig var2, long var3, AccountSipConfig var5);

    public static final native long AccountConfig_sipConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_callConfig_set(long var0, AccountConfig var2, long var3, AccountCallConfig var5);

    public static final native long AccountConfig_callConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_presConfig_set(long var0, AccountConfig var2, long var3, AccountPresConfig var5);

    public static final native long AccountConfig_presConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_mwiConfig_set(long var0, AccountConfig var2, long var3, AccountMwiConfig var5);

    public static final native long AccountConfig_mwiConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_natConfig_set(long var0, AccountConfig var2, long var3, AccountNatConfig var5);

    public static final native long AccountConfig_natConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_mediaConfig_set(long var0, AccountConfig var2, long var3, AccountMediaConfig var5);

    public static final native long AccountConfig_mediaConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_videoConfig_set(long var0, AccountConfig var2, long var3, AccountVideoConfig var5);

    public static final native long AccountConfig_videoConfig_get(long var0, AccountConfig var2);

    public static final native void AccountConfig_ipChangeConfig_set(long var0, AccountConfig var2, long var3, AccountIpChangeConfig var5);

    public static final native long AccountConfig_ipChangeConfig_get(long var0, AccountConfig var2);

    public static final native long new_AccountConfig();

    public static final native void AccountConfig_readObject(long var0, AccountConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void AccountConfig_writeObject(long var0, AccountConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_AccountConfig(long var0);

    public static final native void AccountInfo_id_set(long var0, AccountInfo var2, int var3);

    public static final native int AccountInfo_id_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_isDefault_set(long var0, AccountInfo var2, boolean var3);

    public static final native boolean AccountInfo_isDefault_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_uri_set(long var0, AccountInfo var2, String var3);

    public static final native String AccountInfo_uri_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_regIsConfigured_set(long var0, AccountInfo var2, boolean var3);

    public static final native boolean AccountInfo_regIsConfigured_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_regIsActive_set(long var0, AccountInfo var2, boolean var3);

    public static final native boolean AccountInfo_regIsActive_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_regExpiresSec_set(long var0, AccountInfo var2, long var3);

    public static final native long AccountInfo_regExpiresSec_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_regStatus_set(long var0, AccountInfo var2, int var3);

    public static final native int AccountInfo_regStatus_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_regStatusText_set(long var0, AccountInfo var2, String var3);

    public static final native String AccountInfo_regStatusText_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_regLastErr_set(long var0, AccountInfo var2, int var3);

    public static final native int AccountInfo_regLastErr_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_onlineStatus_set(long var0, AccountInfo var2, boolean var3);

    public static final native boolean AccountInfo_onlineStatus_get(long var0, AccountInfo var2);

    public static final native void AccountInfo_onlineStatusText_set(long var0, AccountInfo var2, String var3);

    public static final native String AccountInfo_onlineStatusText_get(long var0, AccountInfo var2);

    public static final native long new_AccountInfo();

    public static final native void delete_AccountInfo(long var0);

    public static final native void OnIncomingCallParam_callId_set(long var0, OnIncomingCallParam var2, int var3);

    public static final native int OnIncomingCallParam_callId_get(long var0, OnIncomingCallParam var2);

    public static final native void OnIncomingCallParam_rdata_set(long var0, OnIncomingCallParam var2, long var3, SipRxData var5);

    public static final native long OnIncomingCallParam_rdata_get(long var0, OnIncomingCallParam var2);

    public static final native long new_OnIncomingCallParam();

    public static final native void delete_OnIncomingCallParam(long var0);

    public static final native void OnRegStartedParam_renew_set(long var0, OnRegStartedParam var2, boolean var3);

    public static final native boolean OnRegStartedParam_renew_get(long var0, OnRegStartedParam var2);

    public static final native long new_OnRegStartedParam();

    public static final native void delete_OnRegStartedParam(long var0);

    public static final native void OnRegStateParam_status_set(long var0, OnRegStateParam var2, int var3);

    public static final native int OnRegStateParam_status_get(long var0, OnRegStateParam var2);

    public static final native void OnRegStateParam_code_set(long var0, OnRegStateParam var2, int var3);

    public static final native int OnRegStateParam_code_get(long var0, OnRegStateParam var2);

    public static final native void OnRegStateParam_reason_set(long var0, OnRegStateParam var2, String var3);

    public static final native String OnRegStateParam_reason_get(long var0, OnRegStateParam var2);

    public static final native void OnRegStateParam_rdata_set(long var0, OnRegStateParam var2, long var3, SipRxData var5);

    public static final native long OnRegStateParam_rdata_get(long var0, OnRegStateParam var2);

    public static final native void OnRegStateParam_expiration_set(long var0, OnRegStateParam var2, long var3);

    public static final native long OnRegStateParam_expiration_get(long var0, OnRegStateParam var2);

    public static final native long new_OnRegStateParam();

    public static final native void delete_OnRegStateParam(long var0);

    public static final native void OnIncomingSubscribeParam_srvPres_set(long var0, OnIncomingSubscribeParam var2, long var3);

    public static final native long OnIncomingSubscribeParam_srvPres_get(long var0, OnIncomingSubscribeParam var2);

    public static final native void OnIncomingSubscribeParam_fromUri_set(long var0, OnIncomingSubscribeParam var2, String var3);

    public static final native String OnIncomingSubscribeParam_fromUri_get(long var0, OnIncomingSubscribeParam var2);

    public static final native void OnIncomingSubscribeParam_rdata_set(long var0, OnIncomingSubscribeParam var2, long var3, SipRxData var5);

    public static final native long OnIncomingSubscribeParam_rdata_get(long var0, OnIncomingSubscribeParam var2);

    public static final native void OnIncomingSubscribeParam_code_set(long var0, OnIncomingSubscribeParam var2, int var3);

    public static final native int OnIncomingSubscribeParam_code_get(long var0, OnIncomingSubscribeParam var2);

    public static final native void OnIncomingSubscribeParam_reason_set(long var0, OnIncomingSubscribeParam var2, String var3);

    public static final native String OnIncomingSubscribeParam_reason_get(long var0, OnIncomingSubscribeParam var2);

    public static final native void OnIncomingSubscribeParam_txOption_set(long var0, OnIncomingSubscribeParam var2, long var3, SipTxOption var5);

    public static final native long OnIncomingSubscribeParam_txOption_get(long var0, OnIncomingSubscribeParam var2);

    public static final native long new_OnIncomingSubscribeParam();

    public static final native void delete_OnIncomingSubscribeParam(long var0);

    public static final native void OnInstantMessageParam_fromUri_set(long var0, OnInstantMessageParam var2, String var3);

    public static final native String OnInstantMessageParam_fromUri_get(long var0, OnInstantMessageParam var2);

    public static final native void OnInstantMessageParam_toUri_set(long var0, OnInstantMessageParam var2, String var3);

    public static final native String OnInstantMessageParam_toUri_get(long var0, OnInstantMessageParam var2);

    public static final native void OnInstantMessageParam_contactUri_set(long var0, OnInstantMessageParam var2, String var3);

    public static final native String OnInstantMessageParam_contactUri_get(long var0, OnInstantMessageParam var2);

    public static final native void OnInstantMessageParam_contentType_set(long var0, OnInstantMessageParam var2, String var3);

    public static final native String OnInstantMessageParam_contentType_get(long var0, OnInstantMessageParam var2);

    public static final native void OnInstantMessageParam_msgBody_set(long var0, OnInstantMessageParam var2, String var3);

    public static final native String OnInstantMessageParam_msgBody_get(long var0, OnInstantMessageParam var2);

    public static final native void OnInstantMessageParam_rdata_set(long var0, OnInstantMessageParam var2, long var3, SipRxData var5);

    public static final native long OnInstantMessageParam_rdata_get(long var0, OnInstantMessageParam var2);

    public static final native long new_OnInstantMessageParam();

    public static final native void delete_OnInstantMessageParam(long var0);

    public static final native void OnInstantMessageStatusParam_userData_set(long var0, OnInstantMessageStatusParam var2, long var3);

    public static final native long OnInstantMessageStatusParam_userData_get(long var0, OnInstantMessageStatusParam var2);

    public static final native void OnInstantMessageStatusParam_toUri_set(long var0, OnInstantMessageStatusParam var2, String var3);

    public static final native String OnInstantMessageStatusParam_toUri_get(long var0, OnInstantMessageStatusParam var2);

    public static final native void OnInstantMessageStatusParam_msgBody_set(long var0, OnInstantMessageStatusParam var2, String var3);

    public static final native String OnInstantMessageStatusParam_msgBody_get(long var0, OnInstantMessageStatusParam var2);

    public static final native void OnInstantMessageStatusParam_code_set(long var0, OnInstantMessageStatusParam var2, int var3);

    public static final native int OnInstantMessageStatusParam_code_get(long var0, OnInstantMessageStatusParam var2);

    public static final native void OnInstantMessageStatusParam_reason_set(long var0, OnInstantMessageStatusParam var2, String var3);

    public static final native String OnInstantMessageStatusParam_reason_get(long var0, OnInstantMessageStatusParam var2);

    public static final native void OnInstantMessageStatusParam_rdata_set(long var0, OnInstantMessageStatusParam var2, long var3, SipRxData var5);

    public static final native long OnInstantMessageStatusParam_rdata_get(long var0, OnInstantMessageStatusParam var2);

    public static final native long new_OnInstantMessageStatusParam();

    public static final native void delete_OnInstantMessageStatusParam(long var0);

    public static final native void OnTypingIndicationParam_fromUri_set(long var0, OnTypingIndicationParam var2, String var3);

    public static final native String OnTypingIndicationParam_fromUri_get(long var0, OnTypingIndicationParam var2);

    public static final native void OnTypingIndicationParam_toUri_set(long var0, OnTypingIndicationParam var2, String var3);

    public static final native String OnTypingIndicationParam_toUri_get(long var0, OnTypingIndicationParam var2);

    public static final native void OnTypingIndicationParam_contactUri_set(long var0, OnTypingIndicationParam var2, String var3);

    public static final native String OnTypingIndicationParam_contactUri_get(long var0, OnTypingIndicationParam var2);

    public static final native void OnTypingIndicationParam_isTyping_set(long var0, OnTypingIndicationParam var2, boolean var3);

    public static final native boolean OnTypingIndicationParam_isTyping_get(long var0, OnTypingIndicationParam var2);

    public static final native void OnTypingIndicationParam_rdata_set(long var0, OnTypingIndicationParam var2, long var3, SipRxData var5);

    public static final native long OnTypingIndicationParam_rdata_get(long var0, OnTypingIndicationParam var2);

    public static final native long new_OnTypingIndicationParam();

    public static final native void delete_OnTypingIndicationParam(long var0);

    public static final native void OnMwiInfoParam_state_set(long var0, OnMwiInfoParam var2, int var3);

    public static final native int OnMwiInfoParam_state_get(long var0, OnMwiInfoParam var2);

    public static final native void OnMwiInfoParam_rdata_set(long var0, OnMwiInfoParam var2, long var3, SipRxData var5);

    public static final native long OnMwiInfoParam_rdata_get(long var0, OnMwiInfoParam var2);

    public static final native long new_OnMwiInfoParam();

    public static final native void delete_OnMwiInfoParam(long var0);

    public static final native void PresNotifyParam_srvPres_set(long var0, PresNotifyParam var2, long var3);

    public static final native long PresNotifyParam_srvPres_get(long var0, PresNotifyParam var2);

    public static final native void PresNotifyParam_state_set(long var0, PresNotifyParam var2, int var3);

    public static final native int PresNotifyParam_state_get(long var0, PresNotifyParam var2);

    public static final native void PresNotifyParam_stateStr_set(long var0, PresNotifyParam var2, String var3);

    public static final native String PresNotifyParam_stateStr_get(long var0, PresNotifyParam var2);

    public static final native void PresNotifyParam_reason_set(long var0, PresNotifyParam var2, String var3);

    public static final native String PresNotifyParam_reason_get(long var0, PresNotifyParam var2);

    public static final native void PresNotifyParam_withBody_set(long var0, PresNotifyParam var2, boolean var3);

    public static final native boolean PresNotifyParam_withBody_get(long var0, PresNotifyParam var2);

    public static final native void PresNotifyParam_txOption_set(long var0, PresNotifyParam var2, long var3, SipTxOption var5);

    public static final native long PresNotifyParam_txOption_get(long var0, PresNotifyParam var2);

    public static final native long new_PresNotifyParam();

    public static final native void delete_PresNotifyParam(long var0);

    public static final native boolean FindBuddyMatch_match(long var0, FindBuddyMatch var2, String var3, long var4, Buddy var6);

    public static final native boolean FindBuddyMatch_matchSwigExplicitFindBuddyMatch(long var0, FindBuddyMatch var2, String var3, long var4, Buddy var6);

    public static final native void delete_FindBuddyMatch(long var0);

    public static final native long new_FindBuddyMatch();

    public static final native void FindBuddyMatch_director_connect(FindBuddyMatch var0, long var1, boolean var3, boolean var4);

    public static final native void FindBuddyMatch_change_ownership(FindBuddyMatch var0, long var1, boolean var3);

    public static final native long new_Account();

    public static final native void delete_Account(long var0);

    public static final native void Account_create__SWIG_0(long var0, Account var2, long var3, AccountConfig var5, boolean var6) throws Exception;

    public static final native void Account_create__SWIG_1(long var0, Account var2, long var3, AccountConfig var5) throws Exception;

    public static final native void Account_shutdown(long var0, Account var2);

    public static final native void Account_modify(long var0, Account var2, long var3, AccountConfig var5) throws Exception;

    public static final native boolean Account_isValid(long var0, Account var2);

    public static final native void Account_setDefault(long var0, Account var2) throws Exception;

    public static final native boolean Account_isDefault(long var0, Account var2);

    public static final native int Account_getId(long var0, Account var2);

    public static final native long Account_lookup(int var0);

    public static final native long Account_getInfo(long var0, Account var2) throws Exception;

    public static final native void Account_setRegistration(long var0, Account var2, boolean var3) throws Exception;

    public static final native void Account_setOnlineStatus(long var0, Account var2, long var3, PresenceStatus var5) throws Exception;

    public static final native void Account_setTransport(long var0, Account var2, int var3) throws Exception;

    public static final native void Account_presNotify(long var0, Account var2, long var3, PresNotifyParam var5) throws Exception;

    public static final native long Account_enumBuddies2(long var0, Account var2) throws Exception;

    public static final native long Account_findBuddy2(long var0, Account var2, String var3) throws Exception;

    public static final native void Account_onIncomingCall(long var0, Account var2, long var3, OnIncomingCallParam var5);

    public static final native void Account_onIncomingCallSwigExplicitAccount(long var0, Account var2, long var3, OnIncomingCallParam var5);

    public static final native void Account_onRegStarted(long var0, Account var2, long var3, OnRegStartedParam var5);

    public static final native void Account_onRegStartedSwigExplicitAccount(long var0, Account var2, long var3, OnRegStartedParam var5);

    public static final native void Account_onRegState(long var0, Account var2, long var3, OnRegStateParam var5);

    public static final native void Account_onRegStateSwigExplicitAccount(long var0, Account var2, long var3, OnRegStateParam var5);

    public static final native void Account_onIncomingSubscribe(long var0, Account var2, long var3, OnIncomingSubscribeParam var5);

    public static final native void Account_onIncomingSubscribeSwigExplicitAccount(long var0, Account var2, long var3, OnIncomingSubscribeParam var5);

    public static final native void Account_onInstantMessage(long var0, Account var2, long var3, OnInstantMessageParam var5);

    public static final native void Account_onInstantMessageSwigExplicitAccount(long var0, Account var2, long var3, OnInstantMessageParam var5);

    public static final native void Account_onInstantMessageStatus(long var0, Account var2, long var3, OnInstantMessageStatusParam var5);

    public static final native void Account_onInstantMessageStatusSwigExplicitAccount(long var0, Account var2, long var3, OnInstantMessageStatusParam var5);

    public static final native void Account_onTypingIndication(long var0, Account var2, long var3, OnTypingIndicationParam var5);

    public static final native void Account_onTypingIndicationSwigExplicitAccount(long var0, Account var2, long var3, OnTypingIndicationParam var5);

    public static final native void Account_onMwiInfo(long var0, Account var2, long var3, OnMwiInfoParam var5);

    public static final native void Account_onMwiInfoSwigExplicitAccount(long var0, Account var2, long var3, OnMwiInfoParam var5);

    public static final native void Account_director_connect(Account var0, long var1, boolean var3, boolean var4);

    public static final native void Account_change_ownership(Account var0, long var1, boolean var3);

    public static final native void MathStat_n_set(long var0, MathStat var2, int var3);

    public static final native int MathStat_n_get(long var0, MathStat var2);

    public static final native void MathStat_max_set(long var0, MathStat var2, int var3);

    public static final native int MathStat_max_get(long var0, MathStat var2);

    public static final native void MathStat_min_set(long var0, MathStat var2, int var3);

    public static final native int MathStat_min_get(long var0, MathStat var2);

    public static final native void MathStat_last_set(long var0, MathStat var2, int var3);

    public static final native int MathStat_last_get(long var0, MathStat var2);

    public static final native void MathStat_mean_set(long var0, MathStat var2, int var3);

    public static final native int MathStat_mean_get(long var0, MathStat var2);

    public static final native long new_MathStat();

    public static final native void delete_MathStat(long var0);

    public static final native void LossType_burst_set(long var0, LossType var2, long var3);

    public static final native long LossType_burst_get(long var0, LossType var2);

    public static final native void LossType_random_set(long var0, LossType var2, long var3);

    public static final native long LossType_random_get(long var0, LossType var2);

    public static final native long new_LossType();

    public static final native void delete_LossType(long var0);

    public static final native void RtcpStreamStat_update_set(long var0, RtcpStreamStat var2, long var3, TimeVal var5);

    public static final native long RtcpStreamStat_update_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_updateCount_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_updateCount_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_pkt_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_pkt_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_bytes_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_bytes_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_discard_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_discard_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_loss_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_loss_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_reorder_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_reorder_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_dup_set(long var0, RtcpStreamStat var2, long var3);

    public static final native long RtcpStreamStat_dup_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_lossPeriodUsec_set(long var0, RtcpStreamStat var2, long var3, MathStat var5);

    public static final native long RtcpStreamStat_lossPeriodUsec_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_lossType_set(long var0, RtcpStreamStat var2, long var3, LossType var5);

    public static final native long RtcpStreamStat_lossType_get(long var0, RtcpStreamStat var2);

    public static final native void RtcpStreamStat_jitterUsec_set(long var0, RtcpStreamStat var2, long var3, MathStat var5);

    public static final native long RtcpStreamStat_jitterUsec_get(long var0, RtcpStreamStat var2);

    public static final native long new_RtcpStreamStat();

    public static final native void delete_RtcpStreamStat(long var0);

    public static final native void RtcpSdes_cname_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_cname_get(long var0, RtcpSdes var2);

    public static final native void RtcpSdes_name_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_name_get(long var0, RtcpSdes var2);

    public static final native void RtcpSdes_email_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_email_get(long var0, RtcpSdes var2);

    public static final native void RtcpSdes_phone_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_phone_get(long var0, RtcpSdes var2);

    public static final native void RtcpSdes_loc_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_loc_get(long var0, RtcpSdes var2);

    public static final native void RtcpSdes_tool_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_tool_get(long var0, RtcpSdes var2);

    public static final native void RtcpSdes_note_set(long var0, RtcpSdes var2, String var3);

    public static final native String RtcpSdes_note_get(long var0, RtcpSdes var2);

    public static final native long new_RtcpSdes();

    public static final native void delete_RtcpSdes(long var0);

    public static final native void RtcpStat_start_set(long var0, RtcpStat var2, long var3, TimeVal var5);

    public static final native long RtcpStat_start_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_txStat_set(long var0, RtcpStat var2, long var3, RtcpStreamStat var5);

    public static final native long RtcpStat_txStat_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_rxStat_set(long var0, RtcpStat var2, long var3, RtcpStreamStat var5);

    public static final native long RtcpStat_rxStat_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_rttUsec_set(long var0, RtcpStat var2, long var3, MathStat var5);

    public static final native long RtcpStat_rttUsec_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_rtpTxLastTs_set(long var0, RtcpStat var2, long var3);

    public static final native long RtcpStat_rtpTxLastTs_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_rtpTxLastSeq_set(long var0, RtcpStat var2, int var3);

    public static final native int RtcpStat_rtpTxLastSeq_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_rxIpdvUsec_set(long var0, RtcpStat var2, long var3, MathStat var5);

    public static final native long RtcpStat_rxIpdvUsec_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_rxRawJitterUsec_set(long var0, RtcpStat var2, long var3, MathStat var5);

    public static final native long RtcpStat_rxRawJitterUsec_get(long var0, RtcpStat var2);

    public static final native void RtcpStat_peerSdes_set(long var0, RtcpStat var2, long var3, RtcpSdes var5);

    public static final native long RtcpStat_peerSdes_get(long var0, RtcpStat var2);

    public static final native long new_RtcpStat();

    public static final native void delete_RtcpStat(long var0);

    public static final native void JbufState_frameSize_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_frameSize_get(long var0, JbufState var2);

    public static final native void JbufState_minPrefetch_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_minPrefetch_get(long var0, JbufState var2);

    public static final native void JbufState_maxPrefetch_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_maxPrefetch_get(long var0, JbufState var2);

    public static final native void JbufState_burst_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_burst_get(long var0, JbufState var2);

    public static final native void JbufState_prefetch_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_prefetch_get(long var0, JbufState var2);

    public static final native void JbufState_size_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_size_get(long var0, JbufState var2);

    public static final native void JbufState_avgDelayMsec_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_avgDelayMsec_get(long var0, JbufState var2);

    public static final native void JbufState_minDelayMsec_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_minDelayMsec_get(long var0, JbufState var2);

    public static final native void JbufState_maxDelayMsec_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_maxDelayMsec_get(long var0, JbufState var2);

    public static final native void JbufState_devDelayMsec_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_devDelayMsec_get(long var0, JbufState var2);

    public static final native void JbufState_avgBurst_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_avgBurst_get(long var0, JbufState var2);

    public static final native void JbufState_lost_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_lost_get(long var0, JbufState var2);

    public static final native void JbufState_discard_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_discard_get(long var0, JbufState var2);

    public static final native void JbufState_empty_set(long var0, JbufState var2, long var3);

    public static final native long JbufState_empty_get(long var0, JbufState var2);

    public static final native long new_JbufState();

    public static final native void delete_JbufState(long var0);

    public static final native void SdpSession_wholeSdp_set(long var0, SdpSession var2, String var3);

    public static final native String SdpSession_wholeSdp_get(long var0, SdpSession var2);

    public static final native void SdpSession_pjSdpSession_set(long var0, SdpSession var2, long var3);

    public static final native long SdpSession_pjSdpSession_get(long var0, SdpSession var2);

    public static final native long new_SdpSession();

    public static final native void delete_SdpSession(long var0);

    public static final native void MediaTransportInfo_localRtpName_set(long var0, MediaTransportInfo var2, String var3);

    public static final native String MediaTransportInfo_localRtpName_get(long var0, MediaTransportInfo var2);

    public static final native void MediaTransportInfo_localRtcpName_set(long var0, MediaTransportInfo var2, String var3);

    public static final native String MediaTransportInfo_localRtcpName_get(long var0, MediaTransportInfo var2);

    public static final native void MediaTransportInfo_srcRtpName_set(long var0, MediaTransportInfo var2, String var3);

    public static final native String MediaTransportInfo_srcRtpName_get(long var0, MediaTransportInfo var2);

    public static final native void MediaTransportInfo_srcRtcpName_set(long var0, MediaTransportInfo var2, String var3);

    public static final native String MediaTransportInfo_srcRtcpName_get(long var0, MediaTransportInfo var2);

    public static final native long new_MediaTransportInfo();

    public static final native void delete_MediaTransportInfo(long var0);

    public static final native void CallSetting_flag_set(long var0, CallSetting var2, long var3);

    public static final native long CallSetting_flag_get(long var0, CallSetting var2);

    public static final native void CallSetting_reqKeyframeMethod_set(long var0, CallSetting var2, long var3);

    public static final native long CallSetting_reqKeyframeMethod_get(long var0, CallSetting var2);

    public static final native void CallSetting_audioCount_set(long var0, CallSetting var2, long var3);

    public static final native long CallSetting_audioCount_get(long var0, CallSetting var2);

    public static final native void CallSetting_videoCount_set(long var0, CallSetting var2, long var3);

    public static final native long CallSetting_videoCount_get(long var0, CallSetting var2);

    public static final native long new_CallSetting__SWIG_0(boolean var0);

    public static final native long new_CallSetting__SWIG_1();

    public static final native boolean CallSetting_isEmpty(long var0, CallSetting var2);

    public static final native void delete_CallSetting(long var0);

    public static final native void CallMediaInfo_index_set(long var0, CallMediaInfo var2, long var3);

    public static final native long CallMediaInfo_index_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_type_set(long var0, CallMediaInfo var2, int var3);

    public static final native int CallMediaInfo_type_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_dir_set(long var0, CallMediaInfo var2, int var3);

    public static final native int CallMediaInfo_dir_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_status_set(long var0, CallMediaInfo var2, int var3);

    public static final native int CallMediaInfo_status_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_audioConfSlot_set(long var0, CallMediaInfo var2, int var3);

    public static final native int CallMediaInfo_audioConfSlot_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_videoIncomingWindowId_set(long var0, CallMediaInfo var2, int var3);

    public static final native int CallMediaInfo_videoIncomingWindowId_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_videoWindow_set(long var0, CallMediaInfo var2, long var3, VideoWindow var5);

    public static final native long CallMediaInfo_videoWindow_get(long var0, CallMediaInfo var2);

    public static final native void CallMediaInfo_videoCapDev_set(long var0, CallMediaInfo var2, int var3);

    public static final native int CallMediaInfo_videoCapDev_get(long var0, CallMediaInfo var2);

    public static final native long new_CallMediaInfo();

    public static final native void delete_CallMediaInfo(long var0);

    public static final native void CallInfo_id_set(long var0, CallInfo var2, int var3);

    public static final native int CallInfo_id_get(long var0, CallInfo var2);

    public static final native void CallInfo_role_set(long var0, CallInfo var2, int var3);

    public static final native int CallInfo_role_get(long var0, CallInfo var2);

    public static final native void CallInfo_accId_set(long var0, CallInfo var2, int var3);

    public static final native int CallInfo_accId_get(long var0, CallInfo var2);

    public static final native void CallInfo_localUri_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_localUri_get(long var0, CallInfo var2);

    public static final native void CallInfo_localContact_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_localContact_get(long var0, CallInfo var2);

    public static final native void CallInfo_remoteUri_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_remoteUri_get(long var0, CallInfo var2);

    public static final native void CallInfo_remoteContact_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_remoteContact_get(long var0, CallInfo var2);

    public static final native void CallInfo_callIdString_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_callIdString_get(long var0, CallInfo var2);

    public static final native void CallInfo_setting_set(long var0, CallInfo var2, long var3, CallSetting var5);

    public static final native long CallInfo_setting_get(long var0, CallInfo var2);

    public static final native void CallInfo_state_set(long var0, CallInfo var2, int var3);

    public static final native int CallInfo_state_get(long var0, CallInfo var2);

    public static final native void CallInfo_stateText_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_stateText_get(long var0, CallInfo var2);

    public static final native void CallInfo_lastStatusCode_set(long var0, CallInfo var2, int var3);

    public static final native int CallInfo_lastStatusCode_get(long var0, CallInfo var2);

    public static final native void CallInfo_lastReason_set(long var0, CallInfo var2, String var3);

    public static final native String CallInfo_lastReason_get(long var0, CallInfo var2);

    public static final native void CallInfo_media_set(long var0, CallInfo var2, long var3, CallMediaInfoVector var5);

    public static final native long CallInfo_media_get(long var0, CallInfo var2);

    public static final native void CallInfo_provMedia_set(long var0, CallInfo var2, long var3, CallMediaInfoVector var5);

    public static final native long CallInfo_provMedia_get(long var0, CallInfo var2);

    public static final native void CallInfo_connectDuration_set(long var0, CallInfo var2, long var3, TimeVal var5);

    public static final native long CallInfo_connectDuration_get(long var0, CallInfo var2);

    public static final native void CallInfo_totalDuration_set(long var0, CallInfo var2, long var3, TimeVal var5);

    public static final native long CallInfo_totalDuration_get(long var0, CallInfo var2);

    public static final native void CallInfo_remOfferer_set(long var0, CallInfo var2, boolean var3);

    public static final native boolean CallInfo_remOfferer_get(long var0, CallInfo var2);

    public static final native void CallInfo_remAudioCount_set(long var0, CallInfo var2, long var3);

    public static final native long CallInfo_remAudioCount_get(long var0, CallInfo var2);

    public static final native void CallInfo_remVideoCount_set(long var0, CallInfo var2, long var3);

    public static final native long CallInfo_remVideoCount_get(long var0, CallInfo var2);

    public static final native long new_CallInfo();

    public static final native void delete_CallInfo(long var0);

    public static final native void StreamInfo_type_set(long var0, StreamInfo var2, int var3);

    public static final native int StreamInfo_type_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_proto_set(long var0, StreamInfo var2, int var3);

    public static final native int StreamInfo_proto_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_dir_set(long var0, StreamInfo var2, int var3);

    public static final native int StreamInfo_dir_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_remoteRtpAddress_set(long var0, StreamInfo var2, String var3);

    public static final native String StreamInfo_remoteRtpAddress_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_remoteRtcpAddress_set(long var0, StreamInfo var2, String var3);

    public static final native String StreamInfo_remoteRtcpAddress_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_txPt_set(long var0, StreamInfo var2, long var3);

    public static final native long StreamInfo_txPt_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_rxPt_set(long var0, StreamInfo var2, long var3);

    public static final native long StreamInfo_rxPt_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_codecName_set(long var0, StreamInfo var2, String var3);

    public static final native String StreamInfo_codecName_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_codecClockRate_set(long var0, StreamInfo var2, long var3);

    public static final native long StreamInfo_codecClockRate_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_audCodecParam_set(long var0, StreamInfo var2, long var3, CodecParam var5);

    public static final native long StreamInfo_audCodecParam_get(long var0, StreamInfo var2);

    public static final native void StreamInfo_vidCodecParam_set(long var0, StreamInfo var2, long var3, VidCodecParam var5);

    public static final native long StreamInfo_vidCodecParam_get(long var0, StreamInfo var2);

    public static final native long new_StreamInfo();

    public static final native void delete_StreamInfo(long var0);

    public static final native void StreamStat_rtcp_set(long var0, StreamStat var2, long var3, RtcpStat var5);

    public static final native long StreamStat_rtcp_get(long var0, StreamStat var2);

    public static final native void StreamStat_jbuf_set(long var0, StreamStat var2, long var3, JbufState var5);

    public static final native long StreamStat_jbuf_get(long var0, StreamStat var2);

    public static final native long new_StreamStat();

    public static final native void delete_StreamStat(long var0);

    public static final native void OnCallStateParam_e_set(long var0, OnCallStateParam var2, long var3, SipEvent var5);

    public static final native long OnCallStateParam_e_get(long var0, OnCallStateParam var2);

    public static final native long new_OnCallStateParam();

    public static final native void delete_OnCallStateParam(long var0);

    public static final native void OnCallTsxStateParam_e_set(long var0, OnCallTsxStateParam var2, long var3, SipEvent var5);

    public static final native long OnCallTsxStateParam_e_get(long var0, OnCallTsxStateParam var2);

    public static final native long new_OnCallTsxStateParam();

    public static final native void delete_OnCallTsxStateParam(long var0);

    public static final native long new_OnCallMediaStateParam();

    public static final native void delete_OnCallMediaStateParam(long var0);

    public static final native void OnCallSdpCreatedParam_sdp_set(long var0, OnCallSdpCreatedParam var2, long var3, SdpSession var5);

    public static final native long OnCallSdpCreatedParam_sdp_get(long var0, OnCallSdpCreatedParam var2);

    public static final native void OnCallSdpCreatedParam_remSdp_set(long var0, OnCallSdpCreatedParam var2, long var3, SdpSession var5);

    public static final native long OnCallSdpCreatedParam_remSdp_get(long var0, OnCallSdpCreatedParam var2);

    public static final native long new_OnCallSdpCreatedParam();

    public static final native void delete_OnCallSdpCreatedParam(long var0);

    public static final native void OnStreamCreatedParam_stream_set(long var0, OnStreamCreatedParam var2, long var3);

    public static final native long OnStreamCreatedParam_stream_get(long var0, OnStreamCreatedParam var2);

    public static final native void OnStreamCreatedParam_streamIdx_set(long var0, OnStreamCreatedParam var2, long var3);

    public static final native long OnStreamCreatedParam_streamIdx_get(long var0, OnStreamCreatedParam var2);

    public static final native void OnStreamCreatedParam_destroyPort_set(long var0, OnStreamCreatedParam var2, boolean var3);

    public static final native boolean OnStreamCreatedParam_destroyPort_get(long var0, OnStreamCreatedParam var2);

    public static final native void OnStreamCreatedParam_pPort_set(long var0, OnStreamCreatedParam var2, long var3);

    public static final native long OnStreamCreatedParam_pPort_get(long var0, OnStreamCreatedParam var2);

    public static final native long new_OnStreamCreatedParam();

    public static final native void delete_OnStreamCreatedParam(long var0);

    public static final native void OnStreamDestroyedParam_stream_set(long var0, OnStreamDestroyedParam var2, long var3);

    public static final native long OnStreamDestroyedParam_stream_get(long var0, OnStreamDestroyedParam var2);

    public static final native void OnStreamDestroyedParam_streamIdx_set(long var0, OnStreamDestroyedParam var2, long var3);

    public static final native long OnStreamDestroyedParam_streamIdx_get(long var0, OnStreamDestroyedParam var2);

    public static final native long new_OnStreamDestroyedParam();

    public static final native void delete_OnStreamDestroyedParam(long var0);

    public static final native void OnDtmfDigitParam_method_set(long var0, OnDtmfDigitParam var2, int var3);

    public static final native int OnDtmfDigitParam_method_get(long var0, OnDtmfDigitParam var2);

    public static final native void OnDtmfDigitParam_digit_set(long var0, OnDtmfDigitParam var2, String var3);

    public static final native String OnDtmfDigitParam_digit_get(long var0, OnDtmfDigitParam var2);

    public static final native void OnDtmfDigitParam_duration_set(long var0, OnDtmfDigitParam var2, long var3);

    public static final native long OnDtmfDigitParam_duration_get(long var0, OnDtmfDigitParam var2);

    public static final native long new_OnDtmfDigitParam();

    public static final native void delete_OnDtmfDigitParam(long var0);

    public static final native void OnCallTransferRequestParam_dstUri_set(long var0, OnCallTransferRequestParam var2, String var3);

    public static final native String OnCallTransferRequestParam_dstUri_get(long var0, OnCallTransferRequestParam var2);

    public static final native void OnCallTransferRequestParam_statusCode_set(long var0, OnCallTransferRequestParam var2, int var3);

    public static final native int OnCallTransferRequestParam_statusCode_get(long var0, OnCallTransferRequestParam var2);

    public static final native void OnCallTransferRequestParam_opt_set(long var0, OnCallTransferRequestParam var2, long var3, CallSetting var5);

    public static final native long OnCallTransferRequestParam_opt_get(long var0, OnCallTransferRequestParam var2);

    public static final native void OnCallTransferRequestParam_newCall_set(long var0, OnCallTransferRequestParam var2, long var3, Call var5);

    public static final native long OnCallTransferRequestParam_newCall_get(long var0, OnCallTransferRequestParam var2);

    public static final native long new_OnCallTransferRequestParam();

    public static final native void delete_OnCallTransferRequestParam(long var0);

    public static final native void OnCallTransferStatusParam_statusCode_set(long var0, OnCallTransferStatusParam var2, int var3);

    public static final native int OnCallTransferStatusParam_statusCode_get(long var0, OnCallTransferStatusParam var2);

    public static final native void OnCallTransferStatusParam_reason_set(long var0, OnCallTransferStatusParam var2, String var3);

    public static final native String OnCallTransferStatusParam_reason_get(long var0, OnCallTransferStatusParam var2);

    public static final native void OnCallTransferStatusParam_finalNotify_set(long var0, OnCallTransferStatusParam var2, boolean var3);

    public static final native boolean OnCallTransferStatusParam_finalNotify_get(long var0, OnCallTransferStatusParam var2);

    public static final native void OnCallTransferStatusParam_cont_set(long var0, OnCallTransferStatusParam var2, boolean var3);

    public static final native boolean OnCallTransferStatusParam_cont_get(long var0, OnCallTransferStatusParam var2);

    public static final native long new_OnCallTransferStatusParam();

    public static final native void delete_OnCallTransferStatusParam(long var0);

    public static final native void OnCallReplaceRequestParam_rdata_set(long var0, OnCallReplaceRequestParam var2, long var3, SipRxData var5);

    public static final native long OnCallReplaceRequestParam_rdata_get(long var0, OnCallReplaceRequestParam var2);

    public static final native void OnCallReplaceRequestParam_statusCode_set(long var0, OnCallReplaceRequestParam var2, int var3);

    public static final native int OnCallReplaceRequestParam_statusCode_get(long var0, OnCallReplaceRequestParam var2);

    public static final native void OnCallReplaceRequestParam_reason_set(long var0, OnCallReplaceRequestParam var2, String var3);

    public static final native String OnCallReplaceRequestParam_reason_get(long var0, OnCallReplaceRequestParam var2);

    public static final native void OnCallReplaceRequestParam_opt_set(long var0, OnCallReplaceRequestParam var2, long var3, CallSetting var5);

    public static final native long OnCallReplaceRequestParam_opt_get(long var0, OnCallReplaceRequestParam var2);

    public static final native long new_OnCallReplaceRequestParam();

    public static final native void delete_OnCallReplaceRequestParam(long var0);

    public static final native void OnCallReplacedParam_newCallId_set(long var0, OnCallReplacedParam var2, int var3);

    public static final native int OnCallReplacedParam_newCallId_get(long var0, OnCallReplacedParam var2);

    public static final native void OnCallReplacedParam_newCall_set(long var0, OnCallReplacedParam var2, long var3, Call var5);

    public static final native long OnCallReplacedParam_newCall_get(long var0, OnCallReplacedParam var2);

    public static final native long new_OnCallReplacedParam();

    public static final native void delete_OnCallReplacedParam(long var0);

    public static final native void OnCallRxOfferParam_offer_set(long var0, OnCallRxOfferParam var2, long var3, SdpSession var5);

    public static final native long OnCallRxOfferParam_offer_get(long var0, OnCallRxOfferParam var2);

    public static final native void OnCallRxOfferParam_statusCode_set(long var0, OnCallRxOfferParam var2, int var3);

    public static final native int OnCallRxOfferParam_statusCode_get(long var0, OnCallRxOfferParam var2);

    public static final native void OnCallRxOfferParam_opt_set(long var0, OnCallRxOfferParam var2, long var3, CallSetting var5);

    public static final native long OnCallRxOfferParam_opt_get(long var0, OnCallRxOfferParam var2);

    public static final native long new_OnCallRxOfferParam();

    public static final native void delete_OnCallRxOfferParam(long var0);

    public static final native void OnCallAnsweredParam_offer_set(long var0, OnCallAnsweredParam var2, long var3, SdpSession var5);

    public static final native long OnCallAnsweredParam_offer_get(long var0, OnCallAnsweredParam var2);

    public static final native void OnCallAnsweredParam_statusCode_set(long var0, OnCallAnsweredParam var2, int var3);

    public static final native int OnCallAnsweredParam_statusCode_get(long var0, OnCallAnsweredParam var2);

    public static final native void OnCallAnsweredParam_opt_set(long var0, OnCallAnsweredParam var2, long var3, CallSetting var5);

    public static final native long OnCallAnsweredParam_opt_get(long var0, OnCallAnsweredParam var2);

    public static final native void OnCallAnsweredParam_rdata_set(long var0, OnCallAnsweredParam var2, long var3, SipRxData var5);

    public static final native long OnCallAnsweredParam_rdata_get(long var0, OnCallAnsweredParam var2);

    public static final native long new_OnCallAnsweredParam();

    public static final native void delete_OnCallAnsweredParam(long var0);

    public static final native void OnCallRxReinviteParam_offer_set(long var0, OnCallRxReinviteParam var2, long var3, SdpSession var5);

    public static final native long OnCallRxReinviteParam_offer_get(long var0, OnCallRxReinviteParam var2);

    public static final native void OnCallRxReinviteParam_rdata_set(long var0, OnCallRxReinviteParam var2, long var3, SipRxData var5);

    public static final native long OnCallRxReinviteParam_rdata_get(long var0, OnCallRxReinviteParam var2);

    public static final native void OnCallRxReinviteParam_isAsync_set(long var0, OnCallRxReinviteParam var2, boolean var3);

    public static final native boolean OnCallRxReinviteParam_isAsync_get(long var0, OnCallRxReinviteParam var2);

    public static final native void OnCallRxReinviteParam_statusCode_set(long var0, OnCallRxReinviteParam var2, int var3);

    public static final native int OnCallRxReinviteParam_statusCode_get(long var0, OnCallRxReinviteParam var2);

    public static final native void OnCallRxReinviteParam_opt_set(long var0, OnCallRxReinviteParam var2, long var3, CallSetting var5);

    public static final native long OnCallRxReinviteParam_opt_get(long var0, OnCallRxReinviteParam var2);

    public static final native long new_OnCallRxReinviteParam();

    public static final native void delete_OnCallRxReinviteParam(long var0);

    public static final native void OnCallTxOfferParam_opt_set(long var0, OnCallTxOfferParam var2, long var3, CallSetting var5);

    public static final native long OnCallTxOfferParam_opt_get(long var0, OnCallTxOfferParam var2);

    public static final native long new_OnCallTxOfferParam();

    public static final native void delete_OnCallTxOfferParam(long var0);

    public static final native void OnCallRedirectedParam_targetUri_set(long var0, OnCallRedirectedParam var2, String var3);

    public static final native String OnCallRedirectedParam_targetUri_get(long var0, OnCallRedirectedParam var2);

    public static final native void OnCallRedirectedParam_e_set(long var0, OnCallRedirectedParam var2, long var3, SipEvent var5);

    public static final native long OnCallRedirectedParam_e_get(long var0, OnCallRedirectedParam var2);

    public static final native long new_OnCallRedirectedParam();

    public static final native void delete_OnCallRedirectedParam(long var0);

    public static final native void OnCallMediaEventParam_medIdx_set(long var0, OnCallMediaEventParam var2, long var3);

    public static final native long OnCallMediaEventParam_medIdx_get(long var0, OnCallMediaEventParam var2);

    public static final native void OnCallMediaEventParam_ev_set(long var0, OnCallMediaEventParam var2, long var3, MediaEvent var5);

    public static final native long OnCallMediaEventParam_ev_get(long var0, OnCallMediaEventParam var2);

    public static final native long new_OnCallMediaEventParam();

    public static final native void delete_OnCallMediaEventParam(long var0);

    public static final native void OnCallMediaTransportStateParam_medIdx_set(long var0, OnCallMediaTransportStateParam var2, long var3);

    public static final native long OnCallMediaTransportStateParam_medIdx_get(long var0, OnCallMediaTransportStateParam var2);

    public static final native void OnCallMediaTransportStateParam_state_set(long var0, OnCallMediaTransportStateParam var2, int var3);

    public static final native int OnCallMediaTransportStateParam_state_get(long var0, OnCallMediaTransportStateParam var2);

    public static final native void OnCallMediaTransportStateParam_status_set(long var0, OnCallMediaTransportStateParam var2, int var3);

    public static final native int OnCallMediaTransportStateParam_status_get(long var0, OnCallMediaTransportStateParam var2);

    public static final native void OnCallMediaTransportStateParam_sipErrorCode_set(long var0, OnCallMediaTransportStateParam var2, int var3);

    public static final native int OnCallMediaTransportStateParam_sipErrorCode_get(long var0, OnCallMediaTransportStateParam var2);

    public static final native long new_OnCallMediaTransportStateParam();

    public static final native void delete_OnCallMediaTransportStateParam(long var0);

    public static final native void OnCreateMediaTransportParam_mediaIdx_set(long var0, OnCreateMediaTransportParam var2, long var3);

    public static final native long OnCreateMediaTransportParam_mediaIdx_get(long var0, OnCreateMediaTransportParam var2);

    public static final native void OnCreateMediaTransportParam_mediaTp_set(long var0, OnCreateMediaTransportParam var2, long var3);

    public static final native long OnCreateMediaTransportParam_mediaTp_get(long var0, OnCreateMediaTransportParam var2);

    public static final native void OnCreateMediaTransportParam_flags_set(long var0, OnCreateMediaTransportParam var2, long var3);

    public static final native long OnCreateMediaTransportParam_flags_get(long var0, OnCreateMediaTransportParam var2);

    public static final native long new_OnCreateMediaTransportParam();

    public static final native void delete_OnCreateMediaTransportParam(long var0);

    public static final native void OnCreateMediaTransportSrtpParam_mediaIdx_set(long var0, OnCreateMediaTransportSrtpParam var2, long var3);

    public static final native long OnCreateMediaTransportSrtpParam_mediaIdx_get(long var0, OnCreateMediaTransportSrtpParam var2);

    public static final native void OnCreateMediaTransportSrtpParam_srtpUse_set(long var0, OnCreateMediaTransportSrtpParam var2, int var3);

    public static final native int OnCreateMediaTransportSrtpParam_srtpUse_get(long var0, OnCreateMediaTransportSrtpParam var2);

    public static final native void OnCreateMediaTransportSrtpParam_cryptos_set(long var0, OnCreateMediaTransportSrtpParam var2, long var3, SrtpCryptoVector var5);

    public static final native long OnCreateMediaTransportSrtpParam_cryptos_get(long var0, OnCreateMediaTransportSrtpParam var2);

    public static final native long new_OnCreateMediaTransportSrtpParam();

    public static final native void delete_OnCreateMediaTransportSrtpParam(long var0);

    public static final native void CallOpParam_opt_set(long var0, CallOpParam var2, long var3, CallSetting var5);

    public static final native long CallOpParam_opt_get(long var0, CallOpParam var2);

    public static final native void CallOpParam_statusCode_set(long var0, CallOpParam var2, int var3);

    public static final native int CallOpParam_statusCode_get(long var0, CallOpParam var2);

    public static final native void CallOpParam_reason_set(long var0, CallOpParam var2, String var3);

    public static final native String CallOpParam_reason_get(long var0, CallOpParam var2);

    public static final native void CallOpParam_options_set(long var0, CallOpParam var2, long var3);

    public static final native long CallOpParam_options_get(long var0, CallOpParam var2);

    public static final native void CallOpParam_txOption_set(long var0, CallOpParam var2, long var3, SipTxOption var5);

    public static final native long CallOpParam_txOption_get(long var0, CallOpParam var2);

    public static final native void CallOpParam_sdp_set(long var0, CallOpParam var2, long var3, SdpSession var5);

    public static final native long CallOpParam_sdp_get(long var0, CallOpParam var2);

    public static final native long new_CallOpParam__SWIG_0(boolean var0);

    public static final native long new_CallOpParam__SWIG_1();

    public static final native void delete_CallOpParam(long var0);

    public static final native void CallSendRequestParam_method_set(long var0, CallSendRequestParam var2, String var3);

    public static final native String CallSendRequestParam_method_get(long var0, CallSendRequestParam var2);

    public static final native void CallSendRequestParam_txOption_set(long var0, CallSendRequestParam var2, long var3, SipTxOption var5);

    public static final native long CallSendRequestParam_txOption_get(long var0, CallSendRequestParam var2);

    public static final native long new_CallSendRequestParam();

    public static final native void delete_CallSendRequestParam(long var0);

    public static final native void CallVidSetStreamParam_medIdx_set(long var0, CallVidSetStreamParam var2, int var3);

    public static final native int CallVidSetStreamParam_medIdx_get(long var0, CallVidSetStreamParam var2);

    public static final native void CallVidSetStreamParam_dir_set(long var0, CallVidSetStreamParam var2, int var3);

    public static final native int CallVidSetStreamParam_dir_get(long var0, CallVidSetStreamParam var2);

    public static final native void CallVidSetStreamParam_capDev_set(long var0, CallVidSetStreamParam var2, int var3);

    public static final native int CallVidSetStreamParam_capDev_get(long var0, CallVidSetStreamParam var2);

    public static final native long new_CallVidSetStreamParam();

    public static final native void delete_CallVidSetStreamParam(long var0);

    public static final native void CallSendDtmfParam_method_set(long var0, CallSendDtmfParam var2, int var3);

    public static final native int CallSendDtmfParam_method_get(long var0, CallSendDtmfParam var2);

    public static final native void CallSendDtmfParam_duration_set(long var0, CallSendDtmfParam var2, long var3);

    public static final native long CallSendDtmfParam_duration_get(long var0, CallSendDtmfParam var2);

    public static final native void CallSendDtmfParam_digits_set(long var0, CallSendDtmfParam var2, String var3);

    public static final native String CallSendDtmfParam_digits_get(long var0, CallSendDtmfParam var2);

    public static final native long new_CallSendDtmfParam();

    public static final native void delete_CallSendDtmfParam(long var0);

    public static final native long new_Call__SWIG_0(long var0, Account var2, int var3);

    public static final native long new_Call__SWIG_1(long var0, Account var2);

    public static final native void delete_Call(long var0);

    public static final native long Call_getInfo(long var0, Call var2) throws Exception;

    public static final native boolean Call_isActive(long var0, Call var2);

    public static final native int Call_getId(long var0, Call var2);

    public static final native long Call_lookup(int var0);

    public static final native boolean Call_hasMedia(long var0, Call var2);

    public static final native long Call_getMedia(long var0, Call var2, long var3);

    public static final native long Call_getAudioMedia(long var0, Call var2, int var3) throws Exception;

    public static final native long Call_getEncodingVideoMedia(long var0, Call var2, int var3) throws Exception;

    public static final native long Call_getDecodingVideoMedia(long var0, Call var2, int var3) throws Exception;

    public static final native int Call_remoteHasCap(long var0, Call var2, int var3, String var4, String var5);

    public static final native void Call_setUserData(long var0, Call var2, long var3);

    public static final native long Call_getUserData(long var0, Call var2);

    public static final native int Call_getRemNatType(long var0, Call var2) throws Exception;

    public static final native void Call_makeCall(long var0, Call var2, String var3, long var4, CallOpParam var6) throws Exception;

    public static final native void Call_answer(long var0, Call var2, long var3, CallOpParam var5) throws Exception;

    public static final native void Call_hangup(long var0, Call var2, long var3, CallOpParam var5) throws Exception;

    public static final native void Call_setHold(long var0, Call var2, long var3, CallOpParam var5) throws Exception;

    public static final native void Call_reinvite(long var0, Call var2, long var3, CallOpParam var5) throws Exception;

    public static final native void Call_update(long var0, Call var2, long var3, CallOpParam var5) throws Exception;

    public static final native void Call_xfer(long var0, Call var2, String var3, long var4, CallOpParam var6) throws Exception;

    public static final native void Call_xferReplaces(long var0, Call var2, long var3, Call var5, long var6, CallOpParam var8) throws Exception;

    public static final native void Call_processRedirect(long var0, Call var2, int var3) throws Exception;

    public static final native void Call_dialDtmf(long var0, Call var2, String var3) throws Exception;

    public static final native void Call_sendDtmf(long var0, Call var2, long var3, CallSendDtmfParam var5) throws Exception;

    public static final native void Call_sendInstantMessage(long var0, Call var2, long var3, SendInstantMessageParam var5) throws Exception;

    public static final native void Call_sendTypingIndication(long var0, Call var2, long var3, SendTypingIndicationParam var5) throws Exception;

    public static final native void Call_sendRequest(long var0, Call var2, long var3, CallSendRequestParam var5) throws Exception;

    public static final native String Call_dump(long var0, Call var2, boolean var3, String var4) throws Exception;

    public static final native int Call_vidGetStreamIdx(long var0, Call var2);

    public static final native boolean Call_vidStreamIsRunning(long var0, Call var2, int var3, int var4);

    public static final native void Call_vidSetStream(long var0, Call var2, int var3, long var4, CallVidSetStreamParam var6) throws Exception;

    public static final native long Call_getStreamInfo(long var0, Call var2, long var3) throws Exception;

    public static final native long Call_getStreamStat(long var0, Call var2, long var3) throws Exception;

    public static final native long Call_getMedTransportInfo(long var0, Call var2, long var3) throws Exception;

    public static final native void Call_processMediaUpdate(long var0, Call var2, long var3, OnCallMediaStateParam var5);

    public static final native void Call_processStateChange(long var0, Call var2, long var3, OnCallStateParam var5);

    public static final native void Call_onCallState(long var0, Call var2, long var3, OnCallStateParam var5);

    public static final native void Call_onCallStateSwigExplicitCall(long var0, Call var2, long var3, OnCallStateParam var5);

    public static final native void Call_onCallTsxState(long var0, Call var2, long var3, OnCallTsxStateParam var5);

    public static final native void Call_onCallTsxStateSwigExplicitCall(long var0, Call var2, long var3, OnCallTsxStateParam var5);

    public static final native void Call_onCallMediaState(long var0, Call var2, long var3, OnCallMediaStateParam var5);

    public static final native void Call_onCallMediaStateSwigExplicitCall(long var0, Call var2, long var3, OnCallMediaStateParam var5);

    public static final native void Call_onCallSdpCreated(long var0, Call var2, long var3, OnCallSdpCreatedParam var5);

    public static final native void Call_onCallSdpCreatedSwigExplicitCall(long var0, Call var2, long var3, OnCallSdpCreatedParam var5);

    public static final native void Call_onStreamCreated(long var0, Call var2, long var3, OnStreamCreatedParam var5);

    public static final native void Call_onStreamCreatedSwigExplicitCall(long var0, Call var2, long var3, OnStreamCreatedParam var5);

    public static final native void Call_onStreamDestroyed(long var0, Call var2, long var3, OnStreamDestroyedParam var5);

    public static final native void Call_onStreamDestroyedSwigExplicitCall(long var0, Call var2, long var3, OnStreamDestroyedParam var5);

    public static final native void Call_onDtmfDigit(long var0, Call var2, long var3, OnDtmfDigitParam var5);

    public static final native void Call_onDtmfDigitSwigExplicitCall(long var0, Call var2, long var3, OnDtmfDigitParam var5);

    public static final native void Call_onCallTransferRequest(long var0, Call var2, long var3, OnCallTransferRequestParam var5);

    public static final native void Call_onCallTransferRequestSwigExplicitCall(long var0, Call var2, long var3, OnCallTransferRequestParam var5);

    public static final native void Call_onCallTransferStatus(long var0, Call var2, long var3, OnCallTransferStatusParam var5);

    public static final native void Call_onCallTransferStatusSwigExplicitCall(long var0, Call var2, long var3, OnCallTransferStatusParam var5);

    public static final native void Call_onCallReplaceRequest(long var0, Call var2, long var3, OnCallReplaceRequestParam var5);

    public static final native void Call_onCallReplaceRequestSwigExplicitCall(long var0, Call var2, long var3, OnCallReplaceRequestParam var5);

    public static final native void Call_onCallReplaced(long var0, Call var2, long var3, OnCallReplacedParam var5);

    public static final native void Call_onCallReplacedSwigExplicitCall(long var0, Call var2, long var3, OnCallReplacedParam var5);

    public static final native void Call_onCallRxOffer(long var0, Call var2, long var3, OnCallRxOfferParam var5);

    public static final native void Call_onCallRxOfferSwigExplicitCall(long var0, Call var2, long var3, OnCallRxOfferParam var5);

    public static final native void Call_onCallAnswered(long var0, Call var2, long var3, OnCallAnsweredParam var5);

    public static final native void Call_onCallAnsweredSwigExplicitCall(long var0, Call var2, long var3, OnCallAnsweredParam var5);

    public static final native void Call_onCallRxReinvite(long var0, Call var2, long var3, OnCallRxReinviteParam var5);

    public static final native void Call_onCallRxReinviteSwigExplicitCall(long var0, Call var2, long var3, OnCallRxReinviteParam var5);

    public static final native void Call_onCallTxOffer(long var0, Call var2, long var3, OnCallTxOfferParam var5);

    public static final native void Call_onCallTxOfferSwigExplicitCall(long var0, Call var2, long var3, OnCallTxOfferParam var5);

    public static final native void Call_onInstantMessage(long var0, Call var2, long var3, OnInstantMessageParam var5);

    public static final native void Call_onInstantMessageSwigExplicitCall(long var0, Call var2, long var3, OnInstantMessageParam var5);

    public static final native void Call_onInstantMessageStatus(long var0, Call var2, long var3, OnInstantMessageStatusParam var5);

    public static final native void Call_onInstantMessageStatusSwigExplicitCall(long var0, Call var2, long var3, OnInstantMessageStatusParam var5);

    public static final native void Call_onTypingIndication(long var0, Call var2, long var3, OnTypingIndicationParam var5);

    public static final native void Call_onTypingIndicationSwigExplicitCall(long var0, Call var2, long var3, OnTypingIndicationParam var5);

    public static final native int Call_onCallRedirected(long var0, Call var2, long var3, OnCallRedirectedParam var5);

    public static final native int Call_onCallRedirectedSwigExplicitCall(long var0, Call var2, long var3, OnCallRedirectedParam var5);

    public static final native void Call_onCallMediaTransportState(long var0, Call var2, long var3, OnCallMediaTransportStateParam var5);

    public static final native void Call_onCallMediaTransportStateSwigExplicitCall(long var0, Call var2, long var3, OnCallMediaTransportStateParam var5);

    public static final native void Call_onCallMediaEvent(long var0, Call var2, long var3, OnCallMediaEventParam var5);

    public static final native void Call_onCallMediaEventSwigExplicitCall(long var0, Call var2, long var3, OnCallMediaEventParam var5);

    public static final native void Call_onCreateMediaTransport(long var0, Call var2, long var3, OnCreateMediaTransportParam var5);

    public static final native void Call_onCreateMediaTransportSwigExplicitCall(long var0, Call var2, long var3, OnCreateMediaTransportParam var5);

    public static final native void Call_onCreateMediaTransportSrtp(long var0, Call var2, long var3, OnCreateMediaTransportSrtpParam var5);

    public static final native void Call_onCreateMediaTransportSrtpSwigExplicitCall(long var0, Call var2, long var3, OnCreateMediaTransportSrtpParam var5);

    public static final native void Call_director_connect(Call var0, long var1, boolean var3, boolean var4);

    public static final native void Call_change_ownership(Call var0, long var1, boolean var3);

    public static final native long new_JsonDocument();

    public static final native void delete_JsonDocument(long var0);

    public static final native void JsonDocument_loadFile(long var0, JsonDocument var2, String var3) throws Exception;

    public static final native void JsonDocument_loadString(long var0, JsonDocument var2, String var3) throws Exception;

    public static final native void JsonDocument_saveFile(long var0, JsonDocument var2, String var3) throws Exception;

    public static final native String JsonDocument_saveString(long var0, JsonDocument var2) throws Exception;

    public static final native long JsonDocument_getRootContainer(long var0, JsonDocument var2);

    public static final native void OnNatDetectionCompleteParam_status_set(long var0, OnNatDetectionCompleteParam var2, int var3);

    public static final native int OnNatDetectionCompleteParam_status_get(long var0, OnNatDetectionCompleteParam var2);

    public static final native void OnNatDetectionCompleteParam_reason_set(long var0, OnNatDetectionCompleteParam var2, String var3);

    public static final native String OnNatDetectionCompleteParam_reason_get(long var0, OnNatDetectionCompleteParam var2);

    public static final native void OnNatDetectionCompleteParam_natType_set(long var0, OnNatDetectionCompleteParam var2, int var3);

    public static final native int OnNatDetectionCompleteParam_natType_get(long var0, OnNatDetectionCompleteParam var2);

    public static final native void OnNatDetectionCompleteParam_natTypeName_set(long var0, OnNatDetectionCompleteParam var2, String var3);

    public static final native String OnNatDetectionCompleteParam_natTypeName_get(long var0, OnNatDetectionCompleteParam var2);

    public static final native long new_OnNatDetectionCompleteParam();

    public static final native void delete_OnNatDetectionCompleteParam(long var0);

    public static final native void OnNatCheckStunServersCompleteParam_userData_set(long var0, OnNatCheckStunServersCompleteParam var2, long var3);

    public static final native long OnNatCheckStunServersCompleteParam_userData_get(long var0, OnNatCheckStunServersCompleteParam var2);

    public static final native void OnNatCheckStunServersCompleteParam_status_set(long var0, OnNatCheckStunServersCompleteParam var2, int var3);

    public static final native int OnNatCheckStunServersCompleteParam_status_get(long var0, OnNatCheckStunServersCompleteParam var2);

    public static final native void OnNatCheckStunServersCompleteParam_name_set(long var0, OnNatCheckStunServersCompleteParam var2, String var3);

    public static final native String OnNatCheckStunServersCompleteParam_name_get(long var0, OnNatCheckStunServersCompleteParam var2);

    public static final native void OnNatCheckStunServersCompleteParam_addr_set(long var0, OnNatCheckStunServersCompleteParam var2, String var3);

    public static final native String OnNatCheckStunServersCompleteParam_addr_get(long var0, OnNatCheckStunServersCompleteParam var2);

    public static final native long new_OnNatCheckStunServersCompleteParam();

    public static final native void delete_OnNatCheckStunServersCompleteParam(long var0);

    public static final native void OnTimerParam_userData_set(long var0, OnTimerParam var2, long var3);

    public static final native long OnTimerParam_userData_get(long var0, OnTimerParam var2);

    public static final native void OnTimerParam_msecDelay_set(long var0, OnTimerParam var2, long var3);

    public static final native long OnTimerParam_msecDelay_get(long var0, OnTimerParam var2);

    public static final native long new_OnTimerParam();

    public static final native void delete_OnTimerParam(long var0);

    public static final native void SslCertName_type_set(long var0, SslCertName var2, int var3);

    public static final native int SslCertName_type_get(long var0, SslCertName var2);

    public static final native void SslCertName_name_set(long var0, SslCertName var2, String var3);

    public static final native String SslCertName_name_get(long var0, SslCertName var2);

    public static final native long new_SslCertName();

    public static final native void delete_SslCertName(long var0);

    public static final native void SslCertInfo_version_set(long var0, SslCertInfo var2, long var3);

    public static final native long SslCertInfo_version_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_serialNo_set(long var0, SslCertInfo var2, long var3);

    public static final native long SslCertInfo_serialNo_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_subjectCn_set(long var0, SslCertInfo var2, String var3);

    public static final native String SslCertInfo_subjectCn_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_subjectInfo_set(long var0, SslCertInfo var2, String var3);

    public static final native String SslCertInfo_subjectInfo_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_issuerCn_set(long var0, SslCertInfo var2, String var3);

    public static final native String SslCertInfo_issuerCn_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_issuerInfo_set(long var0, SslCertInfo var2, String var3);

    public static final native String SslCertInfo_issuerInfo_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_validityStart_set(long var0, SslCertInfo var2, long var3, TimeVal var5);

    public static final native long SslCertInfo_validityStart_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_validityEnd_set(long var0, SslCertInfo var2, long var3, TimeVal var5);

    public static final native long SslCertInfo_validityEnd_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_validityGmt_set(long var0, SslCertInfo var2, boolean var3);

    public static final native boolean SslCertInfo_validityGmt_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_subjectAltName_set(long var0, SslCertInfo var2, long var3, StringVector var5);

    public static final native long SslCertInfo_subjectAltName_get(long var0, SslCertInfo var2);

    public static final native void SslCertInfo_raw_set(long var0, SslCertInfo var2, String var3);

    public static final native String SslCertInfo_raw_get(long var0, SslCertInfo var2);

    public static final native long new_SslCertInfo();

    public static final native boolean SslCertInfo_isEmpty(long var0, SslCertInfo var2);

    public static final native void delete_SslCertInfo(long var0);

    public static final native void TlsInfo_established_set(long var0, TlsInfo var2, boolean var3);

    public static final native boolean TlsInfo_established_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_protocol_set(long var0, TlsInfo var2, long var3);

    public static final native long TlsInfo_protocol_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_cipher_set(long var0, TlsInfo var2, int var3);

    public static final native int TlsInfo_cipher_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_cipherName_set(long var0, TlsInfo var2, String var3);

    public static final native String TlsInfo_cipherName_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_localAddr_set(long var0, TlsInfo var2, String var3);

    public static final native String TlsInfo_localAddr_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_remoteAddr_set(long var0, TlsInfo var2, String var3);

    public static final native String TlsInfo_remoteAddr_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_localCertInfo_set(long var0, TlsInfo var2, long var3, SslCertInfo var5);

    public static final native long TlsInfo_localCertInfo_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_remoteCertInfo_set(long var0, TlsInfo var2, long var3, SslCertInfo var5);

    public static final native long TlsInfo_remoteCertInfo_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_verifyStatus_set(long var0, TlsInfo var2, long var3);

    public static final native long TlsInfo_verifyStatus_get(long var0, TlsInfo var2);

    public static final native void TlsInfo_verifyMsgs_set(long var0, TlsInfo var2, long var3, StringVector var5);

    public static final native long TlsInfo_verifyMsgs_get(long var0, TlsInfo var2);

    public static final native long new_TlsInfo();

    public static final native boolean TlsInfo_isEmpty(long var0, TlsInfo var2);

    public static final native void delete_TlsInfo(long var0);

    public static final native void OnTransportStateParam_hnd_set(long var0, OnTransportStateParam var2, long var3);

    public static final native long OnTransportStateParam_hnd_get(long var0, OnTransportStateParam var2);

    public static final native void OnTransportStateParam_type_set(long var0, OnTransportStateParam var2, String var3);

    public static final native String OnTransportStateParam_type_get(long var0, OnTransportStateParam var2);

    public static final native void OnTransportStateParam_state_set(long var0, OnTransportStateParam var2, int var3);

    public static final native int OnTransportStateParam_state_get(long var0, OnTransportStateParam var2);

    public static final native void OnTransportStateParam_lastError_set(long var0, OnTransportStateParam var2, int var3);

    public static final native int OnTransportStateParam_lastError_get(long var0, OnTransportStateParam var2);

    public static final native void OnTransportStateParam_tlsInfo_set(long var0, OnTransportStateParam var2, long var3, TlsInfo var5);

    public static final native long OnTransportStateParam_tlsInfo_get(long var0, OnTransportStateParam var2);

    public static final native long new_OnTransportStateParam();

    public static final native void delete_OnTransportStateParam(long var0);

    public static final native void OnSelectAccountParam_rdata_set(long var0, OnSelectAccountParam var2, long var3, SipRxData var5);

    public static final native long OnSelectAccountParam_rdata_get(long var0, OnSelectAccountParam var2);

    public static final native void OnSelectAccountParam_accountIndex_set(long var0, OnSelectAccountParam var2, int var3);

    public static final native int OnSelectAccountParam_accountIndex_get(long var0, OnSelectAccountParam var2);

    public static final native long new_OnSelectAccountParam();

    public static final native void delete_OnSelectAccountParam(long var0);

    public static final native void IpChangeParam_restartListener_set(long var0, IpChangeParam var2, boolean var3);

    public static final native boolean IpChangeParam_restartListener_get(long var0, IpChangeParam var2);

    public static final native void IpChangeParam_restartLisDelay_set(long var0, IpChangeParam var2, long var3);

    public static final native long IpChangeParam_restartLisDelay_get(long var0, IpChangeParam var2);

    public static final native long new_IpChangeParam();

    public static final native void delete_IpChangeParam(long var0);

    public static final native void RegProgressParam_isRegister_set(long var0, RegProgressParam var2, boolean var3);

    public static final native boolean RegProgressParam_isRegister_get(long var0, RegProgressParam var2);

    public static final native void RegProgressParam_code_set(long var0, RegProgressParam var2, int var3);

    public static final native int RegProgressParam_code_get(long var0, RegProgressParam var2);

    public static final native long new_RegProgressParam();

    public static final native void delete_RegProgressParam(long var0);

    public static final native void OnIpChangeProgressParam_op_set(long var0, OnIpChangeProgressParam var2, int var3);

    public static final native int OnIpChangeProgressParam_op_get(long var0, OnIpChangeProgressParam var2);

    public static final native void OnIpChangeProgressParam_status_set(long var0, OnIpChangeProgressParam var2, int var3);

    public static final native int OnIpChangeProgressParam_status_get(long var0, OnIpChangeProgressParam var2);

    public static final native void OnIpChangeProgressParam_transportId_set(long var0, OnIpChangeProgressParam var2, int var3);

    public static final native int OnIpChangeProgressParam_transportId_get(long var0, OnIpChangeProgressParam var2);

    public static final native void OnIpChangeProgressParam_accId_set(long var0, OnIpChangeProgressParam var2, int var3);

    public static final native int OnIpChangeProgressParam_accId_get(long var0, OnIpChangeProgressParam var2);

    public static final native void OnIpChangeProgressParam_callId_set(long var0, OnIpChangeProgressParam var2, int var3);

    public static final native int OnIpChangeProgressParam_callId_get(long var0, OnIpChangeProgressParam var2);

    public static final native void OnIpChangeProgressParam_regInfo_set(long var0, OnIpChangeProgressParam var2, long var3, RegProgressParam var5);

    public static final native long OnIpChangeProgressParam_regInfo_get(long var0, OnIpChangeProgressParam var2);

    public static final native long new_OnIpChangeProgressParam();

    public static final native void delete_OnIpChangeProgressParam(long var0);

    public static final native void OnMediaEventParam_ev_set(long var0, OnMediaEventParam var2, long var3, MediaEvent var5);

    public static final native long OnMediaEventParam_ev_get(long var0, OnMediaEventParam var2);

    public static final native long new_OnMediaEventParam();

    public static final native void delete_OnMediaEventParam(long var0);

    public static final native void UaConfig_maxCalls_set(long var0, UaConfig var2, long var3);

    public static final native long UaConfig_maxCalls_get(long var0, UaConfig var2);

    public static final native void UaConfig_threadCnt_set(long var0, UaConfig var2, long var3);

    public static final native long UaConfig_threadCnt_get(long var0, UaConfig var2);

    public static final native void UaConfig_mainThreadOnly_set(long var0, UaConfig var2, boolean var3);

    public static final native boolean UaConfig_mainThreadOnly_get(long var0, UaConfig var2);

    public static final native void UaConfig_nameserver_set(long var0, UaConfig var2, long var3, StringVector var5);

    public static final native long UaConfig_nameserver_get(long var0, UaConfig var2);

    public static final native void UaConfig_outboundProxies_set(long var0, UaConfig var2, long var3, StringVector var5);

    public static final native long UaConfig_outboundProxies_get(long var0, UaConfig var2);

    public static final native void UaConfig_userAgent_set(long var0, UaConfig var2, String var3);

    public static final native String UaConfig_userAgent_get(long var0, UaConfig var2);

    public static final native void UaConfig_stunServer_set(long var0, UaConfig var2, long var3, StringVector var5);

    public static final native long UaConfig_stunServer_get(long var0, UaConfig var2);

    public static final native void UaConfig_stunTryIpv6_set(long var0, UaConfig var2, boolean var3);

    public static final native boolean UaConfig_stunTryIpv6_get(long var0, UaConfig var2);

    public static final native void UaConfig_stunIgnoreFailure_set(long var0, UaConfig var2, boolean var3);

    public static final native boolean UaConfig_stunIgnoreFailure_get(long var0, UaConfig var2);

    public static final native void UaConfig_natTypeInSdp_set(long var0, UaConfig var2, int var3);

    public static final native int UaConfig_natTypeInSdp_get(long var0, UaConfig var2);

    public static final native void UaConfig_mwiUnsolicitedEnabled_set(long var0, UaConfig var2, boolean var3);

    public static final native boolean UaConfig_mwiUnsolicitedEnabled_get(long var0, UaConfig var2);

    public static final native long new_UaConfig();

    public static final native void UaConfig_readObject(long var0, UaConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void UaConfig_writeObject(long var0, UaConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_UaConfig(long var0);

    public static final native void LogEntry_level_set(long var0, LogEntry var2, int var3);

    public static final native int LogEntry_level_get(long var0, LogEntry var2);

    public static final native void LogEntry_msg_set(long var0, LogEntry var2, String var3);

    public static final native String LogEntry_msg_get(long var0, LogEntry var2);

    public static final native void LogEntry_threadId_set(long var0, LogEntry var2, int var3);

    public static final native int LogEntry_threadId_get(long var0, LogEntry var2);

    public static final native void LogEntry_threadName_set(long var0, LogEntry var2, String var3);

    public static final native String LogEntry_threadName_get(long var0, LogEntry var2);

    public static final native long new_LogEntry();

    public static final native void delete_LogEntry(long var0);

    public static final native void delete_LogWriter(long var0);

    public static final native void LogWriter_write(long var0, LogWriter var2, long var3, LogEntry var5);

    public static final native long new_LogWriter();

    public static final native void LogWriter_director_connect(LogWriter var0, long var1, boolean var3, boolean var4);

    public static final native void LogWriter_change_ownership(LogWriter var0, long var1, boolean var3);

    public static final native void LogConfig_msgLogging_set(long var0, LogConfig var2, long var3);

    public static final native long LogConfig_msgLogging_get(long var0, LogConfig var2);

    public static final native void LogConfig_level_set(long var0, LogConfig var2, long var3);

    public static final native long LogConfig_level_get(long var0, LogConfig var2);

    public static final native void LogConfig_consoleLevel_set(long var0, LogConfig var2, long var3);

    public static final native long LogConfig_consoleLevel_get(long var0, LogConfig var2);

    public static final native void LogConfig_decor_set(long var0, LogConfig var2, long var3);

    public static final native long LogConfig_decor_get(long var0, LogConfig var2);

    public static final native void LogConfig_filename_set(long var0, LogConfig var2, String var3);

    public static final native String LogConfig_filename_get(long var0, LogConfig var2);

    public static final native void LogConfig_fileFlags_set(long var0, LogConfig var2, long var3);

    public static final native long LogConfig_fileFlags_get(long var0, LogConfig var2);

    public static final native void LogConfig_writer_set(long var0, LogConfig var2, long var3, LogWriter var5);

    public static final native long LogConfig_writer_get(long var0, LogConfig var2);

    public static final native long new_LogConfig();

    public static final native void LogConfig_readObject(long var0, LogConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void LogConfig_writeObject(long var0, LogConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_LogConfig(long var0);

    public static final native void MediaConfig_clockRate_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_clockRate_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_sndClockRate_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_sndClockRate_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_channelCount_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_channelCount_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_audioFramePtime_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_audioFramePtime_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_maxMediaPorts_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_maxMediaPorts_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_hasIoqueue_set(long var0, MediaConfig var2, boolean var3);

    public static final native boolean MediaConfig_hasIoqueue_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_threadCnt_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_threadCnt_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_quality_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_quality_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_ptime_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_ptime_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_noVad_set(long var0, MediaConfig var2, boolean var3);

    public static final native boolean MediaConfig_noVad_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_ilbcMode_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_ilbcMode_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_txDropPct_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_txDropPct_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_rxDropPct_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_rxDropPct_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_ecOptions_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_ecOptions_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_ecTailLen_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_ecTailLen_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_sndRecLatency_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_sndRecLatency_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_sndPlayLatency_set(long var0, MediaConfig var2, long var3);

    public static final native long MediaConfig_sndPlayLatency_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_jbInit_set(long var0, MediaConfig var2, int var3);

    public static final native int MediaConfig_jbInit_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_jbMinPre_set(long var0, MediaConfig var2, int var3);

    public static final native int MediaConfig_jbMinPre_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_jbMaxPre_set(long var0, MediaConfig var2, int var3);

    public static final native int MediaConfig_jbMaxPre_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_jbMax_set(long var0, MediaConfig var2, int var3);

    public static final native int MediaConfig_jbMax_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_sndAutoCloseTime_set(long var0, MediaConfig var2, int var3);

    public static final native int MediaConfig_sndAutoCloseTime_get(long var0, MediaConfig var2);

    public static final native void MediaConfig_vidPreviewEnableNative_set(long var0, MediaConfig var2, boolean var3);

    public static final native boolean MediaConfig_vidPreviewEnableNative_get(long var0, MediaConfig var2);

    public static final native long new_MediaConfig();

    public static final native void MediaConfig_readObject(long var0, MediaConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void MediaConfig_writeObject(long var0, MediaConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void delete_MediaConfig(long var0);

    public static final native void EpConfig_uaConfig_set(long var0, EpConfig var2, long var3, UaConfig var5);

    public static final native long EpConfig_uaConfig_get(long var0, EpConfig var2);

    public static final native void EpConfig_logConfig_set(long var0, EpConfig var2, long var3, LogConfig var5);

    public static final native long EpConfig_logConfig_get(long var0, EpConfig var2);

    public static final native void EpConfig_medConfig_set(long var0, EpConfig var2, long var3, MediaConfig var5);

    public static final native long EpConfig_medConfig_get(long var0, EpConfig var2);

    public static final native void EpConfig_readObject(long var0, EpConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native void EpConfig_writeObject(long var0, EpConfig var2, long var3, ContainerNode var5) throws Exception;

    public static final native long new_EpConfig();

    public static final native void delete_EpConfig(long var0);

    public static final native void PendingJob_execute(long var0, PendingJob var2, boolean var3);

    public static final native void delete_PendingJob(long var0);

    public static final native long Endpoint_instance() throws Exception;

    public static final native long new_Endpoint();

    public static final native void delete_Endpoint(long var0);

    public static final native long Endpoint_libVersion(long var0, Endpoint var2);

    public static final native void Endpoint_libCreate(long var0, Endpoint var2) throws Exception;

    public static final native int Endpoint_libGetState(long var0, Endpoint var2);

    public static final native void Endpoint_libInit(long var0, Endpoint var2, long var3, EpConfig var5) throws Exception;

    public static final native void Endpoint_libStart(long var0, Endpoint var2) throws Exception;

    public static final native void Endpoint_libRegisterThread(long var0, Endpoint var2, String var3) throws Exception;

    public static final native boolean Endpoint_libIsThreadRegistered(long var0, Endpoint var2);

    public static final native void Endpoint_libStopWorkerThreads(long var0, Endpoint var2);

    public static final native int Endpoint_libHandleEvents(long var0, Endpoint var2, long var3);

    public static final native void Endpoint_libDestroy___SWIG_0(long var0, Endpoint var2, long var3) throws Exception;

    public static final native void Endpoint_libDestroy___SWIG_1(long var0, Endpoint var2) throws Exception;

    public static final native String Endpoint_utilStrError(long var0, Endpoint var2, int var3);

    public static final native void Endpoint_utilLogWrite__SWIG_0(long var0, Endpoint var2, int var3, String var4, String var5);

    public static final native void Endpoint_utilLogWrite__SWIG_1(long var0, Endpoint var2, long var3, LogEntry var5);

    public static final native int Endpoint_utilVerifySipUri(long var0, Endpoint var2, String var3);

    public static final native int Endpoint_utilVerifyUri(long var0, Endpoint var2, String var3);

    public static final native long Endpoint_utilTimerSchedule(long var0, Endpoint var2, long var3, long var5) throws Exception;

    public static final native void Endpoint_utilTimerCancel(long var0, Endpoint var2, long var3);

    public static final native void Endpoint_utilAddPendingJob(long var0, Endpoint var2, long var3, PendingJob var5);

    public static final native long Endpoint_utilSslGetAvailableCiphers(long var0, Endpoint var2) throws Exception;

    public static final native void Endpoint_natDetectType(long var0, Endpoint var2) throws Exception;

    public static final native int Endpoint_natGetType(long var0, Endpoint var2) throws Exception;

    public static final native void Endpoint_natUpdateStunServers(long var0, Endpoint var2, long var3, StringVector var5, boolean var6) throws Exception;

    public static final native void Endpoint_natCheckStunServers(long var0, Endpoint var2, long var3, StringVector var5, boolean var6, long var7) throws Exception;

    public static final native void Endpoint_natCancelCheckStunServers__SWIG_0(long var0, Endpoint var2, long var3, boolean var5) throws Exception;

    public static final native void Endpoint_natCancelCheckStunServers__SWIG_1(long var0, Endpoint var2, long var3) throws Exception;

    public static final native int Endpoint_transportCreate(long var0, Endpoint var2, int var3, long var4, TransportConfig var6) throws Exception;

    public static final native long Endpoint_transportEnum(long var0, Endpoint var2) throws Exception;

    public static final native long Endpoint_transportGetInfo(long var0, Endpoint var2, int var3) throws Exception;

    public static final native void Endpoint_transportSetEnable(long var0, Endpoint var2, int var3, boolean var4) throws Exception;

    public static final native void Endpoint_transportClose(long var0, Endpoint var2, int var3) throws Exception;

    public static final native void Endpoint_transportShutdown(long var0, Endpoint var2, long var3) throws Exception;

    public static final native void Endpoint_hangupAllCalls(long var0, Endpoint var2);

    public static final native void Endpoint_mediaAdd(long var0, Endpoint var2, long var3, AudioMedia var5);

    public static final native void Endpoint_mediaRemove(long var0, Endpoint var2, long var3, AudioMedia var5);

    public static final native boolean Endpoint_mediaExists(long var0, Endpoint var2, long var3, AudioMedia var5);

    public static final native long Endpoint_mediaMaxPorts(long var0, Endpoint var2);

    public static final native long Endpoint_mediaActivePorts(long var0, Endpoint var2);

    public static final native long Endpoint_mediaEnumPorts2(long var0, Endpoint var2) throws Exception;

    public static final native long Endpoint_mediaEnumVidPorts(long var0, Endpoint var2) throws Exception;

    public static final native long Endpoint_audDevManager(long var0, Endpoint var2);

    public static final native long Endpoint_vidDevManager(long var0, Endpoint var2);

    public static final native long Endpoint_codecEnum2(long var0, Endpoint var2) throws Exception;

    public static final native void Endpoint_codecSetPriority(long var0, Endpoint var2, String var3, short var4) throws Exception;

    public static final native long Endpoint_codecGetParam(long var0, Endpoint var2, String var3) throws Exception;

    public static final native void Endpoint_codecSetParam(long var0, Endpoint var2, String var3, long var4, CodecParam var6) throws Exception;

    public static final native long Endpoint_videoCodecEnum2(long var0, Endpoint var2) throws Exception;

    public static final native void Endpoint_videoCodecSetPriority(long var0, Endpoint var2, String var3, short var4) throws Exception;

    public static final native long Endpoint_getVideoCodecParam(long var0, Endpoint var2, String var3) throws Exception;

    public static final native void Endpoint_setVideoCodecParam(long var0, Endpoint var2, String var3, long var4, VidCodecParam var6) throws Exception;

    public static final native void Endpoint_resetVideoCodecParam(long var0, Endpoint var2, String var3) throws Exception;

    public static final native long Endpoint_srtpCryptoEnum(long var0, Endpoint var2) throws Exception;

    public static final native void Endpoint_handleIpChange(long var0, Endpoint var2, long var3, IpChangeParam var5) throws Exception;

    public static final native void Endpoint_onNatDetectionComplete(long var0, Endpoint var2, long var3, OnNatDetectionCompleteParam var5);

    public static final native void Endpoint_onNatDetectionCompleteSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnNatDetectionCompleteParam var5);

    public static final native void Endpoint_onNatCheckStunServersComplete(long var0, Endpoint var2, long var3, OnNatCheckStunServersCompleteParam var5);

    public static final native void Endpoint_onNatCheckStunServersCompleteSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnNatCheckStunServersCompleteParam var5);

    public static final native void Endpoint_onTransportState(long var0, Endpoint var2, long var3, OnTransportStateParam var5);

    public static final native void Endpoint_onTransportStateSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnTransportStateParam var5);

    public static final native void Endpoint_onTimer(long var0, Endpoint var2, long var3, OnTimerParam var5);

    public static final native void Endpoint_onTimerSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnTimerParam var5);

    public static final native void Endpoint_onSelectAccount(long var0, Endpoint var2, long var3, OnSelectAccountParam var5);

    public static final native void Endpoint_onSelectAccountSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnSelectAccountParam var5);

    public static final native void Endpoint_onIpChangeProgress(long var0, Endpoint var2, long var3, OnIpChangeProgressParam var5);

    public static final native void Endpoint_onIpChangeProgressSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnIpChangeProgressParam var5);

    public static final native void Endpoint_onMediaEvent(long var0, Endpoint var2, long var3, OnMediaEventParam var5);

    public static final native void Endpoint_onMediaEventSwigExplicitEndpoint(long var0, Endpoint var2, long var3, OnMediaEventParam var5);

    public static final native void Endpoint_director_connect(Endpoint var0, long var1, boolean var3, boolean var4);

    public static final native void Endpoint_change_ownership(Endpoint var0, long var1, boolean var3);

    public static final native long AuthCredInfo_SWIGUpcast(long var0);

    public static final native long TlsConfig_SWIGUpcast(long var0);

    public static final native long TransportConfig_SWIGUpcast(long var0);

    public static final native long MediaFormatAudio_SWIGUpcast(long var0);

    public static final native long MediaFormatVideo_SWIGUpcast(long var0);

    public static final native long AudioMedia_SWIGUpcast(long var0);

    public static final native long AudioMediaPlayer_SWIGUpcast(long var0);

    public static final native long AudioMediaRecorder_SWIGUpcast(long var0);

    public static final native long ToneDesc_SWIGUpcast(long var0);

    public static final native long ToneDigit_SWIGUpcast(long var0);

    public static final native long ToneGenerator_SWIGUpcast(long var0);

    public static final native long ExtraAudioDevice_SWIGUpcast(long var0);

    public static final native long VideoMedia_SWIGUpcast(long var0);

    public static final native long BuddyConfig_SWIGUpcast(long var0);

    public static final native long AccountRegConfig_SWIGUpcast(long var0);

    public static final native long AccountSipConfig_SWIGUpcast(long var0);

    public static final native long AccountCallConfig_SWIGUpcast(long var0);

    public static final native long AccountPresConfig_SWIGUpcast(long var0);

    public static final native long AccountMwiConfig_SWIGUpcast(long var0);

    public static final native long AccountNatConfig_SWIGUpcast(long var0);

    public static final native long SrtpOpt_SWIGUpcast(long var0);

    public static final native long RtcpFbConfig_SWIGUpcast(long var0);

    public static final native long AccountMediaConfig_SWIGUpcast(long var0);

    public static final native long AccountVideoConfig_SWIGUpcast(long var0);

    public static final native long AccountConfig_SWIGUpcast(long var0);

    public static final native long JsonDocument_SWIGUpcast(long var0);

    public static final native long UaConfig_SWIGUpcast(long var0);

    public static final native long LogConfig_SWIGUpcast(long var0);

    public static final native long MediaConfig_SWIGUpcast(long var0);

    public static final native long EpConfig_SWIGUpcast(long var0);

    public static void SwigDirector_AudioMediaPlayer_onEof2(AudioMediaPlayer jself) {
        jself.onEof2();
    }

    public static void SwigDirector_Buddy_onBuddyState(Buddy jself) {
        jself.onBuddyState();
    }

    public static void SwigDirector_Buddy_onBuddyEvSubState(Buddy jself, long prm) {
        jself.onBuddyEvSubState(new OnBuddyEvSubStateParam(prm, false));
    }

    public static boolean SwigDirector_FindBuddyMatch_match(FindBuddyMatch jself, String token, long buddy) {
        return jself.match(token, new Buddy(buddy, false));
    }

    public static void SwigDirector_Account_onIncomingCall(Account jself, long prm) {
        jself.onIncomingCall(new OnIncomingCallParam(prm, false));
    }

    public static void SwigDirector_Account_onRegStarted(Account jself, long prm) {
        jself.onRegStarted(new OnRegStartedParam(prm, false));
    }

    public static void SwigDirector_Account_onRegState(Account jself, long prm) {
        jself.onRegState(new OnRegStateParam(prm, false));
    }

    public static void SwigDirector_Account_onIncomingSubscribe(Account jself, long prm) {
        jself.onIncomingSubscribe(new OnIncomingSubscribeParam(prm, false));
    }

    public static void SwigDirector_Account_onInstantMessage(Account jself, long prm) {
        jself.onInstantMessage(new OnInstantMessageParam(prm, false));
    }

    public static void SwigDirector_Account_onInstantMessageStatus(Account jself, long prm) {
        jself.onInstantMessageStatus(new OnInstantMessageStatusParam(prm, false));
    }

    public static void SwigDirector_Account_onTypingIndication(Account jself, long prm) {
        jself.onTypingIndication(new OnTypingIndicationParam(prm, false));
    }

    public static void SwigDirector_Account_onMwiInfo(Account jself, long prm) {
        jself.onMwiInfo(new OnMwiInfoParam(prm, false));
    }

    public static void SwigDirector_Call_onCallState(Call jself, long prm) {
        jself.onCallState(new OnCallStateParam(prm, false));
    }

    public static void SwigDirector_Call_onCallTsxState(Call jself, long prm) {
        jself.onCallTsxState(new OnCallTsxStateParam(prm, false));
    }

    public static void SwigDirector_Call_onCallMediaState(Call jself, long prm) {
        jself.onCallMediaState(new OnCallMediaStateParam(prm, false));
    }

    public static void SwigDirector_Call_onCallSdpCreated(Call jself, long prm) {
        jself.onCallSdpCreated(new OnCallSdpCreatedParam(prm, false));
    }

    public static void SwigDirector_Call_onStreamCreated(Call jself, long prm) {
        jself.onStreamCreated(new OnStreamCreatedParam(prm, false));
    }

    public static void SwigDirector_Call_onStreamDestroyed(Call jself, long prm) {
        jself.onStreamDestroyed(new OnStreamDestroyedParam(prm, false));
    }

    public static void SwigDirector_Call_onDtmfDigit(Call jself, long prm) {
        jself.onDtmfDigit(new OnDtmfDigitParam(prm, false));
    }

    public static void SwigDirector_Call_onCallTransferRequest(Call jself, long prm) {
        jself.onCallTransferRequest(new OnCallTransferRequestParam(prm, false));
    }

    public static void SwigDirector_Call_onCallTransferStatus(Call jself, long prm) {
        jself.onCallTransferStatus(new OnCallTransferStatusParam(prm, false));
    }

    public static void SwigDirector_Call_onCallReplaceRequest(Call jself, long prm) {
        jself.onCallReplaceRequest(new OnCallReplaceRequestParam(prm, false));
    }

    public static void SwigDirector_Call_onCallReplaced(Call jself, long prm) {
        jself.onCallReplaced(new OnCallReplacedParam(prm, false));
    }

    public static void SwigDirector_Call_onCallRxOffer(Call jself, long prm) {
        jself.onCallRxOffer(new OnCallRxOfferParam(prm, false));
    }

    public static void SwigDirector_Call_onCallAnswered(Call jself, long prm) {
        jself.onCallAnswered(new OnCallAnsweredParam(prm, false));
    }

    public static void SwigDirector_Call_onCallRxReinvite(Call jself, long prm) {
        jself.onCallRxReinvite(new OnCallRxReinviteParam(prm, false));
    }

    public static void SwigDirector_Call_onCallTxOffer(Call jself, long prm) {
        jself.onCallTxOffer(new OnCallTxOfferParam(prm, false));
    }

    public static void SwigDirector_Call_onInstantMessage(Call jself, long prm) {
        jself.onInstantMessage(new OnInstantMessageParam(prm, false));
    }

    public static void SwigDirector_Call_onInstantMessageStatus(Call jself, long prm) {
        jself.onInstantMessageStatus(new OnInstantMessageStatusParam(prm, false));
    }

    public static void SwigDirector_Call_onTypingIndication(Call jself, long prm) {
        jself.onTypingIndication(new OnTypingIndicationParam(prm, false));
    }

    public static int SwigDirector_Call_onCallRedirected(Call jself, long prm) {
        return jself.onCallRedirected(new OnCallRedirectedParam(prm, false));
    }

    public static void SwigDirector_Call_onCallMediaTransportState(Call jself, long prm) {
        jself.onCallMediaTransportState(new OnCallMediaTransportStateParam(prm, false));
    }

    public static void SwigDirector_Call_onCallMediaEvent(Call jself, long prm) {
        jself.onCallMediaEvent(new OnCallMediaEventParam(prm, false));
    }

    public static void SwigDirector_Call_onCreateMediaTransport(Call jself, long prm) {
        jself.onCreateMediaTransport(new OnCreateMediaTransportParam(prm, false));
    }

    public static void SwigDirector_Call_onCreateMediaTransportSrtp(Call jself, long prm) {
        jself.onCreateMediaTransportSrtp(new OnCreateMediaTransportSrtpParam(prm, false));
    }

    public static void SwigDirector_LogWriter_write(LogWriter jself, long entry) {
        jself.write(new LogEntry(entry, false));
    }

    public static void SwigDirector_Endpoint_onNatDetectionComplete(Endpoint jself, long prm) {
        jself.onNatDetectionComplete(new OnNatDetectionCompleteParam(prm, false));
    }

    public static void SwigDirector_Endpoint_onNatCheckStunServersComplete(Endpoint jself, long prm) {
        jself.onNatCheckStunServersComplete(new OnNatCheckStunServersCompleteParam(prm, false));
    }

    public static void SwigDirector_Endpoint_onTransportState(Endpoint jself, long prm) {
        jself.onTransportState(new OnTransportStateParam(prm, false));
    }

    public static void SwigDirector_Endpoint_onTimer(Endpoint jself, long prm) {
        jself.onTimer(new OnTimerParam(prm, false));
    }

    public static void SwigDirector_Endpoint_onSelectAccount(Endpoint jself, long prm) {
        jself.onSelectAccount(new OnSelectAccountParam(prm, false));
    }

    public static void SwigDirector_Endpoint_onIpChangeProgress(Endpoint jself, long prm) {
        jself.onIpChangeProgress(new OnIpChangeProgressParam(prm, false));
    }

    public static void SwigDirector_Endpoint_onMediaEvent(Endpoint jself, long prm) {
        jself.onMediaEvent(new OnMediaEventParam(prm, false));
    }

    private static final native void swig_module_init();

    static {
        pjsua2JNI.swig_module_init();
    }
}

