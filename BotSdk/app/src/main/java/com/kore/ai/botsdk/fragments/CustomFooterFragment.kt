package com.kore.ai.botsdk.fragments

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.kore.ai.botsdk.databinding.CustomFooterFragmentBinding
import com.kore.botclient.BotClient
import com.kore.common.SDKConfiguration
import com.kore.common.constants.MediaConstants.Companion.CHOOSE_TYPE_CAPTURE_IMAGE
import com.kore.common.constants.MediaConstants.Companion.CHOOSE_TYPE_CAPTURE_VIDEO
import com.kore.common.constants.MediaConstants.Companion.CHOOSE_TYPE_DOCUMENT_PICK
import com.kore.common.constants.MediaConstants.Companion.CHOOSE_TYPE_IMAGE_PICK
import com.kore.common.constants.MediaConstants.Companion.CHOOSE_TYPE_VIDEO_PICK
import com.kore.common.constants.MediaConstants.Companion.MEDIA_TYPE_DOCUMENT
import com.kore.common.constants.MediaConstants.Companion.MEDIA_TYPE_IMAGE
import com.kore.common.constants.MediaConstants.Companion.MEDIA_TYPE_VIDEO
import com.kore.common.event.UserActionEvent
import com.kore.common.utils.NetworkUtils
import com.kore.common.utils.ToastUtils
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.helper.RuntimePermissionHelper
import com.kore.helper.SpeechSynthesizerHelper
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.speech.GoogleVoiceTypingDisabledException
import com.kore.speech.Speech
import com.kore.speech.SpeechRecognitionNotAvailable
import com.kore.speech.SpeechUtil
import com.kore.ui.R
import com.kore.ui.adapters.ComposeBarAttachmentAdapter
import com.kore.ui.base.BaseFooterFragment
import com.kore.ui.dialog.ActionSheetDialog
import com.kore.ui.dialog.OptionsActionSheetFragment
import com.kore.ui.utils.FileUtils
import com.kore.uploadfile.helper.MediaAttachmentHelper
import com.kore.uploadfile.helper.MediaAttachmentHelper.Companion.REQ_CAMERA
import com.kore.uploadfile.helper.MediaAttachmentHelper.Companion.REQ_FILE
import com.kore.uploadfile.helper.MediaAttachmentHelper.Companion.REQ_IMAGE
import com.kore.uploadfile.helper.MediaAttachmentHelper.Companion.REQ_VIDEO
import com.kore.uploadfile.helper.MediaAttachmentHelper.Companion.REQ_VIDEO_CAPTURE

class CustomFooterFragment : BaseFooterFragment() {
    lateinit var binding: CustomFooterFragmentBinding
    private var actionEvent: (event: UserActionEvent) -> Unit = {}
    private lateinit var selectMediaLauncher: ActivityResultLauncher<Intent>
    private var attachmentsActionSheet: ActionSheetDialog? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var arrAttachments: List<Map<String, *>> = mutableListOf()
    private lateinit var speechSynthesizerHelper: SpeechSynthesizerHelper
    private var isEnabled = false
    private var isAttachedToWindow = false
    private var botOptionModels: ArrayList<BrandingQuickStartButtonActionModel>? = null
    private var botBrandingModel: BotBrandingModel? = null

    private val appSettingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _: ActivityResult ->
        checkAndRequestPermission()
    }

    private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val keys = permissions.keys
            if (permissions[Manifest.permission.RECORD_AUDIO] == true) {
                onRecordAudioPermissionGranted()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), keys.first())) {
                runtimePermissionHelper.showPermissionRationaleDialog(keys.first(), "", "") {}
            } else {
                runtimePermissionHelper.showPermissionDeniedDialog("", "") {}
            }
        }

    private val runtimePermissionHelper by lazy {
        RuntimePermissionHelper(requireActivity(), requestPermissionsLauncher, appSettingsLauncher)
    }

    private val attachmentOptions by lazy { resources.getStringArray(R.array.attachment_options).asList() }
    private val mediaAttachmentHelper by lazy {
        MediaAttachmentHelper(requireContext(), BotClient.getUserId(), BotClient.getAccessToken(), BotClient.getJwtToken())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        selectMediaLauncher =
            (context as AppCompatActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    mediaAttachmentHelper.setListener(this)
                    mediaAttachmentHelper.processResult(result.data)
                }
            }
        sharedPreferences = requireActivity().getSharedPreferences(BotResponseConstants.THEME_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CustomFooterFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        speechSynthesizerHelper = SpeechSynthesizerHelper(requireContext())
        isAttachedToWindow = true
        enableSendButton(isEnabled)
        binding.edtTxtMessage.addTextChangedListener(composeTextWatcher)
        binding.llSend.setOnClickListener {
            if (arrAttachments.isNotEmpty()) {
                val message = binding.edtTxtMessage.text.trim().toString() + "\n" +
                        getDrawableByExt(arrAttachments[0][BotResponseConstants.MEDIA_TYPE] as String) +
                        arrAttachments[0][BotResponseConstants.FILE_NAME]
                actionEvent(BotChatEvent.SendAttachments(message, arrAttachments))
                binding.attachmentRecycler.adapter = null
                binding.attachmentRecycler.isVisible = false
                binding.edtTxtMessage.text?.clear()
                arrAttachments = emptyList()
            } else if (binding.edtTxtMessage.text.trim().toString().isNotEmpty()) {
                actionEvent(BotChatEvent.SendMessage(binding.edtTxtMessage.text.toString(), null))
                binding.edtTxtMessage.text?.clear()
            }
        }
        binding.attachment.setOnClickListener {
            if (binding.attachmentRecycler.adapter == null) {
                showAttachmentActionSheet()
            } else {
                ToastUtils.showToast(requireActivity(), "You can upload only one file", Toast.LENGTH_SHORT)
            }
        }

        binding.recAudioImg.setOnClickListener(onVoiceModeActivated)
        binding.keyboardImage.setOnClickListener(keyboardIconClickListener)
        binding.audioSpeakTts.setOnClickListener(onTTSEnableSwitchClickListener)

        binding.rlSpeaker.setOnClickListener {
            checkAndRequestPermission()
        }

        binding.llSpeechSend.setOnClickListener {
            if (binding.textViewSpeech.text.isNotEmpty()) {
                actionEvent(BotChatEvent.SendMessage(binding.textViewSpeech.text.toString(), null))
                binding.llSpeechSend.isVisible = false
                binding.ivSpeaker.isVisible = true
                binding.textViewSpeech.isVisible = false
                binding.newMenuLogo.isVisible = true
                binding.attachment.isVisible = true
                binding.keyboardImage.isVisible = true
                binding.speakerText.text = requireActivity().getString(R.string.tap_to_speak)
                speechStarted(false)
            }
        }

        val rightTextColor: String? = sharedPreferences.getString(BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR, "#ffffff")
        val rightBgColor: String? = sharedPreferences.getString(BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
        val rightDrawable = ResourcesCompat.getDrawable(
            requireActivity().resources, R.drawable.theme1_right_bubble_bg, requireActivity().theme
        ) as GradientDrawable?

        if (rightDrawable != null) {
            rightTextColor?.toColorInt()?.let { rightDrawable.setColor(it) }
            rightBgColor?.toColorInt()?.let { rightDrawable.setStroke((1.dpToPx(requireContext())), it) }
        }

        binding.textViewSpeech.background = rightDrawable
        rightBgColor?.toColorInt()?.let { binding.textViewSpeech.setTextColor(it) }

        Speech.init(requireActivity(), requireActivity().packageName)

        binding.newMenuLogo.setOnClickListener {
            if (botBrandingModel != null && !botOptionModels.isNullOrEmpty()) {
                val bottomSheetDialog = OptionsActionSheetFragment()
                bottomSheetDialog.setData(botOptionModels)
                bottomSheetDialog.setBrandingModel(botBrandingModel)
                bottomSheetDialog.setActionEvent(actionEvent)
                bottomSheetDialog.show(childFragmentManager, "add_tags")
            }
        }
    }

    private val composeTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isEmpty()) {
                enableSendButton(false)
            } else if (s.isNotEmpty()) {
                if ((SDKConfiguration.getBotConfigModel()?.isWebHook == true && NetworkUtils.isNetworkAvailable(requireContext())) || BotClient.isConnected()) {
                    enableSendButton(true)
                }
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
        actionEvent = onActionEvent
    }

    override fun enableSendButton(enable: Boolean) {
        isEnabled = enable
        if (isAttachedToWindow) {
            binding.llSend.isVisible = enable && binding.edtTxtMessage.text.trim().isNotEmpty()
            binding.recAudioImg.isVisible = !enable
        }
    }

    override fun setMessage(message: String) {
        binding.edtTxtMessage.setText(message)
    }

    override fun showAttachmentActionSheet() {
        if (binding.attachmentRecycler.adapter != null) {
            ToastUtils.showToast(requireActivity(), "You can upload only one file", Toast.LENGTH_SHORT)
            return
        }
        if (attachmentsActionSheet == null) {
            attachmentsActionSheet = ActionSheetDialog(requireContext())
            attachmentsActionSheet?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            attachmentsActionSheet?.let {
                it.window?.let { window ->
                    val wlp: WindowManager.LayoutParams = window.attributes
                    wlp.gravity = Gravity.BOTTOM
                    window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                }
            }
        }
        attachmentsActionSheet?.show(attachmentOptions, this::launchSelectedMode)
    }

    private fun launchSelectedMode(option: String) {
        val capturePhoto = getString(R.string.attachment_capture_photo)
        val captureVideo = getString(R.string.attachment_capture_video)
        val uploadPhoto = getString(R.string.attachment_upload_photo)
        val uploadVideo = getString(R.string.attachment_upload_video)
        val uploadDocument = getString(R.string.attachment_upload_document)
        when (option) {
            capturePhoto -> {
                mediaAttachmentHelper.fileBrowsingActivity(selectMediaLauncher, CHOOSE_TYPE_CAPTURE_IMAGE, REQ_CAMERA, MEDIA_TYPE_IMAGE)
            }

            captureVideo -> {
                mediaAttachmentHelper.fileBrowsingActivity(
                    selectMediaLauncher,
                    CHOOSE_TYPE_CAPTURE_VIDEO,
                    REQ_VIDEO_CAPTURE,
                    MEDIA_TYPE_VIDEO
                )
            }

            uploadPhoto -> {
                mediaAttachmentHelper.fileBrowsingActivity(selectMediaLauncher, CHOOSE_TYPE_IMAGE_PICK, REQ_IMAGE, MEDIA_TYPE_IMAGE)
            }

            uploadVideo -> {
                mediaAttachmentHelper.fileBrowsingActivity(selectMediaLauncher, CHOOSE_TYPE_VIDEO_PICK, REQ_VIDEO, MEDIA_TYPE_VIDEO)
            }

            uploadDocument -> {
                mediaAttachmentHelper.fileBrowsingActivity(
                    selectMediaLauncher,
                    CHOOSE_TYPE_DOCUMENT_PICK,
                    REQ_FILE,
                    MEDIA_TYPE_DOCUMENT,
                    arrayOf("application/pdf")
                )
            }
        }
        attachmentsActionSheet?.dismiss()
    }

    private val keyboardIconClickListener = View.OnClickListener {
        animateLayoutVisible(binding.mainContent)
        animateLayoutGone(binding.defaultFooter)
        binding.recAudioImg.isVisible = true
        binding.keyboardImage.isVisible = false
    }

    private val onVoiceModeActivated = View.OnClickListener {
        animateLayoutGone(binding.mainContent)
        animateLayoutVisible(binding.defaultFooter)
        binding.keyboardImage.isVisible = true
        binding.recAudioImg.isVisible = false
    }

    private fun animateLayoutVisible(view: View) {
        view.visibility = View.VISIBLE
        view.animate().translationY(0f).alpha(1.0f).setDuration(500).setListener(null)
    }

    private fun animateLayoutGone(view: View) {
        view.animate().translationY(view.height.toFloat()).alpha(0.0f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
    }

    private fun checkAndRequestPermission() {
        if (!RuntimePermissionHelper.hasPermissions(requireContext(), listOf(Manifest.permission.RECORD_AUDIO))) {
            runtimePermissionHelper.requestPermissions(listOf(Manifest.permission.RECORD_AUDIO))
        } else {
            onRecordAudioPermissionGranted()
        }
    }

    private fun onRecordAudioPermissionGranted() {
        try {
            Speech.getInstance()?.stopTextToSpeech()
            Speech.getInstance()?.startListening(binding.progress, this@CustomFooterFragment)
        } catch (exc: SpeechRecognitionNotAvailable) {
            showSpeechNotSupportedDialog()
        } catch (exc: GoogleVoiceTypingDisabledException) {
            showEnableGoogleVoiceTyping()
        }
    }

    fun setSpeechSynthesizer(speechSynthesizerHelper1: SpeechSynthesizerHelper) {
        speechSynthesizerHelper = speechSynthesizerHelper1
    }

    override fun setBrandingDetails(botBrandingModel: BotBrandingModel?) {
        this.botBrandingModel = botBrandingModel
        binding.newMenuLogo.isVisible = botBrandingModel?.footer?.buttons?.menu?.show == true
        if (botBrandingModel == null) return
        botOptionModels = botBrandingModel.footer.buttons?.menu?.actions
        if (botBrandingModel.general.colors.useColorPaletteOnly == true) {
            botBrandingModel.general.colors.secondary?.toColorInt()?.let { binding.composeFooterRl.setBackgroundColor(it) }
        } else {
            botBrandingModel.footer.bgColor?.toColorInt()?.let { binding.composeFooterRl.setBackgroundColor(it) }
        }

        val stroke = binding.mainContent.background
        val solidColor = binding.llEdtText.background
        val send = binding.llSend.background
        val sendSmall = binding.sendTv.background

        val footerModel = botBrandingModel.footer

        if (solidColor != null && !footerModel.composeBar?.bgColor.isNullOrEmpty()) {
            solidColor.setTintList(ColorStateList.valueOf(footerModel.composeBar?.bgColor!!.toColorInt()))
            binding.llEdtText.background = solidColor
        }
        if (!footerModel.composeBar?.outlineColor.isNullOrEmpty()) {
            send.setTint(footerModel.composeBar?.outlineColor!!.toColorInt())
            binding.llSend.background = send

            if (stroke != null) {
                stroke.setTint(footerModel.composeBar?.outlineColor!!.toColorInt())
                binding.mainContent.background = stroke
            }

            binding.rlSpeaker.backgroundTintList = ColorStateList.valueOf(footerModel.composeBar?.outlineColor!!.toColorInt())
        }

        if (!footerModel.composeBar?.inlineColor.isNullOrEmpty()) {
            if (sendSmall != null) {
                sendSmall.setTint(footerModel.composeBar?.inlineColor!!.toColorInt())
                binding.sendTv.background = sendSmall
            }
            binding.ivSpeaker.backgroundTintList = ColorStateList.valueOf(footerModel.composeBar?.inlineColor!!.toColorInt())
            val colors = intArrayOf(
                footerModel.composeBar?.inlineColor!!.toColorInt(),
                footerModel.composeBar?.inlineColor!!.toColorInt(),
                footerModel.composeBar?.inlineColor!!.toColorInt()
            )
            binding.progress.setColors(colors)
        }

        if (!footerModel.composeBar?.placeholder.isNullOrEmpty()) {
            binding.edtTxtMessage.hint = footerModel.composeBar?.placeholder
        }

        if (!footerModel.iconsColor.isNullOrEmpty()) {
            binding.newMenuLogo.imageTintList = ColorStateList.valueOf(footerModel.iconsColor!!.toColorInt())
            binding.attachment.imageTintList = ColorStateList.valueOf(footerModel.iconsColor!!.toColorInt())
            binding.recAudioImg.imageTintList = ColorStateList.valueOf(footerModel.iconsColor!!.toColorInt())
            binding.keyboardImage.imageTintList = ColorStateList.valueOf(footerModel.iconsColor!!.toColorInt())
            binding.audioSpeakTts.imageTintList = ColorStateList.valueOf(footerModel.iconsColor!!.toColorInt())
            binding.edtTxtMessage.setHintTextColor(footerModel.iconsColor!!.toColorInt())
        }

        binding.newMenuLogo.isVisible = footerModel.buttons?.menu?.show == true
        binding.attachment.isVisible = footerModel.buttons?.attachment?.show == true
        binding.recAudioImg.isVisible = footerModel.buttons?.microphone?.show == true
        binding.audioSpeakTts.isVisible = footerModel.buttons?.speaker?.show == true
    }

    private val onTTSEnableSwitchClickListener = View.OnClickListener {
        toggleTTSButton(!speechSynthesizerHelper.isTTSEnabled())
        speechSynthesizerHelper.setIsTtsEnabled(!speechSynthesizerHelper.isTTSEnabled())
    }

    private fun toggleTTSButton(enable: Boolean) {
        binding.audioSpeakTts.setImageResource(if (enable) R.mipmap.ic_volume_up_black_24dp else R.mipmap.ic_volume_off_black_24dp)
    }

    private fun showSpeechNotSupportedDialog() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> SpeechUtil.redirectUserToGoogleAppOnPlayStore(requireActivity())
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(R.string.speech_not_available).setCancelable(false).setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.no, dialogClickListener).show()
    }

    private fun showEnableGoogleVoiceTyping() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(R.string.enable_google_voice_typing).setCancelable(false)
            .setPositiveButton(R.string.yes) { dialogInterface, _ -> dialogInterface.dismiss() }.show()
    }

    override fun onStartOfSpeech() {
        binding.ivSpeaker.isVisible = false
        binding.speakerText.text = requireActivity().getString(R.string.listening_tap_end)
        binding.linearLayoutProgress.isVisible = true
        binding.textViewSpeech.isVisible = true
        speechStarted(true)
    }

    private fun speechStarted(isStarted: Boolean) {
        binding.newMenuLogo.visibility = if (isStarted) View.INVISIBLE else View.VISIBLE
        binding.attachment.visibility = if (isStarted) View.INVISIBLE else View.VISIBLE
        binding.keyboardImage.visibility = if (isStarted) View.INVISIBLE else View.VISIBLE
        binding.newMenuLogo.isEnabled = !isStarted
        binding.newMenuLogo.isClickable = !isStarted
        binding.attachment.isEnabled = !isStarted
        binding.attachment.isClickable = !isStarted
        binding.keyboardImage.isEnabled = !isStarted
        binding.keyboardImage.isClickable = !isStarted
        if (isStarted) {
            binding.textViewSpeech.text = ""
        }
    }

    override fun onSpeechRmsChanged(value: Float) {
    }

    override fun onSpeechPartialResults(results: List<String?>?) {
        if (results != null) {
            binding.textViewSpeech.text = results.toString()
            for (partial in results) {
                binding.textViewSpeech.append("$partial ")
            }
        }
    }

    override fun onSpeechResult(result: String?) {
        if (!result.isNullOrEmpty()) {
            binding.linearLayoutProgress.isVisible = false
            binding.speakerText.text = requireActivity().getString(R.string.tap_to_send)
            binding.llSpeechSend.isVisible = true
            binding.textViewSpeech.text = result
        } else {
            binding.linearLayoutProgress.isVisible = false
            binding.llSpeechSend.isVisible = false
            binding.ivSpeaker.isVisible = true
            binding.textViewSpeech.isVisible = false
            binding.newMenuLogo.isVisible = true
            binding.attachment.isVisible = true
            binding.keyboardImage.isVisible = true
            binding.speakerText.text = requireActivity().getString(R.string.tap_to_speak)
            speechStarted(false)
        }
    }

    override fun onDestroy() {
        Speech.getInstance()!!.shutdown()
        super.onDestroy()
    }

    override fun onMediaFile(fileName: String, mediaData: List<Map<String, *>>) {
        arrAttachments = mediaData
        binding.attachmentRecycler.isVisible = true
        binding.attachmentRecycler.adapter = ComposeBarAttachmentAdapter(requireActivity(), this, mediaData)
        enableSendButton(true)
    }

    private fun getDrawableByExt(ext: String): String {
        return if (FileUtils.imageTypes().contains(ext)) {
            requireActivity().resources.getString(R.string.camera)
        } else if (FileUtils.videoTypes().contains(ext)) {
            requireActivity().resources.getString(R.string.video)
        } else {
            requireActivity().resources.getString(R.string.attachment)
        }
    }

    override fun onMediaFilePath(filePath: String) {
    }

    override fun onRemoveAttachment() {
        arrAttachments = mutableListOf()
        binding.attachmentRecycler.isVisible = false
        binding.attachmentRecycler.adapter = null
        enableSendButton(false)
    }
}