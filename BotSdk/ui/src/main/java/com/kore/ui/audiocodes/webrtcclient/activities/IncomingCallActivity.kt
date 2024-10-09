package com.kore.ui.audiocodes.webrtcclient.activities

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSession
import com.audiocodes.mv.webrtcsdk.session.AudioCodesSessionEventListener
import com.audiocodes.mv.webrtcsdk.session.NotifyEvent
import com.audiocodes.mv.webrtcsdk.session.TerminationInfo
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA
import com.audiocodes.mv.webrtcsdk.useragent.WebRTCException
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.db.NativeDBManager
import com.kore.ui.audiocodes.webrtcclient.db.NativeDBManager.QueryType.BY_PHONE_AND_SIP
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.ImageUtils
import com.kore.ui.audiocodes.webrtcclient.general.NotificationUtils
import com.kore.ui.audiocodes.webrtcclient.login.LogoutManager
import com.kore.ui.databinding.IncomingCallActivityBinding

class IncomingCallActivity : BaseAppCompatActivity(), AudioCodesSessionEventListener {
    private lateinit var binding: IncomingCallActivityBinding
    private var session: AudioCodesSession? = null
    private var ringtone: Ringtone? = null
    private val acManager: ACManager = ACManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        NotificationUtils.removeCallNotification(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.incoming_call_activity, null, false)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")
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
        LogUtils.d(TAG, "sessionIndex: $sessionIndex")
        session = AudioCodesUA.getInstance().getSession(sessionIndex)
        val answer = intent.getIntExtra(INTENT_ANSWER_TAG, -1)
        LogUtils.d("aaa", "ANSWER: $answer")
        when (answer) {
            0 -> decline()
            1 -> answer()
            2 -> answerWithVideo()
            else -> {
                unlockScreen()
                initGui()
                startRingingTimer()
                playRingtone()
            }
        }
    }

    private fun unlockScreen() {
        this.window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun initGui() {
        if (session == null) {
            startNextActivity(MainActivity::class.java)
            finish()
            return
        }
        val nativeDBObjectList = NativeDBManager.getContactList(this, BY_PHONE_AND_SIP, session?.remoteNumber?.userName)
        var displayName = session?.remoteNumber?.displayName
        val userName = session?.remoteNumber?.userName
        LogUtils.d(TAG, "check user name: $userName displayName:$displayName")
        if (!nativeDBObjectList.isNullOrEmpty()) {
            LogUtils.d(TAG, "check user nativeDBObjectList: " + nativeDBObjectList.size)
            val nativeDBObject = nativeDBObjectList[0]
            if (nativeDBObject?.photoURI != null) {
                val photoBitmap = ImageUtils.getContactBitmapFromURI(this, Uri.parse(nativeDBObject.photoURI))
                if (photoBitmap != null) {
                    val roundPhotoBitmap = ImageUtils.getCroppedRoundBitmap(
                        photoBitmap,
                        binding.incomingCallImageViewContactImage.drawable.intrinsicHeight
                    )
                    binding.incomingCallImageViewContactImage.setImageBitmap(roundPhotoBitmap)
                }
            }
            displayName = nativeDBObject?.displayName
        }
        binding.incomingCallTextviewContactName.text = displayName
        binding.incomingCallTextviewContactNumber.text = userName
        binding.incomingCallButtonAnswerVideo.isVisible = session?.hasVideo() == true
        binding.incomingCallButtonAnswer.setOnClickListener { answer() }
        binding.incomingCallButtonAnswerVideo.setOnClickListener { answerWithVideo() }
        binding.incomingCallButtonDecline.setOnClickListener { decline() }
        session?.addSessionEventListener(this)
        binding.incomingCallLayoutAcWebrtcVideo.isVisible = session?.hasVideo() == true
        binding.incomingCallImageViewContactImage.isVisible = session?.hasVideo() == false
        binding.incomingCallLayoutAudioCall.isVisible = session?.hasVideo() == false
        if (session?.hasVideo() == true) {
            try {
                session?.showVideo(this)
            } catch (e: WebRTCException) {
                Log.d(TAG, "oops: " + e.message)
            }
        }
    }

    private fun decline() {
        LogUtils.d(TAG, "decline")
        stopRingtone()
        stopRingingTimer()
        session?.reject(null)
        finish()
    }

    private fun answer() {
        stopRingtone()
        stopRingingTimer()
        if (session == null) {
            startNextActivity(MainActivity::class.java)
            finish()
            return
        }
        val callIntent = Intent(this@IncomingCallActivity, CallActivity::class.java)
        callIntent.putExtra(CallActivity.SESSION_ID, session?.sessionID)
        startActivity(callIntent)
        session?.answer(null, false)
        finish()
    }

    private fun answerWithVideo() {
        stopRingtone()
        stopRingingTimer()
        if (session == null) {
            startNextActivity(MainActivity::class.java)
            finish()
            return
        }
        val callIntent = Intent(this@IncomingCallActivity, CallActivity::class.java)
        callIntent.putExtra(CallActivity.SESSION_ID, session?.sessionID)
        startActivity(callIntent)
        session?.answer(null, true)
        finish()
    }

    private fun playRingtone() {
        LogUtils.d(TAG, "playRingtone")
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(applicationContext(), uri)
        ringtone?.play()
    }

    private fun stopRingtone() {
        if (ringtone?.isPlaying == true) ringtone?.stop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtone()
    }

    override fun callTerminated(call: AudioCodesSession, terminationInfo: TerminationInfo) {
        handleCallTermination(call, terminationInfo)
    }

    override fun callProgress(call: AudioCodesSession) {}
    override fun cameraSwitched(frontCamera: Boolean) {}
    override fun reinviteWithVideoCallback(audioCodesSession: AudioCodesSession) {}
    override fun mediaFailed(session: AudioCodesSession) {
        Log.d(TAG, "media failed2")
    }

    override fun incomingNotify(notifyEvent: NotifyEvent, dtmfValue: String) {
        LogUtils.d(TAG, "incomingNotify: $notifyEvent")
        when (notifyEvent) {
            NotifyEvent.TALK -> handler.post {
                Log.d(TAG, "Notify answer call")
                answer()
            }

            NotifyEvent.HOLD -> {}
            else -> {}
        }
    }

    private fun handleCallTermination(session: AudioCodesSession?, terminationInfo: TerminationInfo?) {
        handler.post {
            Log.d(TAG, "Checking for termination")
            stopRingtone()
            stopRingingTimer()
            if (session != null) {
                Toast.makeText(this@IncomingCallActivity, "Call terminated: " + session.callState, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun startRingingTimer() {
        LogUtils.d(TAG, "Start ringing timer")
        if (timerThread == null) {
            timerThread = Thread(Runnable {
                val ringingTimeOutMiliSec = INCOMING_CALL_RINGING_TIMEOUT_SEC * 1000
                LogUtils.d(TAG, "Start ringing timout timer for: $ringingTimeOutMiliSec")
                try {
                    Thread.sleep(ringingTimeOutMiliSec.toLong())
                } catch (e: InterruptedException) {
                    LogUtils.d(TAG, "startRingingTimer interapted")
                    return@Runnable
                }
                session?.reject(null)
                handleCallTermination(null, null)
                //decline();
            })
            timerThread?.start()
        }
    }

    private fun stopRingingTimer() {
        timerThread?.interrupt()
        timerThread = null
    }

    companion object {
        private const val TAG = "IncomingCallActivity"
        const val SESSION_ID = "sessionID"
        const val INCOMING_CALL_RINGING_TIMEOUT_SEC = 30
        const val INTENT_ANSWER_TAG = "ANSWER_TAG"
        private var timerThread: Thread? = null
    }
}