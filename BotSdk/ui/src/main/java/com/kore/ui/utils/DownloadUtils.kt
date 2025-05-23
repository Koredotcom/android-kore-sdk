package com.kore.ui.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.webkit.URLUtil
import android.widget.Toast
import com.kore.ui.R
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import androidx.core.net.toUri

object DownloadUtils {
    fun downloadFile(context: Context, downloadUrl: String, listener: DownloadProgressListener?) {
        Toast.makeText(context, context.getString(R.string.downloading), Toast.LENGTH_LONG).show()
        val handler = Handler(Looper.getMainLooper())
        Executors.newSingleThreadExecutor().execute {
            try {
                val url = URL(downloadUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connect()

                val mimeType = connection.contentType
                val contentDisposition = connection.getHeaderField("Content-Disposition")
                connection.disconnect()

                handler.post { download(context, downloadUrl, contentDisposition, mimeType, listener) }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    Toast.makeText(context, context.getString(R.string.downloading_failed), Toast.LENGTH_LONG).show()
                    listener?.onProgress(-1)
                }
            }
        }
    }

    private fun download(context: Context, url: String, contentDisposition: String?, mimeType: String?, listener: DownloadProgressListener?) {
        val request = DownloadManager.Request(url.toUri())

        val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
        request.setTitle(fileName)
        request.setDescription(context.getString(R.string.downloading_file))

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "KoreMedia/$fileName")

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.allowScanningByMediaScanner()

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)
        Toast.makeText(context, context.getString(R.string.downloading_file_name, fileName), Toast.LENGTH_LONG).show()
        if (listener != null) {
            val handler = Handler()
            val runnable: Runnable = object : Runnable {
                override fun run() {
                    val query = DownloadManager.Query()
                    query.setFilterById(downloadId)
                    val cursor = downloadManager.query(query)

                    if (cursor != null && cursor.moveToFirst()) {
                        val bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                        if (bytesTotal > 0) {
                            val progress = ((bytesDownloaded * 100L) / bytesTotal).toInt()
                            listener.onProgress(progress)
                        }

                        val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            cursor.close()
                            return
                        }
                        cursor.close()
                    }
                    handler.postDelayed(this, 500)
                }
            }
            handler.post(runnable)
        }
    }

    interface DownloadProgressListener {
        fun onProgress(progress: Int)
    }
}
