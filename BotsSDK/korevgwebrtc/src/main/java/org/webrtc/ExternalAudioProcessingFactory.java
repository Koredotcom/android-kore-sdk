/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.nio.ByteBuffer;
import org.webrtc.AudioProcessingFactory;
import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;

public class ExternalAudioProcessingFactory
implements AudioProcessingFactory {
    private long apmPtr = ExternalAudioProcessingFactory.nativeGetDefaultApm();
    private long capturePostProcessingPtr = 0L;
    private long renderPreProcessingPtr = 0L;

    @Override
    public long createNative() {
        if (this.apmPtr == 0L) {
            this.apmPtr = ExternalAudioProcessingFactory.nativeGetDefaultApm();
        }
        return this.apmPtr;
    }

    public void setCapturePostProcessing(@Nullable AudioProcessing processing) {
        this.checkExternalAudioProcessorExists();
        long newPtr = ExternalAudioProcessingFactory.nativeSetCapturePostProcessing(processing);
        if (this.capturePostProcessingPtr != 0L) {
            JniCommon.nativeReleaseRef(this.capturePostProcessingPtr);
            this.capturePostProcessingPtr = 0L;
        }
        this.capturePostProcessingPtr = newPtr;
    }

    public void setRenderPreProcessing(@Nullable AudioProcessing processing) {
        this.checkExternalAudioProcessorExists();
        long newPtr = ExternalAudioProcessingFactory.nativeSetRenderPreProcessing(processing);
        if (this.renderPreProcessingPtr != 0L) {
            JniCommon.nativeReleaseRef(this.renderPreProcessingPtr);
            this.renderPreProcessingPtr = 0L;
        }
        this.renderPreProcessingPtr = newPtr;
    }

    public void setBypassFlagForCapturePost(boolean bypass) {
        this.checkExternalAudioProcessorExists();
        ExternalAudioProcessingFactory.nativeSetBypassFlagForCapturePost(bypass);
    }

    public void setBypassFlagForRenderPre(boolean bypass) {
        this.checkExternalAudioProcessorExists();
        ExternalAudioProcessingFactory.nativeSetBypassFlagForRenderPre(bypass);
    }

    public void destroy() {
        this.checkExternalAudioProcessorExists();
        if (this.renderPreProcessingPtr != 0L) {
            JniCommon.nativeReleaseRef(this.renderPreProcessingPtr);
            this.renderPreProcessingPtr = 0L;
        }
        if (this.capturePostProcessingPtr != 0L) {
            JniCommon.nativeReleaseRef(this.capturePostProcessingPtr);
            this.capturePostProcessingPtr = 0L;
        }
        ExternalAudioProcessingFactory.nativeDestroy();
        this.apmPtr = 0L;
    }

    private void checkExternalAudioProcessorExists() {
        if (this.apmPtr == 0L) {
            throw new IllegalStateException("ExternalAudioProcessor has been disposed.");
        }
    }

    private static native long nativeGetDefaultApm();

    private static native long nativeSetCapturePostProcessing(AudioProcessing var0);

    private static native long nativeSetRenderPreProcessing(AudioProcessing var0);

    private static native void nativeSetBypassFlagForCapturePost(boolean var0);

    private static native void nativeSetBypassFlagForRenderPre(boolean var0);

    private static native void nativeDestroy();

    public static interface AudioProcessing {
        @CalledByNative(value="AudioProcessing")
        public void initialize(int var1, int var2);

        @CalledByNative(value="AudioProcessing")
        public void reset(int var1);

        @CalledByNative(value="AudioProcessing")
        public void process(int var1, int var2, ByteBuffer var3);
    }
}

