/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioDevInfo;
import org.pjsip.pjsua2.AudioDevInfoVector2;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.MediaFormatAudio;
import org.pjsip.pjsua2.SWIGTYPE_p_p_void;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudDevManager {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected AudDevManager(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudDevManager obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                throw new UnsupportedOperationException("C++ destructor does not have public access");
            }
            this.swigCPtr = 0L;
        }
    }

    public int getCaptureDev() throws Exception {
        return pjsua2JNI.AudDevManager_getCaptureDev(this.swigCPtr, this);
    }

    public AudioMedia getCaptureDevMedia() throws Exception {
        return new AudioMedia(pjsua2JNI.AudDevManager_getCaptureDevMedia(this.swigCPtr, this), false);
    }

    public int getPlaybackDev() throws Exception {
        return pjsua2JNI.AudDevManager_getPlaybackDev(this.swigCPtr, this);
    }

    public AudioMedia getPlaybackDevMedia() throws Exception {
        return new AudioMedia(pjsua2JNI.AudDevManager_getPlaybackDevMedia(this.swigCPtr, this), false);
    }

    public void setCaptureDev(int capture_dev) throws Exception {
        pjsua2JNI.AudDevManager_setCaptureDev(this.swigCPtr, this, capture_dev);
    }

    public void setPlaybackDev(int playback_dev) throws Exception {
        pjsua2JNI.AudDevManager_setPlaybackDev(this.swigCPtr, this, playback_dev);
    }

    public AudioDevInfoVector2 enumDev2() throws Exception {
        return new AudioDevInfoVector2(pjsua2JNI.AudDevManager_enumDev2(this.swigCPtr, this), true);
    }

    public void setNullDev() throws Exception {
        pjsua2JNI.AudDevManager_setNullDev(this.swigCPtr, this);
    }

    public SWIGTYPE_p_p_void setNoDev() {
        long cPtr = pjsua2JNI.AudDevManager_setNoDev(this.swigCPtr, this);
        return cPtr == 0L ? null : new SWIGTYPE_p_p_void(cPtr, false);
    }

    public void setSndDevMode(long mode) throws Exception {
        pjsua2JNI.AudDevManager_setSndDevMode(this.swigCPtr, this, mode);
    }

    public void setEcOptions(long tail_msec, long options) throws Exception {
        pjsua2JNI.AudDevManager_setEcOptions(this.swigCPtr, this, tail_msec, options);
    }

    public long getEcTail() throws Exception {
        return pjsua2JNI.AudDevManager_getEcTail(this.swigCPtr, this);
    }

    public boolean sndIsActive() {
        return pjsua2JNI.AudDevManager_sndIsActive(this.swigCPtr, this);
    }

    public void refreshDevs() throws Exception {
        pjsua2JNI.AudDevManager_refreshDevs(this.swigCPtr, this);
    }

    public long getDevCount() {
        return pjsua2JNI.AudDevManager_getDevCount(this.swigCPtr, this);
    }

    public AudioDevInfo getDevInfo(int id2) throws Exception {
        return new AudioDevInfo(pjsua2JNI.AudDevManager_getDevInfo(this.swigCPtr, this, id2), true);
    }

    public int lookupDev(String drv_name, String dev_name) throws Exception {
        return pjsua2JNI.AudDevManager_lookupDev(this.swigCPtr, this, drv_name, dev_name);
    }

    public String capName(int cap) {
        return pjsua2JNI.AudDevManager_capName(this.swigCPtr, this, cap);
    }

    public void setExtFormat(MediaFormatAudio format, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setExtFormat__SWIG_0(this.swigCPtr, this, MediaFormatAudio.getCPtr(format), format, keep);
    }

    public void setExtFormat(MediaFormatAudio format) throws Exception {
        pjsua2JNI.AudDevManager_setExtFormat__SWIG_1(this.swigCPtr, this, MediaFormatAudio.getCPtr(format), format);
    }

    public MediaFormatAudio getExtFormat() throws Exception {
        return new MediaFormatAudio(pjsua2JNI.AudDevManager_getExtFormat(this.swigCPtr, this), true);
    }

    public void setInputLatency(long latency_msec, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setInputLatency__SWIG_0(this.swigCPtr, this, latency_msec, keep);
    }

    public void setInputLatency(long latency_msec) throws Exception {
        pjsua2JNI.AudDevManager_setInputLatency__SWIG_1(this.swigCPtr, this, latency_msec);
    }

    public long getInputLatency() throws Exception {
        return pjsua2JNI.AudDevManager_getInputLatency(this.swigCPtr, this);
    }

    public void setOutputLatency(long latency_msec, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setOutputLatency__SWIG_0(this.swigCPtr, this, latency_msec, keep);
    }

    public void setOutputLatency(long latency_msec) throws Exception {
        pjsua2JNI.AudDevManager_setOutputLatency__SWIG_1(this.swigCPtr, this, latency_msec);
    }

    public long getOutputLatency() throws Exception {
        return pjsua2JNI.AudDevManager_getOutputLatency(this.swigCPtr, this);
    }

    public void setInputVolume(long volume, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setInputVolume__SWIG_0(this.swigCPtr, this, volume, keep);
    }

    public void setInputVolume(long volume) throws Exception {
        pjsua2JNI.AudDevManager_setInputVolume__SWIG_1(this.swigCPtr, this, volume);
    }

    public long getInputVolume() throws Exception {
        return pjsua2JNI.AudDevManager_getInputVolume(this.swigCPtr, this);
    }

    public void setOutputVolume(long volume, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setOutputVolume__SWIG_0(this.swigCPtr, this, volume, keep);
    }

    public void setOutputVolume(long volume) throws Exception {
        pjsua2JNI.AudDevManager_setOutputVolume__SWIG_1(this.swigCPtr, this, volume);
    }

    public long getOutputVolume() throws Exception {
        return pjsua2JNI.AudDevManager_getOutputVolume(this.swigCPtr, this);
    }

    public long getInputSignal() throws Exception {
        return pjsua2JNI.AudDevManager_getInputSignal(this.swigCPtr, this);
    }

    public long getOutputSignal() throws Exception {
        return pjsua2JNI.AudDevManager_getOutputSignal(this.swigCPtr, this);
    }

    public void setInputRoute(int route, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setInputRoute__SWIG_0(this.swigCPtr, this, route, keep);
    }

    public void setInputRoute(int route) throws Exception {
        pjsua2JNI.AudDevManager_setInputRoute__SWIG_1(this.swigCPtr, this, route);
    }

    public int getInputRoute() throws Exception {
        return pjsua2JNI.AudDevManager_getInputRoute(this.swigCPtr, this);
    }

    public void setOutputRoute(int route, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setOutputRoute__SWIG_0(this.swigCPtr, this, route, keep);
    }

    public void setOutputRoute(int route) throws Exception {
        pjsua2JNI.AudDevManager_setOutputRoute__SWIG_1(this.swigCPtr, this, route);
    }

    public int getOutputRoute() throws Exception {
        return pjsua2JNI.AudDevManager_getOutputRoute(this.swigCPtr, this);
    }

    public void setVad(boolean enable, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setVad__SWIG_0(this.swigCPtr, this, enable, keep);
    }

    public void setVad(boolean enable) throws Exception {
        pjsua2JNI.AudDevManager_setVad__SWIG_1(this.swigCPtr, this, enable);
    }

    public boolean getVad() throws Exception {
        return pjsua2JNI.AudDevManager_getVad(this.swigCPtr, this);
    }

    public void setCng(boolean enable, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setCng__SWIG_0(this.swigCPtr, this, enable, keep);
    }

    public void setCng(boolean enable) throws Exception {
        pjsua2JNI.AudDevManager_setCng__SWIG_1(this.swigCPtr, this, enable);
    }

    public boolean getCng() throws Exception {
        return pjsua2JNI.AudDevManager_getCng(this.swigCPtr, this);
    }

    public void setPlc(boolean enable, boolean keep) throws Exception {
        pjsua2JNI.AudDevManager_setPlc__SWIG_0(this.swigCPtr, this, enable, keep);
    }

    public void setPlc(boolean enable) throws Exception {
        pjsua2JNI.AudDevManager_setPlc__SWIG_1(this.swigCPtr, this, enable);
    }

    public boolean getPlc() throws Exception {
        return pjsua2JNI.AudDevManager_getPlc(this.swigCPtr, this);
    }
}

