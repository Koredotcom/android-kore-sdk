/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.pjsua2JNI;

public class AuthCredInfo
extends PersistentObject {
    private transient long swigCPtr;

    protected AuthCredInfo(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AuthCredInfo_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AuthCredInfo obj) {
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
                pjsua2JNI.delete_AuthCredInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    public void setScheme(String value) {
        pjsua2JNI.AuthCredInfo_scheme_set(this.swigCPtr, this, value);
    }

    public String getScheme() {
        return pjsua2JNI.AuthCredInfo_scheme_get(this.swigCPtr, this);
    }

    public void setRealm(String value) {
        pjsua2JNI.AuthCredInfo_realm_set(this.swigCPtr, this, value);
    }

    public String getRealm() {
        return pjsua2JNI.AuthCredInfo_realm_get(this.swigCPtr, this);
    }

    public void setUsername(String value) {
        pjsua2JNI.AuthCredInfo_username_set(this.swigCPtr, this, value);
    }

    public String getUsername() {
        return pjsua2JNI.AuthCredInfo_username_get(this.swigCPtr, this);
    }

    public void setDataType(int value) {
        pjsua2JNI.AuthCredInfo_dataType_set(this.swigCPtr, this, value);
    }

    public int getDataType() {
        return pjsua2JNI.AuthCredInfo_dataType_get(this.swigCPtr, this);
    }

    public void setData(String value) {
        pjsua2JNI.AuthCredInfo_data_set(this.swigCPtr, this, value);
    }

    public String getData() {
        return pjsua2JNI.AuthCredInfo_data_get(this.swigCPtr, this);
    }

    public void setAkaK(String value) {
        pjsua2JNI.AuthCredInfo_akaK_set(this.swigCPtr, this, value);
    }

    public String getAkaK() {
        return pjsua2JNI.AuthCredInfo_akaK_get(this.swigCPtr, this);
    }

    public void setAkaOp(String value) {
        pjsua2JNI.AuthCredInfo_akaOp_set(this.swigCPtr, this, value);
    }

    public String getAkaOp() {
        return pjsua2JNI.AuthCredInfo_akaOp_get(this.swigCPtr, this);
    }

    public void setAkaAmf(String value) {
        pjsua2JNI.AuthCredInfo_akaAmf_set(this.swigCPtr, this, value);
    }

    public String getAkaAmf() {
        return pjsua2JNI.AuthCredInfo_akaAmf_get(this.swigCPtr, this);
    }

    public AuthCredInfo() {
        this(pjsua2JNI.new_AuthCredInfo__SWIG_0(), true);
    }

    public AuthCredInfo(String scheme, String realm, String user_name, int data_type, String data) {
        this(pjsua2JNI.new_AuthCredInfo__SWIG_1(scheme, realm, user_name, data_type, data), true);
    }

    @Override
    public void readObject(ContainerNode node) throws Exception {
        pjsua2JNI.AuthCredInfo_readObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }

    @Override
    public void writeObject(ContainerNode node) throws Exception {
        pjsua2JNI.AuthCredInfo_writeObject(this.swigCPtr, this, ContainerNode.getCPtr(node), node);
    }
}

