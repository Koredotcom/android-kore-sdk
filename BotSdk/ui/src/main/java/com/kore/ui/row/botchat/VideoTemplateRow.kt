package com.kore.ui.row.botchat

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kore.common.constants.MediaConstants
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.common.utils.LogUtils
import com.kore.ui.utils.MediaUtils
import com.kore.common.utils.StringUtils
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.model.constants.BotResponseConstants.VIDEO_CURRENT_POSITION
import com.kore.model.constants.BotResponseConstants.VIDEO_URL
import com.kore.ui.R
import com.kore.ui.databinding.VideoTemplateViewBinding
import com.kore.ui.utils.DownloadUtils

class VideoTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    private val handler = Handler(Looper.getMainLooper())
    override val type: SimpleListRowType =
        BotChatRowType.getRowType(BotChatRowType.ROW_VIDEO_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is VideoTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is VideoTemplateRow) return false
        return otherRow.payload == payload
    }

    private fun setVideoPosition(currentPosition: Int, sbVideo: SeekBar, videoView: VideoView) {
        sbVideo.progress = currentPosition
        videoView.seekTo(currentPosition)
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = VideoTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            var currentPosition = 0
            var totalDuration: Int
            tvVideoTitle.isVisible = payload[BotResponseConstants.KEY_TEXT] != null
            payload[BotResponseConstants.KEY_TEXT]?.let { tvVideoTitle.text = it.toString() }
            val videoUrl = payload[VIDEO_URL] ?: payload[URL]
            videoUrl?.let {
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                Glide.with(binding.root.context)
                    .load(it.toString())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivVideoThumbNail)

                videoView.setVideoPath(it.toString())
                sbVideo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, p1: Int, p2: Boolean) {
                        if (seekBar.isPressed) {
                            videoView.seekTo(p1)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                })
            }

            videoView.setOnClickListener {
                handler.removeCallbacksAndMessages(null)
                llPlayControls.isVisible = true
                if (videoView.isPlaying) hideToolBar(llPlayControls)
            }

            //Add the listeners
            videoView.setOnCompletionListener {
                Toast.makeText(root.context, "Play finished", Toast.LENGTH_LONG).show()
                ivPlayPauseIcon.isSelected = false
                handler.removeCallbacksAndMessages(null)
                llPlayControls.isVisible = true
            }

            videoView.setOnErrorListener { _, _, _ ->
                LogUtils.e("VideoTemplateRow", "ERROR")
                ivPlayPauseIcon.isSelected = false
                handler.removeCallbacksAndMessages(null)
                llPlayControls.isVisible = true
                true
            }

            ivFullScreen.setOnClickListener {
                payload.putIfAbsent(VIDEO_CURRENT_POSITION, currentPosition)
                videoUrl?.let { actionEvent(BotChatEvent.UrlClick(it.toString())) }
                if (ivPlayPauseIcon.isSelected) ivPlayPauseIcon.performClick()
            }

            ivPlayPauseIcon.setOnClickListener { view ->
                view.isSelected = !view.isSelected
                if (view.isSelected) {
                    if (payload[VIDEO_CURRENT_POSITION] != null && (payload[VIDEO_CURRENT_POSITION] as Int) > 0) {
                        setVideoPosition(payload[VIDEO_CURRENT_POSITION] as Int, sbVideo, videoView)
                    }
                    ivVideoThumbNail.isVisible = false
                    videoView.start()
                    hideToolBar(llPlayControls)
                } else {
                    ivVideoThumbNail.isVisible = true
                    videoView.pause()
                }
            }

            videoView.setOnPreparedListener {
                sbVideo.max = videoView.duration
                totalDuration = videoView.duration

                val handler = Handler(Looper.getMainLooper())

                val runnable: Runnable = object : Runnable {
                    override fun run() {
                        try {
                            currentPosition = videoView.currentPosition
                            val stringBuilder: StringBuilder = java.lang.StringBuilder()
                            val append = stringBuilder.append(StringUtils.timeConversion(currentPosition.toLong()))
                                .append("/")
                                .append(StringUtils.timeConversion(totalDuration.toLong()))

                            payload[VIDEO_CURRENT_POSITION] = currentPosition
                            tvVideoTiming.text = append
                            sbVideo.progress = currentPosition
                            handler.postDelayed(this, 1000)
                        } catch (ed: IllegalStateException) {
                            ed.printStackTrace()
                        }
                    }
                }
                handler.postDelayed(runnable, 1000)
            }

            ivVideoMore.setOnClickListener {
                videoUrl?.let { url ->
                    if (url.toString().isNotEmpty()) showMenuPopup(root.context, ivVideoMore, url.toString())
                }
            }
        }
    }

    private fun showMenuPopup(context: Context, anchorView: View, url: String?) {
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
            if (!url.isNullOrEmpty()) {
                DownloadUtils.downloadFile(tvTheme1.context, url, null)
//                actionEvent(BotChatEvent.DownloadLink(id, url, StringUtils.getFileNameFromUrl(url)))
            }
        }
        popupWindow.showAsDropDown(anchorView, -40, 0)
    }

    private fun hideToolBar(view: ViewGroup) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            view.isVisible = false
        }, 5000)
    }
}