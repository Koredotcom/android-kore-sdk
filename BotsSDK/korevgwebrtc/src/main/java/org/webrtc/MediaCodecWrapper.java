/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import java.nio.ByteBuffer;

interface MediaCodecWrapper {
    public void configure(MediaFormat var1, Surface var2, MediaCrypto var3, int var4);

    public void start();

    public void flush();

    public void stop();

    public void release();

    public int dequeueInputBuffer(long var1);

    public void queueInputBuffer(int var1, int var2, int var3, long var4, int var6);

    public int dequeueOutputBuffer(MediaCodec.BufferInfo var1, long var2);

    public void releaseOutputBuffer(int var1, boolean var2);

    public MediaFormat getInputFormat();

    public MediaFormat getOutputFormat();

    public MediaFormat getOutputFormat(int var1);

    public ByteBuffer getInputBuffer(int var1);

    public ByteBuffer getOutputBuffer(int var1);

    public Surface createInputSurface();

    public void setParameters(Bundle var1);

    public MediaCodecInfo getCodecInfo();
}

