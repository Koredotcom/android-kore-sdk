package com.kore.ui.botchat

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kore.common.constants.MediaConstants
import com.kore.common.utils.StringUtils
import com.kore.ui.BR
import com.kore.ui.R
import com.kore.ui.base.BaseActivity
import com.kore.ui.databinding.ActivityVideoFullViewBinding
import com.kore.ui.utils.MediaUtils

class VideoFullViewActivity: BaseActivity<ActivityVideoFullViewBinding, VideoFullView, VideoViewModel>(), VideoFullView {

    internal val videoViewModel: VideoViewModel by viewModels()

    override fun getLayoutID() = R.layout.activity_video_full_view

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): VideoViewModel = videoViewModel

    lateinit var videoUrl: String
    var currentPos: Double = 0.0
    var totalDuration: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoViewModel.init(this)
        binding.viewModel = videoViewModel;

        changeStatusBarColorWithHeight()

        videoUrl = (intent.extras?.get("VideoUrl")) as String

        if (videoUrl.isNotEmpty()) {
            binding.vvFullVideo.setVideoPath(videoUrl)

            binding.vvFullVideo.setOnPreparedListener { mp: MediaPlayer? ->
                setVideoProgress()
                if (intent.hasExtra("CurrentPosition")) {
                    currentPos = intent.extras?.getDouble("CurrentPosition") as Double
                    binding.sbVideo.progress = currentPos.toInt()
                    binding.vvFullVideo.seekTo(currentPos.toInt())
                }

                binding.vvFullVideo.start()
                binding.ivPlayPauseIcon.tag = false
            }
        }

        binding.ivFullScreen.setOnClickListener { v: View? ->
//            val event: VideoTimerEvent = VideoTimerEvent()
//            event.setCurrentPos(current_pos)
//            KoreEventCenter.post(event)
            finish()
        }

        binding.ivPlayPauseIcon.setTag(true)
        binding.ivPlayPauseIcon.setOnClickListener { v: View? ->
            if (v!!.tag as Boolean) {
                binding.ivPlayPauseIcon.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.ic_pause_icon,
                        getTheme()
                    )
                )
                binding.vvFullVideo.start()
                v.setTag(false)
            } else {
                binding.ivPlayPauseIcon.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.ic_play_icon,
                        getTheme()
                    )
                )
                binding.vvFullVideo.pause()
                v.tag = true
            }
        }

        binding.ivVideoMore.setOnClickListener { v: View? ->
//            popupWindow.showAtLocation(
//                ivVideoMore,
//                Gravity.BOTTOM or Gravity.END,
//                80,
//                400
//            )
            showMenuPopup(this@VideoFullViewActivity, binding.ivVideoMore, videoUrl)
        }

//        tvTheme1.setOnClickListener(View.OnClickListener { v: View? ->
//            popupWindow.dismiss()
//            if (checkForPermissionAccessAndRequest()) {
//                KaMediaUtils.saveFileFromUrlToKorePath(this@VideoFullScreenActivity, videoUrl)
//            }
//        })

        OnBackPressedDispatcher().addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                val event: VideoTimerEvent = VideoTimerEvent()
//                event.setCurrentPos(current_pos)
//                KoreEventCenter.post(event)
                finish()
            }
        })
    }

    // display video progress
    fun setVideoProgress() {
        //get the video duration
        currentPos = binding.vvFullVideo.getCurrentPosition().toDouble()
        totalDuration = binding.vvFullVideo.getDuration().toDouble()

        binding.sbVideo.setMax(totalDuration.toInt())
        val handler = Handler()

        val runnable: Runnable = object : Runnable {
            override fun run() {
                try {
                    currentPos = binding.vvFullVideo.getCurrentPosition().toDouble()
                    val str: String =
                        StringUtils.timeConversion(currentPos.toLong()) + "/" + StringUtils.timeConversion(
                            totalDuration.toLong()
                        )
                    binding.tvVideoTiming.text = str
                    binding.sbVideo.progress = currentPos.toInt()
                    handler.postDelayed(this, 1000)
                } catch (ed: IllegalStateException) {
                    Log.e("Video Error", "Failed to set video progress", ed)
                }
            }
        }
        handler.postDelayed(runnable, 1000)

        //seekbar change listener
        binding.sbVideo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                currentPos = seekBar.progress.toDouble()
                binding.vvFullVideo.seekTo(currentPos.toInt())
            }
        })
    }

    fun changeStatusBarColorWithHeight() {
        if (Build.VERSION.SDK_INT >= 35) {
            ViewCompat.setOnApplyWindowInsetsListener(
                binding.root
            ) { view: View?, windowInsets: WindowInsetsCompat? ->
                val insets = windowInsets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                view!!.setPadding(insets.left, insets.top, insets.right, insets.bottom)
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.black
                    )
                )
                WindowInsetsCompat.CONSUMED
            }
        } else {
            val window = getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(
                ContextCompat.getColor(
                    this@VideoFullViewActivity,
                    R.color.black
                )
            )
        }
    }

    private fun showMenuPopup(context: Context, anchorView: View, url: String) {
        val popUpView: View = LayoutInflater.from(context).inflate(R.layout.popup_theme_change, null)
        val tvTheme1 = popUpView.findViewById<TextView>(R.id.tvTheme1)
        val tvTheme2 = popUpView.findViewById<TextView>(R.id.tvTheme2)
        val vTheme = popUpView.findViewById<View>(R.id.vTheme)
        tvTheme1.setText(R.string.download)
        tvTheme2.visibility = View.GONE
        vTheme.visibility = View.GONE
        val popupWindow = PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        tvTheme1.setOnClickListener {
            popupWindow.dismiss()
            MediaUtils.setupAppDir(context, MediaConstants.MEDIA_TYPE_VIDEO)
            videoViewModel.downloadFile("123456", url, StringUtils.getFileNameFromUrl(url))
        }
        popupWindow.showAsDropDown(anchorView, -40, 0)
    }

    override fun onFileDownloadProgress(
        msgId: String,
        progress: Int,
        downloadedBytes: Int
    ) {
    }
}