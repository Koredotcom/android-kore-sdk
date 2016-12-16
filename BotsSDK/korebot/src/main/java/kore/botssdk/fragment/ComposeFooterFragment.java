package kore.botssdk.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import kore.botssdk.R;
import kore.botssdk.autobahn.WebSocket;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.speechtotext.AudioDataReceivedListener;
import kore.botssdk.speechtotext.RecordingThread;
import kore.botssdk.speechtotext.SocketWrapperForTextToSpeech;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.CustomToast;
import kore.botssdk.utils.NetworkUtility;
import kore.botssdk.view.RippleView;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Pradeep Mahato on 31-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ComposeFooterFragment extends BaseSpiceFragment implements ComposeFooterUpdate {

    String LOG_TAG = ComposeFooterFragment.class.getName();

    protected EditText editTextMessage;
    protected TextView sendButton;
    protected RippleView rec_audio;
    protected ImageView rec_audio_img;
    private RecordingThread mRecordingThread;
    private ProgressBar loadingTasksProgressBar,progressBarAudio;
    private static final int REQUEST_RECORD_AUDIO = 13;

    boolean isDisabled, isFirstTime;
    ComposeFooterInterface composeFooterInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_footer_fragment, null);

        findViews(view);

        isDisabled = true;
        isFirstTime = true;
        updateUI();
        setListener();

        return view;
    }

    private void findViews(View view) {
        editTextMessage = (EditText) view.findViewById(R.id.edtTxtMessage);
        sendButton = (TextView) view.findViewById(R.id.sendTv);
        editTextMessage.addTextChangedListener(composeTextWatcher);
        sendButton = (TextView) view.findViewById(R.id.sendTv);
        rec_audio = (RippleView) view.findViewById(R.id.rec_audio);
        rec_audio_img = (ImageView) view.findViewById(R.id.rec_audio_img);
        progressBarAudio = (ProgressBar) view.findViewById(R.id.progressBarAudio);
        rec_audio.setOnClickListener(onAudioMicClicked);
//        footerDivider = view.findViewById(R.id.footer_divider);
//        tasksRl = (RelativeLayout) view.findViewById(R.id.tasksRl);
//        footer = (RelativeLayout) view.findViewById(R.id.fl_footer);
        mRecordingThread = new RecordingThread(audioDataReceivedListener);
    }

    private void updateUI() {
        sendButton.setEnabled(!isDisabled && !isFirstTime);
    }

    private void setListener() {
        if (isDisabled && isFirstTime) {
            sendButton.setOnClickListener(null);
        } else {
            sendButton.setOnClickListener(composeFooterSendBtOnClickListener);
        }
    }

    View.OnClickListener composeFooterSendBtOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = editTextMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                editTextMessage.setText("");
                sendMessageText(msg);
            }
        }
    };

    private void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message);
        } else {
            Log.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public void enableSendButton() {
        isDisabled = false;
        isFirstTime = false;

        updateUI();
        setListener();
    }

    public interface ComposeFooterInterface {
        void onSendClick(String message);
    }

    TextWatcher composeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                sendButton.setVisibility(View.GONE);
                rec_audio.setVisibility(View.VISIBLE);
            } else if (sendButton.getVisibility() != View.VISIBLE && !mRecordingThread.recording()) {
                sendButton.setVisibility(View.VISIBLE);
                rec_audio.setVisibility(View.GONE);
            }else if(mRecordingThread.recording()){
//                rec_audio.setImageResource(R.drawable.mic_btn_done);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };

    boolean isRecording;
    // updates the visualizer every 50 milliseconds
    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                rec_audio.animateRipple();
                // update in 40 milliseconds
                handler.postDelayed(this, 600);
            }
        }
    };

    Handler handler = new Handler();

    View.OnClickListener onAudioMicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!NetworkUtility.isNetworkConnectionAvailable(getActivity())){
                CustomToast.showToast(getActivity(),"Network not available");
                return;
            }
            if(!mRecordingThread.recording()) {
                progressBarAudio.setVisibility(View.VISIBLE);
                if (SocketWrapperForTextToSpeech.getInstance(getActivity()).isConnected()) {
                    startAudioRecordingSafe();
                } else {
                    SocketWrapperForTextToSpeech.getInstance(getActivity()).connect(sListener,"ramachandra.pradeep.challa@kore.com");
                }
            }else {
//                rec_audio_img.setImageResource(R.drawable.mic_btn);
                rec_audio.setVisibility(View.GONE);
                sendButton.setVisibility(View.VISIBLE);
                mRecordingThread.stopRecording();
                try {
                    byte[] eos = "EOS".getBytes("US-ASCII");
                    SocketWrapperForTextToSpeech.getInstance(getActivity()).sendRawData(eos);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    AudioDataReceivedListener audioDataReceivedListener = new AudioDataReceivedListener() {
        @Override
        public void onAudioDataToWave() {
        }

        @Override
        public void onAudioDataToServer(byte[] data) {
            SocketWrapperForTextToSpeech.getInstance(getActivity()).sendRawData(data);
        }

        @Override
        public void onRecordingStarted() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressBarAudio.setVisibility(View.GONE);
                }
            });
            isRecording = true;
            handler.post(updateVisualizer);

        }

        @Override
        public void onRecordingStopped() {
            isRecording = false;
            handler.removeCallbacks(updateVisualizer);
        }
    };

    SocketConnectionListener sListener = new SocketConnectionListener() {
        @Override
        public void onOpen() {
            Log.d(LOG_TAG, "Connection opened");
            startAudioRecordingSafe();
        }
        @Override
        public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
            Log.d(LOG_TAG, "Connection closed reason " + reason);
            rec_audio_img.setImageResource(R.drawable.mic_btn);
            mRecordingThread.stopRecording();
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

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) {
            rec_audio_img.setImageResource(R.drawable.mic_btn_active);
            mRecordingThread.startRecording();
            editTextMessage.setHint("Start talking...");

        } else {
            progressBarAudio.setVisibility(View.GONE);
            requestMicrophonePermission();
        }
    }

    private void requestMicrophonePermission() {
        if (AppPermissionsHelper.shouldShowRationale(getActivity(), android.Manifest.permission.RECORD_AUDIO)) {
            // Show dialog explaining why we need record audio
            CustomToast.showToast(getActivity(), "Microphone access is required in order to record audio");

        } else {
            AppPermissionsHelper.requestForPermission(getActivity(), new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            rec_audio_img.setImageResource(R.drawable.mic_btn_active);
            mRecordingThread.stopRecording();
        }
    }

    StringBuffer sbf = new StringBuffer();
    private void processAndAppendText(String jsonString){
        try {
            JSONObject root = new JSONObject(jsonString);
            if(root != null){
                JSONObject result = root.getJSONObject("result");
                if(result != null){
                    boolean isFinal = result.getBoolean("final");
                    JSONObject hypotheses = (JSONObject) result.getJSONArray("hypotheses").get(0);
                    if(isFinal) {
                        sbf.append(hypotheses.getString("original-transcript") + " ");
                        editTextMessage.setText(sbf.toString());
                        editTextMessage.setSelection(editTextMessage.getText().length());
                    }else{
                        editTextMessage.setText(sbf.toString() + hypotheses.getString("transcript") + " ");
                        editTextMessage.setSelection(editTextMessage.getText().length());
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
