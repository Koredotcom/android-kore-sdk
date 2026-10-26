/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

interface BitrateAdjuster {
    public void setTargets(int var1, int var2);

    public void reportEncodedFrame(int var1);

    public int getAdjustedBitrateBps();

    public int getCodecConfigFramerate();
}

