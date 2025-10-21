package com.kore.ui.botchat

import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.kore.common.utils.NetworkUtils
import com.kore.common.utils.ToastUtils
import com.kore.data.repository.dynamicurl.DynamicUrlRepository
import com.kore.data.repository.dynamicurl.DynamicUrlRepositoryImpl
import com.kore.ui.R
import com.kore.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VideoViewModel: BaseViewModel<VideoFullView>() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var context: Context
    private val dynamicUrlRepository: DynamicUrlRepository = DynamicUrlRepositoryImpl()

    fun init(context: Context) {
        this.context = context
    }

    fun downloadFile(msgId: String, url: String, fileName: String?) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            val name = if (!fileName.isNullOrEmpty()) {
                fileName
            } else {
                SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date()) + ".pdf"
            }
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name)
            ToastUtils.showToast(context, context.getString(R.string.download_started), Toast.LENGTH_LONG)

            viewModelScope.launch {
                var prevProgress = -1
                dynamicUrlRepository.downloadFile(url, file) { progress, downloadedBytes ->
                    handler.post {
                        when (progress) {
                            -1 -> ToastUtils.showToast(context, context.getString(R.string.download_fail))
                            100 -> ToastUtils.showToast(context, context.getString(R.string.download_success))
                        }
                        if (prevProgress != progress && (progress <= 1 || progress % 5 == 0)) {
                            prevProgress = progress
                            getView()?.onFileDownloadProgress(msgId, progress, downloadedBytes)
                        }
                    }
                }
            }
        } else {
            getView()?.onFileDownloadProgress(msgId, -1, 0)
            ToastUtils.showToast(context, context.getString(R.string.no_network), Toast.LENGTH_LONG)
        }
    }

}