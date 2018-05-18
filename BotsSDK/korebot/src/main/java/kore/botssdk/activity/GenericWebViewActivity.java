package kore.botssdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kore.botssdk.R;


public class GenericWebViewActivity extends BotAppCompactActivity {

    String actionbarTitle;
    String url;
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_webview_layout);
        Bundle receivedBundle = getIntent().getExtras();
        url = receivedBundle.getString("url");
        actionbarTitle = receivedBundle.getString("header");
        setUpActionBar();
        webview = (WebView) findViewById(R.id.webView);
        loadUrl();
    }


    protected void loadUrl() {

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");

        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(this.getClass().getSimpleName(), "The URL is " + url);
                boolean value = super.shouldOverrideUrlLoading(view, url);
                return value;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

