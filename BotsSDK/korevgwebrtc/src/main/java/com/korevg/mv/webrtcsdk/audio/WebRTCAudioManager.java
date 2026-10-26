/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.bluetooth.BluetoothAdapter
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.media.AudioManager
 */
package com.korevg.mv.webrtcsdk.audio;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import com.korevg.mv.webrtcsdk.audio.WebRTCAudioRouteListener;
import com.korevg.mv.webrtcsdk.log.Log;
import java.util.ArrayList;
import java.util.List;

public class WebRTCAudioManager {
    private static final String TAG = "WebRTCAudioManager";
    private Context context;
    private AudioManager audioManager;
    private static WebRTCAudioManager instance = null;
    private int audioModeSaveState = -2;
    private boolean speakerSaveState = false;
    private boolean micSaveState = false;
    private boolean bluetoothSCOSaveState = false;
    private AudioRoute currentAudioRoute = AudioRoute.EARPIECE;
    private ArrayList<AudioRoute> audioRoutes = new ArrayList();
    private BroadcastReceiver wiredHeadsetReceiver = new MyBroadcastReceiver();
    private boolean hasHeadset = false;
    private int callCounter;
    private WebRTCAudioRouteListener webRTcAudioRouteListener;

    public static WebRTCAudioManager getInstance() {
        if (instance == null) {
            instance = new WebRTCAudioManager();
        }
        return instance;
    }

    private WebRTCAudioManager() {
    }

    public void init(Context context) {
        this.context = context;
        this.audioManager = (AudioManager)context.getSystemService("audio");
    }

    public WebRTCAudioRouteListener getWebRTcAudioRouteListener() {
        return this.webRTcAudioRouteListener;
    }

    public void setWebRTcAudioRouteListener(WebRTCAudioRouteListener webRTcAudioRouteListener) {
        this.webRTcAudioRouteListener = webRTcAudioRouteListener;
        if (webRTcAudioRouteListener != null) {
            webRTcAudioRouteListener.currentAudioRouteChanged(this.currentAudioRoute);
        }
    }

    public void startCall() {
        Log.d(TAG, "startCall: " + this.callCounter);
        if (this.callCounter == 0) {
            this.currentAudioRoute = AudioRoute.EARPIECE;
            this.audioRoutes.clear();
        }
        this.audioModeSaveState = this.audioManager.getMode();
        ++this.callCounter;
        this.speakerSaveState = this.audioManager.isSpeakerphoneOn();
        this.micSaveState = this.audioManager.isMicrophoneMute();
        this.bluetoothSCOSaveState = this.audioManager.isBluetoothScoOn();
        this.audioManager.requestAudioFocus(null, 0, 2);
        this.audioManager.setMode(3);
        this.setMicrophoneMute(false);
        this.updateAudioRoutes(this.audioManager.isWiredHeadsetOn());
        IntentFilter filter = new IntentFilter("android.intent.action.HEADSET_PLUG");
        filter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        this.context.registerReceiver(this.wiredHeadsetReceiver, filter);
    }

    private boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothAdapter.getProfileConnectionState(1) == 2;
    }

    public void stopCall() {
        --this.callCounter;
        Log.d(TAG, "stopCall: " + this.callCounter);
        if (this.callCounter == 0) {
            try {
                this.setSpeakerphoneOn(this.speakerSaveState);
                this.setMicrophoneMute(this.micSaveState);
                this.setBluetoothSCO(this.bluetoothSCOSaveState);
                this.audioManager.setMode(this.audioModeSaveState);
                this.audioManager.abandonAudioFocus(null);
                this.context.unregisterReceiver(this.wiredHeadsetReceiver);
                Log.d(TAG, "end stopCall");
            }
            catch (Exception e) {
                Log.d(TAG, "oops: " + e.getMessage());
            }
        }
    }

    public boolean setAudioRoute(AudioRoute audioRoute) {
        Log.d(TAG, "Setting audioroute from: " + (Object)((Object)this.currentAudioRoute) + " to: " + (Object)((Object)audioRoute));
        if (audioRoute == this.currentAudioRoute) {
            Log.d(TAG, "current aduio route is already set");
            return true;
        }
        boolean found = false;
        for (AudioRoute route : this.getAvailableAudioRoutes()) {
            if (route != audioRoute) continue;
            found = true;
            break;
        }
        if (!found) {
            Log.d(TAG, "requested audio route is not available");
            return false;
        }
        switch (audioRoute) {
            case SPEAKER_PHONE: {
                this.setSpeakerphoneOn(true);
                this.setBluetoothSCO(false);
                this.currentAudioRoute = AudioRoute.SPEAKER_PHONE;
                break;
            }
            case EARPIECE: {
                this.setSpeakerphoneOn(false);
                this.setBluetoothSCO(false);
                this.currentAudioRoute = AudioRoute.EARPIECE;
                break;
            }
            case WIRED_HEADSET: {
                this.setSpeakerphoneOn(false);
                this.setBluetoothSCO(false);
                this.currentAudioRoute = AudioRoute.WIRED_HEADSET;
                break;
            }
            case BLUETOOTH: {
                this.setSpeakerphoneOn(false);
                this.setBluetoothSCO(true);
                this.currentAudioRoute = AudioRoute.BLUETOOTH;
            }
        }
        if (this.webRTcAudioRouteListener != null) {
            this.webRTcAudioRouteListener.currentAudioRouteChanged(this.currentAudioRoute);
        }
        return true;
    }

    public AudioRoute getCurrentAudioRoute() {
        return this.currentAudioRoute;
    }

    public List<AudioRoute> getAvailableAudioRoutes() {
        return new ArrayList<AudioRoute>(this.audioRoutes);
    }

    private void setBluetoothSCO(boolean on) {
        if (on) {
            this.audioManager.startBluetoothSco();
        } else {
            this.audioManager.stopBluetoothSco();
        }
        boolean wasOn = this.audioManager.isBluetoothScoOn();
        if (wasOn == on) {
            return;
        }
        this.audioManager.setBluetoothScoOn(on);
    }

    private void setSpeakerphoneOn(boolean on) {
        boolean wasOn = this.audioManager.isSpeakerphoneOn();
        if (wasOn == on) {
            return;
        }
        this.audioManager.setSpeakerphoneOn(on);
    }

    private void setMicrophoneMute(boolean on) {
        boolean wasMuted = this.audioManager.isMicrophoneMute();
        if (wasMuted == on) {
            return;
        }
        this.audioManager.setMicrophoneMute(on);
    }

    private void updateAudioRoutes(boolean hasWiredHeadset) {
        boolean audioRoutesChanged = false;
        this.hasHeadset = hasWiredHeadset;
        boolean hasBluetooth = this.isBluetoothHeadsetConnected();
        Log.d(TAG, "hasBluetooth " + hasBluetooth);
        Log.d(TAG, "isBluetoothScoOn: " + this.audioManager.isBluetoothScoOn());
        Log.d(TAG, "isBluetoothScoAvailableOffCall: " + this.audioManager.isBluetoothScoAvailableOffCall());
        Log.d(TAG, "isBluetoothA2dpOn: " + this.audioManager.isBluetoothA2dpOn());
        Log.d(TAG, "isWiredHeadsetOn: " + this.audioManager.isWiredHeadsetOn());
        if (!this.audioRoutes.contains((Object)AudioRoute.SPEAKER_PHONE)) {
            audioRoutesChanged = true;
            this.audioRoutes.add(AudioRoute.SPEAKER_PHONE);
        }
        if (this.context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            if (!this.audioRoutes.contains((Object)AudioRoute.EARPIECE)) {
                audioRoutesChanged = true;
                this.audioRoutes.add(AudioRoute.EARPIECE);
            }
        } else if (this.audioRoutes.contains((Object)AudioRoute.EARPIECE)) {
            audioRoutesChanged = true;
            this.audioRoutes.remove((Object)AudioRoute.EARPIECE);
        }
        if (hasWiredHeadset) {
            if (!this.audioRoutes.contains((Object)AudioRoute.WIRED_HEADSET)) {
                audioRoutesChanged = true;
                this.audioRoutes.add(AudioRoute.WIRED_HEADSET);
                this.setAudioRoute(AudioRoute.WIRED_HEADSET);
            }
        } else if (this.audioRoutes.contains((Object)AudioRoute.WIRED_HEADSET)) {
            audioRoutesChanged = true;
            this.audioRoutes.remove((Object)AudioRoute.WIRED_HEADSET);
            if (this.currentAudioRoute == AudioRoute.WIRED_HEADSET) {
                this.setAudioRoute(AudioRoute.EARPIECE);
            }
        }
        if (hasBluetooth) {
            if (!this.audioRoutes.contains((Object)AudioRoute.BLUETOOTH)) {
                audioRoutesChanged = true;
                this.audioRoutes.add(AudioRoute.BLUETOOTH);
                if (!hasWiredHeadset) {
                    this.setAudioRoute(AudioRoute.BLUETOOTH);
                }
            }
        } else if (this.audioRoutes.contains((Object)AudioRoute.BLUETOOTH)) {
            audioRoutesChanged = true;
            this.audioRoutes.remove((Object)AudioRoute.BLUETOOTH);
            if (this.currentAudioRoute == AudioRoute.BLUETOOTH) {
                this.setAudioRoute(AudioRoute.EARPIECE);
            }
        }
        for (AudioRoute route : this.audioRoutes) {
            Log.d(TAG, "Audio route: " + (Object)((Object)route));
        }
        if (audioRoutesChanged && this.webRTcAudioRouteListener != null) {
            this.webRTcAudioRouteListener.audioRoutesChanged(this.audioRoutes);
        }
    }

    private class MyBroadcastReceiver
    extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(WebRTCAudioManager.TAG, "BroadcastReceiver.onReceive: action=" + action);
            boolean tmpHasHeadSet = WebRTCAudioManager.this.hasHeadset;
            boolean BluetoothConnted = false;
            if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                if (state == 2) {
                    Log.d(WebRTCAudioManager.TAG, "Bluetooth connected");
                    BluetoothConnted = true;
                } else if (state == 0) {
                    Log.d(WebRTCAudioManager.TAG, "Bluetooth disconnected");
                }
            } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 10);
                if (state == 12) {
                    Log.d(WebRTCAudioManager.TAG, "Audio connected");
                } else if (state == 10) {
                    Log.d(WebRTCAudioManager.TAG, "Audio disconnected");
                }
            } else if (action.equals("android.intent.action.HEADSET_PLUG")) {
                int headsetState = intent.getIntExtra("state", 0);
                Log.d(WebRTCAudioManager.TAG, "Headeset headsetState: " + headsetState);
                WebRTCAudioManager.this.hasHeadset = headsetState == 1;
            }
            if (BluetoothConnted) {
                try {
                    Log.d(WebRTCAudioManager.TAG, "SLEEP");
                    Thread.sleep(3000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(WebRTCAudioManager.TAG, "WAKE");
            }
            WebRTCAudioManager.this.updateAudioRoutes(WebRTCAudioManager.this.hasHeadset);
        }
    }

    public static enum AudioRoute {
        SPEAKER_PHONE,
        WIRED_HEADSET,
        EARPIECE,
        BLUETOOTH;

    }
}

