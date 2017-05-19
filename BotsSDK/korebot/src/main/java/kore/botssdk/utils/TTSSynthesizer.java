package kore.botssdk.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Pradeep Mahato on 19-May-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class TTSSynthesizer {

    private TextToSpeech textToSpeech;

    private Context context;
    private MediaPlayer mediaPlayer;

    public TTSSynthesizer(Context context) {
        this.context = context;
        initNative(context);
    }

    public TextToSpeech initNative(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        return textToSpeech;
    }

    public void speak(String textualMessage) {
        if (Constants.ENABLE_SDK) {
            speakViaSDK(textualMessage);
        } else {
            speakViaNative(textualMessage);
        }
    }

    private void speakViaSDK(String textualMessage) {
        String modifiedTextualMessage = textualMessage.replace(" ", "+");
        String url = "http://192.168.10.34:11000/cgi-bin/speech?voice=salli&lang=en_us&text=" + modifiedTextualMessage;

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(mediaPlayerOnPreparedListener);
        }

        try {
            stopTextToSpeechSDK();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnPreparedListener mediaPlayerOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mp != null) {
                stopTextToSpeechSDK();
                mp.start();
            }
        }
    };

    private void speakViaNative(String textualMessage) {
        stopTextToSpeechNative();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(textualMessage, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(textualMessage, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stopTextToSpeech() {
        if (Constants.ENABLE_SDK) {
            stopTextToSpeechSDK();
        } else {
            stopTextToSpeechNative();
        }
    }

    private void stopTextToSpeechSDK() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void stopTextToSpeechNative() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

}
