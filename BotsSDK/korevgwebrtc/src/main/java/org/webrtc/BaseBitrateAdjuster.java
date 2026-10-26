/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.BitrateAdjuster;

class BaseBitrateAdjuster
implements BitrateAdjuster {
    protected int targetBitrateBps;
    protected int targetFps;

    BaseBitrateAdjuster() {
    }

    @Override
    public void setTargets(int targetBitrateBps, int targetFps) {
        this.targetBitrateBps = targetBitrateBps;
        this.targetFps = targetFps;
    }

    @Override
    public void reportEncodedFrame(int size) {
    }

    @Override
    public int getAdjustedBitrateBps() {
        return this.targetBitrateBps;
    }

    @Override
    public int getCodecConfigFramerate() {
        return this.targetFps;
    }
}

