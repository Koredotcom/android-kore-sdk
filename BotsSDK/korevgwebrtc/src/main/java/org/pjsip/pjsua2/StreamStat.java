/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.JbufState;
import org.pjsip.pjsua2.RtcpStat;
import org.pjsip.pjsua2.pjsua2JNI;

public class StreamStat {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected StreamStat(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(StreamStat obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_StreamStat(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setRtcp(RtcpStat value) {
        pjsua2JNI.StreamStat_rtcp_set(this.swigCPtr, this, RtcpStat.getCPtr(value), value);
    }

    public RtcpStat getRtcp() {
        long cPtr = pjsua2JNI.StreamStat_rtcp_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new RtcpStat(cPtr, false);
    }

    public void setJbuf(JbufState value) {
        pjsua2JNI.StreamStat_jbuf_set(this.swigCPtr, this, JbufState.getCPtr(value), value);
    }

    public JbufState getJbuf() {
        long cPtr = pjsua2JNI.StreamStat_jbuf_get(this.swigCPtr, this);
        return cPtr == 0L ? null : new JbufState(cPtr, false);
    }

    public StreamStat() {
        this(pjsua2JNI.new_StreamStat(), true);
    }
}

