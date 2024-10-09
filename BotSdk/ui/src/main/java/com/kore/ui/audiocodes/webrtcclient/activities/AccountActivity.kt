package com.kore.ui.audiocodes.webrtcclient.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.oauth.OAuthManager
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.audiocodes.webrtcclient.general.Prefs

class AccountActivity : BaseAppCompatActivity() {
    private var oAuthEnable = false
    private var secondCall: EditText? = null
    private var transferCall: EditText? = null
    private val oAuthManager: OAuthManager = OAuthManager.getInstance()
    private val acManager: ACManager = ACManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_activity)
        initGui()
    }

    private fun initGui() {
        val sipAccount = Prefs.getSipAccount(this)
        val userNameEditText = findViewById<View>(R.id.account_editText_username) as EditText
        val passwordEditText = findViewById<View>(R.id.account_editText_password) as EditText
        val displayNameEditText = findViewById<View>(R.id.account_editText_displayname) as EditText
        val domainEditText = findViewById<View>(R.id.account_editText_domain) as EditText
        val sipAddressEditText = findViewById<View>(R.id.account_editText_sipaddress) as EditText
        val portEditText = findViewById<View>(R.id.account_editText_port) as EditText
        val transportSpinner = findViewById<View>(R.id.account_editText_transport) as Spinner
        transportSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Transport.values())
        val loginButton = findViewById<View>(R.id.account_button_login) as Button
        secondCall = findViewById<View>(R.id.account_editText_second_call) as EditText
        transferCall = findViewById<View>(R.id.account_editText_transfer_call) as EditText
        secondCall?.setText(Prefs.getSecondCall(this))
        transferCall?.setText(Prefs.getTransferCall(this))
        val statusTextView = findViewById<View>(R.id.account_textview_status) as TextView
        var statusStr = getString(R.string.account_textview_status_prefix) + " " +
                if (acManager.isRegisterState) getString(R.string.account_textview_status_connected) else getString(R.string.account_textview_status_disconnected)
        if (sipAccount.username != null) {
            try {
                statusStr += ", " + sipAccount.username
            } catch (e: Exception) {
                LogUtils.e(TAG, "err: $e")
            }
        }
        statusTextView.text = statusStr
        userNameEditText.setText(sipAccount.username)
        passwordEditText.setText(sipAccount.password)
        displayNameEditText.setText(sipAccount.displayName)
        domainEditText.setText(sipAccount.domain)
        sipAddressEditText.setText(sipAccount.proxy)
        for (i in 0 until transportSpinner.adapter.count) {
            if (transportSpinner.getItemAtPosition(i).toString().equals(sipAccount.transport.toString(), ignoreCase = true)) {
                transportSpinner.setSelection(i)
            }
        }

        portEditText.setText(sipAccount.port.toString())
        val oauthUrlEditText = findViewById<View>(R.id.login_editText_oauth_url) as EditText
        val oauthRealmEditText = findViewById<View>(R.id.login_editText_oauth_realm) as EditText
        val oauthClientIdEditText = findViewById<View>(R.id.login_editText_oauth_clientid) as EditText
        if (oAuthManager.isEnabled == true) {
            oAuthEnable = true
            val layout = findViewById<View>(R.id.oauth_layout) as LinearLayout
            layout.visibility = View.VISIBLE
            oauthUrlEditText.setText(oAuthManager.getURL())
            oauthRealmEditText.setText(oAuthManager.getRealm())
            oauthClientIdEditText.setText(oAuthManager.getClientId())
            passwordEditText.setText("")
        }
        val autologin = findViewById<View>(R.id.autologin_checkBox) as CheckBox
        autologin.isChecked = Prefs.getAutoLogin(this)
        loginButton.setOnClickListener {
            val userName = userNameEditText.text.toString().trim { it <= ' ' }
            val password = passwordEditText.text.toString().trim { it <= ' ' }
            if (validateField(userNameEditText) && validateField(passwordEditText)) {
                sipAccount.username = userName
                if (!oAuthEnable) {
                    sipAccount.password = password
                } else {
                    sipAccount.password = "no password"
                }
                if (validateField(displayNameEditText) && validateField(domainEditText) && validateField(sipAddressEditText) && validateField(
                        portEditText
                    )
                ) {
                    sipAccount.displayName = displayNameEditText.text.toString().trim { it <= ' ' }
                    sipAccount.domain = domainEditText.text.toString().trim { it <= ' ' }
                    sipAccount.proxy = sipAddressEditText.text.toString().trim { it <= ' ' }
                    sipAccount.transport = AppUtils.getTransport(this, transportSpinner.selectedItem.toString().trim { it <= ' ' })
                    val portNumber: Int = try {
                        Integer.valueOf(portEditText.text.toString().trim { it <= ' ' })
                    } catch (e: Exception) {
                        //use default port number;
                        Integer.valueOf(getString(R.string.sip_account_port_default))
                    }
                    sipAccount.port = portNumber
                }
            }
            Log.d(TAG, "start logout")
            Prefs.setSipAccount(this, sipAccount)
            handleOauth(
                oauthUrlEditText, oauthRealmEditText, oauthClientIdEditText,
                userName, password, autologin.isChecked
            )
        }
    }

    private fun handleOauth(
        oauthUrlEditText: EditText, oauthRealmEditText: EditText, oauthClientIdEditText: EditText, userName: String?, password: String?,
        autologin: Boolean
    ) {
        val disconnectCall = Prefs.getDisconnectBrokenConnection(this)
        if (oAuthEnable) {
            oAuthManager.setURL(oauthUrlEditText.text.toString())
            oAuthManager.setRealm(oauthRealmEditText.text.toString())
            oAuthManager.setClientId(oauthClientIdEditText.text.toString())
            oAuthManager.authorize(this, userName, password, object : OAuthManager.LoginCallback {
                override fun onAuthorize(success: Boolean) {
                    if (success) {
                        openNextScreen(autologin, disconnectCall)
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.oauth_error), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            openNextScreen(autologin, disconnectCall)
        }
    }

    private fun validateField(editText: EditText?): Boolean {
        return editText != null && editText.text != null && editText.text.toString().trim { it <= ' ' } != ""
    }

    internal fun openNextScreen(autologin: Boolean, disconnectCall: Boolean) {
        Prefs.setSecondCall(this, secondCall?.text.toString())
        Prefs.setTransferCall(this, transferCall?.text.toString())
        Prefs.setIsFirstLogin(this, false)
        //start login and open app main screen
        acManager.startLogin(this, autologin, disconnectCall)
        startNextActivity(MainActivity::class.java)
        finish()
    }

    companion object {
        private const val TAG = "AccountActivity"
    }
}