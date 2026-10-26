/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.nio.ByteBuffer;
import org.webrtc.CalledByNative;
import org.webrtc.JniCommon;

public class DataChannel {
    private long nativeDataChannel;
    private long nativeObserver;

    @CalledByNative
    public DataChannel(long nativeDataChannel) {
        this.nativeDataChannel = nativeDataChannel;
    }

    public void registerObserver(Observer observer) {
        this.checkDataChannelExists();
        if (this.nativeObserver != 0L) {
            this.nativeUnregisterObserver(this.nativeObserver);
        }
        this.nativeObserver = this.nativeRegisterObserver(observer);
    }

    public void unregisterObserver() {
        this.checkDataChannelExists();
        this.nativeUnregisterObserver(this.nativeObserver);
        this.nativeObserver = 0L;
    }

    public String label() {
        this.checkDataChannelExists();
        return this.nativeLabel();
    }

    public int id() {
        this.checkDataChannelExists();
        return this.nativeId();
    }

    public State state() {
        this.checkDataChannelExists();
        return this.nativeState();
    }

    public long bufferedAmount() {
        this.checkDataChannelExists();
        return this.nativeBufferedAmount();
    }

    public void close() {
        this.checkDataChannelExists();
        this.nativeClose();
    }

    public boolean send(Buffer buffer) {
        this.checkDataChannelExists();
        byte[] data = new byte[buffer.data.remaining()];
        buffer.data.get(data);
        return this.nativeSend(data, buffer.binary);
    }

    public void dispose() {
        this.checkDataChannelExists();
        JniCommon.nativeReleaseRef(this.nativeDataChannel);
        this.nativeDataChannel = 0L;
    }

    @CalledByNative
    long getNativeDataChannel() {
        return this.nativeDataChannel;
    }

    private void checkDataChannelExists() {
        if (this.nativeDataChannel == 0L) {
            throw new IllegalStateException("DataChannel has been disposed.");
        }
    }

    private native long nativeRegisterObserver(Observer var1);

    private native void nativeUnregisterObserver(long var1);

    private native String nativeLabel();

    private native int nativeId();

    private native State nativeState();

    private native long nativeBufferedAmount();

    private native void nativeClose();

    private native boolean nativeSend(byte[] var1, boolean var2);

    public static interface Observer {
        @CalledByNative(value="Observer")
        public void onBufferedAmountChange(long var1);

        @CalledByNative(value="Observer")
        public void onStateChange();

        @CalledByNative(value="Observer")
        public void onMessage(Buffer var1);
    }

    public static enum State {
        CONNECTING,
        OPEN,
        CLOSING,
        CLOSED;


        @CalledByNative(value="State")
        static State fromNativeIndex(int nativeIndex) {
            return State.values()[nativeIndex];
        }
    }

    public static class Buffer {
        public final ByteBuffer data;
        public final boolean binary;

        @CalledByNative(value="Buffer")
        public Buffer(ByteBuffer data, boolean binary) {
            this.data = data;
            this.binary = binary;
        }
    }

    public static class Init {
        public boolean ordered = true;
        public int maxRetransmitTimeMs = -1;
        public int maxRetransmits = -1;
        public String protocol = "";
        public boolean negotiated;
        public int id = -1;

        @CalledByNative(value="Init")
        boolean getOrdered() {
            return this.ordered;
        }

        @CalledByNative(value="Init")
        int getMaxRetransmitTimeMs() {
            return this.maxRetransmitTimeMs;
        }

        @CalledByNative(value="Init")
        int getMaxRetransmits() {
            return this.maxRetransmits;
        }

        @CalledByNative(value="Init")
        String getProtocol() {
            return this.protocol;
        }

        @CalledByNative(value="Init")
        boolean getNegotiated() {
            return this.negotiated;
        }

        @CalledByNative(value="Init")
        int getId() {
            return this.id;
        }
    }
}

