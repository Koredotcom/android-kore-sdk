package com.kore.data.repository.dynamicurl

import android.util.Base64
import com.kore.network.api.ApiClient
import com.kore.network.api.service.DynamicUrlApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DynamicUrlRepositoryImpl : DynamicUrlRepository {

    override suspend fun downloadFile(url: String, destinationFile: File, onProgress: (progress: Int) -> Unit) {
        val dynamicUrlApi = ApiClient.getInstance().createService(DynamicUrlApi::class.java, onProgress)
        try {
            if (url.contains("base64,")) {
                writeBase64ToDisk(url, destinationFile, onProgress)
            } else {
                val response = dynamicUrlApi.downloadFile(url)
                if (response.isSuccessful && response.body() != null)
                    saveToDisk(response.body()!!, destinationFile, onProgress)
            }
            println("File downloaded successfully to ${destinationFile.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            onProgress(-1)
            println("File Download failed: ${e.message}")
        }
    }

    private suspend fun saveToDisk(body: ResponseBody, destinationFile: File, onProgress: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            inputStream = body.byteStream()
            outputStream = FileOutputStream(destinationFile)
            val totalBytes = body.contentLength()
            val buffer = ByteArray(1024)
            var bytesRead = 0
            var downloadedBytes: Long = 0
            var totalProgress = -1

            while (isActive && inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead
                val progress = ((downloadedBytes * 100) / totalBytes).toInt()
                if (totalProgress != progress) {
                    totalProgress = progress
                    if (progress == 100) onProgress(progress)
                }
            }

            outputStream.flush()
            println("Download completed. File saved at: ${destinationFile.absolutePath}")
            inputStream.close()
            outputStream.close()
        }
    }

    private fun writeBase64ToDisk(base64Data: String, fileLocation: File, onProgress: (Int) -> Unit) {
        var fileData = base64Data
        var os: FileOutputStream? = null
        fileData = fileData.substring(fileData.indexOf(",") + 1)
        onProgress(50)
        val pdfAsBytes = Base64.decode(fileData, 0)
        os = FileOutputStream(fileLocation, false)
        os.write(pdfAsBytes)
        os.flush()
        os.close()
        onProgress(100)
    }
}