package com.kore.ui.audiocodes.webrtcclient.general

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import androidx.core.graphics.toColorInt
import com.kore.common.utils.LogUtils
import java.io.FileNotFoundException

object ImageUtils {
    private const val TAG = "ImageUtils"
    fun getContactBitmapFromURI(context: Context, uri: Uri?): Bitmap? {
        try {
            val input = context.contentResolver.openInputStream(uri!!) ?: return null
            return BitmapFactory.decodeStream(input)
        } catch (e: FileNotFoundException) {
            LogUtils.e(TAG, "error: $e")
        }
        return null
    }

    @JvmStatic
    fun getCroppedRoundBitmap(bmp: Bitmap?, radius: Int): Bitmap {
        val resizedBitmap = Bitmap.createScaledBitmap(bmp!!, radius, radius, false)
        return getCroppedRoundBitmap(resizedBitmap)
    }

    private fun getCroppedRoundBitmap(bmp: Bitmap): Bitmap {
        val bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true)
        val w = bmp.width
        val h = bmp.height
        val radius = if (h < w) h else w
        return getCroppedBitmap(bitmap, radius)
    }

    private fun getCroppedBitmap(bmp1: Bitmap, radius: Int): Bitmap {
        var bmp = bmp1
        bmp = cropToSquare(bmp)
        val sbmp: Bitmap = if (bmp.width == radius && bmp.height == radius) {
            bmp
        } else {
            Bitmap.createScaledBitmap(bmp, radius, radius, false)
        }
        val output = Bitmap.createBitmap(sbmp.width, sbmp.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, sbmp.width, sbmp.height)
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = "#ffffff".toColorInt()
        canvas.drawCircle((sbmp.width / 2).toFloat(), (sbmp.height / 2).toFloat(), (sbmp.width / 2).toFloat(), paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(sbmp, rect, rect, paint)
        return output
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH
        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight)
    }
}
