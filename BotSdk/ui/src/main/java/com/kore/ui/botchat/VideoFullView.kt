package com.kore.ui.botchat

import com.kore.ui.base.BaseView

interface VideoFullView: BaseView {
    fun onFileDownloadProgress(msgId: String, progress: Int, downloadedBytes: Int)
}