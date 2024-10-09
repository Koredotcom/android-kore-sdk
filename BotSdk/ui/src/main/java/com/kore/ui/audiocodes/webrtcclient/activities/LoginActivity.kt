package com.kore.ui.audiocodes.webrtcclient.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.audiocodes.mv.webrtcsdk.sip.enums.Transport
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.oauth.OAuthManager
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.audiocodes.webrtcclient.structure.SipAccount
import com.kore.ui.databinding.LoginActivityBinding
import java.util.Locale

class LoginActivity : BaseAppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private var oAuthEnable = false
    private val acManager: ACManager = ACManager.getInstance()
    private val oAuthManager: OAuthManager = OAuthManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.login_activity, null, false)
        setContentView(binding.root)
        initGui()
    }

    private fun initGui() {
        binding.loginTextviewOauthFailed.isVisible = intent.getBooleanExtra(OAUTH_FAILED, false)
        val sipAccount = SipAccount(this)
        binding.loginEditTextTransport.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Transport.values())
        binding.autologinCheckBox.isChecked = true
        binding.callDisconnectCheckBox.isChecked = true
        binding.pushCheckBox.isChecked = Prefs.usePush(this)
        binding.pushCheckBox.setOnCheckedChangeListener { _, isChecked -> Prefs.setUsePush(this, isChecked) }
        binding.oauthCheckBox.setOnCheckedChangeListener { _, isChecked ->
            oAuthEnable = isChecked
            binding.oauthLayout.isVisible = oAuthEnable
        }
        binding.videoHardwareCheckbox.isChecked = Prefs.getIsVideoHardware(this)
        binding.videoHardwareCheckbox.setOnCheckedChangeListener { _, isChecked -> Prefs.setIsVideoHardware(this, isChecked) }
        if (oAuthManager.isEnabled) {
            LogUtils.d("BBB", "getting in here")
            binding.oauthCheckBox.visibility = View.GONE
            oAuthEnable = true
            binding.oauthLayout.isVisible = true
        }
        val debugMode = AppUtils.getStringBoolean(this, R.string.enable_debug_mode)
        if (debugMode) {
            binding.loginEditTextUsername.setText(sipAccount.username)
            binding.loginEditTextPassword.setText(sipAccount.password)
            binding.loginEditTextDisplayname.setText(sipAccount.displayName)
            binding.loginEditTextDomain.setText(sipAccount.domain)
            binding.loginEditTextSipaddress.setText(sipAccount.proxy)
            for (i in 0 until binding.loginEditTextTransport.adapter.count) {
                if (binding.loginEditTextTransport.getItemAtPosition(i).toString()
                        .lowercase(Locale.getDefault()) == sipAccount.transport.toString().lowercase(Locale.getDefault())
                ) {
                    binding.loginEditTextTransport.setSelection(i)
                }
            }

            binding.loginEditTextPort.setText(sipAccount.port.toString())
            binding.oauthCheckBox.isChecked = AppUtils.getStringBoolean(this, R.string.oauth_enable)
            binding.loginEditTextOauthUrl.setText(getString(R.string.oauth_url))
            binding.loginEditTextOauthRealm.setText(getString(R.string.oauth_realm))
            binding.loginEditTextOauthClientid.setText(getString(R.string.oauth_client_id))
        }
        binding.loginButtonLogin.setOnClickListener {
            if (validateField(binding.loginEditTextUsername) && validateField(binding.loginEditTextPassword)) {
                val userName = binding.loginEditTextUsername.text.toString().trim { it <= ' ' }
                val password = binding.loginEditTextPassword.text.toString().trim { it <= ' ' }
                sipAccount.username = userName
                sipAccount.password = if (!oAuthEnable) password else "no password"
                if (validateField(binding.loginEditTextDisplayname) && validateField(binding.loginEditTextDomain) &&
                    validateField(binding.loginEditTextSipaddress) && validateField(binding.loginEditTextPort)
                ) {
                    sipAccount.displayName = binding.loginEditTextDisplayname.text.toString().trim { it <= ' ' }
                    sipAccount.domain = binding.loginEditTextDomain.text.toString().trim { it <= ' ' }
                    sipAccount.proxy = binding.loginEditTextSipaddress.text.toString().trim { it <= ' ' }
                    sipAccount.transport = AppUtils.getTransport(this, binding.loginEditTextTransport.selectedItem.toString().trim { it <= ' ' })
                    val portNumber: Int = try {
                        Integer.valueOf(binding.loginEditTextPort.text.toString().trim { it <= ' ' })
                    } catch (e: Exception) {
                        //use default port number;
                        Integer.valueOf(getString(R.string.sip_account_port_default))
                    }
                    sipAccount.port = portNumber
                }
                Prefs.setSipAccount(this, sipAccount)
                if (oAuthEnable) {
                    oAuthManager.setURL(binding.loginEditTextOauthUrl.text.toString())
                    oAuthManager.setRealm(binding.loginEditTextOauthRealm.text.toString())
                    oAuthManager.setClientId(binding.loginEditTextOauthClientid.text.toString())
                    oAuthManager.authorize(this, userName, password, object : OAuthManager.LoginCallback {
                        override fun onAuthorize(success: Boolean) {
                            if (success) {
                                openNextScreen(binding.autologinCheckBox.isChecked, binding.callDisconnectCheckBox.isChecked)
                            } else {
                                Toast.makeText(this@LoginActivity.applicationContext, getString(R.string.oauth_error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    openNextScreen(binding.autologinCheckBox.isChecked, binding.callDisconnectCheckBox.isChecked)
                }
            }
            Prefs.setSipAccount(this, sipAccount)
            if (!oAuthEnable) {
                openNextScreen(binding.autologinCheckBox.isChecked, binding.callDisconnectCheckBox.isChecked)
            }
        }
        val predefinedConfigMode = AppUtils.getStringBoolean(this, R.string.predefined_config_mode)
        if (predefinedConfigMode) {
            binding.loginEditTextOauthUrl.isEnabled = false
            binding.loginEditTextPassword.isEnabled = false
            binding.loginEditTextDisplayname.isEnabled = false
            binding.loginEditTextDomain.isEnabled = false
            binding.loginEditTextSipaddress.isEnabled = false
            binding.loginEditTextPort.isEnabled = false
            binding.loginEditTextTransport.isEnabled = false

            //use click to call
            binding.autologinCheckBox.isChecked = false
            binding.autologinCheckBox.isEnabled = false
            binding.loginButtonLogin.setText(R.string.login_button_start_text)
        }
    }

    private fun validateField(editText: EditText?): Boolean {
        return editText != null && editText.text != null && editText.text.toString().trim { it <= ' ' } != ""
    }

    internal fun openNextScreen(autologin: Boolean, disconnectCall: Boolean) {
        Prefs.setIsFirstLogin(this, false)
        //start login and open app main screen
        acManager.startLogin(this, autologin, disconnectCall)
        startNextActivity(MainActivity::class.java)
    }

    companion object {
        const val OAUTH_FAILED = "OAUTH_FAILED"
    }
}