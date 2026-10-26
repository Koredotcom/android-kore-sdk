/*
 * Decompiled with CFR 0.152.
 */
package org.pjsip.pjsua2;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AudioMediaPlayerInfo;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsua2JNI;

public class AudioMediaPlayer
extends AudioMedia {
    private transient long swigCPtr;

    protected AudioMediaPlayer(long cPtr, boolean cMemoryOwn) {
        super(pjsua2JNI.AudioMediaPlayer_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(AudioMediaPlayer obj) {
        return obj == null ? 0L : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    public synchronized void delete() {
        if (this.swigCPtr != 0L) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                pjsua2JNI.delete_AudioMediaPlayer(this.swigCPtr);
            }
            this.swigCPtr = 0L;
        }
        super.delete();
    }

    protected void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        this.delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        pjsua2JNI.AudioMediaPlayer_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        pjsua2JNI.AudioMediaPlayer_change_ownership(this, this.swigCPtr, true);
    }

    public AudioMediaPlayer() {
        this(pjsua2JNI.new_AudioMediaPlayer(), true);
        pjsua2JNI.AudioMediaPlayer_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }

    public void createPlayer(String file_name, long options) throws Exception {
        pjsua2JNI.AudioMediaPlayer_createPlayer__SWIG_0(this.swigCPtr, this, file_name, options);
    }

    public void createPlayer(String file_name) throws Exception {
        pjsua2JNI.AudioMediaPlayer_createPlayer__SWIG_1(this.swigCPtr, this, file_name);
    }

    public void createPlaylist(StringVector file_names, String label, long options) throws Exception {
        pjsua2JNI.AudioMediaPlayer_createPlaylist__SWIG_0(this.swigCPtr, this, StringVector.getCPtr(file_names), file_names, label, options);
    }

    public void createPlaylist(StringVector file_names, String label) throws Exception {
        pjsua2JNI.AudioMediaPlayer_createPlaylist__SWIG_1(this.swigCPtr, this, StringVector.getCPtr(file_names), file_names, label);
    }

    public void createPlaylist(StringVector file_names) throws Exception {
        pjsua2JNI.AudioMediaPlayer_createPlaylist__SWIG_2(this.swigCPtr, this, StringVector.getCPtr(file_names), file_names);
    }

    public AudioMediaPlayerInfo getInfo() throws Exception {
        return new AudioMediaPlayerInfo(pjsua2JNI.AudioMediaPlayer_getInfo(this.swigCPtr, this), true);
    }

    public long getPos() throws Exception {
        return pjsua2JNI.AudioMediaPlayer_getPos(this.swigCPtr, this);
    }

    public void setPos(long samples) throws Exception {
        pjsua2JNI.AudioMediaPlayer_setPos(this.swigCPtr, this, samples);
    }

    public static AudioMediaPlayer typecastFromAudioMedia(AudioMedia media) {
        long cPtr = pjsua2JNI.AudioMediaPlayer_typecastFromAudioMedia(AudioMedia.getCPtr(media), media);
        return cPtr == 0L ? null : new AudioMediaPlayer(cPtr, false);
    }

    public void onEof2() {
        if (this.getClass() == AudioMediaPlayer.class) {
            pjsua2JNI.AudioMediaPlayer_onEof2(this.swigCPtr, this);
        } else {
            pjsua2JNI.AudioMediaPlayer_onEof2SwigExplicitAudioMediaPlayer(this.swigCPtr, this);
        }
    }
}

