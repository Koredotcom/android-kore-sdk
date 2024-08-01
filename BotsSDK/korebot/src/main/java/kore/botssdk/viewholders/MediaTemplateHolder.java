package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
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
import android.view.MotionEvent;
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
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class MediaTemplateHolder extends BaseViewHolderNew {
    private final ImageView ivImage;
    private final TextView tvFileName, tvAudioVideoTiming;
    private SharedPreferences sharedPreferences;
    float dp1;
    private final VideoView vvAttachment;
    private final ImageView ivPlayPauseIcon;
    private double current_pos, total_duration;
    private double current_audio_pos, total_audio_duration;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_media, parent, false);
        return new MediaTemplateHolder(view);
    }

    private MediaTemplateHolder(@NonNull View view) {
        super(view, view.getContext());
        LinearLayout llAttachment = view.findViewById(R.id.llAttachment);
        ivImage = view.findViewById(R.id.ivImage);
        tvFileName = view.findViewById(R.id.tvFileName);
        dp1 = DimensionUtil.dp1;
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

        View popUpView = LayoutInflater.from(view.getContext()).inflate(R.layout.theme_change_layout, null);
        tvTheme1 = popUpView.findViewById(R.id.tvTheme1);
        TextView tvTheme2 = popUpView.findViewById(R.id.tvTheme2);
        View vTheme = popUpView.findViewById(R.id.vTheme);
        tvTheme1.setText(R.string.download);
        tvTheme2.setVisibility(GONE);
        vTheme.setVisibility(GONE);

        KaMediaUtils.updateExternalStorageState();
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

        ivVideoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(ivVideoMore, -250, -250);
            }
        });

        ivAudioMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(ivAudioMore, -250, -250);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadOuter payloadOuter = getPayloadOuter(baseBotMessage);
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;

        switch (payloadOuter.getType()) {
            case BundleConstants.MEDIA_TYPE_IMAGE:
                llAudio.setVisibility(GONE);
                rlVideo.setVisibility(GONE);
                vvAttachment.setVisibility(GONE);
                ivImage.setVisibility(VISIBLE);
                tvVideoTitle.setVisibility(GONE);
                tvFileName.setVisibility(GONE);

                if (!StringUtils.isNullOrEmpty(payloadInner.getText())) {
                    tvVideoTitle.setVisibility(VISIBLE);
                    tvVideoTitle.setText(payloadInner.getText());
                }

                if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                    tvFileName.setVisibility(VISIBLE);
                    String fileName = payloadInner.getUrl().substring(payloadInner.getUrl().lastIndexOf("/") + 1);

                    if (!StringUtils.isNullOrEmpty(fileName)) {
                        tvFileName.setText(fileName);
                    }

                    Glide.with(itemView.getContext())
                            .load(payloadInner.getUrl())
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .error(R.drawable.ic_image_photo)
                            .into(new DrawableImageViewTarget(ivImage));
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
                        e.printStackTrace();
                    }
                } else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl())) {
                    try {
                        Uri uri = Uri.parse(payloadInner.getUrl());
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(itemView.getContext(), uri);
                        player.prepareAsync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                tvFileName.setVisibility(VISIBLE);

                if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                    tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getVideoUrl()));
                else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                    tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getUrl()));

                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        setAudioProgress();
                    }
                });

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        player.seekTo(1);
                        ivAudioPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_paly_black, itemView.getContext().getTheme()));
                        ivAudioPlayPauseIcon.setTag(true);
                    }
                });

                ivAudioPlayPauseIcon.setTag(true);
                ivAudioPlayPauseIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((boolean) v.getTag()) {
                            ivAudioPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_pause_black, itemView.getContext().getTheme()));
                            try {
                                player.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            v.setTag(false);
                        } else {
                            ivAudioPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_paly_black, itemView.getContext().getTheme()));
                            try {
                                player.pause();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            v.setTag(true);
                        }
                    }
                });

                tvTheme1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        if (checkForPermissionAccessAndRequest()) {
                            KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_AUDIO, "");
                            if (!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl()))
                                KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getAudioUrl());
                            else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getUrl());
                        } else if (composeFooterInterface != null) {
                            if (!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl()))
                                composeFooterInterface.externalReadWritePermission(payloadInner.getAudioUrl());
                            else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                composeFooterInterface.externalReadWritePermission(payloadInner.getUrl());
                        }
                    }
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

                vvAttachment.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        setVideoProgress();
                    }
                });

                if (payloadInner.getVideoCurrentPosition() > 0)
                    setVideoPosition(payloadInner.getVideoCurrentPosition());

                tvFileName.setVisibility(VISIBLE);

                if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                    tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getVideoUrl()));
                else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                    tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getUrl()));

                vvAttachment.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        vvAttachment.seekTo(1);
                        ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.getContext().getResources(), R.drawable.ic_play_icon, itemView.getContext().getTheme()));
                        ivPlayPauseIcon.setTag(true);
                    }
                });

                vvAttachment.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });

                ivPlayPauseIcon.setTag(true);
                ivPlayPauseIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                    }
                });

                vvAttachment.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        llPlayControls.setVisibility(VISIBLE);
                        hideToolBar();
                        return false;
                    }
                });

                ivFullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(itemView.getContext(), VideoFullScreenActivity.class);
                        if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                            intent.putExtra("VideoUrl", payloadInner.getVideoUrl());
                        else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                            intent.putExtra("VideoUrl", payloadInner.getUrl());

                        intent.putExtra("CurrentPosition", current_pos);
                        itemView.getContext().startActivity(intent);

                    }
                });

                tvTheme1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        if (checkForPermissionAccessAndRequest()) {
                            KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_VIDEO, "");
                            if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                                KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getVideoUrl());
                            else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                KaMediaUtils.saveFileFromUrlToKorePath(itemView.getContext(), payloadInner.getUrl());
                        } else if (composeFooterInterface != null) {
                            if (!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                                composeFooterInterface.externalReadWritePermission(payloadInner.getVideoUrl());
                            else if (!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                composeFooterInterface.externalReadWritePermission(payloadInner.getUrl());
                        }
                    }
                });
                break;
        }
    }

    private SharedPreferences getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    private void hideToolBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llPlayControls.setVisibility(GONE);

                if (popupWindow != null)
                    popupWindow.dismiss();
            }
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
        current_pos = vvAttachment.getCurrentPosition();
        total_duration = vvAttachment.getDuration();

        sbVideo.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = vvAttachment.getCurrentPosition();
                    tvVideoTiming.setText(StringUtils.timeConversion((long) current_pos) + "/" + StringUtils.timeConversion((long) total_duration));
                    sbVideo.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
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
                current_pos = seekBar.getProgress();
                vvAttachment.seekTo((int) current_pos);
            }
        });
    }

    private boolean checkForPermissionAccessAndRequest() {
        return KaPermissionsHelper.hasPermission(itemView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    // display video progress
    public void setAudioProgress() {
        //get the video duration
        current_audio_pos = player.getCurrentPosition();
        total_audio_duration = player.getDuration();

        sbAudioVideo.setMax((int) total_audio_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_audio_pos = player.getCurrentPosition();
                    tvAudioVideoTiming.setText(StringUtils.timeConversion((long) current_audio_pos) + "/" + StringUtils.timeConversion((long) total_audio_duration));
                    sbAudioVideo.setProgress((int) current_audio_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
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
                current_audio_pos = seekBar.getProgress();
                player.seekTo((int) current_audio_pos);
            }
        });
    }
}
