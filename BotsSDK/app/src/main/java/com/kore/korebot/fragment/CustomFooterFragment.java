package com.kore.korebot.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kore.korebot.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kore.botssdk.adapter.ComposebarAttachmentAdapter;
import kore.botssdk.dialogs.OptionsActionSheetFragment;
import kore.botssdk.fragment.footer.BaseFooterFragment;
import kore.botssdk.listener.AttachmentListner;
import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.models.BotOptionsModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.speech.GoogleVoiceTypingDisabledException;
import kore.botssdk.speech.Speech;
import kore.botssdk.speech.SpeechRecognitionNotAvailable;
import kore.botssdk.speech.SpeechUtil;
import kore.botssdk.speech.ui.SpeechProgressView;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.ToastUtils;
import kore.botssdk.utils.Utility;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnknownNullness")
public class CustomFooterFragment extends BaseFooterFragment {
    private final String LOG_TAG = CustomFooterFragment.class.getName();
    private EditText editTextMessage;
    private TextView speakerText;
    private LinearLayout mainContentLayout;
    private LinearLayout defaultFooterLayout;
    private ImageView recAudioImg;
    private ImageView audioSpeakTts;
    private ImageView keyboardImg;
    private ImageView newMenuLogo;
    private ImageView ivAttachment;
    private SpeechProgressView progress;
    private TextView textViewSpeech, sendTv;
    private boolean isDisabled;
    private boolean isFirstTime;
    private boolean isTTSEnabled = false;
    private TTSUpdate ttsUpdate;
    private LinearLayout llSend;
    private BotOptionsModel botOptionsModel;
    private ComposebarAttachmentAdapter composebarAttachmentAdapter;
    private RecyclerView attachmentRecycler;
    private RelativeLayout rlFooter;
    private RelativeLayout rlSpeaker;
    private RelativeLayout rlSpeakerCircle;
    private int[] colors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_footer_fragment, null);
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
        newMenuLogo = view.findViewById(R.id.newMenuLogo);
        progress = view.findViewById(R.id.progress);
        ivAttachment = view.findViewById(R.id.attachemnt);
        rlFooter = view.findViewById(R.id.rlFooter);
        llSend = view.findViewById(R.id.llSend);
        rlSpeaker = view.findViewById(R.id.rlSpeaker);
        sendTv = view.findViewById(R.id.sendTv);
        rlSpeakerCircle = view.findViewById(R.id.rlSpeakerCircle);
        textViewSpeech = view.findViewById(R.id.text_view_speech);

        attachmentRecycler = view.findViewById(R.id.attachment_recycler);
        attachmentRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        colors = new int[]{ResourcesCompat.getColor(requireActivity().getResources(), android.R.color.white, requireActivity().getTheme()), ResourcesCompat.getColor(requireActivity().getResources(), android.R.color.white, requireActivity().getTheme()), ResourcesCompat.getColor(requireActivity().getResources(), android.R.color.white, requireActivity().getTheme())};
        int[] heights = {10, 20, 30, 20, 10};
        progress.setBarMaxHeightsInDp(heights);
        progress.setColors(colors);

        if (composebarAttachmentAdapter == null) {
            composebarAttachmentAdapter = new ComposebarAttachmentAdapter(requireActivity(), new AttachmentListner() {
                @Override
                public void onRemoveAttachment() {
                    attachmentRecycler.setVisibility(View.GONE);
                    enableOrDisableSendButton(composebarAttachmentAdapter.getItemCount() > 0 || !TextUtils.isEmpty(editTextMessage.getText().toString().trim()));
                }
            });
            attachmentRecycler.setAdapter(composebarAttachmentAdapter);
        }
        ivAttachment.setOnClickListener(view1 -> {
            if (composebarAttachmentAdapter.getItemCount() < 1) {
                showAttachmentActionSheet();
            } else {
                ToastUtils.showToast(requireActivity(), "You can upload only one file");
            }
        });
    }

    @Override
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
        speakerText.setOnClickListener(v -> onMicButtonClick());
        audioSpeakTts.setOnClickListener(onTTSEnableSwitchClickListener);
        recAudioImg.setOnClickListener(onVoiceModeActivated);
        newMenuLogo.setOnClickListener(v -> {
            if (botOptionsModel != null && botOptionsModel.getTasks() != null && !botOptionsModel.getTasks().isEmpty()) {
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
        rlFooter.setVisibility(View.VISIBLE);

        if (widgetFooterColor != null) {
            rlFooter.setBackgroundColor(Color.parseColor(widgetFooterColor));
            rlSpeaker.setBackgroundColor(Color.parseColor(widgetFooterColor));
        }
        if (widgetFooterHintColor != null) editTextMessage.setHintTextColor(Color.parseColor(widgetFooterHintColor));

        if (SDKConfiguration.OverrideKoreConfig.showAttachment) ivAttachment.setVisibility(View.VISIBLE);

        if (SDKConfiguration.OverrideKoreConfig.showTextToSpeech) audioSpeakTts.setVisibility(View.VISIBLE);

        if (SDKConfiguration.OverrideKoreConfig.showASRMicroPhone && StringUtils.isNullOrEmpty(editTextMessage.getText().toString()))
            recAudioImg.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences();
        String rightTextColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, "#ffffff");
        String LeftTextColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#ffffff");
        String rightBgColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, "#0078cd");
        GradientDrawable rightDrawable = (GradientDrawable) ResourcesCompat.getDrawable(requireActivity().getResources(), R.drawable.theme1_right_bubble_bg, requireActivity().getTheme());

        if (rightDrawable != null) {
            rightDrawable.setColor(Color.parseColor(rightBgColor));
        }

        textViewSpeech.setBackground(rightDrawable);
        textViewSpeech.setTextColor(Color.parseColor(rightTextColor));
        keyboardImg.setImageTintList(ColorStateList.valueOf(Color.parseColor(LeftTextColor)));
        speakerText.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor(LeftTextColor)));
        speakerText.setTextColor(Color.parseColor(LeftTextColor));
        recAudioImg.setImageTintList(ColorStateList.valueOf(Color.parseColor(LeftTextColor)));
        ivAttachment.setImageTintList(ColorStateList.valueOf(Color.parseColor(LeftTextColor)));
        llSend.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(rightBgColor)));
        sendTv.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(rightTextColor)));
        rlSpeakerCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(rightBgColor)));

        colors = new int[]{Color.parseColor(rightTextColor), Color.parseColor(rightTextColor), Color.parseColor(rightTextColor)};
        progress.setColors(colors);
    }

    SharedPreferences getSharedPreferences() {
        return requireActivity().getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
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

    @Override
    public void setBottomOptionData(BotOptionsModel botOptionsModel) {
        this.botOptionsModel = botOptionsModel;
    }

    @Override
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

                if (SDKConfiguration.OverrideKoreConfig.showASRMicroPhone) recAudioImg.setVisibility(View.VISIBLE);

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
            animateLayoutGone(mainContentLayout);
            animateLayoutVisible(defaultFooterLayout);
            new Handler().postDelayed(() -> onMicButtonClick(), 500);
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

    private void initialSetUp() {
        mainContentLayout.setVisibility(View.GONE);
        animateLayoutGone(mainContentLayout);
        animateLayoutVisible(defaultFooterLayout);
    }

    @Override
    public boolean isTTSEnabled() {
        return isTTSEnabled;
    }

    @Override
    protected void onRecordAudioPermissionGranted() {
        stopTTS();
        Utility.hideVirtualKeyboard(requireActivity());
        speakerText.setVisibility(View.GONE);
        rlSpeakerCircle.setVisibility(View.VISIBLE);
        textViewSpeech.setVisibility(View.VISIBLE);
        textViewSpeech.setText("");

        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, CustomFooterFragment.this);
        } catch (SpeechRecognitionNotAvailable exc) {
            showSpeechNotSupportedDialog();

        } catch (GoogleVoiceTypingDisabledException exc) {
            showEnableGoogleVoiceTyping();
        }
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
        rlSpeakerCircle.setVisibility(View.GONE);
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

    @Override
    public void setComposeText(String text) {
        editTextMessage.setText(text);
        editTextMessage.setSelection(text.length());
    }

    /**
     * this method update the ui of send button based on enable/disable
     */
    @Override
    public void enableOrDisableSendButton(boolean enable) {
        if (composebarAttachmentAdapter.getItemCount() > 0 || enable) {
            llSend.setVisibility(View.VISIBLE);
            recAudioImg.setVisibility(View.GONE);
        } else {
            if (SDKConfiguration.OverrideKoreConfig.showASRMicroPhone)
                recAudioImg.setVisibility(View.VISIBLE);

            llSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void addAttachmentToAdapter(HashMap<String, String> attachmentKey) {
        attachmentRecycler.setVisibility(View.VISIBLE);
        composebarAttachmentAdapter.addAttachment(attachmentKey);
        if (composebarAttachmentAdapter.getItemCount() > 0) {
            enableOrDisableSendButton(true);
        }
    }
}