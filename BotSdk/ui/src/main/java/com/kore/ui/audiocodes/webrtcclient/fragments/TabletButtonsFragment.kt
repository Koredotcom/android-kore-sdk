package com.kore.ui.audiocodes.webrtcclient.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.SettingsActivity
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.onTabChange
import com.kore.ui.audiocodes.webrtcclient.login.LogoutManager
import com.kore.ui.databinding.MainFragmentNavigationButtonsBinding

class TabletButtonsFragment : BaseFragment(), FragmentLifecycle {
    private var binding: MainFragmentNavigationButtonsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment_navigation_buttons, container, false)
        initGui()
        return binding?.root
    }

    private fun initGui() {
        binding?.apply {
            tabletButtonsDialerImageview.setOnClickListener { onTabChange(0) }
            tabletButtonsRecentsImageview.setOnClickListener { onTabChange(1) }
            tabletButtonsContactsImageview.setOnClickListener { onTabChange(2) }
            tabletButtonsChatImageview.setOnClickListener { onTabChange(3) }
            tabletButtonsSettingsImageview.setOnClickListener {
                val intent = Intent(this@TabletButtonsFragment.context, SettingsActivity::class.java)
                startActivity(intent)
            }
            tabletButtonsExitImageview.setOnClickListener { LogoutManager.closeApplication(requireContext(), acManager) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onPauseFragment() {}
    override fun onResumeFragment() {}
}
