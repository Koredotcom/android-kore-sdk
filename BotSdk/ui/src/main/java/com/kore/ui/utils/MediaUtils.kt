package com.kore.ui.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import com.kore.common.constants.MediaConstants
import com.kore.common.utils.LogUtils
import com.kore.exception.NoExternalStorageException
import com.kore.exception.NoWriteAccessException
import com.kore.ui.utils.BitmapUtils.obtainMediaTypeOfExtn
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaUtils {

    companion object {
        private const val MEDIA_APP_FOLDER = "Kore"
        private const val DOWNLOADED_IMAGE_FOLDER = "Kore_Image"
        private const val DOWNLOADED_AUDIO_FOLDER = "Kore_Audio"
        private const val DOWNLOADED_VIDEO_FOLDER = "Kore_Video"
        private const val DOWNLOADED_DOCUMENT_FOLDER = "Kore_Document"
        private const val DOWNLOAD_ARCHIVE_FOLDER = "Kore_Archive"
        private const val LOG_TAG = "MediaUtils"
        private var mediaStorageDir: File? = null

        fun setupAppDir(context: Context, type: String) {
            try {
                val filesDirectory = context.filesDir.absolutePath + File.separator + MEDIA_APP_FOLDER
                mediaStorageDir = if (type.equals(MediaConstants.MEDIA_TYPE_AUDIO, ignoreCase = true))
                    File(filesDirectory, DOWNLOADED_AUDIO_FOLDER)
                else if (type.equals(MediaConstants.MEDIA_TYPE_VIDEO, ignoreCase = true)) {
                    File(filesDirectory, DOWNLOADED_VIDEO_FOLDER)
                } else if (type.equals(MediaConstants.MEDIA_TYPE_IMAGE, ignoreCase = true)) {
                    File(filesDirectory, DOWNLOADED_IMAGE_FOLDER)
                } else if (type.equals(MediaConstants.MEDIA_TYPE_ARCHIVE, ignoreCase = true)) {
                    File(filesDirectory, DOWNLOAD_ARCHIVE_FOLDER)
                } else if (type.equals(MediaConstants.MEDIA_TYPE_DOCUMENT, ignoreCase = true)) {
                    File(filesDirectory, DOWNLOADED_DOCUMENT_FOLDER)
                } else null

                // Create the storage directory if it does not exist
                if (mediaStorageDir?.exists() == false) {
                    val isSuccess = mediaStorageDir?.mkdirs()
                    if (isSuccess == false) LogUtils.e(LOG_TAG, "failed to create App directory");
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Create a File for saving an image or video
         */
        @Throws(NoExternalStorageException::class, NoWriteAccessException::class)
        fun getOutputMediaFile(type: String, mediaFileName: String?): File {
            // Create a media file name
            var fileName = mediaFileName
            if (fileName != null && fileName.indexOf(".") > 0) fileName = fileName.substring(0, fileName.lastIndexOf("."))
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date())
            val appDirPath = getAppDir()
            var mediaFile = File(appDirPath)
            var attemptCount = 0
            if (!mediaFile.exists()) mediaFile.mkdirs()
            while (mediaFile.exists()) {
                var name = if (fileName.isNullOrEmpty()) timeStamp else fileName
                if (attemptCount != 0) name += attemptCount
                attemptCount++
                mediaFile = if (type.equals(MediaConstants.MEDIA_TYPE_AUDIO, ignoreCase = true)) {
                    File(appDirPath + File.separator + name + getMediaExtension(MediaConstants.MEDIA_TYPE_AUDIO, false))
                } else if (type.equals(MediaConstants.MEDIA_TYPE_VIDEO, ignoreCase = true)) {
                    File(appDirPath + File.separator + name + getMediaExtension(MediaConstants.MEDIA_TYPE_VIDEO, false))
                } else if (type.equals(MediaConstants.MEDIA_TYPE_IMAGE, ignoreCase = true)) {
                    File(appDirPath + File.separator + name + getMediaExtension(MediaConstants.MEDIA_TYPE_IMAGE, false))
                } else if (type.equals(MediaConstants.MEDIA_TYPE_ARCHIVE, ignoreCase = true)) {
                    File(appDirPath + File.separator + name + ".kore") //".kore"
                } else {
                    File(appDirPath + File.separator + name + "." + type) //".kore"
                }
            }
            return mediaFile
        }

        @Throws(NoExternalStorageException::class, NoWriteAccessException::class)
        fun getAppDir(): String? {
            return mediaStorageDir?.path
        }

        fun saveFileToKoreWithStream(mContext: Context, uri: Uri?, fileName: String, extn: String): String? {
            if (uri == null) return null
            try {
                val contentResolver = mContext.contentResolver
                val file = getOutputMediaFile(obtainMediaTypeOfExtn(extn), fileName)
                val inputStream = contentResolver.openInputStream(uri)
                val out: OutputStream = FileOutputStream(file)
                var read: Int
                val bytes = ByteArray(1024)
                while (inputStream!!.read(bytes).also { read = it } != -1) {
                    out.write(bytes, 0, read)
                }
                inputStream.close()
                out.close()
                LogUtils.d("file create", "success scenario$fileName$extn")
                return file.absolutePath
            } catch (e: Exception) {
                LogUtils.d("file create", "fail scenario")
                e.printStackTrace()
            }
            return null
        }

        fun saveFileToKorePath(sourceFilePath: String?, destinationFilePath: String?) {
            var bis: BufferedInputStream? = null
            var bos: BufferedOutputStream? = null
            var fos: FileOutputStream? = null
            var fis: FileInputStream? = null
            if (sourceFilePath == null) return
            try {
                fis = FileInputStream(sourceFilePath)
                bis = BufferedInputStream(fis)
                fos = FileOutputStream(destinationFilePath, false)
                bos = BufferedOutputStream(fos)
                val buf = ByteArray(1024)
                do {
                    bos.write(buf)
                } while (bis.read(buf) != -1)
                bos.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    bos?.close()
                    fos?.close()
                    bis?.close()
                    fis?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun getMediaExtension(MEDIA_TYPE: String, isPlain: Boolean): String {
            var audioExtn: String?
            var videoExtn: String?
            if (isPlain) {
                audioExtn = "m4a"
                videoExtn = "mp4"
            } else {
                audioExtn = ".m4a"
                videoExtn = ".mp4"
            }
            return if (MEDIA_TYPE.equals(MediaConstants.MEDIA_TYPE_VIDEO, ignoreCase = true)) {
                videoExtn
            } else if (MEDIA_TYPE.equals(MediaConstants.MEDIA_TYPE_IMAGE, ignoreCase = true)) {
                if (isPlain) "jpg" else ".jpg"
            } else {
                audioExtn
            }
        }

        @JvmStatic
        fun getRealPath(context: Context, uri: Uri): String? {
            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    var id = DocumentsContract.getDocumentId(uri)
                    id = id.substring(id.lastIndexOf(":") + 1)
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:".toRegex(), "")
                        }

                        val contentUriPrefixesToTry = arrayOf(
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                        )

                        for (contentUriPrefix in contentUriPrefixesToTry) {
                            val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), id.toLong())
                            try {
                                val path: String? = getDataColumn(context, contentUri, null, null)
                                if (path != null) {
                                    return path
                                }
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    var contentUri = uri
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address
                if (isGooglePhotosUri(uri)) return uri.lastPathSegment
                if (isDeviceContactsUri(uri)) return uri.lastPathSegment
                return if (isGoogleDriveDocument(uri)) {
                    null
                } else getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        private fun isGoogleDriveDocument(uri: Uri): Boolean {
            return "com.google.android.apps.docs.storage.legacy" == uri.authority || "com.google.android.apps.docs.storage" == uri.authority
        }

        fun isGoogleDrivePhotos(uri: Uri): Boolean {
            return "com.google.android.apps.docs.storage" == uri.authority
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
            if (uri == null) return null
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority || "com.android.providers.media.photopicker" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is GooglePhotos.
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Contacts.
         */
        private fun isDeviceContactsUri(uri: Uri): Boolean {
            return "com.android.contacts" == uri.authority
        }
    }
}