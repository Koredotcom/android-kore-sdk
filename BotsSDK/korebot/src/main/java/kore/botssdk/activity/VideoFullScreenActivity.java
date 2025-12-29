package kore.botssdk.activity;

import static kore.botssdk.utils.BundleConstants.CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CHOOSE_VIDEO_BUNDLED_PERMISSION_REQUEST;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
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
public class VideoFullScreenActivity extends BotAppCompactActivity {
    VideoView vvFullScreen;
    String videoUrl;
    ImageView ivPlayPauseIcon;
    TextView tvVideoTiming;
    SeekBar sbVideo;
    double current_pos, total_duration;
    PopupWindow popupWindow;
    ImageView ivVideoMore;
    private boolean wasPlayingBeforeOrientationChange = false;
    private Handler progressHandler;
    private boolean isRestoringFromConfigChange = false;
    private int savedVideoPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.video_full_screen);

        changeStatusBarColorWithHeight();

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

        KaMediaUtils.setupAppDir(this, BundleConstants.MEDIA_TYPE_VIDEO);
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        videoUrl = Objects.requireNonNull(getIntent().getExtras()).getString("VideoUrl");

        if (!StringUtils.isNullOrEmpty(videoUrl)) {
            vvFullScreen.setVideoPath(videoUrl);

            vvFullScreen.setOnPreparedListener(mp -> {
                setVideoProgress();

                // Check if we're restoring from configuration change first
                if (isRestoringFromConfigChange && savedVideoPosition > 0) {
                    vvFullScreen.seekTo(savedVideoPosition);

                    // Restore playing state
                    if (wasPlayingBeforeOrientationChange) {
                        vvFullScreen.start();
                        ivPlayPauseIcon.setTag(false);
                        ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, getTheme()));
                    } else {
                        vvFullScreen.pause();
                        ivPlayPauseIcon.setTag(true);
                        ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, getTheme()));
                    }

                    // Reset the restoration flag
                    isRestoringFromConfigChange = false;
                } else if (getIntent().hasExtra("CurrentPosition")) {
                    // Handle initial position from intent
                    current_pos = Objects.requireNonNull(getIntent().getExtras()).getDouble("CurrentPosition");
                    sbVideo.setProgress((int) current_pos);
                    vvFullScreen.seekTo((int) current_pos);
                    vvFullScreen.start();
                    ivPlayPauseIcon.setTag(false);
                } else {
                    // Start normally
                    vvFullScreen.start();
                    ivPlayPauseIcon.setTag(false);
                }
            });
        }

        ivFullScreen.setOnClickListener(v -> {
            VideoTimerEvent event = new VideoTimerEvent();
            event.setCurrentPos(current_pos);
            KoreEventCenter.post(event);
            finish();
        });

        ivPlayPauseIcon.setTag(true);
        ivPlayPauseIcon.setOnClickListener(v -> {
            if ((boolean) v.getTag()) {
                ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_icon, getTheme()));
                vvFullScreen.start();
                v.setTag(false);
            } else {
                ivPlayPauseIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_icon, getTheme()));
                vvFullScreen.pause();
                v.setTag(true);
            }
        });

        ivVideoMore.setOnClickListener(v -> popupWindow.showAtLocation(ivVideoMore, Gravity.BOTTOM | Gravity.END, 80, 400));

        tvTheme1.setOnClickListener(v -> {
            popupWindow.dismiss();
            if (checkForPermissionAccessAndRequest()) {
                KaMediaUtils.saveFileFromUrlToKorePath(VideoFullScreenActivity.this, videoUrl);
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
    public void setVideoProgress() {
        //get the video duration
        current_pos = vvFullScreen.getCurrentPosition();
        total_duration = vvFullScreen.getDuration();

        sbVideo.setMax((int) total_duration);
        if (progressHandler == null) {
            progressHandler = new Handler();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = vvFullScreen.getCurrentPosition();
                    String str = StringUtils.timeConversion((long) current_pos) + "/" + StringUtils.timeConversion((long) total_duration);
                    tvVideoTiming.setText(str);
                    sbVideo.setProgress((int) current_pos);
                    progressHandler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    Log.e("Video Error", "Failed to set video progress", ed);
                }
            }
        };
        progressHandler.postDelayed(runnable, 1000);

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
                vvFullScreen.seekTo((int) current_pos);
            }
        });
    }

    boolean checkForPermissionAccessAndRequest() {
        if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return true;
        } else {
            KaPermissionsHelper.requestForPermission(this, CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHOOSE_VIDEO_BUNDLED_PERMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                KaMediaUtils.saveFileFromUrlToKorePath(VideoFullScreenActivity.this, videoUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save critical video playback state for orientation changes
        if (vvFullScreen != null) {
            int currentPosition = vvFullScreen.getCurrentPosition();
            boolean isCurrentlyPlaying = vvFullScreen.isPlaying();

            outState.putInt("current_pos", currentPosition);
            outState.putDouble("total_duration", total_duration);
            outState.putBoolean("wasPlaying", isCurrentlyPlaying);
            outState.putInt("savedVideoPosition", currentPosition);
            outState.putBoolean("isRestoringFromConfigChange", true);

            Log.d("VideoFullScreen", "Saving instance state - Position: " + currentPosition + ", Playing: " + isCurrentlyPlaying);
        }
        outState.putString("videoUrl", videoUrl);
        outState.putBoolean("playPauseIconTag", ivPlayPauseIcon != null ? (Boolean) ivPlayPauseIcon.getTag() : true);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore video playback state after orientation changes
        current_pos = savedInstanceState.getInt("current_pos", 0);
        total_duration = savedInstanceState.getDouble("total_duration", 0);
        wasPlayingBeforeOrientationChange = savedInstanceState.getBoolean("wasPlaying", false);
        videoUrl = savedInstanceState.getString("videoUrl");
        boolean playPauseIconTag = savedInstanceState.getBoolean("playPauseIconTag", true);
        savedVideoPosition = savedInstanceState.getInt("savedVideoPosition", 0);
        isRestoringFromConfigChange = savedInstanceState.getBoolean("isRestoringFromConfigChange", false);

        if (ivPlayPauseIcon != null) {
            ivPlayPauseIcon.setTag(playPauseIconTag);
        }

        Log.d("VideoFullScreen", "Restoring instance state - Position: " + savedVideoPosition + ", Was playing: " + wasPlayingBeforeOrientationChange);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Handle orientation change without recreating the activity
        // Capture current video state before the VideoView potentially resets
        if (vvFullScreen != null) {
            savedVideoPosition = vvFullScreen.getCurrentPosition();
            wasPlayingBeforeOrientationChange = vvFullScreen.isPlaying();
            current_pos = savedVideoPosition;

            // Set flag to indicate we need to restore from config change
            isRestoringFromConfigChange = true;
        }
    }
}
