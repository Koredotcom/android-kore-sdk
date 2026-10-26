/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.im;

import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.SWIGTYPE_p_void;

public class InstanceMessageID
extends SWIGTYPE_p_void {
    private long ID;

    public InstanceMessageID() {
        this(System.currentTimeMillis());
    }

    public InstanceMessageID(long ID) {
        super(ID, false);
        this.ID = ID;
    }

    public long getID() {
        return this.ID;
    }

    public static long getID(OnInstantMessageStatusParam prm) {
        return InstanceMessageID.getCPtr(prm.getUserData());
    }

    public String toString() {
        return "ID: " + this.ID;
    }
}

