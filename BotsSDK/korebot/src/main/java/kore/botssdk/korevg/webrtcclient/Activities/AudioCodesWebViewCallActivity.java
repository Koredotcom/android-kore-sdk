package kore.botssdk.korevg.webrtcclient.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kore.botssdk.BuildConfig;
import kore.botssdk.bot.BotClient;
import kore.botssdk.korevg.webrtcclient.General.ACManager;
import kore.botssdk.korevg.webrtcclient.General.AppUtils;
import kore.botssdk.korevg.webrtcclient.General.ClickToCallManager;
import kore.botssdk.models.EventIceServersModel;
import kore.botssdk.models.EventMessageModel;
import kore.botssdk.models.EventModel;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;

/**
 * Debug-only alternate agent-call engine. The default KoreVG/PJSIP path remains unchanged.
 */
public class AudioCodesWebViewCallActivity extends Activity {
    private static final String TAG = "ACWebViewCall";
    private static final String EXTRA_CONFIG = "kore.botssdk.ac_webview_call_config";
    private static final String ASSET_ORIGIN = "https://appassets.androidplatform.net";
    private static final int REQUEST_MEDIA_PERMISSIONS = 8127;
    private static WeakReference<AudioCodesWebViewCallActivity> activeCall = new WeakReference<>(null);

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private WebView webView;
    private WebViewAssetLoader assetLoader;
    private String callConfigJson;
    private boolean videoCall;
    private boolean clickToCall;
    private boolean callScriptStarted;
    private boolean callConnected;
    private boolean finishingCall;
    private boolean terminationSent;
    private int previousAudioMode = AudioManager.MODE_NORMAL;
    private boolean previousSpeakerphoneOn;
    private AudioManager audioManager;

    /** Starts the alternate call engine only when explicitly enabled by the host app. */
    public static boolean launchIfEnabled(Activity host, String sipTarget, boolean videoCall) {
        if (!SDKConfiguration.OverrideKoreConfig.useAudioCodesWebViewForAgentCalls) {
            return false;
        }

        EventModel eventModel = AppUtils.getEventModel();
        BotClient botClient = AppUtils.getBotClient();
        if (eventModel == null || eventModel.getMessage() == null || botClient == null) return false;

        try {
            JSONObject input = makeCallConfig(eventModel.getMessage(), sipTarget, botClient.getUserId(), videoCall);
            Intent intent = new Intent(host, AudioCodesWebViewCallActivity.class);
            intent.putExtra(EXTRA_CONFIG, input.toString());
            host.startActivity(intent);
            ACManager.getInstance().clearPendingAgentSipAccount();
            return true;
        } catch (JSONException | IllegalArgumentException exception) {
            Toast.makeText(host, "Unable to prepare the WebView call: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            return true;
        }
    }

    /** Starts an outbound click-to-call using the same bundled AudioCodes WebView engine. */
    public static boolean launchClickToCall(Activity host) {
        try {
            JSONObject input = ClickToCallManager.makeCallConfig(host);
            Intent intent = new Intent(host, AudioCodesWebViewCallActivity.class);
            intent.putExtra(EXTRA_CONFIG, input.toString());
            ClickToCallManager.sendStartEvent();
            host.startActivity(intent);
            return true;
        } catch (JSONException | IllegalStateException exception) {
            Toast.makeText(host, "Unable to prepare click-to-call: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /** Called when the bot sends terminate_agent_webrtc while this path is active. */
    public static void terminateActiveCall() {
        AudioCodesWebViewCallActivity activity = activeCall.get();
        if (activity != null) activity.mainHandler.post(() -> activity.finishCall(false));
    }

    private static JSONObject makeCallConfig(EventMessageModel message, String sipTarget,
                                             String sipIdentity, boolean videoCall) throws JSONException {
        if (message.getAddresses() == null || message.getAddresses().isEmpty()) {
            throw new IllegalArgumentException("The call event has no signaling addresses");
        }
        if (message.getDomain() == null || message.getDomain().trim().isEmpty()) {
            throw new IllegalArgumentException("The call event has no SIP domain");
        }
        if (sipIdentity == null || sipIdentity.trim().isEmpty()) {
            throw new IllegalArgumentException("The SIP identity is empty");
        }

        JSONObject input = new JSONObject();
        input.put("addresses", new JSONArray(message.getAddresses()));
        input.put("domain", message.getDomain());
        input.put("iceServers", toIceServerJson(message.getIceServers()));
        input.put("sipIdentity", sipIdentity);
        input.put("target", normalizeTarget(sipTarget, message.getSipURI()));
        input.put("callType", videoCall ? "video" : "audio");
        input.put("agentName", message.getFirstName() == null ? "Agent" : message.getFirstName());
        input.put("conversationId", message.getConversationId() == null ? "" : message.getConversationId());

        JSONArray headers = new JSONArray();
        if (videoCall) {
            if (message.getaId() != null) headers.put("X-botName:" + message.getaId());
            if (message.getConversationId() != null) headers.put("X-cId:" + message.getConversationId());
            headers.put("X-isVideoCall:true");
        }
        input.put("headers", headers);
        return input;
    }

    private static JSONArray toIceServerJson(List<EventIceServersModel> source) {
        JSONArray result = new JSONArray();
        if (source == null) return result;
        for (EventIceServersModel entry : source) {
            if (entry == null || entry.getIceUrls().isEmpty()) continue;
            JSONObject server = new JSONObject();
            try {
                server.put("urls", new JSONArray(entry.getIceUrls()));
                if (entry.getUsername() != null) server.put("username", entry.getUsername());
                if (entry.getCredential() != null) server.put("credential", entry.getCredential());
                result.put(server);
            } catch (JSONException ignored) {
                // Skip malformed ICE entries and let WebRTC use the remaining servers.
            }
        }
        return result;
    }

    private static String normalizeTarget(String target, String sipUri) {
        String value = target == null ? "" : target.trim();
        if (value.isEmpty()) value = sipUri == null ? "" : sipUri.trim();
        String lower = value.toLowerCase();
        if (lower.startsWith("sips:")) value = value.substring(5);
        else if (lower.startsWith("sip:")) value = value.substring(4);
        int at = value.indexOf('@');
        if (at >= 0) value = value.substring(0, at);
        if (value.startsWith("<")) value = value.substring(1);
        if (value.endsWith(">")) value = value.substring(0, value.length() - 1);
        if (value.trim().isEmpty()) throw new IllegalArgumentException("The call event has no SIP target");
        return value.trim();
    }

    private static boolean isAssetOrigin(Uri uri) {
        return uri != null
                && "https".equalsIgnoreCase(uri.getScheme())
                && "appassets.androidplatform.net".equalsIgnoreCase(uri.getHost())
                && uri.getPort() == -1
                && uri.getUserInfo() == null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        callConfigJson = getIntent().getStringExtra(EXTRA_CONFIG);
        if (callConfigJson == null || callConfigJson.isEmpty()) {
            finish();
            return;
        }
        try {
            JSONObject config = new JSONObject(callConfigJson);
            videoCall = "video".equals(config.optString("callType"));
            clickToCall = config.optBoolean("isClickToCall", false);
        } catch (JSONException exception) {
            finish();
            return;
        }
        activeCall = new WeakReference<>(this);
        requestMediaPermissionsOrLoad();
    }

    private void requestMediaPermissionsOrLoad() {
        ArrayList<String> missing = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            missing.add(Manifest.permission.RECORD_AUDIO);
        }
        if (videoCall && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            missing.add(Manifest.permission.CAMERA);
        }
        if (!missing.isEmpty()) {
            requestPermissions(missing.toArray(new String[0]), REQUEST_MEDIA_PERMISSIONS);
            return;
        }
        initializeWebView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_MEDIA_PERMISSIONS) return;
        boolean granted = true;
        for (int result : grantResults) granted &= result == PackageManager.PERMISSION_GRANTED;
        if (granted) initializeWebView();
        else {
            Toast.makeText(this, "Microphone and camera permission are required for this call", Toast.LENGTH_LONG).show();
            notifyCallTerminated();
            finish();
        }
    }

    private void initializeWebView() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            previousAudioMode = audioManager.getMode();
            previousSpeakerphoneOn = audioManager.isSpeakerphoneOn();
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setSpeakerphoneOn(true);
        }

        assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        webView = new WebView(this);
        webView.setBackgroundColor(android.graphics.Color.rgb(16, 24, 40));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new AndroidCallBridge(), "AndroidCallBridge");
        webView.setWebViewClient(new WebViewClientCompat() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !isAssetOrigin(request.getUrl());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel();
                finishCall(true);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> {
                    if (!isAssetOrigin(request.getOrigin())) {
                        request.deny();
                        return;
                    }
                    ArrayList<String> allowed = new ArrayList<>();
                    for (String resource : request.getResources()) {
                        if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(resource)
                                && checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            allowed.add(resource);
                        } else if (PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource)
                                && videoCall
                                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            allowed.add(resource);
                        }
                    }
                    if (allowed.size() == request.getResources().length && !allowed.isEmpty()) {
                        request.grant(allowed.toArray(new String[0]));
                    } else {
                        request.deny();
                    }
                });
            }
        });
        setContentView(webView);
        if (BuildConfig.DEBUG) WebView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl(ASSET_ORIGIN + "/assets/ac_webrtc_call.html");
    }

    private void startCallInWebView() {
        if (callScriptStarted || finishingCall || webView == null) return;
        callScriptStarted = true;
        String quotedJson = JSONObject.quote(callConfigJson);
        webView.evaluateJavascript("window.startAgentCall(JSON.parse(" + quotedJson + "));", value -> {
            if (value != null && value.startsWith("\"ERROR:")) {
                Toast.makeText(this, "The WebRTC page failed to start", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onJavascriptEvent(String type, String detail) {
        if (finishingCall) return;
        switch (type) {
            case "ready":
                startCallInWebView();
                break;
            case "connected":
                callConnected = true;
                break;
            case "endCall":
                finishCall(true);
                break;
            case "terminated":
                finishCall(true);
                break;
            case "error":
                Toast.makeText(this, detail == null || detail.isEmpty() ? "The WebRTC call failed" : detail,
                        Toast.LENGTH_LONG).show();
                finishCall(true);
                break;
            default:
                break;
        }
    }

    private void finishCall(boolean notifyBackend) {
        if (finishingCall) return;
        finishingCall = true;
        if (notifyBackend) notifyCallTerminated();
        if (webView == null) {
            finish();
            return;
        }
        webView.evaluateJavascript("window.stopAgentCall && window.stopAgentCall();", ignored -> finish());
        mainHandler.postDelayed(() -> {
            if (!isFinishing()) finish();
        }, 1000);
    }

    private void notifyCallTerminated() {
        if (clickToCall) return;
        if (terminationSent) return;
        terminationSent = true;
        EventModel eventModel = AppUtils.getEventModel();
        BotClient botClient = AppUtils.getBotClient();
        if (eventModel != null && eventModel.getMessage() != null && botClient != null) {
            eventModel.getMessage().setType(BundleConstants.CALL_AGENT_WEBRTC_TERMINATED);
            botClient.sendMessage(eventModel.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        finishCall(true);
    }

    @Override
    protected void onDestroy() {
        if (!finishingCall && callConnected) notifyCallTerminated();
        if (webView != null) {
            webView.removeJavascriptInterface("AndroidCallBridge");
            webView.stopLoading();
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(previousSpeakerphoneOn);
            audioManager.setMode(previousAudioMode);
        }
        if (activeCall.get() == this) activeCall.clear();
        super.onDestroy();
    }

    private final class AndroidCallBridge {
        @JavascriptInterface
        public void onEvent(String type, String detail) {
            mainHandler.post(() -> onJavascriptEvent(type, detail));
        }
    }
}
