/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.session;

public class RemoteContact {
    protected String displayName = "";
    protected String userName = "";
    protected String domain = "";
    protected String scheme = "";

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

