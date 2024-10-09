package com.kore.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.kore.common.SDKConfiguration
import com.kore.common.utils.LogUtils
import com.kore.constants.SharedPrefConstants.KEY_HEADER_TYPE
import com.kore.constants.SharedPrefConstants.PREF_MAIN
import com.kore.data.repository.branding.BrandingRepository
import com.kore.data.repository.branding.BrandingRepositoryImpl
import com.kore.data.repository.preference.PreferenceRepository
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.ui.R
import com.kore.ui.botchat.BotChatActivity
import com.kore.ui.databinding.WelcomeScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeScreenActivity : AppCompatActivity() {
    companion object {
        private val LOG_TAG = WelcomeScreenActivity::class.java.simpleName
    }

    private lateinit var binding: WelcomeScreenBinding
    private lateinit var navController: NavController
    private val brandingRepository: BrandingRepository = BrandingRepositoryImpl()
    private val prefRepository: PreferenceRepository = PreferenceRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvStarterTitle.setText(R.string.app_name)
        applyFragment()
        Handler(Looper.getMainLooper()).postDelayed({
            prefRepository.putIntValue(this, PREF_MAIN, KEY_HEADER_TYPE, 2)
            applyFragment()
        }, 2000)

        binding.llStartConversation.setOnClickListener {
            intent = Intent(applicationContext, BotChatActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        })

        val botConfigModel = SDKConfiguration.getBotConfigModel()
        MainScope().launch {
            withContext(Dispatchers.IO) {
                try {
                    if (botConfigModel != null) {
                        when (val result = brandingRepository.getBranding(botConfigModel.botId, "")) {
                            is com.kore.common.Result.Success -> LogUtils.e(LOG_TAG, "getJwtGrant error: ${result.data.toString()}")
                            is com.kore.common.Result.Error -> LogUtils.e(LOG_TAG, "getJwtGrant error: ${result.exception.message}")
                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    LogUtils.e(LOG_TAG, "Something went wrong. error: ${e.message}")
                }
            }
        }
    }

    private fun applyFragment() {
        navController = Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
        val key = prefRepository.getIntValue(this, PREF_MAIN, KEY_HEADER_TYPE, 1)

        navController.popBackStack()
        when (key) {
            1 -> navController.navigate(R.id.welcomeHeaderOne)
            2 -> navController.navigate(R.id.welcomeHeaderTwo)
            3 -> navController.navigate(R.id.welcomeHeaderThree)
        }
    }
}