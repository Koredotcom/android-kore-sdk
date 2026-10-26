/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.useragent;

public class AudioConfiguration {
    private boolean audioEchoCancellation;
    private boolean audioAutoGainControl;
    private boolean audioHighpassFilter;
    private boolean audioNoiseSuppression;

    public AudioConfiguration() {
    }

    public AudioConfiguration(boolean audioEchoCancellation, boolean audioAutoGainControl, boolean audioHighpassFilter, boolean audioNoiseSuppression) {
        this.audioEchoCancellation = audioEchoCancellation;
        this.audioAutoGainControl = audioAutoGainControl;
        this.audioHighpassFilter = audioHighpassFilter;
        this.audioNoiseSuppression = audioNoiseSuppression;
    }

    public boolean isAudioEchoCancellation() {
        return this.audioEchoCancellation;
    }

    public void setAudioEchoCancellation(boolean audioEchoCancellation) {
        this.audioEchoCancellation = audioEchoCancellation;
    }

    public boolean isAudioAutoGainControl() {
        return this.audioAutoGainControl;
    }

    public void setAudioAutoGainControl(boolean audioAutoGainControl) {
        this.audioAutoGainControl = audioAutoGainControl;
    }

    public boolean isAudioHighpassFilter() {
        return this.audioHighpassFilter;
    }

    public void setAudioHighpassFilter(boolean audioHighpassFilter) {
        this.audioHighpassFilter = audioHighpassFilter;
    }

    public boolean isAudioNoiseSuppression() {
        return this.audioNoiseSuppression;
    }

    public void setAudioNoiseSuppression(boolean audioNoiseSuppression) {
        this.audioNoiseSuppression = audioNoiseSuppression;
    }
}

