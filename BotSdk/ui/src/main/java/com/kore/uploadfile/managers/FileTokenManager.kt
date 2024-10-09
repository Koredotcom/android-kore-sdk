package com.kore.uploadfile.managers

import android.content.Context
import com.kore.common.utils.LogUtils
import com.kore.common.utils.LogUtils.d
import com.kore.common.utils.LogUtils.e
import com.kore.common.utils.NetworkUtils.Companion.isNetworkAvailable
import com.kore.ui.audiocodes.webrtcclient.general.Log
import com.kore.uploadfile.configurations.Constants
import com.kore.uploadfile.configurations.FileUploadEndPoints
import com.kore.uploadfile.listeners.FileTokenListener
import com.kore.uploadfile.ssl.HttpsUrlConnectionBuilder
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Hashtable
import javax.net.ssl.HttpsURLConnection

class FileTokenManager(
    host: String,
    private val listener: FileTokenListener?,
    accessToken: String,
    context: Context,
    userOrTeamId: String?,
    isAnonymousUser: Boolean,
    isWebHook: Boolean,
    botId: String
) {
    private var url: String

    init {
        url = if (isAnonymousUser) {
            if (!isWebHook) {
                host + FileUploadEndPoints.ANONYMOUS_FILE_TOKEN
            } else {
                host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_FILE_TOKEN, botId, "ivr")
            }
        } else {
            if (!isWebHook) {
                host + String.format(FileUploadEndPoints.FILE_TOKEN, userOrTeamId)
            } else {
                host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_FILE_TOKEN, botId, "ivr")
            }
        }
        if (isNetworkAvailable(context)) {
            try {
                val resp = getFileToken(url, accessToken)
                d("FileTokenManager", "The Final resp is $resp")
                val json1 = JSONObject(resp)
                val fileToken = json1.getString("fileToken")
                val expireOn = json1.getString("expiresOn")
                val hsh = Hashtable<String, String>()
                hsh["fileToken"] = fileToken
                hsh["expiresOn"] = expireOn
                try {
                    if (!resp.equals("", ignoreCase = true)) {
                        this.listener?.fileTokenReceivedSuccessfully(hsh)
                    } else {
                        this.listener?.fileTokenReceivedWithFailure(Constants.SERVER_ERROR, "Unable to reach server")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    e("FILE_TOKEN_SERVICE", e.toString())
                }
            } catch (ex: Exception) {
                this.listener?.fileTokenReceivedWithFailure(Constants.SERVER_ERROR, ex.toString())
            }
        } else {
            this.listener?.fileTokenReceivedWithFailure(Constants.NETWORK_FAILURE, "No network available.")
        }
    }

    private fun getFileToken(url: String, header: String?): String {
        var text = ""
        var reader: BufferedReader? = null
        var conn: HttpsURLConnection? = null
        var inputStreamReader: InputStreamReader? = null
        var inputStream: InputStream? = null
        // Send data
        try {
            // Send POST data request
            val koreHttpsUrlConnectionBuilder = HttpsUrlConnectionBuilder(url)
            conn = koreHttpsUrlConnectionBuilder.httpsURLConnection
            conn.doOutput = true
            if (header != null) conn.addRequestProperty("Authorization", "bearer $header")
            conn.addRequestProperty("User-Agent", Constants.userAgent)
            println(" The userAgent in FileToken Service is ${Constants.userAgent}")
            inputStream = conn.inputStream
            inputStreamReader = InputStreamReader(inputStream)

            // Get the server response
            reader = BufferedReader(inputStreamReader)
            val sb = StringBuilder()
            var line: String?

            // Read Server Response
            while (reader.readLine().also { line = it } != null) {
                // Append server response in string
                sb.append(line).append("\n")
            }
            text = sb.toString()
        } catch (ex: Exception) {
            e("FileTokenManager", ex.toString())
        } finally {
            try {
                conn?.disconnect()
                reader?.close()
                inputStream?.close()
                inputStreamReader?.close()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        return text
    }
}