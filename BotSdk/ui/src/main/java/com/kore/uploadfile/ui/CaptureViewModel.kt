package com.kore.uploadfile.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.kore.common.base.BaseViewModel
import com.kore.common.constants.MediaConstants
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_EXT
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_NAME
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_PATH
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_URI
import com.kore.common.constants.MediaConstants.Companion.EXTRA_THUMBNAIL_FILE_PATH
import com.kore.common.constants.MediaConstants.Companion.FOR_MESSAGE
import com.kore.common.constants.MediaConstants.Companion.MEDIA_TYPE_IMAGE
import com.kore.common.exception.NoExternalStorageException
import com.kore.common.exception.NoWriteAccessException
import com.kore.common.utils.BitmapUtils
import com.kore.common.utils.BitmapUtils.createImageThumbnail
import com.kore.common.utils.BitmapUtils.decodeBitmapFromFile
import com.kore.common.utils.BitmapUtils.getExtensionFromFileName
import com.kore.common.utils.BitmapUtils.getScaledBitmap
import com.kore.common.utils.BitmapUtils.rotateIfNecessary
import com.kore.common.utils.LogUtils
import com.kore.common.utils.MediaUtils
import com.kore.common.utils.MediaUtils.Companion.saveFileToKoreWithStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("StaticFieldLeak")
class CaptureViewModel : BaseViewModel<CaptureView>() {
    companion object {
        private val LOG_TAG = CaptureViewModel::class.java.simpleName
        private const val COMPRESS_QUALITY = 100
    }

    private lateinit var context: Context
    private var imagePickType: String? = null
    private var fileContext: String? = null
    private var mediaType: String? = MEDIA_TYPE_IMAGE
    private var isPortrait = true

    fun init(context: Context, imagePickType: String?, fileContext: String?, mediaType: String?) {
        this.context = context
        this.imagePickType = imagePickType
        this.fileContext = fileContext
        this.mediaType = mediaType
        getView()?.init()
    }

    private fun getFullImage(photoPath: String?, onImage: (filePath: String, fileName: String, fileExtension: String) -> Unit) {
        var path: String? = null
        path = photoPath
        if (path == null) return
        val file = File(path)
        LogUtils.d(LOG_TAG, "real image path  $path")
        LogUtils.d(LOG_TAG, "getFullImage() :: full image file absolute path::$path")
        var thePic = decodeBitmapFromFile(file, 800, 600)
        try {
            // compress the image
            LogUtils.d(LOG_TAG, "getFullImage() :: file.exists() ---------------------------------------- " + file.exists())
            val fOut = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.newOutputStream(file.toPath())
            } else {
                FileOutputStream(file)
            }
            thePic!!.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fOut)
            fOut.flush()
            fOut.close()
            thePic = rotateIfNecessary(file.absolutePath, thePic)
        } catch (e: Exception) {
            LogUtils.e(LOG_TAG, e.toString())
        }
        var mediaFileName = file.absolutePath.substring(file.absolutePath.lastIndexOf("/") + 1, file.absolutePath.length)
        mediaFileName = mediaFileName.substring(0, mediaFileName.lastIndexOf("."))
        val mediaExtension = file.absolutePath.substring(file.absolutePath.lastIndexOf(".") + 1)
        try {
            // ensuring a 100 millisecond delay so that thumbnail image name is different than the camera captured image name
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        try {
            // create thumbnail and return as base64
            createImageThumbnail(file.absolutePath, thePic, isPortrait)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onImage(file.absolutePath, mediaFileName, mediaExtension)
    }

    fun onImagePick(
        result: ActivityResult,
        thumbnailFilePath: String,
        onCancel: () -> Unit,
        onResult: (filePath: String, fileName: String, fileExtension: String, intent: Intent) -> Unit
    ) {
        val selectedUri = getUri(result, onCancel) ?: return
        if (MediaUtils.isGoogleDrivePhotos(selectedUri)) {
            saveFile(selectedUri, result.resultCode)
            return
        }
        if (fileContext != null && (fileContext.equals(FOR_MESSAGE, true) ||
                    fileContext.equals(MediaConstants.FOR_PROFILE, true))
        ) {
            getImageForGalleryFooter(selectedUri, thumbnailFilePath, onResult)
        }
    }

    fun onVideoPick(
        result: ActivityResult,
        onResult: (filePath: String, fileName: String, fileExtension: String, uri: Uri) -> Unit,
        onCancel: () -> Unit
    ) {
        val selectedUri = getUri(result, onCancel) ?: return
        val filePath = getPath(context, selectedUri)
        if (filePath.isNullOrEmpty()) {
            saveFile(selectedUri, result.resultCode)
            return
        }
        if (fileContext.equals(FOR_MESSAGE, ignoreCase = true)) {
            LogUtils.d("", "onActivityResult() :: file absolute path::${filePath}")
            var fileName = filePath.substring(filePath.lastIndexOf("/") + 1)
            fileName = fileName.substring(0, fileName.lastIndexOf("."))
            val fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1)
            onResult(filePath, fileName, fileExtension, selectedUri)
        }
    }

    fun onCaptureVideo(
        cameraMediaUri: Uri?,
        thumbnailFilePath: String,
        onCancel: () -> Unit,
        onResult: (filePath: String, fileName: String, fileExtension: String, intent: Intent) -> Unit
    ) {
        if (fileContext.equals(FOR_MESSAGE, ignoreCase = true) && cameraMediaUri != null) {
            getImageForGalleryFooter(cameraMediaUri, thumbnailFilePath, onResult)
        } else {
            onCancel()
        }
    }

    fun onFilePick(
        result: ActivityResult,
        onResult: (filePath: String, fileName: String, fileExtension: String, selectedFile: Uri) -> Unit,
        onCancel: () -> Unit
    ) {
//        val selectedFile: Uri? = getUri(result, onCancel)
//        LogUtils.d(LOG_TAG, "onActivityResult() :: resultCode OK :: CHOOSE_FILE")
//        if (selectedFile == null) {
//            LogUtils.d(LOG_TAG, "onActivityResult() :: resultCode OK :: The Uri is =$selectedFile")
//            Toast.makeText(context, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show()
//            return
//        }
//        val realPath = getRealPath(context, selectedFile)
//        var realFileName = ""
//        if (realPath != null) {
//            realFileName = try {
//                realPath.substring(realPath.lastIndexOf("/") + 1)
//            } catch (e: java.lang.Exception) {
//                Toast.makeText(context, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show()
//                onCancel()
//                return
//            }
//            val fileExtension = getExtensionFromFileName(realFileName) // will not be null but "" and will return as not whitelisted
//            val file = File(realPath)
//            val fileName = file.absolutePath.substring(file.absolutePath.lastIndexOf("/") + 1)
//            onResult(file.absolutePath, fileName, fileExtension, selectedFile)
//        } else {
//            saveFile(selectedFile, result.resultCode)
//            return
//        }
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val selectedFile = data.data
                val fileExtension: String
                if (selectedFile == null) {
                    Toast.makeText(context, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show()
                    onCancel()
                    return
                }
                val realPath: String? = getFileNameByUri(context, selectedFile)
                var realFileName = ""
                if (realPath != null) {
                    realFileName = try {
                        realPath.substring(realPath.lastIndexOf("/") + 1)
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(context, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show()
                        onCancel()
                        return
                    }
                    fileExtension = getExtensionFromFileName(realFileName)
                    var file: File? = null
                    try {
                        file = MediaUtils.getOutputMediaFile(BitmapUtils.obtainMediaTypeOfExtn(fileExtension), realFileName)
                    } catch (e: NoExternalStorageException) {
                        e.printStackTrace()
                    } catch (e: NoWriteAccessException) {
                        e.printStackTrace()
                    }
                    var filePath = ""
                    if (file != null) {
                        filePath = file.absolutePath
                    }
                    if (fileExtension != BitmapUtils.EXT_VIDEO) MediaUtils.saveFileToKorePath(realPath, filePath)
                    val fileName = filePath.substring(filePath.lastIndexOf("/") + 1)
                    onResult(filePath, fileName, fileExtension, selectedFile)
                } else {
                    val returnCursor: Cursor? = context.contentResolver.query(selectedFile, null, null, null, null)
                    if (returnCursor != null) {
                        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        returnCursor.moveToFirst()
                        val fileName = returnCursor.getString(nameIndex)
                        fileExtension = getExtensionFromFileName(fileName)
                        val filePath = saveFileToKoreWithStream(context, selectedFile, fileName, fileExtension)
                        returnCursor.close()
                        if (filePath != null) {
                            onResult(filePath, fileName, fileExtension, selectedFile)
                        }
                    }
                }
            } else {
                onCancel()
            }
        } else {
            onCancel()
        }
    }

    private fun getFileNameByUri(context: Context, uri: Uri): String? {
        var filepath: String? = ""
        val file: File
        if (uri.scheme?.compareTo("content") == 0) {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION),
                null,
                null,
                null
            )
            if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                val mImagePath = cursor.getString(columnIndex)
                cursor.close()
                filepath = mImagePath
            }
        } else if (uri.scheme!!.compareTo("file") == 0) {
            try {
                file = File(URI(uri.toString()))
                if (file.exists()) filepath = file.absolutePath
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        } else {
            filepath = uri.path
        }
        return filepath
    }

    fun onCaptureImage(currentMediaPath: String, onResult: (filePath: String, fileName: String, fileExtension: String) -> Unit) {
        LogUtils.d("", "onActivityResult() :: Camera capture...")
        //get the Uri for the captured image
        if (fileContext.equals(FOR_MESSAGE, true) || fileContext.equals(MediaConstants.FOR_PROFILE, true)) {
            getFullImage(currentMediaPath) { filePath, fileName, fileExtension ->
                onResult(filePath, fileName, fileExtension)
            }
        }
    }

    private fun getUri(result: ActivityResult, onCancel: () -> Unit): Uri? {
        if (result.resultCode != Activity.RESULT_OK) {
            onCancel()
            return null
        }
        val selectedUri = result.data?.data
        if (selectedUri == null) {
            onCancel()
        }
        return selectedUri
    }

    private fun getImageForGalleryFooter(
        imageUri: Uri,
        thumbnailFilePath: String,
        onResult: (filePath: String, fileName: String, fileExtension: String, intent: Intent) -> Unit
    ) {
        var mediaFilePath = ""
        var mediaFileName = ""
        var mediaFileExtension = ""
        var fOut: OutputStream? = null
        try {
            val highResBitmap = if (imageUri.toString().endsWith(".mp4")) {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(context, imageUri)
                    // Get frame at the 1st second as a thumbnail
                    retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                } catch (ex: IllegalArgumentException) {
                    // Handle any exceptions that occur when setting data source
                    ex.printStackTrace()
                } finally {
                    retriever.release()
                }
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
            var bitmapPic: Bitmap = getScaledBitmap(highResBitmap as Bitmap)
            LogUtils.d(
                LOG_TAG,
                "getImageForGalleryFooter() :: ***** picture height ::" + bitmapPic?.height + " and width::" + bitmapPic?.width
            )
            try {
                val returnCursor: Cursor? = context.contentResolver.query(imageUri, null, null, null, null)
                val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor?.moveToFirst()
                nameIndex?.let { returnCursor.getString(it) }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            val mediaType = this.mediaType ?: MEDIA_TYPE_IMAGE
            val file: File = MediaUtils.getOutputMediaFile(mediaType, null)
            fOut = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.newOutputStream(file.toPath())
            } else FileOutputStream(file)
            bitmapPic.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fOut!!)
            fOut.flush()
            mediaFilePath = file.absolutePath
            LogUtils.d(LOG_TAG, " file absolute path::$mediaFilePath")
            mediaFileName = mediaFilePath.substring(mediaFilePath.lastIndexOf("/") + 1, mediaFilePath.length)
            mediaFileName = mediaFileName.substring(0, mediaFileName.lastIndexOf("."))
            mediaFileExtension = mediaFilePath.substring(mediaFilePath.lastIndexOf(".") + 1)
            bitmapPic = rotateIfNecessary(mediaFilePath, bitmapPic)

            // create thumbnail and return as base64
            createImageThumbnail(mediaFilePath, bitmapPic, isPortrait)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            fOut?.close()
        }

        //display the returned cropped image
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_FILE_NAME, mediaFileName)
        resultIntent.putExtra(EXTRA_FILE_PATH, mediaFilePath)
        resultIntent.putExtra(EXTRA_THUMBNAIL_FILE_PATH, thumbnailFilePath)
        resultIntent.putExtra(EXTRA_FILE_EXT, mediaFileExtension)
        resultIntent.putExtra(EXTRA_FILE_URI, imageUri)
        onResult(mediaFilePath, mediaFileName, mediaFileExtension, resultIntent)
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    private fun getPath(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (MediaUtils.isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (MediaUtils.isDownloadsDocument(uri)) {
                var id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    id = id.replaceFirst("raw:".toRegex(), "")
                } else if (id.startsWith("msf:")) {
                    id = id.replaceFirst("msf:".toRegex(), "")
                }
                if (id.contains("/Download/")) return id
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return try {
                    MediaUtils.getDataColumn(context, contentUri, null, null)
                } catch (e: java.lang.Exception) {
                    null
                }
            } else if (MediaUtils.isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                var contentUri: Uri? = null
                contentUri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return MediaUtils.getDataColumn(context, contentUri, selection, selectionArgs)
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return null
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun saveFile(uri: Uri, resultCode: Int) {
        // Start the coroutine
        CoroutineScope(Dispatchers.Main).launch {
            var fileName: String? = null
            var filePath: String? = null
            var fileExtension = ""
            withContext(Dispatchers.IO) {
                val returnCursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
                val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor?.moveToFirst()
                fileName = returnCursor?.getString(nameIndex!!)
                fileExtension = getExtensionFromFileName(fileName) // will not be null but "" and will return as not whitelisted

                filePath = saveFileToKoreWithStream(context, uri, fileName!!, fileExtension)
            }

            filePath?.let {
                fileName = it.substring(it.lastIndexOf("/") + 1)
            }
            getView()?.onFileSaved(filePath, fileName, fileExtension, resultCode)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    @Throws(IOException::class)
    fun createVideoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val videoFileName = "VIDEO_$timeStamp"
        val mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File.createTempFile(videoFileName, ".mp4", mediaStorageDir)
    }
}