package kore.botssdk.audiocodes.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import kore.botssdk.utils.AsyncTasks;

public class OAuthManager {

    protected static final String TAG = "OAUTH";

    private boolean active;

    private EventsCallback eventsCallback;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editorSP;
    private static final String PREFS_KEY = "oauth_prefs";
    private static final String REFRESH_TOKEN_KEY = "refreshToken";
    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String URL_KEY = "url";
    private static final String REALM_KEY = "realm";
    private static final String CLINET_ID_KEY = "clientId";

    private static OAuthManager instance;

    public static synchronized OAuthManager getInstance() {
        if (instance == null) {
            instance = new OAuthManager();
        }
        return instance;
    }

    public boolean isEnabled() {
        return getValue(ACCESS_TOKEN_KEY) != null;
    }

    public void initialize(Context context, EventsCallback eventsCallback) {
        Log.d(TAG, "initialize");
        this.eventsCallback = eventsCallback;
        sharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        editorSP = sharedPreferences.edit();
        checkToken(context);
    }

    public void setClientId(String clientId) {
        putValue(CLINET_ID_KEY, clientId);
    }

    public void setURL(String url) {
        putValue(URL_KEY, url);
    }

    public void setRealm(String realm) {
        putValue(REALM_KEY, realm);
    }

    public void authorize(Context context, String userName, String password, LoginCallback loginCallback) {
        if (userName != null && password != null) {
            putValue(REFRESH_TOKEN_KEY, null);
            Log.d(TAG, "authorize");
            new AuthorizeTask(context, userName, password, loginCallback).executeAsync();
        } else {
            Log.d(TAG, "userName || password null");
        }
    }

    private class AuthorizeTask extends AsyncTasks<Void, Void, Void> {
        private final String userName;
        private final String password;
        private final LoginCallback loginCallback;
        private JSONObject jsonObject;
        private final Context context;

        public AuthorizeTask(Context context, String userName, String password, LoginCallback loginCallback) {
            this.context = context;
            this.userName = userName;
            this.password = password;
            this.loginCallback = loginCallback;
        }

        @Override
        protected void onPostExecute() {
            handleResults(context, jsonObject, loginCallback);
        }

        @Override
        protected void doInBackground(Void... arg0) {
            try {
                HttpURLConnection connection = HttpManager.getConnection(makeURL());
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String body = "grant_type=" + "password" +
                        "&username=" + userName +
                        "&password=" + password +
                        "&client_id=" + getValue(CLINET_ID_KEY);
                HttpManager.setBody(connection, body);
                jsonObject = HttpManager.make(connection);
            } catch (Throwable e) {
                Log.e(TAG, "AuthorizeTask", e);
            }
        }
    }

    private class RefreshTokenTask extends AsyncTasks<Void, Void, Void> {
        private JSONObject jsonObject;
        private final Context context;

        public RefreshTokenTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute() {
            handleResults(context, jsonObject, null);
        }

        @Override
        protected void doInBackground(Void... arg0) {
            try {
                HttpURLConnection connection = HttpManager.getConnection(makeURL());
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String body = "grant_type=" + "refresh_token" + "&refresh_token=" + getValue(REFRESH_TOKEN_KEY) +
                        "&client_id=" + getValue(CLINET_ID_KEY);
                HttpManager.setBody(connection, body);
                jsonObject = HttpManager.make(connection);
            } catch (Throwable e) {
                Log.e(TAG, "RefreshTokenTask", e);
            }
        }
    }

    private String makeURL() {
        String server = getValue(URL_KEY);
        String realm = getValue(REALM_KEY);
        return server + "realms/" + realm + "/protocol/openid-connect/token";
    }

    private void handleResults(Context context, JSONObject jsonObject, LoginCallback loginCallback) {
        try {
            if (jsonObject != null && jsonObject.has("access_token")) {
                Log.d(TAG, "success!");
                String access_token = jsonObject.getString("access_token");
                String expires_in = jsonObject.getString("expires_in");
                String refresh_token = jsonObject.getString("refresh_token");
                putValue(ACCESS_TOKEN_KEY, access_token);
                putValue(REFRESH_TOKEN_KEY, refresh_token);
                long tokenExpires = Long.parseLong(expires_in);
                setRefreshTokenAlarm(context, tokenExpires);
                active = true;
                eventsCallback.onUpdateToken(access_token);
                if (loginCallback != null) {
                    loginCallback.onAuthorize(true);
                }
            } else {
                Log.d(TAG, "fail!");
                if (loginCallback != null) {
                    loginCallback.onAuthorize(false);
                } else {
                    putValue(REFRESH_TOKEN_KEY, null);
                    eventsCallback.onRelogin();
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error", e);
            if (loginCallback != null) {
                loginCallback.onAuthorize(false);
            }
        }
    }

    protected void refreshToke(Context context) {
        Log.d(TAG, "refreshToke");
        new RefreshTokenTask(context).executeAsync();
    }

    private void checkToken(Context context) {
        if (isEnabled()) {
            // eventsCallback.onUpdateToken(getValue(ACCESS_TOKEN_KEY));
            Log.d(TAG, "token available");
            if (!active) {
                refreshToke(context);
            }
        } else {
            Log.d(TAG, "no token available");
        }
    }

    protected void setRefreshTokenAlarm(Context context, long refreshTokenExpries) {
        long time = (long) (refreshTokenExpries * 0.9);
        OAuthReceiver.setRefreshTokenAlarm(context, time);
    }

    protected void setAccessTokenAlarm(Context context, long accessTokenExpries) {
        long time = (long) (accessTokenExpries * 0.9);
        OAuthReceiver.setRefreshTokenAlarm(context, time);
    }

    private String getValue(String key) {
        key = key.toLowerCase();
        String value = sharedPreferences.getString(key, null);
        return value;
    }

    private void putValue(String key, String value) {
        key = key.toLowerCase();
        editorSP.putString(key, value);
        editorSP.commit();
    }

    public interface LoginCallback {
        public void onAuthorize(boolean success);
    }

    public interface EventsCallback {
        public void onRelogin();

        public void onUpdateToken(String token);
    }

    public String getURL() {
        return getValue(URL_KEY);
    }

    public String getRealm() {
        return getValue(REALM_KEY);
    }

    public String getClientId() {
        return getValue(CLINET_ID_KEY);
    }
}
