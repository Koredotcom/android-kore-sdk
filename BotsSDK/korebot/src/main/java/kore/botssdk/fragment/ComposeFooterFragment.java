package kore.botssdk.fragment;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static kore.botssdk.activity.KaCaptureImageActivity.THUMBNAIL_FILE_PATH;
import static kore.botssdk.utils.BitmapUtils.getBufferSize;
import static kore.botssdk.utils.BitmapUtils.rotateIfNecessary;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PERMISSION_REQUEST;
import static kore.botssdk.viewUtils.FileUtils.EXT_JPG;
import static kore.botssdk.viewUtils.FileUtils.EXT_PNG;
import static kore.botssdk.viewUtils.FileUtils.EXT_VIDEO;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.PermissionChecker;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.activity.KaCaptureImageActivity;
import kore.botssdk.adapter.AttachmentOptionsAdapter;
import kore.botssdk.adapter.ComposebarAttachmentAdapter;
import kore.botssdk.bot.BotClient;
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
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.SharedPreferenceUtils;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.viewUtils.FileUtils;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class ComposeFooterFragment extends Fragment implements ComposeFooterUpdate, SpeechDelegate {
    private final String LOG_TAG = ComposeFooterFragment.class.getName();
    private EditText editTextMessage;
    private TextView speakerText;
    private LinearLayout mainContentLayout;
    private LinearLayout defaultFooterLayout;
    private ImageView recAudioImg;
    private ImageView audioSpeakTts;
    private ImageView keyboardImg;
    private ImageView newMenuLogo;
    private SpeechProgressView progress;
    private TextView textViewSpeech;
    private static final int REQUEST_RECORD_AUDIO = 13;
    private boolean isDisabled, isFirstTime, isTTSEnabled = false;
    private ComposeFooterInterface composeFooterInterface;
    private TTSUpdate ttsUpdate;
    private LinearLayout linearLayoutProgress, llSend;
    private BotOptionsModel botOptionsModel;
    private ReUsableListViewActionSheet listViewActionSheet;
    private AttachmentOptionsAdapter adapter;
    private static final int REQ_IMAGE = 444;
    private static final int REQ_CAMERA = 443;
    private static final int REQ_VIDEO = 445;
    private static final int REQ_VIDEO_CAPTURE = 446;
    private static final int REQ_FILE = 448;
    private ComposebarAttachmentAdapter composebarAttachmentAdapter;
    private RecyclerView attachment_recycler;
    private Attachment attachment;
    private static long totalFileSize;
    private final int compressQualityInt = 100;
    private String jwt;
    private RelativeLayout rlFooter;
    private boolean isAgentConnected;
    private BotClient botClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Speech.init(getContext(), requireActivity().getPackageName());
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
        keyboardImg.performClick();
        return view;
    }

    @Override
    public void onDestroy() {
        KoreEventCenter.unregister(this);
        Speech.getInstance().shutdown();
        super.onDestroy();
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    private void findViews(View view) {
        mainContentLayout = view.findViewById(R.id.mainContent);
        defaultFooterLayout = view.findViewById(R.id.default_footer);
        editTextMessage = view.findViewById(R.id.edtTxtMessage);
        editTextMessage.addTextChangedListener(composeTextWatcher);
        speakerText = view.findViewById(R.id.speaker_text);
        recAudioImg = view.findViewById(R.id.rec_audio_img);
        keyboardImg = view.findViewById(R.id.keyboard_image);
        audioSpeakTts = view.findViewById(R.id.audio_speak_tts);
        linearLayoutProgress = view.findViewById(R.id.linearLayoutProgress);
        newMenuLogo = view.findViewById(R.id.newMenuLogo);
        progress = view.findViewById(R.id.progress);
        ImageView ivAttachment = view.findViewById(R.id.attachemnt);
        rlFooter = view.findViewById(R.id.rlFooter);
        llSend = view.findViewById(R.id.llSend);

        if (SDKConfiguration.BubbleColors.showAttachment) ivAttachment.setVisibility(View.VISIBLE);

        if (SDKConfiguration.BubbleColors.showTextToSpeech) audioSpeakTts.setVisibility(View.VISIBLE);

        if (SDKConfiguration.BubbleColors.showASRMicroPhone) recAudioImg.setVisibility(View.VISIBLE);

        attachment_recycler = view.findViewById(R.id.attachment_recycler);
        attachment_recycler.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        int[] colors = {
                requireActivity().getResources().getColor(android.R.color.black),
                requireActivity().getResources().getColor(android.R.color.darker_gray),
                requireActivity().getResources().getColor(android.R.color.black),
                requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                requireActivity().getResources().getColor(android.R.color.holo_red_dark)
        };
        progress.setColors(colors);

        textViewSpeech = view.findViewById(R.id.text_view_speech);
        if (composebarAttachmentAdapter == null) {
            composebarAttachmentAdapter = new ComposebarAttachmentAdapter(requireActivity(), new AttachmentListner() {
                @Override
                public void onRemoveAttachment() {
                    attachment_recycler.setVisibility(View.GONE);
                    enableOrDisableSendButton(composebarAttachmentAdapter.getItemCount() > 0 || !TextUtils.isEmpty(editTextMessage.getText().toString().trim()));
                }
            });
            attachment_recycler.setAdapter(composebarAttachmentAdapter);
        }
        ivAttachment.setOnClickListener(view1 -> {
            if (composebarAttachmentAdapter.getItemCount() < 1) {
                showAttachmentActionSheet();
            } else {
                ToastUtils.showToast(requireActivity(), "You can upload only one file");
            }
        });
    }

    public void updateUI() {
        if (llSend != null) {
            llSend.setEnabled(!isDisabled && !isFirstTime);
            llSend.setAlpha(llSend.isEnabled() ? 1.0f : 0.5f);
        }
    }

    private void setListener() {
        if (isDisabled && isFirstTime) {
            llSend.setOnClickListener(null);
        } else {
            llSend.setOnClickListener(composeFooterSendBtOnClickListener);
        }
    }

    private void setListenerExplicitly() {
        keyboardImg.setOnClickListener(keyboardIconClickListener);
        speakerText.setOnClickListener(v -> onButtonClick());
        audioSpeakTts.setOnClickListener(onTTSEnableSwitchClickListener);
        recAudioImg.setOnClickListener(onVoiceModeActivated);
        newMenuLogo.setOnClickListener(v -> {
            if (botOptionsModel != null && botOptionsModel.getTasks() != null && botOptionsModel.getTasks().size() > 0) {
                OptionsActionSheetFragment bottomSheetDialog = new OptionsActionSheetFragment();
                bottomSheetDialog.setisFromFullView(false);
                bottomSheetDialog.setSkillName("skillName", "trigger");
                bottomSheetDialog.setData(botOptionsModel);
                bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                bottomSheetDialog.show(requireActivity().getSupportFragmentManager(), "add_tags");
            }
        });
    }

    public void changeThemeBackGround(String widgetFooterColor, String widgetFooterHintColor) {
        if (widgetFooterColor != null) rlFooter.setBackgroundColor(Color.parseColor(widgetFooterColor));
        if (widgetFooterHintColor != null) editTextMessage.setHintTextColor(Color.parseColor(widgetFooterHintColor));
    }

    void toggleTTSButton() {
        if (isTTSEnabled) {
            audioSpeakTts.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            audioSpeakTts.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(), false);
        } else {
            LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    void sendMessageAttachmentText(String message, ArrayList<HashMap<String, String>> dataList) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(), dataList, false);
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
                llSend.setVisibility(View.GONE);

                if (SDKConfiguration.BubbleColors.showASRMicroPhone) recAudioImg.setVisibility(View.VISIBLE);

                if (isAgentConnected && botClient != null)
                    botClient.sendReceipts(BundleConstants.STOP_TYPING, "");

            } else if ((llSend.getVisibility() != View.VISIBLE)
                    || (s.length() > 0 && llSend.getVisibility() != View.VISIBLE)) {

                if (isAgentConnected && botClient != null)
                    botClient.sendReceipts(BundleConstants.TYPING, "");

                llSend.setVisibility(View.VISIBLE);
                recAudioImg.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    final View.OnClickListener composeFooterSendBtOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = editTextMessage.getText().toString();
            if (!msg.trim().isEmpty()) {
                if (composebarAttachmentAdapter.getItemCount() > 0) {
                    if (Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".png")
                            || Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".jpg")
                            || Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".jpeg"))
                        sendMessageAttachmentText(msg + "\n" + requireActivity().getResources().getString(R.string.camera) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                    else if (Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".mp4"))
                        sendMessageAttachmentText(msg + "\n" + requireActivity().getResources().getString(R.string.video) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                    else if (Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".mp3")
                            || Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".m4a"))
                        sendMessageAttachmentText(msg + "\n" + requireActivity().getResources().getString(R.string.audio) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                    else
                        sendMessageAttachmentText(msg + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                } else
                    sendMessageText(msg);

                editTextMessage.setText("");
                composebarAttachmentAdapter.clearAll();
                enableOrDisableSendButton(false);
            } else if (composebarAttachmentAdapter.getItemCount() > 0) {
                if (Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".png")
                        || Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".jpg")
                        || Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".jpeg"))
                    sendMessageAttachmentText(requireActivity().getResources().getString(R.string.camera) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                else if (Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".mp4"))
                    sendMessageAttachmentText(requireActivity().getResources().getString(R.string.video) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                else if (Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".mp3")
                        || Objects.requireNonNull(composebarAttachmentAdapter.getData().get(0).get("fileName")).contains(".m4a"))
                    sendMessageAttachmentText(requireActivity().getResources().getString(R.string.audio) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());
                else
                    sendMessageAttachmentText(requireActivity().getResources().getString(R.string.attachment) + " " + composebarAttachmentAdapter.getData().get(0).get("fileName"), composebarAttachmentAdapter.getData());

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
            animateLayoutVisible(defaultFooterLayout);
            new Handler().postDelayed(() -> onButtonClick(), 500);
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

    public void initialSetUp() {
        mainContentLayout.setVisibility(View.GONE);
        animateLayoutGone(mainContentLayout);
        animateLayoutVisible(defaultFooterLayout);
    }

    public boolean isTTSEnabled() {
        return isTTSEnabled;
    }

    private void requestMicrophonePermission() {
        KaPermissionsHelper.requestForPermission(requireActivity(), CAPTURE_IMAGE_BUNDLED_PERMISSION_REQUEST,
                Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onRecordAudioPermissionGranted();
        }
    }

    void animateLayoutVisible(View view) {
        view.setVisibility(View.VISIBLE);
        view.animate().translationY(0).alpha(1.0f).setDuration(500).setListener(null);
    }

    void animateLayoutGone(final View view) {
        view.animate().translationY(view.getHeight()).alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
    }

    void onButtonClick() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_GRANTED) {
                onRecordAudioPermissionGranted();
            } else {
                requestMicrophonePermission();
            }
        }
    }

    private void onRecordAudioPermissionGranted() {
        stopTTS();
        Utility.hideVirtualKeyboard(requireActivity());
        speakerText.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);
        textViewSpeech.setVisibility(View.VISIBLE);
        textViewSpeech.setText("");

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
        textViewSpeech.setText(results.toString());
        for (String partial : results) {
            textViewSpeech.append(partial + " ");
        }
    }

    @Override
    public void onSpeechResult(String result) {
        speakerText.setVisibility(View.VISIBLE);
        linearLayoutProgress.setVisibility(View.GONE);
        textViewSpeech.setText(result);

        if (result.isEmpty()) {
            Speech.getInstance().say(getString(R.string.repeat));

        } else {
            if (composeFooterInterface != null) {
                composeFooterInterface.onSendClick(result, false);
                editTextMessage.setText("");
            } else {
                LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
            }
        }
        textViewSpeech.setVisibility(View.GONE);
    }

    public void setBotClient(BotClient botClient) {
        this.botClient = botClient;
    }

    public void setIsAgentConnected(boolean isAgentConnected) {
        this.isAgentConnected = isAgentConnected;
    }

    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    SpeechUtil.redirectUserToGoogleAppOnPlayStore(requireActivity());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.speech_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.enable_google_voice_typing)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    // do nothing
                })
                .show();
    }

    public void setComposeText(String text) {
        editTextMessage.setText(text);
        editTextMessage.setSelection(text.length());
    }

    void showAttachmentActionSheet() {
        if (listViewActionSheet == null) {
            listViewActionSheet = new ReUsableListViewActionSheet(requireActivity());
            listViewActionSheet.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams wlp = listViewActionSheet.getWindow().getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            listViewActionSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        listViewActionSheet.show();
        if (adapter == null) {
            ArrayList<String> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.attachments_strings)));
            adapter = new AttachmentOptionsAdapter(requireActivity(), options);
            listViewActionSheet.setAdapter(adapter);
            listViewActionSheet.getOptionsListView().setOnItemClickListener((parent, view, position, id) -> launchSelectedMode(options.get(position)));
        }
    }

    private void launchSelectedMode(String option) {
        final String capturePhoto = getString(R.string.attachment_capture_photo);
        final String captureVideo = getString(R.string.attachment_capture_video);
        final String uploadPhoto = getString(R.string.attachment_upload_photo);
        final String uploadVideo = getString(R.string.attachment_upload_video);
        final String uploadDocument = getString(R.string.attachment_upload_document);
        if (option.equals(capturePhoto))
            fileBrowsingActivity(KoreMedia.CHOOSE_TYPE_CAPTURE_IMAGE, REQ_CAMERA, BundleConstants.MEDIA_TYPE_IMAGE);
        else if (option.equals(captureVideo))
            fileBrowsingActivity(KoreMedia.CHOOSE_TYPE_CAPTURE_VIDEO, REQ_VIDEO_CAPTURE, BundleConstants.MEDIA_TYPE_VIDEO);
        else if (option.equals(uploadPhoto))
            fileBrowsingActivity(KoreMedia.CHOOSE_TYPE_IMAGE_PICK, REQ_IMAGE, BundleConstants.MEDIA_TYPE_IMAGE);
        else if (option.equals(uploadVideo))
            fileBrowsingActivity(KoreMedia.CHOOSE_TYPE_VIDEO_PICK, REQ_VIDEO, BundleConstants.MEDIA_TYPE_VIDEO);
        else if (option.equals(uploadDocument))
            fileBrowsingActivity(KoreMedia.CHOOSE_TYPE_DOCUMENT_PICK, REQ_FILE, BundleConstants.MEDIA_TYPE_DOCUMENT);
        listViewActionSheet.dismiss();
    }

    public void fileBrowsingActivity(String chooseType, int reqCode, String mediaType) {
        Intent mediaPickerIntent = new Intent(requireActivity(), KaCaptureImageActivity.class);
        mediaPickerIntent.putExtra("pickType", chooseType);
        mediaPickerIntent.putExtra("fileContext", BundleConstants.FOR_MESSAGE);
        mediaPickerIntent.putExtra("mediaType", mediaType);

        if (reqCode == REQ_VIDEO || reqCode == REQ_VIDEO_CAPTURE) {
            activityVideoResultLaunch.launch(mediaPickerIntent);
        } else {
            activityImageResultLaunch.launch(mediaPickerIntent);
        }
    }

    private final ActivityResultLauncher<Intent> activityImageResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String filePath = result.getData().getStringExtra("filePath");
                    String fileName = result.getData().getStringExtra("fileName");
                    String filePathThumbnail = result.getData().getStringExtra(THUMBNAIL_FILE_PATH);
                    ((BotChatActivity) requireActivity()).sendImage(filePath, fileName, filePathThumbnail);
                }
            });

    private final ActivityResultLauncher<Intent> activityVideoResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String fileExtn = result.getData().getStringExtra("fileExtn");
                    if (fileExtn != null && fileExtn.equals(EXT_VIDEO) && result.getData().getParcelableExtra("fileUri") != null) {
                        processVideoResponse(result.getData().getParcelableExtra("fileUri"), false, result.getData());
                    } else if (fileExtn != null && (fileExtn.equalsIgnoreCase(EXT_JPG) || fileExtn.equalsIgnoreCase(EXT_PNG))) {
                        processImageResponse(result.getData());
                    } else {
                        String filePath = result.getData().getStringExtra("filePath");
                        if (filePath == null) {
                            ToastUtils.showToast(requireActivity(), "Unable to attach file!");
                            return;
                        }
                        String fileName = result.getData().getStringExtra("fileName");
                        String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                        String mediaType = BitmapUtils.obtainMediaTypeOfExtn(extn);
                        processFileUpload(fileName, filePath, extn, mediaType, null, null);
                    }
                }
            });

    private void processFileUpload(String fileName, String filePath, String extn, String mediaType, String thumbnailFilePath, String orientation) {
        if (!SDKConfiguration.Client.isWebHook) {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                    filePath, "bearer " + SocketWrapper.getInstance(requireActivity()).getAccessToken(),
                    SocketWrapper.getInstance(requireActivity()).getBotUserId(), "workflows", extn,
                    getBufferSize(mediaType),
                    new Messenger(messagesMediaUploadAcknowledgeHandler),
                    thumbnailFilePath, "AT_" + System.currentTimeMillis(),
                    requireActivity(), mediaType, SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        } else {
            KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                    filePath, "bearer " + jwt,
                    SocketWrapper.getInstance(requireActivity()).getBotUserId(), "workflows", extn,
                    getBufferSize(mediaType),
                    new Messenger(messagesMediaUploadAcknowledgeHandler),
                    thumbnailFilePath, "AT_" + System.currentTimeMillis(),
                    requireActivity(), mediaType, SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
        }
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
            OutputStream fOut = null;
            if (thePic != null) {
                try {
                    // compress the image
                    File _file = new File(filePath);

                    LogUtils.d(LOG_TAG, " file.exists() ---------------------------------------- " + _file.exists());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fOut = Files.newOutputStream(_file.toPath());
                    } else fOut = new FileOutputStream(_file);

                    thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
                    thePic = rotateIfNecessary(filePath, thePic);
                    orientation = thePic.getWidth() > thePic.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
                    fOut.flush();
                } catch (Exception e) {
                    LogUtils.e(LOG_TAG, e.toString());
                } finally {
                    try {
                        assert fOut != null;
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!SDKConfiguration.Client.isWebHook) {
                KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                        filePath, "bearer " + SocketWrapper.getInstance(requireActivity()).getAccessToken(),
                        SocketWrapper.getInstance(requireActivity()).getBotUserId(), "workflows", extn,
                        KoreMedia.BUFFER_SIZE_IMAGE,
                        new Messenger(messagesMediaUploadAcknowledgeHandler),
                        filePathThumbnail, "AT_" + System.currentTimeMillis(),
                        requireActivity(), BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.bot_id));
            } else {
                KoreWorker.getInstance().addTask(new UploadBulkFile(fileName,
                        filePath, "bearer " + jwt,
                        SocketWrapper.getInstance(requireActivity()).getBotUserId(), "workflows", extn,
                        KoreMedia.BUFFER_SIZE_IMAGE,
                        new Messenger(messagesMediaUploadAcknowledgeHandler),
                        filePathThumbnail, "AT_" + System.currentTimeMillis(),
                        requireActivity(), BitmapUtils.obtainMediaTypeOfExtn(extn), SDKConfiguration.Server.SERVER_URL, orientation, true, SDKConfiguration.Client.isWebHook, SDKConfiguration.Client.webHook_bot_id));
            }
        } else {
            ToastUtils.showToast(requireActivity(), "Unable to attach file!");
        }
    }

    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey) {
        attachment_recycler.setVisibility(View.VISIBLE);
        composebarAttachmentAdapter.addAttachment(attachmentKey);
        if (composebarAttachmentAdapter.getItemCount() > 0) {
            enableOrDisableSendButton(true);
        }
    }

    /**
     * this method update the ui of send button based on enable/disable
     *
     * @param enable
     */
    public void enableOrDisableSendButton(boolean enable) {
        if (composebarAttachmentAdapter.getItemCount() > 0 || enable) {
            llSend.setVisibility(View.VISIBLE);
            recAudioImg.setVisibility(View.GONE);
        } else {
            if (SDKConfiguration.BubbleColors.showASRMicroPhone)
                recAudioImg.setVisibility(View.VISIBLE);

            llSend.setVisibility(View.GONE);
        }
    }

    public void setJwtToken(String jwt) {
        this.jwt = jwt;
    }

    private void processVideoResponse(Uri selectedImage, boolean isCapturedVideo, Intent intent) {
        String realPath;
        String orientation;
        String fileName = null;
        if (isCapturedVideo)
            realPath = selectedImage.getPath();
        else {
            realPath = KaMediaUtils.getRealPath(requireActivity(), selectedImage);
        }
        if (realPath != null) {
            if (realPath.length() > 0) {
                int startInd = realPath.lastIndexOf(File.separator) + 1;
                int endInd = realPath.indexOf(".", startInd);
                fileName = realPath.substring(startInd, endInd);
            }

            String extn = realPath.substring(realPath.lastIndexOf(".") + 1);
            Bitmap thumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.videoplaceholder_left);

            Bitmap hover = BitmapFactory.decodeResource(getResources(), R.drawable.btn_video_play_irc);
            thumbnail = overlay(thumbnail, hover);
            orientation = thumbnail.getWidth() > thumbnail.getHeight() ? BitmapUtils.ORIENTATION_LS : BitmapUtils.ORIENTATION_PT;
            String bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, realPath, compressQualityInt);
            processFileUpload(fileName, realPath, extn, BitmapUtils.obtainMediaTypeOfExtn(extn), bmpPath, orientation);
        } else {
            try {
                DocumentFile pickFile = DocumentFile.fromSingleUri(requireActivity(), selectedImage);
                String name = null;
                String type = null;

                if (pickFile != null) {
                    name = pickFile.getName();
                    type = pickFile.getType();
                }

                if (type != null && type.contains("video")) {
                    KaMediaUtils.setupAppDir(requireContext(), BundleConstants.MEDIA_TYPE_VIDEO);
                    String filePath = KaMediaUtils.getAppDir() + File.separator + name;
                    new SaveVideoTask(filePath, name, selectedImage, requireActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                        if (out != null)
                            out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    final Handler messagesMediaUploadAcknowledgeHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle reply = msg.getData();

            if (reply.getBoolean(UploadBulkFile.isFileSizeMore_key, false)) {
                showFreemiumDialog();
                return;
            }

            if (reply.getBoolean("success", true)) {
                long fileSizeBytes = reply.getLong(UploadBulkFile.fileSizeBytes_key);
                totalFileSize = totalFileSize + fileSizeBytes;
//                String messageId = reply.getString(Constants.MESSAGE_ID);
                String mediaFilePath = reply.getString("filePath");
                String MEDIA_TYPE = reply.getString("fileExtn");
                String mediaFileId = reply.getString("fileId");
                String mediaFileName = reply.getString("fileName");
                String componentType = reply.getString("componentType");
                String thumbnailURL = reply.getString("thumbnailURL");
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
                String keyExtenssion = FileUtils.VideoTypes().contains(MEDIA_TYPE) ? "." + MEDIA_TYPE : "";
                attachmentKey.put("fileName", mediaFileName + keyExtenssion);
                attachmentKey.put("fileType", componentType);
                attachmentKey.put("fileId", mediaFileId);
                attachmentKey.put("localFilePath", mediaFilePath);
                attachmentKey.put("fileExtn", MEDIA_TYPE);
                attachmentKey.put("thumbnailURL", thumbnailURL);
                addAttachmentToAdapter(attachmentKey);
            } else {
                String errorMsg = reply.getString(UploadBulkFile.error_msz_key);
                if (!TextUtils.isEmpty(errorMsg)) {
                    ToastUtils.showToast(requireActivity(), errorMsg);
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
        if (attachment == null) {
            attachment = SharedPreferenceUtils.getInstance(requireActivity()).getAttachmentPref("");
        }
        FreemiumData freemiumData = new FreemiumData(FreemiumType.alertDialogType);
        freemiumData.message = "File uploads are limited to " + attachment.getSize() + "MB\nunder the Free plan.";
        String enterprise_msg = "File uploads are limited to " + attachment.getSize() + "MB\nunder the Enterprise Plan";
//        new Freemium(requireActivity(), freemiumData, null).showFreemiumDialog(true,enterprise_msg);
    }
}