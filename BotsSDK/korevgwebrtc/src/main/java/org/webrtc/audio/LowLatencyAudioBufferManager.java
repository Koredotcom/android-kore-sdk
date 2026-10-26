/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.AudioTrack
 *  android.os.Build$VERSION
 */
package org.webrtc.audio;

import android.media.AudioTrack;
import android.os.Build;
import org.webrtc.Logging;

class LowLatencyAudioBufferManager {
    private static final String TAG = "LowLatencyAudioBufferManager";
    private int prevUnderrunCount = 0;
    private int ticksUntilNextDecrease = 10;
    private boolean keepLoweringBufferSize = true;
    private int bufferIncreaseCounter = 0;

    public void maybeAdjustBufferSize(AudioTrack audioTrack) {
        if (Build.VERSION.SDK_INT >= 26) {
            int underrunCount = audioTrack.getUnderrunCount();
            if (underrunCount > this.prevUnderrunCount) {
                if (this.bufferIncreaseCounter < 5) {
                    int currentBufferSize = audioTrack.getBufferSizeInFrames();
                    int newBufferSize = currentBufferSize + audioTrack.getPlaybackRate() / 100;
                    Logging.d(TAG, "Underrun detected! Increasing AudioTrack buffer size from " + currentBufferSize + " to " + newBufferSize);
                    audioTrack.setBufferSizeInFrames(newBufferSize);
                    ++this.bufferIncreaseCounter;
                }
                this.keepLoweringBufferSize = false;
                this.prevUnderrunCount = underrunCount;
                this.ticksUntilNextDecrease = 10;
            } else if (this.keepLoweringBufferSize) {
                --this.ticksUntilNextDecrease;
                if (this.ticksUntilNextDecrease <= 0) {
                    int currentBufferSize;
                    int bufferSize10ms = audioTrack.getPlaybackRate() / 100;
                    int newBufferSize = Math.max(bufferSize10ms, (currentBufferSize = audioTrack.getBufferSizeInFrames()) - bufferSize10ms);
                    if (newBufferSize != currentBufferSize) {
                        Logging.d(TAG, "Lowering AudioTrack buffer size from " + currentBufferSize + " to " + newBufferSize);
                        audioTrack.setBufferSizeInFrames(newBufferSize);
                    }
                    this.ticksUntilNextDecrease = 10;
                }
            }
        }
    }
}

