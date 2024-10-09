package com.kore.ui.audiocodes.webrtcclient.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.audiocodes.mv.webrtcsdk.session.DTMFOptions.DTMFMethod
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.general.Log
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.databinding.GeneralSettingsActivityBinding

class GeneralSettingsActivity : BaseAppCompatActivity() {
    private lateinit var binding: GeneralSettingsActivityBinding
    private val acManager: ACManager = ACManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.general_settings_activity, null, false)
        setContentView(binding.root)
        initGui()
    }

    private fun initGui() {
        binding.generalSettingsCheckboxRedirectCall.setOnCheckedChangeListener { _, isChecked ->
            binding.generalSettingsEdittextRedirectCall.isVisible = isChecked
        }
        val saveButton = findViewById<View>(R.id.general_settings_button_save) as Button
        binding.generalSettingsSpinnerDtmfType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, DTMFMethod.values())
        binding.generalSettingsSpinnerLogLevelType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Log.LogLevel.values())
        try {
            val dtmfMethod = Prefs.getDTMFType(this)
            val currentDTMFType = dtmfMethod.toString()
            LogUtils.d(TAG, "dtmfMethod: $dtmfMethod currentDTMFType: $currentDTMFType")
            for (i in 0 until binding.generalSettingsSpinnerDtmfType.adapter.count) {
                if (binding.generalSettingsSpinnerDtmfType.getItemAtPosition(i).toString().equals(currentDTMFType, ignoreCase = true)) {
                    binding.generalSettingsSpinnerDtmfType.setSelection(i)
                }
            }
            val logLevel = Prefs.getLogLevel(this)
            val currentLogLevel = logLevel.toString()
            LogUtils.d(TAG, "logLevel: $logLevel currentLogLevel: $currentLogLevel")
            for (i in 0 until binding.generalSettingsSpinnerLogLevelType.adapter.count) {
                if (binding.generalSettingsSpinnerLogLevelType.getItemAtPosition(i).toString().equals(currentLogLevel, ignoreCase = true)) {
                    binding.generalSettingsSpinnerLogLevelType.setSelection(i)
                }
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "error: $e")
        }
        binding.generalSettingsCheckboxAutoRedirect.isChecked = Prefs.getIsAutoRedirect(this)
        binding.generalSettingsCheckboxRedirectCall.isChecked = Prefs.getIsRedirectCall(this)
        binding.generalSettingsEdittextRedirectCall.setText(Prefs.getRedirectCallUser(this))
        saveButton.setOnClickListener {
            try {
                val saveDTMFMethod: DTMFMethod = DTMFMethod.values()[binding.generalSettingsSpinnerDtmfType.selectedItemPosition]
                Prefs.setDTMFType(this, saveDTMFMethod)
                LogUtils.d(TAG, "saveDTMFMethod: $saveDTMFMethod")
                val saveLogLevel: Log.LogLevel = Log.LogLevel.values()[binding.generalSettingsSpinnerLogLevelType.selectedItemPosition]
                Prefs.setLogLevel(this, saveLogLevel)
                Log.setLogLevel(saveLogLevel)
                LogUtils.d(TAG, "saveLogLevel: $saveLogLevel")
                Prefs.setIsAutoRedirect(this, binding.generalSettingsCheckboxAutoRedirect.isChecked)
                Prefs.setRedirectCallUser(this, binding.generalSettingsEdittextRedirectCall.text.toString())
                acManager.updateWebRTCConfig(this)
            } catch (e: Exception) {
                LogUtils.e(TAG, "error: $e")
            }
            finish()
        }
    }

    companion object {
        private const val TAG = "GeneralSettingsActivity"
    }
}