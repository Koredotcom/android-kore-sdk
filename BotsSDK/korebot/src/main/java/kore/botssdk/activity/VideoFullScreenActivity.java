package kore.botssdk.activity;

import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.VideoTimerEvent;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class VideoFullScreenActivity extends BotAppCompactActivity
{
    VideoView vvFullScreen;
    String videoUrl;
    ImageView ivPlayPauseIcon;
    TextView tvVideoTiming;
    SeekBar sbVideo;
    double current_pos, total_duration;
    PopupWindow popupWindow;
    ImageView ivVideoMore;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_screen);
        KoreEventCenter.register(this);
        vvFullScreen = findViewById(R.id.vvFullVideo);
        ivPlayPauseIcon = findViewById(R.id.ivPlayPauseIcon);
        ImageView ivFullScreen = findViewById(R.id.ivFullScreen);
        sbVideo = findViewById(R.id.sbVideo);
        tvVideoTiming = findViewById(R.id.tvVideoTiming);
        ivVideoMore = findViewById(R.id.ivVideoMore);

        View popUpView = View.inflate(VideoFullScreenActivity.this, R.layout.theme_change_layout, null);
        TextView tvTheme1 = popUpView.findViewById(R.id.tvTheme1);
        TextView tvTheme2 = popUpView.findViewById(R.id.tvTheme2);
        View vTheme = popUpView.findViewById(R.id.vTheme);
        tvTheme1.setText(getText(R.string.download));
        tvTheme2.setVisibility(View.GONE);
        vTheme.setVisibility(View.GONE);

        KaMediaUtils.updateExternalStorageState();
        KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_VIDEO, "");
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        videoUrl = Objects.requireNonNull(getIntent().getExtras()).getString("VideoUrl");

        if(!StringUtils.isNullOrEmpty(videoUrl))
        {
            vvFullScreen.setVideoPath(videoUrl);

            vvFullScreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    setVideoProgress();

                    if(getIntent().hasExtra("CurrentPosition"))
                    {
                        current_pos = Objects.requireNonNull(getIntent().getExtras()).getDouble("CurrentPosition");
                        sbVideo.setProgress((int)current_pos);
                        vvFullScreen.seekTo((int)current_pos);
                    }

                    vvFullScreen.start();
                    ivPlayPauseIcon.setTag(false);
                }
            });
        }

        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoTimerEvent event = new VideoTimerEvent();
                event.setCurrentPos(current_pos);
                KoreEventCenter.post(event);
                finish();
            }
        });

        ivPlayPauseIcon.setTag(true);
        ivPlayPauseIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((boolean)v.getTag())
                {
                    ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, getTheme()));
                    vvFullScreen.start();
                    v.setTag(false);
                }
                else
                {
                    ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, getTheme()));
                    vvFullScreen.pause();
                    v.setTag(true);
                }
            }
        });

        ivVideoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(ivVideoMore, Gravity.BOTTOM|Gravity.END, 80, 400);
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
                    KaMediaUtils.saveFileFromUrlToKorePath(VideoFullScreenActivity.this, videoUrl);
                }
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                VideoTimerEvent event = new VideoTimerEvent();
                event.setCurrentPos(current_pos);
                KoreEventCenter.post(event);
                finish();
            }
        });
    }

    // display video progress
    public void setVideoProgress()
    {
        //get the video duration
        current_pos = vvFullScreen.getCurrentPosition();
        total_duration = vvFullScreen.getDuration();

        sbVideo.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    current_pos = vvFullScreen.getCurrentPosition();
                    String str = StringUtils.timeConversion((long) current_pos)+"/"+StringUtils.timeConversion((long) total_duration);
                    tvVideoTiming.setText(str);
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
                vvFullScreen.seekTo((int) current_pos);
            }
        });
    }

    public void onEvent(double currentPos)
    {
        Log.e("Current Position", String.valueOf(currentPos));
    }

    boolean checkForPermissionAccessAndRequest()
    {
        if (KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return true;
        }
        else
        {
            KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*,Manifest.permission.RECORD_AUDIO*/)) {
                KaMediaUtils.saveFileFromUrlToKorePath(VideoFullScreenActivity.this, videoUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
