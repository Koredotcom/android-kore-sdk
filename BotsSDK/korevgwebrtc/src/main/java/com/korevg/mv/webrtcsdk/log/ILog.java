/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.log;

public interface ILog {
    public int v(String var1, String var2);

    public int v(String var1, String var2, Throwable var3);

    public int d(String var1, String var2);

    public int d(String var1, String var2, Throwable var3);

    public int i(String var1, String var2);

    public int i(String var1, String var2, Throwable var3);

    public int w(String var1, String var2);

    public int w(String var1, String var2, Throwable var3);

    public int e(String var1, String var2);

    public int e(String var1, String var2, Throwable var3);
}

