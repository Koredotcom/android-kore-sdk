package kore.botssdk.common.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Base64
import android.util.Log
import kore.botssdk.speechtotext.TtsWebSocketWrapper
import kore.botssdk.utils.Constants
import kore.botssdk.websocket.SocketConnectionListener
import java.util.Locale

class TTSSynthesizer(private val context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    var que = ArrayList<String>()
    private var isTtsEnabled = true
    private fun initNative(context: Context?): TextToSpeech {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
            }
        }
        return textToSpeech!!
    }

    fun speak(textualMessage: String, accessToken: String) {
        if (Constants.ENABLE_SDK) {
            speakViaSDK(textualMessage, accessToken)
        } else {
            speakViaNative(textualMessage)
        }
    }

    private fun initializeWebSocket() {
        TtsWebSocketWrapper.getInstance(context).connect(sListener)
    }

    private fun speakViaSDK(textualMessage: String, accessToken: String) {
        TtsWebSocketWrapper.getInstance(context).sendMessage(textualMessage, accessToken)
    }

    var mediaPlayerOnPreparedListener = MediaPlayer.OnPreparedListener { mp ->
        mp?.start()
    }

    private fun speakViaNative(textualMessage: String) {
        // stopTextToSpeechNative();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech?.speak(textualMessage, TextToSpeech.QUEUE_ADD, null, null)
        } else {
            textToSpeech?.speak(textualMessage, TextToSpeech.QUEUE_ADD, null)
        }
    }

    fun stopTextToSpeech() {
        if (Constants.ENABLE_SDK) {
            stopTextToSpeechSDK()
        } else {
            stopTextToSpeechNative()
        }
    }

    private fun stopTextToSpeechSDK() {
        que.clear()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    private fun stopTextToSpeechNative() {
        textToSpeech?.stop()
    }

    private val sListener: SocketConnectionListener = object : SocketConnectionListener {
        override fun onOpen(isReconnection: Boolean) {
            Log.d(LOG_TAG, "Connection opened")
        }

        override fun onClose(code: Int, reason: String) {
            Log.d(LOG_TAG, "Connection closed reason $reason")
        }

        override fun onTextMessage(payload: String) {
            Log.d(LOG_TAG, "Message received is 1 $payload")
        }

        override fun refreshJwtToken() {}
        override fun onRawTextMessage(payload: ByteArray) {
            Log.d(LOG_TAG, "Message received is 2 $payload")
        }

        override fun onBinaryMessage(payload: ByteArray) {
            Log.d(LOG_TAG, "Message received is 3 $payload")
            val audio = Base64.encodeToString(payload, Base64.NO_WRAP)
            que.add(audio)
            if (!mediaPlayer.isPlaying && que.size <= 1) {
                PlayAudio(audio)
            }
        }
    }

    init {
        if (!Constants.ENABLE_SDK) {
            initNative(context)
        } else {
            Handler().postDelayed({ initializeWebSocket() }, 400)
            mediaPlayer.setOnPreparedListener(mediaPlayerOnPreparedListener)
            mediaPlayer.setOnCompletionListener {
                que.removeAt(0)
                if (que.size > 0) {
                    PlayAudio(que[0])
                }
            }
        }
    }

    private fun PlayAudio(audio: String) {
        try {
            val url = "data:audio/mp3;base64,$audio"
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
        } catch (ex: Exception) {
            print(ex.message)
        }
    }

    fun isTtsEnabled(): Boolean {
        return isTtsEnabled
    }

    fun setTtsEnabled(ttsEnabled: Boolean) {
        this.isTtsEnabled = ttsEnabled
        if (!ttsEnabled) {
            stopTextToSpeech()
        }
    }

    companion object {
        var LOG_TAG: String = TTSSynthesizer::class.java.simpleName
    }
}