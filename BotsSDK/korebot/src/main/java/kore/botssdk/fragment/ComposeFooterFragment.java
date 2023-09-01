package kore.botssdk.fragment;

import static android.app.Activity.RESULT_OK;
import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS;
import static android.speech.RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static kore.botssdk.activity.KaCaptureImageActivity.THUMBNAIL_FILE_PATH;
import static kore.botssdk.utils.BitmapUtils.getBufferSize;
import static kore.botssdk.utils.BitmapUtils.rotateIfNecessary;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.view.viewUtils.FileUtils.EXT_JPG;
import static kore.botssdk.view.viewUtils.FileUtils.EXT_PNG;
import static kore.botssdk.view.viewUtils.FileUtils.EXT_VIDEO;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.activity.KaCaptureImageActivity;
import kore.botssdk.adapter.AttachmentOptionsAdapter;
import kore.botssdk.adapter.ComposebarAttachmentAdapter;
import kore.botssdk.dialogs.OptionsActionSheetFragment;
import kore.botssdk.dialogs.ReUsableListViewActionSheet;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.fileupload.core.KoreWorker;
import kore.botssdk.fileupload.core.UploadBulkFile;
import kore.botssdk.listener.AttachmentListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.FreemiumData;
import kore.botssdk.models.FreemiumType;
import kore.botssdk.models.KoreComponentModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.models.limits.Attachment;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.SharedPreferenceUtils;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewUtils.FileUtils;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class ComposeFooterFragment extends Fragment implements ComposeFooterUpdate, SpeechDelegate {

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    final String LOG_TAG = ComposeFooterFragment.class.getName();

    protected EditText editTextMessage;
    protected TextView sendButton;
    protected TextView speakerText;
    protected LinearLayout mainContentLayout;
    protected LinearLayout defaultFooterLayout;
    protected ImageView rec_audio_img;
    protected ImageView audio_speak_tts;
    protected  ImageView keyboard_img;
    protected ImageView newMenuLogo;
    private SpeechProgressView progress;
    private TextView text_view_speech;
    private static final int REQUEST_RECORD_AUDIO = 13;

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    boolean isDisabled, isFirstTime, isTTSEnabled = true;
    ComposeFooterInterface composeFooterInterface;
    private final String TapToSpeakFragmentTag = "TapToSpeakFragment";
//    private TapToSpeakFragment tapToSpeakFragment;
    private TTSUpdate ttsUpdate;
    private LinearLayout linearLayoutProgress;
    private BotOptionsModel botOptionsModel;
    protected ReUsableListViewActionSheet listViewActionSheet;
    private AttachmentOptionsAdapter adapter;
    static final int REQ_IMAGE = 444;
    static final int REQ_CAMERA = 443;
    static final int REQ_VIDEO = 445;
    static final int REQ_VIDEO_CAPTURE = 446;
    static final int REQ_FILE = 448;
    private String mCurrentPhotoPath;
    private Uri cameraVideoUri1;
    private ImageView attachemnt;
    private ComposebarAttachmentAdapter composebarAttachmentAdapter;
    RecyclerView attachment_recycler;
    private Attachment attachment;
    private static long totalFileSize;
    private final int compressQualityInt = 100;
    private String jwt;

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
        initialSetUp();
        keyboard_img.performClick();
        return view;
    }

    @Override
    public void onDestroy() {
        KoreEventCenter.unregister(this);
        Speech.getInstance().shutdown();
        super.onDestroy();
    }

    private void findViews(View view) {
        mainContentLayout = view.findViewById(R.id.mainContent);
        defaultFooterLayout = view.findViewById(R.id.default_footer);
        editTextMessage = view.findViewById(R.id.edtTxtMessage);
        editTextMessage.addTextChangedListener(composeTextWatcher);
        sendButton = view.findViewById(R.id.sendTv);
        speakerText = view.findViewById(R.id.speaker_text);
        rec_audio_img = view.findViewById(R.id.rec_audio_img);
        keyboard_img = view.findViewById(R.id.keyboard_image);
        audio_speak_tts = view.findViewById(R.id.audio_speak_tts);
        linearLayoutProgress = view.findViewById(R.id.linearLayoutProgress);
        newMenuLogo = view.findViewById(R.id.newMenuLogo);
        progress = view.findViewById(R.id.progress);
        attachemnt= view.findViewById(R.id.attachemnt);
        attachment_recycler=view.findViewById(R.id.attachment_recycler);
        attachment_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        int[] colors = {
                getContext().getResources().getColor(android.R.color.black),
                getContext().getResources().getColor(android.R.color.darker_gray),
                getContext().getResources().getColor(android.R.color.black),
                getContext().getResources().getColor(android.R.color.holo_orange_dark),
                getContext().getResources().getColor(android.R.color.holo_red_dark)
        };
        progress.setColors(colors);

        text_view_speech = view.findViewById(R.id.text_view_speech);
        if(composebarAttachmentAdapter==null)
        {   composebarAttachmentAdapter=new ComposebarAttachmentAdapter(getActivity(), new AttachmentListner() {
            @Override
            public void onRemoveAttachment() {

                enableOrDisableSendButton(composebarAttachmentAdapter.getItemCount() > 0 || !TextUtils.isEmpty(editTextMessage.getText().toString().trim()));
            }
        });
            attachment_recycler.setAdapter(composebarAttachmentAdapter);
        }
        attachemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(composebarAttachmentAdapter.getItemCount()<1) {
                    showAttachmentActionSheet();
                }else
                {
                    ToastUtils.showToast(getActivity(),"You can upload only one file");
                }
            }
        });

//        footerDivider = view.findViewById(R.id.footer_divider);
//        tasksRl = (RelativeLayout) view.findViewById(R.id.tasksRl);
//        footer = (RelativeLayout) view.findViewById(R.id.fl_footer);
//        mRecordingThread = new RawAudioRecorder(audioDataReceivedListener);
    }

    public void updateUI() {
        sendButton.setEnabled(!isDisabled && !isFirstTime);
        sendButton.setAlpha(sendButton.isEnabled() ? 1.0f : 0.5f);
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
        newMenuLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(botOptionsModel != null && botOptionsModel.getTasks() != null && botOptionsModel.getTasks().size() > 0)
                {
                    OptionsActionSheetFragment bottomSheetDialog = new OptionsActionSheetFragment();
                    bottomSheetDialog.setisFromFullView(false);
                    bottomSheetDialog.setSkillName("skillName","trigger");
                    bottomSheetDialog.setData(botOptionsModel);
                    bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                    bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
                }
            }
        });
    }

    private void toggleTTSButton() {
        if (isTTSEnabled) {
            audio_speak_tts.setImageResource(R.mipmap.ic_volume_up_black_24dp);
        } else {
            audio_speak_tts.setImageResource(R.mipmap.ic_volume_off_black_24dp);
        }
    }

    private void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(),false);
           /* if (tapToSpeakFragment != null && !tapToSpeakFragment.isDetached()) {
                tapToSpeakFragment.clearBuffAndCloseFragment();
                editTextMessage.setText("");
            }*/
        } else {
            LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    private void sendMessageAttachmentText(String message, ArrayList<HashMap<String, String>> dataList)
    {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(),dataList, false);
        } else {
            LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setBottomOptionData(BotOptionsModel botOptionsModel) {
        this.botOptionsModel = botOptionsModel;
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



    final TextWatcher composeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                sendButton.setVisibility(View.GONE);
                rec_audio_img.setVisibility(View.VISIBLE);
            } else if ((sendButton.getVisibility() != View.VISIBLE )
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

    final View.OnClickListener composeFooterSendBtOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = editTextMessage.getText().toString();
            if (!msg.trim().isEmpty())
            {
                if(composebarAttachmentAdapter.getItemCount()>0)
                {
                    if(composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".png")
                        || composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".jpg")
                        || composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".jpeg"))
                        sendMessageAttachmentText(msg+"\n"+getActivity().getResources().getString(R.string.camera)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"),composebarAttachmentAdapter.getData());
                    else if(composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".mp4"))
                        sendMessageAttachmentText(msg+"\n"+getActivity().getResources().getString(R.string.video)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"),composebarAttachmentAdapter.getData());
                    else if(composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".mp3")
                            || composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".m4a"))
                        sendMessageAttachmentText(msg+"\n"+getActivity().getResources().getString(R.string.audio)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"),composebarAttachmentAdapter.getData());
                    else
                        sendMessageAttachmentText(msg+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"),composebarAttachmentAdapter.getData());

                }
                else
                    sendMessageText(msg);

                editTextMessage.setText("");
                composebarAttachmentAdapter.clearAll();
                enableOrDisableSendButton(false);
            }
            else if(composebarAttachmentAdapter.getItemCount()>0)
            {
                if(composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".png")
                        || composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".jpg")
                        || composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".jpeg"))
                    sendMessageAttachmentText(getActivity().getResources().getString(R.string.camera)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                else if(composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".mp4"))
                    sendMessageAttachmentText(getActivity().getResources().getString(R.string.video)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                else if(composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".mp3")
                        || composebarAttachmentAdapter.getData().get(0).get("fileName").contains(".m4a"))
                    sendMessageAttachmentText(getActivity().getResources().getString(R.string.audio)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                else
                    sendMessageAttachmentText(getActivity().getResources().getString(R.string.attachment)+" "+composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());

                composebarAttachmentAdapter.clearAll();
                enableOrDisableSendButton(false);
            }
        }
    };
    final View.OnClickListener keyboardIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                animateLayoutVisible(mainContentLayout);
                animateLayoutGone(defaultFooterLayout);
        }
    };

    final View.OnClickListener onVoiceModeActivated = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainContentLayout.setVisibility(View.GONE);
            animateLayoutGone(mainContentLayout);
//            animateLayoutGone(newMenuLogo);
            animateLayoutVisible(defaultFooterLayout);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onButtonClick();
                }
            },500);
        }
    };

    final View.OnClickListener onTTSEnableSwitchClickListener = new View.OnClickListener() {
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

    public void initialSetUp()
    {
        mainContentLayout.setVisibility(View.GONE);
        animateLayoutGone(mainContentLayout);
//        animateLayoutGone(newMenuLogo);
        animateLayoutVisible(defaultFooterLayout);
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
        AppPermissionsHelper.requestForPermission(requireActivity(), new String[]{
                Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    private void onButtonClick() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_GRANTED) {
                onRecordAudioPermissionGranted();
            } else {
                requestMicrophonePermission();
            }
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
                composeFooterInterface.onSendClick(result,false);
                editTextMessage.setText("");
            } else {
                LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
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

    public void setComposeText(String text)
    {
        editTextMessage.setText(text);
        editTextMessage.setSelection(text.length());
    }

    void showAttachmentActionSheet() {
        if (listViewActionSheet == null) {
            listViewActionSheet = new ReUsableListViewActionSheet(getActivity());
            listViewActionSheet.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams wlp = listViewActionSheet.getWindow().getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            // attachmentOptions.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            listViewActionSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        listViewActionSheet.show();
        if (adapter == null) {
            ArrayList<String> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.attachments_strings)));
            adapter = new AttachmentOptionsAdapter(getActivity(), options);
            listViewActionSheet.setAdapter(adapter);
            listViewActionSheet.getOptionsListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    launchSelectedMode(position);
                }
            });
        }
    }

    private void launchSelectedMode(int position) {
        switch (position) {
            case 4:
                fileBrowsingActivity(BundleConstants.CHOOSE_TYPE_CAMERA, REQ_CAMERA, BundleConstants.MEDIA_TYPE_IMAGE);
                break;
            case 0:
                fileBrowsingActivity(BundleConstants.CHOOSE_TYPE_GALLERY, REQ_IMAGE, BundleConstants.MEDIA_TYPE_IMAGE);
                break;
            case 3:
                launchVideoRecorder();
                break;
            case 1:
                fileBrowsingActivity(BundleConstants.CHOOSE_TYPE_VIDEO_GALLERY, REQ_VIDEO, BundleConstants.MEDIA_TYPE_VIDEO);
                break;
            case 2:
                fileBrowsingActivity(BundleConstants.CHOOSE_TYPE_FILE, REQ_FILE, BundleConstants.MEDIA_TYPE_DOCUMENT);
                break;


        }
        listViewActionSheet.dismiss();
    }

    private void launchVideoRecorder() {
        if (KaPermissionsHelper.hasPermission(getActivity(), Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
            Intent profilePicEditIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            profilePicEditIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

            try {
                Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                        requireActivity().getPackageName() + ".provider",
                        Objects.requireNonNull(createVideoFile()));
                profilePicEditIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //profilePicEditIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
            profilePicEditIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            profilePicEditIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startActivityForResult(profilePicEditIntent, REQ_VIDEO_CAPTURE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                KaPermissionsHelper.requestForPermission(getActivity(), CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST, Manifest.permission.CAMERA,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            }
            else
            {
                KaPermissionsHelper.requestForPermission(getActivity(), CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST, Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private Uri getImageUri() {
        // Store image in Kore folder
        KaMediaUtils.updateExternalStorageState();
//        KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_VIDEO, userData.getId());

        cameraVideoUri1 = null;
        try {
            File actualImageFile = KaMediaUtils.getOutputMediaFile(BundleConstants.MEDIA_TYPE_VIDEO, null);
            Uri uri;
            cameraVideoUri1 = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", actualImageFile);

            LogUtils.d(LOG_TAG, "actual file image path" + actualImageFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cameraVideoUri1;
    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VIDEO_" + timeStamp;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtils.e("Camera", "Oops! Failed create "
                        + "Camera" + " directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + videoFileName + "." + "mp4");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = mediaFile.getAbsolutePath();
        return mediaFile;
    }

    public void fileBrowsingActivity(String choosetype, int reqCode, String mediaType) {
        Intent photoPickerIntent = new Intent(getActivity(), KaCaptureImageActivity.class);
        photoPickerIntent.putExtra("pickType", choosetype);
        photoPickerIntent.putExtra("fileContext", BundleConstants.FOR_MESSAGE);
        photoPickerIntent.putExtra("mediaType", mediaType);

        if (reqCode == REQ_VIDEO) {
            activityVideoResultLaunch.launch(photoPickerIntent);
        } else {
            activityImageResultLaunch.launch(photoPickerIntent);
        }
    }

    final ActivityResultLauncher<Intent> activityImageResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String filePath = result.getData().getStringExtra("filePath");
                        String fileName = result.getData().getStringExtra("fileName");
                        String filePathThumbnail = result.getData().getStringExtra(THUMBNAIL_FILE_PATH);
                        ((BotChatActivity) requireActivity()).sendImage(filePath, fileName, filePathThumbnail);
                    }
                }
            });

    final ActivityResultLauncher<Intent> activityVideoResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String fileExtn = result.getData().getStringExtra("fileExtn");
                        if (fileExtn != null && fileExtn.equals(EXT_VIDEO) && result.getData().getParcelableExtra("fileUri") != null) {
                            processVideoResponse(result.getData().getParcelableExtra("fileUri"), false, result.getData());
                        } else if (fileExtn != null && (fileExtn.equalsIgnoreCase(EXT_JPG) || fileExtn.equalsIgnoreCase(EXT_PNG))) {
                            processImageResponse(result.getData());
                        } else {
                            String filePath = result.getData().getStringExtra("filePath");
                            if (filePath == null) {
                                ToastUtils.showToast(getActivity(), "Unable to attach file!");
                                return;
                            }
                            String fileName = result.getData().getStringExtra("fileName");
                            String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                            String mediaType = BitmapUtils.obtainMediaTypeOfExtn(extn);
                            processFileUpload(fileName, filePath, extn, mediaType, null, null);
                        }
                    }
                }
            });

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        if ((reqCode == REQ_CAMERA || reqCode == REQ_IMAGE) && resCode == RESULT_OK) {
//            processImageResponse(data);
            String filePath = data.getStringExtra("filePath");
            String fileName = data.getStringExtra("fileName");
            String filePathThumbnail = data.getStringExtra(THUMBNAIL_FILE_PATH);
            ((BotChatActivity) requireActivity()).sendImage(filePath, fileName, filePathThumbnail);
          /*  String filePath = data.getStringExtra("filePath");
            String fileName = data.getStringExtra("fileName");
            String filePathThumbnail = data.getStringExtra(THUMBNAIL_FILE_PATH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                  new AbstractAddInformation.SaveCapturedImageTask(filePath, fileName, filePathThumbnail).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new AbstractAddInformation.SaveCapturedImageTask(filePath, fileName, filePathThumbnail).execute();
            }*/
        } else if (reqCode == REQ_VIDEO && resCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                cameraVideoUri1 = extras.getParcelable("fileUri");
                processVideoResponse(cameraVideoUri1, false, data);
            }

        } else if (reqCode == REQ_VIDEO_CAPTURE && resCode == RESULT_OK) {
            try {
                // ensuring a 100 millisecond delay so that thumbnail image name is different than the camera captured image name
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogUtils.e(LOG_TAG, "onActivityResult() - Excep: = " + e.getMessage());
            }
            String path = mCurrentPhotoPath;
            cameraVideoUri1 = Uri.parse(path);
            processVideoResponse(cameraVideoUri1, true, data);
        } else if (reqCode == REQ_FILE && resCode == RESULT_OK) {
            String fileExtn = data.getStringExtra("fileExtn");
            if (fileExtn != null && fileExtn.equals(EXT_VIDEO) && data.getParcelableExtra("fileUri") != null) {
                processVideoResponse(data.getParcelableExtra("fileUri"), false, data);
            } else if (fileExtn != null && (fileExtn.equalsIgnoreCase(EXT_JPG) || fileExtn.equalsIgnoreCase(EXT_PNG))) {
                processImageResponse(data);
            } else {
                String filePath = data.getStringExtra("filePath");
                if (filePath == null) {
                    ToastUtils.showToast(getActivity(), "Unable to attach file!");
                    return;
                }
                String fileName = data.getStringExtra("fileName");
                String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                String mediaType = BitmapUtils.obtainMediaTypeOfExtn(extn);
                processFileUpload(fileName, filePath, extn, mediaType, null, null);
            }
        }
        else if(reqCode == REQ_CODE_SPEECH_INPUT)
        {
            if (resCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                editTextMessage.setText(result.get(0));
                editTextMessage.setEnabled(true);
            }else{
                editTextMessage.setEnabled(true);
            }
        }
    }

    void processFileUpload(String fileName, String filePath, String extn, String mediaType, String thumbnailFilePath, String orientation) {

        long fileLimit=  getFileMaxSize();
        if(!SDKConfiguration.Client.isWebHook)
        {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                    filePath, "bearer " + SocketWrapper.getInstance(getActivity()).getAccessToken(),
                    SocketWrapper.getInstance(getActivity()).getBotUserId(), "workflows", extn,
                    getBufferSize(mediaType),
                    new Messenger(messagesMediaUploadAcknowledgeHandler),
                    thumbnailFilePath, "AT_" + System.currentTimeMillis(),
                    getActivity(), mediaType, SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        }
        else
        {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                    filePath, "bearer " + jwt,
                    SocketWrapper.getInstance(getActivity()).getBotUserId(), "workflows", extn,
                    getBufferSize(mediaType),
                    new Messenger(messagesMediaUploadAcknowledgeHandler),
                    thumbnailFilePath, "AT_" + System.currentTimeMillis(),
                    getActivity(), mediaType, SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        }
    }

    private long getFileMaxSize() {
        long FILE_MAX_SIZE = getFileLimit();
        if(FILE_MAX_SIZE!=-1) {
            FILE_MAX_SIZE = FILE_MAX_SIZE * 1024 * 1024;
        }

        return FILE_MAX_SIZE;
    }

    private int getFileLimit() {
        attachment= SharedPreferenceUtils.getInstance(getActivity()).getAttachmentPref("");

        int file_limit = -1;
        if (attachment != null) {
            file_limit = attachment.getSize();
        }

        return file_limit;
    }

    private void processImageResponse(Intent data) {
        String filePath = data.getStringExtra("filePath");
        String fileName;
        String filePathThumbnail;
        String orientation = null;
        if (filePath != null) {
            fileName = data.getStringExtra("fileName");
            filePathThumbnail = data.getStringExtra(THUMBNAIL_FILE_PATH);
            String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
            Bitmap thePic = BitmapUtils.decodeBitmapFromFile(filePath, 800, 600, false);
//                    compressImage(filePath);
            OutputStream fOut = null;
            if (thePic != null) {
                try {
                    // compress the image
                    File _file = new File(filePath);

                    LogUtils.d(LOG_TAG, " file.exists() ---------------------------------------- " + _file.exists());
                    fOut = new FileOutputStream(_file);

                    thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
                    thePic = rotateIfNecessary(filePath, thePic);
                    orientation = thePic.getWidth() > thePic.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                    fOut.flush();
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, e.toString());
                }
                finally {
                    try {
                        assert fOut != null;
                        fOut.close();
                    }
                    catch (Exception e){e.printStackTrace();}
                }
            }
            long fileLimit =  getFileMaxSize();

            if(!SDKConfiguration.Client.isWebHook)
            {
                KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                        filePath, "bearer " + SocketWrapper.getInstance(getActivity()).getAccessToken(),
                        SocketWrapper.getInstance(getActivity()).getBotUserId(), "workflows", extn,
                        KoreMedia.BUFFER_SIZE_IMAGE,
                        new Messenger(messagesMediaUploadAcknowledgeHandler),
                        filePathThumbnail, "AT_" + System.currentTimeMillis(),
                        getActivity(), BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation,true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
            }
            else
            {
                KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                        filePath, "bearer " + jwt,
                        SocketWrapper.getInstance(getActivity()).getBotUserId(), "workflows", extn,
                        KoreMedia.BUFFER_SIZE_IMAGE,
                        new Messenger(messagesMediaUploadAcknowledgeHandler),
                        filePathThumbnail, "AT_" + System.currentTimeMillis(),
                        getActivity(), BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation,true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.webHook_bot_id));
            }


        } else {
            ToastUtils.showToast(getActivity(), "Unable to attach file!");
        }

    }

    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey)
    {
        composebarAttachmentAdapter.addAttachment(attachmentKey);
        if(composebarAttachmentAdapter.getItemCount()>0)
        {
            enableOrDisableSendButton(true);
        }
    }

    /**
     * this method update the ui of send button based on enable/disable
     *
     * @param enable
     */
    public void enableOrDisableSendButton(boolean enable) {

        if(composebarAttachmentAdapter.getItemCount()>0 || enable)
        {
            sendButton.setVisibility(View.VISIBLE);
            rec_audio_img.setVisibility(View.GONE);
        }
        else
        {
            rec_audio_img.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.GONE);
        }
    }

    public void setJwtToken(String jwt)
    {
        this.jwt = jwt;
    }

    private void processVideoResponse(Uri selectedImage, boolean isCapturedVideo, Intent intent) {
        String realPath;
        String orientation;
        String fileName = null;
        if (isCapturedVideo)
            realPath = selectedImage.getPath();
        else {
            realPath = KaMediaUtils.getRealPath(getActivity(), selectedImage);
        }
        if (realPath != null) {
            if (realPath.length() > 0) {
                int startInd = realPath.lastIndexOf(File.separator) + 1;
                int endInd = realPath.indexOf(".", startInd);
                fileName = realPath.substring(startInd, endInd);
            }

            Bitmap thumbnail = null;
            String extn = realPath.substring(realPath.lastIndexOf(".") + 1);
//            if (!isCapturedVideo) {
//                int videoThumbnailIndexId = BitmapUtils.getVideoIdFromFilePath(requireActivity(), selectedImage);
//                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(requireActivity().getContentResolver(), videoThumbnailIndexId, MediaStore.Video.Thumbnails.MINI_KIND, null);
//            } else {
//                thumbnail = ThumbnailUtils.createVideoThumbnail(realPath, MediaStore.Video.Thumbnails.MINI_KIND);
//            }

//            if (thumbnail == null) {
                thumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.videoplaceholder_left);
//            }

            Bitmap hover = BitmapFactory.decodeResource(getResources(), R.drawable.btn_video_play_irc);
            thumbnail = overlay(thumbnail, hover);
            orientation = thumbnail.getWidth() > thumbnail.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
            String bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, realPath, compressQualityInt);
            processFileUpload(fileName, realPath, extn, BitmapUtils.obtainMediaTypeOfExtn(extn), bmpPath, orientation);
        }
        else {
            try {
                DocumentFile pickFile = DocumentFile.fromSingleUri(requireActivity(), selectedImage);
                String name = null;
                String type = null;

                if (pickFile != null) {
                    name = pickFile.getName();
                    type = pickFile.getType();
                }

                if (type != null && type.contains("video")) {
                    KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_VIDEO, "");
                    String filePath = KaMediaUtils.getAppDir() + File.separator + name;
                    new SaveVideoTask(filePath, name, selectedImage, getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveVideoTask extends AsyncTask<String, String, String> {

        private final String filePath;
        private String fileName;
        private final Uri fileUri;
        private final WeakReference<Context> mContext;

        SaveVideoTask(String filePath, String fileName, Uri fileUri, Context mContext) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.fileUri = fileUri;
            this.mContext = new WeakReference<>(mContext);
        }


        @Override
        protected String doInBackground(String... params) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);
            if (filePath != null && mContext.get() != null) {
//                    compressImage(filePath);
                FileOutputStream fOut = null;
                BufferedOutputStream out = null;
                try {

                    fOut = new FileOutputStream(filePath);
                    out = new BufferedOutputStream(fOut);
                    InputStream in = mContext.get().getContentResolver().openInputStream(fileUri);
                    byte[] buffer = new byte[8192];
                    int len = 0;
                    while ((len = in != null ? in.read(buffer) : 0) >= 0) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, e.toString());
                    return null;
                } finally {
                    if (fOut != null) {
                        try {
                            fOut.getFD().sync();
                            fOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        if(out != null)
                            out.close();
                    }catch (Exception e){e.printStackTrace();}
                }
            }
            return filePath;
        }


        @Override
        protected void onPostExecute(String realPath) {
//            filePath = path;
            if (realPath != null && mContext.get() != null) {
                if (realPath.length() > 0) {
                    int startInd = realPath.lastIndexOf(File.separator) + 1;
                    int endInd = realPath.indexOf(".", startInd);
                    fileName = realPath.substring(startInd, endInd);
                }
                int videoThumbnailIndexId = 0;
                String extn = realPath.substring(realPath.lastIndexOf(".") + 1);
                try {
                    videoThumbnailIndexId = BitmapUtils.getVideoIdFromFilePath(mContext.get(), fileUri);
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
//            Bitmap bitMap = ThumbnailUtils.createVideoThumbnail(realPath, MediaStore.Video.Thumbnails.MINI_KIND);
                Bitmap hover = BitmapFactory.decodeResource(getResources(), R.drawable.btn_video_play_irc);
                Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(requireActivity().getContentResolver(), videoThumbnailIndexId, MediaStore.Video.Thumbnails.MINI_KIND, null);
                if (thumbnail == null) {
                    thumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.videoplaceholder_left);

                }
                thumbnail = overlay(thumbnail, hover);
                String orientation = thumbnail.getWidth() > thumbnail.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                String bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, realPath, compressQualityInt);
                processFileUpload(fileName, realPath, extn, BitmapUtils.obtainMediaTypeOfExtn(extn), bmpPath, orientation);
            }
        }
    }

    private Bitmap overlay(Bitmap bitmap1, Bitmap bitmap2) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = bitmap2.getWidth();
        int bitmap2Height = bitmap2.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, marginLeft, marginTop, null);
        return overlayBitmap;
    }

    final Handler messagesMediaUploadAcknowledgeHandler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle reply = msg.getData();

            if (reply.getBoolean(UploadBulkFile.isFileSizeMore_key, false)) {
                showFreemiumDialog();
                return;
            }

            if (reply.getBoolean("success", true)) {
                long fileSizeBytes = reply.getLong(UploadBulkFile.fileSizeBytes_key);
                totalFileSize= totalFileSize+fileSizeBytes;
//                String messageId = reply.getString(Constants.MESSAGE_ID);
                String mediaFilePath = reply.getString("filePath");
                String MEDIA_TYPE = reply.getString("fileExtn");
                String mediaFileId = reply.getString("fileId");
                String mediaFileName = reply.getString("fileName");
                String componentType = reply.getString("componentType");
                String thumbnailURL = reply.getString("thumbnailURL");
                String orientation = reply.getString(BundleConstants.ORIENTATION);
                String COMPONENT_DESCRIPTION = reply.getString("componentDescription") != null ? reply.getString("componentDescription") : null;
                HashMap<String, Object> COMPONENT_DATA = reply.getSerializable("componentData") != null ? ((HashMap<String, Object>) reply.getSerializable("componentData")) : null;
                String fileSize = reply.getString("fileSize");
                KoreComponentModel koreMedia = new KoreComponentModel();
                koreMedia.setMediaType(BitmapUtils.getAttachmentType(componentType));

                HashMap<String, Object> cmpData = new HashMap<>(1);
                cmpData.put("fileName", mediaFileName);
                /*if (isEditKnowledgeFlow) {
                    cmpData.put("isNewComponent", true);
                }*/
                koreMedia.setComponentData(cmpData);
                koreMedia.setMediaFileName(getComponentId(componentType));
                koreMedia.setMediaFilePath(mediaFilePath);
                koreMedia.setFileSize(fileSize);

                koreMedia.setMediafileId(mediaFileId);
                koreMedia.setMediaThumbnail(thumbnailURL);

                HashMap<String, String> attachmentKey = new HashMap<>();
                String keyExtenssion= FileUtils.VideoTypes().contains(MEDIA_TYPE) ?"." + MEDIA_TYPE:"";
                attachmentKey.put("fileName", mediaFileName + keyExtenssion);
                attachmentKey.put("fileType", componentType);
                attachmentKey.put("fileId", mediaFileId);
                attachmentKey.put("localFilePath", mediaFilePath);
                attachmentKey.put("fileExtn", MEDIA_TYPE);
                attachmentKey.put("thumbnailURL", thumbnailURL);
                ((BotChatActivity) getActivity()).mediaAttachment(attachmentKey);
                // kaComponentModels.add(koreMedia);
                //insertTags(koreMedia, componentType, orientation, mediaFileName);

            }else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if(!TextUtils.isEmpty(errorMsg)) {
                    ToastUtils.showToast(getActivity(), errorMsg);
                }
            }
        }
    };

    private String getComponentId(String componentType) {
        if (componentType.equalsIgnoreCase(BundleConstants.MEDIA_TYPE_IMAGE)) {
            return "image_" + System.currentTimeMillis();
        } else if (componentType.equalsIgnoreCase(BundleConstants.MEDIA_TYPE_VIDEO)) {
            return "video_" + System.currentTimeMillis();
        } else {
            return "doc_" + System.currentTimeMillis();
        }
    }

    private void showFreemiumDialog() {
        if(attachment==null) {
            attachment= SharedPreferenceUtils.getInstance(getActivity()).getAttachmentPref("");
        }
        FreemiumData freemiumData= new FreemiumData(FreemiumType.alertDialogType);
        freemiumData.message="File uploads are limited to "+attachment.getSize()+"MB\nunder the Free plan.";
        String enterprise_msg="File uploads are limited to "+attachment.getSize()+"MB\nunder the Enterprise Plan";
//        new Freemium(getActivity(), freemiumData, null).showFreemiumDialog(true,enterprise_msg);
    }

    public void setSectionSelected(/*SECTION_TYPE sectionSelected*/) {
//        animateLayoutVisible();
//        Log.d("IKIDO", "setting current state " + sectionSelected);
        /*try{
            throw new Exception("IKIDO");
        }catch (Exception e){
            e.printStackTrace();
        }*/
       /* current_state = sectionSelected;
        switch (sectionSelected) {
            *//*case SECTION_YOU:
                setYouSelected(true);
                setKnowledgeSelected(false);
                setComposeSectionSelected(false);
                animateLayoutGone();
                break;
            case SECTION_COMPOSE:
                setYouSelected(false);
                setKnowledgeSelected(false);
                setComposeSectionSelected(true);
                break;*//*
            case SECTION_COMPOSE_WITH_COMPOSE_BAR:
                showComposeBarOnly();
                break;
            case SECTION_COMPOSE_OPEN:
                keyBoardAction();
                break;
            *//*case SECTION_KNOWLEDGE:
                animateLayoutGone();
                setYouSelected(false);
                setKnowledgeSelected(true);
                setComposeSectionSelected(false);
                break;*//*

        }*/
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