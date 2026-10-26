/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.webrtc.CalledByNative;
import org.webrtc.ContextUtils;
import org.webrtc.Logging;
import org.webrtc.NetworkChangeDetector;
import org.webrtc.NetworkChangeDetectorFactory;
import org.webrtc.NetworkMonitorAutoDetect;

public class NetworkMonitor {
    private static final String TAG = "NetworkMonitor";
    private NetworkChangeDetectorFactory networkChangeDetectorFactory = new NetworkChangeDetectorFactory(){

        @Override
        public NetworkChangeDetector create(NetworkChangeDetector.Observer observer, Context context) {
            return new NetworkMonitorAutoDetect(observer, context);
        }
    };
    private final ArrayList<Long> nativeNetworkObservers;
    private final ArrayList<NetworkObserver> networkObservers;
    private final Object networkChangeDetectorLock = new Object();
    @Nullable
    private NetworkChangeDetector networkChangeDetector;
    private int numObservers = 0;
    private volatile NetworkChangeDetector.ConnectionType currentConnectionType;

    private NetworkMonitor() {
        this.nativeNetworkObservers = new ArrayList();
        this.networkObservers = new ArrayList();
        this.currentConnectionType = NetworkChangeDetector.ConnectionType.CONNECTION_UNKNOWN;
    }

    public void setNetworkChangeDetectorFactory(NetworkChangeDetectorFactory factory) {
        NetworkMonitor.assertIsTrue(this.numObservers == 0);
        this.networkChangeDetectorFactory = factory;
    }

    @Deprecated
    public static void init(Context context) {
    }

    @CalledByNative
    public static NetworkMonitor getInstance() {
        return InstanceHolder.instance;
    }

    private static void assertIsTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError((Object)"Expected to be true");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void startMonitoring(Context applicationContext) {
        Object object = this.networkChangeDetectorLock;
        synchronized (object) {
            ++this.numObservers;
            if (this.networkChangeDetector == null) {
                this.networkChangeDetector = this.createNetworkChangeDetector(applicationContext);
            }
            this.currentConnectionType = this.networkChangeDetector.getCurrentConnectionType();
        }
    }

    @Deprecated
    public void startMonitoring() {
        this.startMonitoring(ContextUtils.getApplicationContext());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @CalledByNative
    private void startMonitoring(@Nullable Context applicationContext, long nativeObserver) {
        Logging.d(TAG, "Start monitoring with native observer " + nativeObserver);
        this.startMonitoring(applicationContext != null ? applicationContext : ContextUtils.getApplicationContext());
        ArrayList<Long> arrayList = this.nativeNetworkObservers;
        synchronized (arrayList) {
            this.nativeNetworkObservers.add(nativeObserver);
        }
        this.updateObserverActiveNetworkList(nativeObserver);
        this.notifyObserversOfConnectionTypeChange(this.currentConnectionType);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stopMonitoring() {
        Object object = this.networkChangeDetectorLock;
        synchronized (object) {
            if (--this.numObservers == 0) {
                this.networkChangeDetector.destroy();
                this.networkChangeDetector = null;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @CalledByNative
    private void stopMonitoring(long nativeObserver) {
        Logging.d(TAG, "Stop monitoring with native observer " + nativeObserver);
        this.stopMonitoring();
        ArrayList<Long> arrayList = this.nativeNetworkObservers;
        synchronized (arrayList) {
            this.nativeNetworkObservers.remove(nativeObserver);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @CalledByNative
    private boolean networkBindingSupported() {
        Object object = this.networkChangeDetectorLock;
        synchronized (object) {
            return this.networkChangeDetector != null && this.networkChangeDetector.supportNetworkCallback();
        }
    }

    @CalledByNative
    private static int androidSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    private NetworkChangeDetector.ConnectionType getCurrentConnectionType() {
        return this.currentConnectionType;
    }

    private NetworkChangeDetector createNetworkChangeDetector(Context appContext) {
        return this.networkChangeDetectorFactory.create(new NetworkChangeDetector.Observer(){

            @Override
            public void onConnectionTypeChanged(NetworkChangeDetector.ConnectionType newConnectionType) {
                NetworkMonitor.this.updateCurrentConnectionType(newConnectionType);
            }

            @Override
            public void onNetworkConnect(NetworkChangeDetector.NetworkInformation networkInfo) {
                NetworkMonitor.this.notifyObserversOfNetworkConnect(networkInfo);
            }

            @Override
            public void onNetworkDisconnect(long networkHandle) {
                NetworkMonitor.this.notifyObserversOfNetworkDisconnect(networkHandle);
            }

            @Override
            public void onNetworkPreference(List<NetworkChangeDetector.ConnectionType> types, int preference) {
                NetworkMonitor.this.notifyObserversOfNetworkPreference(types, preference);
            }
        }, appContext);
    }

    private void updateCurrentConnectionType(NetworkChangeDetector.ConnectionType newConnectionType) {
        this.currentConnectionType = newConnectionType;
        this.notifyObserversOfConnectionTypeChange(newConnectionType);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void notifyObserversOfConnectionTypeChange(NetworkChangeDetector.ConnectionType newConnectionType) {
        ArrayList<NetworkObserver> javaObservers;
        List<Long> nativeObservers = this.getNativeNetworkObserversSync();
        for (Long nativeObserver : nativeObservers) {
            this.nativeNotifyConnectionTypeChanged(nativeObserver);
        }
        ArrayList<NetworkObserver> arrayList = this.networkObservers;
        synchronized (arrayList) {
            javaObservers = new ArrayList<NetworkObserver>(this.networkObservers);
        }
        for (NetworkObserver observer : javaObservers) {
            observer.onConnectionTypeChanged(newConnectionType);
        }
    }

    private void notifyObserversOfNetworkConnect(NetworkChangeDetector.NetworkInformation networkInfo) {
        List<Long> nativeObservers = this.getNativeNetworkObserversSync();
        for (Long nativeObserver : nativeObservers) {
            this.nativeNotifyOfNetworkConnect(nativeObserver, networkInfo);
        }
    }

    private void notifyObserversOfNetworkDisconnect(long networkHandle) {
        List<Long> nativeObservers = this.getNativeNetworkObserversSync();
        for (Long nativeObserver : nativeObservers) {
            this.nativeNotifyOfNetworkDisconnect(nativeObserver, networkHandle);
        }
    }

    private void notifyObserversOfNetworkPreference(List<NetworkChangeDetector.ConnectionType> types, int preference) {
        List<Long> nativeObservers = this.getNativeNetworkObserversSync();
        for (NetworkChangeDetector.ConnectionType type : types) {
            for (Long nativeObserver : nativeObservers) {
                this.nativeNotifyOfNetworkPreference(nativeObserver, type, preference);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateObserverActiveNetworkList(long nativeObserver) {
        List<NetworkChangeDetector.NetworkInformation> networkInfoList;
        Object object = this.networkChangeDetectorLock;
        synchronized (object) {
            networkInfoList = this.networkChangeDetector == null ? null : this.networkChangeDetector.getActiveNetworkList();
        }
        if (networkInfoList == null || networkInfoList.size() == 0) {
            return;
        }
        NetworkChangeDetector.NetworkInformation[] networkInfos = new NetworkChangeDetector.NetworkInformation[networkInfoList.size()];
        networkInfos = networkInfoList.toArray(networkInfos);
        this.nativeNotifyOfActiveNetworkList(nativeObserver, networkInfos);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<Long> getNativeNetworkObserversSync() {
        ArrayList<Long> arrayList = this.nativeNetworkObservers;
        synchronized (arrayList) {
            return new ArrayList<Long>(this.nativeNetworkObservers);
        }
    }

    @Deprecated
    public static void addNetworkObserver(NetworkObserver observer) {
        NetworkMonitor.getInstance().addObserver(observer);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addObserver(NetworkObserver observer) {
        ArrayList<NetworkObserver> arrayList = this.networkObservers;
        synchronized (arrayList) {
            this.networkObservers.add(observer);
        }
    }

    @Deprecated
    public static void removeNetworkObserver(NetworkObserver observer) {
        NetworkMonitor.getInstance().removeObserver(observer);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeObserver(NetworkObserver observer) {
        ArrayList<NetworkObserver> arrayList = this.networkObservers;
        synchronized (arrayList) {
            this.networkObservers.remove(observer);
        }
    }

    public static boolean isOnline() {
        NetworkChangeDetector.ConnectionType connectionType = NetworkMonitor.getInstance().getCurrentConnectionType();
        return connectionType != NetworkChangeDetector.ConnectionType.CONNECTION_NONE;
    }

    private native void nativeNotifyConnectionTypeChanged(long var1);

    private native void nativeNotifyOfNetworkConnect(long var1, NetworkChangeDetector.NetworkInformation var3);

    private native void nativeNotifyOfNetworkDisconnect(long var1, long var3);

    private native void nativeNotifyOfActiveNetworkList(long var1, NetworkChangeDetector.NetworkInformation[] var3);

    private native void nativeNotifyOfNetworkPreference(long var1, NetworkChangeDetector.ConnectionType var3, int var4);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    NetworkChangeDetector getNetworkChangeDetector() {
        Object object = this.networkChangeDetectorLock;
        synchronized (object) {
            return this.networkChangeDetector;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    int getNumObservers() {
        Object object = this.networkChangeDetectorLock;
        synchronized (object) {
            return this.numObservers;
        }
    }

    static NetworkMonitorAutoDetect createAndSetAutoDetectForTest(Context context) {
        NetworkChangeDetector networkChangeDetector;
        NetworkMonitor networkMonitor = NetworkMonitor.getInstance();
        networkMonitor.networkChangeDetector = networkChangeDetector = networkMonitor.createNetworkChangeDetector(context);
        return (NetworkMonitorAutoDetect)networkChangeDetector;
    }

    private static class InstanceHolder {
        static final NetworkMonitor instance = new NetworkMonitor();

        private InstanceHolder() {
        }
    }

    public static interface NetworkObserver {
        public void onConnectionTypeChanged(NetworkChangeDetector.ConnectionType var1);
    }
}

