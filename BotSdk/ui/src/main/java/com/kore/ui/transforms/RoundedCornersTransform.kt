package com.kore.ui.transforms

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.squareup.picasso.Transformation
import androidx.core.graphics.createBitmap

class RoundedCornersTransform : Transformation {
    fun setR(r: Float) {
        this.r = r
    }

    private var r = 0f
    override fun transform(source: Bitmap): Bitmap? {
        try {
            val size = source.width.coerceAtMost(source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) source.recycle()
            val config: Bitmap.Config? = source.config
            val bitmap = createBitmap(size, size, config ?: Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader
            paint.isAntiAlias = true
            if (r == 0f) {
                r = size / 8f
            }
            canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), r, r, paint)
            squaredBitmap.recycle()
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun key(): String {
        return "rounded_corners"
    }
}