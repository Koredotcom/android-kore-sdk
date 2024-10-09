package com.kore.ui.audiocodes.webrtcclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.LoginStateChanged
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.registerLginStateChange
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.unregisterLoginStateChange
import com.kore.ui.audiocodes.webrtcclient.general.Prefs
import com.kore.ui.databinding.MainFragmentTabletInfoBinding

class TabletInfoFragment : BaseFragment(), FragmentLifecycle {
    private var binding: MainFragmentTabletInfoBinding? = null
    private val loginStateChanged = LoginStateChanged { handler.post { updateUi() } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment_tablet_info, container, false)
        registerLginStateChange(loginStateChanged)
        updateUi()
        return binding?.root
    }

    private fun updateUi() {
        val statusStr =
            if (acManager.isRegisterState) getString(R.string.account_textview_status_connected) else getString(R.string.account_textview_status_disconnected)
        binding?.apply {
            tabletInfoStateValueTextview.text = statusStr
            val sipAccount = Prefs.getSipAccount(requireContext())
            tabletInfoDisplayNameValueTextview.text = sipAccount.displayName
            tabletInfoUserNameValueTextview.text = sipAccount.username
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        unregisterLoginStateChange(loginStateChanged)
    }

    override fun onPauseFragment() {}
    override fun onResumeFragment() {}
}