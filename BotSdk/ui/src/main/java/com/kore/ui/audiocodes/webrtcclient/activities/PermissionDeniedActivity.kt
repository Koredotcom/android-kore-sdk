package com.kore.ui.audiocodes.webrtcclient.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManager
import com.kore.ui.databinding.PermissionDeniedActivityBinding

class PermissionDeniedActivity : BaseAppCompatActivity() {
    private lateinit var binding: PermissionDeniedActivityBinding
    private val acManager: ACManager = ACManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.permission_denied_activity, null, false)
        setContentView(binding.root)
        initGui()
    }

    private fun initGui() {
        binding.permissionDeniedButtonContinue.setOnClickListener {
            if (Prefs.getIsFirstLogin(this)) {
                startNextActivity(LoginActivity::class.java)
                finish()
            } else {
                //start login and open app main screen
                acManager.startLogin(this)
                startNextActivity(MainActivity::class.java)
                finish()
            }
        }
        binding.permissionDeniedButtonEdit.setOnClickListener {
            PermissionManager.askForSystemAlertWindowPermission(this)
            finish()
        }
    }
}