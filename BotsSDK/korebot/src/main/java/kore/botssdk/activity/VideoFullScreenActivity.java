package kore.botssdk.activity;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;
import com.kore.ai.widgetsdk.events.EntityEditEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import io.socket.client.On;
import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.ProfileColorUpdateEvent;
import kore.botssdk.events.VideoTimerEvent;
import kore.botssdk.exceptions.NoExternalStorageException;
import kore.botssdk.exceptions.NoWriteAccessException;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.utils.AppPermissionsHelper;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.ToastUtils;

import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST;

public class VideoFullScreenActivity extends BotAppCompactActivity
{
    private VideoView vvFullScreen;
    private String videoUrl;
    private ImageView ivPlayPauseIcon, ivFullScreen;
    private TextView tvVideoTiming;
    private SeekBar sbVideo;
    private double current_pos, total_duration;
    private PopupWindow popupWindow;
    private View popUpView;
    private ImageView ivVideoMore;
    private TextView tvTheme1, tvTheme2;
    private View vTheme;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_screen);
        KoreEventCenter.register(this);
        vvFullScreen = (VideoView) findViewById(R.id.vvFullVideo);
        ivPlayPauseIcon = (ImageView) findViewById(R.id.ivPlayPauseIcon);
        ivFullScreen = (ImageView) findViewById(R.id.ivFullScreen);
        sbVideo = (SeekBar) findViewById(R.id.sbVideo);
        tvVideoTiming = (TextView) findViewById(R.id.tvVideoTiming);
        ivVideoMore = (ImageView) findViewById(R.id.ivVideoMore);

        popUpView = LayoutInflater.from(VideoFullScreenActivity.this).inflate(R.layout.theme_change_layout, null);
        tvTheme1 = (TextView) popUpView.findViewById(R.id.tvTheme1);
        tvTheme2 = (TextView) popUpView.findViewById(R.id.tvTheme2);
        vTheme = (View) popUpView.findViewById(R.id.vTheme);
        tvTheme1.setText("Download");
        tvTheme2.setVisibility(View.GONE);
        vTheme.setVisibility(View.GONE);

        KaMediaUtils.updateExternalStorageState();
        KaMediaUtils.setupAppDir(BundleConstants.MEDIA_TYPE_VIDEO, "");
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        videoUrl = getIntent().getExtras().getString("VideoUrl");
        vvFullScreen.setVideoURI(Uri.parse(videoUrl));

        vvFullScreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();

                if(getIntent().hasExtra("CurrentPosition"))
                {
                    current_pos = getIntent().getExtras().getDouble("CurrentPosition");
                    sbVideo.setProgress((int)current_pos);
                    vvFullScreen.seekTo((int)current_pos);
                }

                vvFullScreen.start();
                ivPlayPauseIcon.setTag(false);
            }
        });

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
                    ivPlayPauseIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_icon));
                    vvFullScreen.start();
                    v.setTag(false);
                }
                else
                {
                    ivPlayPauseIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_icon));
                    vvFullScreen.pause();
                    v.setTag(true);
                }
            }
        });

        ivVideoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(ivVideoMore, Gravity.BOTTOM|Gravity.RIGHT, 80, 400);
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
//                    new DownloadFileFromURL().execute(videoUrl);
                }
            }
        });
    }

    // display video progress
    public void setVideoProgress()
    {
        //get the video duration
        current_pos = vvFullScreen.getCurrentPosition();
        total_duration = vvFullScreen.getDuration();

        //display video duration
//        total.setText(timeConversion((long) total_duration));
//        current.setText(timeConversion((long) current_pos));
        sbVideo.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    current_pos = vvFullScreen.getCurrentPosition();
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
                vvFullScreen.seekTo((int) current_pos);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        VideoTimerEvent event = new VideoTimerEvent();
        event.setCurrentPos(current_pos);
        KoreEventCenter.post(event);
        super.onBackPressed();
    }

    public void onEvent(double currentPos)
    {
        Log.e("Current Position", currentPos+"");
    }

    private boolean checkForPermissionAccessAndRequest()
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         if(requestCode == CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST)
         {
             if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*,Manifest.permission.RECORD_AUDIO*/)) {
                 KaMediaUtils.saveFileFromUrlToKorePath(VideoFullScreenActivity.this, videoUrl);
             }
             else
             {
                 Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
             }
         }
    }
}
