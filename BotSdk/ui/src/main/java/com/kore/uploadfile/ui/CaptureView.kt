package com.kore.uploadfile.ui

import com.kore.ui.base.BaseView

interface CaptureView : BaseView {
    fun init()
    fun showProgress(msg: String)
    fun dismissProgress()
    fun onFileSaved(filePath: String?, fileName: String?, fileExtension: String?, resultCode: Int)
}