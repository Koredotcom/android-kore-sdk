package com.kore.ai.widgetsdk.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.kora.ai.widgetsdk.R;


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
        url = receivedBundle.getString("url");
        actionbarTitle = receivedBundle.getString("header");
        setUpActionBar();
        webview = (WebView) findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.mProgress);
        loadUrl();
    }


    protected void loadUrl() {

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
       webview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(this.getClass().getSimpleName(), "The URL is " + url);
                return super.shouldOverrideUrlLoading(view, url);
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
                Log.d(LOG_TAG, consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
            /*@Override
            public void onProgressChanged(WebView view, int newProgress) {
                setTitle("Loading... " +newProgress +"%");
//                KaGenericWebViewActivity.this.setProgress(newProgress * 100);

                if(newProgress == 100)
                    setTitle(R.string.app_name);
            }*/

        });

        webview.loadUrl(url);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void NativeBridgeUp(String responseData) {

            Log.d(this.getClass().getSimpleName(), "The response data is " + responseData);
        }

        @JavascriptInterface
        public void NativeBridgeDown() {
        }
    }

    @Override
    public void onBackPressed() {
        //Go back to Login screen
        //Push right animation used
        super.onBackPressed();
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled (true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        actionBar.setTitle(actionbarTitle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}

