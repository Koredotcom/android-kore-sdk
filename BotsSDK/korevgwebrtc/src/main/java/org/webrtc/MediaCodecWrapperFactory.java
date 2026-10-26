/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.io.IOException;
import org.webrtc.MediaCodecWrapper;

interface MediaCodecWrapperFactory {
    public MediaCodecWrapper createByCodecName(String var1) throws IOException;
}

