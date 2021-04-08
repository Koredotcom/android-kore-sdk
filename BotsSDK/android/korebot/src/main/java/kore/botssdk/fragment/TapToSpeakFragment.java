/*
package kore.botssdk.fragment;

*/
/**
 * Created by AmitYadav on 1/20/2017.
 *//*


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.event.TapToSpeakEventPublisher;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.speechtotext.AudioCue;
import kore.botssdk.speechtotext.AudioDataReceivedListener;
import kore.botssdk.speechtotext.AudioRecorder;
import kore.botssdk.speechtotext.AudioTaskListener;
import kore.botssdk.speechtotext.RawAudioRecorder;
import kore.botssdk.speechtotext.SocketWrapperForTextToSpeech;
import kore.botssdk.speechtotext.SoundPlayCompletionListener;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.view.RippleView;
import kore.botssdk.websocket.SocketConnectionListener;

public class TapToSpeakFragment extends Fragment {
    private String LOG_TAG = TapToSpeakFragment.class.getName();
    private Handler handler = new Handler();
    private RippleView rec_audio;
    private TextView txtSpeakNow;
    private RawAudioRecorder mRecordingThread;
    private AudioCue audioCue;
    private AudioTaskListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tap_to_speak_layout, container, false);
        rec_audio = (RippleView) view.findViewById(R.id.rec_audio);

        txtSpeakNow = (TextView) view.findViewById(R.id.txtSpeakNow);
        txtSpeakNow.setText(R.string.please_wait);

        mRecordingThread = new RawAudioRecorder(audioDataReceivedListener);
        audioCue = new AudioCue(getActivity(), soundListener);
        view.findViewById(R.id.stop_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeWebSocket();
            }
        }, 400);
        return view;
    }

    */
/*private Runnable updateVisualizer = new Runnable() {

        @Override
        public void run() {
            rec_audio.animateRipple();
            handler.postDelayed(this, 600);
        }
    };*//*


    private void initializeWebSocket() {
        if (!NetworkUtility.isNetworkConnectionAvailable(getActivity())) {
            txtSpeakNow.setText(R.string.no_network);
            return;
        }

        if (SocketWrapperForTextToSpeech.getInstance(getActivity()).isConnected()) {
            audioCue.playStartSoundAndSleep();
        } else {
            SocketWrapperForTextToSpeech.getInstance(getActivity()).connect(sListener, SDKConfiguration.Client.identity);
        }
    }

    SoundPlayCompletionListener soundListener = new SoundPlayCompletionListener() {
        @Override
        public void onAudioCompleted(Type soundType) {
            if (soundType == Type.START) {
                recordAudio();
            }
        }
    };

    AudioDataReceivedListener audioDataReceivedListener = new AudioDataReceivedListener() {
        @Override
        public void onAudioDataToWave() {
        }

        @Override
        public void onAudioDataToServer(byte[] data) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    rec_audio.animateRipple();
                }
            });
            SocketWrapperForTextToSpeech.getInstance(getActivity()).sendRawData(data);
        }

        @Override
        public void onRecordingStarted() {
//            handler.post(updateVisualizer);
        }

        @Override
        public void onRecordingStopped() {
            sendEOF();
            audioCue.playStopSound();
            mRecordingThread.stop();
//            handler.removeCallbacks(updateVisualizer);
        }
    };

    SocketConnectionListener sListener = new SocketConnectionListener() {
        @Override
        public void onOpen(boolean isReconnection) {
            Log.d(LOG_TAG, "Connection opened");
            audioCue.playStartSoundAndSleep();
        }

        @Override
        public void onClose(int code, String reason) {
            Log.d(LOG_TAG, "Connection closed reason " + reason);
            mRecordingThread.stop();
            sbf.delete(0, sbf.length());
        }

        @Override
        public void refreshJwtToken() {

        }

        @Override
        public void onTextMessage(String payload) {
            Log.d(LOG_TAG, "Message received is 1 " + payload);
            processAndAppendText(payload);
        }

        @Override
        public void onRawTextMessage(byte[] payload) {
            Log.d(LOG_TAG, "Message received is 2 " + payload);
        }

        @Override
        public void onBinaryMessage(byte[] payload) {
            Log.d(LOG_TAG, "Message received is 3 " + payload);
        }
    };

    private void recordAudio() {
        txtSpeakNow.setText(R.string.speak_now);
        mRecordingThread.startRecording();
    }

    StringBuffer sbf = new StringBuffer();

    private void processAndAppendText(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            if (root != null) {
                JSONObject result = root.getJSONObject("result");
                if (result != null) {
                    boolean isFinal = result.getBoolean("final");
                    JSONObject hypotheses = (JSONObject) result.getJSONArray("hypotheses").get(0);
//                    Log.d(LOG_TAG,"The Dash isss "+hypotheses.getString("original-transcript"));
//                    if(hypotheses.has("transcript")) {
                    if (isFinal) {
                        sbf.append(hypotheses.getString("transcript"));
                        mListener.audioDataToTextView(sbf.toString());
//                        editTextMessage.setText(sbf.toString());
//                        editTextMessage.setSelection(editTextMessage.getText().length());
                    } else {
                        mListener.audioDataToTextView(sbf.toString() + hypotheses.getString("transcript"));
//                        editTextMessage.setText(sbf.toString() + hypotheses.getString("transcript"));
//                        editTextMessage.setSelection(editTextMessage.getText().length());
                    }
//                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BotChatActivity) {
            ((BotChatActivity) getActivity()).ttsOnStop();
        }
    }

    private void closeFragment() {
        sendEOF();
        mRecordingThread.stop();
//        handler.removeCallbacks(updateVisualizer);
        if (mListener != null)
            mListener.onCloseButtonClicked(0);
        TapToSpeakEventPublisher.stop();
    }

    public void clearBuffAndCloseFragment() {
        sbf.delete(0, sbf.length());
        if (!TapToSpeakFragment.this.isDetached() && TapToSpeakFragment.this.getActivity() != null)
            closeFragment();
    }

    public AudioRecorder.State getState() {
        if (mRecordingThread != null)
            return mRecordingThread.getState();
        else return AudioRecorder.State.ERROR;
    }

    private void sendEOF() {
        try {
            byte[] eos = "EOS".getBytes("US-ASCII");
            SocketWrapperForTextToSpeech.getInstance(getActivity()).sendRawData(eos);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public AudioTaskListener getmListener() {
        return mListener;
    }

    public void setmListener(AudioTaskListener mListener) {
        this.mListener = mListener;
    }
}
*/
