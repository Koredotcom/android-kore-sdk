package com.kore.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.common.constants.MediaConstants
import com.kore.common.extensions.dpToPx
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

object BitmapUtils {
    private val LOG_TAG = BitmapUtils::class.java.simpleName

    const val ORIENTATION_LS = "landscape"
    const val ORIENTATION_PT = "portrait"
    private const val EXT_DOTX = "dotx"
    private const val EXT_DOTM = "dotm"
    private const val EXT_DOCM = "docm"
    private const val EXT_DOT = "dot"
    private const val EXT_XLA = "xla"
    private const val EXT_XLT = "xlt"
    private const val EXT_XLL = "xll"
    private const val EXT_XLW = "xlw"
    private const val EXT_XLSB = "xlsb"
    private const val EXT_XLSM = "xlsm"
    private const val EXT_XLTX = "xltx"
    private const val EXT_XLTM = "xltm"
    private const val EXT_XLAM = "xlam"
    private const val EXT_POTM = "potm"
    private const val EXT_POTX = "potx"
    private const val EXT_SLDX = "sldx"
    private const val EXT_JPEG = "jpeg"
    private const val EXT_IMAGE = "image"
    private const val EXT_AUDIO_10 = "m4a"
    private const val EXT_AUDIO_9 = "amr"
    private const val EXT_AUDIO_MP3 = "mp3"
    private const val EXT_AUDIO_MPEG = "mpeg"
    private const val EXT_AUDIO_MID = "mid"
    private const val EXT_AUDIO_BASIC = "au"
    private const val EXT_AUDIO_SND = "snd"
    private const val EXT_AUDIO_WAV = "wav"
    private const val EXT_AUDIO_AAC = "aac"
    private const val EXT_AUDIO_PLAIN = "audio"
    private const val EXT_VIDEO_MOV = "mov"
    private const val EXT_VIDEO_FLV = "flv"
    private const val EXT_VIDEO_WMV = "wmv"
    private const val EXT_VIDEO_VOB = "vob"
    const val EXT_VIDEO = "mp4"
    private const val EXT_VIDEO_3GP = "3gp"
    private const val EXT_VIDEO_3GP2 = "3gp2"
    private const val EXT_VIDEO_PLAIN = "video"
    private const val EXT_7ZIP = "7z"
    private const val EXT_ZIP = "zip"
    private const val EXT_RAR = "rar"

    //3dimageFileFormats
    private const val EXT_PPT = "ppt"
    private const val EXT_PPTX = "pptx"
    private const val EXT_PDF = "pdf"

    //rasterimageFileFormats
    private const val EXT_BMP = "bmp"
    private const val EXT_GIF = "gif"
    private const val EXT_JPG = "jpg"
    private const val EXT_PNG = "png"

    //spreadsheetFileFormats
    private const val EXT_XLS = "xls"
    private const val EXT_XLSX = "xlsx"

    //textFileFormats
    private const val EXT_DOC = "doc"
    private const val EXT_DOCX = "docx"
    private const val EXT_TXT = "txt"

    //vectorimageFileFormats
    private const val EXT_AI = "ai"
    private const val EXT_EPS = "eps"
    private const val EXT_PS = "ps"
    private const val EXT_MPG = "mpg"
    private const val TYPE_DOC_ATTACHMENT = 101
    private const val TYPE_PPT_ATTACHMENT = 102
    private const val TYPE_EXCEL_ATTACHMENT = 103
    private const val TYPE_PDF_ATTACHMENT = 104
    private const val TYPE_IMAGE_ATTACHMENT = 105
    private const val TYPE_VIDEO_ATTACHMENT = 106
    private const val TYPE_AUDIO_ATTACHMENT = 107
    private const val TYPE_ZIP_ATTACHMENT = 108
    private const val TYPE_AI_ATTACHMENT = 110
    private const val TYPE_PS_ATTACHMENT = 111
    private const val TYPE_EPS_ATTACHMENT = 112
    private const val TYPE_OTHER_ATTACHMENT = 109
    private const val TYPE_TEXT = 113

    private const val THUMBNAIL_WIDTH = 320
    private const val THUMBNAIL_HEIGHT = 240
    private const val COMPRESS_QUALITY = 100

    private fun decodeBitmap(file: File, options: BitmapFactory.Options, rotationAngle: Float): Bitmap? {
        var bitmap: Bitmap? = null
        var bm: Bitmap? = null
        try {
            val fis = FileInputStream(file)
            options.inJustDecodeBounds = false
            bm = BitmapFactory.decodeStream(fis, null, options)
            val matrix = Matrix()
            matrix.setRotate(rotationAngle, bm!!.width.toFloat() / 2, bm.height.toFloat() / 2)
            bitmap = Bitmap.createBitmap(bm, 0, 0, options.outWidth, options.outHeight, matrix, true)
        } catch (oom: OutOfMemoryError) {
            bm?.recycle()
            options.inSampleSize *= 2
            bitmap = decodeBitmap(file, options, rotationAngle)
        } catch (oom: FileNotFoundException) {
            bm?.recycle()
            options.inSampleSize *= 2
            bitmap = decodeBitmap(file, options, rotationAngle)
        } finally {
            if (bm != null && bm != bitmap) bm.recycle()
        }
        return bitmap
    }

    fun decodeBitmapFromFile(filename: String?, reqWidth: Int, reqHeight: Int): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filename, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filename, options)
    }

    fun rotateIfNecessary(fileName: String?, bitmap: Bitmap): Bitmap {
        var innerbitmap = bitmap
        val exif: ExifInterface?
        try {
            exif = ExifInterface(fileName!!)
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("6", ignoreCase = true)) {
                innerbitmap = rotate(bitmap, 90)
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("8", ignoreCase = true)) {
                innerbitmap = rotate(bitmap, 270)
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equals("3", ignoreCase = true)) {
                innerbitmap = rotate(bitmap, 180)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return innerbitmap
    }

    fun decodeBitmapFromFile(file: File, reqWidth: Int, reqHeight: Int): Bitmap? {
        val exif: ExifInterface
        var orientString: String? = null
        try {
            exif = ExifInterface(file.absolutePath)
            orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = orientString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
        var rotationAngle = 0
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270
        val options = BitmapFactory.Options()
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return decodeBitmap(file, options, rotationAngle.toFloat())
    }

    /**
     * Calculate an inSampleSize for use in a [BitmapFactory.Options] object when decoding
     * bitmaps using the decode* methods from [BitmapFactory]. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     * method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight
                && halfWidth / inSampleSize > reqWidth
            ) {
                inSampleSize *= 2
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).
            var totalPixels = (width * height / inSampleSize).toLong()

            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = (reqWidth * reqHeight * 6).toLong()
            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2
                totalPixels /= 2
            }
        }
        return inSampleSize
    }

    fun rotate(bitmap: Bitmap?, degree: Int): Bitmap {
        val w = bitmap!!.width
        val h = bitmap.height
        val mtx = Matrix()
        mtx.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
    }

    fun getExtensionFromFileName(fileName: String?): String {
        var extn = ""
        if (fileName != null && fileName.contains(".")) {
            extn = fileName.substring(fileName.lastIndexOf(".") + 1)
        }
        return extn
    }

    fun categorisedAttachmentType(extn: String?): Int {
        return when (extn?.lowercase(Locale.getDefault())) {
            EXT_PDF -> TYPE_PDF_ATTACHMENT
            EXT_DOC, EXT_DOCX, EXT_DOTX, EXT_DOTM, EXT_DOCM, EXT_DOT -> TYPE_DOC_ATTACHMENT
            EXT_SLDX, EXT_PPT, EXT_PPTX, EXT_POTM, EXT_POTX -> TYPE_PPT_ATTACHMENT
            EXT_XLSX, EXT_XLSM, EXT_XLSB, EXT_XLS, EXT_XLL, EXT_XLA, EXT_XLAM, EXT_XLT, EXT_XLTM, EXT_XLW, EXT_XLTX -> TYPE_EXCEL_ATTACHMENT
            EXT_PNG, EXT_GIF, EXT_BMP, EXT_JPG, EXT_JPEG, EXT_IMAGE -> TYPE_IMAGE_ATTACHMENT
            EXT_VIDEO, EXT_VIDEO_MOV, EXT_VIDEO_FLV, EXT_VIDEO_VOB, EXT_VIDEO_WMV, EXT_MPG, EXT_VIDEO_3GP, EXT_VIDEO_3GP2, EXT_VIDEO_PLAIN -> TYPE_VIDEO_ATTACHMENT
            EXT_AUDIO_WAV, EXT_AUDIO_MP3, EXT_AUDIO_MPEG, EXT_AUDIO_MID, EXT_AUDIO_BASIC, EXT_AUDIO_SND, EXT_AUDIO_10, EXT_AUDIO_9, EXT_AUDIO_AAC, EXT_AUDIO_PLAIN -> TYPE_AUDIO_ATTACHMENT
            EXT_ZIP, EXT_7ZIP, EXT_RAR -> TYPE_ZIP_ATTACHMENT
            EXT_AI -> TYPE_AI_ATTACHMENT
            EXT_PS -> TYPE_PS_ATTACHMENT
            EXT_EPS -> TYPE_EPS_ATTACHMENT
            EXT_TXT -> TYPE_TEXT
            else -> TYPE_OTHER_ATTACHMENT
        }
    }

    @SuppressLint("LogConditional")
    fun createImageThumbnailForBulk(bitmap: Bitmap, thumbNailPath: String, compressQualityInt: Int): String {
        var thumbnail = bitmap
        var thumbPath = thumbNailPath
        val index = thumbPath.lastIndexOf(".")
        thumbPath = thumbPath.substring(0, index) + "_th.png"
        LogUtils.d("BitmapUtils", "createImageThumbnailForBulk() - thumbnail path = $thumbPath")
        val thumbnailFile = File(thumbPath)
        LogUtils.d("BitmapUtils", "createImageThumbnailForBulk() - thumbnail file name & path = " + thumbnailFile.absolutePath)
        try {
            val fos = FileOutputStream(thumbnailFile)
            thumbnail.compress(Bitmap.CompressFormat.PNG, compressQualityInt, fos)
            thumbnail = rotateIfNecessary(thumbPath, thumbnail)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            LogUtils.e("BitmapUtils", "createImageThumbnailForBulk() - Excep: = " + e.message)
        } finally {
            thumbnail.recycle()
        }
        return thumbPath
    }

    // determines extn media type. If type !image && !video && !audio then return extn
    // Method generally used with creating dir
    @JvmStatic
    fun obtainMediaTypeOfExtn(extension: String): String {
        return when (categorisedAttachmentType(extension)) {
            TYPE_IMAGE_ATTACHMENT -> MediaConstants.MEDIA_TYPE_IMAGE
            TYPE_VIDEO_ATTACHMENT -> MediaConstants.MEDIA_TYPE_VIDEO
            TYPE_AUDIO_ATTACHMENT -> MediaConstants.MEDIA_TYPE_AUDIO
            else -> extension
        }
    }

    fun getAttachmentType(extn: String?): String {
        return when (categorisedAttachmentType(extn)) {
            TYPE_IMAGE_ATTACHMENT -> MediaConstants.MEDIA_TYPE_IMAGE
            TYPE_VIDEO_ATTACHMENT -> MediaConstants.MEDIA_TYPE_VIDEO
            TYPE_AUDIO_ATTACHMENT -> MediaConstants.MEDIA_TYPE_AUDIO
            else -> MediaConstants.MEDIA_TYPE_ATTACHMENT
        }
    }

    fun getScaledBitmap(image: Bitmap): Bitmap {
        var actualHeight = image.height.toFloat()
        var actualWidth = image.width.toFloat()
        val maxHeight = 600f
        val maxWidth = 800f
        var imgRatio = actualWidth / actualHeight
        val maxRatio = maxWidth / maxHeight
        //        int compressionQuality = 50;//50 percent compression
//        KoreLogger.debugLog(LOG_TAG, "#############@@@@@@@@@@@@@@@@ Image size before compress ::" + sizeOf(image));
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                //adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight
                actualWidth = imgRatio * actualWidth
                actualHeight = maxHeight
            } else if (imgRatio > maxRatio) {
                //adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth
                actualHeight = imgRatio * actualHeight
                actualWidth = maxWidth
            } else {
                actualHeight = maxHeight
                actualWidth = maxWidth
            }
        }
        return Bitmap.createScaledBitmap(
            image,
            actualWidth.toInt(),
            actualHeight.toInt(),
            true
        ) //.compress(Bitmap.CompressFormat.JPEG, compressionQuality, stream);
    }

    fun getVideoIdFromFilePath(context: Context, uri: Uri?): Int {
        var photoId = 1
        val columns = arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.DURATION)
        val orderBy = MediaStore.Video.Media.DATE_TAKEN

        // TODO This will break if we have no matching item in the MediaStore.
        var cursor = context.contentResolver.query(uri!!, columns, null, null, orderBy)
        if (cursor == null) cursor =
            context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy)
        cursor!!.moveToLast()
        if (cursor.count > 0) {
            val image_column_index = cursor
                .getColumnIndex(MediaStore.Video.Media._ID)
            if (image_column_index != -1) {
                cursor.getString(image_column_index)
                photoId = cursor.getInt(image_column_index)
            }
        }
        cursor.close()
        return photoId
    }

    fun getBufferSize(mediaType: String?): Int {
        return when (mediaType) {
            MediaConstants.MEDIA_TYPE_IMAGE -> MediaConstants.BUFFER_SIZE_IMAGE
            MediaConstants.MEDIA_TYPE_AUDIO -> MediaConstants.BUFFER_SIZE_AUDIO
            else -> MediaConstants.BUFFER_SIZE_VIDEO
        }
    }

    fun createImageThumbnail(filePath: String, finalMap: Bitmap?, isPortrait: Boolean) {
        val scaledBitmap: Bitmap?
        var extBitmap: Bitmap?
        var originalOrientation = 0
        var degrees = 0
        if (finalMap == null) return
        val originalHeight = finalMap.height
        val originalWidth = finalMap.width
        val targetWidth: Int
        val targetHeight: Int
        val thumbnailFilePath: String
        LogUtils.d(LOG_TAG, "createImageThumbnail() :: original height$originalHeight")
        LogUtils.d(LOG_TAG, "createImageThumbnail() :: original width$originalWidth")
        if (finalMap.width <= THUMBNAIL_WIDTH && finalMap.height <= THUMBNAIL_HEIGHT) {
//            thumbnailFilePath = filePath
            return
        }
        try {
            val fullImageExif = ExifInterface(filePath)
            originalOrientation = fullImageExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: Original image orientation$originalOrientation")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (!isPortrait) {
            targetWidth = THUMBNAIL_WIDTH
            targetHeight = targetWidth * originalHeight / originalWidth
            LogUtils.d(
                LOG_TAG,
                "createImageThumbnail() :: calculated thumbnail height for landscape mode" + targetHeight + "and width is" + targetWidth
            )
        } else {
            targetHeight = THUMBNAIL_HEIGHT
            targetWidth = targetHeight * originalWidth / originalHeight
            LogUtils.d(
                LOG_TAG,
                "createImageThumbnail() :: calculated thumbnail width for portrait mode: width " + targetWidth + "and height " + targetHeight
            )
        }
        when (originalOrientation) {
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                degrees = 180
                LogUtils.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by $degrees")
            }

            ExifInterface.ORIENTATION_ROTATE_90 -> {
                degrees = 90
                LogUtils.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by $degrees")
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                degrees = 270
                LogUtils.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by $degrees")
            }
        }
        LogUtils.d(LOG_TAG, "createImageThumbnail() :: Thumbnail rotation is $degrees")
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        scaledBitmap = Bitmap.createScaledBitmap(finalMap, targetWidth, targetHeight, true)
        extBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, targetWidth, targetHeight, matrix, true)
        try {
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: target width$targetWidth")
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: target height$targetHeight")
            val ind = filePath.lastIndexOf(".")
            thumbnailFilePath = filePath.substring(0, ind) + "_th.png"
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: thumbNailPath:::::::::::::$thumbnailFilePath")
            val thumbnailFile = File(thumbnailFilePath)
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: thumbnailFileName & path::::::::::::::" + thumbnailFile.absolutePath)
            val out = FileOutputStream(thumbnailFile)
            extBitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, out)
            out.flush()
            out.close()
            extBitmap = rotateIfNecessary(thumbnailFile.absolutePath, extBitmap)
        } catch (ee: Exception) {
            ee.printStackTrace()
            LogUtils.e(LOG_TAG, "Error while create Image Thumbnail$ee")
        } finally {
            scaledBitmap.recycle()
            extBitmap?.recycle()
        }
    }

    fun loadImage(context: Context, imageContent: String, view: ImageView, placeHolder: Int, roundRadius: Int = -1) {
        var imageData = imageContent
        if (imageData.contains(",")) {
            imageData = imageData.substring(imageData.indexOf(",") + 1)
            val decodedString = Base64.decode(imageData.toByteArray(), Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            view.setImageBitmap(decodedByte)
        } else {
            var reqBuilder = Glide.with(context).load(imageData).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            if (roundRadius != -1) {
                reqBuilder = reqBuilder.apply(RequestOptions.bitmapTransform(RoundedCorners(roundRadius.dpToPx(context))))
            }
            reqBuilder.error(placeHolder).into<DrawableImageViewTarget>(DrawableImageViewTarget(view))
        }
    }
}