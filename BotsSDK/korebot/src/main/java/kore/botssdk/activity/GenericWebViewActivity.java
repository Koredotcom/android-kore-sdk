package kore.botssdk.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.BuildConfig;
import kore.botssdk.R;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;

@SuppressLint("SetJavaScriptEnabled")
public class GenericWebViewActivity extends BotAppCompactActivity {

    String actionbarTitle;
    String url;
    WebView webview;
    ProgressBar mProgressBar;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("finish_activity")) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_webview_layout);
        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            url = receivedBundle.getString("url");
            actionbarTitle = receivedBundle.getString("header");
        }

        setUpActionBar();
        webview = findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.mProgress);
        loadUrl();

        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
    }


    protected void loadUrl() {
        if (!StringUtils.isNullOrEmpty(url)) {
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.setWebViewClient(new WebViewClient() {
                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    LogUtils.e("Loaded URL", url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    LogUtils.e("WebResourceRequest URL", request.getUrl().getHost());
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    webview.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    webview.setVisibility(View.VISIBLE);
                }
            });
            webview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onCloseWindow(WebView window) {
                    super.onCloseWindow(window);
                    GenericWebViewActivity.this.finish();
                }

                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    if (BuildConfig.DEBUG) {
                        Log.d(LOG_TAG, consoleMessage.message() + " -- From line "
                                + consoleMessage.lineNumber() + " of "
                                + consoleMessage.sourceId());
                    }

                    return super.onConsoleMessage(consoleMessage);
                }

            });

            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

            webview.loadUrl(url);
        }
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_black_24dp, getTheme()));
            actionBar.setTitle(actionbarTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
        }

        return true;
    }
}

