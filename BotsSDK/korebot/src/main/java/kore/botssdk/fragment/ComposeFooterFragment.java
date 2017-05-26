package kore.botssdk.fragment;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.event.TapToSpeakEvent;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.speechtotext.AudioRecorder;
import kore.botssdk.speechtotext.AudioTaskListener;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.Utility;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ComposeFooterFragment extends BaseSpiceFragment implements ComposeFooterUpdate {

    String LOG_TAG = ComposeFooterFragment.class.getName();

    protected EditText editTextMessage;
    protected TextView sendButton;
    //    protected RippleView rec_audio;
    protected ImageView rec_audio_img;
    protected ImageView audio_speak_tts;
    //    private RawAudioRecorder mRecordingThread;
//    private ProgressBar loadingTasksProgressBar,progressBarAudio;
    private static final int REQUEST_RECORD_AUDIO = 13;

    boolean isDisabled, isFirstTime, isTTSEnabled = false;
    ComposeFooterInterface composeFooterInterface;
    private String TapToSpeakFragmentTag = "TapToSpeakFragment";
    private TapToSpeakFragment tapToSpeakFragment;
    private TTSUpdate ttsUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bot_footer_fragment, null);
        findViews(view);
        isDisabled = true;
        isFirstTime = true;
        updateUI();
        setListener();
        setListenerExplicitly();
        toggleTTSButton();
        KoreEventCenter.register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        KoreEventCenter.unregister(this);
        super.onDestroy();
    }

    private void findViews(View view) {
        editTextMessage = (EditText) view.findViewById(R.id.edtTxtMessage);
        editTextMessage.addTextChangedListener(composeTextWatcher);
        sendButton = (TextView) view.findViewById(R.id.sendTv);
        rec_audio_img = (ImageView) view.findViewById(R.id.rec_audio_img);
        audio_speak_tts = (ImageView) view.findViewById(R.id.audio_speak_tts);

//        footerDivider = view.findViewById(R.id.footer_divider);
//        tasksRl = (RelativeLayout) view.findViewById(R.id.tasksRl);
//        footer = (RelativeLayout) view.findViewById(R.id.fl_footer);
//        mRecordingThread = new RawAudioRecorder(audioDataReceivedListener);
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

    private void setListenerExplicitly() {
        rec_audio_img.setOnClickListener(onAudioMicClicked);
        audio_speak_tts.setOnClickListener(onTTSEnableSwitchClickListener);
    }

    private void toggleTTSButton() {
        if (isTTSEnabled) {
            audio_speak_tts.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            audio_speak_tts.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    private void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message);
            if (tapToSpeakFragment != null && !tapToSpeakFragment.isDetached()) {
                tapToSpeakFragment.clearBuffAndCloseFragment();
                editTextMessage.setText("");
            }
        } else {
            Log.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setTtsUpdate(TTSUpdate ttsUpdate) {
        this.ttsUpdate = ttsUpdate;
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                sendButton.setVisibility(View.GONE);
                rec_audio_img.setVisibility(View.VISIBLE);
            } else if ((sendButton.getVisibility() != View.VISIBLE && tapToSpeakFragment != null && tapToSpeakFragment.getState() != AudioRecorder.State.RECORDING)
                    || (s.length() > 0 && sendButton.getVisibility() != View.VISIBLE)) {
                sendButton.setVisibility(View.VISIBLE);
                rec_audio_img.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    View.OnClickListener onAudioMicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAudioRecordingSafe();
        }
    };

    View.OnClickListener composeFooterSendBtOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = editTextMessage.getText().toString();
            if (!msg.trim().isEmpty()) {
                editTextMessage.setText("");
                sendMessageText(msg);
            }
        }
    };

    View.OnClickListener onTTSEnableSwitchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isTTSEnabled = !isTTSEnabled;
            toggleTTSButton();
            if (ttsUpdate != null) {
                ttsUpdate.ttsUpdateListener(isTTSEnabled);
            }
        }
    };

    private void stopTTS() {
        if (ttsUpdate != null) {
            ttsUpdate.ttsOnStop();
        }
    }

    public boolean isTTSEnabled() {
        return isTTSEnabled;
    }

    private void stopRecording() {
        if (editTextMessage.getText().toString().trim().length() == 0) {
            rec_audio_img.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.GONE);
        } else {
            rec_audio_img.setVisibility(View.GONE);
            sendButton.setVisibility(View.VISIBLE);
        }
    }

    private void startAudioRecordingSafe() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//                editTextMessage.setHint("Start talking...");
                showTapToSpeakFragment();

            } else {
                requestMicrophonePermission();
            }
        } else {
//            editTextMessage.setHint("Start talking...");
            showTapToSpeakFragment();
        }
    }

    private void requestMicrophonePermission() {
        AppPermissionsHelper.requestForPermission(getActivity(), new String[]{
                android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            rec_audio_img.setImageResource(R.drawable.mic_btn_active);
//            mRecordingThread.startRecording();
//            editTextMessage.setHint("Start talking...");
            showTapToSpeakFragment();
        }
    }

    private void showTapToSpeakFragment() {
        stopTTS();
        Utility.hideVirtualKeyboard(getActivity());
        editTextMessage.setEnabled(false);
        rec_audio_img.setVisibility(View.GONE);

        tapToSpeakFragment = new TapToSpeakFragment();
        tapToSpeakFragment.setmListener(mListener);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        ft.replace(R.id.tap_to_speak, tapToSpeakFragment, TapToSpeakFragmentTag);
        ft.commit();
    }

    public void onEventMainThread(TapToSpeakEvent.Stop event) {


    }

    AudioTaskListener mListener = new AudioTaskListener() {
        @Override
        public void onCloseButtonClicked(int resultCode) {
            editTextMessage.setEnabled(true);
            try {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fm.findFragmentByTag(TapToSpeakFragmentTag));
                ft.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            stopRecording();
        }

        @Override
        public void audioDataToTextView(String text) {
            if (tapToSpeakFragment != null && !tapToSpeakFragment.isDetached() && tapToSpeakFragment.getState() == AudioRecorder.State.RECORDING) {
                editTextMessage.setText(text);
                editTextMessage.setSelection(editTextMessage.getText().length());
            }
        }
    };

}