package com.kore.ui.botchat

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.kore.SDKConfig
import com.kore.SDKConfig.isMinimized
import com.kore.botclient.BotClient
import com.kore.botclient.ConnectionState
import com.kore.common.event.UserActionEvent
import com.kore.common.utils.LogUtils
import com.kore.event.BotChatEvent
import com.kore.model.BaseBotMessage
import com.kore.model.BotEventResponse
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.END_DATE
import com.kore.model.constants.BotResponseConstants.FORMAT
import com.kore.model.constants.BotResponseConstants.HEADER_SIZE_COMPACT
import com.kore.model.constants.BotResponseConstants.HEADER_SIZE_LARGE
import com.kore.model.constants.BotResponseConstants.START_DATE
import com.kore.network.api.responsemodels.branding.BotActiveThemeModel
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.services.ClosingService
import com.kore.ui.BR
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity.Companion.CALL_AGENT_WEBRTC_ACCEPTED
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity.Companion.CALL_AGENT_WEBRTC_REJECTED
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity.Companion.CANCEL_AGENT_WEBRTC
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity.Companion.TERMINATE_AGENT_WEBRTC
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.audiocodes.webrtcclient.structure.SipAccount
import com.kore.ui.base.BaseActivity
import com.kore.ui.base.BaseContentFragment
import com.kore.ui.base.BaseFooterFragment
import com.kore.ui.base.BaseHeaderFragment
import com.kore.ui.botchat.dialog.WelcomeDialogFragment
import com.kore.ui.botchat.fragment.ChatContentFragment
import com.kore.ui.botchat.fragment.ChatFooterFragment
import com.kore.ui.botchat.fragment.ChatHeaderOneFragment
import com.kore.ui.botchat.fragment.ChatHeaderThreeFragment
import com.kore.ui.botchat.fragment.ChatHeaderTwoFragment
import com.kore.ui.botchat.fragment.ChatV2HeaderFragment
import com.kore.ui.databinding.ActivityBotChatBinding
import com.kore.ui.databinding.IncomingCallLayoutBinding
import com.kore.ui.utils.BundleConstants
import com.kore.ui.utils.BundleConstants.EXTRA_RESULT
import org.webrtc.NetworkMonitor.isOnline
import java.util.Calendar


class BotChatActivity : BaseActivity<ActivityBotChatBinding, BotChatView, BotChatViewModel>(), BotChatView {
    private var contentFragment: BaseContentFragment = SDKConfig.getCustomContentFragment() ?: ChatContentFragment()
    private var footerFragment: BaseFooterFragment = SDKConfig.getCustomFooterFragment() ?: ChatFooterFragment()
    internal val botChatViewModel: BotChatViewModel by viewModels()
    private val gson: Gson = Gson()
    private val launchTime = System.currentTimeMillis()
    private val networkCallback = NetworkCallbackImpl()
    private val acManager: ACManager = ACManager.getInstance()
    private var alertDialog: Dialog? = null

    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun getLayoutID(): Int = R.layout.activity_bot_chat

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): BotChatViewModel = botChatViewModel

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentFragment.setView(this)
        binding.viewModel = botChatViewModel
        botChatViewModel.init(this)
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isOnline()) {
                    showCloseAlert()
                }
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(onDestroyReceiver, IntentFilter(BundleConstants.DESTROY_EVENT), RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(onDestroyReceiver, IntentFilter(BundleConstants.DESTROY_EVENT))
        }
        startService(Intent(this.applicationContext, ClosingService::class.java))
    }

    override fun onPause() {
        super.onPause()
        botChatViewModel.setIsActivityResumed(false)
    }

    override fun onResume() {
        super.onResume()
        botChatViewModel.setIsActivityResumed(true)
        botChatViewModel.sendReadReceipts()
    }

    override fun onDestroy() {
        super.onDestroy()
        botChatViewModel.onTerminate()
    }

    private fun addFragmentsToContainer(id: Int, fragment: Fragment) {
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.add(id, fragment)
        tr.commitAllowingStateLoss()
    }

    private fun addHeaderFragmentToActivity(fragment: Fragment, brandingModel: BotBrandingModel?) {
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()

        val bundle = Bundle()

        fragment.arguments = bundle
        if (fragment is BaseHeaderFragment) {
            fragment.setActionEvent(this::onActionEvent)
            fragment.setBrandingHeader(brandingModel?.header)
            fragment.setBrandingDetails(brandingModel)
        }
        tr.add(R.id.header_container, fragment)
        tr.commitAllowingStateLoss()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        botChatViewModel.disconnectBot()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun init() {
        binding.footerContainer.isVisible = true
        binding.disconnect.setOnClickListener { botChatViewModel.disconnectBot() }

        binding.send.setOnClickListener {
            val message = binding.message.text
            if (!message.isNullOrEmpty() && message.trim().isNotEmpty()) {
                botChatViewModel.sendMessage(message.toString(), null)
            }
        }
        addFragmentsToContainer(R.id.chat_container, contentFragment)
        addFragmentsToContainer(R.id.footer_container, footerFragment)
        binding.root.postDelayed(
            {
                contentFragment.setActionEvent(this::onActionEvent)
                footerFragment.setActionEvent(this::onActionEvent)
            }, 1000
        )
    }

    override fun addMessageToAdapter(baseBotMessage: BaseBotMessage) {
        contentFragment.addMessagesToAdapter(listOf(baseBotMessage), false)
    }

    override fun onActionEvent(event: UserActionEvent) {
        when (event) {
            is BotChatEvent.SendMessage -> {
                botChatViewModel.sendMessage(event.message, event.payload)
                contentFragment.hideQuickReplies()
            }

            is BotChatEvent.SendAttachments -> botChatViewModel.sendAttachment(event.message, event.attachments)
            is BotChatEvent.UrlClick -> openWebView(event.url, event.header.ifEmpty { getString(R.string.app_name) })
            is BotChatEvent.PhoneNumberClick -> {}
            is BotChatEvent.OnDropDownItemClicked -> footerFragment.setMessage(event.selectedItem)
            is BotChatEvent.ShowAttachmentOptions -> footerFragment.showAttachmentActionSheet()
            is BotChatEvent.DownloadLink -> botChatViewModel.downloadFile(event.msgId, event.url, event.fileName)
            is BotChatEvent.OnBackPressed -> if (isOnline()) showCloseAlert()
        }
    }

    override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
        footerFragment.enableSendButton(state == ConnectionState.CONNECTED)
    }

    override fun onBrandingDetails(header: BotActiveThemeModel?) {
        if (header?.botMessage == null && header?.brandingModel != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.clProgress.isVisible = false
                if (header.brandingModel?.welcomeScreen?.show == true) {
                    WelcomeDialogFragment(header.brandingModel!!).apply {
                        setListener(object : WelcomeDialogFragment.WelcomeDialogListener {
                            override fun onUpdateUI() {
                                binding.chatWindow.isVisible = true
                            }
                        })
                        this.show(supportFragmentManager, "My Dialog")
                    }
                }

                val customHeaderFragment = SDKConfig.getCustomHeaderFragment(header.brandingModel?.header?.size.toString())

                when (header.brandingModel?.header?.size) {
                    HEADER_SIZE_COMPACT -> addHeaderFragmentToActivity(
                        customHeaderFragment ?: ChatHeaderOneFragment(),
                        header.brandingModel
                    )

                    HEADER_SIZE_LARGE -> addHeaderFragmentToActivity(
                        customHeaderFragment ?: ChatHeaderThreeFragment(),
                        header.brandingModel
                    )

                    else -> addHeaderFragmentToActivity(customHeaderFragment ?: ChatHeaderTwoFragment(), header.brandingModel)
                }
                contentFragment.onBrandingDetails()

            }, 2000)
        } else {
            binding.clProgress.isVisible = false
            binding.chatWindow.isVisible = true
            val customHeaderFragment = SDKConfig.getCustomHeaderFragment(HEADER_SIZE_COMPACT)
            addHeaderFragmentToActivity(customHeaderFragment ?: ChatV2HeaderFragment(), header?.brandingModel)
        }
        footerFragment.setBrandingDetails(header?.brandingModel)
    }

    override fun onBotEventMessage(botResponse: BotEventResponse) {
        val body = botResponse.message ?: return
        if (body[BotResponseConstants.SIP_USER] != null) {
            if (body.isNotEmpty()) {
                val proxy = (body[BotResponseConstants.ADDRESSES] as List<String>)[0].split(":")[1].replace("//", "")
                val sipAccount = SipAccount(this)
                sipAccount.username = botChatViewModel.getUserId()
                sipAccount.displayName = botChatViewModel.getUserId()
                sipAccount.domain = body[BotResponseConstants.DOMAIN] as String
                sipAccount.proxy = proxy
                sipAccount.port = 5060
                sipAccount.transport = Transport.UDP

                Prefs.setSipAccount(this, sipAccount)
                Prefs.setIsAutoRedirect(this, true)

                showAlertDialog(body)
            }
        } else {
            when (body[BotResponseConstants.TYPE].toString()) {
                CANCEL_AGENT_WEBRTC,
                TERMINATE_AGENT_WEBRTC -> {
                    ACManager.getInstance().terminate()
                    if (alertDialog?.isShowing == true) alertDialog?.dismiss()
                    val intent = Intent(CallActivity.ACTION_CALL_TERMINATED)
                    sendBroadcast(intent)
                }
            }
        }
    }

    override fun onFileDownloadProgress(msgId: String, progress: Int, downloadedBytes: Int) {
        contentFragment.onFileDownloadProgress(msgId, progress, downloadedBytes)
    }

    override fun onSwipeRefresh() {
        botChatViewModel.fetchChatHistory(false)
    }

    private fun showAlertDialog(eventModel: HashMap<String, Any>) {
        alertDialog = Dialog(this@BotChatActivity)
        val dialogBinding: IncomingCallLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.incoming_call_layout, null, false)
        alertDialog?.setContentView(dialogBinding.root)
        alertDialog?.setCancelable(false)
        dialogBinding.tvAgentName.text = eventModel[BotResponseConstants.FIRST_NAME] as String
        dialogBinding.tvTypeOfCall.text = getString(R.string.incoming_audio_call)
        if (eventModel[BotResponseConstants.IS_VIDEO_CALL] as Boolean) {
            dialogBinding.tvTypeOfCall.text = getString(R.string.incoming_video_call)
        }
        dialogBinding.tvCallAccept.setOnClickListener {
            eventModel[BotResponseConstants.TYPE] = CALL_AGENT_WEBRTC_ACCEPTED
            botChatViewModel.sendMessage("", gson.toJson(eventModel).toString())
            BotClient.setCallEventMessage(eventModel)
            openNextScreen(eventModel[BotResponseConstants.SIP_USER] as String, eventModel[BotResponseConstants.IS_VIDEO_CALL] as Boolean)
            alertDialog?.dismiss()
        }
        dialogBinding.tvCallReject.setOnClickListener {
            eventModel[BotResponseConstants.TYPE] = CALL_AGENT_WEBRTC_REJECTED
            botChatViewModel.sendMessage("", gson.toJson(eventModel).toString())
            alertDialog?.dismiss()
        }
        alertDialog?.show()
    }

    private fun openNextScreen(sipUser: String, isVideoCall: Boolean) {
        Prefs.setIsFirstLogin(this, false)
        //start login and open app main screen
        Thread {
            if (!acManager.isRegisterState) acManager.startLogin(this, false, false)
            if (!acManager.isRegisterState && Prefs.getAutoLogin(this)) {
                Toast.makeText(this@BotChatActivity, R.string.no_registration, Toast.LENGTH_SHORT).show()
            } else {
                acManager.callNumber(this, sipUser, isVideoCall)
            }
        }.start()
    }

    private fun openWebView(url: String, header: String) {
        val intent = Intent(this, GeneralWebViewActivity::class.java)
        intent.putExtra(GeneralWebViewActivity.EXTRA_URL, url)
        intent.putExtra(GeneralWebViewActivity.EXTRA_HEADER, header)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun showCalenderTemplate(payload: HashMap<String, Any>) {
        if (BotResponseConstants.TEMPLATE_TYPE_DATE == payload[BotResponseConstants.KEY_TEMPLATE_TYPE]) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(payload[BotResponseConstants.KEY_TITLE] as String)
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            builder.setCalendarConstraints(botChatViewModel.minRange(cal.timeInMillis).build())
            builder.setTheme(R.style.MyMaterialCalendarTheme)
            try {
                val picker = builder.build()
                picker.show(supportFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { selection -> botChatViewModel.onDatePicked(selection) }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        } else {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText(payload[BotResponseConstants.KEY_TITLE] as String)
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            builder.setCalendarConstraints(
                botChatViewModel.limitRange(
                    (payload[START_DATE] as String?) ?: "", (payload[END_DATE] as String?) ?: "", (payload[FORMAT] as String?) ?: ""
                ).build()
            )
            builder.setTheme(R.style.MyMaterialCalendarTheme)
            try {
                val picker = builder.build()
                picker.show(supportFragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { selection -> botChatViewModel.onRangeDatePicked(selection) }
            } catch (e: java.lang.IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    override fun showTypingIndicator(icon: String?) {
        contentFragment.showTypingIndicator(icon, true)
    }

    override fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?) {
        contentFragment.showQuickReplies(quickReplies, type)
    }

    override fun getAdapterLastItems(): BaseBotMessage? {
        return contentFragment.getAdapterLastItem()
    }

    override fun hideQuickReplies() {
        contentFragment.hideQuickReplies()
    }

    override fun onChatHistory(list: List<BaseBotMessage>) {
        contentFragment.addMessagesToAdapter(list, !isMinimized())
    }

    fun showCloseAlert() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
            val intent = Intent()
            val result = java.util.HashMap<String, String>()
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val event = R.string.bot_minimize_event
                    botChatViewModel.sendCloseOrMinimizeEvent(event)
                    botChatViewModel.onMinimize(contentFragment.getAdapterCount())
                    result["event_code"] = "BotMinimized"
                    result["event_message"] = "Bot Minimized by the user"
                    intent.putExtra(EXTRA_RESULT, Gson().toJson(result))
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    val event = if (botChatViewModel.isAgentTransfer()) R.string.agent_bot_close_event else R.string.bot_close_event
                    botChatViewModel.sendCloseOrMinimizeEvent(event)
                    botChatViewModel.onMinimize(0)
                    result["event_code"] = "BotClosed"
                    result["event_message"] = "Bot closed by the user"
                    intent.putExtra(EXTRA_RESULT, Gson().toJson(result))
                }

                DialogInterface.BUTTON_NEUTRAL -> {
                    dialog?.dismiss()
                    return@OnClickListener
                }
            }
            dialog?.dismiss()
            botChatViewModel.disconnectBot()
            setResult(RESULT_OK, intent)
            finish()
        }
        val builder = AlertDialog.Builder(this@BotChatActivity)
        builder.setMessage(R.string.close_or_minimize).setCancelable(false)
            .setPositiveButton(R.string.minimize, dialogClickListener)
            .setNegativeButton(R.string.close, dialogClickListener)
            .setNeutralButton(R.string.cancel, dialogClickListener).show()
    }

    inner class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            binding.root.post {
                if (System.currentTimeMillis() - launchTime > 2000) botChatViewModel.onNetworkAvailable()
            }
        }

        override fun onLost(network: Network) {
            if (botChatViewModel.isWebhook()) binding.root.post { footerFragment.enableSendButton(false) }
        }
    }

    private var onDestroyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            LogUtils.i("BotChatActivity", "onDestroyReceiver called")
            botChatViewModel.onTerminate()
        }
    }
}