package kore.botssdk.audiocodes.webrtcclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import kore.botssdk.R;
import kore.botssdk.application.BotApplication;
import kore.botssdk.audiocodes.webrtcclient.General.AppUtils;
import kore.botssdk.audiocodes.webrtcclient.General.Log;
import kore.botssdk.net.SDKConfiguration;

public class BaseAppCompatActivity extends AppCompatActivity {

    public Handler handler;
    public boolean hasToolbar = true;
    private Toolbar toolbar;
    public View layoutView;
    private static final String TAG = "BaseAppCompatActivity";


    protected void onCreate(Bundle savedInstanceState) {
        AppUtils.checkOrientation(this);
        super.onCreate(savedInstanceState);
        BotApplication.setCurrentActivity(this);
        handler = new Handler();
//        NotificationUtils.createAppNotification();
    }

    public void startNextActivity(Class<?> cls) {
        Intent intent = new Intent(BaseAppCompatActivity.this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void setContentView(int layoutResID) {
        layoutView = View.inflate(this, layoutResID, null);
        setContentView(layoutView);
        //addToolbar();
    }

    @Override
    public void setContentView(View view) {
        layoutView = view;
        super.setContentView(view);
        addToolbar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        layoutView = view;
        super.setContentView(view, params);
        addToolbar();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (hasToolbar) {
            Log.d(TAG, "onPrepareOptionsMenu: " + menu.hasVisibleItems());
            Log.d(TAG, "onPrepareOptionsMenu: " + menu.size());
            adjustTitleBar(menu.size() > 0);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void addToolbar() {
        if (hasToolbar) {
            View tmptoolBar = LayoutInflater.from(this).inflate(R.layout.custom_toolbar, (ViewGroup) layoutView, false);
            ((ViewGroup) layoutView).addView(tmptoolBar, 0);
            toolbar = tmptoolBar.findViewById(R.id.custom_toolbar_id);
            toolbar.setTitle(SDKConfiguration.Client.bot_name);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
        }
    }


    public void adjustTitleBar(boolean hasMenu) {
        Log.d(TAG, "adjustTitleBar, hasMenu: " + hasMenu);

        // menu button
        int layoutMarginLeft = 0;//value in dp - this is the size of the menu icon

        Log.d(TAG, "toolBar.getPaddingLeft(); " + toolbar.getPaddingLeft());

        if (hasMenu) {
            int paddingLeft = toolbar.getPaddingLeft();
            if (paddingLeft != 12) {
                layoutMarginLeft = 40;//value in dp - this is the size of the menu icon
            } else {
                layoutMarginLeft = 36;
            }
        }
        Log.d(TAG, "layoutMarginLeft: " + layoutMarginLeft);
        int layoutMarginLeftInPixel = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, layoutMarginLeft, getResources()
                        .getDisplayMetrics());


        ImageView image = findViewById(R.id.toolbar_title);
        TextView tvToolBarTitle = findViewById(R.id.tvToolBarTitle);
        LinearLayout.LayoutParams marginParams = (LinearLayout.LayoutParams) image.getLayoutParams();
        marginParams.setMargins(layoutMarginLeftInPixel, 0, 0, 0);
        image.setLayoutParams(marginParams);
        tvToolBarTitle.setLayoutParams(marginParams);
        tvToolBarTitle.setText(SDKConfiguration.Client.bot_name);
    }

    @Override
    protected void onDestroy() {
//        NotificationUtils.createAppNotification();
        super.onDestroy();
        BotApplication.setCurrentActivity(null);
    }

    @Override
    protected void onPause() {
//        NotificationUtils.createAppNotification();
        super.onPause();
        BotApplication.setCurrentActivity(null);
    }

    @Override
    protected void onResume() {
//        NotificationUtils.createAppNotification();
        super.onResume();
        BotApplication.setCurrentActivity(this);
    }
}
