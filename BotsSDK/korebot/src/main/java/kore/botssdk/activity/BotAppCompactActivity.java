package kore.botssdk.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import kore.botssdk.R;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.ToastUtils;

@SuppressLint("UnknownNullness")
public class BotAppCompactActivity extends AppCompatActivity {

    protected final String LOG_TAG = getClass().getSimpleName();
    private ProgressDialog mProgressDialog;
    private FrameLayout contentContainer;
    private View status_bar_bg;
    private SharedPreferences sharedPreferences;

    public void finish() {
        super.finish();
    }

    protected void onCreate(Bundle data) {
        super.onCreate(data);
        setContentView(R.layout.activity_base);
        contentContainer = findViewById(R.id.content_container);
        status_bar_bg = findViewById(R.id.status_bar_bg);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base_frame), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, 0, insets.right, insets.bottom);
            if(sharedPreferences.getInt(BundleConstants.STATUS_BAR_HEIGHT, 0) == 0) sharedPreferences.edit().putInt(BundleConstants.STATUS_BAR_HEIGHT, insets.top).apply();
            return WindowInsetsCompat.CONSUMED;
        });

    }


    // Method for child activities to set their layout inside the base layout
    protected void setContentLayout(@LayoutRes int layoutResId) {
        LayoutInflater.from(this).inflate(layoutResId, contentContainer, true);
    }

    protected void changeStatusBarColor(String color) {
        if (SDKConfig.isUpdateStatusBarColor()) {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= 35) {
                status_bar_bg.setVisibility(VISIBLE);
                ViewGroup.LayoutParams params = status_bar_bg.getLayoutParams();
                params.height = sharedPreferences.getInt(BundleConstants.STATUS_BAR_HEIGHT, 0);
                status_bar_bg.setLayoutParams(params);

                if(color.isBlank())
                    status_bar_bg.setBackgroundColor(ContextCompat.getColor(BotAppCompactActivity.this, R.color.primary));
                else
                    status_bar_bg.setBackgroundColor(Color.parseColor(color));
            }
            else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(color));
            }
        }
    }

    // Optional: get container reference
    protected FrameLayout getContentContainer() {
        return contentContainer;
    }

    protected void showProgress(String msg, boolean isCancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setContentView(R.layout.progress_indicator);
        ((TextView) mProgressDialog.findViewById(R.id.loadingText)).setText(TextUtils.isEmpty(msg) ? "please wait" : msg);
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    protected final void showToast(String message) {
        if (message != null && !message.equals("INVALID_ACCESS_TOKEN"))
            ToastUtils.showToast(this, message);
    }

    protected final void showToast(String msg, int length) {
        ToastUtils.showToast(this, msg, length);
    }

}
