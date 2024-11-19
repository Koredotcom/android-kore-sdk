package kore.botssdk.fragment.footer;

import static android.app.Activity.RESULT_OK;
import static kore.botssdk.activity.KaCaptureImageActivity.THUMBNAIL_FILE_PATH;
import static kore.botssdk.view.viewUtils.FileUtils.EXT_JPG;
import static kore.botssdk.view.viewUtils.FileUtils.EXT_PNG;
import static kore.botssdk.view.viewUtils.FileUtils.EXT_VIDEO;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.activity.KaCaptureImageActivity;
import kore.botssdk.adapter.AttachmentOptionsAdapter;
import kore.botssdk.bot.BotClient;
import kore.botssdk.dialogs.ReUsableListViewActionSheet;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.ComposeFooterUpdate;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotBrandingModel;
import kore.botssdk.models.BrandingQuickStartButtonActionModel;
import kore.botssdk.models.FreemiumData;
import kore.botssdk.models.FreemiumType;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.models.limits.Attachment;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.speech.Speech;
import kore.botssdk.speech.SpeechDelegate;
import kore.botssdk.speech.SpeechUtil;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.BundleUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.SharedPreferenceUtils;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.viewmodels.footer.BotFooterViewModel;
import kore.botssdk.viewmodels.footer.BotFooterViewModelFactory;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public abstract class BaseFooterFragment extends Fragment implements ComposeFooterUpdate, SpeechDelegate {
    protected final String LOG_TAG = BaseFooterFragment.class.getName();
    protected boolean isDisabled;
    protected ComposeFooterInterface composeFooterInterface;
    protected ArrayList<BrandingQuickStartButtonActionModel> botOptionsModel;
    protected ReUsableListViewActionSheet listViewActionSheet;
    protected AttachmentOptionsAdapter adapter;
    private static final int REQ_IMAGE = 444;
    private static final int REQ_CAMERA = 443;
    private static final int REQ_VIDEO = 445;
    private static final int REQ_VIDEO_CAPTURE = 446;
    private static final int REQ_FILE = 448;
    protected Attachment attachment;
    protected String jwt;
    protected BotBrandingModel botBrandingModel;
    protected int[] colors;
    protected boolean isAgentConnected;
    protected BotClient botClient;
    protected BotFooterViewModel mFooterViewModel;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    onRecordAudioPermissionGranted();
                }
            }
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDisabled = true;
        Speech.init(getContext(), requireActivity().getPackageName());
        BotFooterViewModelFactory factory = new BotFooterViewModelFactory(requireActivity(), BaseFooterFragment.this);
        mFooterViewModel = new ViewModelProvider(this, factory).get(BotFooterViewModel.class);
        getBundleInfo();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
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

    public void setBotClient(BotClient botClient) {
        this.botClient = botClient;
    }

    public abstract void setBotBrandingModel(BotBrandingModel botBrandingModel);

    void getBundleInfo() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            botBrandingModel = (BotBrandingModel) bundle.getSerializable(BundleUtils.BRANDING);
        }
    }

    public abstract void updateUI();

    protected void sendMessageText(String message) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(), false);
        } else {
            LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    protected void sendMessageAttachmentText(String message, ArrayList<HashMap<String, String>> dataList) {
        if (composeFooterInterface != null) {
            composeFooterInterface.onSendClick(message.trim(), dataList, false);
        } else {
            LogUtils.e(LOG_TAG, "ComposeFooterInterface is not found. Please set the interface first.");
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public abstract void setTtsUpdate(TTSUpdate ttsUpdate);

    @Override
    public abstract void enableSendButton();

    public abstract boolean isTTSEnabled();

    protected void requestMicrophonePermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    protected void animateLayoutVisible(View view) {
        view.setVisibility(View.VISIBLE);
        view.animate().translationY(0).alpha(1.0f).setDuration(500).setListener(null);
    }

    protected void animateLayoutGone(final View view) {
        view.animate().translationY(view.getHeight()).alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
    }

    protected void onButtonClick() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (KaPermissionsHelper.hasPermission(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
                onRecordAudioPermissionGranted();
            } else {
                requestMicrophonePermission();
            }
        }
    }

    protected abstract void onRecordAudioPermissionGranted();

    @Override
    public abstract void onStartOfSpeech();

    @Override
    public void onSpeechRmsChanged(float value) {
    }

    @Override
    public abstract void onSpeechPartialResults(List<String> results);

    @Override
    public abstract void onSpeechResult(String result);

    void showSpeechNotSupportedDialog() {
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
        builder.setMessage(R.string.speech_not_available).setCancelable(false).setPositiveButton(R.string.yes, dialogClickListener).setNegativeButton(R.string.no, dialogClickListener).show();
    }

    void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.enable_google_voice_typing).setCancelable(false).setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            // do nothing
        }).show();
    }

    public abstract void setComposeText(String text);

    void showAttachmentActionSheet() {
        if (listViewActionSheet == null) {
            listViewActionSheet = new ReUsableListViewActionSheet(requireActivity());
            listViewActionSheet.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams wlp = Objects.requireNonNull(listViewActionSheet.getWindow()).getAttributes();
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

    protected void launchSelectedMode(String option) {
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
        Intent photoPickerIntent = new Intent(requireActivity(), KaCaptureImageActivity.class);
        photoPickerIntent.putExtra("pickType", chooseType);
        photoPickerIntent.putExtra("fileContext", BundleConstants.FOR_MESSAGE);
        photoPickerIntent.putExtra("mediaType", mediaType);
        if (reqCode == REQ_VIDEO) {
            activityVideoResultLaunch.launch(photoPickerIntent);
        } else {
            activityImageResultLaunch.launch(photoPickerIntent);
        }
    }

    final ActivityResultLauncher<Intent> activityImageResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String filePath = result.getData().getStringExtra("filePath");
                String fileName = result.getData().getStringExtra("fileName");
                String filePathThumbnail = result.getData().getStringExtra(THUMBNAIL_FILE_PATH);
                composeFooterInterface.sendImage(filePath, fileName, filePathThumbnail);
            }
        }
    });

    final ActivityResultLauncher<Intent> activityVideoResultLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String fileExtn = result.getData().getStringExtra("fileExtn");
                if (fileExtn != null && fileExtn.equals(EXT_VIDEO) && result.getData().getParcelableExtra("fileUri") != null) {
                    mFooterViewModel.processVideoResponse(SDKConfiguration.Client.isWebHook ? jwt : SocketWrapper.getInstance(requireActivity()).getAccessToken(), result.getData().getParcelableExtra("fileUri"));
                } else if (fileExtn != null && (fileExtn.equalsIgnoreCase(EXT_JPG) || fileExtn.equalsIgnoreCase(EXT_PNG))) {
                    mFooterViewModel.processImageResponse(SDKConfiguration.Client.isWebHook ? jwt : SocketWrapper.getInstance(requireActivity()).getAccessToken(), result.getData());
                } else {
                    String filePath = result.getData().getStringExtra("filePath");
                    if (filePath == null) {
                        ToastUtils.showToast(requireActivity(), "Unable to attach file!");
                        return;
                    }
                    String fileName = result.getData().getStringExtra("fileName");
                    String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                    String mediaType = BitmapUtils.obtainMediaTypeOfExtn(extn);
                    mFooterViewModel.processFileUpload(SDKConfiguration.Client.isWebHook ? jwt : SocketWrapper.getInstance(requireActivity()).getAccessToken(), fileName, filePath, extn, mediaType, null, null);
                }
            }
        }
    });


    @Override
    public abstract void addAttachmentToAdapter(HashMap<String, String> attachmentKey);

    /**
     * this method update the ui of send button based on enable/disable
     *
     * @param enable
     */
    public abstract void enableOrDisableSendButton(boolean enable);

    public void setJwtToken(String jwt) {
        this.jwt = jwt;
    }

    public void setIsAgentConnected(boolean isAgentConnected) {
        this.isAgentConnected = isAgentConnected;
    }

    @Override
    public void showFreemiumDialog() {
        if (attachment == null) {
            attachment = SharedPreferenceUtils.getInstance(requireActivity()).getAttachmentPref("");
        }
        FreemiumData freemiumData = new FreemiumData(FreemiumType.alertDialogType);
        freemiumData.message = "File uploads are limited to " + attachment.getSize() + "MB\nunder the Free plan.";
    }
}