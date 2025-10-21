package com.kore.ui.row.botchat

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
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
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.constants.MediaConstants
import com.kore.common.event.UserActionEvent
import com.kore.common.utils.StringUtils
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.COMPONENT_TYPE_AUDIO
import com.kore.model.constants.BotResponseConstants.COMPONENT_TYPE_IMAGE
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.databinding.ImageTemplateViewBinding
import com.kore.ui.row.SimpleListRow
import com.kore.ui.utils.DownloadUtils
import com.kore.ui.utils.MediaUtils

class ImageTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val tempType: String,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_IMAGE_PROVIDER)
    private val preferenceRepository = PreferenceRepositoryImpl()

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ImageTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ImageTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = ImageTemplateViewBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            ivImage.isVisible = tempType == COMPONENT_TYPE_IMAGE
            llAudio.isVisible = tempType == COMPONENT_TYPE_AUDIO
            download.isVisible = tempType == COMPONENT_TYPE_IMAGE
            tvVideoTitle.isVisible = payload[KEY_TEXT] != null
            payload[KEY_TEXT]?.let { tvVideoTitle.text = it.toString() }

            when (tempType) {
                COMPONENT_TYPE_IMAGE -> {
                    download.setTextColor(
                        preferenceRepository.getStringValue(download.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5").toColorInt()
                    )
                    Glide.with(root.context)
                        .load(payload[BotResponseConstants.URL])
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .error(R.drawable.ic_image_photo)
                        .into<DrawableImageViewTarget>(DrawableImageViewTarget(ivImage))
                    download.setRoundedCorner(8f)
                    download.setOnClickListener {
                        val imageUrl = payload[BotResponseConstants.URL] as String?
                        if (!imageUrl.isNullOrEmpty())
                            DownloadUtils.downloadFile(download.context, imageUrl, null)
                        //actionEvent(BotChatEvent.UrlClick(imageUrl.toString()))
                    }
                }

                COMPONENT_TYPE_AUDIO -> {
                    val player = MediaPlayer()

                    val audioUrl =
                        when ((payload[BotResponseConstants.AUDIO_URL] as String).isNotEmpty()) {
                            true -> payload[BotResponseConstants.AUDIO_URL] as String
                            else -> payload[BotResponseConstants.URL] as String
                        }

                    try {
                        val uri = audioUrl.toUri()
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
                        player.setDataSource(root.context, uri)
                        player.prepareAsync()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    player.setOnCompletionListener {
                        it.seekTo(1)
                        ivAudioPlayPauseIcon.isSelected = false
                    }

                    player.setOnErrorListener { _, _, _ ->
                        ivAudioPlayPauseIcon.isSelected = false
                        true
                    }

                    player.setOnPreparedListener {
                        var currentAudioPos: Double
                        val totalAudioDuration = it.duration.toDouble()
                        sbAudioVideo.max = totalAudioDuration.toInt()
                        val handler = Handler(Looper.getMainLooper())
                        val runnable: Runnable = object : Runnable {
                            override fun run() {
                                try {
                                    currentAudioPos = it.currentPosition.toDouble()
                                    val duration = StringBuffer()
                                    duration.append(StringUtils.timeConversion(currentAudioPos.toLong()))
                                        .append("/").append(StringUtils.timeConversion(totalAudioDuration.toLong()))
                                    tvAudioVideoTiming.text = duration
                                    sbAudioVideo.progress = currentAudioPos.toInt()
                                    handler.postDelayed(this, 100)
                                } catch (ed: IllegalStateException) {
                                    ed.printStackTrace()
                                }
                            }
                        }
                        handler.postDelayed(runnable, 1000)

                        //seekbar change listener
                        sbAudioVideo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
                            override fun onStartTrackingTouch(seekBar: SeekBar) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar) {
                                currentAudioPos = seekBar.progress.toDouble()
                                player.seekTo(currentAudioPos.toInt())
                            }
                        })
                    }

                    ivAudioPlayPauseIcon.setOnClickListener { view ->
                        view.isSelected = !view.isSelected
                        if (view.isSelected) {
                            try {
                                player.start()
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            try {
                                player.pause()
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    ivAudioMore.setOnClickListener { showMenuPopup(root.context, ivAudioMore, audioUrl) }
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
            MediaUtils.setupAppDir(context, MediaConstants.MEDIA_TYPE_AUDIO)
            if (!url.isNullOrEmpty()) {
                DownloadUtils.downloadFile(tvTheme1.context, url, null)
//                actionEvent(BotChatEvent.DownloadLink(id, url, StringUtils.getFileNameFromUrl(url)))
            }
        }

        popupWindow.showAsDropDown(anchorView, -40, 0)
    }
}