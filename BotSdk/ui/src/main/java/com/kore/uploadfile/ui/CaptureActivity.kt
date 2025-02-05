package com.kore.uploadfile.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.kore.common.constants.MediaConstants
import com.kore.common.constants.MediaConstants.Companion.EXTRA_ACTION
import com.kore.common.constants.MediaConstants.Companion.EXTRA_DOCUMENT_MIME
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_CONTEXT
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_EXT
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_NAME
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_PATH
import com.kore.common.constants.MediaConstants.Companion.EXTRA_FILE_URI
import com.kore.common.constants.MediaConstants.Companion.EXTRA_MEDIA_TYPE
import com.kore.common.constants.MediaConstants.Companion.EXTRA_PICK_TYPE
import com.kore.common.constants.MediaConstants.Companion.EXTRA_THUMBNAIL_FILE_PATH
import com.kore.common.utils.LogUtils
import com.kore.helper.RuntimePermissionHelper
import com.kore.helper.RuntimePermissionHelper.Companion.hasPermissions
import com.kore.ui.R
import com.kore.ui.base.BaseActivity
import com.kore.ui.databinding.ActivityCaptureMediaBinding
import com.kore.ui.utils.MediaUtils.Companion.setupAppDir
import java.io.IOException

class CaptureActivity : BaseActivity<ActivityCaptureMediaBinding, CaptureView, CaptureViewModel>(), CaptureView {
    companion object {
        private val LOG_TAG = CaptureActivity::class.java.simpleName
    }

    private val runtimeMediaPermissions = ArrayList<String>()
    private lateinit var filePickMimes: Array<String>
    private val botChatViewModel: CaptureViewModel by viewModels()
    private var mediaPickType: String? = null
    private var fileContext: String? = null
    private var mediaType: String? = MediaConstants.MEDIA_TYPE_IMAGE

    private var mediaFileName: String? = null
    private var mediaFilePath: String? = null
    private var mediaExtension: String? = null
    private var thumbnailFilePath: String = ""

    private lateinit var currentMediaPath: String
    private var cameraMediaUri: Uri? = null

    private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isPermissionGranted = true
            for (permission in permissions.keys) {
                if (permissions[permission] == true) {
                    continue
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    isPermissionGranted = false
                    runtimePermissionHelper.showPermissionRationaleDialog(permission, "", "") { finishAndCancelOperation() }
                    break
                } else {
                    isPermissionGranted = false
                    runtimePermissionHelper.showPermissionDeniedDialog("", "") { finishAndCancelOperation() }
                    break
                }
            }
            if (!isPermissionGranted) return@registerForActivityResult
            openMediaIntent(mediaPickType)
        }
    private val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        checkForPermissionAccessAndRequest()
    }

    private val runtimePermissionHelper = RuntimePermissionHelper(this, requestPermissionsLauncher, settingsLauncher)

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode != Activity.RESULT_OK) {
            finishAndCancelOperation()
            return@registerForActivityResult
        }
        botChatViewModel.onImagePick(result, thumbnailFilePath, {}) { filePath, fileName, fileExtension, intent ->
            mediaFilePath = filePath
            mediaFileName = fileName
            mediaExtension = fileExtension
            intent.putExtra(EXTRA_FILE_URI, cameraMediaUri)
            intent.putExtra(EXTRA_FILE_PATH, filePath)
            setResult(result.resultCode, intent)
            finish()
        }
    }
    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode != Activity.RESULT_OK) {
            finishAndCancelOperation()
            return@registerForActivityResult
        }
        botChatViewModel.onVideoPick(
            result,
            { filePath, fileName, fileExtension, uri ->
                //display the returned video
                mediaFilePath = filePath
                mediaFileName = fileName
                mediaExtension = fileExtension
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_FILE_URI, uri)
                resultIntent.putExtra(EXTRA_FILE_PATH, filePath)
                setResult(result.resultCode, resultIntent)
                finish()
            },
            { finishAndCancelOperation() })
    }
    private val captureVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode != Activity.RESULT_OK) {
            finishAndCancelOperation()
            return@registerForActivityResult
        }
        botChatViewModel.onCaptureVideo(cameraMediaUri, thumbnailFilePath, { finishAndCancelOperation() },
            { filePath, fileName, fileExtension, intent ->
                mediaFilePath = filePath
                mediaFileName = fileName
                mediaExtension = fileExtension
                setResult(result.resultCode, intent)
                finish()
            })
    }
    private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode != Activity.RESULT_OK) {
            finishAndCancelOperation()
            return@registerForActivityResult
        }
        botChatViewModel.onFilePick(
            result,
            { filePath, fileName, fileExtension, selectedFile ->
                mediaFilePath = filePath
                mediaFileName = fileName
                mediaExtension = fileExtension
                finishOperation(selectedFile, fileExtension, result.resultCode)
            },
            { finishAndCancelOperation() }
        )
    }
    private val captureImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode != Activity.RESULT_OK) {
            finishAndCancelOperation()
            return@registerForActivityResult
        }
        botChatViewModel.onCaptureImage(currentMediaPath) { filePath, fileName, fileExtension ->
            mediaFilePath = filePath
            mediaFileName = fileName
            mediaExtension = fileExtension
            val resultIntent = if (result.resultCode != Activity.RESULT_OK || result.data == null) Intent() else result.data!!
            resultIntent.putExtra(EXTRA_ACTION, "IMAGE_DONE")
            resultIntent.putExtra(EXTRA_FILE_NAME, mediaFileName)
            resultIntent.putExtra(EXTRA_FILE_PATH, mediaFilePath)
            resultIntent.putExtra(EXTRA_THUMBNAIL_FILE_PATH, thumbnailFilePath)
            resultIntent.putExtra(EXTRA_FILE_URI, cameraMediaUri)
            setResult(result.resultCode, resultIntent)
            finish()
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_capture_media

    override fun getBindingVariable(): Int = -1

    override fun getViewModel(): CaptureViewModel = botChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePickMimes = resources.getStringArray(R.array.file_pick_mimes)
        val extras = intent.extras
        if (extras != null) {
            mediaPickType = extras.getString(EXTRA_PICK_TYPE)
            LogUtils.d(LOG_TAG, "onCreate() :: extras :: imagePickType ::::$mediaPickType")
            fileContext = extras.getString(EXTRA_FILE_CONTEXT)
            LogUtils.d(LOG_TAG, "onCreate() :: extras :: fileContext ::::$fileContext")
            mediaType = if (extras.containsKey(EXTRA_MEDIA_TYPE)) extras.getString(EXTRA_MEDIA_TYPE) else MediaConstants.MEDIA_TYPE_IMAGE
        }
        botChatViewModel.init(this, mediaPickType, fileContext, mediaType)
    }

    override fun init() {
        initMediaRuntimePermissions()
        checkForPermissionAccessAndRequest()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@CaptureActivity.finish()
            }
        })
    }

    private fun checkForPermissionAccessAndRequest() {
        if ((mediaPickType != MediaConstants.CHOOSE_TYPE_CAPTURE_IMAGE && mediaPickType != MediaConstants.CHOOSE_TYPE_CAPTURE_VIDEO && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) || hasPermissions(this, runtimeMediaPermissions)) {
            openMediaIntent(mediaPickType)
        } else {
            runtimePermissionHelper.requestPermissions(runtimeMediaPermissions)
        }
    }

    private fun initMediaRuntimePermissions() {
        when (mediaPickType) {
            MediaConstants.CHOOSE_TYPE_CAPTURE_IMAGE -> runtimeMediaPermissions.add(Manifest.permission.CAMERA)

            MediaConstants.CHOOSE_TYPE_CAPTURE_VIDEO -> {
                runtimeMediaPermissions.add(Manifest.permission.CAMERA)
                runtimeMediaPermissions.add(Manifest.permission.RECORD_AUDIO)
            }

            MediaConstants.CHOOSE_TYPE_IMAGE_PICK -> {
                runtimeMediaPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            MediaConstants.CHOOSE_TYPE_VIDEO_PICK -> {
                runtimeMediaPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun finishAndCancelOperation() {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_ACTION, "IMAGE_CANCEL")
        resultIntent.putExtra(EXTRA_FILE_NAME, "")
        setResult(Activity.RESULT_CANCELED, resultIntent)
        finish()
    }

    private fun finishOperation(uri: Uri?, fileExtension: String?, resultCode: Int) {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_ACTION, MediaConstants.CHOOSE_TYPE_DOCUMENT_PICK)
        resultIntent.putExtra(EXTRA_FILE_NAME, mediaFileName)
        resultIntent.putExtra(EXTRA_FILE_PATH, mediaFilePath)
        resultIntent.putExtra(EXTRA_FILE_URI, uri)
        resultIntent.putExtra(EXTRA_FILE_EXT, fileExtension)
        resultIntent.putExtra(EXTRA_MEDIA_TYPE, mediaType)
        setResult(resultCode, resultIntent)
        finish()
    }

    override fun showProgress(msg: String) {
        binding.progressBar.isVisible = true
    }

    override fun dismissProgress() {
        binding.progressBar.isVisible = false
    }

    override fun onFileSaved(filePath: String?, fileName: String?, fileExtension: String?, resultCode: Int) {
        mediaFilePath = filePath
        mediaFileName = fileName
        mediaExtension = fileExtension
        finishOperation(null, fileExtension, resultCode)
    }

    @SuppressLint("IntentReset")
    private fun openMediaIntent(mediaPickType: String?) {
        try {
            mediaType?.let { setupAppDir(this, it) }

            // in case of picture taken from camera capture
            if (MediaConstants.CHOOSE_TYPE_CAPTURE_IMAGE == mediaPickType) {
                //use standard intent to capture an image
                val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
                captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                try {
                    val file = botChatViewModel.createImageFile()
                    currentMediaPath = file.absolutePath
                    cameraMediaUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraMediaUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                captureImageLauncher.launch(captureIntent)
            } else if (MediaConstants.CHOOSE_TYPE_CAPTURE_VIDEO == mediaPickType) {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

                try {
                    val file = botChatViewModel.createVideoFile()
                    cameraMediaUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraMediaUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                captureVideoLauncher.launch(intent)
            } else if (MediaConstants.CHOOSE_TYPE_IMAGE_PICK == mediaPickType) {
                //use standard intent to pick an image from gallery
                val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(photoPickerIntent)
            } else if (mediaPickType == MediaConstants.CHOOSE_TYPE_VIDEO_PICK) {
                //use standard intent to pick a video from gallery
                val videoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                videoPickerIntent.type = "video/*"
                videoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE)
                videoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                videoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                videoPickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                pickVideoLauncher.launch(videoPickerIntent)
            } else if (mediaPickType == MediaConstants.CHOOSE_TYPE_DOCUMENT_PICK) {
                val filePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
                val extras = intent.extras
                val mime =
                    if (extras != null && extras.containsKey(EXTRA_DOCUMENT_MIME) && extras.getStringArray(EXTRA_DOCUMENT_MIME) != null) {
                        intent.extras?.getStringArray(EXTRA_DOCUMENT_MIME)
                    } else {
                        filePickMimes
                    }
                filePickerIntent.type = "text/*"
                filePickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mime)
                filePickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                filePickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                filePickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                filePickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                pickFileLauncher.launch(filePickerIntent)
            }
        } catch (ex: ActivityNotFoundException) {
            //display an error message
            val errorMessage = "Your device doesn't support capturing images!"
            val toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}