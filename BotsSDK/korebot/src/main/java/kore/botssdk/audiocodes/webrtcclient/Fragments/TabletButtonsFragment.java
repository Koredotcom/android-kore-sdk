package kore.botssdk.audiocodes.webrtcclient.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.Activities.SettingsActivity;
import kore.botssdk.audiocodes.webrtcclient.Callbacks.CallBackHandler;
import kore.botssdk.audiocodes.webrtcclient.Login.LogoutManager;

public class TabletButtonsFragment extends BaseFragment implements FragmentLifecycle {

    private static final String TAG = "ChatFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment_navigation_buttons, container, false);
        initGui(rootView);
        return rootView;
    }

    private void initGui(View rootView)
    {
        ImageView dialerButton = (ImageView) rootView.findViewById(R.id.tablet_buttons_dialer_imageview);
        ImageView recentsButton = (ImageView) rootView.findViewById(R.id.tablet_buttons_recents_imageview);
        ImageView contactsButton = (ImageView) rootView.findViewById(R.id.tablet_buttons_contacts_imageview);
        ImageView chatButton = (ImageView) rootView.findViewById(R.id.tablet_buttons_chat_imageview);
        ImageView settingsButton = (ImageView) rootView.findViewById(R.id.tablet_buttons_settings_imageview);
        ImageView exitButton = (ImageView) rootView.findViewById(R.id.tablet_buttons_exit_imageview);


        dialerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallBackHandler.onTabChange(0);
            }
        });
        recentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallBackHandler.onTabChange(1);
            }
        });
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallBackHandler.onTabChange(2);
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallBackHandler.onTabChange(3);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabletButtonsFragment.this.getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutManager.closeApplication();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }


}
