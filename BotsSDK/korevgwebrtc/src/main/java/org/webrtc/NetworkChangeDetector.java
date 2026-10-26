/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.List;
import org.webrtc.CalledByNative;

public interface NetworkChangeDetector {
    public ConnectionType getCurrentConnectionType();

    public boolean supportNetworkCallback();

    @Nullable
    public List<NetworkInformation> getActiveNetworkList();

    public void destroy();

    public static abstract class Observer {
        public abstract void onConnectionTypeChanged(ConnectionType var1);

        public abstract void onNetworkConnect(NetworkInformation var1);

        public abstract void onNetworkDisconnect(long var1);

        public abstract void onNetworkPreference(List<ConnectionType> var1, int var2);

        public String getFieldTrialsString() {
            return "";
        }
    }

    public static class NetworkInformation {
        public final String name;
        public final ConnectionType type;
        public final ConnectionType underlyingTypeForVpn;
        public final long handle;
        public final IPAddress[] ipAddresses;

        public NetworkInformation(String name, ConnectionType type, ConnectionType underlyingTypeForVpn, long handle, IPAddress[] addresses) {
            this.name = name;
            this.type = type;
            this.underlyingTypeForVpn = underlyingTypeForVpn;
            this.handle = handle;
            this.ipAddresses = addresses;
        }

        @CalledByNative(value="NetworkInformation")
        private IPAddress[] getIpAddresses() {
            return this.ipAddresses;
        }

        @CalledByNative(value="NetworkInformation")
        private ConnectionType getConnectionType() {
            return this.type;
        }

        @CalledByNative(value="NetworkInformation")
        private ConnectionType getUnderlyingConnectionTypeForVpn() {
            return this.underlyingTypeForVpn;
        }

        @CalledByNative(value="NetworkInformation")
        private long getHandle() {
            return this.handle;
        }

        @CalledByNative(value="NetworkInformation")
        private String getName() {
            return this.name;
        }
    }

    public static class IPAddress {
        public final byte[] address;

        public IPAddress(byte[] address) {
            this.address = address;
        }

        @CalledByNative(value="IPAddress")
        private byte[] getAddress() {
            return this.address;
        }
    }

    public static enum ConnectionType {
        CONNECTION_UNKNOWN,
        CONNECTION_ETHERNET,
        CONNECTION_WIFI,
        CONNECTION_5G,
        CONNECTION_4G,
        CONNECTION_3G,
        CONNECTION_2G,
        CONNECTION_UNKNOWN_CELLULAR,
        CONNECTION_BLUETOOTH,
        CONNECTION_VPN,
        CONNECTION_NONE;

    }
}

