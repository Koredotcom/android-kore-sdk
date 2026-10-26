/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.Logging;
import org.webrtc.NativeLibraryLoader;

class NativeLibrary {
    private static String TAG = "NativeLibrary";
    private static Object lock = new Object();
    private static boolean libraryLoaded;

    NativeLibrary() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void initialize(NativeLibraryLoader loader, String libraryName) {
        Object object = lock;
        synchronized (object) {
            if (libraryLoaded) {
                Logging.d(TAG, "Native library has already been loaded.");
                return;
            }
            Logging.d(TAG, "Loading native library: " + libraryName);
            libraryLoaded = loader.load(libraryName);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static boolean isLoaded() {
        Object object = lock;
        synchronized (object) {
            return libraryLoaded;
        }
    }

    static class DefaultLoader
    implements NativeLibraryLoader {
        DefaultLoader() {
        }

        @Override
        public boolean load(String name) {
            Logging.d(TAG, "Loading library: " + name);
            try {
                System.loadLibrary(name);
            }
            catch (UnsatisfiedLinkError e) {
                Logging.e(TAG, "Failed to load native library: " + name, e);
                return false;
            }
            return true;
        }
    }
}

