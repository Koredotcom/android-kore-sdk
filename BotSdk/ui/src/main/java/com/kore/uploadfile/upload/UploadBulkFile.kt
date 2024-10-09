package com.kore.uploadfile.upload

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.view.Gravity
import android.widget.Toast
import com.google.gson.Gson
import com.kore.common.constants.MediaConstants
import com.kore.common.utils.LogUtils.d
import com.kore.common.utils.LogUtils.e
import com.kore.uploadfile.configurations.Constants
import com.kore.uploadfile.configurations.Constants.userAgent
import com.kore.uploadfile.configurations.FileUploadEndPoints
import com.kore.uploadfile.listeners.ChunkUploadListener
import com.kore.uploadfile.listeners.FileTokenListener
import com.kore.uploadfile.listeners.FileUploadedListener
import com.kore.uploadfile.managers.BotDBManager
import com.kore.uploadfile.managers.FileTokenManager
import com.kore.uploadfile.models.ChunkInfo
import com.kore.uploadfile.models.FileUploadInfo
import com.kore.uploadfile.models.MissingChunks
import com.kore.uploadfile.services.FileUploadServiceExecutor
import com.kore.uploadfile.ssl.HttpsUrlConnectionBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.ByteArrayBody
import org.apache.http.entity.mime.content.StringBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.Arrays
import java.util.Hashtable
import java.util.concurrent.ExecutorService
import javax.crypto.CipherInputStream
import javax.net.ssl.HttpsURLConnection

class UploadBulkFile(
    private val fileName: String?,
    private val outFilePath: String?,
    private val accessToken: String?,
    private val userId: String?,
    private val fileContext: String?,
    private val fileExtn: String?,
    private val BUFFER_SIZE: Int,
    private val messenger: Messenger,
    private val thumbnailFilePath: String?,
    private val messageId: String,
    private val context: Context,
    private val componentType: String,
    private val host: String,
    private val orientation: String?,
    private val isAnonymousUser: Boolean,
    private val isWebHook: Boolean,
    private val botId: String
) : Work, FileTokenListener, ChunkUploadListener {
    private val LOG_TAG = javaClass.simpleName
    private var fileToken: String? = null
    private var listener: FileUploadedListener? = null
    private var noOfMergeAttempts = 0
    var uploadInfo: FileUploadInfo? = FileUploadInfo()
    var executor: ExecutorService = FileUploadServiceExecutor.instance!!.getExecutor()
    private var pDialog: ProgressDialog? = null

    private fun upLoadProgressState(progress: Int, show: Boolean) {
        val mainHandler = Handler(context.mainLooper)
        val myRunnable = Runnable {
            if (show) {
                if (pDialog == null) {
                    pDialog = ProgressDialog(context)
                    pDialog!!.setCancelable(false)
                    pDialog!!.isIndeterminate = false
                    pDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    pDialog!!.max = 100
                }
                pDialog!!.setMessage("Uploading...")
                pDialog!!.progress = progress
                if (!pDialog!!.isShowing) {
                    pDialog!!.show()
                }
            } else {
                if (pDialog != null && pDialog!!.isShowing) {
                    pDialog!!.dismiss()
                }
            }
        }
        d("progress", "$progress done")
        mainHandler.post(myRunnable)
    }

    @Synchronized
    override fun fileTokenReceivedSuccessfully(hsh: Hashtable<String, String>) {
        fileToken = hsh["fileToken"]
        startUpload()
    }

    override fun fileTokenReceivedWithFailure(errCode: String, reason: String) {
        d(LOG_TAG, "file upload failed because of getting file token and the reason is $reason")
        sendUploadFailedNotice(false, reason)
    }

    @Synchronized
    private fun startUpload() {
        noOfMergeAttempts = 1
        val chunkbaos = ByteArrayOutputStream()
        var fis: FileInputStream? = null
        var dataSizeRead = 0
        var totalBytesWritten = 0
        val buffer = ByteArray(BUFFER_SIZE)
        try {
            fis = FileInputStream(outFilePath)
            if (fis.channel.size() > FILE_SIZE_20MB) {
                sendUploadFailedNotice(false, null)
                showToastMsg("File size can't be more than 20 MB!")
                d(LOG_TAG, "File size can't be more than 20 mb")
                return
            }
            if (fis.available() > 0) {
                var chunkCount = fis.channel.size().toInt() / BUFFER_SIZE
                if (fis.channel.size().toInt() % BUFFER_SIZE > 0) {
                    chunkCount++
                }
                setChunkCount(chunkCount)
            } else {
                sendUploadFailedNotice(false, null)
                showToastMsg("File not available")
                d(LOG_TAG, "File not available")
                return
            }
            if (fis.available() > 0) {
                var chunkNo = 0
                while (fis.read(buffer).also { dataSizeRead = it } > 0) {
                    chunkbaos.write(buffer, 0, dataSizeRead)
                    chunkbaos.flush()
                    totalBytesWritten += dataSizeRead
                    d(LOG_TAG, "5#################################The chunk number is $chunkNo")
                    val chunkInfo = ChunkInfo()
                    chunkInfo.fileName = fileName
                    chunkInfo.number = chunkNo
                    chunkInfo.offset = totalBytesWritten - buffer.size
                    chunkInfo.size = dataSizeRead
                    if (BotDBManager.instance.chunkInfoMap[fileToken] != null) {
                        BotDBManager.instance.chunkInfoMap[fileToken]?.set(chunkNo, chunkInfo)
                    } else {
                        val hMap = HashMap<Int, ChunkInfo>(1)
                        hMap[chunkNo] = chunkInfo
                        BotDBManager.instance.chunkInfoMap[fileToken!!] = hMap
                    }
                    Thread.sleep(10)
                    System.gc()
                    upLoadProgressState(0, true)
                    executor.execute(
                        UploadExecutor(
                            fileName!!,
                            fileToken!!,
                            accessToken!!,
                            userId!!,
                            chunkbaos.toByteArray(),
                            chunkNo,
                            this,
                            host,
                            isAnonymousUser,
                            isWebHook,
                            botId
                        )
                    )
                    chunkNo++
                    chunkbaos.reset() /*chunk size doubles in next iteration*/
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToastMsg("Unable to attach file")
        } finally {
            try {
                chunkbaos.close()
                fis?.close()
            } catch (ex: java.lang.Exception) {
                println("Exception: $ex")
            }
        }
    }

    private fun showToastMsg(msg: String) {
        val mainHandler = Handler(context.mainLooper)
        val myRunnable = Runnable {
            val toastView = Toast.makeText(context, msg, Toast.LENGTH_LONG)
            toastView.setGravity(Gravity.CENTER, 0, 0)
            toastView.show()
        }
        mainHandler.post(myRunnable)
    }

    @Synchronized
    private fun startUploadFailedChunks(failedChunkList: ArrayList<String>) {
        val chunkBaos = ByteArrayOutputStream()
        var fis: InputStream? = null
        var dataSizeRead = 0
        val buffer = ByteArray(BUFFER_SIZE)
        try {
            fis = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.newInputStream(Paths.get(outFilePath))
            } else {
                FileInputStream(outFilePath)
            }
            if (fis?.available()!! > 0) {
                var chunkNo = 0
                while (fis.read(buffer).also { dataSizeRead = it } > 0) {
                    chunkBaos.write(buffer, 0, dataSizeRead)
                    chunkBaos.flush()
                    d(LOG_TAG, "6########################The chunk number is $chunkNo")
                    Thread.sleep(10)
                    System.gc()
                    if (failedChunkList.contains(chunkNo.toString() + "")) {
                        executor.execute(
                            UploadExecutor(
                                fileName!!,
                                fileToken!!,
                                accessToken!!,
                                userId!!,
                                chunkBaos.toByteArray(),
                                chunkNo,
                                this,
                                host,
                                isAnonymousUser,
                                isWebHook,
                                botId
                            )
                        )
                    }
                    chunkNo++
                    chunkBaos.reset() /*chunk size doubles in next iteration*/
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                chunkBaos.close()
                fis?.close()
            } catch (ex: Exception) {
            }
        }
    }

    @Synchronized
    override fun notifyChunkUploadCompleted(chunkNo: String?, fileName: String) {
        if (uploadInfo == null) return
        try {
            uploadInfo!!.uploadCount = uploadInfo!!.uploadCount + 1
            upLoadProgressState(uploadInfo!!.uploadCount * 100 / uploadInfo!!.totalChunks, true)
            val chInfo = BotDBManager.instance.chunkInfoMap[fileToken]!![chunkNo!!.toInt()]
            chInfo!!.isUploaded = true
            BotDBManager.instance.chunkInfoMap[fileToken]?.set(chunkNo.toInt(), chInfo)
            BotDBManager.instance.fileUploadInfoMap[fileToken!!] = uploadInfo!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (uploadInfo!!.totalChunks == uploadInfo!!.uploadCount) {
            d(LOG_TAG, "Sending merge request" + fileName + "as no failed chunks in recorded")
            try {
                sendMergeSignal(uploadInfo!!.totalChunks, fileContext)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun initiateFileUpload(listener: FileUploadedListener?) {
        this.listener = listener
        accessToken?.replace("bearer ", "")?.let {
            FileTokenManager(
                host,
                this,
                it,
                context,
                userId,
                isAnonymousUser,
                isWebHook,
                botId
            )
        }
    }

    private fun setChunkCount(n: Int) {
        if (uploadInfo != null) {
            uploadInfo?.totalChunks = n
            try {
                BotDBManager.instance.fileUploadInfoMap[fileToken!!] = uploadInfo!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun sendMergeSignal(dataCount: Int, fileContext: String?) {
        d(LOG_TAG, "(((( re attempt count $noOfMergeAttempts")
        if (noOfMergeAttempts > Constants.MERGE_RE_ATTEMPT_COUNT) {
            sendUploadFailedNotice(true, null)
            return
        }
        noOfMergeAttempts++
        var fileID = ""
        var httpsURLConnection: HttpsURLConnection? = null
        var bufferedReader: BufferedReader? = null
        var inputStreamReader: InputStreamReader? = null
        var dataOutputStream: DataOutputStream? = null
        var fis: InputStream? = null
        try {
            val koreHttpsUrlConnectionBuilder: HttpsUrlConnectionBuilder = if (isAnonymousUser) {
                if (!isWebHook) HttpsUrlConnectionBuilder(
                    host + String.format(
                        FileUploadEndPoints.ANONYMOUS_MERGE,
                        userId,
                        fileToken
                    )
                ) else HttpsUrlConnectionBuilder(host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_MERGE, botId, fileToken))
            } else {
                if (!isWebHook) HttpsUrlConnectionBuilder(
                    host + String.format(
                        FileUploadEndPoints.MERGE_FILE,
                        userId,
                        fileToken
                    )
                ) else HttpsUrlConnectionBuilder(host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_MERGE, botId, fileToken))
            }
            httpsURLConnection = koreHttpsUrlConnectionBuilder.httpsURLConnection
            httpsURLConnection.connectTimeout = Constants.CONNECTION_TIMEOUT
            httpsURLConnection.useCaches = false
            httpsURLConnection.doOutput = true
            httpsURLConnection.doInput = true
            httpsURLConnection.readTimeout = Constants.CONNECTION_READ_TIMEOUT
            val reqEntity = MultipartEntityBuilder.create()
            if (thumbnailFilePath != null && !thumbnailFilePath.equals("", ignoreCase = true) && fileContext.equals(
                    "knowledge",
                    ignoreCase = true
                )
            ) {
                try {
                    fis = FileInputStream(thumbnailFilePath)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                var thumbBaos: ByteArrayOutputStream? = null
                if (fis != null && (fis is CipherInputStream || fis.available() > 0)) {
                    thumbBaos = ByteArrayOutputStream()
                    reqEntity.addPart("thumbnailUpload", StringBody("true", ContentType.TEXT_PLAIN))
                    val startInd = thumbnailFilePath.lastIndexOf(File.separator) + 1
                    val endInd = thumbnailFilePath.indexOf(".", startInd)
                    val thumbnailfileName = thumbnailFilePath.substring(startInd, endInd)
                    var dataRead = 0
                    val buff = ByteArray(2 * 1024)
                    /*file size is less than BUFFER_SIZE..just send the file*/
                    while (fis.read(buff).also { dataRead = it } != -1) {
                        thumbBaos.write(buff, 0, dataRead)
                    }
                    reqEntity.addPart("thumbnail", ByteArrayBody(thumbBaos.toByteArray(), "image/png", thumbnailfileName))
                    reqEntity.addPart("thumbnailExtension", StringBody("png", ContentType.TEXT_PLAIN))
                    d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP     Thumbnail uploaded")
                } else {
                    reqEntity.addPart("thumbnailUpload", StringBody("false", ContentType.TEXT_PLAIN))
                }
            } else {
                reqEntity.addPart("thumbnailUpload", StringBody("false", ContentType.TEXT_PLAIN))
                d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP   No Thumbnail ")
            }
            d(LOG_TAG, "M#####The chunk number is $dataCount")
            reqEntity.addPart("totalChunks", StringBody(dataCount.toString() + "", ContentType.TEXT_PLAIN))
            reqEntity.addPart("fileExtension", StringBody(fileExtn, ContentType.TEXT_PLAIN))
            reqEntity.addPart("fileToken", StringBody(fileToken, ContentType.TEXT_PLAIN))
            if (!fileName.isNullOrEmpty()) reqEntity.addPart("filename", StringBody(fileName, ContentType.TEXT_PLAIN))
            if (fileContext != null) {
                d(LOG_TAG, "Adding fileContext  !!!")
                reqEntity.addPart("fileContext", StringBody(fileContext, ContentType.TEXT_PLAIN))
            } else {
                d(LOG_TAG, "There is no fileContext !!")
            }
            d(LOG_TAG, "***********AccessToken in SendMergeSignal**********$accessToken")
            httpsURLConnection.setRequestProperty("User-Agent", userAgent)
            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache")
            httpsURLConnection.setRequestProperty("Authorization", accessToken)
            httpsURLConnection.requestMethod = "PUT"
            val entity = reqEntity.build()
            httpsURLConnection.setRequestProperty(entity.contentType.name, entity.contentType.value)
            dataOutputStream = DataOutputStream(httpsURLConnection.outputStream)
            entity.writeTo(dataOutputStream)
            inputStreamReader = InputStreamReader(httpsURLConnection.inputStream)
            bufferedReader = BufferedReader(inputStreamReader)
            val serverResponse = StringBuilder()
            var c = bufferedReader.read()
            while (c != -1) {
                serverResponse.append(c.toChar())
                c = bufferedReader.read()
            }
            upLoadProgressState(100, false)
            d(LOG_TAG, "Got serverResponse for merge $serverResponse")
            val statusCode = httpsURLConnection.responseCode
            d(LOG_TAG, "status code for merge$statusCode")
            if (statusCode == 200) {
                uploadInfo!!.isUploaded = true
                val jsonObject = JSONObject(serverResponse.toString())
                var thumbnailURL: String? = null
                if (jsonObject.has("fileId")) {
                    fileID = jsonObject["fileId"] as String
                    if (jsonObject.has("thumbnailURL")) {
                        thumbnailURL = jsonObject["thumbnailURL"] as String
                    }
                    uploadInfo?.fileId = fileID
                    uploadInfo?.isMergeTriggered = true
                    val msg = Message.obtain()
                    val data = Bundle()
                    data.putBoolean("success", true)
                    data.putString(Constants.MESSAGE_ID, messageId)
                    data.putString(MediaConstants.EXTRA_FILE_EXT, fileExtn)
                    data.putString(MediaConstants.EXTRA_FILE_PATH, outFilePath)
                    data.putString(MediaConstants.EXTRA_FILE_ID, fileID)
                    data.putString(MediaConstants.EXTRA_FILE_NAME, fileName)
                    data.putString(MediaConstants.EXTRA_COMPONENT_TYPE, componentType)
                    data.putString(MediaConstants.EXTRA_FILE_SIZE, getFileSizeMegaBytes(File(outFilePath)))
                    data.putString(MediaConstants.EXTRA_THUMBNAIL_URL, thumbnailURL)
                    data.putString(MediaConstants.EXTRA_ORIENTATION, orientation)
                    msg.data = data //put the data here
                    try {
                        messenger.send(msg)
                    } catch (e: RemoteException) {
                        d("error", "error$e")
                    }
                    if (listener != null) listener!!.fileUploaded()
                    d(LOG_TAG, "The fileId is " + fileID + " is listener is null " + if (listener == null) "True " else "false")
                }
            } else {
                d(LOG_TAG, "The Resp is $serverResponse")
                sendUploadFailedNotice(true, null)
            }
        } catch (e: Exception) {
            if (e is SocketTimeoutException) {
                sendUploadFailedNotice(true, null)
            } else if (httpsURLConnection != null) {
                var resCode = -1
                try {
                    resCode = httpsURLConnection.responseCode
                } catch (ex: Exception) {
                    e.printStackTrace()
                }
                d(LOG_TAG, "Hi res code is $resCode")
                if (resCode == Constants.UPLOAD_ERROR_CODE_404) {
                    handleErr404(httpsURLConnection.errorStream, fileName)
                } else {
                    if (e.toString().contains("FileNotFoundException")) {
                        sendUploadFailedNotice(true, "Please select a valid file!")
                    } else {
                        sendUploadFailedNotice(true, null)
                    }
                }
            } else {
                sendUploadFailedNotice(true, "Server not connected!")
            }
            e.printStackTrace()
            e(LOG_TAG, "Failed to send the merge initiation message $e")
        } finally {
            try {
                httpsURLConnection?.disconnect()
                fis?.close()
                bufferedReader?.close()
                inputStreamReader?.close()
                dataOutputStream?.close()
                BotDBManager.instance.fileUploadInfoMap[fileToken!!] = uploadInfo!!
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileSizeMegaBytes(file: File): String {
        return if (file.length() < 1024 * 1024) {
            df2.format(file.length().toDouble() / 1024) + "kb"
        } else df2.format(file.length().toDouble() / (1024 * 1024)) + "mb"
    }

    private fun handleErr404(errorStream: InputStream, fileName: String?) {
        d(LOG_TAG, "Starting upload failed chunks")
        val response = StringBuilder()
        var line: String?
        val br = BufferedReader(InputStreamReader(errorStream))
        try {
            while (br.readLine().also { line = it } != null) {
                response.append(line)
            }
            if (response.isNotEmpty()) {
                d(LOG_TAG, "Failed chunks are $response")
                val msc = Gson().fromJson(response.toString(), MissingChunks::class.java)
                if (msc != null) {
                    val (msg) = msc.errors[0]
                    val missingChunks = ArrayList(
                        Arrays.asList(
                            *msg!!.substring(1, msg.length - 1).split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()))
                    changeUploadInfoForFailedChunks(missingChunks, fileName)
                    startUploadFailedChunks(missingChunks)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeUploadInfoForFailedChunks(chunkNumbers: ArrayList<String>, fileName: String?) {
        if (uploadInfo == null) return
        try {
            uploadInfo!!.uploadCount = uploadInfo!!.uploadCount - chunkNumbers.size
            for (chunkNo in chunkNumbers) {
                val chInfo = BotDBManager.instance.chunkInfoMap[fileToken]?.get(chunkNo.toInt())
                chInfo!!.isUploaded = false
                BotDBManager.instance.chunkInfoMap[fileToken]?.set(chunkNo.toInt(), chInfo)
            }
            uploadInfo!!.isUploaded = false
            BotDBManager.instance.fileUploadInfoMap.set(fileToken!!, uploadInfo!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendUploadFailedNotice(isFromMergeSignal: Boolean, errorMessage: String?) {
        uploadInfo!!.isUploaded = false
        val msg = Message.obtain()
        val data = Bundle()
        data.putString(Constants.MESSAGE_ID, messageId)
        data.putString(MediaConstants.EXTRA_FILE_EXT, fileExtn)
        data.putString("fileId", Constants.LOCAL_FILE_ID)
        data.putString("fileName", fileName)
        data.putString("filePath", outFilePath)
        data.putString("componentType", componentType)
        data.putBoolean("success", false)
        if (errorMessage != null) {
            data.putString(error_msz_key, errorMessage)
        }
        msg.data = data //put the data here
        try {
            messenger.send(msg)
        } catch (e: RemoteException) {
            d("error", "error")
        }
        if (listener != null) listener!!.fileUploaded()
        upLoadProgressState(100, false)
        uploadInfo!!.isMergeTriggered = isFromMergeSignal
    }

    companion object {
        private val df2 = DecimalFormat("###.##")
        const val error_msz_key = "error_msz_key"
        const val isFileSizeMore_key = "isFileSizeMore"
        const val FILE_SIZE_20MB = (20 * 1024 * 1024).toLong()
    }
}