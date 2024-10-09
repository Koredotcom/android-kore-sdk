package com.kore.uploadfile.upload

import com.kore.common.utils.LogUtils
import com.kore.uploadfile.configurations.Constants
import com.kore.uploadfile.configurations.Constants.userAgent
import com.kore.uploadfile.configurations.FileUploadEndPoints
import com.kore.uploadfile.listeners.ChunkUploadListener
import com.kore.uploadfile.ssl.HttpsUrlConnectionBuilder
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.StringBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import javax.net.ssl.HttpsURLConnection

class UploadExecutor(
    private val fileName: String,
    private val fileToken: String,
    private val accessToken: String,
    private val userOrTeamId: String,
    private val dataToSet: ByteArray?,
    private val chunkNo: Int,
    private val listener: ChunkUploadListener?,
    private val host: String,
    private val isAnonymousUser: Boolean,
    private val isWebhook: Boolean,
    private val botId: String
) : Runnable {
    private val LOG_TAG = "UploadExecutor"

    override fun run() {
        var serverResponse: StringBuilder? = null
        var httpsURLConnection: HttpsURLConnection? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        var dataOutputStream: DataOutputStream? = null
        try {
            LogUtils.d(LOG_TAG, "About to send chunks" + chunkNo + "for file" + fileName)
            val fullUrl: String = if (isAnonymousUser) {
                if (!isWebhook) host + String.format(
                    FileUploadEndPoints.ANONYMOUS_CHUNK_UPLOAD,
                    userOrTeamId,
                    fileToken
                ) else host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_CHUNK_UPLOAD, botId, "ivr", fileToken)
            } else {
                if (!isWebhook) host + String.format(FileUploadEndPoints.CHUNK_UPLOAD, userOrTeamId, fileToken) else host + String.format(
                    FileUploadEndPoints.WEBHOOK_ANONYMOUS_CHUNK_UPLOAD,
                    botId,
                    "ivr",
                    fileToken
                )
            }
            val koreHttpsUrlConnectionBuilder = HttpsUrlConnectionBuilder(fullUrl)
            httpsURLConnection = koreHttpsUrlConnectionBuilder.httpsURLConnection
            httpsURLConnection.connectTimeout = Constants.CONNECTION_TIMEOUT
            httpsURLConnection.requestMethod = "POST"
            httpsURLConnection.useCaches = false
            httpsURLConnection.doOutput = true
            httpsURLConnection.doInput = true
            httpsURLConnection.setRequestProperty("User-Agent", userAgent)
            httpsURLConnection.setRequestProperty("Authorization", accessToken)
            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache")
            httpsURLConnection.readTimeout = Constants.CONNECTION_READ_TIMEOUT
            val builder = MultipartEntityBuilder.create()
            builder.addPart("chunkNo", StringBody(chunkNo.toString() + ""))
            builder.addPart("fileToken", StringBody(fileToken))
            builder.addPart("chunk", ByteArrayBody(dataToSet, fileName))
            val reqEntity = builder.build()
            httpsURLConnection.setRequestProperty(reqEntity.contentType.name, reqEntity.contentType.value)
            dataOutputStream = DataOutputStream(httpsURLConnection.outputStream)
            reqEntity.writeTo(dataOutputStream)

            //Real upload starting here -->>
            LogUtils.d("upload new", "good so far")
            inputStreamReader = InputStreamReader(httpsURLConnection.inputStream);
            bufferedReader = BufferedReader(inputStreamReader)
            serverResponse = StringBuilder()
            var c = bufferedReader.read()
            while (c != -1) {
                serverResponse.append(c.toChar())
                c = bufferedReader.read()
            }
            LogUtils.d(LOG_TAG, "Got serverResponse for chunk upload$serverResponse")
            val statusCode = httpsURLConnection.responseCode
            LogUtils.e(LOG_TAG, "status code for chunks" + chunkNo + "is" + statusCode)
            var chunkNumber: String? = null
            if (statusCode == 200) {
                val jsonObject = JSONObject(serverResponse.toString())
                if (jsonObject.has("chunkNo")) {
                    chunkNumber = jsonObject["chunkNo"] as String
                    listener?.notifyChunkUploadCompleted(chunkNumber, fileName)
                    LogUtils.e(LOG_TAG, "Response for chunk ::::" + chunkNumber + "for file" + fileName)
                }
            } else {
                listener?.notifyChunkUploadCompleted(chunkNumber, fileName)
                throw Exception("Response code not 200")
            }
        } catch (e: Exception) {
            LogUtils.e(LOG_TAG, "Exception in uploading chunk $e")
            e.printStackTrace()
            listener?.notifyChunkUploadCompleted(chunkNo.toString() + "", fileName)
            LogUtils.e(LOG_TAG, "Failed to post message for chunk no:: $chunkNo")
        } finally {
            try {
                httpsURLConnection?.disconnect()
                dataOutputStream?.close()
                inputStreamReader?.close()
                bufferedReader?.close()
            } catch (ex: java.lang.Exception) {
                println("Exception: $ex")
            }
        }
    }
}