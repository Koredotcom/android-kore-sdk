package com.kore.ui.audiocodes.webrtcclient.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.activities.SettingsActivity
import com.kore.ui.audiocodes.webrtcclient.general.ACManager
import com.kore.ui.audiocodes.webrtcclient.login.LogoutManager

open class BaseFragment : Fragment() {
    val acManager: ACManager = ACManager.getInstance()
    var handler: Handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        val intent = Intent(this@BaseFragment.context, SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.action_exit -> {
                        LogoutManager.closeApplication(requireContext(), acManager)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    companion object {
        private const val SETTING_MENU = 100
        private const val EXIT_MENU = 101
    }
}