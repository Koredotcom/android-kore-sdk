package com.kore.widgets.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.kore.widgets.R

object WidgetUtils {
    @SuppressLint("MissingPermission")
    fun launchDialer(context: Context, url: String?) {
        try {
            val intent = Intent(if (hasPermission(context, Manifest.permission.CALL_PHONE)) Intent.ACTION_CALL else Intent.ACTION_DIAL)
            intent.data = Uri.parse(url)
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NO_HISTORY
                    or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
        }
    }

    fun showEmailIntent(activity: Activity, recipientEmail: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$recipientEmail")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        try {
            activity.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Error while launching email intent!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasPermission(context: Context?, vararg permission: String?): Boolean {
        var shouldShowRequestPermissionRationale = true
        val permissionLength = permission.size
        for (i in 0 until permissionLength) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                    ActivityCompat.checkSelfPermission(context!!, permission[i]!!) == PackageManager.PERMISSION_GRANTED
        }
        return shouldShowRequestPermissionRationale
    }
}