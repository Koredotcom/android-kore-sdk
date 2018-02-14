package kore.botssdk.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.event.TapToSpeakEvent;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.FormActionTemplate;
import kore.botssdk.speechtotext.AudioRecorder;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.Utility;

import static android.app.Activity.RESULT_OK;
import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS;
import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ComposeFooterFragment extends BaseSpiceFragment implements ComposeFooterUpdate, SpeechDelegate {

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    String LOG_TAG = ComposeFooterFragment.class.getName();

    protected EditText editTextMessage;
    protected TextView sendButton;
    protected TextView speakerText;
    protected LinearLayout mainContentLayout;
    protected LinearLayout defaultFooterLayout;
    //    protected RippleView rec_audio;
    protected ImageView rec_audio_img;
    protected ImageView audio_speak_tts;
    protected  ImageView keyboard_img;
    private SpeechProgressView progress;
    private TextView text_view_speech;
    //    private RawAudioRecorder mRecordingThread;
//    private ProgressBar loadingTasksProgressBar,progressBarAudio;
    private static final int REQUEST_RECORD_AUDIO = 13;

    boolean isDisabled, isFirstTime, isTTSEnabled = true;
    ComposeFooterInterface composeFooterInterface;
    private String TapToSpeakFragmentTag = "TapToSpeakFragment";
    private TapToSpeakFragment tapToSpeakFragment;
    private TTSUpdate ttsUpdate;
    private LinearLayout linearLayoutProgress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Speech.init(getContext(), getContext().getPackageName());
    }

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
        Speech.getInstance().shutdown();
        super.onDestroy();
    }

    private void findViews(View view) {
        mainContentLayout = (LinearLayout)view.findViewById(R.id.mainContent);
        defaultFooterLayout = (LinearLayout) view.findViewById(R.id.default_footer);
        editTextMessage = (EditText) view.findViewById(R.id.edtTxtMessage);
        editTextMessage.addTextChangedListener(composeTextWatcher);
        sendButton = (TextView) view.findViewById(R.id.sendTv);
        speakerText = (TextView)view.findViewById(R.id.speaker_text);
        rec_audio_img = (ImageView) view.findViewById(R.id.rec_audio_img);
        keyboard_img = (ImageView)view.findViewById(R.id.keyboard_image);
        audio_speak_tts = (ImageView) view.findViewById(R.id.audio_speak_tts);
        linearLayoutProgress = (LinearLayout) view.findViewById(R.id.linearLayoutProgress);

        progress = (SpeechProgressView) view.findViewById(R.id.progress);
        int[] colors = {
                ContextCompat.getColor(getContext(), android.R.color.black),
                ContextCompat.getColor(getContext(), android.R.color.darker_gray),
                ContextCompat.getColor(getContext(), android.R.color.black),
                ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark),
                ContextCompat.getColor(getContext(), android.R.color.holo_red_dark)
        };
        progress.setColors(colors);

        text_view_speech = (TextView) view.findViewById(R.id.text_view_speech);

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
        keyboard_img.setOnClickListener(keyboardIconClickListener);
        speakerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
        audio_speak_tts.setOnClickListener(onTTSEnableSwitchClickListener);
        rec_audio_img.setOnClickListener(onVoiceModeActivated);

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
            composeFooterInterface.onSendClick(message.trim());
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
        if (ttsUpdate != null) {
            ttsUpdate.ttsUpdateListener(isTTSEnabled);
        }
    }

    @Override
    public void enableSendButton() {
        isDisabled = false;
        isFirstTime = false;

        updateUI();
        setListener();
    }

    public interface ComposeFooterInterface {
        /**
         * @param message : Title and payload, Both are same
         */
        void onSendClick(String message);

        /**
         * @param message : Title of the button
         * @param payload : Payload to be send
         */
        void onSendClick(String message, String payload);

        void onFormActionButtonClicked(FormActionTemplate fTemplate);
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

    /*View.OnClickListener onAudioMicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAudioRecordingSafe();
        }
    };*/

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
    View.OnClickListener keyboardIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                animateLayoutVisible(mainContentLayout);

                animateLayoutGone(defaultFooterLayout);
                editTextMessage.requestFocus();
                Utility.showVirtualKeyboard(getActivity(),editTextMessage);
        }
    };

    View.OnClickListener onVoiceModeActivated = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainContentLayout.setVisibility(View.GONE);
            animateLayoutGone(mainContentLayout);
            animateLayoutVisible(defaultFooterLayout);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onButtonClick();
                }
            },500);
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

    /*private void startAudioRecordingSafe() {
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
    }*/

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
            onRecordAudioPermissionGranted();
        }
    }

    private void showTapToSpeakFragment() {
        stopTTS();
        editTextMessage.setEnabled(false);
        animateLayoutVisible(mainContentLayout);
        animateLayoutGone(defaultFooterLayout);

        promptSpeechInput();
        Utility.showVirtualKeyboard(getActivity(),mainContentLayout);
   /*     rec_audio_img.setVisibility(View.GONE);

        tapToSpeakFragment = new TapToSpeakFragment();
        tapToSpeakFragment.setmListener(mListener);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        ft.replace(R.id.tap_to_speak, tapToSpeakFragment, TapToSpeakFragmentTag);
        ft.commit();*/
    }


    void animateLayoutVisible(View view){
        view.setVisibility(View.VISIBLE);
        view.animate().translationY(0).alpha(1.0f).setDuration(500).setListener(null);
    }
    void animateLayoutGone(final View view){
        view.animate().translationY(view.getHeight()).alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,60);
        intent.putExtra(EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,10);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
               "Say something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                   "Speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextMessage.setText(result.get(0));
                    editTextMessage.setEnabled(true);
                }else{
                    editTextMessage.setEnabled(true);
                }
                break;
            }

        }
    }
    public void onEventMainThread(TapToSpeakEvent.Stop event) {


    }
    private void onButtonClick() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//                editTextMessage.setHint("Start talking...");
                    onRecordAudioPermissionGranted();

                } else {
                    requestMicrophonePermission();
                }
            } else {
//            editTextMessage.setHint("Start talking...");
//                showTapToSpeakFragment();
                onRecordAudioPermissionGranted();
            }
            /*RxPermissions.getInstance(this)
                    .request(Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            onRecordAudioPermissionGranted();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.permission_required, Toast.LENGTH_LONG);
                        }
                    });*/
        }
    }
    private void onRecordAudioPermissionGranted() {
        stopTTS();
        Utility.hideVirtualKeyboard(getActivity());
        /*editTextMessage.setEnabled(false);
        animateLayoutVisible(mainContentLayout);
        animateLayoutGone(defaultFooterLayout);*/
        speakerText.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);
        text_view_speech.setVisibility(View.VISIBLE);
        text_view_speech.setText("");

        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, ComposeFooterFragment.this);

        } catch (SpeechRecognitionNotAvailable exc) {
            showSpeechNotSupportedDialog();

        } catch (GoogleVoiceTypingDisabledException exc) {
            showEnableGoogleVoiceTyping();
        }
    }
    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {

        text_view_speech.setText(results.toString());
        for (String partial : results) {
            text_view_speech.append(partial + " ");
        }
    }

    @Override
    public void onSpeechResult(String result) {
        speakerText.setVisibility(View.VISIBLE);
        linearLayoutProgress.setVisibility(View.GONE);
        text_view_speech.setText(result);

        if (result.isEmpty()) {
            Speech.getInstance().say(getString(R.string.repeat));

        }else{
            if (composeFooterInterface != null) {
                composeFooterInterface.onSendClick(result);
                editTextMessage.setText("");
            } else {
                Log.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
            }
            /*else {
            Speech.getInstance().say(result);
        }*/
        }
        text_view_speech.setVisibility(View.GONE);

    }
    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(getActivity());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.speech_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.enable_google_voice_typing)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .show();
    }


    /*AudioTaskListener mListener = new AudioTaskListener() {
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
    };*/

}