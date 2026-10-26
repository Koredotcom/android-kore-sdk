/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.BaseBitrateAdjuster;

class FramerateBitrateAdjuster
extends BaseBitrateAdjuster {
    private static final int INITIAL_FPS = 30;

    FramerateBitrateAdjuster() {
    }

    @Override
    public void setTargets(int targetBitrateBps, int targetFps) {
        if (this.targetFps == 0) {
            targetFps = 30;
        }
        super.setTargets(targetBitrateBps, targetFps);
        this.targetBitrateBps = this.targetBitrateBps * 30 / this.targetFps;
    }

    @Override
    public int getCodecConfigFramerate() {
        return 30;
    }
}

