/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip;

import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.CallState;
import com.korevg.mv.webrtcsdk.session.CallTermination;
import com.korevg.mv.webrtcsdk.session.CallTransferState;
import com.korevg.mv.webrtcsdk.session.DTMF;
import com.korevg.mv.webrtcsdk.session.NotifyEvent;
import com.korevg.mv.webrtcsdk.session.RemoteContact;
import com.korevg.mv.webrtcsdk.session.TerminationInfo;
import com.korevg.mv.webrtcsdk.sip.CallDialog;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public abstract class ACSipCall {
    public static final String DIALOG_TAG = "WEBRTCDialog";
    private static final String TAG = "ACSipCall";
    protected IRequestSDP requestSDPListener;
    protected Hashtable<String, String> inviteHeaders;
    protected String remoteSDP;
    private ArrayList<CallDialog> callDialogs = new ArrayList();
    protected long startTime;
    public CallTermination terminationReason = null;
    protected RemoteContact redirectContact;
    protected RemoteContact transferRemoteContact;
    protected RemoteContact remoteContact;
    protected ACSipCall transferRemoteCall;
    protected ICallEventListener callEventListener;

    public void setiCallEventListener(ICallEventListener iCallEventListener) {
        this.callEventListener = iCallEventListener;
    }

    public void setInviteHeaders(Hashtable<String, String> inviteHeaders) {
        this.inviteHeaders = inviteHeaders;
    }

    public abstract void sendRinging();

    public abstract void checkForTransfer();

    public abstract CallTransferState getTransferState();

    public abstract CallDialog.TransferState getDialogTransferState();

    public abstract CallDialog.TransferMode getDialogTransferMode();

    public void setRequestSDPListener(IRequestSDP requestSDPListener) {
        this.requestSDPListener = requestSDPListener;
    }

    public abstract String getCallId();

    public abstract void answer(boolean var1);

    public abstract void reject();

    public abstract void redirect(RemoteContact var1);

    public abstract RemoteContact getRemoteNumber();

    public abstract void hold(boolean var1);

    public abstract void sendDTMF(DTMF var1);

    public abstract void sendInfo(String var1);

    public abstract void terminateCall();

    public abstract void terminateCall(CallTermination var1);

    public abstract int getCallDuration();

    public String getRemoteSDP() {
        return this.remoteSDP;
    }

    public void setRemoteSDP(String remoteSDP) {
        this.remoteSDP = remoteSDP;
    }

    public long getCallStartTime() {
        return this.startTime;
    }

    public RemoteContact getRedirectContact() {
        return this.redirectContact;
    }

    public RemoteContact getTransferContact() {
        return this.transferRemoteContact;
    }

    public ACSipCall getTransferCall() {
        return this.transferRemoteCall;
    }

    public abstract void transferCall(RemoteContact var1);

    public abstract void transferCall(ACSipCall var1);

    public CallDialog.TransferState getTransferState(int id2) {
        return this.getDialog(id2).getTransferState();
    }

    public CallDialog.TransferMode getTransfer(int id2) {
        return this.getDialog(id2).getTransferMode();
    }

    protected CallDialog getDialog(int id2) {
        for (CallDialog dialog : this.callDialogs) {
            if (dialog.getId() != id2) continue;
            Log.d(DIALOG_TAG, "getDialog: " + dialog.getId());
            return dialog;
        }
        Log.d(DIALOG_TAG, "not found dialog: " + id2);
        return null;
    }

    public CallDialog getActiveDialog() {
        for (CallDialog dialog : this.callDialogs) {
            Log.d(DIALOG_TAG, "dialog: " + dialog.getId());
            if (!dialog.isActiveDialog()) continue;
            Log.d(DIALOG_TAG, "getActiveDialog: " + dialog.getId());
            return dialog;
        }
        Log.d(DIALOG_TAG, "  getActiveDialog null");
        return null;
    }

    protected void addCallDialog(int id2, boolean activeDialog) {
        CallDialog dialog = new CallDialog();
        dialog.setId(id2);
        this.callDialogs.add(dialog);
        this.setActiveDialog(id2);
        Log.d(DIALOG_TAG, "addCallDialog: " + dialog.getId());
    }

    protected void removeDialog(int id2) {
        block2: {
            CallDialog dialog;
            Log.d(DIALOG_TAG, "removeDialog: " + id2);
            int index = -1;
            boolean wasActiveDialog = false;
            Iterator<CallDialog> iterator = this.callDialogs.iterator();
            while (iterator.hasNext()) {
                dialog = iterator.next();
                if (dialog.getId() != id2) continue;
                if (dialog.isActiveDialog()) {
                    wasActiveDialog = true;
                }
                index = this.callDialogs.indexOf(dialog);
            }
            this.callDialogs.remove(index);
            if (!wasActiveDialog || this.callDialogs.size() <= 0 || !(iterator = this.callDialogs.iterator()).hasNext()) break block2;
            dialog = iterator.next();
            Log.d(DIALOG_TAG, "remove - set active: " + id2);
            dialog.setActiveDialog(true);
        }
    }

    protected int getDialogsAmount() {
        return this.callDialogs.size();
    }

    public int getActiveDialogID() {
        return this.getActiveDialog().getId();
    }

    protected void setActiveDialog(int id2) {
        Log.d(DIALOG_TAG, "setActiveDialog: " + id2);
        for (CallDialog dialog : this.callDialogs) {
            if (dialog.getId() == id2) {
                Log.d(DIALOG_TAG, "activate: " + id2);
                dialog.setActiveDialog(true);
                continue;
            }
            dialog.setActiveDialog(false);
        }
    }

    public CallState getCallState() {
        CallDialog dialog = this.getActiveDialog();
        if (dialog == null) {
            Log.d(DIALOG_TAG, "dialog == null");
            return null;
        }
        Log.d(DIALOG_TAG, "dialog == " + dialog.getId());
        return dialog.getCallState();
    }

    public static interface ICallEventListener {
        public void callTerminated(TerminationInfo var1);

        public void callProgress(CallState var1);

        public void reinviteWithVideoCallback();

        public void incomingTransfer();

        public void transferDialogTerminated(int var1, int var2);

        public void incomingNotify(NotifyEvent var1, String var2);
    }

    public static interface IRequestSDP {
        public String requestLocalSDP(String var1, int var2);

        public void incomingReinvite(String var1);

        public void onCallAnswered(String var1);

        public void onCallReplaced(String var1, int var2);

        public void onTransferHold(boolean var1);
    }
}

