/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip.pjsip;

import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.CallState;
import com.korevg.mv.webrtcsdk.session.CallTermination;
import com.korevg.mv.webrtcsdk.session.CallTransferState;
import com.korevg.mv.webrtcsdk.session.DTMF;
import com.korevg.mv.webrtcsdk.session.InfoAlert;
import com.korevg.mv.webrtcsdk.session.NotifyEvent;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.session.TerminationInfo;
import com.korevg.mv.webrtcsdk.sip.ACSipCall;
import com.korevg.mv.webrtcsdk.sip.CallDialog;
import com.korevg.mv.webrtcsdk.sip.SIPUriParser;
import com.korevg.mv.webrtcsdk.sip.pjsip.ACPjSipManager;
import com.korevg.mv.webrtcsdk.useragent.ACConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSendRequestParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.OnCallAnsweredParam;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallRedirectedParam;
import org.pjsip.pjsua2.OnCallReplaceRequestParam;
import org.pjsip.pjsua2.OnCallReplacedParam;
import org.pjsip.pjsua2.OnCallRxOfferParam;
import org.pjsip.pjsua2.OnCallSdpCreatedParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnCallTransferRequestParam;
import org.pjsip.pjsua2.OnCallTransferStatusParam;
import org.pjsip.pjsua2.OnCallTsxStateParam;
import org.pjsip.pjsua2.OnCallTxOfferParam;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.SipEventBody;
import org.pjsip.pjsua2.SipHeader;
import org.pjsip.pjsua2.SipHeaderVector;
import org.pjsip.pjsua2.SipTxOption;
import org.pjsip.pjsua2.TimeVal;
import org.pjsip.pjsua2.TsxStateEvent;

public class ACPjSipCall
extends ACSipCall {
    private static final String TAG = "ACPjSipCall";
    public PjSipCall call;
    private boolean userTerminated = false;

    public ACPjSipCall(ACPjSipManager.ACPjSipAccount sipAccount, int callId) {
        this.call = new PjSipCall(sipAccount, callId);
        if (callId >= 0) {
            try {
                this.remoteContact = SIPUriParser.parseSipUri(this.call.getInfo().getRemoteUri());
            }
            catch (Exception e) {
                Log.d(TAG, "oops: ", e);
            }
        }
        Log.d(TAG, "Call created1: " + this.call.getId());
        this.addCallDialog(this.call.getId(), true);
    }

    public void makeCall(RemoteContact callTo) throws Exception {
        this.remoteContact = callTo;
        String remoteUserString = SIPUriParser.parseSipContact(callTo);
        CallOpParam prm = new CallOpParam(true);
        this.addHeaders(prm);
        this.call.makeCall(remoteUserString, prm);
    }

    public void delete() {
        this.call.delete();
    }

    @Override
    public void sendRinging() {
        this.getActiveDialog().setCallState(CallState.RINGING);
        this.answerInternal(180);
    }

    @Override
    public void checkForTransfer() {
        if (this.getActiveDialog().getTransferState() == CallDialog.TransferState.PRE_HOLD_BLIND) {
            this.getActiveDialog().setTransferState(CallDialog.TransferState.IN_PROGRESS);
            this.transferCallInternal(this.transferRemoteContact);
        }
    }

    @Override
    public CallDialog.TransferState getDialogTransferState() {
        return this.getActiveDialog().getTransferState();
    }

    @Override
    public CallDialog.TransferMode getDialogTransferMode() {
        return this.getActiveDialog().getTransferMode();
    }

    @Override
    public CallTransferState getTransferState() {
        CallDialog.TransferMode transferMode = this.getActiveDialog().getTransferMode();
        CallDialog.TransferState state = this.getActiveDialog().getTransferState();
        switch (transferMode) {
            case NONE: {
                return CallTransferState.NONE;
            }
            case RECEIVED: {
                if (state == CallDialog.TransferState.FAILED) {
                    return CallTransferState.TRANSFER_REQUEST_RECEIVED_FAILED;
                }
                if (state == CallDialog.TransferState.SUCCESS) {
                    return CallTransferState.TRANSFER_REQUEST_RECEIVED_SUCCEEDED;
                }
                return CallTransferState.TRANSFER_REQUEST_RECEIVED_IN_PROGRESS;
            }
            case SEND: {
                if (state == CallDialog.TransferState.FAILED) {
                    return CallTransferState.TRANSFER_REQUEST_SEND_FAILED;
                }
                if (state == CallDialog.TransferState.SUCCESS) {
                    return CallTransferState.TRANSFER_REQUEST_SEND_SUCCEEDED;
                }
                return CallTransferState.TRANSFER_REQUEST_SEND_IN_PROGRESS;
            }
            case REPLACED: {
                return CallTransferState.TRANSFER_REPLACED;
            }
        }
        return CallTransferState.NONE;
    }

    @Override
    public void redirect(final RemoteContact contact) {
        Log.d(TAG, "Begin of redirect");
        this.userTerminated = true;
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                CallOpParam prm = new CallOpParam();
                Hashtable<String, String> redirectHeaders = new Hashtable<String, String>();
                redirectHeaders.put("Contact", SIPUriParser.parseSipContact(contact));
                ACPjSipCall.this.addHeaders(prm, redirectHeaders);
                prm.setStatusCode(302);
                ACPjSipCall.this.call.hangup(prm);
                Log.d(ACPjSipCall.TAG, "Begin of redirect internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops: " + e1.getMessage());
        }
        Log.d(TAG, "End of redirect");
    }

    @Override
    public String getCallId() {
        String callId = null;
        try {
            callId = this.call.getInfo().getCallIdString();
            Log.d(TAG, "callId: " + callId);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
        return callId;
    }

    @Override
    public int getCallDuration() {
        int duration = -1;
        try {
            TimeVal timeVal = this.call.getInfo().getConnectDuration();
            duration = timeVal.getSec();
            Log.d(TAG, "duration: " + duration);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
        return duration;
    }

    @Override
    public void transferCall(ACSipCall remoteCall) {
        Log.d(TAG, "Begin of transferCall remoteCall");
        CallDialog activeDialog = this.getActiveDialog();
        this.transferRemoteCall = remoteCall;
        activeDialog.setCallState(CallState.TRANSFER);
        activeDialog.setTransferMode(CallDialog.TransferMode.SEND);
        remoteCall.getActiveDialog().setCallState(CallState.TRANSFER);
        remoteCall.getActiveDialog().setTransferState(CallDialog.TransferState.PRE_HOLD_ATTENDED);
        this.transferCallInternal(this.transferRemoteCall);
        Log.d(TAG, "End of transferCall remoteCall");
    }

    private void transferCallInternal(ACSipCall transferCall) {
        CallOpParam param = new CallOpParam(true);
        this.addHeaders(param);
        param.setStatusCode(603);
        try {
            this.call.xferReplaces(((ACPjSipCall)transferCall).call, param);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: ", e);
        }
    }

    @Override
    public void transferCall(RemoteContact transferTo) {
        Log.d(TAG, "Begin of transferCall ");
        CallDialog activeDialog = this.getActiveDialog();
        this.transferRemoteContact = transferTo;
        activeDialog.setCallState(CallState.TRANSFER);
        if (activeDialog.getCallState() == CallState.HOLD) {
            this.transferCallInternal(this.transferRemoteContact);
        } else {
            activeDialog.setTransferState(CallDialog.TransferState.PRE_HOLD_BLIND);
            this.requestSDPListener.onTransferHold(true);
        }
        Log.d(TAG, "End of transferCall");
    }

    private void transferCallInternal(RemoteContact transferTo) {
        String remoteUser = SIPUriParser.parseSipContact(transferTo);
        Log.d(TAG, "Transferring to contact: " + remoteUser);
        CallOpParam param = new CallOpParam(true);
        this.addHeaders(param);
        param.setStatusCode(603);
        try {
            this.call.xfer(remoteUser, param);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: ", e);
        }
    }

    @Override
    public void answer(boolean withVideo) {
        this.getActiveDialog().setCallState(CallState.CONNECTING);
        Log.d(TAG, "Begin of answer, video: " + withVideo);
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipCall.this.answerInternal(200);
                Log.d(ACPjSipCall.TAG, "End of answer internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops: " + e1.getMessage());
        }
        Log.d(TAG, "End of answer, video: " + withVideo);
    }

    void answerInternal(int code) {
        CallOpParam prm = new CallOpParam();
        if (code == 180 && ACPjSipManager.getInstance().allowHeader != null) {
            Hashtable<String, String> allowHeaders = new Hashtable<String, String>();
            allowHeaders.put("Allow-Events", ACPjSipManager.getInstance().allowHeader);
            this.addHeaders(prm, allowHeaders);
        } else {
            this.addHeaders(prm);
        }
        prm.setStatusCode(code);
        try {
            Log.d(TAG, "Replying to call: " + code);
            this.call.answer(prm);
        }
        catch (Exception exception) {
            // empty catch block
        }
        Log.d(TAG, "End of answer internal");
    }

    private void addHeaders(CallOpParam prm) {
        this.addHeaders(prm, null);
    }

    private void addHeaders(CallOpParam prm, Hashtable<String, String> extraHeaders) {
        SipHeader sipHeader;
        Set<String> keys;
        SipHeaderVector tmp = prm.getTxOption().getHeaders();
        int i = 0;
        while ((long)i < tmp.size()) {
            SipHeader h = tmp.get(i);
            Log.d(TAG, "hname: " + h.getHName());
            Log.d(TAG, "hvalue: " + h.getHValue());
            ++i;
        }
        SipHeaderVector sipHeaderVector = new SipHeaderVector();
        if (this.inviteHeaders != null) {
            keys = this.inviteHeaders.keySet();
            for (String string2 : keys) {
                sipHeader = new SipHeader();
                Log.d(TAG, "Value of " + string2 + " is: " + (String)this.inviteHeaders.get(string2));
                sipHeader.setHName(string2);
                sipHeader.setHValue((String)this.inviteHeaders.get(string2));
                sipHeaderVector.add(sipHeader);
            }
        }
        if (extraHeaders != null) {
            keys = extraHeaders.keySet();
            for (String string3 : keys) {
                sipHeader = new SipHeader();
                Log.d(TAG, "Value of " + string3 + " is: " + extraHeaders.get(string3));
                sipHeader.setHName(string3);
                sipHeader.setHValue(extraHeaders.get(string3));
                sipHeaderVector.add(sipHeader);
            }
        }
        if (sipHeaderVector.size() != 0L) {
            prm.getTxOption().setHeaders(sipHeaderVector);
        }
    }

    @Override
    public void reject() {
        Log.d(TAG, "Begin of reject");
        this.userTerminated = true;
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                CallOpParam prm = new CallOpParam();
                ACPjSipCall.this.addHeaders(prm);
                prm.setStatusCode(486);
                ACPjSipCall.this.call.hangup(prm);
                Log.d(ACPjSipCall.TAG, "Begin of reject internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops: " + e1.getMessage());
        }
        Log.d(TAG, "End of reject");
    }

    @Override
    public RemoteContact getRemoteNumber() {
        if (this.remoteContact == null) {
            ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
            Future<RemoteContact> result = executor.submit(new Callable<RemoteContact>(){

                @Override
                public RemoteContact call() throws Exception {
                    CallDialog activeCallDialog = ACPjSipCall.this.getActiveDialog();
                    Call call = Call.lookup(activeCallDialog.getId());
                    try {
                        return SIPUriParser.parseSipUri(call.getInfo().getRemoteUri());
                    }
                    catch (Exception e) {
                        Log.d(ACPjSipCall.TAG, "oops: " + e.getMessage());
                        return null;
                    }
                }
            });
            try {
                this.remoteContact = result.get();
            }
            catch (Exception e1) {
                Log.d(TAG, "Oops: " + e1.getMessage());
            }
        }
        return this.remoteContact;
    }

    @Override
    public void hold(final boolean hold) {
        Log.d(TAG, "Begin of hold: " + hold);
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        executor.execute(new Runnable(){

            @Override
            public void run() {
                CallOpParam param = new CallOpParam();
                ACPjSipCall.this.addHeaders(param);
                try {
                    CallDialog dialog = ACPjSipCall.this.getActiveDialog();
                    Log.d(ACPjSipCall.TAG, "hold dialogid: " + dialog.getId());
                    Call call = Call.lookup(dialog.getId());
                    if (hold) {
                        call.setHold(param);
                    } else {
                        CallSetting opt = param.getOpt();
                        opt.setFlag(1L);
                        call.reinvite(param);
                        Log.d(ACPjSipCall.TAG, "End of hold");
                    }
                }
                catch (Exception exc) {
                    Log.d(ACPjSipCall.TAG, "oops: " + exc.getMessage());
                }
            }
        });
    }

    @Override
    public void terminateCall(CallTermination termination) {
        this.terminationReason = termination;
        this.terminateCall();
    }

    @Override
    public void terminateCall() {
        Log.d(TAG, "Begin of terminateCall");
        this.userTerminated = true;
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipCall.this.terminateCallInternal();
                Log.d(ACPjSipCall.TAG, "End of terminateCallInternal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops: " + e1.getMessage());
        }
        Log.d(TAG, "End of terminateCall");
    }

    public void terminateCallInternal() {
        CallOpParam prm = new CallOpParam();
        this.addHeaders(prm);
        prm.setStatusCode(486);
        try {
            CallDialog dialog = this.getActiveDialog();
            Log.d(TAG, "terminateCallInternal dialogid: " + dialog.getId());
            Call call = Call.lookup(dialog.getId());
            call.hangup(prm);
        }
        catch (Exception e) {
            Log.d(TAG, "oops: " + e.getMessage());
        }
    }

    @Override
    public void sendDTMF(final DTMF dtmf) {
        Log.d(TAG, "Begin of sendDTMF");
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipCall.this.sendDTMFInternal(dtmf);
                Log.d(ACPjSipCall.TAG, "Begin of sendDTMF internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops: " + e1.getMessage());
        }
        Log.d(TAG, "End of terminateCall");
    }

    private void sendDTMFInternal(DTMF dtmf) {
        CallSendRequestParam prm = new CallSendRequestParam();
        prm.setMethod("INFO");
        SipTxOption txo = new SipTxOption();
        txo.setContentType(" application/dtmf-relay");
        txo.setMsgBody("Signal=" + dtmf.getDTMFString() + "\nDuration=" + ACConfiguration.getConfiguration().getDtmfOptions().duration);
        prm.setTxOption(txo);
        try {
            this.call.sendRequest(prm);
        }
        catch (Exception e) {
            Log.d(TAG, "Oops: " + e.getMessage());
        }
    }

    @Override
    public void sendInfo(final String info) {
        Log.d(TAG, "Begin of sendInfo");
        ScheduledExecutorService executor = ACPjSipManager.getInstance().getExecutor();
        Future<Void> result = executor.submit(new Callable<Void>(){

            @Override
            public Void call() throws Exception {
                ACPjSipCall.this.sendInfoInternal(info);
                Log.d(ACPjSipCall.TAG, "Begin of sendInfo internal");
                return null;
            }
        });
        try {
            result.get();
        }
        catch (Exception e1) {
            Log.d(TAG, "Oops: " + e1.getMessage());
        }
        Log.d(TAG, "End of terminateCall");
    }

    private void sendInfoInternal(String info) {
        CallSendRequestParam prm = new CallSendRequestParam();
        prm.setMethod("INFO");
        SipTxOption txo = new SipTxOption();
        txo.setContentType(" application/json");
        txo.setMsgBody(info + "\n");
        prm.setTxOption(txo);
        try {
            this.call.sendRequest(prm);
        }
        catch (Exception e) {
            Log.d(TAG, "Oops: " + e.getMessage());
        }
    }

    public String getHeader(String sipMessage, String reason) {
        BufferedReader br = new BufferedReader(new StringReader(sipMessage));
        try {
            String line;
            do {
                line = br.readLine();
                Log.d(TAG, "line: " + line);
                if (line != null) continue;
                return null;
            } while (!line.startsWith(reason));
            return line;
        }
        catch (IOException e) {
            return null;
        }
    }

    public void checkCallTsxStateBody(String sipMessage) {
        Log.d(TAG, "checkCallTsxStateBody: " + sipMessage);
        String checkHeader = this.getHeader(sipMessage, "Event: ");
        if (checkHeader != null) {
            Log.d(TAG, "reasonHeader: " + checkHeader);
            String headerValue = checkHeader.replace("Event: ", "");
            Log.d(TAG, "headerValue: " + headerValue);
            NotifyEvent notifyEvent = null;
            if (headerValue.toLowerCase().equals("talk")) {
                notifyEvent = NotifyEvent.TALK;
            } else if (headerValue.toLowerCase().equals("hold")) {
                notifyEvent = NotifyEvent.HOLD;
            } else if (headerValue.toLowerCase().equals("dtmf")) {
                notifyEvent = NotifyEvent.DTMF;
            } else if (headerValue.toLowerCase().equals("conference")) {
                notifyEvent = NotifyEvent.CONFERENCE;
            }
            checkHeader = this.getHeader(sipMessage, "Signal=");
            String dtmfValue = null;
            if (checkHeader != null) {
                String string2 = checkHeader.replace("Signal=", "");
            }
            Log.d(TAG, "NotifyEvent: " + (Object)((Object)notifyEvent));
            Log.d(TAG, "NotifyEvent dtmfValue: " + dtmfValue);
            this.callEventListener.incomingNotify(notifyEvent, dtmfValue);
        } else {
            NotifyEvent notifyEvent = NotifyEvent.MESSAGE;
            String messageValue = null;
            messageValue = this.getHeader(sipMessage, "{");
            if (messageValue != null) {
                Log.d(TAG, "NotifyEvent: " + (Object)((Object)notifyEvent));
                Log.d(TAG, "NotifyEvent messageValue: " + messageValue);
                this.callEventListener.incomingNotify(notifyEvent, messageValue);
            }
        }
    }

    public InfoAlert getInfoAlert(String sipMessage) {
        Log.d(TAG, "checkIncomingCallBody: " + sipMessage);
        String checkHeader = this.getHeader(sipMessage, "Alert-Info: ");
        InfoAlert infoAlert = null;
        if (checkHeader != null) {
            Log.d(TAG, "checkHeader: " + checkHeader);
            String headerValue = checkHeader.replace("Alert-Info: ", "");
            Log.d(TAG, "headerValue: " + headerValue);
            infoAlert = new InfoAlert();
            if (headerValue.toLowerCase().contains("info=alert-autoanswer")) {
                infoAlert.autoAnswer = true;
                infoAlert.delay = -1;
            }
            Log.d(TAG, "infoAlert autoAnswer: " + infoAlert.autoAnswer);
            Log.d(TAG, "infoAlert delay: " + infoAlert.delay);
        }
        return infoAlert;
    }

    private class PjSipCall
    extends Call {
        private Account account;
        private int dialogPredictTries;

        PjSipCall(Account account) {
            super(account);
            this.account = account;
        }

        PjSipCall(Account account, int callId) {
            super(account, callId);
            this.account = account;
        }

        @Override
        public void makeCall(String dst_uri, CallOpParam prm) throws Exception {
            Log.d(ACPjSipCall.TAG, "makeCall: " + this.getId());
            super.makeCall(dst_uri, prm);
        }

        @Override
        public void onCallAnswered(OnCallAnsweredParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallAnswered");
            String remoteSDPStr = prm.getRdata().getWholeMsg();
            int index = remoteSDPStr.indexOf("v=0");
            String remoteSDP = remoteSDPStr.substring(index, remoteSDPStr.length());
            Log.d(ACPjSipCall.TAG, "whole sdp: " + remoteSDP);
            ACPjSipCall.this.requestSDPListener.onCallAnswered(remoteSDP);
            super.onCallAnswered(prm);
        }

        @Override
        public void onCallTransferRequest(OnCallTransferRequestParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallTransferRequest:" + prm.getStatusCode());
            Log.d(ACPjSipCall.TAG, "onCallTransferRequest:" + prm.getDstUri());
            int statusCode = prm.getStatusCode();
            ACPjSipCall.this.transferRemoteContact = SIPUriParser.parseSipUri(prm.getDstUri());
            ACPjSipCall.this.getActiveDialog().setTransferMode(CallDialog.TransferMode.RECEIVED);
            ACPjSipCall.this.getActiveDialog().setTransferState(CallDialog.TransferState.IN_PROGRESS);
            ACPjSipCall.this.getActiveDialog().setCallState(CallState.TRANSFER);
            ACPjSipCall.this.callEventListener.callProgress(ACPjSipCall.this.getActiveDialog().getCallState());
            ACPjSipCall.this.callEventListener.incomingTransfer();
            Log.d(ACPjSipCall.TAG, "id: " + this.getId());
            super.onCallTransferRequest(prm);
        }

        @Override
        public void onCallTsxState(OnCallTsxStateParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallTsxState");
            super.onCallTsxState(prm);
            if (prm != null && prm.getE().getBody() != null) {
                String wholeMsg = prm.getE().getBody().getTsxState().getSrc().getRdata().getWholeMsg();
                ACPjSipCall.this.checkCallTsxStateBody(wholeMsg);
            }
        }

        @Override
        public void onCallTransferStatus(OnCallTransferStatusParam prm) {
            int statusCode = prm.getStatusCode();
            Log.d(ACPjSipCall.TAG, "onCallTransferStatus:" + statusCode);
            ACPjSipCall.this.getActiveDialog().setTransferMode(CallDialog.TransferMode.SEND);
            if (statusCode == 200) {
                ACPjSipCall.this.getActiveDialog().setTransferState(CallDialog.TransferState.SUCCESS);
                ACPjSipCall.this.callEventListener.callProgress(ACPjSipCall.this.getActiveDialog().getCallState());
                ACPjSipCall.this.terminateCallInternal();
            } else if (statusCode == 100 || statusCode == 180) {
                ACPjSipCall.this.getActiveDialog().setTransferState(CallDialog.TransferState.IN_PROGRESS);
                ACPjSipCall.this.callEventListener.callProgress(ACPjSipCall.this.getActiveDialog().getCallState());
            } else {
                Log.d(ACPjSipCall.TAG, "onCallTransferStatus:  transferDialogTerminated");
                ACPjSipCall.this.getActiveDialog().setCallState(CallState.HOLD);
                ACPjSipCall.this.getActiveDialog().setTransferState(CallDialog.TransferState.FAILED);
                ACPjSipCall.this.requestSDPListener.onTransferHold(false);
            }
            super.onCallTransferStatus(prm);
        }

        @Override
        public void onCallReplaceRequest(OnCallReplaceRequestParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallReplaceRequest:" + prm.getStatusCode());
            super.onCallReplaceRequest(prm);
        }

        @Override
        public void onCallReplaced(OnCallReplacedParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallReplaced old dialog:" + ACPjSipCall.this.getActiveDialog().getId());
            Log.d(ACPjSipCall.TAG, "onCallReplaced new dialog:" + prm.getNewCallId());
            ACPjSipCall.this.call = new PjSipCall((ACPjSipManager.ACPjSipAccount)ACPjSipCall.this.call.account, prm.getNewCallId());
            ACPjSipCall.this.getActiveDialog().setTransferMode(CallDialog.TransferMode.REPLACED);
            ACPjSipCall.this.getActiveDialog().setTransferState(CallDialog.TransferState.IN_PROGRESS);
            ACPjSipCall.this.getActiveDialog().setCallState(CallState.TRANSFER);
            if (ACPjSipCall.this.callEventListener != null) {
                ACPjSipCall.this.callEventListener.callProgress(ACPjSipCall.this.getActiveDialog().getCallState());
            }
            ACPjSipCall.this.addCallDialog(prm.getNewCallId(), true);
            CallDialog dialog = ACPjSipCall.this.getDialog(prm.getNewCallId());
            dialog.setCallState(CallState.CONNECTED);
            dialog.setTransferMode(CallDialog.TransferMode.REPLACED);
            if (ACPjSipCall.this.callEventListener != null) {
                ACPjSipCall.this.callEventListener.callProgress(ACPjSipCall.this.getActiveDialog().getCallState());
            }
            super.onCallReplaced(prm);
        }

        @Override
        public void onCallSdpCreated(OnCallSdpCreatedParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallSdpCreated");
            CallDialog dialog = ACPjSipCall.this.getActiveDialog();
            if (dialog == null) {
                Log.d(ACPjSipCall.TAG, "How can dialog be null?");
            }
            int dialogId = dialog.getId();
            Log.d(ACPjSipCall.TAG, "onCallSdpCreated getId: " + dialogId);
            Log.d(ACPjSipCall.TAG, "onCallSdpCreated getCallState: " + (Object)((Object)dialog.getCallState()));
            if (dialog.getCallState() != CallState.RINGING) {
                SdpSession localsdp = prm.getSdp();
                SdpSession remotesdp = prm.getRemSdp();
                String remoteSDPString = remotesdp.getWholeSdp();
                if (dialog.getTransferMode() == CallDialog.TransferMode.REPLACED) {
                    ACPjSipCall.this.requestSDPListener.onCallReplaced(remoteSDPString, dialogId);
                } else if (dialog.getCallState() == CallState.TRANSFER && dialog.getTransferState() == CallDialog.TransferState.IN_PROGRESS && dialog.getTransferMode() == CallDialog.TransferMode.RECEIVED) {
                    Log.d(ACPjSipCall.TAG, "dialogPredictTries: " + this.dialogPredictTries);
                    dialogId = dialogId + this.dialogPredictTries + 1;
                    Log.d("WEBRTCDialog", "making new dialog id: " + dialogId);
                    if (dialogId >= 4) {
                        Log.d("WEBRTCDialog", "dialogId is out of limit. setting new id: " + (dialogId %= 4));
                    }
                    ++this.dialogPredictTries;
                }
                String localSDPString = ACPjSipCall.this.requestSDPListener.requestLocalSDP(remoteSDPString, dialogId);
                if (localSDPString != null) {
                    localsdp.setWholeSdp(localSDPString);
                }
            }
            super.onCallSdpCreated(prm);
        }

        @Override
        public void onCallTxOffer(OnCallTxOfferParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallTxOffer");
            super.onCallTxOffer(prm);
        }

        @Override
        public void onCallRxOffer(OnCallRxOfferParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallRxOffer");
            String remoteSDPString = prm.getOffer().getWholeSdp();
            Log.d(ACPjSipCall.TAG, "remoteSDPString: " + remoteSDPString);
            ACPjSipCall.this.requestSDPListener.incomingReinvite(remoteSDPString);
            super.onCallRxOffer(prm);
        }

        @Override
        public int onCallRedirected(OnCallRedirectedParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallRedirected");
            ACPjSipCall.this.redirectContact = SIPUriParser.parseSipUri(prm.getTargetUri());
            Log.d(ACPjSipCall.TAG, "calll redirected to: " + ACPjSipCall.this.redirectContact.getUserName());
            Log.d(ACPjSipCall.TAG, "automatic redirection: " + ACConfiguration.getConfiguration().getAutomaticCallOnRedirect());
            if (ACConfiguration.getConfiguration().getAutomaticCallOnRedirect()) {
                ACPjSipCall.this.getActiveDialog().setCallState(CallState.REDIRECT);
                if (ACPjSipCall.this.callEventListener != null) {
                    ACPjSipCall.this.callEventListener.callProgress(CallState.REDIRECT);
                }
                return 2;
            }
            ACPjSipCall.this.getActiveDialog().setCallState(CallState.REDIRECT_STOP);
            if (ACPjSipCall.this.callEventListener != null) {
                ACPjSipCall.this.callEventListener.callProgress(CallState.REDIRECT_STOP);
            }
            return 4;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void onCallState(OnCallStateParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallState");
            try {
                block28: {
                    CallState newCallState;
                    CallDialog dialog;
                    block27: {
                        CallInfo ci = this.getInfo();
                        Log.d(ACPjSipCall.TAG, "last reason: " + ci.getLastReason());
                        int id2 = ci.getId();
                        Log.d(ACPjSipCall.TAG, "onCallState id:" + id2);
                        Log.d(ACPjSipCall.TAG, "onCallState remote uri:" + ci.getRemoteUri());
                        if (ACPjSipCall.this.remoteContact != null) {
                            String displayName = SIPUriParser.parseSipUri(ACPjSipCall.this.call.getInfo().getRemoteUri()).getDisplayName();
                            Log.d(ACPjSipCall.TAG, "remoteContact set display name:" + displayName);
                            ACPjSipCall.this.remoteContact.setDisplayName(displayName);
                        }
                        if ((dialog = ACPjSipCall.this.getDialog(id2)) == null) {
                            if (ACPjSipCall.this.getActiveDialog().getId() == -1) {
                                ACPjSipCall.this.getActiveDialog().setId(id2);
                                dialog = ACPjSipCall.this.getActiveDialog();
                            } else {
                                Log.d(ACPjSipCall.TAG, "Adding new dialog: " + id2);
                                ACPjSipCall.this.addCallDialog(id2, false);
                                dialog = ACPjSipCall.this.getDialog(id2);
                                dialog.setCallState(CallState.TRANSFER);
                                dialog.setTransferState(CallDialog.TransferState.IN_PROGRESS);
                                ACPjSipCall.this.transferRemoteContact = SIPUriParser.parseSipUri(ci.getRemoteUri());
                            }
                        }
                        Log.d(ACPjSipCall.TAG, "t mode: " + (Object)((Object)dialog.getTransferMode()));
                        Log.d(ACPjSipCall.TAG, "t state: " + (Object)((Object)dialog.getTransferState()));
                        Log.d(ACPjSipCall.TAG, "c state: " + (Object)((Object)dialog.getCallState()));
                        boolean transferDialog = false;
                        if (dialog.getCallState() == CallState.TRANSFER && ACPjSipCall.this.getDialogsAmount() > 1) {
                            transferDialog = true;
                        }
                        Log.d(ACPjSipCall.TAG, "remote contact: " + ci.getRemoteContact());
                        newCallState = CallState.NULL;
                        int pjsipState = ci.getState();
                        Log.d(ACPjSipCall.TAG, "pjsipState: " + pjsipState);
                        Log.d(ACPjSipCall.TAG, "transferDialog: " + transferDialog);
                        if (pjsipState == 5) {
                            if (!transferDialog) {
                                ACPjSipCall.this.startTime = System.currentTimeMillis();
                            } else {
                                dialog.setTransferState(CallDialog.TransferState.SUCCESS);
                                ACPjSipCall.this.setActiveDialog(dialog.getId());
                                Log.d(ACPjSipCall.TAG, "new active dialog: " + dialog.getId());
                                if (ACPjSipCall.this.callEventListener != null) {
                                    Log.d(ACPjSipCall.TAG, "callProgress: " + dialog.getId());
                                    ACPjSipCall.this.callEventListener.callProgress(CallState.TRANSFER);
                                }
                                ACPjSipCall.this.remoteContact = ACPjSipCall.this.transferRemoteContact;
                                transferDialog = false;
                                ACPjSipCall.this.transferRemoteContact = null;
                                dialog.setTransferState(CallDialog.TransferState.NONE);
                                dialog.setTransferMode(CallDialog.TransferMode.NONE);
                            }
                            newCallState = CallState.CONNECTED;
                        }
                        if (!transferDialog) {
                            if (pjsipState == 1) {
                                newCallState = CallState.CALLING;
                            }
                            if (pjsipState == 3) {
                                newCallState = CallState.RINGING;
                            }
                            if (pjsipState == 4) {
                                newCallState = CallState.CONNECTING;
                            }
                        } else {
                            newCallState = dialog.getCallState();
                        }
                        if (pjsipState != 6) break block27;
                        TerminationInfo terminationInfo = new TerminationInfo();
                        int code = ci.getLastStatusCode();
                        Log.d(ACPjSipCall.TAG, "termination code: " + code);
                        CallTermination termination = code == 404 || "Not Found".equals(ci.getLastReason()) ? (dialog.getCallState() == CallState.REDIRECT_STOP ? CallTermination.TERMINATED_REDIRECT_STOP : (dialog.getCallState() == CallState.REDIRECT ? CallTermination.TERMINATED_REDIRECT : CallTermination.TERMINATED_NOT_FOUND)) : (code == 603 ? CallTermination.TERMINATED_DECLINED : (code == 486 ? (ACPjSipCall.this.userTerminated ? CallTermination.TERMINATED_LOCAL : CallTermination.TERMINATED_DECLINED) : (code == 200 ? CallTermination.TERMINATED_REMOTE : (ACPjSipCall.this.userTerminated ? CallTermination.TERMINATED_LOCAL : (dialog.getCallState() == CallState.REDIRECT_STOP ? CallTermination.TERMINATED_REDIRECT_STOP : (dialog.getCallState() == CallState.REDIRECT ? CallTermination.TERMINATED_REDIRECT : CallTermination.TERMINATED_UNKNOWN))))));
                        if (ACPjSipCall.this.terminationReason != null) {
                            termination = ACPjSipCall.this.terminationReason;
                        }
                        if (dialog.getCallState() == CallState.TRANSFER) {
                            termination = CallTermination.TERMINATED_TRANSFER;
                        }
                        if (ACPjSipCall.this.callEventListener != null) {
                            if (dialog.isActiveDialog()) {
                                TsxStateEvent state;
                                SipEventBody body;
                                if (transferDialog) {
                                    ACPjSipCall.this.removeDialog(dialog.getId());
                                    ACPjSipCall.this.getActiveDialog().setCallState(CallState.HOLD);
                                    ACPjSipCall.this.callEventListener.transferDialogTerminated(dialog.getId(), ACPjSipCall.this.getActiveDialog().getId());
                                    Log.d(ACPjSipCall.TAG, "SDP1 getDialogTransferMode: " + (Object)((Object)ACPjSipCall.this.getDialogTransferMode()));
                                    Log.d(ACPjSipCall.TAG, "SDP1 getCallState: " + (Object)((Object)ACPjSipCall.this.getCallState()));
                                    return;
                                }
                                terminationInfo.callTermination = termination;
                                terminationInfo.pjssipStatusCode = ci.getLastStatusCode();
                                try {
                                    terminationInfo.statusCode = terminationInfo.pjssipStatusCode;
                                    Log.d(ACPjSipCall.TAG, ": " + terminationInfo.statusCode);
                                }
                                catch (Exception exception) {
                                    // empty catch block
                                }
                                terminationInfo.reason = ci.getLastReason();
                                if (termination == CallTermination.TERMINATED_LOCAL && terminationInfo.pjssipStatusCode == 486) {
                                    terminationInfo.pjssipStatusCode = 200;
                                    terminationInfo.statusCode = 200;
                                    terminationInfo.reason = "";
                                }
                                if ((body = prm.getE().getBody()) != null && (state = body.getTsxState()) != null && state.getSrc() != null && state.getSrc().getRdata() != null) {
                                    String sipMessage = state.getSrc().getRdata().getWholeMsg();
                                    Log.d(ACPjSipCall.TAG, "sipMessage: " + sipMessage);
                                    terminationInfo.reasonHeader = ACPjSipCall.this.getHeader(sipMessage, "Reason: ");
                                    terminationInfo.sipMessage = sipMessage;
                                }
                                ACPjSipCall.this.callEventListener.callTerminated(terminationInfo);
                                return;
                            }
                            if (code != 200 && code != 410) {
                                Log.d(ACPjSipCall.TAG, "dialog failed");
                                ACPjSipCall.this.getActiveDialog().setTransferState(CallDialog.TransferState.FAILED);
                                ACPjSipCall.this.terminateCallInternal();
                                return;
                            }
                            Log.d(ACPjSipCall.TAG, "not sending termination");
                            if (dialog.getCallState() == CallState.TRANSFER && dialog.getTransferMode() == CallDialog.TransferMode.RECEIVED) {
                                Log.d(ACPjSipCall.TAG, "transferee hangup");
                                ACPjSipCall.this.removeDialog(dialog.getId());
                            }
                        }
                        break block28;
                    }
                    dialog.setCallState(newCallState);
                }
                if (ACPjSipCall.this.callEventListener == null) return;
                ACPjSipCall.this.callEventListener.callProgress(ACPjSipCall.this.getActiveDialog().getCallState());
                return;
            }
            catch (Exception e) {
                Log.d(ACPjSipCall.TAG, "oops: " + e.getMessage());
                return;
            }
        }

        @Override
        public void onCallMediaState(OnCallMediaStateParam prm) {
            Log.d(ACPjSipCall.TAG, "onCallMediaState");
        }
    }
}

