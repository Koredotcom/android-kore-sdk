/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class AccountNatConfig
extends PersistentObject {
    private transient long swigCPtr;

    protected AccountNatConfig(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AccountNatConfig_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AccountNatConfig obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AccountNatConfig(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setSipStunUse(int value) {
        pjsua2JNI.AccountNatConfig_sipStunUse_set(this.swigCPtr, this, value);
    }

    public int getSipStunUse() {
        return pjsua2JNI.AccountNatConfig_sipStunUse_get(this.swigCPtr, this);
    }

    public void setMediaStunUse(int value) {
        pjsua2JNI.AccountNatConfig_mediaStunUse_set(this.swigCPtr, this, value);
    }

    public int getMediaStunUse() {
        return pjsua2JNI.AccountNatConfig_mediaStunUse_get(this.swigCPtr, this);
    }

    public void setNat64Opt(int value) {
        pjsua2JNI.AccountNatConfig_nat64Opt_set(this.swigCPtr, this, value);
    }

    public int getNat64Opt() {
        return pjsua2JNI.AccountNatConfig_nat64Opt_get(this.swigCPtr, this);
    }

    public void setIceEnabled(boolean value) {
        pjsua2JNI.AccountNatConfig_iceEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getIceEnabled() {
        return pjsua2JNI.AccountNatConfig_iceEnabled_get(this.swigCPtr, this);
    }

    public void setIceMaxHostCands(int value) {
        pjsua2JNI.AccountNatConfig_iceMaxHostCands_set(this.swigCPtr, this, value);
    }

    public int getIceMaxHostCands() {
        return pjsua2JNI.AccountNatConfig_iceMaxHostCands_get(this.swigCPtr, this);
    }

    public void setIceAggressiveNomination(boolean value) {
        pjsua2JNI.AccountNatConfig_iceAggressiveNomination_set(this.swigCPtr, this, value);
    }

    public boolean getIceAggressiveNomination() {
        return pjsua2JNI.AccountNatConfig_iceAggressiveNomination_get(this.swigCPtr, this);
    }

    public void setIceNominatedCheckDelayMsec(long value) {
        pjsua2JNI.AccountNatConfig_iceNominatedCheckDelayMsec_set(this.swigCPtr, this, value);
    }

    public long getIceNominatedCheckDelayMsec() {
        return pjsua2JNI.AccountNatConfig_iceNominatedCheckDelayMsec_get(this.swigCPtr, this);
    }

    public void setIceWaitNominationTimeoutMsec(int value) {
        pjsua2JNI.AccountNatConfig_iceWaitNominationTimeoutMsec_set(this.swigCPtr, this, value);
    }

    public int getIceWaitNominationTimeoutMsec() {
        return pjsua2JNI.AccountNatConfig_iceWaitNominationTimeoutMsec_get(this.swigCPtr, this);
    }

    public void setIceNoRtcp(boolean value) {
        pjsua2JNI.AccountNatConfig_iceNoRtcp_set(this.swigCPtr, this, value);
    }

    public boolean getIceNoRtcp() {
        return pjsua2JNI.AccountNatConfig_iceNoRtcp_get(this.swigCPtr, this);
    }

    public void setIceAlwaysUpdate(boolean value) {
        pjsua2JNI.AccountNatConfig_iceAlwaysUpdate_set(this.swigCPtr, this, value);
    }

    public boolean getIceAlwaysUpdate() {
        return pjsua2JNI.AccountNatConfig_iceAlwaysUpdate_get(this.swigCPtr, this);
    }

    public void setTurnEnabled(boolean value) {
        pjsua2JNI.AccountNatConfig_turnEnabled_set(this.swigCPtr, this, value);
    }

    public boolean getTurnEnabled() {
        return pjsua2JNI.AccountNatConfig_turnEnabled_get(this.swigCPtr, this);
    }

    public void setTurnServer(String value) {
        pjsua2JNI.AccountNatConfig_turnServer_set(this.swigCPtr, this, value);
    }

    public String getTurnServer() {
        return pjsua2JNI.AccountNatConfig_turnServer_get(this.swigCPtr, this);
    }

    public void setTurnConnType(int value) {
        pjsua2JNI.AccountNatConfig_turnConnType_set(this.swigCPtr, this, value);
    }

    public int getTurnConnType() {
        return pjsua2JNI.AccountNatConfig_turnConnType_get(this.swigCPtr, this);
    }

    public void setTurnUserName(String value) {
        pjsua2JNI.AccountNatConfig_turnUserName_set(this.swigCPtr, this, value);
    }

    public String getTurnUserName() {
        return pjsua2JNI.AccountNatConfig_turnUserName_get(this.swigCPtr, this);
    }

    public void setTurnPasswordType(int value) {
        pjsua2JNI.AccountNatConfig_turnPasswordType_set(this.swigCPtr, this, value);
    }

    public int getTurnPasswordType() {
        return pjsua2JNI.AccountNatConfig_turnPasswordType_get(this.swigCPtr, this);
    }

    public void setTurnPassword(String value) {
        pjsua2JNI.AccountNatConfig_turnPassword_set(this.swigCPtr, this, value);
    }

    public String getTurnPassword() {
        return pjsua2JNI.AccountNatConfig_turnPassword_get(this.swigCPtr, this);
    }

    public void setContactRewriteUse(int value) {
        pjsua2JNI.AccountNatConfig_contactRewriteUse_set(this.swigCPtr, this, value);
    }

    public int getContactRewriteUse() {
        return pjsua2JNI.AccountNatConfig_contactRewriteUse_get(this.swigCPtr, this);
    }

    public void setContactRewriteMethod(int value) {
        pjsua2JNI.AccountNatConfig_contactRewriteMethod_set(this.swigCPtr, this, value);
    }

    public int getContactRewriteMethod() {
        return pjsua2JNI.AccountNatConfig_contactRewriteMethod_get(this.swigCPtr, this);
    }

    public void setContactUseSrcPort(int value) {
        pjsua2JNI.AccountNatConfig_contactUseSrcPort_set(this.swigCPtr, this, value);
    }

    public int getContactUseSrcPort() {
        return pjsua2JNI.AccountNatConfig_contactUseSrcPort_get(this.swigCPtr, this);
    }

    public void setViaRewriteUse(int value) {
        pjsua2JNI.AccountNatConfig_viaRewriteUse_set(this.swigCPtr, this, value);
    }

    public int getViaRewriteUse() {
        return pjsua2JNI.AccountNatConfig_viaRewriteUse_get(this.swigCPtr, this);
    }

    public void setSdpNatRewriteUse(int value) {
        pjsua2JNI.AccountNatConfig_sdpNatRewriteUse_set(this.swigCPtr, this, value);
    }

    public int getSdpNatRewriteUse() {
        return pjsua2JNI.AccountNatConfig_sdpNatRewriteUse_get(this.swigCPtr, this);
    }

    public void setSipOutboundUse(int value) {
        pjsua2JNI.AccountNatConfig_sipOutboundUse_set(this.swigCPtr, this, value);
    }

    public int getSipOutboundUse() {
        return pjsua2JNI.AccountNatConfig_sipOutboundUse_get(this.swigCPtr, this);
    }

    public void setSipOutboundInstanceId(String value) {
        pjsua2JNI.AccountNatConfig_sipOutboundInstanceId_set(this.swigCPtr, this, value);
    }

    public String getSipOutboundInstanceId() {
        return pjsua2JNI.AccountNatConfig_sipOutboundInstanceId_get(this.swigCPtr, this);
    }

    public void setSipOutboundRegId(String value) {
        pjsua2JNI.AccountNatConfig_sipOutboundRegId_set(this.swigCPtr, this, value);
    }

    public String getSipOutboundRegId() {
        return pjsua2JNI.AccountNatConfig_sipOutboundRegId_get(this.swigCPtr, this);
    }

    public void setUdpKaIntervalSec(long value) {
        pjsua2JNI.AccountNatConfig_udpKaIntervalSec_set(this.swigCPtr, this, value);
    }

    public long getUdpKaIntervalSec() {
        return pjsua2JNI.AccountNatConfig_udpKaIntervalSec_get(this.swigCPtr, this);
    }

    public void setUdpKaData(String value) {
        pjsua2JNI.AccountNatConfig_udpKaData_set(this.swigCPtr, this, value);
    }

    public String getUdpKaData() {
        return pjsua2JNI.AccountNatConfig_udpKaData_get(this.swigCPtr, this);
    }

    public AccountNatConfig() {
        this(pjsua2JNI.new_AccountNatConfig(), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountNatConfig_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AccountNatConfig_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

