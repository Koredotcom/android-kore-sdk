package com.kore.ui.audiocodes.webrtcclient.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.audiocodes.mv.webrtcsdk.useragent.AudioCodesUA
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.oauth.OAuthManager
import com.kore.ui.audiocodes.oauth.OAuthManager.EventsCallback
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.NotificationUtils
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.audiocodes.webrtcclient.login.LoginManager
import com.kore.ui.botchat.BotChatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseAppCompatActivity() {
    private val oAuthManager: OAuthManager = OAuthManager.getInstance()
    private val acManager: ACManager = ACManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        hasToolbar = false
        super.onCreate(savedInstanceState)
        appStateCheck()
        setContentView(R.layout.splash_activity)
        initStartApp();
    }

    private fun appStateCheck() {
        NotificationUtils.removeAllNotifications(this)
        if (LoginManager.getAppState(this) == LoginManager.AppLoginState.ACTIVE) {
            //skip splash screen
            startApp()
        }
    }

    private fun initStartApp() {
        isPermissionRequestActive = false
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        if (LoginManager.getAppState(this) == LoginManager.AppLoginState.CLOSED) {
            //for full login
            Handler(Looper.getMainLooper()).postDelayed({ startApp() }, 500)
        } else {
            //for quick login
            startApp()
        }
    }

    private fun startApp() {
        oAuthManager.initialize(applicationContext, object : EventsCallback {
            override fun onReLogin() {
                LogUtils.d(TAG, "reLogin!")
                Prefs.setIsFirstLogin(this@SplashActivity, true)
                val intent = Intent(applicationContext, BotChatActivity::class.java)
                intent.putExtra(LoginActivity.OAUTH_FAILED, true)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            override fun onUpdateToken(token: String?) {
                AudioCodesUA.getInstance().setOauthToken(token)
            }
        })
        LogUtils.d(TAG, "startApp")
        //firstLogin
        //Prefs.setFirstLogin(true);
        val isFirstLogin = Prefs.getIsFirstLogin(this)
        LogUtils.d(TAG, "isFirstLogin: $isFirstLogin")
        Prefs.setUsePush(this, true)
        //check if all permission are granted
        if (allPermissionsGranted) {
            if (isFirstLogin) {
                startNextActivity(BotChatActivity::class.java)
                finish()
                return
            }
            //start login and open app main screen
            acManager.startLogin(this)
            startNextActivity(BotChatActivity::class.java)
            finish()
        } else {
            startNextActivity(PermissionDeniedActivity::class.java)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d(TAG, "onStop")
        if (isPermissionRequestActive) finish()
    }

    companion object {
        private const val TAG = "SplashActivity"
        private var isPermissionRequestActive = false
        private var allPermissionsGranted = true
    }
}