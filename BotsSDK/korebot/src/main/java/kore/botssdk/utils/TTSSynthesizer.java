package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import kore.botssdk.speechtotext.TtsWebSocketWrapper;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Pradeep Mahato on 19-May-17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class TTSSynthesizer {

    TextToSpeech textToSpeech;

    private final Context context;
    final MediaPlayer mediaPlayer = new MediaPlayer();
    public static final String LOG_TAG = TTSSynthesizer.class.getSimpleName();
    public final ArrayList<String> que = new ArrayList<>();
    public boolean ttsEnabled = true;



    public TTSSynthesizer(Context context) {
        this.context = context;
        if(!Constants.ENABLE_SDK) {
            initNative(context);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initializeWebSocket();
                }
            }, 400);
            mediaPlayer.setOnPreparedListener(mediaPlayerOnPreparedListener);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    que.remove(0);
                    if(que.size() >0) {
                        PlayAudio(que.get(0));
                    }
                }
            });
        }
    }

    public final void initNative(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

    }

    public void speak(String textualMessage,String accessToken) {
        if (Constants.ENABLE_SDK) {
            speakViaSDK(textualMessage,accessToken);
        } else {
            speakViaNative(textualMessage);
        }
    }

    void initializeWebSocket(){
        TtsWebSocketWrapper.getInstance(context).connect(sListener);
    }

    private void speakViaSDK(String textualMessage,String accessToken) {
        TtsWebSocketWrapper.getInstance(context).sendMessage(textualMessage,accessToken);
    }

    final MediaPlayer.OnPreparedListener mediaPlayerOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mp != null) {
                mp.start();
            }
        }
    };

    private void speakViaNative(String textualMessage) {
       // stopTextToSpeechNative();
        textToSpeech.speak(textualMessage, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void stopTextToSpeech() {
        if (Constants.ENABLE_SDK) {
            stopTextToSpeechSDK();
        } else {
            stopTextToSpeechNative();
        }
    }

    private void stopTextToSpeechSDK() {
        que.clear();
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void stopTextToSpeechNative() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
    private final SocketConnectionListener sListener = new SocketConnectionListener() {
        @Override
        public void onOpen(boolean isReconnection) {
            LogUtils.d(LOG_TAG, "Connection opened");

        }

        @Override
        public void onClose(int code, String reason) {
            LogUtils.d(LOG_TAG, "Connection closed reason " + reason);
        }

        @Override
        public void onTextMessage(String payload) {
            LogUtils.d(LOG_TAG, "Message received is 1 " + payload);
        }

        @Override
        public void refreshJwtToken() {

        }

        @Override
        public void onRawTextMessage(byte[] payload) {
            LogUtils.d(LOG_TAG, "Message received is 2 " + Arrays.toString(payload));
        }

        @Override
        public void onBinaryMessage(byte[] payload) {
            LogUtils.d(LOG_TAG, "Message received is 3 " + Arrays.toString(payload));
            String audio = Base64.encodeToString(payload,
                    Base64.NO_WRAP);
            que.add(audio);
            if(!mediaPlayer.isPlaying()  && que.size() <=1) {
                PlayAudio(audio);
            }
        }
    };
    void PlayAudio(String audio){
        try
        {
            String url = "data:audio/mp3;base64,"+audio;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        }
        catch(Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public boolean isTtsEnabled() {
        return ttsEnabled;
    }

    public void setTtsEnabled(boolean ttsEnabled) {
        this.ttsEnabled = ttsEnabled;
        if(!ttsEnabled){
            stopTextToSpeech();
        }
    }
}
