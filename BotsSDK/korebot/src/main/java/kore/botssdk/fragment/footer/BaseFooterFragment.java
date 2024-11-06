package kore.botssdk.fragment.footer;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static kore.botssdk.activity.KaCaptureImageActivity.THUMBNAIL_FILE_PATH;
import static kore.botssdk.viewUtils.FileUtils.EXT_JPG;
import static kore.botssdk.viewUtils.FileUtils.EXT_PNG;
import static kore.botssdk.viewUtils.FileUtils.EXT_VIDEO;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
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
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.speech.Speech;
import kore.botssdk.speech.SpeechDelegate;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
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
    protected ComposeFooterInterface composeFooterInterface;
    private static final int REQ_IMAGE = 444;
    private static final int REQ_CAMERA = 443;
    private static final int REQ_VIDEO = 445;
    private static final int REQ_VIDEO_CAPTURE = 446;
    private static final int REQ_FILE = 448;
    protected String jwt;
    protected boolean isAgentConnected;
    protected BotClient botClient;
    protected BotFooterViewModel footerViewModel;
    private ReUsableListViewActionSheet listViewActionSheet;
    private AttachmentOptionsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Speech.init(getContext(), requireActivity().getPackageName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
        BotFooterViewModelFactory factory = new BotFooterViewModelFactory(requireActivity(), BaseFooterFragment.this);
        footerViewModel = new ViewModelProvider(this, factory).get(BotFooterViewModel.class);
    }

    @Override
    public void onDestroy() {
        KoreEventCenter.unregister(this);
        Speech.getInstance().shutdown();
        super.onDestroy();
    }

    public abstract void setDisabled(boolean disabled);

    public abstract void updateUI();

    public abstract void changeThemeBackGround(String widgetFooterColor, String widgetFooterHintColor);

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public abstract void setBottomOptionData(BotOptionsModel botOptionsModel);

    public abstract void setTtsUpdate(TTSUpdate ttsUpdate);

    public abstract void enableSendButton();

    public abstract boolean isTTSEnabled();

    private void requestMicrophonePermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    onRecordAudioPermissionGranted();
                }
            }
    );

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

    protected void onMicButtonClick() {
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

    protected abstract void onRecordAudioPermissionGranted();

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {
    }

    @Override
    public abstract void onSpeechPartialResults(List<String> results);

    @Override
    public abstract void onSpeechResult(String result);

    public void setBotClient(BotClient botClient) {
        this.botClient = botClient;
    }

    public void setIsAgentConnected(boolean isAgentConnected) {
        this.isAgentConnected = isAgentConnected;
    }

    public abstract void setComposeText(String text);

    protected void showAttachmentActionSheet() {
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
                    footerViewModel.sendImage(filePath, fileName, filePathThumbnail);
                }
            });

    private final ActivityResultLauncher<Intent> activityVideoResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String jwtToken = SDKConfiguration.Client.isWebHook ? jwt : SocketWrapper.getInstance(requireActivity()).getAccessToken();
                    String fileExtn = result.getData().getStringExtra("fileExtn");
                    if (fileExtn != null && fileExtn.equals(EXT_VIDEO) && result.getData().getParcelableExtra("fileUri") != null) {
                        footerViewModel.processVideoResponse(jwtToken, result.getData().getParcelableExtra("fileUri"));
                    } else if (fileExtn != null && (fileExtn.equalsIgnoreCase(EXT_JPG) || fileExtn.equalsIgnoreCase(EXT_PNG))) {
                        footerViewModel.processImageResponse(jwtToken, result.getData());
                    } else {
                        String filePath = result.getData().getStringExtra("filePath");
                        if (filePath == null) {
                            ToastUtils.showToast(requireActivity(), "Unable to attach file!");
                            return;
                        }
                        String fileName = result.getData().getStringExtra("fileName");
                        String extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                        String mediaType = BitmapUtils.obtainMediaTypeOfExtn(extn);
                        footerViewModel.processFileUpload(jwtToken, fileName, filePath, extn, mediaType, null, null);
                    }
                }
            });


    public abstract void enableOrDisableSendButton(boolean enable);

    public void setJwtToken(String jwt) {
        this.jwt = jwt;
        footerViewModel.setJwtToken(jwt);
    }

    @Override
    public abstract void addAttachmentToAdapter(HashMap<String, String> attachmentKey);
}