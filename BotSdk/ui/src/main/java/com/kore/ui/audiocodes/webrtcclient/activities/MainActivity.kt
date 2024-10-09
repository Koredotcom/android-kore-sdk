package com.kore.ui.audiocodes.webrtcclient.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.kore.common.utils.LogUtils
import com.kore.ui.R
import com.kore.ui.audiocodes.webrtcclient.adapters.ViewPagerAdapter
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.LoginStateChanged
import com.kore.ui.audiocodes.webrtcclient.callbacks.CallBackHandler.TabChangeCallback
import com.kore.ui.audiocodes.webrtcclient.fragments.ChatFragment
import com.kore.ui.audiocodes.webrtcclient.fragments.DialerFragment
import com.kore.ui.audiocodes.webrtcclient.fragments.FragmentLifecycle
import com.kore.ui.audiocodes.webrtcclient.fragments.contactlist.ContactListFragment
import com.kore.ui.audiocodes.webrtcclient.fragments.recents.RecentListFragment
import com.kore.ui.audiocodes.webrtcclient.general.AppUtils
import com.kore.ui.audiocodes.webrtcclient.login.LoginManager
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManager
import com.kore.ui.audiocodes.webrtcclient.permissions.PermissionManagerType

class MainActivity : BaseAppCompatActivity() {
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager
    private var loginStateChanged = LoginStateChanged { state ->
        handler.post {
            Toast.makeText(applicationContext(), "login state: $state", Toast.LENGTH_SHORT).show()
            LogUtils.d(TAG, "login state: $state")
        }
    }
    private var tabChangeCallback = TabChangeCallback { tabIndex ->
        handler.post { viewPager.setCurrentItem(tabIndex, true) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val displayMode = AppUtils.checkOrientation(this)
        super.onCreate(savedInstanceState)
        initRotationMode(displayMode)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val displayMode = AppUtils.checkOrientation(this)
        super.onConfigurationChanged(newConfig)
        initRotationMode(displayMode)
    }

    //
    private fun initRotationMode(displayMode: Int) {
        LogUtils.d(TAG, "get orientation: $displayMode")
        LoginManager.setAppState(this, LoginManager.AppLoginState.ACTIVE)
        initDataBase()
        if (displayMode == 1) { // its portrait mode
            setContentView(R.layout.main_activity)
            initTabs()
        } else { // its landscape
            setContentView(R.layout.main_fragments)
            initFragments()
        }

        CallBackHandler.registerLginStateChange(loginStateChanged)
    }

    private fun initFragments() {
        initTabs()
    }

    private fun initTabs() {
        CallBackHandler.registerTabChangeCallback(tabChangeCallback)
        viewPager = findViewById<View>(R.id.main_activity_pager) as ViewPager
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DialerFragment(), getString(R.string.tabs_dialer_title))
        adapter.addFragment(RecentListFragment(), getString(R.string.tabs_recents_title))
        val contactPermission = PermissionManager.instance.checkPermission(this, PermissionManagerType.CONTACTS)
        if (contactPermission) {
            adapter.addFragment(ContactListFragment(), getString(R.string.tabs_contacts_title))
        }
        adapter.addFragment(ChatFragment(), getString(R.string.tabs_chat_title))
        viewPager.adapter = adapter

        //For refreshing data when returning to fragment
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            var currentPosition = 0
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                val fragmentToHide = adapter.getItem(currentPosition) as FragmentLifecycle
                fragmentToHide.onPauseFragment()
                val fragmentToShow = adapter.getItem(position) as FragmentLifecycle
                fragmentToShow.onResumeFragment()
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
//        tabLayout.setSelectedTabIndicator(R.drawable.main_tab_indicator)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        CallBackHandler.unregisterLoginStateChange(loginStateChanged)
        CallBackHandler.unregisterTabChangeCallback(tabChangeCallback)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}