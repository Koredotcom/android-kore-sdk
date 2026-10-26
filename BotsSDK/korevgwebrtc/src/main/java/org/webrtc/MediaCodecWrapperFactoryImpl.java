/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.MediaCodec
 *  android.media.MediaCodec$BufferInfo
 *  android.media.MediaCodecInfo
 *  android.media.MediaCrypto
 *  android.media.MediaFormat
 *  android.os.Bundle
 *  android.view.Surface
 */
package org.webrtc;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.webrtc.MediaCodecWrapper;
import org.webrtc.MediaCodecWrapperFactory;

class MediaCodecWrapperFactoryImpl
implements MediaCodecWrapperFactory {
    MediaCodecWrapperFactoryImpl() {
    }

    @Override
    public MediaCodecWrapper createByCodecName(String name) throws IOException {
        return new MediaCodecWrapperImpl(MediaCodec.createByCodecName((String)name));
    }

    private static class MediaCodecWrapperImpl
    implements MediaCodecWrapper {
        private final MediaCodec mediaCodec;

        public MediaCodecWrapperImpl(MediaCodec mediaCodec) {
            this.mediaCodec = mediaCodec;
        }

        @Override
        public void configure(MediaFormat format, Surface surface, MediaCrypto crypto, int flags) {
            this.mediaCodec.configure(format, surface, crypto, flags);
        }

        @Override
        public void start() {
            this.mediaCodec.start();
        }

        @Override
        public void flush() {
            this.mediaCodec.flush();
        }

        @Override
        public void stop() {
            this.mediaCodec.stop();
        }

        @Override
        public void release() {
            this.mediaCodec.release();
        }

        @Override
        public int dequeueInputBuffer(long timeoutUs) {
            return this.mediaCodec.dequeueInputBuffer(timeoutUs);
        }

        @Override
        public void queueInputBuffer(int index, int offset, int size, long presentationTimeUs, int flags) {
            this.mediaCodec.queueInputBuffer(index, offset, size, presentationTimeUs, flags);
        }

        @Override
        public int dequeueOutputBuffer(MediaCodec.BufferInfo info, long timeoutUs) {
            return this.mediaCodec.dequeueOutputBuffer(info, timeoutUs);
        }

        @Override
        public void releaseOutputBuffer(int index, boolean render) {
            this.mediaCodec.releaseOutputBuffer(index, render);
        }

        @Override
        public MediaFormat getInputFormat() {
            return this.mediaCodec.getInputFormat();
        }

        @Override
        public MediaFormat getOutputFormat() {
            return this.mediaCodec.getOutputFormat();
        }

        @Override
        public MediaFormat getOutputFormat(int index) {
            return this.mediaCodec.getOutputFormat(index);
        }

        @Override
        public ByteBuffer getInputBuffer(int index) {
            return this.mediaCodec.getInputBuffer(index);
        }

        @Override
        public ByteBuffer getOutputBuffer(int index) {
            return this.mediaCodec.getOutputBuffer(index);
        }

        @Override
        public Surface createInputSurface() {
            return this.mediaCodec.createInputSurface();
        }

        @Override
        public void setParameters(Bundle params) {
            this.mediaCodec.setParameters(params);
        }

        @Override
        public MediaCodecInfo getCodecInfo() {
            return this.mediaCodec.getCodecInfo();
        }
    }
}

