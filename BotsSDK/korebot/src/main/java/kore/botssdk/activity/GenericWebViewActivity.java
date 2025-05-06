package kore.botssdk.activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

import kore.botssdk.R;
import kore.botssdk.utils.StringUtils;

@SuppressLint({"SetJavaScriptEnabled", "UnKnownNullness"})
public class GenericWebViewActivity extends BotAppCompactActivity {
    private String actionbarTitle;
    private String url;
    private WebView webview;
    private ProgressBar mProgressBar;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
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
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        return false;
                    } else if (url.startsWith("intent://")) {
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            if (intent != null) {
                                startActivity(intent);
                                finish();
                                return true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        return true;
                    }
                    return false;
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
        webview.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            downloadFile(GenericWebViewActivity.this, url, contentDisposition, mimeType);
        });
    }

    private void downloadFile(Context context, String url, String contentDisposition, String mimeType) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        request.setTitle(fileName);
        request.setDescription("Downloading file...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);

        Toast.makeText(context, "Downloading file: \"" + fileName + "\". Please check in status bar!", Toast.LENGTH_LONG).show();
        new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(GenericWebViewActivity.this::finish, 1000);
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

