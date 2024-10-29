package com.kore.ui.audiocodes.webrtcclient.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.audiocodes.mv.webrtcsdk.audio.WebRTCAudioManager
import com.audiocodes.mv.webrtcsdk.audio.WebRTCAudioManager.AudioRoute
import com.audiocodes.mv.webrtcsdk.audio.WebRTCAudioRouteListener
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSessionEventListener
import com.audiocodes.mv.webrtcsdk.session.CallState
import com.audiocodes.mv.webrtcsdk.session.CallTermination
import com.audiocodes.mv.webrtcsdk.session.CallTransferState
import com.audiocodes.mv.webrtcsdk.session.DTMF
import com.audiocodes.mv.webrtcsdk.session.NotifyEvent
import com.audiocodes.mv.webrtcsdk.session.RemoteContact
import com.audiocodes.mv.webrtcsdk.session.TerminationInfo
import com.audiocodes.mv.webrtcsdk.useragent.ACConfiguration
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA
import com.audiocodes.mv.webrtcsdk.useragent.WebRTCException
import com.google.gson.Gson
import com.kore.botclient.BotClient
import com.kore.common.utils.LogUtils
import com.kore.model.constants.BotResponseConstants.TYPE
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.LoginStateChanged
import com.kore.ui.audiocodes.webrtcclient.db.NativeDBManager
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.audiocodes.webrtcclient.general.ImageUtils
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.audiocodes.webrtcclient.login.LogoutManager
import com.kore.ui.audiocodes.webrtcclient.services.CallForegroundService
import com.kore.ui.databinding.CallActivityBinding
import java.util.Locale

class CallActivity : BaseAppCompatActivity(), AudioCodesSessionEventListener {
    private val acManager: ACManager = ACManager.getInstance()
    private lateinit var binding: CallActivityBinding
    private var session: AudioCodesSession? = null
    private var dmfEnabled = false
    private var guiInitialized = false
    private lateinit var ac: WebRTCAudioManager
    private var route = AudioRoute.EARPIECE
    private var sessionList = ArrayList<Int>()
    private var myAudioCodesSessionEventListener = MyAudioCodesSessionEventListener()
    private var lastSessionIndex = 0
    private lateinit var callActivity: CallActivity
    private val timerHandler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable
    private var callDuration = 0
    private var loginStateChanged = LoginStateChanged {
        handler.post {
            binding.callButtonEndCall.isEnabled = false
            binding.callButtonHold.isEnabled = false
        }
    }
    private val callTerminateReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if (intent?.action == ACTION_CALL_TERMINATED) finish()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callActivity = this
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.call_activity, null, false)
        setContentView(binding.root)
        CallBackHandler.registerLginStateChange(loginStateChanged)
        ac = WebRTCAudioManager.getInstance()
        ac.setAudioRoute(AudioRoute.SPEAKER_PHONE)
        var sessionIndex = this.intent.getIntExtra(SESSION_ID, -1)
        if (sessionIndex == -1) {
            Log.d(TAG, "getActiveSession: " + acManager.activeSession)
            Log.d(TAG, "getSessionList: " + acManager.sessionList)
            sessionIndex = if (acManager.activeSession != null) {
                acManager.activeSession!!.sessionID
            } else {
                //close application
                LogoutManager.closeApplication(this, acManager)
                return
            }
        }
        LogUtils.d(TAG, "Session: $sessionIndex")
        if (!sessionList.contains(sessionIndex)) {
            sessionList.add(sessionIndex)
        }
        session = AudioCodesUA.getInstance().getSession(sessionIndex)
        if (session == null) {
            Log.d(TAG, "Session is null!!!")
            Toast.makeText(this, getString(R.string.session_null_error), Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        lastSessionIndex = sessionIndex
        updateUI()
        CallForegroundService.startService(this)
        val intentFilter = IntentFilter(ACTION_CALL_TERMINATED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(callTerminateReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(callTerminateReceiver, intentFilter)
        }
        timerRunnable = Runnable {
            session?.let {
                callDuration += 1
                binding.callTextviewDuration.text = convertSecondsToTime(callDuration)
                timerHandler.postDelayed(timerRunnable, 1000)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        var sessionIndex = intent.getIntExtra(SESSION_ID, -1)
        if (sessionIndex == -1) {
            sessionIndex = lastSessionIndex
        } else {
            lastSessionIndex = sessionIndex
        }
        LogUtils.d(TAG, "Session: $sessionIndex")
        if (!sessionList.contains(sessionIndex)) {
            sessionList.add(sessionIndex)
        }
        if (session?.hasVideo() == true) {
            session?.stopVideo()
        }
        session = AudioCodesUA.getInstance().getSession(sessionIndex)
        binding.callButtonSwitchCall.isEnabled = true
        updateUI()
    }

    private fun initGUI() {
        // transfer call is always enabled, if only 1 call is present, it will transfer the call to a preconfigured number
        // otherwise it will transfer the active call to the 2nd call.
        binding.callButtonTransfer.isEnabled = true
        binding.callButtonEndCall.setOnClickListener {
            LogUtils.d(TAG, "session getTermination: " + session?.termination)
            if (session?.termination == CallTermination.TERMINATED_NOT_FOUND) {
                handleCallTermination(session)
                return@setOnClickListener
            }
            if (session?.termination == CallTermination.TERMINATED_MEDIA_FAILED) {
                handleCallTermination(session)
                return@setOnClickListener
            }
            //session.
            LogUtils.d(TAG, "session getCallState: " + session?.callState)
            LogUtils.d(TAG, "session getTermination: " + session?.termination)
//            session?.terminate()
            val message = BotClient.getCallEventMessage()
            message?.let {
                it[TYPE] = CALL_AGENT_WEBRTC_TERMINATED
                BotClient.getInstance().sendMessage("", Gson().toJson(it))
            }
            ACManager.getInstance().terminate()
            handleCallTermination(session)
        }
        binding.callButtonEndVideoCall.setOnClickListener {
            LogUtils.d(TAG, "session getTermination: " + session?.termination)
            if (session?.termination == CallTermination.TERMINATED_NOT_FOUND) {
                handleCallTermination(session)
                return@setOnClickListener
            }
            if (session?.termination == CallTermination.TERMINATED_MEDIA_FAILED) {
                handleCallTermination(session)
                return@setOnClickListener
            }
            //session.
            LogUtils.d(TAG, "session getCallState: " + session?.callState)
            LogUtils.d(TAG, "session getTermination: " + session?.termination)
            session?.terminate()
            handleCallTermination(session)
        }
        binding.callButtonSwitchCamera.setOnClickListener { session?.switchCamera() }
        binding.callButtonHold.setOnClickListener {
            val hold = session?.isLocalHold ?: true
            setHold(!hold)
        }
        binding.callButtonVideoHold.setOnClickListener {
            val hold = session?.isLocalHold ?: true
            setHold(!hold)
        }
        binding.callButtonMute.setOnClickListener { v: View ->
            muteAudio(binding.callButtonMute)
        }
        binding.callButtonVideoMute.setOnClickListener { v: View ->
            muteAudio(binding.callButtonVideoMute)
        }
        binding.callButtonSwitchCall.setOnClickListener {
            for (index in sessionList) {
                LogUtils.d(TAG, "Session ID: $index")
            }
            for (index in sessionList) {
                if (index != session?.sessionID) {
                    session?.hold(true)
                    session = AudioCodesUA.getInstance().getSession(index)
                    session?.hold(false)
                    updateUI()
                    Log.d(TAG, "Session index: $index")
                    return@setOnClickListener
                }
            }
        }
        binding.callButtonAddCall.setOnClickListener {
            val call = Prefs.getSecondCall(this)
            if (call == null || call == "") {
                Toast.makeText(this, getString(R.string.no_remote_contact), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                LogUtils.d(TAG, "Calling to: $call")
                if (session?.hasVideo() == true) {
                    session?.stopVideo()
                }
                val contact = RemoteContact()
                contact.userName = call
                session = AudioCodesUA.getInstance().call(contact, session?.hasVideo() == true, null)
                LogUtils.d(TAG, "Added call_activity: " + session?.sessionID)
                if (!sessionList.contains(session?.sessionID)) {
                    session?.sessionID?.let { it1 -> sessionList.add(it1) }
                }
                binding.callButtonSwitchCall.isEnabled = true
                updateCallGui()
                updateUI()
            } catch (e: WebRTCException) {
                LogUtils.d(TAG, "oops: " + e.message)
            }
        }
        binding.callButtonTransfer.setOnClickListener {
            if (sessionList.size > 1) {
                for (index in sessionList) {
                    if (index != session?.sessionID) {
                        val transferSession = AudioCodesUA.getInstance().getSession(index)
                        transferSession.transferCall(session)
                        updateUI()
                        return@setOnClickListener
                    }
                }
            } else {
                val call = Prefs.getTransferCall(this)
                if (call == null || call == "") {
                    Toast.makeText(this, getString(R.string.no_remote_contact), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val transferContact = RemoteContact()
                transferContact.userName = call
                session?.transferCall(transferContact)
            }
        }
        binding.callButtonAudioRoute.setOnClickListener {
            route = when (route) {
                AudioRoute.BLUETOOTH -> AudioRoute.SPEAKER_PHONE
                AudioRoute.SPEAKER_PHONE -> AudioRoute.EARPIECE
                else -> AudioRoute.BLUETOOTH
            }
            LogUtils.d("WebRTCAudioManager", "new route = $route")
            ac.setAudioRoute(route)
        }
        binding.callButtonVideoRoute.setOnClickListener {
            binding.callButtonAudioRoute.performClick()
        }
        binding.callButtonAddVideo.setOnClickListener {
            LogUtils.d(TAG, "hasVideo: " + session?.hasVideo() + " isVideoMuted: " + session?.isVideoMuted)
            if (session?.hasVideo() == true) {
                Log.d(TAG, "new video mute stat: " + session?.isVideoMuted)
                session?.muteVideo(session?.isVideoMuted == false)
            } else {
                Log.d(TAG, "add video to the session")
                session?.reinviteWithVideo()
            }
            updateVideoButton()
        }
        binding.callButtonDtmf.setOnClickListener { v: View ->
            dmfEnabled = !dmfEnabled
            updateCallGui()
            (v as ImageView).setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_dial_pad_off, theme))
            if (dmfEnabled) v.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_dial_pad_on, theme))
        }
        session?.addSessionEventListener(this)
        initDtmf()
        updateVideoButton()
        guiInitialized = true
    }

    private fun muteAudio(view: ImageView) {
        var isAudioMuted = session?.isAudioMuted ?: return
        LogUtils.d(TAG, "isAudioMuted before: $isAudioMuted")
        isAudioMuted = !isAudioMuted
        session?.muteAudio(isAudioMuted)
        session?.muteVideo(isAudioMuted)
        isAudioMuted = session?.isAudioMuted!!
        LogUtils.d(TAG, "isAudioMuted after: $isAudioMuted")
        view.isSelected = isAudioMuted
        view.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_call_mute_off, theme))
        if (isAudioMuted) view.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_call_mute_on, theme))
    }

    private fun setHold(hold: Boolean) {
        LogUtils.d(TAG, "setHold: $hold")
        binding.callButtonHold.isSelected = hold
        session?.hold(hold)
    }

    private fun initDtmf() {
        binding.callButtonKeypad1.setOnClickListener { session?.sendDTMF(DTMF.ONE) }
        binding.callButtonKeypad2.setOnClickListener { session?.sendDTMF(DTMF.TWO) }
        binding.callButtonKeypad3.setOnClickListener { session?.sendDTMF(DTMF.THREE) }
        binding.callButtonKeypad4.setOnClickListener { session?.sendDTMF(DTMF.FOUR) }
        binding.callButtonKeypad5.setOnClickListener { session?.sendDTMF(DTMF.FIVE) }
        binding.callButtonKeypad6.setOnClickListener { session?.sendDTMF(DTMF.SIX) }
        binding.callButtonKeypad7.setOnClickListener { session?.sendDTMF(DTMF.SEVEN) }
        binding.callButtonKeypad8.setOnClickListener { session?.sendDTMF(DTMF.EIGHT) }
        binding.callButtonKeypad9.setOnClickListener { session?.sendDTMF(DTMF.NINE) }
        binding.callButtonKeypad0.setOnClickListener { session?.sendDTMF(DTMF.ZERO) }
        binding.callButtonKeypadAsterisk.setOnClickListener { session?.sendDTMF(DTMF.STAR) }
    }

    fun updateUI() {
        handler.post {
            if (!guiInitialized) {
                initGUI()
                WebRTCAudioManager.getInstance().webRTcAudioRouteListener = MyWebRTcAudioRouteListener()
                updateCallGui()
            }
            val remote = session?.remoteNumber
            if (remote != null) {
                binding.callTextviewCallState.text = "${getString(R.string.call_textview_call_state)} ${session?.callState}"
                val nativeDBObjectList = NativeDBManager.getContactList(this, NativeDBManager.QueryType.BY_PHONE_AND_SIP, remote.userName)
                var displayName = remote.displayName
                val userName = remote.userName
                if (!nativeDBObjectList.isNullOrEmpty()) {
                    val nativeDBObject = nativeDBObjectList[0]
                    if (nativeDBObject != null) {
                        val photoBitmap = ImageUtils.getContactBitmapFromURI(this, Uri.parse(nativeDBObject.photoURI))
                        if (photoBitmap != null) {
                            val roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(
                                photoBitmap,
                                binding.callImageViewContactImage.drawable.intrinsicHeight
                            )
                            binding.callImageViewContactImage.setImageBitmap(roundPhotoBitmap)
                        }
                    }
                    if (nativeDBObject != null) {
                        displayName = nativeDBObject.displayName
                    }
                }
                binding.callTextviewContactName.text = displayName
                binding.callTextviewContactNumber.text = userName
                session?.addSessionEventListener(myAudioCodesSessionEventListener)
            } else {
                finish()
                Log.d(TAG, "RemoteContact null!")
            }
        }
    }

    private fun updateVideoButton() {
        val hasVideoInt: Int = if (session?.hasVideo() == true && session?.isVideoMuted == false) {
            R.mipmap.call_button_icon_mute_video
        } else {
            R.mipmap.call_button_icon_add_video
        }
        binding.callButtonAddVideo.setImageResource(hasVideoInt)
    }

    fun updateCallGui() {
        Log.d(TAG, "updateCallGui")
        val videoCallLayout = findViewById<View>(R.id.call_layout_ac_webrtc_video)
        val audioCallLayout = findViewById<View>(R.id.call_layout_audio_call)
        val dtmfCallLayout = findViewById<View>(R.id.call_layout_dtmf_pad)
        val topButtonsLayout = findViewById<View>(R.id.call_layout_top_buttons)
        val llVideo = findViewById<View>(R.id.llVideoButtons)
        val buttonsLayout = findViewById<View>(R.id.call_layout_buttons)
        if (dmfEnabled) {
            Log.d(TAG, "updateCallGui: DTMF")
            //show dtmf screen
            videoCallLayout.visibility = View.GONE
            llVideo.visibility = View.GONE
            audioCallLayout.visibility = View.VISIBLE
            buttonsLayout.visibility = View.VISIBLE
            dtmfCallLayout.visibility = View.VISIBLE
            topButtonsLayout.visibility = View.GONE
        } else if (session?.hasVideo() == true) {
            Log.d(TAG, "updateCallGui: video")
            //show video screen
            videoCallLayout.visibility = View.VISIBLE
            llVideo.visibility = View.VISIBLE
            audioCallLayout.visibility = View.GONE
            buttonsLayout.visibility = View.GONE
            dtmfCallLayout.visibility = View.GONE
            topButtonsLayout.visibility = View.VISIBLE
            try {
                session?.setLocaLRenderPosition(70, 50)
                session?.showVideo(this@CallActivity)
            } catch (e: WebRTCException) {
                Log.d(TAG, "oops: " + e.message)
            }
        } else {
            Log.d(TAG, "updateCallGui: audio")
            //show call screen
            videoCallLayout.visibility = View.GONE
            llVideo.visibility = View.GONE
            audioCallLayout.visibility = View.VISIBLE
            buttonsLayout.visibility = View.VISIBLE
            dtmfCallLayout.visibility = View.GONE
            topButtonsLayout.visibility = View.VISIBLE
        }
        updateVideoButton()
    }

    override fun onResume() {
        super.onResume()
        if (session != null && session?.termination != null) {
            handleCallTermination(session)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(callTerminateReceiver)
        //CallBackHandler.unregisterCallStateChanged(callStateChanged);
        CallBackHandler.unregisterLoginStateChange(loginStateChanged)
    }

    override fun callTerminated(session: AudioCodesSession, terminationInfo: TerminationInfo) {
        LogUtils.d(TAG, "callTerminated")
        LogUtils.d(TAG, "terminationInfo1: " + terminationInfo.callTermination)
        LogUtils.d(TAG, "terminationInfo2: " + terminationInfo.statusCode)
        LogUtils.d(TAG, "terminationInfo2: " + terminationInfo.pjssipStatusCode)
        LogUtils.d(TAG, "terminationInfo3: " + terminationInfo.reason)
        LogUtils.d(TAG, "terminationInfo4: " + terminationInfo.reasonHeader)
        LogUtils.d(TAG, "terminationInfo5: " + terminationInfo.sipMessage)
        // handleCallTermination(session,terminationInfo);
    }

    override fun callProgress(session: AudioCodesSession) {
        LogUtils.d(TAG, "callProgress CallState: " + session.callState)
        if (session.callState == CallState.CONNECTED || session.callState == CallState.HOLD) {
            if (session.callState == CallState.CONNECTED && callDuration == 0) {
                timerHandler.postDelayed(timerRunnable, 1000)
            }
            handler.post {
                binding.callButtonEndCall.isEnabled = true
                binding.callButtonHold.isEnabled = true
            }
        }
    }

    override fun mediaFailed(session: AudioCodesSession) {
        Log.d(TAG, "media failed1")
    }

    override fun cameraSwitched(frontCamera: Boolean) {}

    override fun reinviteWithVideoCallback(audioCodesSession: AudioCodesSession) {}

    override fun incomingNotify(notifyEvent: NotifyEvent, dtmfValue: String) {
        LogUtils.e("Called", "Duration ${session?.duration()}")
    }

    fun onNotifyEvent(notifyEvent: NotifyEvent, dtmfValue: String?) {
        LogUtils.d(TAG, "onNotifyEvent: $notifyEvent")
        when (notifyEvent) {
            NotifyEvent.TALK -> handler.post {
                Log.d(TAG, "Notify resume")
                if (session?.isLocalHold == true) {
                    setHold(false)
                }
            }

            NotifyEvent.HOLD -> handler.post {
                Log.d(TAG, "Notify hold")
                if (session?.isLocalHold == false) {
                    setHold(true)
                }
            }

            NotifyEvent.DTMF -> {
                Log.d(TAG, "Notify DTMF")
                val thread = Thread {
                    val dtmfValueArr = getDTMFArrayFromString(dtmfValue)
                    if (dtmfValueArr != null) {
                        for (dtmf in dtmfValueArr) {
                            if (dtmf != null) {
                                try {
                                    Thread.sleep((ACConfiguration.getConfiguration().dtmfOptions.duration + 30).toLong())
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                Log.d(TAG, "incomingNotify dtmfValue: $dtmf")
                                session?.sendDTMF(dtmf)
                            }
                        }
                    }
                }
                thread.start()
            }

            else -> {}
        }
    }

    private fun getDTMFArrayFromString(dtmfString: String?): Array<DTMF?>? {
        if (dtmfString == null) {
            return null
        }
        val dtmfValueArr = dtmfString.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var res: Array<DTMF?>? = null
        if (dtmfValueArr.isNotEmpty()) {
            res = arrayOfNulls(dtmfValueArr.size - 1)
            var charIndex = 0
            for (signalValueArrChar in dtmfValueArr) {
                if (signalValueArrChar == "") {
                    continue
                }
                when (signalValueArrChar) {
                    "1" -> res[charIndex] = DTMF.ONE
                    "2" -> res[charIndex] = DTMF.TWO
                    "3" -> res[charIndex] = DTMF.THREE
                    "4" -> res[charIndex] = DTMF.FOUR
                    "5" -> res[charIndex] = DTMF.FIVE
                    "6" -> res[charIndex] = DTMF.SIX
                    "7" -> res[charIndex] = DTMF.SEVEN
                    "8" -> res[charIndex] = DTMF.EIGHT
                    "9" -> res[charIndex] = DTMF.NINE
                    "0" -> res[charIndex] = DTMF.ZERO
                    "#" -> res[charIndex] = DTMF.POUND
                    "*" -> res[charIndex] = DTMF.STAR
                }
                charIndex++
            }
        }
        return res
    }

    inner class MyAudioCodesSessionEventListener : AudioCodesSessionEventListener {
        override fun callTerminated(call: AudioCodesSession, info: TerminationInfo) {
            CallForegroundService.stopService(this@CallActivity)
            handler.post {
                Log.d(TAG, "MyAudioCodesSessionEventListener")
                handleCallTermination(call)
            }
        }

        override fun callProgress(call: AudioCodesSession) {
            LogUtils.d(TAG, "Call state: " + call.callState)
            LogUtils.d(TAG, "Call session id: " + call.sessionID)
            LogUtils.d(TAG, "Call number: " + call.remoteNumber.userName)
            handler.post {
                if (call.callState == CallState.RINGING) {
                    LogUtils.d(TAG, "do local ring!")
                    AppUtils.startRingingMP(this@CallActivity, "ringback.wav", true, false, null)
                }
                if (call.callState == CallState.CONNECTED) {
                    AppUtils.stopRingingMP(this@CallActivity)
                }
                if (call.sessionID == session?.sessionID) {
                    binding.callTextviewCallState.text = getString(R.string.call_textview_call_state) + " " + call.callState
                    if (call.callState == CallState.TRANSFER) {
                        if (call.transferContact != null) {
                            binding.callTextviewCallTransferStateNumber.text =
                                call.transferState.toString() + " - " + call.transferContact.userName
                        }
                    } else {
                        binding.callTextviewCallTransferStateNumber.text = ""
                    }
                    // updateUI();
                }
                updateUI()
            }
        }

        override fun cameraSwitched(frontCamera: Boolean) {
            LogUtils.d(TAG, "cameraSwitched: $frontCamera")
        }

        override fun reinviteWithVideoCallback(audioCodesSession: AudioCodesSession) {
            LogUtils.d(
                TAG,
                "reInviteWithVideoCallback name: " + audioCodesSession.remoteNumber.displayName + " userName: " + audioCodesSession.remoteNumber.userName
            )
            Log.d(TAG, "hasVideo: " + audioCodesSession.hasVideo())
            handler.post {
                //audioCodesSession.answer(null, true);
                try {
                    //audioCodesSession.showVideo(CallActivity.this);
                    updateCallGui()
                } catch (e: Exception) {
                    LogUtils.e(TAG, "error: $e")
                }
            }
        }

        override fun mediaFailed(session: AudioCodesSession) {
            Log.d(TAG, "media failed2")
            if (handler != null) {
                handler.post { Toast.makeText(this@CallActivity, "Media failure, trying to reconnect....", Toast.LENGTH_LONG).show() }
            }
        }

        override fun incomingNotify(notifyEvent: NotifyEvent, dtmfValue: String) {
            LogUtils.d(TAG, "NotifyEvent: $notifyEvent")
            onNotifyEvent(notifyEvent, dtmfValue)
        }
    }

    fun handleCallTermination(audioCodesSession: AudioCodesSession?) {
        handler.post {
            LogUtils.d(TAG, "Checking for termination: " + audioCodesSession!!.sessionID)
            Toast.makeText(this@CallActivity, "Call terminated: " + audioCodesSession!!.termination, Toast.LENGTH_SHORT).show()
            AppUtils.stopRingingMP(this)
            sessionList.remove(audioCodesSession.sessionID)
            LogUtils.d(TAG, "sessionList.size() " + sessionList.size)
            val currentSession = session
            if (sessionList.size == 0) {
                WebRTCAudioManager.getInstance().webRTcAudioRouteListener = null
                finish()
            } else {
                binding.callButtonSwitchCall.isEnabled = false
                for (session1 in sessionList) {
                    Log.d(TAG, "session1: id $session1")
                }
                try {
                    session = AudioCodesUA.getInstance().getSession(sessionList[0])
                } catch (e: Exception) {
                    Log.d(TAG, "oops: " + e.message)
                }
                if (session == null) {
                    session = AudioCodesUA.getInstance().getSession(0)
                }
                if (audioCodesSession.termination != CallTermination.TERMINATED_TRANSFER) {
                    if (currentSession!!.sessionID == audioCodesSession.sessionID && currentSession.transferState == CallTransferState.NONE) {
                        session?.hold(false)
                    }
                }
                updateUI()
            }
        }
    }

    internal inner class MyWebRTcAudioRouteListener : WebRTCAudioRouteListener {
        override fun audioRoutesChanged(audioRouteList: List<AudioRoute>) {
            LogUtils.d(TAG, "ROUTS: $audioRouteList")
        }

        override fun currentAudioRouteChanged(newAudioRoute: AudioRoute) {
            LogUtils.d(TAG, "currentAudioRouteChanged: $newAudioRoute")
            route = newAudioRoute
            val audioRouteInt = when (route) {
                AudioRoute.SPEAKER_PHONE -> R.drawable.ic_audio_speaker_on
                AudioRoute.EARPIECE -> R.drawable.ic_audio_off
                else -> R.mipmap.call_button_icon_bluetooth
            }
            binding.callButtonAudioRoute.setImageResource(audioRouteInt)
        }
    }

    private fun convertSecondsToTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun finish() {
        super.finish()
        timerHandler.removeCallbacks(timerRunnable)
    }

    companion object {
        private const val TAG = "CallActivity"
        const val SESSION_ID = "sessionID"
        const val ACTION_CALL_TERMINATED = "com.kore.botsdk.ACTION_CALL_TERMINATED"
        const val CALL_AGENT_WEBRTC_TERMINATED = "call_agent_webrtc_terminated"
        const val CANCEL_AGENT_WEBRTC = "cancel_agent_webrtc"
        const val TERMINATE_AGENT_WEBRTC = "terminate_agent_webrtc"
        const val CALL_AGENT_WEBRTC_ACCEPTED = "call_agent_webrtc_accepted"
        const val CALL_AGENT_WEBRTC_REJECTED = "call_agent_webrtc_rejected"
    }
}