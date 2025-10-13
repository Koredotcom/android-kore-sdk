package kore.botssdk.activity;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import kore.botssdk.R;
import kore.botssdk.audiocodes.webrtcclient.db.MySQLiteHelper;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.utils.BundleConstants;

public class BotAppCompactActivity extends AppCompatActivity {
    private MySQLiteHelper dataBase;
    protected final String LOG_TAG = getClass().getSimpleName();
    private FrameLayout contentContainer;
    private View statusBarLayout;
    private SharedPreferences sharedPreferences;

    public void finish() {
        super.finish();
    }

    protected void onCreate(Bundle data) {
        super.onCreate(data);
        setContentView(R.layout.activity_base);

        contentContainer = findViewById(R.id.content_container);
        statusBarLayout = findViewById(R.id.status_bar_bg);
        sharedPreferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base_frame), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, 0, insets.right, insets.bottom);
            if (sharedPreferences.getInt(BundleConstants.STATUS_BAR_HEIGHT, 0) == 0)
                sharedPreferences.edit().putInt(BundleConstants.STATUS_BAR_HEIGHT, insets.top).apply();
            return WindowInsetsCompat.CONSUMED;
        });

        initDataBase();
    }

    // Method for child activities to set their layout inside the base layout
    protected void setContentLayout(@LayoutRes int layoutResId) {
        LayoutInflater.from(this).inflate(layoutResId, contentContainer, true);
    }

    protected void changeStatusBarColor(String color) {
        if (SDKConfig.isUpdateStatusBarColor()) {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= 35) {
                statusBarLayout.setVisibility(VISIBLE);
                ViewGroup.LayoutParams params = statusBarLayout.getLayoutParams();
                params.height = sharedPreferences.getInt(BundleConstants.STATUS_BAR_HEIGHT, 0);
                statusBarLayout.setLayoutParams(params);

                if (color.isBlank())
                    statusBarLayout.setBackgroundColor(ContextCompat.getColor(BotAppCompactActivity.this, R.color.primary));
                else
                    statusBarLayout.setBackgroundColor(Color.parseColor(color));
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(color));
            }
        }
        else changeStatusBarColorWithHeight();
    }

    protected void changeStatusBarColorWithHeight() {
            if (Build.VERSION.SDK_INT >= 35) {
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.base_frame), (view, windowInsets) -> {
                    Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
                    return WindowInsetsCompat.CONSUMED;
                });
        }
    }
    public void initDataBase() {
        dataBase = new MySQLiteHelper(this);
    }

    public MySQLiteHelper getDataBase() {
        return dataBase;
    }
}
