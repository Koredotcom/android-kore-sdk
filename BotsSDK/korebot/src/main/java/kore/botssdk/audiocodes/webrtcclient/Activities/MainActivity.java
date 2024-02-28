package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.Adapters.ViewPagerAdapter;
import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.Fragments.ChatFragment;
import kore.botssdk.audiocodes.webrtcclient.Fragments.ContactListFragment;
import kore.botssdk.audiocodes.webrtcclient.Fragments.DialerFragment;
import kore.botssdk.audiocodes.webrtcclient.Fragments.FragmentLifecycle;
import kore.botssdk.audiocodes.webrtcclient.Fragments.RecentListFragment;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.audiocodes.webrtcclient.Login.LoginManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManager;
import kore.botssdk.audiocodes.webrtcclient.Permissions.PermissionManagerType;


public class MainActivity extends BaseAppCompatActivity {


    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    private static final String TAG = "MainActivity";

    CallBackHandler.LoginStateChanged loginStateChanged = new CallBackHandler.LoginStateChanged() {
        @Override
        public void loginStateChange(boolean state) {
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BotApplication.getGlobalContext(), "login state: "+state, Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"login state: "+state);
                    }
                });
            }
        }
    };

    CallBackHandler.TabChangeCallback tabChangeCallback = new CallBackHandler.TabChangeCallback() {
        @Override
        public void onTabChange(int tabIndex) {
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                      //  adapter.setPrimaryItem(tabIndex);
                        viewPager.setCurrentItem(tabIndex, true);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int displaymode = AppUtils.checkOrientation(this);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_activity);

        initRotationMode(displaymode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int displaymode = AppUtils.checkOrientation(this);
        super.onConfigurationChanged(newConfig);
        initRotationMode(displaymode);
    }

    //
    private void initRotationMode(int displaymode)
    {
        Log.d(TAG, "get orientation: "+displaymode);
        if (displaymode == 1) { // its portrait mode
            setContentView(R.layout.main_activity);
            LoginManager.setAppState(LoginManager.AppLoginState.ACTIVE);
            BotApplication.initDataBase();
            initTabs();
        } else {// its landscape
            setContentView(R.layout.main_fragments);
            LoginManager.setAppState(LoginManager.AppLoginState.ACTIVE);
            BotApplication.initDataBase();
            initFragments();
        }


        //initRotationMode();
        CallBackHandler.registerLginStateChange(loginStateChanged);
    }

    private void initFragments()
    {

        initTabs();
    }

    private void initTabs()
    {
       // public static void unregisterTabChangeCallback(CallBackHandler.TabChangeCallback
      //  tabChangeCallback) {
        CallBackHandler.registerTabChangeCallback(tabChangeCallback);

        viewPager = (ViewPager)findViewById(R.id.main_activity_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DialerFragment(), getString(R.string.tabs_dialer_title));
        adapter.addFragment(new RecentListFragment(), getString(R.string.tabs_recents_title));
        boolean contactPermission = PermissionManager.getInstance().checkPermission(PermissionManagerType.CONTACTS);
        if (contactPermission) {
            adapter.addFragment(new ContactListFragment(), getString(R.string.tabs_contacts_title));
        }
        adapter.addFragment(new ChatFragment(), getString(R.string.tabs_chat_title));
        // adapter.addFragment(new AddEntryFargment(), getString(R.string.tabs_settings_title));

        viewPager.setAdapter(adapter);

        //For refreshing data when returning to fragment
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                FragmentLifecycle fragmentToHide = (FragmentLifecycle) adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(position);
                fragmentToShow.onResumeFragment();

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    protected void onDestroy() {
        CallBackHandler.unregisterLoginStateChange(loginStateChanged);
        CallBackHandler.unregisterTabChangeCallback(tabChangeCallback);
        super.onDestroy();
    }


}
