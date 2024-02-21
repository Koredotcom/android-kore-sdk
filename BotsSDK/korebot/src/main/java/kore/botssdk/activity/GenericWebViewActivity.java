package kore.botssdk.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.R;
import kore.botssdk.utils.StringUtils;

@SuppressLint({"SetJavaScriptEnabled", "UnKnownNullness"})
public class GenericWebViewActivity extends BotAppCompactActivity {

    String actionbarTitle;
    String url;
    WebView webview;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_webview_layout);
        Bundle receivedBundle = getIntent().getExtras();
        if(receivedBundle != null)
        {
            url = receivedBundle.getString("url");
            actionbarTitle = receivedBundle.getString("header");
        }

        setUpActionBar();
        webview = findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.mProgress);
        loadUrl();

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


    protected void loadUrl()
    {
        if(!StringUtils.isNullOrEmpty(url))
        {
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
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
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
                    return super.onConsoleMessage(consoleMessage);
                }

            });

            webview.loadUrl(url);
        }
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled (true);
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

