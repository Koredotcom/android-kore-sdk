package kore.botssdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import kore.botssdk.R;

/**
 * Created by Pradeep Mahato on 31-May-16.
 */
public class BotHomeActivity extends BaseSpiceActivity {

    Button launchBotBtn;
    Button logOutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_home_activity_layout);

        findViews();
        setListeners();
    }

    private void findViews() {
        launchBotBtn = (Button) findViewById(R.id.launchBotBtn);
        logOutBtn = (Button) findViewById(R.id.logOutBtn);
    }

    private void setListeners() {
        launchBotBtn.setOnClickListener(launchBotBtnOnClickListener);
    }

    /**
     * START of : Listeners
     */

    View.OnClickListener launchBotBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
            startActivity(intent);
        }
    };

    /**
     * END of : Listeners
     */
}
