/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.pjsua2JNI;

public class TransportInfo {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected TransportInfo(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(TransportInfo obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_TransportInfo(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setId(int value) {
        pjsua2JNI.TransportInfo_id_set(this.swigCPtr, this, value);
    }

    public int getId() {
        return pjsua2JNI.TransportInfo_id_get(this.swigCPtr, this);
    }

    public void setType(int value) {
        pjsua2JNI.TransportInfo_type_set(this.swigCPtr, this, value);
    }

    public int getType() {
        return pjsua2JNI.TransportInfo_type_get(this.swigCPtr, this);
    }

    public void setTypeName(String value) {
        pjsua2JNI.TransportInfo_typeName_set(this.swigCPtr, this, value);
    }

    public String getTypeName() {
        return pjsua2JNI.TransportInfo_typeName_get(this.swigCPtr, this);
    }

    public void setInfo(String value) {
        pjsua2JNI.TransportInfo_info_set(this.swigCPtr, this, value);
    }

    public String getInfo() {
        return pjsua2JNI.TransportInfo_info_get(this.swigCPtr, this);
    }

    public void setFlags(long value) {
        pjsua2JNI.TransportInfo_flags_set(this.swigCPtr, this, value);
    }

    public long getFlags() {
        return pjsua2JNI.TransportInfo_flags_get(this.swigCPtr, this);
    }

    public void setLocalAddress(String value) {
        pjsua2JNI.TransportInfo_localAddress_set(this.swigCPtr, this, value);
    }

    public String getLocalAddress() {
        return pjsua2JNI.TransportInfo_localAddress_get(this.swigCPtr, this);
    }

    public void setLocalName(String value) {
        pjsua2JNI.TransportInfo_localName_set(this.swigCPtr, this, value);
    }

    public String getLocalName() {
        return pjsua2JNI.TransportInfo_localName_get(this.swigCPtr, this);
    }

    public void setUsageCount(long value) {
        pjsua2JNI.TransportInfo_usageCount_set(this.swigCPtr, this, value);
    }

    public long getUsageCount() {
        return pjsua2JNI.TransportInfo_usageCount_get(this.swigCPtr, this);
    }

    public TransportInfo() {
        this(pjsua2JNI.new_TransportInfo(), true);
    }
}

