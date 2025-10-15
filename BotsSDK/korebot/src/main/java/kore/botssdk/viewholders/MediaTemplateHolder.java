package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static kore.botssdk.viewUtils.DimensionUtil.dp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import kore.botssdk.R;
import kore.botssdk.activity.VideoFullScreenActivity;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.VideoTimerEvent;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DownloadUtils;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;

public class MediaTemplateHolder extends BaseViewHolder {
    private final ImageView ivImage;
    private final TextView tvDownload;
    private final TextView tvAudioVideoTiming;
    private SharedPreferences sharedPreferences;
    private final VideoView vvAttachment;
    private final ImageView ivPlayPauseIcon;
    private double currentPos, totalDuration;
    private double currentAudioPos, totalAudioDuration;
    private final SeekBar sbVideo, sbAudioVideo;
    private final TextView tvVideoTiming;
    private final RelativeLayout rlVideo;
    private final LinearLayout llAudio, llPlayControls;
    private final ImageView ivAudioPlayPauseIcon, ivFullScreen, ivAudioMore;
    private final MediaPlayer player = new MediaPlayer();
    private final ImageView ivVideoMore;
    private final PopupWindow popupWindow;
    private final TextView tvTheme1;
    private final TextView tvVideoTitle;

    public static MediaTemplateHolder getInstance(ViewGroup parent) {
        return new MediaTemplateHolder(createView(R.layout.template_media, parent));
    }

    private MediaTemplateHolder(@NonNull View view) {
        super(view, view.getContext());
        LinearLayout llAttachment = view.findViewById(R.id.llAttachment);
        ivImage = view.findViewById(R.id.ivImage);
        tvDownload = view.findViewById(R.id.download);
        vvAttachment = view.findViewById(R.id.vvAttachment);
        ivPlayPauseIcon = view.findViewById(R.id.ivPlayPauseIcon);
        sbVideo = view.findViewById(R.id.sbVideo);
        tvVideoTiming = view.findViewById(R.id.tvVideoTiming);
        rlVideo = view.findViewById(R.id.rlVideo);
        llAudio = view.findViewById(R.id.llAudio);
        ivAudioPlayPauseIcon = view.findViewById(R.id.ivAudioPlayPauseIcon);
        tvAudioVideoTiming = view.findViewById(R.id.tvAudioVideoTiming);
        sbAudioVideo = view.findViewById(R.id.sbAudioVideo);
        llPlayControls = view.findViewById(R.id.llPlayControls);
        ivFullScreen = view.findViewById(R.id.ivFullScreen);
        ivVideoMore = view.findViewById(R.id.ivVideoMore);
        ivAudioMore = view.findViewById(R.id.ivAudioMore);
        tvVideoTitle = view.findViewById(R.id.tvVideoTitle);
        setRoundedCorner(tvDownload, 8f);

        View popUpView = LayoutInflater.from(view.getContext()).inflate(R.layout.theme_change_layout, null);
        tvTheme1 = popUpView.findViewById(R.id.tvTheme1);
        TextView tvTheme2 = popUpView.findViewById(R.id.tvTheme2);
        View vTheme = popUpView.findViewById(R.id.vTheme);
        tvTheme1.setText(R.string.download);
        tvTheme2.setVisibility(GONE);
        vTheme.setVisibility(GONE);

        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        KoreEventCenter.register(this);
        sharedPreferences = getSharedPreferences(view.getContext());
        String leftBgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff");
        GradientDrawable leftDrawable = (GradientDrawable) ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.theme1_left_bubble_bg, view.getContext().getTheme());

        if (leftDrawable != null) {
            leftDrawable.setColor(Color.parseColor(leftBgColor));
            leftDrawable.setStroke((int) (1 * dp1), Color.parseColor(leftBgColor));
            llAttachment.setBackground(leftDrawable);
        }

        ivVideoMore.setOnClickListener(v -> popupWindow.showAsDropDown(ivVideoMore, -20, 0));
        ivAudioMore.setOnClickListener(v -> popupWindow.showAsDropDown(ivAudioMore, -20, 0));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadOuter payloadOuter = getPayloadOuter(baseBotMessage);
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        tvDownload.setTextColor(Color.parseColor(SDKConfiguration.BubbleColors.quickReplyColor));

        switch (payloadOuter.getType()) {
            case BundleConstants.MEDIA_TYPE_IMAGE:
                llAudio.setVisibility(GONE);
                rlVideo.setVisibility(GONE);
                vvAttachment.setVisibility(GONE);
                ivImage.setVisibility(VISIBLE);
                tvVideoTitle.setVisibility(GONE);

                if (!StringUtils.isNullOrEmpty(payloadInner.getText())) {
                    tvVideoTitle.setVisibility(VISIBLE);
                    tvVideoTitle.setText(payloadInner.getText());
                }

                if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                    tvDownload.setVisibility(VISIBLE);
                    Glide.with(itemView.getContext())
                            .load(payloadInner.getUrl())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .error(R.drawable.ic_image_photo)
                            .into(new DrawableImageViewTarget(ivImage));
                    tvDownload.setOnClickListener(view -> {
                        DownloadUtils.downloadFile(view.getContext(), payloadInner.getUrl(), null);
                    });
                }
                break;
            case BundleConstants.MEDIA_TYPE_AUDIO:
                ivImage.setVisibility(GONE);
                rlVideo.setVisibility(GONE);
                vvAttachment.setVisibility(GONE);
                llAudio.setVisibility(VISIBLE);
                tvVideoTitle.setVisibility(GONE);

                if (!StringUtils.isNullOrEmpty(payloadInner.getText())) {
                    tvVideoTitle.setVisibility(VISIBLE);
                    tvVideoTitle.setText(payloadInner.getText());
                }

                if (!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl())) {
                    try {
                        Uri uri = Uri.parse(payloadInner.getAudioUrl());
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(itemView.getContext(), uri);
                        player.prepareAsync();
                    } catch (Exception e) {
                        LogUtils.e("Audio Player Pass", String.valueOf(e));
                    }
                } else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                    try {
                        Uri uri = Uri.parse(payloadInner.getUrl());
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(itemView.getContext(), uri);
                        player.prepareAsync();
                    } catch (Exception e) {
                        LogUtils.e("Audio Player Pass", String.valueOf(e));
                    }
                }

                player.setOnPreparedListener(mp -> setAudioProgress());

                player.setOnCompletionListener(mp -> {
                    player.seekTo(1);
                    ivAudioPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_paly_black, itemView.getContext().getTheme()));
                    ivAudioPlayPauseIcon.setTag(true);
                });

                ivAudioPlayPauseIcon.setTag(true);
                ivAudioPlayPauseIcon.setOnClickListener(v -> {
                    if ((boolean) v.getTag()) {
                        ivAudioPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_pause_black, itemView.getContext().getTheme()));
                        try {
                            player.start();
                        } catch (Exception e) {
                            LogUtils.e("Audio Player Pass", String.valueOf(e));
                        }
                        v.setTag(false);
                    } else {
                        ivAudioPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_paly_black, itemView.getContext().getTheme()));
                        try {
                            player.pause();
                        } catch (Exception e) {
                            LogUtils.e("Audio Player Pass", String.valueOf(e));
                        }
                        v.setTag(true);
                    }
                });

                tvTheme1.setOnClickListener(v -> {
                    popupWindow.dismiss();
//                    KaMediaUtils.setupAppDir(itemView.getContext(), BundleConstants.MEDIA_TYPE_AUDIO);
                    if (!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl()))
                        DownloadUtils.downloadFile(itemView.getContext(), payloadInner.getAudioUrl(), null);
//                        KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getAudioUrl());
                    else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                        DownloadUtils.downloadFile(itemView.getContext(), payloadInner.getUrl(), null);
//                        KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getUrl());
                });

                break;
            case BundleConstants.MEDIA_TYPE_VIDEO:
                ivImage.setVisibility(GONE);
                rlVideo.setVisibility(VISIBLE);
                vvAttachment.setVisibility(VISIBLE);
                llAudio.setVisibility(GONE);
                tvVideoTitle.setVisibility(GONE);

                if (!StringUtils.isNullOrEmpty(payloadInner.getText())) {
                    tvVideoTitle.setVisibility(VISIBLE);
                    tvVideoTitle.setText(payloadInner.getText());
                }

                if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                    vvAttachment.setVideoPath(payloadInner.getVideoUrl());
                else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                    vvAttachment.setVideoPath(payloadInner.getUrl());

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vvAttachment.getLayoutParams();
                layoutParams.height = (int) (190 * dp1);
                vvAttachment.setLayoutParams(layoutParams);
                ivImage.setVisibility(GONE);
                rlVideo.setVisibility(VISIBLE);
                vvAttachment.setVisibility(VISIBLE);

                vvAttachment.setOnPreparedListener(mp -> setVideoProgress());

                if (payloadInner.getVideoCurrentPosition() > 0)
                    setVideoPosition(payloadInner.getVideoCurrentPosition());

                vvAttachment.setOnCompletionListener(mp -> {
                    vvAttachment.seekTo(1);
                    ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_play_icon, itemView.getContext().getTheme()));
                    ivPlayPauseIcon.setTag(true);
                });

                vvAttachment.setOnErrorListener((mp, what, extra) -> false);

                ivPlayPauseIcon.setTag(true);
                ivPlayPauseIcon.setOnClickListener(v -> {
                    if ((boolean) v.getTag()) {
                        ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_pause_icon, itemView.getContext().getTheme()));
                        vvAttachment.requestFocus();
                        vvAttachment.start();
                        v.setTag(false);
                    } else {
                        ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_play_icon, itemView.getContext().getTheme()));
                        vvAttachment.pause();
                        v.setTag(true);
                    }

                    hideToolBar();
                });

                vvAttachment.setOnTouchListener((v, event) -> {
                    llPlayControls.setVisibility(VISIBLE);
                    hideToolBar();
                    return false;
                });

                ivFullScreen.setOnClickListener(v -> {

                    Intent intent = new Intent(itemView.getContext(), VideoFullScreenActivity.class);
                    if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                        intent.putExtra("VideoUrl", payloadInner.getVideoUrl());
                    else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                        intent.putExtra("VideoUrl", payloadInner.getUrl());

                    intent.putExtra("CurrentPosition", currentPos);
                    itemView.getContext().startActivity(intent);

                });

                tvTheme1.setOnClickListener(v -> {
                    popupWindow.dismiss();
//                    KaMediaUtils.setupAppDir(itemView.getContext(), BundleConstants.MEDIA_TYPE_VIDEO);
                    if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                        DownloadUtils.downloadFile(itemView.getContext(), payloadInner.getVideoUrl(), null);
//                        KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getVideoUrl());
                    else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                        DownloadUtils.downloadFile(itemView.getContext(), payloadInner.getUrl(), null);
//                        KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getUrl());
                });
                break;
        }
    }

    private SharedPreferences getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    private void hideToolBar() {
        new Handler().postDelayed(() -> {
            llPlayControls.setVisibility(GONE);

            if (popupWindow != null)
                popupWindow.dismiss();
        }, 5000);
    }

    private void setVideoPosition(double current_position) {
        sbVideo.setProgress((int) current_position);
        vvAttachment.seekTo((int) current_position);
    }

    public void onEvent(VideoTimerEvent event) {
        if (vvAttachment.getVisibility() == VISIBLE) {
            sbVideo.setProgress((int) event.getCurrentPos());
            vvAttachment.seekTo((int) event.getCurrentPos());
            ivAudioPlayPauseIcon.performClick();
        }
    }

    // display video progress
    public void setVideoProgress() {
        //get the video duration
        currentPos = vvAttachment.getCurrentPosition();
        totalDuration = vvAttachment.getDuration();

        sbVideo.setMax((int) totalDuration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    currentPos = vvAttachment.getCurrentPosition();
                    String video_timing = StringUtils.timeConversion((long) currentPos) + "/" + StringUtils.timeConversion((long) totalDuration);
                    tvVideoTiming.setText(video_timing);
                    sbVideo.setProgress((int) currentPos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    LogUtils.e("Video Progress", String.valueOf(ed));
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listener
        sbVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentPos = seekBar.getProgress();
                vvAttachment.seekTo((int) currentPos);
            }
        });
    }

    // display video progress
    public void setAudioProgress() {
        //get the video duration
        currentAudioPos = player.getCurrentPosition();
        totalAudioDuration = player.getDuration();

        sbAudioVideo.setMax((int) totalAudioDuration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    currentAudioPos = player.getCurrentPosition();
                    String video_timing = StringUtils.timeConversion((long) currentAudioPos) + "/" + StringUtils.timeConversion((long) totalAudioDuration);
                    tvAudioVideoTiming.setText(video_timing);
                    sbAudioVideo.setProgress((int) currentAudioPos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    LogUtils.e("Audio Progress", String.valueOf(ed));
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listener
        sbAudioVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentAudioPos = seekBar.getProgress();
                player.seekTo((int) currentAudioPos);
            }
        });
    }
}