package com.kore.ui.base

import androidx.fragment.app.Fragment
import com.kore.common.event.UserActionEvent
import com.kore.listeners.AttachmentListener
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.speech.SpeechDelegate
import com.kore.uploadfile.helper.MediaAttachmentHelper

abstract class BaseFooterFragment : Fragment(), SpeechDelegate, MediaAttachmentHelper.MediaAttachmentListener, AttachmentListener {
    abstract fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit)
    abstract fun setBrandingDetails(botBrandingModel: BotBrandingModel?)
    abstract fun enableSendButton(enable: Boolean)
    abstract fun setMessage(message: String)
    abstract fun showAttachmentActionSheet()
}