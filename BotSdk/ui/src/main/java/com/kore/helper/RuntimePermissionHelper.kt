package com.kore.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RuntimePermissionHelper(
    private val activity: Activity,
    private val permissionLauncher: ActivityResultLauncher<Array<String>>?,
    private val settingsLauncher: ActivityResultLauncher<Intent>?
) {
    companion object {
        private const val SCHEME_PACKAGE = "package"
        private const val TITLE_PERMISSION_NEEDED = "Permission Needed"
        private const val TITLE_PERMISSION_DENIED = "Permission Denied"
        private const val MESSAGE1 = "We need this permission. Please grant the permission."
        private const val MESSAGE2 = "Without this permission, some features of the app may not work."

        fun hasPermissions(context: Context, permissions: List<String>): Boolean {
            var isGranted = true
            for (permission in permissions) {
                isGranted = isGranted && context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
            return isGranted
        }
    }

    /**
     * @param requestCode : The user's request code
     * @param permissions  : Array of desired permissions.
     */
    fun requestPermissions(permissions: List<String>) {
        val shouldShowRequestPermissionRationale: Boolean = shouldShowRationale(permissions)
        if (shouldShowRequestPermissionRationale) {
            permissionLauncher?.launch(permissions.toTypedArray())
        } else {
            permissionLauncher?.launch(permissions.toTypedArray())
        }
    }

    private fun shouldShowRationale(permissions: List<String>): Boolean {
        var shouldShowRequestPermissionRationale = false
        for (permission in permissions) {
            shouldShowRequestPermissionRationale =
                shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }
        return shouldShowRequestPermissionRationale
    }

    fun showPermissionRationaleDialog(permission: String, cancelTitle: String, cancelMsg: String, onCancel: () -> Unit) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(cancelTitle.ifEmpty { TITLE_PERMISSION_NEEDED })
            .setMessage(cancelMsg.ifEmpty { MESSAGE1 })
            .setPositiveButton("OK") { _, _ ->
                permissionLauncher?.launch(arrayOf(permission))
            }
            .setNegativeButton("Cancel") { _, _ ->
                onCancel()
            }
            .show()
    }

    fun showPermissionDeniedDialog(cancelTitle: String, cancelMsg: String, onCancel: () -> Unit = {}) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(cancelTitle.ifEmpty { TITLE_PERMISSION_DENIED })
            .setMessage(cancelMsg.ifEmpty { MESSAGE2 })
            .setPositiveButton("OK") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                onCancel()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts(SCHEME_PACKAGE, activity.packageName, null)
        intent.data = uri
        settingsLauncher?.launch(intent)
    }
}