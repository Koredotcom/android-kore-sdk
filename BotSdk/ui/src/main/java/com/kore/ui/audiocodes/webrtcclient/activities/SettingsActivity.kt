package com.kore.ui.audiocodes.webrtcclient.activities

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.AccountActivity
import com.kore.ui.audiocodes.webrtcclient.activities.CallStatsActivity
import com.kore.ui.audiocodes.webrtcclient.activities.GeneralSettingsActivity
import com.kore.ui.databinding.SettingsActivityBinding

class SettingsActivity : BaseAppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.settings_activity, null, false)
        setContentView(binding.root)
        initGui()
    }

    private fun initGui() {
        binding.settingsButtonGeneral.setOnClickListener { startNextActivity(GeneralSettingsActivity::class.java) }
        binding.settingsButtonAccount.setOnClickListener { startNextActivity(AccountActivity::class.java) }
        binding.settingsButtonCallStats.setOnClickListener { startNextActivity(CallStatsActivity::class.java) }
    }
}