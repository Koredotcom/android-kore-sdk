package kore.botssdk.activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import kore.botssdk.R;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.utils.StringUtils;

@SuppressLint({"SetJavaScriptEnabled", "UnKnownNullness"})
public class GenericWebViewActivity extends BotAppCompactActivity {
    private String actionbarTitle;
    private String url;
    private WebView webview;
    private TextView tvPleaseWait;
    private ProgressBar mProgressBar;
    private final Handler handler = new Handler(Looper.getMainLooper());
    public static String EXTRA_URL = "url";
    public static String EXTRA_HEADER = "header";

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_webview_layout);
        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            url = receivedBundle.getString(EXTRA_URL);
            actionbarTitle = receivedBundle.getString(EXTRA_HEADER);
        }

        setUpActionBar();
        webview = findViewById(R.id.webView);
        tvPleaseWait = findViewById(R.id.please_wait);
        mProgressBar = findViewById(R.id.mProgress);
        loadUrl();
        webview.setBackgroundColor(Color.WHITE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
        } else if (itemId == R.id.action_share) {
            onShare();
            return true;
        } else if (itemId == R.id.action_download) {
            onDownload();
            return true;
        } else if (itemId == R.id.action_copy_link) {
            onCopyLink();
            return true;
        }

        return true;
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
                    tvPleaseWait.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    tvPleaseWait.setVisibility(View.GONE);
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
        webview.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> downloadFile(url, contentDisposition, mimeType, false));
    }

    private void downloadFile(String url, String contentDisposition, String mimeType, boolean isFromMenus) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        request.setTitle(fileName);
        request.setDescription(getString(R.string.downloading_file));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);

        Toast.makeText(this, getString(R.string.downloading_file_name, fileName), Toast.LENGTH_LONG).show();
        if (!isFromMenus) {
            handler.postDelayed(GenericWebViewActivity.this::finish, 1000);
        }
    }

    private void onDownload() {
        Toast.makeText(this, getString(R.string.downloading), Toast.LENGTH_LONG).show();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                String mimeType = connection.getContentType();
                String contentDisposition = connection.getHeaderField("Content-Disposition");
                connection.disconnect();

                handler.post(() -> downloadFile(this.url, contentDisposition, mimeType, true));
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> Toast.makeText(this, getString(R.string.downloading_failed), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void onShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, "Share URL via");
        startActivity(chooser);
    }

    private void onCopyLink() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", url);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (SDKConfig.isIsShowActionBar()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_black_24dp, getTheme()));
                actionBar.setTitle(actionbarTitle);
            } else {
                actionBar.setTitle("");
            }
        }
    }
}

