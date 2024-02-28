package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.os.Bundle;
import android.view.View;

import kore.botssdk.R;

public class SettingsActivity extends BaseAppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_activity);

        initGui();
    }

    private void initGui() {

        int[] settingsButtonClickListID = {R.id.settings_button_general, R.id.settings_button_account, R.id.settings_button_call_stats};

        View.OnClickListener settingsClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View clickedView) {
                if (clickedView == null) {
                    return;
                }
                int id = clickedView.getId();
                if (id == R.id.settings_button_general) {
                    startNextActivity(GeneralSettingsActivity.class);
                } else if (id == R.id.settings_button_account) {
                    startNextActivity(AccountActivity.class);
                } else if (id == R.id.settings_button_call_stats) {
                    startNextActivity(CallStatsActivity.class);
                }

            }
        };

        for (int settingsButtonClickID : settingsButtonClickListID) {
            View view = findViewById(settingsButtonClickID);
            if (view != null) {
                view.setOnClickListener(settingsClickListener);
            }
        }
    }
//
//
//    private void openNextScreen(Class cls) {
//        //start login and open app main screen
//        //ACManager.getInstance().startLogin();
//        startNextActivity(cls);
//    }

}
