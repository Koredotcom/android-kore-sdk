/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.BaseBitrateAdjuster;

class FramerateBitrateAdjuster
extends BaseBitrateAdjuster {
    private static final int DEFAULT_FRAMERATE_FPS = 30;

    FramerateBitrateAdjuster() {
    }

    @Override
    public void setTargets(int targetBitrateBps, double targetFramerateFps) {
        this.targetFramerateFps = 30.0;
        this.targetBitrateBps = (int)((double)(targetBitrateBps * 30) / targetFramerateFps);
    }
}

