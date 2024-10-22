package com.kore.ai.botsdk.row

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.kore.ai.botsdk.databinding.RowDownloadLinkTemplateBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.model.BotResponse
import com.kore.model.PayloadOuter
import com.kore.model.constants.BotResponseConstants.FILE_NAME
import com.kore.model.constants.BotResponseConstants.PROGRESS
import com.kore.model.constants.BotResponseConstants.URL

class DownloadLinkTemplateRow(
    override val type: SimpleListRowType,
    private val botResponse: BotResponse,
    private val isEnabled: Boolean,
    private val actionEvent: (actionEvent: UserActionEvent) -> Unit
) : SimpleListRow() {
    private var payload: Map<String, Any>? = (botResponse.message[0].cInfo?.body as PayloadOuter).payload
    private val downloadProgress: Int = payload?.get(PROGRESS) as Int? ?: -1

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DownloadLinkTemplateRow) return false
        return otherRow.botResponse.messageId == botResponse.messageId && otherRow.isEnabled == isEnabled
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is DownloadLinkTemplateRow) return false
        return false
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, "", true, true)
        val childBinding = RowDownloadLinkTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            if (payload == null) return
            val fileName = payload?.get(FILE_NAME) as String?
            tvPdfItemTitle.text = fileName
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowDownloadLinkTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowDownloadLinkTemplateBinding.commonBind() {
        val isProgressVisible = downloadProgress in 0..99
        ivPdfDownload.isVisible = downloadProgress == -1 || downloadProgress == 100
        pbDownload.isVisible = isProgressVisible
        if (downloadProgress > 0) pbDownload.progress = downloadProgress
        if (downloadProgress != 0) pbDownload.clearAnimation()

        ivPdfDownload.setOnClickListener {
            if (payload != null && payload?.get(URL) != null) {
                pbDownload.isVisible = true
                ivPdfDownload.isVisible = false
                pbDownload.progress = 75
                pbDownload.startAnimation(AnimationUtils.loadAnimation(root.context, com.kore.ui.R.anim.rotate_indefinitely))
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                actionEvent(BotChatEvent.DownloadLink(botResponse.messageId, payload?.get(URL).toString(), payload?.get(FILE_NAME) as String?))
//                actionEvent(BotChatEvent.DownloadLink(botResponse.messageId, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "BigBuckBunnnnnnyyyy.mp4"))
            }
        }
    }
}