package com.kore.common.utils

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.kore.botclient.R

class ToastUtils {

    companion object {

        @JvmOverloads
        fun showToast(context: Context, msg: String, len: Int = Toast.LENGTH_SHORT) {
            var length = len
            if (!(length == Toast.LENGTH_LONG || length == Toast.LENGTH_SHORT)) {
                length = Toast.LENGTH_SHORT
            }
            val yOffset = context.resources.getDimensionPixelSize(R.dimen.toast_y_offset_default)
            val gravity = context.resources.getInteger(R.integer.toast_gravity_default)
            showToast(context, msg, length, gravity, 0, yOffset)
        }

        /**
         * Centralized method to show toast message
         *
         * @param msg     Message to be displayed
         * @param length  Toast.LENGTH_SHORT or Toast.LENGTH_LONG
         * @param gravity Anchor point
         * @param xOffset x-offset
         * @param yOffset y-0ffset
         */
        fun showToast(context: Context, msg: String, length: Int, gravity: Int, xOffset: Int, yOffset: Int) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                val toast = Toast.makeText(context, msg, length)
                toast.setGravity(gravity, xOffset, yOffset)
                toast.show()
            } else {
                if (context is Activity) {
                    context.runOnUiThread {
                        val toast = Toast.makeText(context, msg, length)
                        toast.setGravity(gravity, xOffset, yOffset)
                        toast.show()
                    }
                }
            }
        }
    }
}