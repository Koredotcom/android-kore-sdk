/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.sip;

import com.korevg.mv.webrtcsdk.log.Log;
import com.korevg.mv.webrtcsdk.session.CallState;

public class CallDialog {
    private static final String TAG = "CallDialog";
    private int id;
    private boolean activeDialog;
    private CallState callState = CallState.NULL;
    private TransferMode transferMode = TransferMode.NONE;
    private TransferState transferState = TransferState.NONE;

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        Log.d("WEBRTCDialog", "rename dialog id from: " + this.id + " to " + id2);
        this.id = id2;
    }

    public boolean isActiveDialog() {
        return this.activeDialog;
    }

    public void setActiveDialog(boolean activeDialog) {
        Log.d(TAG, "Call ID: " + this.getId() + " - old activeDialog: " + this.activeDialog + " new activeDialog: " + activeDialog);
        this.activeDialog = activeDialog;
    }

    public CallState getCallState() {
        return this.callState;
    }

    public void setCallState(CallState callState) {
        Log.d(TAG, "Call ID: " + this.getId() + " - old callState: " + (Object)((Object)this.callState) + " new callState: " + (Object)((Object)callState));
        this.callState = callState;
    }

    public TransferState getTransferState() {
        return this.transferState;
    }

    public void setTransferState(TransferState transferState) {
        Log.d(TAG, "Call ID: " + this.getId() + " - old transferState: " + (Object)((Object)this.transferState) + " new transferState: " + (Object)((Object)transferState));
        this.transferState = transferState;
    }

    public TransferMode getTransferMode() {
        return this.transferMode;
    }

    public void setTransferMode(TransferMode transfer) {
        Log.d(TAG, "Call ID: " + this.getId() + " - old transferMode: " + (Object)((Object)this.transferMode) + " new transferMode: " + (Object)((Object)transfer));
        this.transferMode = transfer;
    }

    public static enum TransferState {
        NONE,
        PRE_HOLD_BLIND,
        PRE_HOLD_ATTENDED,
        IN_PROGRESS,
        FAILED,
        SUCCESS;

    }

    public static enum TransferMode {
        NONE,
        RECEIVED,
        SEND,
        REPLACED;

    }
}

