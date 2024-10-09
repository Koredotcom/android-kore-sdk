package com.kore.ui.utils

import android.content.res.Resources
import android.graphics.Color

object ColorTemplate {
    val MATERIAL_COLORS = intArrayOf(rgb("#4A9AF2"), rgb("#5BC8C4"), rgb("#e74c3c"), rgb("#3498db"))
    fun rgb(hex: String): Int {
        val color = hex.replace("#", "").toLong(16).toInt()
        val r = color shr 16 and 255
        val g = color shr 8 and 255
        val b = color and 255
        return Color.rgb(r, g, b)
    }

    val holoBlue: Int
        get() = Color.rgb(51, 181, 229)

    fun colorWithAlpha(color: Int, alpha: Int): Int {
        return color and 16777215 or (alpha and 255 shl 24)
    }

    fun createColors(r: Resources, colors: IntArray): List<Int?> {
        val result: ArrayList<Int> = ArrayList()
        val var4 = colors.size
        for (var5 in 0 until var4) {
            val i = colors[var5]
            result.add(r.getColor(i))
        }
        return result
    }

    fun createColors(colors: IntArray): List<Int?> {
        val result: ArrayList<Int> = ArrayList()
        val var3 = colors.size
        for (var4 in 0 until var3) {
            val i = colors[var4]
            result.add(i)
        }
        return result
    }
}