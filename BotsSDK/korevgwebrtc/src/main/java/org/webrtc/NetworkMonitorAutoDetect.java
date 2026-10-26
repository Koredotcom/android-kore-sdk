/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.ConnectivityManager
 *  android.net.ConnectivityManager$NetworkCallback
 *  android.net.LinkAddress
 *  android.net.LinkProperties
 *  android.net.Network
 *  android.net.NetworkCapabilities
 *  android.net.NetworkInfo
 *  android.net.NetworkRequest$Builder
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.p2p.WifiP2pGroup
 *  android.net.wifi.p2p.WifiP2pManager
 *  android.net.wifi.p2p.WifiP2pManager$Channel
 *  android.os.Build$VERSION
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import androidx.annotation.Nullable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.webrtc.Logging;
import org.webrtc.NetworkChangeDetector;
import org.webrtc.PeerConnectionFactory;

public class NetworkMonitorAutoDetect
extends BroadcastReceiver
implements NetworkChangeDetector {
    private static final long INVALID_NET_ID = -1L;
    private static final String TAG = "NetworkMonitorAutoDetect";
    private final NetworkChangeDetector.Observer observer;
    private final IntentFilter intentFilter;
    private final Context context;
    @Nullable
    private final ConnectivityManager.NetworkCallback mobileNetworkCallback;
    @Nullable
    private final ConnectivityManager.NetworkCallback allNetworkCallback;
    private ConnectivityManagerDelegate connectivityManagerDelegate;
    private WifiManagerDelegate wifiManagerDelegate;
    private WifiDirectManagerDelegate wifiDirectManagerDelegate;
    private boolean isRegistered;
    private NetworkChangeDetector.ConnectionType connectionType;
    private String wifiSSID;

    @SuppressLint(value={"NewApi"})
    public NetworkMonitorAutoDetect(NetworkChangeDetector.Observer observer, Context context) {
        this.observer = observer;
        this.context = context;
        this.connectivityManagerDelegate = new ConnectivityManagerDelegate(context);
        this.wifiManagerDelegate = new WifiManagerDelegate(context);
        NetworkState networkState = this.connectivityManagerDelegate.getNetworkState();
        this.connectionType = NetworkMonitorAutoDetect.getConnectionType(networkState);
        this.wifiSSID = this.getWifiSSID(networkState);
        this.intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        if (PeerConnectionFactory.fieldTrialsFindFullName("IncludeWifiDirect").equals("Enabled")) {
            this.wifiDirectManagerDelegate = new WifiDirectManagerDelegate(observer, context);
        }
        this.registerReceiver();
        if (this.connectivityManagerDelegate.supportNetworkCallback()) {
            ConnectivityManager.NetworkCallback tempNetworkCallback = new ConnectivityManager.NetworkCallback();
            try {
                this.connectivityManagerDelegate.requestMobileNetwork(tempNetworkCallback);
            }
            catch (SecurityException e) {
                Logging.w(TAG, "Unable to obtain permission to request a cellular network.");
                tempNetworkCallback = null;
            }
            this.mobileNetworkCallback = tempNetworkCallback;
            this.allNetworkCallback = new SimpleNetworkCallback();
            this.connectivityManagerDelegate.registerNetworkCallback(this.allNetworkCallback);
        } else {
            this.mobileNetworkCallback = null;
            this.allNetworkCallback = null;
        }
    }

    @Override
    public boolean supportNetworkCallback() {
        return this.connectivityManagerDelegate.supportNetworkCallback();
    }

    void setConnectivityManagerDelegateForTests(ConnectivityManagerDelegate delegate) {
        this.connectivityManagerDelegate = delegate;
    }

    void setWifiManagerDelegateForTests(WifiManagerDelegate delegate) {
        this.wifiManagerDelegate = delegate;
    }

    boolean isReceiverRegisteredForTesting() {
        return this.isRegistered;
    }

    @Override
    @Nullable
    public List<NetworkChangeDetector.NetworkInformation> getActiveNetworkList() {
        List<NetworkChangeDetector.NetworkInformation> connectivityManagerList = this.connectivityManagerDelegate.getActiveNetworkList();
        if (connectivityManagerList == null) {
            return null;
        }
        ArrayList<NetworkChangeDetector.NetworkInformation> result = new ArrayList<NetworkChangeDetector.NetworkInformation>(connectivityManagerList);
        if (this.wifiDirectManagerDelegate != null) {
            result.addAll(this.wifiDirectManagerDelegate.getActiveNetworkList());
        }
        return result;
    }

    @Override
    public void destroy() {
        if (this.allNetworkCallback != null) {
            this.connectivityManagerDelegate.releaseCallback(this.allNetworkCallback);
        }
        if (this.mobileNetworkCallback != null) {
            this.connectivityManagerDelegate.releaseCallback(this.mobileNetworkCallback);
        }
        if (this.wifiDirectManagerDelegate != null) {
            this.wifiDirectManagerDelegate.release();
        }
        this.unregisterReceiver();
    }

    private void registerReceiver() {
        if (this.isRegistered) {
            return;
        }
        this.isRegistered = true;
        this.context.registerReceiver((BroadcastReceiver)this, this.intentFilter);
    }

    private void unregisterReceiver() {
        if (!this.isRegistered) {
            return;
        }
        this.isRegistered = false;
        this.context.unregisterReceiver((BroadcastReceiver)this);
    }

    public NetworkState getCurrentNetworkState() {
        return this.connectivityManagerDelegate.getNetworkState();
    }

    public long getDefaultNetId() {
        return this.connectivityManagerDelegate.getDefaultNetId();
    }

    private static NetworkChangeDetector.ConnectionType getConnectionType(boolean isConnected, int networkType, int networkSubtype) {
        if (!isConnected) {
            return NetworkChangeDetector.ConnectionType.CONNECTION_NONE;
        }
        switch (networkType) {
            case 9: {
                return NetworkChangeDetector.ConnectionType.CONNECTION_ETHERNET;
            }
            case 1: {
                return NetworkChangeDetector.ConnectionType.CONNECTION_WIFI;
            }
            case 6: {
                return NetworkChangeDetector.ConnectionType.CONNECTION_4G;
            }
            case 7: {
                return NetworkChangeDetector.ConnectionType.CONNECTION_BLUETOOTH;
            }
            case 0: {
                switch (networkSubtype) {
                    case 1: 
                    case 2: 
                    case 4: 
                    case 7: 
                    case 11: 
                    case 16: {
                        return NetworkChangeDetector.ConnectionType.CONNECTION_2G;
                    }
                    case 3: 
                    case 5: 
                    case 6: 
                    case 8: 
                    case 9: 
                    case 10: 
                    case 12: 
                    case 14: 
                    case 15: 
                    case 17: {
                        return NetworkChangeDetector.ConnectionType.CONNECTION_3G;
                    }
                    case 13: 
                    case 18: {
                        return NetworkChangeDetector.ConnectionType.CONNECTION_4G;
                    }
                    case 20: {
                        return NetworkChangeDetector.ConnectionType.CONNECTION_5G;
                    }
                }
                return NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN_CELLULAR;
            }
            case 17: {
                return NetworkChangeDetector.ConnectionType.CONNECTION_VPN;
            }
        }
        return NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN;
    }

    public static NetworkChangeDetector.ConnectionType getConnectionType(NetworkState networkState) {
        return NetworkMonitorAutoDetect.getConnectionType(networkState.isConnected(), networkState.getNetworkType(), networkState.getNetworkSubType());
    }

    @Override
    public NetworkChangeDetector.ConnectionType getCurrentConnectionType() {
        return NetworkMonitorAutoDetect.getConnectionType(this.getCurrentNetworkState());
    }

    private static NetworkChangeDetector.ConnectionType getUnderlyingConnectionTypeForVpn(NetworkState networkState) {
        if (networkState.getNetworkType() != 17) {
            return NetworkChangeDetector.ConnectionType.CONNECTION_NONE;
        }
        return NetworkMonitorAutoDetect.getConnectionType(networkState.isConnected(), networkState.getUnderlyingNetworkTypeForVpn(), networkState.getUnderlyingNetworkSubtypeForVpn());
    }

    private String getWifiSSID(NetworkState networkState) {
        if (NetworkMonitorAutoDetect.getConnectionType(networkState) != NetworkChangeDetector.ConnectionType.CONNECTION_WIFI) {
            return "";
        }
        return this.wifiManagerDelegate.getWifiSSID();
    }

    public void onReceive(Context context, Intent intent) {
        NetworkState networkState = this.getCurrentNetworkState();
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            this.connectionTypeChanged(networkState);
        }
    }

    private void connectionTypeChanged(NetworkState networkState) {
        NetworkChangeDetector.ConnectionType newConnectionType = NetworkMonitorAutoDetect.getConnectionType(networkState);
        String newWifiSSID = this.getWifiSSID(networkState);
        if (newConnectionType == this.connectionType && newWifiSSID.equals(this.wifiSSID)) {
            return;
        }
        this.connectionType = newConnectionType;
        this.wifiSSID = newWifiSSID;
        Logging.d(TAG, "Network connectivity changed, type is: " + (Object)((Object)this.connectionType));
        this.observer.onConnectionTypeChanged(newConnectionType);
    }

    @SuppressLint(value={"NewApi"})
    private static long networkToNetId(Network network) {
        if (Build.VERSION.SDK_INT >= 23) {
            return network.getNetworkHandle();
        }
        return Integer.parseInt(network.toString());
    }

    static class WifiDirectManagerDelegate
    extends BroadcastReceiver {
        private static final int WIFI_P2P_NETWORK_HANDLE = 0;
        private final Context context;
        private final NetworkChangeDetector.Observer observer;
        @Nullable
        private NetworkChangeDetector.NetworkInformation wifiP2pNetworkInfo;

        WifiDirectManagerDelegate(NetworkChangeDetector.Observer observer, Context context) {
            this.context = context;
            this.observer = observer;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
            intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
            context.registerReceiver((BroadcastReceiver)this, intentFilter);
            if (Build.VERSION.SDK_INT > 28) {
                WifiP2pManager manager = (WifiP2pManager)context.getSystemService("wifip2p");
                WifiP2pManager.Channel channel = manager.initialize(context, context.getMainLooper(), null);
                manager.requestGroupInfo(channel, wifiP2pGroup -> this.onWifiP2pGroupChange(wifiP2pGroup));
            }
        }

        @SuppressLint(value={"InlinedApi"})
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(intent.getAction())) {
                WifiP2pGroup wifiP2pGroup = (WifiP2pGroup)intent.getParcelableExtra("p2pGroupInfo");
                this.onWifiP2pGroupChange(wifiP2pGroup);
            } else if ("android.net.wifi.p2p.STATE_CHANGED".equals(intent.getAction())) {
                int state = intent.getIntExtra("wifi_p2p_state", 0);
                this.onWifiP2pStateChange(state);
            }
        }

        public void release() {
            this.context.unregisterReceiver((BroadcastReceiver)this);
        }

        public List<NetworkChangeDetector.NetworkInformation> getActiveNetworkList() {
            if (this.wifiP2pNetworkInfo != null) {
                return Collections.singletonList(this.wifiP2pNetworkInfo);
            }
            return Collections.emptyList();
        }

        private void onWifiP2pGroupChange(@Nullable WifiP2pGroup wifiP2pGroup) {
            NetworkInterface wifiP2pInterface;
            if (wifiP2pGroup == null || wifiP2pGroup.getInterface() == null) {
                return;
            }
            try {
                wifiP2pInterface = NetworkInterface.getByName(wifiP2pGroup.getInterface());
            }
            catch (SocketException e) {
                Logging.e(NetworkMonitorAutoDetect.TAG, "Unable to get WifiP2p network interface", e);
                return;
            }
            ArrayList<InetAddress> interfaceAddresses = Collections.list(wifiP2pInterface.getInetAddresses());
            NetworkChangeDetector.IPAddress[] ipAddresses = new NetworkChangeDetector.IPAddress[interfaceAddresses.size()];
            for (int i = 0; i < interfaceAddresses.size(); ++i) {
                ipAddresses[i] = new NetworkChangeDetector.IPAddress(((InetAddress)interfaceAddresses.get(i)).getAddress());
            }
            this.wifiP2pNetworkInfo = new NetworkChangeDetector.NetworkInformation(wifiP2pGroup.getInterface(), NetworkChangeDetector.ConnectionType.CONNECTION_WIFI, NetworkChangeDetector.ConnectionType.CONNECTION_NONE, 0L, ipAddresses);
            this.observer.onNetworkConnect(this.wifiP2pNetworkInfo);
        }

        private void onWifiP2pStateChange(int state) {
            if (state == 1) {
                this.wifiP2pNetworkInfo = null;
                this.observer.onNetworkDisconnect(0L);
            }
        }
    }

    static class WifiManagerDelegate {
        @Nullable
        private final Context context;

        WifiManagerDelegate(Context context) {
            this.context = context;
        }

        WifiManagerDelegate() {
            this.context = null;
        }

        String getWifiSSID() {
            String ssid;
            WifiInfo wifiInfo;
            Intent intent = this.context.registerReceiver(null, new IntentFilter("android.net.wifi.STATE_CHANGE"));
            if (intent != null && (wifiInfo = (WifiInfo)intent.getParcelableExtra("wifiInfo")) != null && (ssid = wifiInfo.getSSID()) != null) {
                return ssid;
            }
            return "";
        }
    }

    static class ConnectivityManagerDelegate {
        @Nullable
        private final ConnectivityManager connectivityManager;

        ConnectivityManagerDelegate(Context context) {
            this.connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        }

        ConnectivityManagerDelegate() {
            this.connectivityManager = null;
        }

        NetworkState getNetworkState() {
            if (this.connectivityManager == null) {
                return new NetworkState(false, -1, -1, -1, -1);
            }
            return this.getNetworkState(this.connectivityManager.getActiveNetworkInfo());
        }

        @SuppressLint(value={"NewApi"})
        NetworkState getNetworkState(@Nullable Network network) {
            if (network == null || this.connectivityManager == null) {
                return new NetworkState(false, -1, -1, -1, -1);
            }
            NetworkInfo networkInfo = this.connectivityManager.getNetworkInfo(network);
            if (networkInfo == null) {
                Logging.w(NetworkMonitorAutoDetect.TAG, "Couldn't retrieve information from network " + network.toString());
                return new NetworkState(false, -1, -1, -1, -1);
            }
            if (networkInfo.getType() != 17) {
                NetworkCapabilities networkCapabilities = this.connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities == null || !networkCapabilities.hasTransport(4)) {
                    return this.getNetworkState(networkInfo);
                }
                return new NetworkState(networkInfo.isConnected(), 17, -1, networkInfo.getType(), networkInfo.getSubtype());
            }
            if (networkInfo.getType() == 17) {
                NetworkInfo underlyingActiveNetworkInfo;
                if (Build.VERSION.SDK_INT >= 23 && network.equals((Object)this.connectivityManager.getActiveNetwork()) && (underlyingActiveNetworkInfo = this.connectivityManager.getActiveNetworkInfo()) != null && underlyingActiveNetworkInfo.getType() != 17) {
                    return new NetworkState(networkInfo.isConnected(), 17, -1, underlyingActiveNetworkInfo.getType(), underlyingActiveNetworkInfo.getSubtype());
                }
                return new NetworkState(networkInfo.isConnected(), 17, -1, -1, -1);
            }
            return this.getNetworkState(networkInfo);
        }

        private NetworkState getNetworkState(@Nullable NetworkInfo networkInfo) {
            if (networkInfo == null || !networkInfo.isConnected()) {
                return new NetworkState(false, -1, -1, -1, -1);
            }
            return new NetworkState(true, networkInfo.getType(), networkInfo.getSubtype(), -1, -1);
        }

        @SuppressLint(value={"NewApi"})
        Network[] getAllNetworks() {
            if (this.connectivityManager == null) {
                return new Network[0];
            }
            return this.connectivityManager.getAllNetworks();
        }

        @Nullable
        List<NetworkChangeDetector.NetworkInformation> getActiveNetworkList() {
            if (!this.supportNetworkCallback()) {
                return null;
            }
            ArrayList<NetworkChangeDetector.NetworkInformation> netInfoList = new ArrayList<NetworkChangeDetector.NetworkInformation>();
            for (Network network : this.getAllNetworks()) {
                NetworkChangeDetector.NetworkInformation info = this.networkToInfo(network);
                if (info == null) continue;
                netInfoList.add(info);
            }
            return netInfoList;
        }

        @SuppressLint(value={"NewApi"})
        long getDefaultNetId() {
            if (!this.supportNetworkCallback()) {
                return -1L;
            }
            NetworkInfo defaultNetworkInfo = this.connectivityManager.getActiveNetworkInfo();
            if (defaultNetworkInfo == null) {
                return -1L;
            }
            Network[] networks = this.getAllNetworks();
            long defaultNetId = -1L;
            for (Network network : networks) {
                NetworkInfo networkInfo;
                if (!this.hasInternetCapability(network) || (networkInfo = this.connectivityManager.getNetworkInfo(network)) == null || networkInfo.getType() != defaultNetworkInfo.getType()) continue;
                if (defaultNetId != -1L) {
                    throw new RuntimeException("Multiple connected networks of same type are not supported.");
                }
                defaultNetId = NetworkMonitorAutoDetect.networkToNetId(network);
            }
            return defaultNetId;
        }

        @SuppressLint(value={"NewApi"})
        @Nullable
        private NetworkChangeDetector.NetworkInformation networkToInfo(@Nullable Network network) {
            if (network == null || this.connectivityManager == null) {
                return null;
            }
            LinkProperties linkProperties = this.connectivityManager.getLinkProperties(network);
            if (linkProperties == null) {
                Logging.w(NetworkMonitorAutoDetect.TAG, "Detected unknown network: " + network.toString());
                return null;
            }
            if (linkProperties.getInterfaceName() == null) {
                Logging.w(NetworkMonitorAutoDetect.TAG, "Null interface name for network " + network.toString());
                return null;
            }
            NetworkState networkState = this.getNetworkState(network);
            NetworkChangeDetector.ConnectionType connectionType = NetworkMonitorAutoDetect.getConnectionType(networkState);
            if (connectionType == NetworkChangeDetector.ConnectionType.CONNECTION_NONE) {
                Logging.d(NetworkMonitorAutoDetect.TAG, "Network " + network.toString() + " is disconnected");
                return null;
            }
            if (connectionType == NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN || connectionType == NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN_CELLULAR) {
                Logging.d(NetworkMonitorAutoDetect.TAG, "Network " + network.toString() + " connection type is " + (Object)((Object)connectionType) + " because it has type " + networkState.getNetworkType() + " and subtype " + networkState.getNetworkSubType());
            }
            NetworkChangeDetector.ConnectionType underlyingConnectionTypeForVpn = NetworkMonitorAutoDetect.getUnderlyingConnectionTypeForVpn(networkState);
            NetworkChangeDetector.NetworkInformation networkInformation = new NetworkChangeDetector.NetworkInformation(linkProperties.getInterfaceName(), connectionType, underlyingConnectionTypeForVpn, NetworkMonitorAutoDetect.networkToNetId(network), this.getIPAddresses(linkProperties));
            return networkInformation;
        }

        @SuppressLint(value={"NewApi"})
        boolean hasInternetCapability(Network network) {
            if (this.connectivityManager == null) {
                return false;
            }
            NetworkCapabilities capabilities = this.connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasCapability(12);
        }

        @SuppressLint(value={"NewApi"})
        public void registerNetworkCallback(ConnectivityManager.NetworkCallback networkCallback) {
            this.connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().addCapability(12).build(), networkCallback);
        }

        @SuppressLint(value={"NewApi"})
        public void requestMobileNetwork(ConnectivityManager.NetworkCallback networkCallback) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(12).addTransportType(0);
            this.connectivityManager.requestNetwork(builder.build(), networkCallback);
        }

        @SuppressLint(value={"NewApi"})
        NetworkChangeDetector.IPAddress[] getIPAddresses(LinkProperties linkProperties) {
            NetworkChangeDetector.IPAddress[] ipAddresses = new NetworkChangeDetector.IPAddress[linkProperties.getLinkAddresses().size()];
            int i = 0;
            for (LinkAddress linkAddress : linkProperties.getLinkAddresses()) {
                ipAddresses[i] = new NetworkChangeDetector.IPAddress(linkAddress.getAddress().getAddress());
                ++i;
            }
            return ipAddresses;
        }

        @SuppressLint(value={"NewApi"})
        public void releaseCallback(ConnectivityManager.NetworkCallback networkCallback) {
            if (this.supportNetworkCallback()) {
                Logging.d(NetworkMonitorAutoDetect.TAG, "Unregister network callback");
                this.connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }

        public boolean supportNetworkCallback() {
            return Build.VERSION.SDK_INT >= 21 && this.connectivityManager != null;
        }
    }

    @SuppressLint(value={"NewApi"})
    private class SimpleNetworkCallback
    extends ConnectivityManager.NetworkCallback {
        private SimpleNetworkCallback() {
        }

        public void onAvailable(Network network) {
            Logging.d(NetworkMonitorAutoDetect.TAG, "Network becomes available: " + network.toString());
            this.onNetworkChanged(network);
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            Logging.d(NetworkMonitorAutoDetect.TAG, "capabilities changed: " + networkCapabilities.toString());
            this.onNetworkChanged(network);
        }

        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            Logging.d(NetworkMonitorAutoDetect.TAG, "link properties changed");
            this.onNetworkChanged(network);
        }

        public void onLosing(Network network, int maxMsToLive) {
            Logging.d(NetworkMonitorAutoDetect.TAG, "Network " + network.toString() + " is about to lose in " + maxMsToLive + "ms");
        }

        public void onLost(Network network) {
            Logging.d(NetworkMonitorAutoDetect.TAG, "Network " + network.toString() + " is disconnected");
            NetworkMonitorAutoDetect.this.observer.onNetworkDisconnect(NetworkMonitorAutoDetect.networkToNetId(network));
        }

        private void onNetworkChanged(Network network) {
            NetworkChangeDetector.NetworkInformation networkInformation = NetworkMonitorAutoDetect.this.connectivityManagerDelegate.networkToInfo(network);
            if (networkInformation != null) {
                NetworkMonitorAutoDetect.this.observer.onNetworkConnect(networkInformation);
            }
        }
    }

    static class NetworkState {
        private final boolean connected;
        private final int type;
        private final int subtype;
        private final int underlyingNetworkTypeForVpn;
        private final int underlyingNetworkSubtypeForVpn;

        public NetworkState(boolean connected, int type, int subtype, int underlyingNetworkTypeForVpn, int underlyingNetworkSubtypeForVpn) {
            this.connected = connected;
            this.type = type;
            this.subtype = subtype;
            this.underlyingNetworkTypeForVpn = underlyingNetworkTypeForVpn;
            this.underlyingNetworkSubtypeForVpn = underlyingNetworkSubtypeForVpn;
        }

        public boolean isConnected() {
            return this.connected;
        }

        public int getNetworkType() {
            return this.type;
        }

        public int getNetworkSubType() {
            return this.subtype;
        }

        public int getUnderlyingNetworkTypeForVpn() {
            return this.underlyingNetworkTypeForVpn;
        }

        public int getUnderlyingNetworkSubtypeForVpn() {
            return this.underlyingNetworkSubtypeForVpn;
        }
    }
}

