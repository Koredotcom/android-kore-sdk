package com.kore.uploadfile.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Parcelable
import android.os.Process
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.documentfile.provider.DocumentFile
import com.kore.common.SDKConfiguration
import com.kore.common.constants.MediaConstants
import com.kore.common.constants.MediaConstants.Companion.EXTRA_COMPONENT_TYPE
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_EXT
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_ID
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_NAME
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_PATH
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_TYPE
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_URI
import com.kore.common.constants.MediaConstants.Companion.EXTRA_LOCAL_FILE_PATH
import com.kore.common.constants.MediaConstants.Companion.EXTRA_MEDIA_TYPE
import com.kore.common.constants.MediaConstants.Companion.FOR_MESSAGE
import com.kore.common.constants.MediaConstants.Companion.MEDIA_TYPE_VIDEO
import com.kore.common.utils.BitmapUtils
import com.kore.common.utils.LogUtils
import com.kore.common.utils.MediaUtils
import com.kore.common.utils.ToastUtils.Companion.showToast
import com.kore.ui.R
import com.kore.uploadfile.ui.CaptureActivity
import com.kore.uploadfile.upload.KoreWorker
import com.kore.uploadfile.upload.UploadBulkFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class MediaAttachmentHelper(
    private val context: Context,
    private val botUserId: String,
    private val accessToken: String,
    private val jwt: String = ""
) {
    private var callBackListener: MediaAttachmentListener? = null

    private var requestedCode = 0

    companion object {
        const val REQ_VIDEO_CAPTURE = 446
        const val REQ_IMAGE = 444
        const val REQ_CAMERA = 443
        const val REQ_VIDEO = 445
        const val REQ_FILE = 448
        private const val COMPRESS_QUALITY = 100
        private const val LOG_TAG = "MediaAttachmentHelper"

        private const val EXT_VIDEO = "mp4"
        private const val EXT_JPG = "jpg"
        private const val EXT_PNG = "png"
    }

    fun setListener(callBackListener: MediaAttachmentListener) {
        this.callBackListener = callBackListener
    }

    private var messagesMediaUploadAcknowledgeHandler: Handler =
        object : Handler(Looper.getMainLooper()) {
            @Synchronized
            override fun handleMessage(msg: Message) {
                val reply = msg.data
                if (reply.getBoolean(UploadBulkFile.isFileSizeMore_key, false)) {
                    showToast(context, context.getString(R.string.attachment_size_more_msg))
                    return
                }
                if (reply.getBoolean("success", true)) {
                    val mediaFilePath = reply.getString(EXTRA_FILE_PATH)
                    val mediaType = reply.getString(EXTRA_FILE_EXT)
                    val mediaFileId = reply.getString(EXTRA_FILE_ID)
                    val mediaFileName = reply.getString(EXTRA_FILE_NAME)
                    val componentType = reply.getString(EXTRA_COMPONENT_TYPE)
                    val cmpData = HashMap<String, Any?>(1)
                    cmpData[EXTRA_FILE_NAME] = mediaFileName

                    val attachmentKey = HashMap<String, String?>()
                    attachmentKey[EXTRA_FILE_NAME] = "$mediaFileName.$mediaType"
                    attachmentKey[EXTRA_FILE_TYPE] = componentType
                    attachmentKey[EXTRA_FILE_ID] = mediaFileId
                    attachmentKey[EXTRA_LOCAL_FILE_PATH] = mediaFilePath
                    attachmentKey[EXTRA_MEDIA_TYPE] = mediaType
                    callBackListener?.onMediaFile(
                        "$mediaFileName.$mediaType",
                        ArrayList<HashMap<String, String?>>().apply { add(attachmentKey) })
                } else {
                    val errorMsg = reply.getString(UploadBulkFile.error_msz_key)
                    errorMsg?.let { showToast(context, errorMsg) }
                }
            }
        }

    fun fileBrowsingActivity(
        launcher: ActivityResultLauncher<Intent>,
        chooseType: String,
        reqCode: Int,
        mediaType: String,
        mime: Array<String>? = null
    ) {
        requestedCode = reqCode
        val photoPickerIntent = Intent(context, CaptureActivity::class.java)
        photoPickerIntent.putExtra(MediaConstants.EXTRA_PICK_TYPE, chooseType)
        photoPickerIntent.putExtra(MediaConstants.EXTRA_FILE_CONTEXT, FOR_MESSAGE)
        photoPickerIntent.putExtra(EXTRA_MEDIA_TYPE, mediaType)
        photoPickerIntent.putExtra(MediaConstants.EXTRA_DOCUMENT_MIME, mime)
        launcher.launch(photoPickerIntent)
    }

    @Suppress("DEPRECATION")
    fun processResult(data: Intent?, isUploadMedia: Boolean = true) {
        var extension = BitmapUtils.getExtensionFromFileName(data?.getStringExtra(EXTRA_FILE_PATH))
        if (extension.isEmpty()) {
            Toast.makeText(context, "Please check the file format before uploading the file!", Toast.LENGTH_LONG).show()
            return
        }
        when (requestedCode) {
            REQ_CAMERA, REQ_IMAGE -> {
                val filePath: String = data?.getStringExtra(EXTRA_FILE_PATH) ?: return
                val fileName: String = data.getStringExtra(EXTRA_FILE_NAME) ?: return
                val filePathThumbnail: String = data.getStringExtra(MediaConstants.EXTRA_THUMBNAIL_FILE_PATH) ?: ""
                if (isUploadMedia) {
                    SaveCapturedImageTask(filePath, fileName, filePathThumbnail).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                } else {
                    callBackListener?.onMediaFilePath(filePath)
                }
            }

            REQ_VIDEO -> {
                data?.let {
                    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        it.getParcelableExtra(EXTRA_FILE_URI, Uri::class.java)
                    } else {
                        it.getParcelableExtra(EXTRA_FILE_URI)
                    }
                    val filePath = it.getStringExtra(EXTRA_FILE_PATH)

                    if (uri != null) {
                        processVideoResponse(uri)
                    } else if (filePath != null) {
                        val fileName = it.getStringExtra(EXTRA_FILE_NAME)
                        val fileExtension = it.getStringExtra(EXTRA_FILE_EXT) ?: ""
                        val mediaType = it.getStringExtra(EXTRA_MEDIA_TYPE) ?: ""
                        processFileUpload(fileName, filePath, fileExtension, mediaType, null, null)
                    }
                }
            }

            REQ_VIDEO_CAPTURE -> {
                data?.let {
                    val filePath: String? = it.getStringExtra(EXTRA_FILE_PATH)
                    if (filePath == null) {
                        showToast(context, "Unable to attach file!")
                        return
                    }
                    val fileName: String? = it.getStringExtra(EXTRA_FILE_NAME)
                    val fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1)
                    val mediaType = BitmapUtils.obtainMediaTypeOfExtn(fileExtension)
                    processFileUpload(fileName, filePath, fileExtension, mediaType, null, null)
                }
            }

            REQ_FILE -> {
                val fileExtension: String = data?.getStringExtra(EXTRA_FILE_EXT) ?: return
                if (fileExtension == EXT_VIDEO && data.getParcelableExtra<Parcelable>(EXTRA_FILE_URI) != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        processVideoResponse(data.getParcelableExtra(EXTRA_FILE_URI, Uri::class.java))
                    } else {
                        processVideoResponse(data.getParcelableExtra<Parcelable>(EXTRA_FILE_URI) as Uri)
                    }
                } else if (fileExtension.equals(EXT_JPG, ignoreCase = true) || fileExtension.equals(EXT_PNG, ignoreCase = true)) {
                    processImageResponse(data)
                } else {
                    val filePath: String = data.getStringExtra(EXTRA_FILE_PATH) ?: return
                    val fileName: String = data.getStringExtra(EXTRA_FILE_NAME) ?: return
                    if (!isUploadMedia) {
                        callBackListener?.onMediaFilePath(filePath)
                        return
                    }
                    extension = filePath.substring(filePath.lastIndexOf(".") + 1)
                    val mediaType = BitmapUtils.obtainMediaTypeOfExtn(extension)
                    processFileUpload(
                        if (fileName.contains(".")) fileName.substring(0, fileName.lastIndexOf(".")) else fileName,
                        filePath,
                        extension,
                        mediaType,
                        null,
                        null
                    )
                }
            }
        }
    }

    private fun processVideoResponse(selectedImage: Uri?) {
        var fileName: String? = null
        if (selectedImage == null) return
        val realPath: String? = MediaUtils.getRealPath(context, selectedImage)
        if (realPath != null) {
            if (realPath.isNotEmpty()) {
                val startInd = realPath.lastIndexOf(File.separator) + 1
                val endInd = realPath.indexOf(".", startInd)
                fileName = realPath.substring(startInd, endInd)
            }
            val extension = realPath.substring(realPath.lastIndexOf(".") + 1)
            var thumbnail = BitmapFactory.decodeResource(context.resources, R.drawable.videoplaceholder_left)

            val hover = BitmapFactory.decodeResource(context.resources, R.drawable.btn_video_play_irc)
            thumbnail = overlay(thumbnail, hover)
            val orientation = if (thumbnail.width > thumbnail.height) BitmapUtils.ORIENTATION_LS else BitmapUtils.ORIENTATION_PT
            val bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, realPath, COMPRESS_QUALITY)
            processFileUpload(
                fileName,
                realPath,
                extension,
                BitmapUtils.obtainMediaTypeOfExtn(extension),
                bmpPath,
                orientation
            )
        } else {
            try {
                val pickFile = DocumentFile.fromSingleUri(context, selectedImage)
                var name: String? = null
                var type: String? = null
                if (pickFile != null) {
                    name = pickFile.name
                    type = pickFile.type
                }
                if (type != null && type.contains("video")) {
                    MediaUtils.setupAppDir(context, MEDIA_TYPE_VIDEO)
                    val filePath: String = MediaUtils.getAppDir() + File.separator + name
                    saveVideoTask(filePath, name, selectedImage)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveVideoTask(filePath: String?, name: String?, uri: Uri) {
        var fileName = name
        // Start the coroutine
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (filePath != null) {
                    var fOut: FileOutputStream? = null
                    var out: BufferedOutputStream? = null
                    try {
                        fOut = FileOutputStream(filePath)
                        out = BufferedOutputStream(fOut)
                        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                        val buffer = ByteArray(8192)
                        var len = 0
                        while ((inputStream?.read(buffer) ?: 0.also { len = it }) >= 0) {
                            out.write(buffer, 0, len)
                        }
                        out.flush()
                    } catch (e: java.lang.Exception) {
                        LogUtils.e(LOG_TAG, e.toString())
                    } finally {
                        if (fOut != null) {
                            try {
                                fOut.fd.sync()
                                fOut.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        try {
                            out?.close()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            if (filePath != null) {
                if (filePath.isNotEmpty()) {
                    val startInd: Int = filePath.lastIndexOf(File.separator) + 1
                    val endInd: Int = filePath.indexOf(".", startInd)
                    fileName = filePath.substring(startInd, endInd)
                }
                var videoThumbnailIndexId = 0
                val extn: String = filePath.substring(filePath.lastIndexOf(".") + 1)
                try {
                    videoThumbnailIndexId = BitmapUtils.getVideoIdFromFilePath(context, uri)
                } catch (se: SecurityException) {
                    se.printStackTrace()
                }
                val hover = BitmapFactory.decodeResource(context.resources, R.drawable.btn_video_play_irc)
                var thumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                    context.contentResolver,
                    videoThumbnailIndexId.toLong(),
                    MediaStore.Video.Thumbnails.MINI_KIND,
                    null
                )
                if (thumbnail == null) {
                    thumbnail = BitmapFactory.decodeResource(context.resources, R.drawable.videoplaceholder_left)
                }
                thumbnail = overlay(thumbnail!!, hover)
                val orientation = if (thumbnail.width > thumbnail.height) BitmapUtils.ORIENTATION_LS else BitmapUtils.ORIENTATION_PT
                val bmpPath = BitmapUtils.createImageThumbnailForBulk(thumbnail, filePath, COMPRESS_QUALITY)
                processFileUpload(fileName, filePath, extn, BitmapUtils.obtainMediaTypeOfExtn(extn), bmpPath, orientation)
            }
        }
    }

    private fun overlay(bitmap1: Bitmap, bitmap2: Bitmap): Bitmap {
        val bitmap1Width = bitmap1.width
        val bitmap1Height = bitmap1.height
        val bitmap2Width = bitmap2.width
        val bitmap2Height = bitmap2.height
        val marginLeft = (bitmap1Width * 0.5 - bitmap2Width * 0.5).toFloat()
        val marginTop = (bitmap1Height * 0.5 - bitmap2Height * 0.5).toFloat()
        val overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.config)
        val canvas = Canvas(overlayBitmap)
        canvas.drawBitmap(bitmap1, Matrix(), null)
        canvas.drawBitmap(bitmap2, marginLeft, marginTop, null)
        return overlayBitmap
    }

    private fun processFileUpload(
        fileName: String?,
        filePath: String,
        extension: String,
        mediaType: String,
        thumbnailFilePath: String?,
        orientation: String?
    ) {
        val botConfig = SDKConfiguration.getBotConfigModel() ?: return
        KoreWorker.getInstance().addTask(
            UploadBulkFile(
                fileName,
                filePath,
                "bearer ${if (botConfig.isWebHook) jwt else accessToken}",
                botUserId,
                "workflows",
                extension,
                BitmapUtils.getBufferSize(mediaType),
                Messenger(messagesMediaUploadAcknowledgeHandler),
                thumbnailFilePath,
                "AT_" + System.currentTimeMillis(),
                context,
                mediaType,
                botConfig.botUrl,
                orientation,
                true,
                botConfig.isWebHook,
                botConfig.botId
            )
        )
    }

    private fun processImageResponse(data: Intent) {
        val filePath = data.getStringExtra(EXTRA_FILE_PATH)
        var orientation: String? = null
        if (filePath == null) {
            showToast(context, "Unable to attach file!")
            return
        }
        val fileName: String? = data.getStringExtra(EXTRA_FILE_NAME)
        val filePathThumbnail: String? = data.getStringExtra(MediaConstants.EXTRA_THUMBNAIL_FILE_PATH)
        val fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1)
        var thePic = BitmapUtils.decodeBitmapFromFile(filePath, 800, 600)
        if (thePic != null) {
            try {
                // compress the image
                val file = File(filePath)
                Log.d(LOG_TAG, " file.exists() ---------------------------------------- " + file.exists())
                val fOut = FileOutputStream(file)
                thePic.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fOut)
                thePic = BitmapUtils.rotateIfNecessary(filePath, thePic)
                if (thePic == null) return
                orientation = if (thePic.width > thePic.height) BitmapUtils.ORIENTATION_LS else BitmapUtils.ORIENTATION_PT
                fOut.flush()
                fOut.close()
            } catch (e: Exception) {
                LogUtils.e(LOG_TAG, e.toString())
            }
        }
        val botConfig = SDKConfiguration.getBotConfigModel() ?: return
        KoreWorker.getInstance().addTask(
            UploadBulkFile(
                fileName,
                filePath,
                if (!botConfig.isWebHook) {
                    "bearer $accessToken"
                } else {
                    "bearer $jwt"
                },
                botUserId,
                "workflows",
                fileExtension,
                MediaConstants.BUFFER_SIZE_IMAGE,
                Messenger(messagesMediaUploadAcknowledgeHandler),
                filePathThumbnail,
                "AT_" + System.currentTimeMillis(),
                context,
                BitmapUtils.obtainMediaTypeOfExtn(fileExtension),
                botConfig.botUrl,
                orientation,
                true,
                botConfig.isWebHook,
                botConfig.botId
            )
        )
    }

    @Suppress("DEPRECATION")
    inner class SaveCapturedImageTask(
        private val filePath: String?,
        private val fileName: String,
        private val filePathThumbnail: String
    ) :
        AsyncTask<String?, String?, String?>() {
        private var orientation: String? = null
        override fun doInBackground(vararg params: String?): String? {
            Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE)
            var extn: String? = null
            if (filePath != null) {
                extn = filePath.substring(filePath.lastIndexOf(".") + 1)
                var thePic = BitmapUtils.decodeBitmapFromFile(filePath, 800, 600)
                if (thePic != null) {
                    try {
                        // compress the image
                        var fOut: OutputStream? = null
                        val file = File(filePath)
                        Log.d(LOG_TAG, " file.exists() ---------------------------------------- " + file.exists())
                        fOut = FileOutputStream(file)
                        thePic.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                        thePic = BitmapUtils.rotateIfNecessary(filePath, thePic)
                        if (thePic == null) return null
                        orientation = if (thePic.width > thePic.height) BitmapUtils.ORIENTATION_LS else BitmapUtils.ORIENTATION_PT
                        fOut.flush()
                        fOut.close()
                    } catch (e: java.lang.Exception) {
                        LogUtils.e(LOG_TAG, e.toString())
                    }
                }
            }
            return extn!!
        }

        override fun onPostExecute(extn: String?) {
            if (extn != null) {
                //Common place for addition to composeBar
                val botConfig = SDKConfiguration.getBotConfigModel() ?: return
                KoreWorker.getInstance().addTask(
                    UploadBulkFile(
                        fileName,
                        filePath,
                        "bearer " + if (!botConfig.isWebHook) accessToken else jwt,
                        botUserId,
                        "workflows",
                        extn,
                        MediaConstants.BUFFER_SIZE_IMAGE,
                        Messenger(messagesMediaUploadAcknowledgeHandler),
                        filePathThumbnail,
                        "AT_" + System.currentTimeMillis(),
                        context,
                        BitmapUtils.obtainMediaTypeOfExtn(extn),
                        botConfig.botUrl,
                        orientation,
                        true,
                        botConfig.isWebHook,
                        botConfig.botId
                    )
                )
            } else {
                showToast(context, "Unable to attach!")
            }
        }
    }

    interface MediaAttachmentListener {
        fun onMediaFile(fileName: String, mediaData: List<Map<String, *>>)
        fun onMediaFilePath(filePath: String)
    }
}