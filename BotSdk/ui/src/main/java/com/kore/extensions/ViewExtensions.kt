package com.kore.extensions

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

fun View.setRoundedCorner(radius: Float) {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    clipToOutline = true
}