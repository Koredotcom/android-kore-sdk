package kore.botssdk.audiocodes.webrtcclient.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.Activities.SettingsActivity;
import kore.botssdk.audiocodes.webrtcclient.Login.LogoutManager;

public class BaseFragment extends Fragment {
    public Handler handler;

    private final String TAG = "BaseFragment";

    private static final int SETTING_MENU = 100;
    private static final int EXIT_MENU = 101;

    //public List<OptionsMenuObject> optionsMenuObjectList = new ArrayList<OptionsMenuObject>();

    public void onCreate(@Nullable Bundle savedInstanceState) {

        //optionsMenuObjectList = new ArrayList<OptionsMenuObject>();
        super.onCreate(savedInstanceState);
        setMenuVisibility(true);
        setHasOptionsMenu(true);
        handler = new Handler();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Clear current contents
        menu.clear();
        menu.add(Menu.NONE, SETTING_MENU, Menu.NONE, getString(R.string.menu_option_settings));
        menu.add(Menu.NONE, EXIT_MENU, Menu.NONE, getString(R.string.menu_option_exit));
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SETTING_MENU:
                Intent intent = new Intent(BaseFragment.this.getContext(), SettingsActivity.class);
                startActivity(intent);
                //finish();
                return true;
            case EXIT_MENU:
                LogoutManager.closeApplication();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
