/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.BaseBitrateAdjuster;

class DynamicBitrateAdjuster
extends BaseBitrateAdjuster {
    private static final double BITRATE_ADJUSTMENT_SEC = 3.0;
    private static final double BITRATE_ADJUSTMENT_MAX_SCALE = 4.0;
    private static final int BITRATE_ADJUSTMENT_STEPS = 20;
    private static final double BITS_PER_BYTE = 8.0;
    private double deviationBytes;
    private double timeSinceLastAdjustmentMs;
    private int bitrateAdjustmentScaleExp;

    DynamicBitrateAdjuster() {
    }

    @Override
    public void setTargets(int targetBitrateBps, int targetFps) {
        if (this.targetBitrateBps > 0 && targetBitrateBps < this.targetBitrateBps) {
            this.deviationBytes = this.deviationBytes * (double)targetBitrateBps / (double)this.targetBitrateBps;
        }
        super.setTargets(targetBitrateBps, targetFps);
    }

    @Override
    public void reportEncodedFrame(int size) {
        if (this.targetFps == 0) {
            return;
        }
        double expectedBytesPerFrame = (double)this.targetBitrateBps / 8.0 / (double)this.targetFps;
        this.deviationBytes += (double)size - expectedBytesPerFrame;
        this.timeSinceLastAdjustmentMs += 1000.0 / (double)this.targetFps;
        double deviationThresholdBytes = (double)this.targetBitrateBps / 8.0;
        double deviationCap = 3.0 * deviationThresholdBytes;
        this.deviationBytes = Math.min(this.deviationBytes, deviationCap);
        this.deviationBytes = Math.max(this.deviationBytes, -deviationCap);
        if (this.timeSinceLastAdjustmentMs <= 3000.0) {
            return;
        }
        if (this.deviationBytes > deviationThresholdBytes) {
            int bitrateAdjustmentInc = (int)(this.deviationBytes / deviationThresholdBytes + 0.5);
            this.bitrateAdjustmentScaleExp -= bitrateAdjustmentInc;
            this.bitrateAdjustmentScaleExp = Math.max(this.bitrateAdjustmentScaleExp, -20);
            this.deviationBytes = deviationThresholdBytes;
        } else if (this.deviationBytes < -deviationThresholdBytes) {
            int bitrateAdjustmentInc = (int)(-this.deviationBytes / deviationThresholdBytes + 0.5);
            this.bitrateAdjustmentScaleExp += bitrateAdjustmentInc;
            this.bitrateAdjustmentScaleExp = Math.min(this.bitrateAdjustmentScaleExp, 20);
            this.deviationBytes = -deviationThresholdBytes;
        }
        this.timeSinceLastAdjustmentMs = 0.0;
    }

    private double getBitrateAdjustmentScale() {
        return Math.pow(4.0, (double)this.bitrateAdjustmentScaleExp / 20.0);
    }

    @Override
    public int getAdjustedBitrateBps() {
        return (int)((double)this.targetBitrateBps * this.getBitrateAdjustmentScale());
    }
}

