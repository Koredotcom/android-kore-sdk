/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.BitrateAdjuster;

class BaseBitrateAdjuster
implements BitrateAdjuster {
    protected int targetBitrateBps;
    protected double targetFramerateFps;

    BaseBitrateAdjuster() {
    }

    @Override
    public void setTargets(int targetBitrateBps, double targetFramerateFps) {
        this.targetBitrateBps = targetBitrateBps;
        this.targetFramerateFps = targetFramerateFps;
    }

    @Override
    public void reportEncodedFrame(int size) {
    }

    @Override
    public int getAdjustedBitrateBps() {
        return this.targetBitrateBps;
    }

    @Override
    public double getAdjustedFramerateFps() {
        return this.targetFramerateFps;
    }
}

