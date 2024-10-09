package com.kore.ui.audiocodes.webrtcclient.general

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus
import com.audiocodes.mv.webrtcsdk.log.LogLevel
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSessionEventListener
import com.audiocodes.mv.webrtcsdk.session.CallState
import com.audiocodes.mv.webrtcsdk.session.DTMFOptions
import com.audiocodes.mv.webrtcsdk.session.InfoAlert
import com.audiocodes.mv.webrtcsdk.session.NotifyEvent
import com.audiocodes.mv.webrtcsdk.session.RemoteContact
import com.audiocodes.mv.webrtcsdk.session.TerminationInfo
import com.audiocodes.mv.webrtcsdk.useragent.ACConfiguration
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesEventListener
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA
import com.audiocodes.mv.webrtcsdk.useragent.WebRTCException
import com.kore.common.utils.LogUtils
import com.kore.ui.audiocodes.webrtcclient.activities.CallActivity
import com.kore.ui.audiocodes.webrtcclient.activities.IncomingCallActivity
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.loginStateChange
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.onMessageStatus
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.onNewMessage
import com.kore.ui.audiocodes.webrtcclient.db.MySQLiteHelper
import com.kore.ui.audiocodes.webrtcclient.structure.CallEntry
import com.kore.ui.audiocodes.webrtcclient.structure.SipAccount
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class ACManager private constructor() : AudioCodesEventListener {
    var isRegisterState = false
        private set
    private var dataBase: MySQLiteHelper? = null
    private lateinit var context: Context

    companion object {
        private const val TAG = "ACManager"

        @SuppressLint("StaticFieldLeak")
        private var acManager: ACManager? = null

        @Synchronized
        fun getInstance(): ACManager {
            if (acManager == null) {
                acManager = ACManager()
            }
            return acManager!!
        }
    }

    @JvmOverloads
    fun startLogin(context: Context, autologin: Boolean = Prefs.getAutoLogin(context), disconnectCall: Boolean = Prefs.getDisconnectBrokenConnection(context)) {
        this.context = context
        if (dataBase == null) dataBase = MySQLiteHelper(context)
        LogUtils.d(TAG, "startLogin")
        var loginit = false
        AudioCodesUA.getInstance().disconnectOnBrokenConnection(disconnectCall)
        try {
            initACUA()
            loginit = true
        } catch (e: Exception) {
            LogUtils.d(TAG, "can't set log level")
        }
        initWebRTC(context, Prefs.getSipAccount(context))
        Prefs.setAutoLogin(context, autologin)
        Prefs.setDisconnectBrokenConnection(context, disconnectCall)
        try {
            AudioCodesUA.getInstance().login(context, autologin)
            if (!loginit) {
                initACUA()
            }
        } catch (e: Exception) {
            LogUtils.d(TAG, "Error in login: $e")
            Toast.makeText(context, "Error in login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initACUA() {
        AudioCodesUA.getInstance().setLogger(LogI())
        AudioCodesUA.getInstance().setLogLevel(LogLevel.VERBOSE)
    }

    fun startLogout() {
        LogUtils.d(TAG, "startLogout")
        try {
            AudioCodesUA.getInstance().logout()
        } catch (e: Exception) {
            LogUtils.d(TAG, "Error in logout")
        }
    }

    private fun initWebRTC(context: Context, sipAccount: SipAccount) {
        val proxy = sipAccount.proxy
        val port = sipAccount.port
        val domain = sipAccount.domain
        val transport = sipAccount.transport
        val username = sipAccount.username
        val password = sipAccount.password
        val displayName = sipAccount.displayName
        LogUtils.d(TAG, "sipAccount: $sipAccount")

        //   AudioCodesUA.getInstance().setContactRewrite(true);
        AudioCodesUA.getInstance().setServerConfig(proxy, port, domain, transport, ArrayList())
        // AudioCodesUA.getInstance().setServerConfig(proxy,port,domain,transport, null);
        AudioCodesUA.getInstance().setAllowHeader(null)
        LogUtils.d(TAG, "setVerifyServer: " + true)
        AudioCodesUA.getInstance().setVerifyServer(true)
        val useCustomPemFile = false
        if (useCustomPemFile) {
            // make sure context is not null at this point
            AudioCodesUA.getInstance().setCaCertFilePath(copyPemFile(context))
        }
        AudioCodesUA.getInstance().setAccount(username, password, displayName)
        AudioCodesUA.getInstance().setListener(this)
        updateWebRTCConfig(context)
    }

    fun updateWebRTCConfig(context: Context) {
        this.context = context
        //set dtmf settings
        val dtmfMethod = Prefs.getDTMFType(context)
        val dtmfOptions = DTMFOptions()
        dtmfOptions.dtmfMethod = dtmfMethod
        LogUtils.d(TAG, "use dtmfMethod: $dtmfMethod")
        ACConfiguration.getConfiguration().dtmfOptions = dtmfOptions
        LogUtils.d(TAG, "use isAutoRedirect: " + Prefs.getIsAutoRedirect(context))
        ACConfiguration.getConfiguration().automaticCallOnRedirect = Prefs.getIsAutoRedirect(context)
        AudioCodesUA.getInstance().setVideoCodecHardwareAceleration(Prefs.getIsVideoHardware(context))
        val remoteContact = RemoteContact()
        remoteContact.scheme = null
        remoteContact.displayName = Prefs.getRedirectCallUser(context)
        remoteContact.userName = Prefs.getRedirectCallUser(context)
        remoteContact.domain = Prefs.getSipAccount(context).domain
        LogUtils.d(TAG, "use isRedirectCall: " + Prefs.getIsRedirectCall(context) + " with RedirectCallUser: " + Prefs.getRedirectCallUser(context))
        ACConfiguration.getConfiguration().setRedirect(Prefs.getIsRedirectCall(context), remoteContact)
    }

    private fun copyPemFile(context: Context): String {
        // this file should be in assets and provided by the user of the SDK
        val filePath = "cacert.pem"
        val cacheDir = File(context.cacheDir, filePath)
        try {
            val `in` = context.assets.open(filePath)
            val out = FileOutputStream(cacheDir.path)
            var read: Int
            val buffer = ByteArray(4096)
            while (`in`.read(buffer).also { read = it } > 0) {
                out.write(buffer, 0, read)
            }
            out.close()
            `in`.close()
        } catch (e: Exception) {
            LogUtils.d(TAG, "oops: " + e.message)
        }
        return cacheDir.path
    }

    override fun loginStateChanged(isLogin: Boolean, code: Int, cause: String) {
        LogUtils.e(TAG, "loginStateChanged isLogin: $isLogin cause: $cause")
        LogUtils.e(TAG, "loginStateChanged currentState: $isLogin prevState: $isRegisterState")
        if (isRegisterState != isLogin || !Prefs.getAutoLogin(context)) {
            isRegisterState = isLogin
            loginStateChange(isRegisterState)
            NotificationUtils.createAppNotification(context, this)
        }
    }

    val activeSession: AudioCodesSession?
        get() {
            var audioCodesSession: AudioCodesSession? = null
            val audioCodesSessionArrayList = AudioCodesUA.getInstance().sessionList
            for (session in audioCodesSessionArrayList) {
                if (session.callState != null) {
                    audioCodesSession = session
                    break
                }
            }
            //AudioCodesSession audioCodesSession = AudioCodesUA.getInstance().getSession(0);
            //return audioCodesSession;
            return audioCodesSession
        }
    val sessionList: ArrayList<AudioCodesSession>
        get() = AudioCodesUA.getInstance().sessionList

    @JvmOverloads
    fun callNumber(context: Context, callNumber: String, videoCall: Boolean = false) {
        try {
            AppUtils.saveVolumeSettings(context, true)
            AppUtils.setLastCallVolumeSettings(context)
            AppUtils.setSpeaker(context, true)
            LogUtils.d(TAG, "start callNumber: $callNumber isVideoCall: $videoCall")
            val contact = RemoteContact()
            contact.userName = callNumber
            contact.displayName = callNumber
            val session = AudioCodesUA.getInstance().call(contact, videoCall, null)
            val callIntent = Intent(context, CallActivity::class.java)
            session.addSessionEventListener(audioCodesSessionEventListener)
            LogUtils.d(TAG, "callNumber startActivity")
            callIntent.putExtra(CallActivity.SESSION_ID, session.sessionID)
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(callIntent)
        } catch (e: WebRTCException) {
            LogUtils.d(TAG, "oops: " + e.message)
        }
    }

    override fun incomingCall(call: AudioCodesSession, infoAlert: InfoAlert) {
        var curCallConnected = false
        try {
            LogUtils.d(TAG, "Incoming call")
            LogUtils.d(TAG, "Remote user: " + call.remoteNumber.userName)
            LogUtils.d(TAG, "current  getActiveSession().getCallState(): " + activeSession!!.callState)
            curCallConnected = activeSession!!.callState == CallState.CONNECTED
            LogUtils.d(TAG, "current is connected: $curCallConnected")
            AppUtils.saveVolumeSettings(context, true)
            AppUtils.setLastCallVolumeSettings(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (curCallConnected && call.remoteNumber.userName == activeSession!!.remoteNumber.userName && call.remoteNumber.displayName == activeSession!!.remoteNumber.displayName) {
            LogUtils.d(TAG, "this is the second instance of the same call, reject it")
            call.reject(null)
            return
        }
        if (infoAlert.autoAnswer) {
            LogUtils.d(TAG, "InfoAlert autoAnswer: " + infoAlert.autoAnswer)
            if (infoAlert.delay > 0) {
                //you can add a different auto answer protocol here if a long delay is needed
            } else {

                //answer call immediately, without showing incoming call GUI
                val callIntent = Intent(context, CallActivity::class.java)
                callIntent.putExtra(CallActivity.SESSION_ID, call.sessionID)
                context.startActivity(callIntent)
                call.answer(null, false)
                return
            }
        }
        call.addSessionEventListener(audioCodesSessionEventListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !curCallConnected) {
            val video = AudioCodesUA.getInstance().getSession(call.sessionID).hasVideo()
            NotificationUtils.createCallNotification(context, call.remoteNumber.displayName, call.sessionID, video)
            val session = AudioCodesUA.getInstance().getSession(call.sessionID)
            session.addSessionEventListener(object : AudioCodesSessionEventListener {
                override fun callTerminated(audioCodesSession: AudioCodesSession, info: TerminationInfo) {
                    NotificationUtils.removeCallNotification(context)
                }

                override fun callProgress(audioCodesSession: AudioCodesSession) {}
                override fun cameraSwitched(b: Boolean) {}
                override fun reinviteWithVideoCallback(audioCodesSession: AudioCodesSession) {}
                override fun mediaFailed(session: AudioCodesSession) {
                    LogUtils.d(TAG, "media failed3")
                }

                override fun incomingNotify(notifyEvent: NotifyEvent, s: String) {
                    LogUtils.d(TAG, "incomingNotify: $notifyEvent")
                    when (notifyEvent) {
                        NotifyEvent.TALK -> {
                            LogUtils.d(TAG, "Notify answer call")
                            NotificationUtils.removeCallNotification(context)
                            val incomingCallIntent = Intent(context, IncomingCallActivity::class.java)
                            incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            incomingCallIntent.putExtra(IncomingCallActivity.SESSION_ID, call.sessionID)
                            incomingCallIntent.putExtra(IncomingCallActivity.INTENT_ANSWER_TAG, 1)
                            context.startActivity(incomingCallIntent)
                            session.removeSessionEventListener(this)
                        }

                        NotifyEvent.HOLD -> {}
                        else -> {}
                    }
                }
            })
        } else {
            val incomingCallIntent = Intent(context, IncomingCallActivity::class.java)
            incomingCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            incomingCallIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            incomingCallIntent.putExtra(IncomingCallActivity.SESSION_ID, call.sessionID)
            context.startActivity(incomingCallIntent)
        }
    }

    override fun onIncomingInstantMessage(remoteContact: RemoteContact, message: String) {
        LogUtils.d(TAG, "onInstantMessage")
        LogUtils.d(TAG, remoteContact.userName + ", " + message)
        onNewMessage(remoteContact.userName, message)
    }

    override fun onInstantMessageStatus(instanceMessageStatus: InstanceMessageStatus, ID: Long) {
        LogUtils.d(TAG, "onInstantMessageStatus $ID: $instanceMessageStatus")
        onMessageStatus(instanceMessageStatus, ID)
    }

    private var audioCodesSessionEventListener: AudioCodesSessionEventListener = object : AudioCodesSessionEventListener {
        override fun callTerminated(session: AudioCodesSession, info: TerminationInfo) {
            LogUtils.d(TAG, "callTerminated name: " + session.remoteNumber.displayName + " userName: " + session.remoteNumber.userName)

            //Save current level volume
            AppUtils.saveVolumeSettings(context, true)
            //restore previous volume settings
            AppUtils.restorePrevVolumeSettings(context)
            if (dataBase == null) dataBase = MySQLiteHelper(context)

            //save call to recents
            saveCallHistory(session)
            //save statistics
            val acCallStatistics = session.stats
            Prefs.setCallState(context, acCallStatistics)
            LogUtils.d(TAG, "ACCallStatistics: $acCallStatistics")

            //CallBackHandler.callStateChanged(CallState.NULL);
            //session.getCallState()
            //BotApplication.getDataBase().addEntry(new CallEntry("Avi", 1512997724646L, CallEntry.CallType.NOT_ANSWERED));
        }

        private fun saveCallHistory(session: AudioCodesSession) {
            val callEntry = CallEntry()
            callEntry.contactName = session.remoteNumber.displayName
            callEntry.contactNumber = session.remoteNumber.userName
            var callStartTime = session.callStartTime
            if (callStartTime == 0L) {
                callStartTime = Date().time
            }
            callEntry.startTime = callStartTime
            var callDuration = session.duration().toLong()
            if (callDuration > 0) {
                callDuration *= 1000 //from sec to milisec
            }
            var callType = CallEntry.CallType.OUTGOING
            if (!session.isOutgoing) {
                callType = if (callDuration > 0) {
                    CallEntry.CallType.INCOMING
                } else {
                    CallEntry.CallType.MISSED
                }
            }
            callEntry.callType = callType
            callEntry.duration = callDuration
            dataBase?.addEntry(callEntry)
        }

        override fun callProgress(session: AudioCodesSession) {
            LogUtils.d(TAG, "callProgress name: " + session.remoteNumber.displayName + " userName: " + session.remoteNumber.userName)
        }

        override fun cameraSwitched(frontCamera: Boolean) {
            LogUtils.d(TAG, "cameraSwitched isfrontCamera: $frontCamera")
        }

        override fun mediaFailed(session: AudioCodesSession) {
            LogUtils.d(TAG, "media failed4")
        }

        override fun incomingNotify(notifyEvent: NotifyEvent, s: String) {}
        override fun reinviteWithVideoCallback(audioCodesSession: AudioCodesSession) {
            LogUtils.d(
                TAG,
                "reinviteWithVideoCallback name: " + audioCodesSession.remoteNumber.displayName + " userName: " + audioCodesSession.remoteNumber.userName
            )
            //audioCodesSession.answer(null, true);
        }
    }

    fun sendInstantMessage(user: String, message: String?) {
        LogUtils.d(TAG, "sendInstantMessage: $user")
        val remoteContact = RemoteContact()
        remoteContact.userName = user
        remoteContact.displayName = user
        val messageID = AudioCodesUA.getInstance().sendInstantMessage(remoteContact, message)
        LogUtils.d(TAG, "message ID $messageID")
    }
}
