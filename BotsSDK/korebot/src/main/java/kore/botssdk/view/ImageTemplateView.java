package kore.botssdk.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import kore.botssdk.R;
import kore.botssdk.activity.VideoFullScreenActivity;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.VideoTimerEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class ImageTemplateView extends LinearLayout
{
    private final Context mContext;
    private ImageView ivImage;
    private TextView tvFileName, tvAudioVideoTiming;
    private SharedPreferences sharedPreferences;
    private String leftbgColor;
    private GradientDrawable  leftDrawable;
    float dp1;
    private LinearLayout llAttachment;
    private VideoView vvAttachment, vvFullScreen;
    private MediaController mediacontroller;
    private ImageView ivPlayPauseIcon;
    private double current_pos, total_duration;
    private double current_audio_pos, total_audio_duration;
    private SeekBar sbVideo, sbAudioVideo;
    private TextView tvVideoTiming;
    private RelativeLayout rlVideo;
    private WebView wvAudio;
    private LinearLayout llAudio, llPlayControls;
    private ImageView ivAudioPlayPauseIcon, ivFullScreen, ivAudioMore;
    private final MediaPlayer player = new MediaPlayer();
    private PayloadInner payloadInner;
    private ImageView ivVideoMore;
    private PopupWindow popupWindow;
    private View popUpView;
    private TextView tvTheme1, tvTheme2, tvVideoTitle;
    private View vTheme;
    private String fileName;

    public ImageTemplateView(Context context)
    {
        super(context);
        this.mContext = context;
//        init();
    }

    public ImageTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
//        init();
    }

    public ImageTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
//        init();
    }

    private void init()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.image_template, this, true);
        KaFontUtils.applyCustomFont(getContext(), view);
        llAttachment = (LinearLayout) view.findViewById(R.id.llAttachment);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        tvFileName = (TextView) view.findViewById(R.id.tvFileName);
        dp1 = DimensionUtil.dp1;
        vvAttachment = (VideoView) view.findViewById(R.id.vvAttachment);
        ivPlayPauseIcon = (ImageView) view.findViewById(R.id.ivPlayPauseIcon);
        sbVideo = (SeekBar) view.findViewById(R.id.sbVideo);
        tvVideoTiming = (TextView) view.findViewById(R.id.tvVideoTiming);
        rlVideo = (RelativeLayout) view.findViewById(R.id.rlVideo);
        wvAudio = (WebView) view.findViewById(R.id.wvAudio);
        llAudio = (LinearLayout) view.findViewById(R.id.llAudio);
        ivAudioPlayPauseIcon = (ImageView) view.findViewById(R.id.ivAudioPlayPauseIcon);
        tvAudioVideoTiming = (TextView) view.findViewById(R.id.tvAudioVideoTiming);
        sbAudioVideo = (SeekBar) view.findViewById(R.id.sbAudioVideo);
        llPlayControls = (LinearLayout) view.findViewById(R.id.llPlayControls);
        ivFullScreen = (ImageView) view.findViewById(R.id.ivFullScreen);
        ivVideoMore = (ImageView) findViewById(R.id.ivVideoMore);
        ivAudioMore = (ImageView) findViewById(R.id.ivAudioMore);
        tvVideoTitle = (TextView) findViewById(R.id.tvVideoTitle);

        popUpView = LayoutInflater.from(getContext()).inflate(R.layout.theme_change_layout, null);
        tvTheme1 = (TextView) popUpView.findViewById(R.id.tvTheme1);
        tvTheme2 = (TextView) popUpView.findViewById(R.id.tvTheme2);
        vTheme = (View) popUpView.findViewById(R.id.vTheme);
        tvTheme1.setText("Download");
        tvTheme2.setVisibility(View.GONE);
        vTheme.setVisibility(View.GONE);

        KaMediaUtils.updateExternalStorageState();
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        KoreEventCenter.register(this);
        sharedPreferences = getSharedPreferences();
        leftbgColor= sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff");
        leftDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.theme1_left_bubble_bg);

        leftDrawable.setColor(Color.parseColor(leftbgColor));
        leftDrawable.setStroke((int) (1*dp1), Color.parseColor(leftbgColor));
        llAttachment.setBackground(leftDrawable);

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

    private boolean checkForPermissionAccessAndRequest()
    {
        return KaPermissionsHelper.hasPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }

    private SharedPreferences getSharedPreferences()
    {
        sharedPreferences = mContext.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;

    public void populateData(final PayloadInner payloadInner, String templateType)
    {
        if(payloadInner != null && templateType != null)
        {
            init();
            this.payloadInner = payloadInner;
            switch (templateType)
            {
                case BundleConstants.MEDIA_TYPE_IMAGE:
                    llAudio.setVisibility(GONE);
                    rlVideo.setVisibility(GONE);
                    vvAttachment.setVisibility(GONE);
                    ivImage.setVisibility(VISIBLE);
                    tvVideoTitle.setVisibility(GONE);
                    tvFileName.setVisibility(GONE);

                    if(!StringUtils.isNullOrEmpty(payloadInner.getText()))
                    {
                        tvVideoTitle.setVisibility(VISIBLE);
                        tvVideoTitle.setText(payloadInner.getText());
                    }

                    if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                    {
                        tvFileName.setVisibility(VISIBLE);
                        fileName = payloadInner.getUrl().substring(payloadInner.getUrl().lastIndexOf("/") + 1);

                        if(!StringUtils.isNullOrEmpty(fileName))
                        {
                            tvFileName.setText(fileName);
                        }

                        Glide.with(getContext())
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

                    if(!StringUtils.isNullOrEmpty(payloadInner.getText()))
                    {
                        tvVideoTitle.setVisibility(VISIBLE);
                        tvVideoTitle.setText(payloadInner.getText());
                    }

                    if(!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl()))
                    {
                        try
                        {
                            Uri uri = Uri.parse(payloadInner.getAudioUrl());
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(getContext(), uri);
                            player.prepareAsync();
                        } catch(Exception e) {
                            System.out.println(e);
                        }
                    }
                    else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                    {
                        try
                        {
                            Uri uri = Uri.parse(payloadInner.getUrl());
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(getContext(), uri);
                            player.prepareAsync();
                        } catch(Exception e) {
                            System.out.println(e);
                        }
                    }

                    tvFileName.setVisibility(VISIBLE);

                    if(!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                        tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getVideoUrl()));
                    else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
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
                            ivAudioPlayPauseIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_paly_black));
                            ivAudioPlayPauseIcon.setTag(true);
                        }
                    });

                    ivAudioPlayPauseIcon.setTag(true);
                    ivAudioPlayPauseIcon.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if((boolean)v.getTag())
                            {
                                ivAudioPlayPauseIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause_black));
                                try
                                {
                                    player.start();
                                } catch(Exception e) {
                                    System.out.println(e);
                                }
                                v.setTag(false);
                            }
                            else
                            {
                                ivAudioPlayPauseIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_paly_black));
                                try
                                {
                                    player.pause();
                                } catch(Exception e) {
                                    System.out.println(e);
                                }
                                v.setTag(true);
                            }
                        }
                    });

                    tvTheme1.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            popupWindow.dismiss();
                            if(checkForPermissionAccessAndRequest())
                            {
                                KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_AUDIO, "");
                                if(!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl()))
                                    KaMediaUtils.saveFileFromUrlToKorePath(getContext(), payloadInner.getAudioUrl());
                                else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                    KaMediaUtils.saveFileFromUrlToKorePath(getContext(), payloadInner.getUrl());
                            }
                            else if(composeFooterInterface != null)
                            {
                                if(!StringUtils.isNullOrEmpty(payloadInner.getAudioUrl()))
                                    composeFooterInterface.externalReadWritePermission(payloadInner.getAudioUrl());
                                else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
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

                    if(!StringUtils.isNullOrEmpty(payloadInner.getText()))
                    {
                        tvVideoTitle.setVisibility(VISIBLE);
                        tvVideoTitle.setText(payloadInner.getText());
                    }

                    if(!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                        vvAttachment.setVideoPath(payloadInner.getVideoUrl());
                    else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                        vvAttachment.setVideoPath(payloadInner.getUrl());

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)vvAttachment.getLayoutParams();
                    layoutParams.height=(int)(190 * dp1);
                    vvAttachment.setLayoutParams(layoutParams);
                    ivImage.setVisibility(GONE);
                    rlVideo.setVisibility(VISIBLE);
                    vvAttachment.setVisibility(VISIBLE);

                    vvAttachment.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            setVideoProgress(payloadInner);
                        }
                    });

                    if(payloadInner.getVideoCurrentPosition() > 0)
                        setVideoPosition(payloadInner.getVideoCurrentPosition());

                    tvFileName.setVisibility(VISIBLE);

                    if(!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                        tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getVideoUrl()));
                    else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                        tvFileName.setText(StringUtils.getFileNameFromUrl(payloadInner.getUrl()));

                    vvAttachment.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            vvAttachment.seekTo(1);
                            ivPlayPauseIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_play_icon));
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
                    ivPlayPauseIcon.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if((boolean)v.getTag())
                            {
                                ivPlayPauseIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause_icon));
                                vvAttachment.requestFocus();
                                vvAttachment.start();
                                v.setTag(false);
                            }
                            else
                            {
                                ivPlayPauseIcon.setImageDrawable(getContext().getDrawable(R.drawable.ic_play_icon));
                                vvAttachment.pause();
                                v.setTag(true);
                            }

                            hideToolBar();
                        }
                    });

                    vvAttachment.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event)
                        {
                            llPlayControls.setVisibility(VISIBLE);
                            hideToolBar();
                            return false;
                        }
                    });

                    ivFullScreen.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getContext(), VideoFullScreenActivity.class);
                            if(!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                                intent.putExtra("VideoUrl", payloadInner.getVideoUrl());
                            else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                intent.putExtra("VideoUrl", payloadInner.getUrl());

                            intent.putExtra("CurrentPosition", current_pos);
                            getContext().startActivity(intent);

                        }
                    });

                    tvTheme1.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            popupWindow.dismiss();
                            if(checkForPermissionAccessAndRequest())
                            {
                                KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_VIDEO, "");
                                if(!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                                    KaMediaUtils.saveFileFromUrlToKorePath(getContext(), payloadInner.getVideoUrl());
                                else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                    KaMediaUtils.saveFileFromUrlToKorePath(getContext(), payloadInner.getUrl());
                            }
                            else if(composeFooterInterface != null)
                            {
                                if(!StringUtils.isNullOrEmpty(payloadInner.getVideoUrl()))
                                    composeFooterInterface.externalReadWritePermission(payloadInner.getVideoUrl());
                                else if(!StringUtils.isNullOrEmpty(payloadInner.getUrl()))
                                    composeFooterInterface.externalReadWritePermission(payloadInner.getUrl());
                            }
                        }
                    });
                break;
            }
        }
    }

    private void setVideoPosition(double current_position)
    {
        sbVideo.setProgress((int)current_position);
        vvAttachment.seekTo((int)current_position);
    }

    public void onEvent(VideoTimerEvent event)
    {
        if(vvAttachment.getVisibility() == VISIBLE)
        {
            sbVideo.setProgress((int)event.getCurrentPos());
            vvAttachment.seekTo((int)event.getCurrentPos());
            ivAudioPlayPauseIcon.performClick();
        }
    }

    // display video progress
    public void setVideoProgress(PayloadInner payloadInner)
    {
        //get the video duration
        current_pos = vvAttachment.getCurrentPosition();
        total_duration = vvAttachment.getDuration();

        sbVideo.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    current_pos = vvAttachment.getCurrentPosition();
                    tvVideoTiming.setText(StringUtils.timeConversion((long) current_pos)+"/"+StringUtils.timeConversion((long) total_duration));
                    sbVideo.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
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

    @Override
    protected void onDetachedFromWindow()
    {
        if(payloadInner != null)
            payloadInner.setVideoCurrentPosition(current_pos);

        super.onDetachedFromWindow();
    }

    private void hideToolBar()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                llPlayControls.setVisibility(GONE);

                if(popupWindow != null)
                    popupWindow.dismiss();
            }
        }, 5000);
    }

    // display video progress
    public void setAudioProgress()
    {
        //get the video duration
        current_audio_pos = player.getCurrentPosition();
        total_audio_duration = player.getDuration();

        //display video duration
//        total.setText(timeConversion((long) total_duration));
//        current.setText(timeConversion((long) current_pos));
        sbAudioVideo.setMax((int) total_audio_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    current_audio_pos = player.getCurrentPosition();
                    tvAudioVideoTiming.setText(StringUtils.timeConversion((long) current_audio_pos)+"/"+StringUtils.timeConversion((long) total_audio_duration));
                    sbAudioVideo.setProgress((int) current_audio_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
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
